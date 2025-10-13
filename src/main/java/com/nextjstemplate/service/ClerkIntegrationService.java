package com.nextjstemplate.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service Interface for operations with Clerk authentication platform.
 */
public interface ClerkIntegrationService {
    /**
     * Create a new user in Clerk.
     *
     * @param email     the user's email
     * @param password  the user's password
     * @param firstName the user's first name
     * @param lastName  the user's last name
     * @param metadata  additional user metadata
     * @return the created user data including Clerk user ID
     */
    Map<String, Object> createUser(String email, String password, String firstName, String lastName, Map<String, Object> metadata);

    /**
     * Get a user by their Clerk user ID.
     *
     * @param clerkUserId the Clerk user ID
     * @return the user data if found
     */
    Optional<Map<String, Object>> getUserById(String clerkUserId);

    /**
     * Get a user by their email address.
     *
     * @param email the user's email
     * @return the user data if found
     */
    Optional<Map<String, Object>> getUserByEmail(String email);

    /**
     * Update a user in Clerk.
     *
     * @param clerkUserId the Clerk user ID
     * @param updates     map of fields to update
     * @return the updated user data
     */
    Map<String, Object> updateUser(String clerkUserId, Map<String, Object> updates);

    /**
     * Delete a user from Clerk.
     *
     * @param clerkUserId the Clerk user ID
     */
    void deleteUser(String clerkUserId);

    /**
     * Validate a Clerk session token.
     *
     * @param sessionToken the session token to validate
     * @return the decoded token claims if valid
     */
    Optional<Map<String, Object>> validateSessionToken(String sessionToken);

    /**
     * Verify a JWT token from Clerk.
     *
     * @param token the JWT token
     * @return the decoded token claims if valid
     */
    Optional<Map<String, Object>> verifyJwtToken(String token);

    /**
     * Get organizations for a user.
     *
     * @param clerkUserId the Clerk user ID
     * @return list of organizations the user belongs to
     */
    List<Map<String, Object>> getUserOrganizations(String clerkUserId);

    /**
     * Get organization memberships for a user.
     *
     * @param clerkUserId the Clerk user ID
     * @return list of organization memberships with roles
     */
    List<Map<String, Object>> getUserOrganizationMemberships(String clerkUserId);

    /**
     * Add a user to an organization.
     *
     * @param organizationId the organization ID
     * @param clerkUserId    the Clerk user ID
     * @param role           the role to assign
     * @return the created membership data
     */
    Map<String, Object> addUserToOrganization(String organizationId, String clerkUserId, String role);

    /**
     * Remove a user from an organization.
     *
     * @param organizationId the organization ID
     * @param clerkUserId    the Clerk user ID
     */
    void removeUserFromOrganization(String organizationId, String clerkUserId);

    /**
     * Update user's role in an organization.
     *
     * @param organizationId the organization ID
     * @param clerkUserId    the Clerk user ID
     * @param role           the new role
     * @return the updated membership data
     */
    Map<String, Object> updateUserOrganizationRole(String organizationId, String clerkUserId, String role);

    /**
     * Authenticate user with email and password.
     *
     * @param email    the user's email
     * @param password the user's password
     * @return authentication response with session token
     */
    Optional<Map<String, Object>> authenticateUser(String email, String password);

    /**
     * Refresh an access token.
     *
     * @param refreshToken the refresh token
     * @return new access and refresh tokens
     */
    Optional<Map<String, Object>> refreshAccessToken(String refreshToken);

    /**
     * Get user sessions.
     *
     * @param clerkUserId the Clerk user ID
     * @return list of active sessions
     */
    List<Map<String, Object>> getUserSessions(String clerkUserId);

    /**
     * Revoke a session.
     *
     * @param sessionId the session ID to revoke
     */
    void revokeSession(String sessionId);

    /**
     * Get the JWKS (JSON Web Key Set) from Clerk for token verification.
     *
     * @return the JWKS data
     */
    Map<String, Object> getJwks();
}
