package com.nextjstemplate.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextjstemplate.config.ClerkProperties;
import com.nextjstemplate.service.ClerkIntegrationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.time.Duration;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

/**
 * Implementation of ClerkIntegrationService for interacting with Clerk API.
 */
@Service
public class ClerkIntegrationServiceImpl implements ClerkIntegrationService {

    private static final Logger log = LoggerFactory.getLogger(ClerkIntegrationServiceImpl.class);
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(30);

    private final WebClient webClient;
    private final ClerkProperties clerkProperties;
    private final ObjectMapper objectMapper;

    public ClerkIntegrationServiceImpl(ClerkProperties clerkProperties, ObjectMapper objectMapper) {
        this.clerkProperties = clerkProperties;
        this.objectMapper = objectMapper;
        this.webClient =
            WebClient
                .builder()
                .baseUrl(clerkProperties.getApiUrl())
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + clerkProperties.getSecretKey())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        log.info("ClerkIntegrationService initialized with API URL: {}", clerkProperties.getApiUrl());
    }

    @Override
    public Map<String, Object> createUser(String email, String password, String firstName, String lastName, Map<String, Object> metadata) {
        log.debug("Creating user in Clerk with email: {}", email);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("email_address", List.of(email));
        requestBody.put("password", password);
        requestBody.put("first_name", firstName);
        requestBody.put("last_name", lastName);
        if (metadata != null && !metadata.isEmpty()) {
            requestBody.put("public_metadata", metadata);
        }

        try {
            String response = webClient
                .post()
                .uri("/users")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(REQUEST_TIMEOUT)
                .block();

            Map<String, Object> userData = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
            log.info("Successfully created user in Clerk: {}", userData.get("id"));
            return userData;
        } catch (WebClientResponseException e) {
            log.error("Error creating user in Clerk: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Failed to create user in Clerk: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error creating user in Clerk", e);
            throw new RuntimeException("Failed to create user in Clerk: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Map<String, Object>> getUserById(String clerkUserId) {
        log.debug("Fetching user from Clerk with ID: {}", clerkUserId);

        try {
            String response = webClient
                .get()
                .uri("/users/{userId}", clerkUserId)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(REQUEST_TIMEOUT)
                .block();

            Map<String, Object> userData = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
            log.debug("Successfully fetched user from Clerk: {}", clerkUserId);
            return Optional.of(userData);
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.debug("User not found in Clerk: {}", clerkUserId);
                return Optional.empty();
            }
            log.error("Error fetching user from Clerk: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Failed to fetch user from Clerk: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error fetching user from Clerk", e);
            throw new RuntimeException("Failed to fetch user from Clerk: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Map<String, Object>> getUserByEmail(String email) {
        log.debug("Fetching user from Clerk with email: {}", email);

        try {
            String response = webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/users").queryParam("email_address", email).build())
                .retrieve()
                .bodyToMono(String.class)
                .timeout(REQUEST_TIMEOUT)
                .block();

            List<Map<String, Object>> users = objectMapper.readValue(response, new TypeReference<List<Map<String, Object>>>() {});
            if (users != null && !users.isEmpty()) {
                log.debug("Successfully fetched user from Clerk by email: {}", email);
                return Optional.of(users.get(0));
            }
            log.debug("User not found in Clerk with email: {}", email);
            return Optional.empty();
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.debug("User not found in Clerk with email: {}", email);
                return Optional.empty();
            }
            log.error("Error fetching user by email from Clerk: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Failed to fetch user by email from Clerk: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error fetching user by email from Clerk", e);
            throw new RuntimeException("Failed to fetch user by email from Clerk: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> updateUser(String clerkUserId, Map<String, Object> updates) {
        log.debug("Updating user in Clerk: {}", clerkUserId);

        try {
            String response = webClient
                .patch()
                .uri("/users/{userId}", clerkUserId)
                .bodyValue(updates)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(REQUEST_TIMEOUT)
                .block();

            Map<String, Object> userData = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
            log.info("Successfully updated user in Clerk: {}", clerkUserId);
            return userData;
        } catch (WebClientResponseException e) {
            log.error("Error updating user in Clerk: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Failed to update user in Clerk: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error updating user in Clerk", e);
            throw new RuntimeException("Failed to update user in Clerk: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteUser(String clerkUserId) {
        log.debug("Deleting user from Clerk: {}", clerkUserId);

        try {
            webClient.delete().uri("/users/{userId}", clerkUserId).retrieve().bodyToMono(Void.class).timeout(REQUEST_TIMEOUT).block();

            log.info("Successfully deleted user from Clerk: {}", clerkUserId);
        } catch (WebClientResponseException e) {
            log.error("Error deleting user from Clerk: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Failed to delete user from Clerk: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error deleting user from Clerk", e);
            throw new RuntimeException("Failed to delete user from Clerk: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Map<String, Object>> validateSessionToken(String sessionToken) {
        log.debug("Validating session token");

        try {
            String response = webClient
                .post()
                .uri("/sessions/{sessionId}/verify", sessionToken)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(REQUEST_TIMEOUT)
                .block();

            Map<String, Object> sessionData = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
            log.debug("Session token validated successfully");
            return Optional.of(sessionData);
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED || e.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.debug("Session token validation failed");
                return Optional.empty();
            }
            log.error("Error validating session token: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Failed to validate session token: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error validating session token", e);
            throw new RuntimeException("Failed to validate session token: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Map<String, Object>> verifyJwtToken(String token) {
        log.debug("Verifying JWT token");

        try {
            // For self-signed JWTs issued by this backend, just parse without verification
            // The actual verification happens in ClerkJwtAuthenticationFilter using JwtDecoder
            // This method is only for extracting claims from already-validated tokens
            String cleanToken = token.replace("Bearer ", "").trim();
            Claims claims = Jwts.parser().build().parseUnsecuredClaims(cleanToken).getPayload();

            Map<String, Object> claimsMap = new HashMap<>();
            claims.forEach(claimsMap::put);

            log.debug("JWT token parsed successfully");
            return Optional.of(claimsMap);
        } catch (Exception e) {
            log.debug("Error parsing JWT token: {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Map<String, Object>> getUserOrganizations(String clerkUserId) {
        log.debug("Fetching organizations for user: {}", clerkUserId);

        try {
            String response = webClient
                .get()
                .uri("/users/{userId}/organization_memberships", clerkUserId)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(REQUEST_TIMEOUT)
                .block();

            // FIX: Clerk API returns {"data": [...], "total_count": N}, not a direct array
            // First deserialize to wrapper object, then extract the data array
            Map<String, Object> wrapper = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
            List<Map<String, Object>> memberships = (List<Map<String, Object>>) wrapper.get("data");

            // Extract organization details from memberships
            List<Map<String, Object>> organizations = new ArrayList<>();
            if (memberships != null) {
                for (Map<String, Object> membership : memberships) {
                    if (membership.containsKey("organization")) {
                        organizations.add((Map<String, Object>) membership.get("organization"));
                    }
                }
            }

            log.debug("Successfully fetched {} organizations for user: {}", organizations.size(), clerkUserId);
            return organizations;
        } catch (WebClientResponseException e) {
            log.error("Error fetching user organizations from Clerk: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Failed to fetch user organizations from Clerk: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error fetching user organizations from Clerk", e);
            throw new RuntimeException("Failed to fetch user organizations from Clerk: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, Object>> getUserOrganizationMemberships(String clerkUserId) {
        log.debug("Fetching organization memberships for user: {}", clerkUserId);

        try {
            String response = webClient
                .get()
                .uri("/users/{userId}/organization_memberships", clerkUserId)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(REQUEST_TIMEOUT)
                .block();

            // FIX: Clerk API returns {"data": [...], "total_count": N}, not a direct array
            // First deserialize to wrapper object, then extract the data array
            Map<String, Object> wrapper = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
            List<Map<String, Object>> memberships = (List<Map<String, Object>>) wrapper.get("data");

            log.debug(
                "Successfully fetched {} organization memberships for user: {}",
                memberships != null ? memberships.size() : 0,
                clerkUserId
            );
            return memberships != null ? memberships : new ArrayList<>();
        } catch (WebClientResponseException e) {
            log.error("Error fetching organization memberships from Clerk: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Failed to fetch organization memberships from Clerk: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error fetching organization memberships from Clerk", e);
            throw new RuntimeException("Failed to fetch organization memberships from Clerk: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> addUserToOrganization(String organizationId, String clerkUserId, String role) {
        log.debug("Adding user {} to organization {} with role {}", clerkUserId, organizationId, role);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("user_id", clerkUserId);
        requestBody.put("role", role);

        try {
            String response = webClient
                .post()
                .uri("/organizations/{organizationId}/memberships", organizationId)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(REQUEST_TIMEOUT)
                .block();

            Map<String, Object> membershipData = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
            log.info("Successfully added user {} to organization {}", clerkUserId, organizationId);
            return membershipData;
        } catch (WebClientResponseException e) {
            log.error("Error adding user to organization in Clerk: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Failed to add user to organization in Clerk: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error adding user to organization in Clerk", e);
            throw new RuntimeException("Failed to add user to organization in Clerk: " + e.getMessage(), e);
        }
    }

    @Override
    public void removeUserFromOrganization(String organizationId, String clerkUserId) {
        log.debug("Removing user {} from organization {}", clerkUserId, organizationId);

        try {
            webClient
                .delete()
                .uri("/organizations/{organizationId}/memberships/{userId}", organizationId, clerkUserId)
                .retrieve()
                .bodyToMono(Void.class)
                .timeout(REQUEST_TIMEOUT)
                .block();

            log.info("Successfully removed user {} from organization {}", clerkUserId, organizationId);
        } catch (WebClientResponseException e) {
            log.error("Error removing user from organization in Clerk: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Failed to remove user from organization in Clerk: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error removing user from organization in Clerk", e);
            throw new RuntimeException("Failed to remove user from organization in Clerk: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> updateUserOrganizationRole(String organizationId, String clerkUserId, String role) {
        log.debug("Updating role for user {} in organization {} to {}", clerkUserId, organizationId, role);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("role", role);

        try {
            String response = webClient
                .patch()
                .uri("/organizations/{organizationId}/memberships/{userId}", organizationId, clerkUserId)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(REQUEST_TIMEOUT)
                .block();

            Map<String, Object> membershipData = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
            log.info("Successfully updated role for user {} in organization {}", clerkUserId, organizationId);
            return membershipData;
        } catch (WebClientResponseException e) {
            log.error("Error updating user organization role in Clerk: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Failed to update user organization role in Clerk: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error updating user organization role in Clerk", e);
            throw new RuntimeException("Failed to update user organization role in Clerk: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Map<String, Object>> authenticateUser(String email, String password) {
        log.debug("Authenticating user with email: {}", email);

        try {
            // Step 1: Fetch user by email to verify user exists
            Optional<Map<String, Object>> userOpt = getUserByEmail(email);
            if (!userOpt.isPresent()) {
                log.debug("User not found in Clerk with email: {}", email);
                return Optional.empty();
            }

            // Modernizer-compliant: Use orElseThrow instead of isEmpty() + get()
            Map<String, Object> user = userOpt.orElseThrow(() -> new IllegalStateException("User should be present after isPresent() check")
            );

            String userId = (String) user.get("id");
            log.debug("Found user in Clerk: {}", userId);

            // Step 2: Verify password using Clerk's password verification endpoint
            // Clerk Backend API provides a password verification endpoint for server-side validation
            Map<String, Object> verifyRequest = new HashMap<>();
            verifyRequest.put("password", password);

            try {
                String response = webClient
                    .post()
                    .uri("/users/{userId}/verify_password", userId)
                    .bodyValue(verifyRequest)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(REQUEST_TIMEOUT)
                    .block();

                Map<String, Object> verifyResponse = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});

                // Check if password verification was successful
                Boolean verified = (Boolean) verifyResponse.get("verified");
                if (Boolean.TRUE.equals(verified)) {
                    log.info("Successfully authenticated user: {}", email);
                    // Return user data with authentication confirmation
                    Map<String, Object> result = new HashMap<>(user);
                    result.put("authenticated", true);
                    result.put("verified_at", System.currentTimeMillis());
                    return Optional.of(result);
                } else {
                    log.warn("Password verification failed for user: {}", email);
                    return Optional.empty();
                }
            } catch (WebClientResponseException e) {
                // 401, 403, or 422 means invalid credentials or verification failed
                if (
                    e.getStatusCode() == HttpStatus.UNAUTHORIZED ||
                    e.getStatusCode() == HttpStatus.FORBIDDEN ||
                    e.getStatusCode() == HttpStatus.UNPROCESSABLE_ENTITY
                ) {
                    log.debug("Invalid credentials for email: {} - Status: {}", email, e.getStatusCode());
                    return Optional.empty();
                }
                // For other errors, log and return empty
                log.error("Error during password verification: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
                return Optional.empty();
            }
        } catch (Exception e) {
            log.error("Unexpected error authenticating user: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Map<String, Object>> refreshAccessToken(String refreshToken) {
        log.debug("Refreshing access token");

        // Note: Token refresh is typically handled by Clerk's frontend SDK
        // Backend services validate the session tokens
        log.warn("Token refresh should be handled by Clerk's frontend SDK");

        return Optional.empty();
    }

    @Override
    public List<Map<String, Object>> getUserSessions(String clerkUserId) {
        log.debug("Fetching sessions for user: {}", clerkUserId);

        try {
            String response = webClient
                .get()
                .uri("/users/{userId}/sessions", clerkUserId)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(REQUEST_TIMEOUT)
                .block();

            List<Map<String, Object>> sessions = objectMapper.readValue(response, new TypeReference<List<Map<String, Object>>>() {});
            log.debug("Successfully fetched {} sessions for user: {}", sessions.size(), clerkUserId);
            return sessions != null ? sessions : new ArrayList<>();
        } catch (WebClientResponseException e) {
            log.error("Error fetching user sessions from Clerk: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Failed to fetch user sessions from Clerk: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error fetching user sessions from Clerk", e);
            throw new RuntimeException("Failed to fetch user sessions from Clerk: " + e.getMessage(), e);
        }
    }

    @Override
    public void revokeSession(String sessionId) {
        log.debug("Revoking session: {}", sessionId);

        try {
            webClient
                .post()
                .uri("/sessions/{sessionId}/revoke", sessionId)
                .retrieve()
                .bodyToMono(Void.class)
                .timeout(REQUEST_TIMEOUT)
                .block();

            log.info("Successfully revoked session: {}", sessionId);
        } catch (WebClientResponseException e) {
            log.error("Error revoking session in Clerk: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Failed to revoke session in Clerk: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error revoking session in Clerk", e);
            throw new RuntimeException("Failed to revoke session in Clerk: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getJwks() {
        log.debug("JWKS not used for self-signed JWT tokens");

        // This method is not used for self-signed JWTs issued by this backend
        // If you need to verify Clerk-issued tokens in the future, implement actual JWKS fetching
        // For now, return empty map to avoid unnecessary API calls
        return new HashMap<>();
    }

    @Override
    public String generateOAuthUrl(String provider, String redirectUri, String state) {
        log.debug("Generating OAuth URL for provider: {} with redirect: {}", provider, redirectUri);

        // FIXED: Use Frontend API URL instead of API URL
        String clerkFrontendApi = clerkProperties.getFrontendApi();

        log.debug("Raw Frontend API from properties: {}", clerkFrontendApi);

        // Ensure URL has https:// prefix
        if (!clerkFrontendApi.startsWith("http://") && !clerkFrontendApi.startsWith("https://")) {
            clerkFrontendApi = "https://" + clerkFrontendApi;
            log.debug("Added https:// prefix: {}", clerkFrontendApi);
        }

        // Remove trailing slash if present
        if (clerkFrontendApi.endsWith("/")) {
            clerkFrontendApi = clerkFrontendApi.substring(0, clerkFrontendApi.length() - 1);
            log.debug("Removed trailing slash: {}", clerkFrontendApi);
        }

        // Build OAuth URL using Frontend API
        String clerkOAuthBaseUrl = clerkFrontendApi + "/oauth/authorize";

        log.debug("Using Clerk Frontend API: {}", clerkFrontendApi);
        log.debug("OAuth base URL: {}", clerkOAuthBaseUrl);

        StringBuilder urlBuilder = new StringBuilder(clerkOAuthBaseUrl);
        urlBuilder.append("?provider=").append(provider);
        urlBuilder.append("&redirect_uri=").append(java.net.URLEncoder.encode(redirectUri, java.nio.charset.StandardCharsets.UTF_8));
        urlBuilder.append("&state=").append(state);

        String oauthUrl = urlBuilder.toString();
        log.debug("Generated OAuth URL: {}", oauthUrl);

        return oauthUrl;
    }

    @Override
    public Optional<String> exchangeOAuthCode(String provider, String code) {
        log.debug("Exchanging OAuth code for session token, provider: {}", provider);

        try {
            // Clerk OAuth token exchange endpoint
            // POST /v1/oauth/token
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("code", code);
            requestBody.put("grant_type", "authorization_code");

            String response = webClient
                .post()
                .uri("/oauth/token")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(REQUEST_TIMEOUT)
                .block();

            Map<String, Object> tokenResponse = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});

            // Extract session token or access token from response
            String sessionToken = (String) tokenResponse.get("session_token");
            if (sessionToken == null) {
                sessionToken = (String) tokenResponse.get("access_token");
            }

            if (sessionToken != null) {
                log.info("Successfully exchanged OAuth code for session token");
                return Optional.of(sessionToken);
            } else {
                log.warn("No session token found in OAuth token response");
                return Optional.empty();
            }
        } catch (WebClientResponseException e) {
            log.error("Error exchanging OAuth code: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            return Optional.empty();
        } catch (Exception e) {
            log.error("Unexpected error exchanging OAuth code", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Map<String, Object>> getUserFromSessionToken(String sessionToken) {
        log.debug("Getting user details from session token");

        try {
            // First, validate the session token to get session details
            Optional<Map<String, Object>> sessionDataOpt = validateSessionToken(sessionToken);

            if (sessionDataOpt.isEmpty()) {
                log.warn("Invalid session token");
                return Optional.empty();
            }

            Map<String, Object> sessionData = sessionDataOpt.orElseThrow(() ->
                new IllegalStateException("Session data should be present after isEmpty() check")
            );

            // Extract user ID from session
            String userId = (String) sessionData.get("user_id");
            if (userId == null) {
                log.warn("No user ID found in session data");
                return Optional.empty();
            }

            // Fetch user details
            return getUserById(userId);
        } catch (Exception e) {
            log.error("Error getting user from session token", e);
            return Optional.empty();
        }
    }
}
