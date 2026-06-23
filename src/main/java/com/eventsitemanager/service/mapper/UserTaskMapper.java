package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.EventDetails;
import com.eventsitemanager.domain.UserProfile;
import com.eventsitemanager.domain.UserTask;
import com.eventsitemanager.service.dto.EventDetailsDTO;
import com.eventsitemanager.service.dto.UserProfileDTO;
import com.eventsitemanager.service.dto.UserTaskDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserTask} and its DTO {@link UserTaskDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserTaskMapper extends EntityMapper<UserTaskDTO, UserTask> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userProfileId")
    @Mapping(target = "event", source = "event", qualifiedByName = "eventDetailsId")
    UserTaskDTO toDto(UserTask s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);

    @Named("eventDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventDetailsDTO toDtoEventDetailsId(EventDetails eventDetails);
}
