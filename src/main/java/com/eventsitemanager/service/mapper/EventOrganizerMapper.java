package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.EventDetails;
import com.eventsitemanager.domain.EventOrganizer;
import com.eventsitemanager.domain.UserProfile;
import com.eventsitemanager.service.dto.EventDetailsDTO;
import com.eventsitemanager.service.dto.EventOrganizerDTO;
import com.eventsitemanager.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventOrganizer} and its DTO {@link EventOrganizerDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventOrganizerMapper extends EntityMapper<EventOrganizerDTO, EventOrganizer> {
    @Mapping(target = "event", source = "event", qualifiedByName = "eventDetailsId")
    @Mapping(target = "organizer", source = "organizer", qualifiedByName = "userProfileId")
    EventOrganizerDTO toDto(EventOrganizer s);

    @Named("eventDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventDetailsDTO toDtoEventDetailsId(EventDetails eventDetails);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
