package com.eventsitemanager.repository;

import com.eventsitemanager.domain.MembershipSubscription;
import com.eventsitemanager.domain.UserProfile;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MembershipSubscription entity.
 *
 * CRITICAL: This repository includes methods for race condition prevention
 * based on patterns from TicketGenerationService.
 */
@SuppressWarnings("unused")
@Repository
public interface MembershipSubscriptionRepository
    extends JpaRepository<MembershipSubscription, Long>, JpaSpecificationExecutor<MembershipSubscription> {
    List<MembershipSubscription> findByTenantIdAndUserProfile(String tenantId, UserProfile userProfile);

    /**
     * Find subscription by Stripe subscription ID.
     * CRITICAL: Used for webhook handling and duplicate prevention.
     * The unique index on stripe_subscription_id ensures at most one result.
     */
    Optional<MembershipSubscription> findByStripeSubscriptionId(String stripeSubscriptionId);

    /**
     * Check if subscription exists by Stripe subscription ID.
     * More efficient than findByStripeSubscriptionId when only checking existence.
     */
    boolean existsByStripeSubscriptionId(String stripeSubscriptionId);

    List<MembershipSubscription> findByTenantIdAndSubscriptionStatus(String tenantId, String subscriptionStatus);

    /**
     * Find subscription by Stripe subscription ID and tenant ID.
     * Used for tenant-aware webhook processing.
     */
    Optional<MembershipSubscription> findByStripeSubscriptionIdAndTenantId(String stripeSubscriptionId, String tenantId);

    /**
     * Find user's active subscription by user profile ID and allowed statuses.
     * Used to check if user already has an active subscription.
     */
    @Query(
        "SELECT ms FROM MembershipSubscription ms WHERE ms.userProfile.id = :userProfileId AND ms.subscriptionStatus IN :statuses ORDER BY ms.createdAt DESC"
    )
    List<MembershipSubscription> findByUserProfileIdAndSubscriptionStatusIn(
        @Param("userProfileId") Long userProfileId,
        @Param("statuses") List<String> statuses
    );

    /**
     * Find user's active subscription (single result).
     * Returns the most recent active/trial subscription for a user.
     */
    @Query(
        "SELECT ms FROM MembershipSubscription ms WHERE ms.userProfile.id = :userProfileId AND ms.tenantId = :tenantId AND ms.subscriptionStatus IN ('ACTIVE', 'TRIAL') ORDER BY ms.createdAt DESC"
    )
    Optional<MembershipSubscription> findActiveSubscriptionByUserProfileIdAndTenantId(
        @Param("userProfileId") Long userProfileId,
        @Param("tenantId") String tenantId
    );

    /**
     * Find subscriptions expiring soon (for trial expiration notifications, etc.)
     */
    @Query("SELECT ms FROM MembershipSubscription ms WHERE ms.currentPeriodEnd <= :expirationDate AND ms.subscriptionStatus IN :statuses")
    List<MembershipSubscription> findExpiringSoon(
        @Param("expirationDate") LocalDate expirationDate,
        @Param("statuses") List<String> statuses
    );

    /**
     * Find by Stripe customer ID.
     */
    List<MembershipSubscription> findByStripeCustomerId(String stripeCustomerId);

    /**
     * Count active subscriptions for a membership plan.
     */
    @Query(
        "SELECT COUNT(ms) FROM MembershipSubscription ms WHERE ms.membershipPlan.id = :planId AND ms.subscriptionStatus IN ('ACTIVE', 'TRIAL', 'PAST_DUE')"
    )
    long countActiveSubscriptionsByPlanId(@Param("planId") Long planId);
}
