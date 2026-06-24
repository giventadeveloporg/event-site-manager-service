package com.eventsitemanager.service;

/**
 * Service Interface for checking user permissions and roles.
 */
public interface PermissionService {
    /**
     * Check if a user has a specific role.
     *
     * @param clerkUserId the Clerk user ID
     * @param role        the role to check
     * @param tenantId    the tenant ID
     * @return true if user has the role
     */
    boolean hasRole(String clerkUserId, String role, String tenantId);

    /**
     * Check if a user has at least the minimum required role.
     *
     * @param clerkUserId  the Clerk user ID
     * @param requiredRole the minimum required role
     * @param tenantId     the tenant ID
     * @return true if user has sufficient role
     */
    boolean hasMinimumRole(String clerkUserId, String requiredRole, String tenantId);

    /**
     * Check if a user has any of the specified roles.
     *
     * @param clerkUserId the Clerk user ID
     * @param roles       array of roles to check
     * @param tenantId    the tenant ID
     * @return true if user has at least one of the roles
     */
    boolean hasAnyRole(String clerkUserId, String[] roles, String tenantId);

    /**
     * Check if a user has all specified roles.
     *
     * @param clerkUserId the Clerk user ID
     * @param roles       array of roles to check
     * @param tenantId    the tenant ID
     * @return true if user has all roles
     */
    boolean hasAllRoles(String clerkUserId, String[] roles, String tenantId);

    /**
     * Check if a user can access a specific resource.
     *
     * @param clerkUserId  the Clerk user ID
     * @param resourceId   the resource identifier
     * @param resourceType the type of resource
     * @param tenantId     the tenant ID
     * @return true if user can access the resource
     */
    boolean canAccessResource(String clerkUserId, String resourceId, String resourceType, String tenantId);

    /**
     * Check if a user can perform an action on a resource.
     *
     * @param clerkUserId  the Clerk user ID
     * @param action       the action (e.g., "read", "write", "delete")
     * @param resourceId   the resource identifier
     * @param resourceType the type of resource
     * @param tenantId     the tenant ID
     * @return true if user can perform the action
     */
    boolean canPerformAction(String clerkUserId, String action, String resourceId, String resourceType, String tenantId);

    /**
     * Check if a user is an admin in a tenant.
     *
     * @param clerkUserId the Clerk user ID
     * @param tenantId    the tenant ID
     * @return true if user is an admin
     */
    boolean isAdmin(String clerkUserId, String tenantId);

    /**
     * Check if a user is a super admin (across all tenants).
     *
     * @param clerkUserId the Clerk user ID
     * @return true if user is a super admin
     */
    boolean isSuperAdmin(String clerkUserId);

    /**
     * Get user's role in a specific tenant.
     *
     * @param clerkUserId the Clerk user ID
     * @param tenantId    the tenant ID
     * @return the user's role, or null if not found
     */
    String getUserRole(String clerkUserId, String tenantId);

    /**
     * Check if a user has access to a tenant.
     *
     * @param clerkUserId the Clerk user ID
     * @param tenantId    the tenant ID
     * @return true if user has access
     */
    boolean hasTenantAccess(String clerkUserId, String tenantId);
}
