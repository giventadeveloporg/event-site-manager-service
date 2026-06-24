package com.eventsitemanager.repository;

import com.eventsitemanager.domain.SatelliteDomain;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SatelliteDomain entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SatelliteDomainRepository extends JpaRepository<SatelliteDomain, Long>, JpaSpecificationExecutor<SatelliteDomain> {
    Optional<SatelliteDomain> findBySatelliteKey(String satelliteKey);
    Optional<SatelliteDomain> findByHostname(String hostname);
    List<SatelliteDomain> findByEnabledTrue();
    Optional<SatelliteDomain> findByTenantId(String tenantId);
}
