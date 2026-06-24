package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.EventDetails;
import com.eventsitemanager.domain.EventProgramDirectors;
import com.eventsitemanager.service.dto.EventDetailsDTO;
import com.eventsitemanager.service.dto.EventProgramDirectorsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventProgramDirectors} and its DTO
 * {@link EventProgramDirectorsDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventProgramDirectorsMapper extends EntityMapper<EventProgramDirectorsDTO, EventProgramDirectors> {
    @Mapping(target = "event", source = "event", qualifiedByName = "eventDetailsId")
    EventProgramDirectorsDTO toDto(EventProgramDirectors s);

    @Named("eventDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventDetailsDTO toDtoEventDetailsId(EventDetails eventDetails);
}
