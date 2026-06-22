package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.EventPoll;
import com.eventsitemanager.domain.EventPollOption;
import com.eventsitemanager.domain.EventPollResponse;
import com.eventsitemanager.domain.UserProfile;
import com.eventsitemanager.service.dto.EventPollDTO;
import com.eventsitemanager.service.dto.EventPollOptionDTO;
import com.eventsitemanager.service.dto.EventPollResponseDTO;
import com.eventsitemanager.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventPollResponse} and its DTO {@link EventPollResponseDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventPollResponseMapper extends EntityMapper<EventPollResponseDTO, EventPollResponse> {
    @Mapping(target = "poll", source = "poll", qualifiedByName = "eventPollId")
    @Mapping(target = "pollOption", source = "pollOption", qualifiedByName = "eventPollOptionId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userProfileId")
    EventPollResponseDTO toDto(EventPollResponse s);

    @Named("eventPollId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventPollDTO toDtoEventPollId(EventPoll eventPoll);

    @Named("eventPollOptionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventPollOptionDTO toDtoEventPollOptionId(EventPollOption eventPollOption);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
