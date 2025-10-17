package com.nextjstemplate.web.rest;

import com.nextjstemplate.domain.ClerkUserTenant;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.repository.UserProfileRepository;
import com.nextjstemplate.service.ClerkAuthService;
import com.nextjstemplate.service.ClerkIntegrationService;
import com.nextjstemplate.service.ClerkUserTenantService;
import com.nextjstemplate.service.OAuthStateService;
import com.nextjstemplate.service.dto.OAuthStateData;
import com.nextjstemplate.service.dto.SignInResponse;
import com.nextjstemplate.web.rest.errors.InvalidCredentialsException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for OAuth social authentication flows.
 * Handles OAuth initiation and callback for Google, Facebook, GitHub, etc.
 */
@RestController
@RequestMapping("/api/oauth")
@Tag(name = "OAuth Authentication", description = "Backend-driven OAuth flow for social providers")
public class OAuthController {

    private final Logger log = LoggerFactory.getLogger(OAuthController.class);

    private final ClerkIntegrationService clerkIntegrationService;
    private final ClerkAuthService clerkAuthService;
    private final OAuthStateService oauthStateService;
    private final ClerkUserTenantService clerkUserTenantService;
    private final UserProfileRepository userProfileRepository;

    @Value("${server.port:8080}")
    private int serverPort;

    @Value("${server.base-url:http://localhost}")
    private String serverBaseUrl;

    @Value("${frontend.url:http://localhost:3000}")
    private String frontendUrl;

    public OAuthController(
        ClerkIntegrationService clerkIntegrationService,
        ClerkAuthService clerkAuthService,
        OAuthStateService oauthStateService,
        ClerkUserTenantService clerkUserTenantService,
        UserProfileRepository userProfileRepository
    ) {
        this.clerkIntegrationService = clerkIntegrationService;
        this.clerkAuthService = clerkAuthService;
        this.oauthStateService = oauthStateService;
        this.clerkUserTenantService = clerkUserTenantService;
        this.userProfileRepository = userProfileRepository;
    }

