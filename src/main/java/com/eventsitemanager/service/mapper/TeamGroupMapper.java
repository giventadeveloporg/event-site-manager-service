package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.TeamGroup;
import com.eventsitemanager.service.dto.TeamGroupDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TeamGroup} and its DTO {@link TeamGroupDTO}.
 */
@Mapper(componentModel = "spring")
public interface TeamGroupMapper extends EntityMapper<TeamGroupDTO, TeamGroup> {}
