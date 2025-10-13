package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.repository.UserProfileRepository;
import com.nextjstemplate.service.ClerkAuthService;
import com.nextjstemplate.service.ClerkIntegrationService;
import com.nextjstemplate.service.UserProfileService;
import com.nextjstemplate.service.dto.*;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of ClerkAuthService for handling authentication operations.
 */
@Service
@Transactional
public class ClerkAuthServiceImpl implements ClerkAuthService {

    private static final Logger log = LoggerFactory.getLogger(ClerkAuthServiceImpl.class);

    private final ClerkIntegrationService clerkIntegrationService;
    private final UserProfileService userProfileService;
    private final UserProfileRepository userProfileRepository;
    private final JwtEncoder jwtEncoder;

    @Value("${jhipster.security.authentication.jwt.token-validity-in-seconds:86400}")
    private Long tokenValidityInSeconds;

    public ClerkAuthServiceImpl(
        ClerkIntegrationService clerkIntegrationService,
        UserProfileService userProfileService,
        UserProfileRepository userProfileRepository,
        JwtEncoder jwtEncoder
    ) {
        this.clerkIntegrationService = clerkIntegrationService;
        this.userProfileService = userProfileService;
        this.userProfileRepository = userProfileRepository;
        this.jwtEncoder = jwtEncoder;
    }

