package com.eventsitemanager.repository;

import com.eventsitemanager.domain.GasStationLocation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface GasStationLocationRepository
    extends JpaRepository<GasStationLocation, Long>, JpaSpecificationExecutor<GasStationLocation> {}
