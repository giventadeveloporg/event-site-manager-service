package com.eventsitemanager.repository;

import com.eventsitemanager.domain.GasStationRecommendation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface GasStationRecommendationRepository
    extends JpaRepository<GasStationRecommendation, Long>, JpaSpecificationExecutor<GasStationRecommendation> {}
