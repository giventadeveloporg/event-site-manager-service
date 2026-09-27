package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.LastMatch;
import com.eventsitemanager.service.dto.LastMatchDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LastMatch} and its DTO {@link LastMatchDTO}.
 */
@Mapper(componentModel = "spring")
public interface LastMatchMapper extends EntityMapper<LastMatchDTO, LastMatch> {}
