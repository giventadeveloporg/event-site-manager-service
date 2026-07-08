package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.GasStationUserStationAssignment;
import com.eventsitemanager.service.dto.GasStationUserStationAssignmentDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GasStationUserStationAssignmentMapper
    extends EntityMapper<GasStationUserStationAssignmentDTO, GasStationUserStationAssignment> {}
