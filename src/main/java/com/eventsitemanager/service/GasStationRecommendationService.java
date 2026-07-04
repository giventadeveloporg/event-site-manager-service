package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.GasStationRecommendationDTO;
import java.util.Optional;

public interface GasStationRecommendationService {
    GasStationRecommendationDTO save(GasStationRecommendationDTO gasStationRecommendationDTO);
    GasStationRecommendationDTO update(GasStationRecommendationDTO gasStationRecommendationDTO);
    Optional<GasStationRecommendationDTO> partialUpdate(GasStationRecommendationDTO gasStationRecommendationDTO);
    Optional<GasStationRecommendationDTO> findOne(Long id);
    void delete(Long id);
}
