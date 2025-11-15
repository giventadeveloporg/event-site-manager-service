package com.nextjstemplate.repository;

import com.nextjstemplate.domain.MembershipSubscription;
import com.nextjstemplate.domain.UserProfile;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MembershipSubscription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MembershipSubscriptionRepository
    extends JpaRepository<MembershipSubscription, Long>, JpaSpecificationExecutor<MembershipSubscription> {
    List<MembershipSubscription> findByTenantIdAndUserProfile(String tenantId, UserProfile userProfile);

    Optional<MembershipSubscription> findByStripeSubscriptionId(String stripeSubscriptionId);

    List<MembershipSubscription> findByTenantIdAndSubscriptionStatus(String tenantId, String subscriptionStatus);
}
