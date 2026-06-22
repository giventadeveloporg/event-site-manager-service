package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.EventDetails;
import com.eventsitemanager.domain.EventMedia;
import com.eventsitemanager.domain.UserProfile;
import com.eventsitemanager.service.dto.EventDetailsDTO;
import com.eventsitemanager.service.dto.EventMediaDTO;
import com.eventsitemanager.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventMedia} and its DTO {@link EventMediaDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventMediaMapper extends EntityMapper<EventMediaDTO, EventMedia> {
    /*@Mapping(target = "event", source = "event", qualifiedByName = "eventDetailsId")
    @Mapping(target = "uploadedBy", source = "uploadedBy", qualifiedByName = "userProfileId")*/
    EventMediaDTO toDto(EventMedia s);

    @Named("eventDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventDetailsDTO toDtoEventDetailsId(EventDetails eventDetails);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
