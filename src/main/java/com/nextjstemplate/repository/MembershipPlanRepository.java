package com.nextjstemplate.repository;

import com.nextjstemplate.domain.MembershipPlan;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
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
}
