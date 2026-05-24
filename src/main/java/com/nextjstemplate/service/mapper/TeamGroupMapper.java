package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.TeamGroup;
import com.nextjstemplate.service.dto.TeamGroupDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TeamGroup} and its DTO {@link TeamGroupDTO}.
 */
@Mapper(componentModel = "spring")
public interface TeamGroupMapper extends EntityMapper<TeamGroupDTO, TeamGroup> {}
