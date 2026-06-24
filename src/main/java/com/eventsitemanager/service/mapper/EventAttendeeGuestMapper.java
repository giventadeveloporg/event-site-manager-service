package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.EventAttendee;
import com.eventsitemanager.domain.EventAttendeeGuest;
import com.eventsitemanager.service.dto.EventAttendeeDTO;
import com.eventsitemanager.service.dto.EventAttendeeGuestDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventAttendeeGuest} and its DTO {@link EventAttendeeGuestDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventAttendeeGuestMapper extends EntityMapper<EventAttendeeGuestDTO, EventAttendeeGuest> {
    @Mapping(target = "primaryAttendee", source = "primaryAttendee", qualifiedByName = "eventAttendeeId")
    EventAttendeeGuestDTO toDto(EventAttendeeGuest s);

    @Named("eventAttendeeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventAttendeeDTO toDtoEventAttendeeId(EventAttendee eventAttendee);
}
