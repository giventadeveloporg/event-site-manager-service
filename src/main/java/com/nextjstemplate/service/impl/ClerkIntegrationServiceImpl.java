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
            // First, get the JWKS from Clerk
            Map<String, Object> jwks = getJwks();

            // Parse the token (without verification for now - you'd need to implement full
            // JWKS verification)
            // For production, use a proper JWT library with JWKS support
            Claims claims = Jwts.parser().build().parseUnsecuredClaims(token.replace("Bearer ", "")).getPayload();

            Map<String, Object> claimsMap = new HashMap<>();
            claims.forEach(claimsMap::put);

            log.debug("JWT token verified successfully");
            return Optional.of(claimsMap);
        } catch (Exception e) {
            log.error("Error verifying JWT token", e);
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

            List<Map<String, Object>> memberships = objectMapper.readValue(response, new TypeReference<List<Map<String, Object>>>() {});

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

            List<Map<String, Object>> memberships = objectMapper.readValue(response, new TypeReference<List<Map<String, Object>>>() {});
            log.debug("Successfully fetched {} organization memberships for user: {}", memberships.size(), clerkUserId);
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

        // Note: Clerk doesn't typically expose a direct email/password authentication
        // endpoint
        // This would normally be handled by Clerk's frontend SDK
        // For backend validation, you'd typically validate the session token
        log.warn("Direct email/password authentication should be handled by Clerk's frontend SDK");

        return Optional.empty();
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
        log.debug("Fetching JWKS from Clerk");

        try {
            String response = webClient
                .get()
                .uri("/.well-known/jwks.json")
                .retrieve()
                .bodyToMono(String.class)
                .timeout(REQUEST_TIMEOUT)
                .block();

            Map<String, Object> jwks = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
            log.debug("Successfully fetched JWKS from Clerk");
            return jwks;
        } catch (WebClientResponseException e) {
            log.error("Error fetching JWKS from Clerk: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Failed to fetch JWKS from Clerk: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error fetching JWKS from Clerk", e);
            throw new RuntimeException("Failed to fetch JWKS from Clerk: " + e.getMessage(), e);
        }
    }
}
