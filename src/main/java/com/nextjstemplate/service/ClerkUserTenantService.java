package com.nextjstemplate.service;

import com.nextjstemplate.domain.ClerkUserTenant;
import com.nextjstemplate.domain.UserProfile;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ClerkUserTenant}.
 * Handles multi-tenant user memberships with role-based access control.
 */
public interface ClerkUserTenantService {
    /**
     * Save a tenant membership.
     *
     * @param clerkUserTenant the entity to save
     * @return the persisted entity
     */
    ClerkUserTenant save(ClerkUserTenant clerkUserTenant);

    /**
     * Update a tenant membership.
     *
     * @param clerkUserTenant the entity to update
     * @return the persisted entity
     */
    ClerkUserTenant update(ClerkUserTenant clerkUserTenant);

    /**
     * Partially update a tenant membership.
     *
     * @param clerkUserTenant the entity to update partially
     * @return the persisted entity
     */
    Optional<ClerkUserTenant> partialUpdate(ClerkUserTenant clerkUserTenant);

    /**
     * Get all tenant memberships.
     *
     * @return the list of entities
     */
    List<ClerkUserTenant> findAll();

    /**
     * Get one tenant membership by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ClerkUserTenant> findOne(Long id);

    /**
     * Delete the tenant membership by id.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Get all tenant memberships for a user profile.
     *
     * @param userProfile the user profile
     * @return list of tenant memberships
     */
    List<ClerkUserTenant> findByUserProfile(UserProfile userProfile);

    /**
     * Get all tenant memberships for a user profile ID.
     *
     * @param userProfileId the user profile ID
     * @return list of tenant memberships
     */
    List<ClerkUserTenant> findByUserProfileId(Long userProfileId);

    /**
     * Get all tenant memberships for a specific tenant.
     *
     * @param tenantId the tenant identifier
     * @return list of tenant memberships
     */
    List<ClerkUserTenant> findByTenantId(String tenantId);

    /**
     * Find a specific tenant membership for a user and tenant combination.
     *
     * @param userProfile the user profile
     * @param tenantId the tenant identifier
     * @return optional tenant membership
     */
    Optional<ClerkUserTenant> findByUserProfileAndTenantId(UserProfile userProfile, String tenantId);

    /**
     * Find a specific tenant membership by user profile ID and tenant ID.
     *
     * @param userProfileId the user profile ID
     * @param tenantId the tenant identifier
     * @return optional tenant membership
     */
    Optional<ClerkUserTenant> findByUserProfileIdAndTenantId(Long userProfileId, String tenantId);

    /**
     * Check if a user has access to a specific tenant.
     *
     * @param userProfileId the user profile ID
     * @param tenantId the tenant identifier
     * @return true if user has active membership
     */
    boolean hasAccessToTenant(Long userProfileId, String tenantId);

    /**
     * Get all active tenant memberships for a user.
     *
     * @param userProfileId the user profile ID
     * @return list of active tenant memberships
     */
    List<ClerkUserTenant> findActiveByUserProfileId(Long userProfileId);

    /**
     * Get all active memberships for a tenant.
     *
     * @param tenantId the tenant identifier
     * @return list of active tenant memberships
     */
    List<ClerkUserTenant> findActiveByTenantId(String tenantId);

    /**
     * Count active members for a tenant.
     *
     * @param tenantId the tenant identifier
     * @return count of active members
     */
    long countActiveByTenantId(String tenantId);

    /**
     * Get or create a tenant membership for a user.
     * If membership doesn't exist, creates one with default role.
     *
     * @param userProfile the user profile
     * @param tenantId the tenant identifier
     * @param defaultRole the default role if creating new membership
     * @return the tenant membership
     */
    ClerkUserTenant getOrCreateMembership(UserProfile userProfile, String tenantId, String defaultRole);

    /**
     * Get or create a tenant membership by user profile ID.
     *
     * @param userProfileId the user profile ID
     * @param tenantId the tenant identifier
     * @param defaultRole the default role if creating new membership
     * @return the tenant membership
     */
    ClerkUserTenant getOrCreateMembershipByUserProfileId(Long userProfileId, String tenantId, String defaultRole);

    /**
     * Update membership role.
     *
     * @param userProfileId the user profile ID
     * @param tenantId the tenant identifier
     * @param newRole the new role
     * @return the updated membership
     */
    Optional<ClerkUserTenant> updateRole(Long userProfileId, String tenantId, String newRole);

    /**
     * Update membership status.
     *
     * @param userProfileId the user profile ID
     * @param tenantId the tenant identifier
     * @param newStatus the new status
     * @return the updated membership
     */
    Optional<ClerkUserTenant> updateStatus(Long userProfileId, String tenantId, String newStatus);

    /**
     * Revoke user access to a tenant.
     *
     * @param userProfileId the user profile ID
     * @param tenantId the tenant identifier
     */
    void revokeAccess(Long userProfileId, String tenantId);

    /**
     * Grant user access to a tenant.
     *
     * @param userProfileId the user profile ID
     * @param tenantId the tenant identifier
     * @param role the role to grant
     * @return the created membership
     */
    ClerkUserTenant grantAccess(Long userProfileId, String tenantId, String role);
}
