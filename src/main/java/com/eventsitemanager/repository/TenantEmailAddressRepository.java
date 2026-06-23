package com.eventsitemanager.repository;

import com.eventsitemanager.domain.TenantEmailAddress;
import java.util.List;
import java.util.Optional;
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

    Optional<TenantEmailAddress> findByTenantIdAndIsDefaultTrue(String tenantId);

    List<TenantEmailAddress> findByTenantIdAndEmailType(String tenantId, com.eventsitemanager.domain.enumeration.TenantEmailType emailType);
}
