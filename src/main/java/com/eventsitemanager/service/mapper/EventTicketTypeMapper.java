package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.EventDetails;
import com.eventsitemanager.domain.EventTicketType;
import com.eventsitemanager.service.dto.EventDetailsDTO;
import com.eventsitemanager.service.dto.EventTicketTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventTicketType} and its DTO {@link EventTicketTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventTicketTypeMapper extends EntityMapper<EventTicketTypeDTO, EventTicketType> {
    @Mapping(target = "event", source = "event", qualifiedByName = "eventDetailsId")
    EventTicketTypeDTO toDto(EventTicketType s);

    @Named("eventDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventDetailsDTO toDtoEventDetailsId(EventDetails eventDetails);
}
