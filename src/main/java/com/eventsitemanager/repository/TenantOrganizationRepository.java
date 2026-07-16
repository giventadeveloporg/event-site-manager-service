package com.eventsitemanager.repository;

import com.eventsitemanager.domain.TenantOrganization;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TenantOrganization entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TenantOrganizationRepository
    extends JpaRepository<TenantOrganization, Long>, JpaSpecificationExecutor<TenantOrganization> {
    /** All tenant IDs only — lets the frontend derive the next tenant sequence without paging whole rows. */
    @Query("SELECT t.tenantId FROM TenantOrganization t")
    java.util.List<String> findAllTenantIds();
}
