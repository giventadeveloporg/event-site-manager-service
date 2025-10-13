package com.nextjstemplate.service;

import java.util.List;
import java.util.Map;

/**
 * Service Interface for synchronizing Clerk Organizations with application
 * data.
 */
public interface OrganizationSyncService {
    /**
     * Sync all organizations for a user from Clerk to the database.
     *
     * @param clerkUserId the Clerk user ID
     * @return list of synced organization data
     */
    List<Map<String, Object>> syncUserOrganizations(String clerkUserId);

    /**
     * Sync a specific organization membership.
     *
     * @param clerkUserId    the Clerk user ID
     * @param organizationId the Clerk organization ID
     * @return the synced organization membership data
     */
    Map<String, Object> syncOrganizationMembership(String clerkUserId, String organizationId);

    /**
     * Update user role based on Clerk organization role.
     *
     * @param clerkUserId the Clerk user ID
     * @param clerkRole   the role from Clerk organization
     * @param tenantId    the tenant ID
     * @return the mapped application role
     */
    String updateUserRoleFromClerkRole(String clerkUserId, String clerkRole, String tenantId);

    /**
     * Get all organization memberships for a user.
     *
     * @param clerkUserId the Clerk user ID
     * @return list of organization memberships
     */
    List<Map<String, Object>> getUserOrganizationMemberships(String clerkUserId);

    /**
     * Sync organization metadata to tenant settings.
     *
     * @param organizationId the Clerk organization ID
     * @return true if sync was successful
     */
    boolean syncOrganizationToTenant(String organizationId);
}
