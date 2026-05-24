package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.EventCompetitionParticipant;
import com.nextjstemplate.service.dto.EventCompetitionParticipantDTO;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.service.dto.UserProfileDTO;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.service.dto.UserProfileDTO;
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
    UserProfileDTO toDtoUserProfileId(com.nextjstemplate.domain.UserProfile entity);
    @Named("guardianUserProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoGuardianUserProfileId(com.nextjstemplate.domain.UserProfile entity);
}