    /**
     * GET /api/oauth/{provider}/initiate : Initiate OAuth flow for social provider
     *
     * @param provider    the OAuth provider (google, facebook, github, etc.)
     * @param tenantId    the tenant identifier
     * @param redirectUrl the URL to redirect to after successful authentication (optional)
     * @return 302 redirect to Clerk OAuth URL
     */
    @GetMapping("/{provider}/initiate")
    @Operation(summary = "Initiate OAuth flow", description = "Redirects to Clerk OAuth page for social authentication")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "302", description = "Redirect to OAuth provider"),
            @ApiResponse(responseCode = "400", description = "Invalid provider or tenant"),
        }
    )
    public ResponseEntity<Void> initiateOAuth(
        @Parameter(description = "OAuth provider name", example = "google", required = true) @PathVariable String provider,
        @Parameter(description = "Tenant identifier", example = "tenant_demo_001", required = true) @RequestParam String tenantId,
        @Parameter(description = "Redirect URL after authentication", example = "/dashboard") @RequestParam(
            required = false,
            defaultValue = "/"
        ) String redirectUrl
    ) {
        log.info("Initiating OAuth flow for provider: {}, tenant: {}", provider, tenantId);

        try {
            // Validate provider
            if (!isValidProvider(provider)) {
                log.warn("Invalid OAuth provider: {}", provider);
                return ResponseEntity.badRequest().build();
            }

            // Generate state token for CSRF protection
            OAuthStateData stateData = oauthStateService.createStateToken(provider, tenantId, redirectUrl);

            // Build callback URL
            String callbackUrl = buildCallbackUrl(provider);

            // Generate Clerk OAuth URL
            String oauthUrl = clerkIntegrationService.generateOAuthUrl(provider, callbackUrl, stateData.getStateToken());

            log.debug("Redirecting to OAuth URL: {}", oauthUrl);

            // Redirect to Clerk OAuth page
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(oauthUrl)).build();
        } catch (Exception e) {
            log.error("Error initiating OAuth flow: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/oauth/{provider}/callback : Handle OAuth callback from Clerk
     *
     * @param provider the OAuth provider
     * @param code     the authorization code from OAuth
     * @param state    the state parameter for CSRF protection
     * @param error    error code if OAuth failed (optional)
     * @return 302 redirect to frontend with JWT token or error
     */
    @GetMapping("/{provider}/callback")
    @Operation(summary = "Handle OAuth callback", description = "Processes OAuth callback and redirects to frontend with JWT")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "302", description = "Redirect to frontend with token"),
            @ApiResponse(responseCode = "400", description = "Invalid callback parameters"),
            @ApiResponse(responseCode = "401", description = "Authentication failed"),
        }
    )
    public ResponseEntity<Void> handleOAuthCallback(
        @Parameter(description = "OAuth provider name", example = "google", required = true) @PathVariable String provider,
        @Parameter(description = "Authorization code from OAuth", example = "4/0AX4XfWh...", required = false) @RequestParam(
            required = false
        ) String code,
        @Parameter(description = "State token for CSRF protection", example = "abc123xyz", required = true) @RequestParam String state,
        @Parameter(description = "Error code if OAuth failed", example = "access_denied") @RequestParam(required = false) String error,
        @Parameter(description = "Error description", example = "User denied access") @RequestParam(
            name = "error_description",
            required = false
        ) String errorDescription
    ) {
        log.info("Handling OAuth callback for provider: {}", provider);

        try {
            // Check for OAuth error
            if (error != null) {
                log.warn("OAuth error received: {} - {}", error, errorDescription);
                String errorRedirectUrl = buildFrontendErrorUrl(error, errorDescription);
                return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(errorRedirectUrl)).build();
            }

            // Validate state token (CSRF protection)
            Optional<OAuthStateData> stateDataOpt = oauthStateService.validateAndConsumeStateToken(state);

            if (stateDataOpt.isEmpty()) {
                log.warn("Invalid or expired state token: {}", state);
                String errorRedirectUrl = buildFrontendErrorUrl("invalid_state", "Security validation failed");
                return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(errorRedirectUrl)).build();
            }

            OAuthStateData stateData = stateDataOpt.orElseThrow(() ->
                new IllegalStateException("State data should be present after isEmpty() check")
            );

            // Verify provider matches
            if (!provider.equals(stateData.getProvider())) {
                log.warn("Provider mismatch: expected {}, got {}", stateData.getProvider(), provider);
                String errorRedirectUrl = buildFrontendErrorUrl("provider_mismatch", "Invalid provider");
                return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(errorRedirectUrl)).build();
            }

            // Exchange authorization code for session token
            Optional<String> sessionTokenOpt = clerkIntegrationService.exchangeOAuthCode(provider, code);

            if (sessionTokenOpt.isEmpty()) {
                log.warn("Failed to exchange OAuth code for session token");
                String errorRedirectUrl = buildFrontendErrorUrl("token_exchange_failed", "Failed to complete authentication");
                return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(errorRedirectUrl)).build();
            }

            String sessionToken = sessionTokenOpt.orElseThrow(() ->
                new IllegalStateException("Session token should be present after isEmpty() check")
            );

            // Get user details from session token
            Optional<Map<String, Object>> userDataOpt = clerkIntegrationService.getUserFromSessionToken(sessionToken);

            if (userDataOpt.isEmpty()) {
                log.warn("Failed to get user details from session token");
                String errorRedirectUrl = buildFrontendErrorUrl("user_fetch_failed", "Failed to retrieve user information");
                return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(errorRedirectUrl)).build();
            }

            Map<String, Object> userData = userDataOpt.orElseThrow(() ->
                new IllegalStateException("User data should be present after isEmpty() check")
            );

            String clerkUserId = (String) userData.get("id");
            String email = extractEmail(userData);
            String firstName = (String) userData.get("first_name");
            String lastName = (String) userData.get("last_name");
            String tenantId = stateData.getTenantId();

            log.info("Successfully authenticated user via OAuth: {} ({}) for tenant: {}", email, provider, tenantId);

            // Multi-tenant membership handling: Get or create user profile and tenant membership
            UserProfile userProfile = getOrCreateUserProfile(clerkUserId, email, firstName, lastName, provider);

            // Get or create tenant membership for this user
            ClerkUserTenant tenantMembership = clerkUserTenantService.getOrCreateMembership(
                userProfile,
                tenantId,
                "member" // Default role for OAuth sign-ups
            );

            log.info(
                "User {} has {} access to tenant {} with role: {}",
                userProfile.getId(),
                tenantMembership.getStatus(),
                tenantId,
                tenantMembership.getRole()
            );

            // Build success redirect URL to frontend with tenant context
            String successRedirectUrl = buildFrontendSuccessUrl(
                clerkUserId,
                email,
                firstName,
                lastName,
                tenantId,
                tenantMembership.getRole(),
                stateData.getRedirectUrl()
            );

            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(successRedirectUrl)).build();
        } catch (Exception e) {
            log.error("Error handling OAuth callback: {}", e.getMessage(), e);
            String errorRedirectUrl = buildFrontendErrorUrl("internal_error", "An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(errorRedirectUrl)).build();
        }
    }

    /**
     * Validate OAuth provider name.
     */
    private boolean isValidProvider(String provider) {
        return provider != null && provider.matches("^(google|facebook|github|apple|microsoft)$");
    }

    /**
     * Build callback URL for OAuth redirect.
     */
    private String buildCallbackUrl(String provider) {
        String baseUrl = serverBaseUrl;
        if (!baseUrl.contains(":") && serverPort != 80 && serverPort != 443) {
            baseUrl = baseUrl + ":" + serverPort;
        }
        return baseUrl + "/api/oauth/" + provider + "/callback";
    }

    /**
     * Build frontend error redirect URL.
     */
    private String buildFrontendErrorUrl(String error, String description) {
        try {
            String encodedError = URLEncoder.encode(error, StandardCharsets.UTF_8);
            String encodedDescription = URLEncoder.encode(
                description != null ? description : "Authentication failed",
                StandardCharsets.UTF_8
            );
            return frontendUrl + "/auth/callback?error=" + encodedError + "&error_description=" + encodedDescription;
        } catch (Exception e) {
            log.error("Error building frontend error URL", e);
            return frontendUrl + "/auth/callback?error=unknown";
        }
    }

    /**
     * Build frontend success redirect URL with user info and tenant context.
     */
    private String buildFrontendSuccessUrl(
        String userId,
        String email,
        String firstName,
        String lastName,
        String tenantId,
        String role,
        String redirectUrl
    ) {
        try {
            StringBuilder url = new StringBuilder(frontendUrl);
            url.append("/auth/callback");
            url.append("?success=true");
            url.append("&user_id=").append(URLEncoder.encode(userId, StandardCharsets.UTF_8));
            url.append("&email=").append(URLEncoder.encode(email, StandardCharsets.UTF_8));
            url.append("&tenant_id=").append(URLEncoder.encode(tenantId, StandardCharsets.UTF_8));
            url.append("&role=").append(URLEncoder.encode(role, StandardCharsets.UTF_8));

            if (firstName != null) {
                url.append("&first_name=").append(URLEncoder.encode(firstName, StandardCharsets.UTF_8));
            }
            if (lastName != null) {
                url.append("&last_name=").append(URLEncoder.encode(lastName, StandardCharsets.UTF_8));
            }
            if (redirectUrl != null && !redirectUrl.isEmpty()) {
                url.append("&redirect=").append(URLEncoder.encode(redirectUrl, StandardCharsets.UTF_8));
            }

            return url.toString();
        } catch (Exception e) {
            log.error("Error building frontend success URL", e);
            return frontendUrl + "/auth/callback?success=true";
        }
    }

    /**
     * Extract email from Clerk user data.
     */
    @SuppressWarnings("unchecked")
    private String extractEmail(Map<String, Object> userData) {
        // Clerk stores emails as array of email objects
        Object emailsObj = userData.get("email_addresses");
        if (emailsObj instanceof java.util.List) {
            java.util.List<Object> emails = (java.util.List<Object>) emailsObj;
            if (!emails.isEmpty() && emails.get(0) instanceof Map) {
                Map<String, Object> primaryEmail = (Map<String, Object>) emails.get(0);
                return (String) primaryEmail.get("email_address");
            }
        }

        // Fallback to direct email field
        return (String) userData.get("email");
    }

    /**
     * Get or create UserProfile for OAuth authentication.
     * This implements the multi-tenant shared user pool pattern where one user
     * can belong to multiple tenants.
     *
     * @param clerkUserId Clerk user ID (globally unique)
     * @param email User email
     * @param firstName User first name
     * @param lastName User last name
     * @param provider OAuth provider name
     * @return UserProfile entity
     */
    private UserProfile getOrCreateUserProfile(String clerkUserId, String email, String firstName, String lastName, String provider) {
        log.debug("Getting or creating UserProfile for Clerk user ID: {}", clerkUserId);

        // Check if user already exists by Clerk User ID (tenant-agnostic lookup)
        return userProfileRepository
            .findByClerkUserId(clerkUserId)
            .map(userProfile -> {
                log.debug("Found existing UserProfile: {}", userProfile.getId());

                // Update last sign-in timestamp
                userProfile.setLastSignInAt(ZonedDateTime.now());
                userProfile.setUpdatedAt(ZonedDateTime.now());

                // Update basic info if changed (email, name might have been updated in Clerk)
                if (email != null && !email.equals(userProfile.getEmail())) {
                    log.info("Updating email for user {} from {} to {}", userProfile.getId(), userProfile.getEmail(), email);
                    userProfile.setEmail(email);
                }
                if (firstName != null && !firstName.equals(userProfile.getFirstName())) {
                    userProfile.setFirstName(firstName);
                }
                if (lastName != null && !lastName.equals(userProfile.getLastName())) {
                    userProfile.setLastName(lastName);
                }

                return userProfileRepository.save(userProfile);
            })
            .orElseGet(() -> {
                // Create new user profile (tenant-agnostic - no single tenant_id)
                log.info("Creating new UserProfile for Clerk user ID: {}", clerkUserId);

                UserProfile newUser = new UserProfile();
                newUser.setClerkUserId(clerkUserId);
                newUser.setEmail(email);
                newUser.setFirstName(firstName);
                newUser.setLastName(lastName);
                newUser.setAuthProvider(provider);
                newUser.setEmailVerified(true); // OAuth users are email verified
                newUser.setUserStatus("active");
                newUser.setUserRole("member");
                newUser.setCreatedAt(ZonedDateTime.now());
                newUser.setUpdatedAt(ZonedDateTime.now());
                newUser.setLastSignInAt(ZonedDateTime.now());

                // Generate a unique userId if needed
                newUser.setUserId(java.util.UUID.randomUUID().toString());

                return userProfileRepository.save(newUser);
            });
    }
}
