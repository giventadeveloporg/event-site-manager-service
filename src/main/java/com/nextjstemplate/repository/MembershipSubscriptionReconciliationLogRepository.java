package com.nextjstemplate.repository;

import com.nextjstemplate.domain.MembershipSubscription;
import com.nextjstemplate.domain.MembershipSubscriptionReconciliationLog;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MembershipSubscriptionReconciliationLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MembershipSubscriptionReconciliationLogRepository
    extends
        JpaRepository<MembershipSubscriptionReconciliationLog, Long>, JpaSpecificationExecutor<MembershipSubscriptionReconciliationLog> {
    List<MembershipSubscriptionReconciliationLog> findBySubscriptionOrderByProcessedAtDesc(MembershipSubscription subscription);

    List<MembershipSubscriptionReconciliationLog> findByTenantIdOrderByProcessedAtDesc(String tenantId);

    List<MembershipSubscriptionReconciliationLog> findBySubscriptionIdOrderByProcessedAtDesc(Long subscriptionId);
}
