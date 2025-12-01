package com.nextjstemplate.repository;

import com.nextjstemplate.domain.MembershipPlan;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MembershipPlan entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MembershipPlanRepository extends JpaRepository<MembershipPlan, Long>, JpaSpecificationExecutor<MembershipPlan> {
    Optional<MembershipPlan> findByTenantIdAndPlanCode(String tenantId, String planCode);

    List<MembershipPlan> findByTenantIdAndIsActiveTrue(String tenantId);

    List<MembershipPlan> findByTenantId(String tenantId);

    boolean existsByTenantIdAndPlanCode(String tenantId, String planCode);

    Optional<MembershipPlan> findByStripePriceId(String stripePriceId);

    Optional<MembershipPlan> findByStripeProductId(String stripeProductId);

    @Query("SELECT mp FROM MembershipPlan mp WHERE mp.tenantId = :tenantId AND mp.isActive = :isActive ORDER BY mp.price ASC")
    List<MembershipPlan> findByTenantIdAndIsActive(@Param("tenantId") String tenantId, @Param("isActive") Boolean isActive);

    @Query(
        "SELECT COUNT(ms) > 0 FROM MembershipSubscription ms WHERE ms.membershipPlan.id = :planId AND ms.subscriptionStatus IN ('ACTIVE', 'TRIAL', 'PAST_DUE')"
    )
    boolean hasActiveSubscriptions(@Param("planId") Long planId);
}
