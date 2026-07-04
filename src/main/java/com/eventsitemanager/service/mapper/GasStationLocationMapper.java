package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.GasStationLocation;
import com.eventsitemanager.service.dto.GasStationLocationDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GasStationLocationMapper extends EntityMapper<GasStationLocationDTO, GasStationLocation> {}
