package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.GasStationDailyMetrics;
import com.eventsitemanager.service.dto.GasStationDailyMetricsDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GasStationDailyMetricsMapper extends EntityMapper<GasStationDailyMetricsDTO, GasStationDailyMetrics> {}
