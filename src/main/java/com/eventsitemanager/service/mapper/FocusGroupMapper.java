package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.FocusGroup;
import com.eventsitemanager.service.dto.FocusGroupDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FocusGroup} and its DTO {@link FocusGroupDTO}.
 */
@Mapper(componentModel = "spring")
public interface FocusGroupMapper extends EntityMapper<FocusGroupDTO, FocusGroup> {}
