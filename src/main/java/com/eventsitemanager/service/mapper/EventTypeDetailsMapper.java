package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.EventTypeDetails;
import com.eventsitemanager.service.dto.EventTypeDetailsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventTypeDetails} and its DTO {@link EventTypeDetailsDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventTypeDetailsMapper extends EntityMapper<EventTypeDetailsDTO, EventTypeDetails> {}
