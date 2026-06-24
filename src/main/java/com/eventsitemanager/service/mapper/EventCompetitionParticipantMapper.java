package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.EventCompetitionParticipant;
import com.eventsitemanager.domain.UserProfile;
import com.eventsitemanager.domain.UserProfile;
import com.eventsitemanager.service.dto.EventCompetitionParticipantDTO;
import com.eventsitemanager.service.dto.UserProfileDTO;
import com.eventsitemanager.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventCompetitionParticipant} and its DTO {@link EventCompetitionParticipantDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventCompetitionParticipantMapper extends EntityMapper<EventCompetitionParticipantDTO, EventCompetitionParticipant> {
    @Mapping(target = "userProfile", source = "userProfile", qualifiedByName = "userProfileId")
    @Mapping(target = "guardianUserProfile", source = "guardianUserProfile", qualifiedByName = "guardianUserProfileId")
    EventCompetitionParticipantDTO toDto(EventCompetitionParticipant s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(com.eventsitemanager.domain.UserProfile entity);

    @Named("guardianUserProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoGuardianUserProfileId(com.eventsitemanager.domain.UserProfile entity);
}
