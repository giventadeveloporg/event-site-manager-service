package com.eventsitemanager.repository;

import com.eventsitemanager.domain.TenantEmailAddress;
import com.eventsitemanager.domain.enumeration.TenantEmailType;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TenantEmailAddress entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TenantEmailAddressRepository
    extends JpaRepository<TenantEmailAddress, Long>, JpaSpecificationExecutor<TenantEmailAddress> {
    List<TenantEmailAddress> findByTenantId(String tenantId);

    List<TenantEmailAddress> findByTenantIdAndIsActive(String tenantId, Boolean isActive);

    List<TenantEmailAddress> findByTenantIdAndEmailTypeAndIsDefaultTrue(String tenantId, TenantEmailType emailType);

    List<TenantEmailAddress> findByTenantIdAndEmailType(String tenantId, TenantEmailType emailType);
}
