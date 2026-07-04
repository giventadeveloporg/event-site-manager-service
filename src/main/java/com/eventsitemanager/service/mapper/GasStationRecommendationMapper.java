package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.GasStationRecommendation;
import com.eventsitemanager.service.dto.GasStationRecommendationDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GasStationRecommendationMapper extends EntityMapper<GasStationRecommendationDTO, GasStationRecommendation> {}
