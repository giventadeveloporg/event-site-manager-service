package com.nextjstemplate.repository;

import com.nextjstemplate.domain.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the UserProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long>, JpaSpecificationExecutor<UserProfile> {
    Optional<UserProfile> findByUserId(String userId);

    Optional<UserProfile> findByEmail(String email);

    // New: Find by email and tenantId
    Optional<UserProfile> findByEmailAndTenantId(String email, String tenantId);

    // New: Find all subscribed emails for a tenant
    @Query("SELECT u.email FROM UserProfile u WHERE u.tenantId = :tenantId AND u.isEmailSubscribed = true AND u.email IS NOT NULL")
    List<String> findSubscribedEmailsByTenantId(@Param("tenantId") String tenantId);

    // New: Find subscribed users with pagination (includes all user data needed for
    // email sending)
    @Query("SELECT u FROM UserProfile u WHERE u.tenantId = :tenantId AND u.isEmailSubscribed = true AND u.email IS NOT NULL AND u.emailSubscriptionToken IS NOT NULL")
    Page<UserProfile> findSubscribedUsersByTenantIdWithPagination(@Param("tenantId") String tenantId,
            Pageable pageable);
}
