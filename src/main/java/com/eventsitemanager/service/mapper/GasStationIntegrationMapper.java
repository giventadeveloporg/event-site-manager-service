package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.GasStationIntegration;
import com.eventsitemanager.service.dto.GasStationIntegrationDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GasStationIntegrationMapper extends EntityMapper<GasStationIntegrationDTO, GasStationIntegration> {}
