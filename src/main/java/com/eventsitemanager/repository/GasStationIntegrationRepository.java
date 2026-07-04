package com.eventsitemanager.repository;

import com.eventsitemanager.domain.GasStationIntegration;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface GasStationIntegrationRepository
    extends JpaRepository<GasStationIntegration, Long>, JpaSpecificationExecutor<GasStationIntegration> {}
