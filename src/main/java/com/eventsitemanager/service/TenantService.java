package com.eventsitemanager.service;

import com.eventsitemanager.domain.UserProfile;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for multi-tenant operations.
 */
public interface TenantService {
    /**
     * Validate if a tenant ID is valid.
     *
     * @param tenantId the tenant ID to validate
     * @return true if valid, false otherwise
     */
    boolean isValidTenant(String tenantId);

    /**
     * Get all users for a specific tenant.
     *
     * @param tenantId the tenant ID
     * @return list of user profiles for the tenant
     */
    List<UserProfile> getUsersForTenant(String tenantId);

    /**
     * Get all tenants a user belongs to.
     *
     * @param userEmail the user's email
     * @return list of tenant IDs the user belongs to
     */
    List<String> getUserTenants(String userEmail);

    /**
     * Check if a user belongs to a tenant.
     *
     * @param userEmail the user's email
     * @param tenantId  the tenant ID
     * @return true if user belongs to tenant, false otherwise
     */
    boolean userBelongsToTenant(String userEmail, String tenantId);

    /**
     * Add a user to a tenant.
     *
     * @param userEmail the user's email
     * @param tenantId  the tenant ID
     * @return the created or updated user profile
     */
    Optional<UserProfile> addUserToTenant(String userEmail, String tenantId);

    /**
     * Remove a user from a tenant.
     *
     * @param userEmail the user's email
     * @param tenantId  the tenant ID
     * @return true if removal was successful
     */
    boolean removeUserFromTenant(String userEmail, String tenantId);

    /**
     * Switch user's active tenant context.
     *
     * @param clerkUserId    the Clerk user ID
     * @param targetTenantId the target tenant ID
     * @return Optional containing the user profile for the target tenant
     */
    Optional<UserProfile> switchTenant(String clerkUserId, String targetTenantId);

    /**
     * Get the default tenant ID.
     *
     * @return the default tenant ID
     */
    String getDefaultTenant();

    /**
     * Validate tenant access for a user.
     *
     * @param clerkUserId the Clerk user ID
     * @param tenantId    the tenant ID to validate
     * @return true if user has access to the tenant
     */
    boolean validateTenantAccess(String clerkUserId, String tenantId);
}
