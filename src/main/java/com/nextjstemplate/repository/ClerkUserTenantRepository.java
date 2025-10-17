package com.nextjstemplate.repository;

import com.nextjstemplate.domain.ClerkUserTenant;
import com.nextjstemplate.domain.UserProfile;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ClerkUserTenant entity.
 * Manages multi-tenant user memberships.
 */
@Repository
public interface ClerkUserTenantRepository extends JpaRepository<ClerkUserTenant, Long> {
    /**
     * Find all tenant memberships for a specific user profile.
     *
     * @param userProfile the user profile
     * @return list of tenant memberships
     */
    List<ClerkUserTenant> findByUserProfile(UserProfile userProfile);

    /**
     * Find all tenant memberships for a user profile ID.
     *
     * @param userProfileId the user profile ID
     * @return list of tenant memberships
     */
    @Query("SELECT cut FROM ClerkUserTenant cut WHERE cut.userProfile.id = :userProfileId")
    List<ClerkUserTenant> findByUserProfileId(@Param("userProfileId") Long userProfileId);

    /**
     * Find all tenant memberships for a specific tenant.
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
    @Query("SELECT cut FROM ClerkUserTenant cut WHERE cut.userProfile.id = :userProfileId AND cut.tenantId = :tenantId")
    Optional<ClerkUserTenant> findByUserProfileIdAndTenantId(
        @Param("userProfileId") Long userProfileId,
        @Param("tenantId") String tenantId
    );

    /**
     * Check if a user has access to a specific tenant.
     *
     * @param userProfileId the user profile ID
     * @param tenantId the tenant identifier
     * @return true if user has active membership
     */
    @Query(
        "SELECT CASE WHEN COUNT(cut) > 0 THEN true ELSE false END " +
        "FROM ClerkUserTenant cut " +
        "WHERE cut.userProfile.id = :userProfileId " +
        "AND cut.tenantId = :tenantId " +
        "AND cut.status = 'active'"
    )
    boolean existsActiveByUserProfileIdAndTenantId(@Param("userProfileId") Long userProfileId, @Param("tenantId") String tenantId);

    /**
     * Find all active tenant memberships for a user.
     *
     * @param userProfileId the user profile ID
     * @return list of active tenant memberships
     */
    @Query("SELECT cut FROM ClerkUserTenant cut " + "WHERE cut.userProfile.id = :userProfileId " + "AND cut.status = 'active'")
    List<ClerkUserTenant> findActiveByUserProfileId(@Param("userProfileId") Long userProfileId);

    /**
     * Find all active memberships for a tenant.
     *
     * @param tenantId the tenant identifier
     * @return list of active tenant memberships
     */
    @Query("SELECT cut FROM ClerkUserTenant cut " + "WHERE cut.tenantId = :tenantId " + "AND cut.status = 'active'")
    List<ClerkUserTenant> findActiveByTenantId(@Param("tenantId") String tenantId);

    /**
     * Count active members for a tenant.
     *
     * @param tenantId the tenant identifier
     * @return count of active members
     */
    @Query("SELECT COUNT(cut) FROM ClerkUserTenant cut " + "WHERE cut.tenantId = :tenantId " + "AND cut.status = 'active'")
    long countActiveByTenantId(@Param("tenantId") String tenantId);

    /**
     * Delete all tenant memberships for a user profile.
     *
     * @param userProfile the user profile
     */
    void deleteByUserProfile(UserProfile userProfile);

    /**
     * Delete a specific tenant membership.
     *
     * @param userProfile the user profile
     * @param tenantId the tenant identifier
     */
    void deleteByUserProfileAndTenantId(UserProfile userProfile, String tenantId);
}