    @Override
    public SignUpResponse signUp(SignUpRequest request) {
        log.info("Processing sign-up request for email: {} and tenant: {}", request.getEmail(), request.getTenantId());

        // Check if user already exists with this email and tenant combination
        Optional<UserProfile> existingUser = userProfileRepository.findByEmailAndTenantId(request.getEmail(), request.getTenantId());
        if (existingUser.isPresent()) {
            throw new RuntimeException("User already exists with email " + request.getEmail() + " for tenant " + request.getTenantId());
        }

        try {
            // Create user in Clerk
            Map<String, Object> clerkUser = clerkIntegrationService.createUser(
                request.getEmail(),
                request.getPassword(),
                request.getFirstName(),
                request.getLastName(),
                request.getMetadata()
            );

            String clerkUserId = (String) clerkUser.get("id");
            log.info("User created in Clerk with ID: {}", clerkUserId);

            // Create user profile in database
            UserProfileDTO userProfileDTO = new UserProfileDTO();
            userProfileDTO.setUserId(clerkUserId);
            userProfileDTO.setEmail(request.getEmail());
            userProfileDTO.setFirstName(request.getFirstName());
            userProfileDTO.setLastName(request.getLastName());
            userProfileDTO.setTenantId(request.getTenantId());
            userProfileDTO.setPhone(request.getPhone());
            userProfileDTO.setUserStatus("ACTIVE");
            userProfileDTO.setUserRole("USER");

            ZonedDateTime now = ZonedDateTime.now();
            userProfileDTO.setCreatedAt(now);
            userProfileDTO.setUpdatedAt(now);

            UserProfileDTO savedProfile = userProfileService.save(userProfileDTO);
            log.info("User profile created in database with ID: {}", savedProfile.getId());

            // Generate JWT token
            String jwtToken = generateJwtToken(clerkUserId, request.getEmail(), request.getTenantId());

            // Prepare response
            SignUpResponse response = new SignUpResponse();
            response.setId(savedProfile.getId());
            response.setClerkUserId(clerkUserId);
            response.setEmail(request.getEmail());
            response.setFirstName(request.getFirstName());
            response.setLastName(request.getLastName());
            response.setTenantId(request.getTenantId());
            response.setAccessToken(jwtToken);
            response.setTokenType("Bearer");
            response.setExpiresIn(tokenValidityInSeconds);
            response.setCreatedAt(now);
            response.setMessage("User registered successfully");

            log.info("Sign-up completed successfully for user: {}", clerkUserId);
            return response;
        } catch (Exception e) {
            log.error("Error during sign-up process for email: {}", request.getEmail(), e);
            throw new RuntimeException("Sign-up failed: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<SignInResponse> signIn(SignInRequest request) {
        log.info("Processing sign-in request for email: {} and tenant: {}", request.getEmail(), request.getTenantId());

        try {
            // Find user in database by email and tenant
            UserProfile userProfile = userProfileRepository
                .findByEmailAndTenantId(request.getEmail(), request.getTenantId())
                .orElseThrow(() -> {
                    log.warn("User not found for email: {} and tenant: {}", request.getEmail(), request.getTenantId());
                    return new RuntimeException("User not found");
                });

            // Verify user with Clerk
            clerkIntegrationService
                .getUserById(userProfile.getUserId())
                .orElseThrow(() -> {
                    log.warn("User not found in Clerk: {}", userProfile.getUserId());
                    return new RuntimeException("User not found in Clerk");
                });

            // Generate JWT token
            String jwtToken = generateJwtToken(userProfile.getUserId(), userProfile.getEmail(), userProfile.getTenantId());

            // Update last sign-in timestamp
            ZonedDateTime now = ZonedDateTime.now();
            userProfile.setUpdatedAt(now);
            userProfileRepository.save(userProfile);

            // Prepare response
            SignInResponse response = new SignInResponse();
            response.setId(userProfile.getId());
            response.setClerkUserId(userProfile.getUserId());
            response.setEmail(userProfile.getEmail());
            response.setFirstName(userProfile.getFirstName());
            response.setLastName(userProfile.getLastName());
            response.setTenantId(userProfile.getTenantId());
            response.setAccessToken(jwtToken);
            response.setTokenType("Bearer");
            response.setExpiresIn(tokenValidityInSeconds);
            response.setLastSignInAt(now);
            response.setMessage("User authenticated successfully");

            log.info("Sign-in completed successfully for user: {}", userProfile.getUserId());
            return Optional.of(response);
        } catch (Exception e) {
            log.error("Error during sign-in process for email: {}", request.getEmail(), e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<TokenValidationResponse> validateToken(TokenValidationRequest request) {
        log.debug("Validating token");

        try {
            // Verify JWT token with Clerk
            Map<String, Object> claims = clerkIntegrationService
                .verifyJwtToken(request.getToken())
                .orElseThrow(() -> {
                    log.debug("Token validation failed");
                    return new RuntimeException("Invalid or expired token");
                });

            String clerkUserId = (String) claims.get("sub");

            // Load user from database
            UserProfile userProfile = userProfileRepository
                .findByUserId(clerkUserId)
                .orElseThrow(() -> {
                    log.warn("User not found in database for Clerk ID: {}", clerkUserId);
                    return new RuntimeException("User not found");
                });

            // Get user organizations
            List<Map<String, Object>> organizations = clerkIntegrationService.getUserOrganizations(clerkUserId);

            // Prepare response
            TokenValidationResponse response = new TokenValidationResponse();
            response.setValid(true);
            response.setId(userProfile.getId());
            response.setClerkUserId(userProfile.getUserId());
            response.setEmail(userProfile.getEmail());
            response.setFirstName(userProfile.getFirstName());
            response.setLastName(userProfile.getLastName());
            response.setTenantId(userProfile.getTenantId());
            response.setRoles(Arrays.asList(userProfile.getUserRole()));
            response.setOrganizations(organizations);
            response.setClaims(claims);

            log.debug("Token validated successfully for user: {}", clerkUserId);
            return Optional.of(response);
        } catch (Exception e) {
            log.error("Error validating token", e);
            TokenValidationResponse response = new TokenValidationResponse();
            response.setValid(false);
            response.setError("Token validation failed: " + e.getMessage());
            return Optional.of(response);
        }
    }

    @Override
    public Optional<TokenValidationResponse> getUserByClerkId(String clerkUserId) {
        log.debug("Fetching user by Clerk ID: {}", clerkUserId);

        try {
            UserProfile userProfile = userProfileRepository
                .findByUserId(clerkUserId)
                .orElseThrow(() -> {
                    log.warn("User not found in database for Clerk ID: {}", clerkUserId);
                    return new RuntimeException("User not found");
                });

            // Get user organizations
            List<Map<String, Object>> organizations = clerkIntegrationService.getUserOrganizations(clerkUserId);

            // Prepare response
            TokenValidationResponse response = new TokenValidationResponse();
            response.setValid(true);
            response.setId(userProfile.getId());
            response.setClerkUserId(userProfile.getUserId());
            response.setEmail(userProfile.getEmail());
            response.setFirstName(userProfile.getFirstName());
            response.setLastName(userProfile.getLastName());
            response.setTenantId(userProfile.getTenantId());
            response.setRoles(Arrays.asList(userProfile.getUserRole()));
            response.setOrganizations(organizations);

            log.debug("User fetched successfully: {}", clerkUserId);
            return Optional.of(response);
        } catch (Exception e) {
            log.error("Error fetching user by Clerk ID", e);
            return Optional.empty();
        }
    }

    @Override
    public String generateJwtToken(String clerkUserId, String email, String tenantId) {
        log.debug("Generating JWT token for user: {}", clerkUserId);

        Instant now = Instant.now();
        Instant expiresAt = now.plus(tokenValidityInSeconds, ChronoUnit.SECONDS);

        JwtClaimsSet claims = JwtClaimsSet
            .builder()
            .issuer("nextjs-template-boot")
            .issuedAt(now)
            .expiresAt(expiresAt)
            .subject(clerkUserId)
            .claim("email", email)
            .claim("tenant_id", tenantId)
            .claim("auth_provider", "clerk")
            .build();

        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();

        log.debug("JWT token generated successfully");
        return token;
    }

    @Override
    public Optional<RefreshTokenResponse> refreshToken(RefreshTokenRequest request) {
        log.debug("Processing token refresh request");

        try {
            // Validate the current token
            TokenValidationRequest validationRequest = new TokenValidationRequest();
            validationRequest.setToken(request.getRefreshToken());

            TokenValidationResponse userInfo = validateToken(validationRequest)
                .orElseThrow(() -> {
                    log.warn("Invalid refresh token provided");
                    return new RuntimeException("Invalid refresh token");
                });

            if (!userInfo.isValid()) {
                log.warn("Invalid refresh token provided");
                return Optional.empty();
            }

            // Generate a new token
            String newToken = generateJwtToken(userInfo.getClerkUserId(), userInfo.getEmail(), userInfo.getTenantId());

            // Prepare response
            RefreshTokenResponse response = new RefreshTokenResponse();
            response.setAccessToken(newToken);
            response.setTokenType("Bearer");
            response.setExpiresIn(tokenValidityInSeconds);
            response.setMessage("Token refreshed successfully");

            log.info("Token refreshed successfully for user: {}", userInfo.getClerkUserId());
            return Optional.of(response);
        } catch (Exception e) {
            log.error("Error refreshing token", e);
            return Optional.empty();
        }
    }

    @Override
    public SignOutResponse signOut(SignOutRequest request) {
        log.info("Processing sign-out request for session: {}", request.getSessionId());

        SignOutResponse response = new SignOutResponse();

        try {
            // Revoke session in Clerk
            clerkIntegrationService.revokeSession(request.getSessionId());

            // Clear Spring Security context
            org.springframework.security.core.context.SecurityContextHolder.clearContext();

            response.setSuccess(true);
            response.setMessage("User signed out successfully");

            log.info("Sign-out completed successfully for session: {}", request.getSessionId());
            return response;
        } catch (Exception e) {
            log.error("Error during sign-out for session: {}", request.getSessionId(), e);
            response.setSuccess(false);
            response.setMessage("Sign-out failed: " + e.getMessage());
            return response;
        }
    }

    @Override
    public Optional<SocialSignInResponse> socialSignIn(SocialSignInRequest request) {
        log.info("Processing social sign-in for tenant: {} with provider: {}", request.getTenantId(), request.getProvider());

        try {
            // Validate session token with Clerk
            Optional<Map<String, Object>> sessionData = clerkIntegrationService.validateSessionToken(request.getClerkSessionToken());

            if (sessionData.isEmpty()) {
                log.warn("Invalid Clerk session token");
                return Optional.empty();
            }

            // Extract user ID from session
            Map<String, Object> sessionDataMap = sessionData.orElseThrow(() -> {
                log.warn("Session data is empty");
                return new RuntimeException("Invalid session data");
            });
            String clerkUserId = (String) sessionDataMap.get("user_id");
            if (clerkUserId == null) {
                log.warn("No user ID found in session data");
                return Optional.empty();
            }

            // Get user details from Clerk
            Map<String, Object> clerkUser = clerkIntegrationService
                .getUserById(clerkUserId)
                .orElseThrow(() -> {
                    log.warn("User not found in Clerk: {}", clerkUserId);
                    return new RuntimeException("User not found in Clerk");
                });

            // Extract user information
            String email = extractEmail(clerkUser);
            String firstName = (String) clerkUser.getOrDefault("first_name", "");
            String lastName = (String) clerkUser.getOrDefault("last_name", "");
            String profileImageUrl = (String) clerkUser.get("profile_image_url");

            // Check if user exists in database
            Optional<UserProfile> userProfileOpt = userProfileRepository.findByEmailAndTenantId(email, request.getTenantId());

            boolean isNewUser = userProfileOpt.isEmpty();
            UserProfile userProfile;

            if (isNewUser) {
                // Create new user profile
                UserProfileDTO newUserProfile = new UserProfileDTO();
                newUserProfile.setUserId(clerkUserId);
                newUserProfile.setEmail(email);
                newUserProfile.setFirstName(firstName);
                newUserProfile.setLastName(lastName);
                newUserProfile.setTenantId(request.getTenantId());
                newUserProfile.setUserStatus("ACTIVE");
                newUserProfile.setUserRole("USER");

                ZonedDateTime now = ZonedDateTime.now();
                newUserProfile.setCreatedAt(now);
                newUserProfile.setUpdatedAt(now);

                UserProfileDTO savedProfile = userProfileService.save(newUserProfile);
                userProfile =
                    userProfileRepository
                        .findById(savedProfile.getId())
                        .orElseThrow(() -> new RuntimeException("Failed to retrieve saved user profile"));
                log.info("Created new user profile for social sign-in: {}", savedProfile.getId());
            } else {
                // Update existing user profile
                userProfile = userProfileOpt.orElseThrow(() -> new RuntimeException("User profile not found"));
                userProfile.setUserId(clerkUserId);
                if (profileImageUrl != null) {
                    userProfile.setProfileImageUrl(profileImageUrl);
                }
                userProfile.setUpdatedAt(ZonedDateTime.now());
                userProfile = userProfileRepository.save(userProfile);
                log.info("Updated existing user profile: {}", userProfile.getId());
            }

            // Generate JWT token
            String jwtToken = generateJwtToken(clerkUserId, email, request.getTenantId());

            // Prepare response
            SocialSignInResponse response = new SocialSignInResponse();
            response.setId(userProfile.getId());
            response.setClerkUserId(clerkUserId);
            response.setEmail(email);
            response.setFirstName(firstName);
            response.setLastName(lastName);
            response.setTenantId(request.getTenantId());
            response.setAuthProvider(request.getProvider() != null ? request.getProvider() : "social");
            response.setNewUser(isNewUser);
            response.setAccessToken(jwtToken);
            response.setTokenType("Bearer");
            response.setExpiresIn(tokenValidityInSeconds);
            response.setLastSignInAt(ZonedDateTime.now());
            response.setMessage(
                "User authenticated successfully with " + (request.getProvider() != null ? request.getProvider() : "social provider")
            );

            log.info("Social sign-in completed successfully for user: {}", clerkUserId);
            return Optional.of(response);
        } catch (Exception e) {
            log.error("Error during social sign-in", e);
            return Optional.empty();
        }
    }

    /**
     * Extract email from Clerk user data.
     */
    private String extractEmail(Map<String, Object> clerkUser) {
        // Try to get primary email
        Object emailAddressesObj = clerkUser.get("email_addresses");
        if (emailAddressesObj instanceof List) {
            List<?> emailAddresses = (List<?>) emailAddressesObj;
            if (!emailAddresses.isEmpty() && emailAddresses.get(0) instanceof Map) {
                Map<?, ?> primaryEmail = (Map<?, ?>) emailAddresses.get(0);
                Object emailAddress = primaryEmail.get("email_address");
                if (emailAddress != null) {
                    return emailAddress.toString();
                }
            }
        }

        // Fallback to direct email field
        Object email = clerkUser.get("email");
        return email != null ? email.toString() : "";
    }
}
