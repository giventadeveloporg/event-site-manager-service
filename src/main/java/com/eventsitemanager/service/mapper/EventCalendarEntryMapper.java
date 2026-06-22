package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.EventCalendarEntry;
import com.eventsitemanager.domain.EventDetails;
import com.eventsitemanager.domain.UserProfile;
import com.eventsitemanager.service.dto.EventCalendarEntryDTO;
import com.eventsitemanager.service.dto.EventDetailsDTO;
import com.eventsitemanager.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventCalendarEntry} and its DTO {@link EventCalendarEntryDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventCalendarEntryMapper extends EntityMapper<EventCalendarEntryDTO, EventCalendarEntry> {
    @Mapping(target = "event", source = "event", qualifiedByName = "eventDetailsId")
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "userProfileId")
    EventCalendarEntryDTO toDto(EventCalendarEntry s);

    @Named("eventDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventDetailsDTO toDtoEventDetailsId(EventDetails eventDetails);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
