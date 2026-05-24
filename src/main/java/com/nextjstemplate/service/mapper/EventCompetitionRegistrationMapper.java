package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.EventCompetitionRegistration;
import com.nextjstemplate.service.dto.EventCompetitionRegistrationDTO;
import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.service.dto.EventDetailsDTO;
import com.nextjstemplate.domain.EventCompetition;
import com.nextjstemplate.service.dto.EventCompetitionDTO;
import com.nextjstemplate.domain.EventCompetitionParticipant;
import com.nextjstemplate.service.dto.EventCompetitionParticipantDTO;
import com.nextjstemplate.domain.EventCompetitionRegistration;
import com.nextjstemplate.service.dto.EventCompetitionRegistrationDTO;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventCompetitionRegistration} and its DTO {@link EventCompetitionRegistrationDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventCompetitionRegistrationMapper extends EntityMapper<EventCompetitionRegistrationDTO, EventCompetitionRegistration> {
    @Mapping(target = "event", source = "event", qualifiedByName = "eventDetailsId")
    @Mapping(target = "competition", source = "competition", qualifiedByName = "eventCompetitionId")
    @Mapping(target = "participantProfile", source = "participantProfile", qualifiedByName = "eventCompetitionParticipantId")
    @Mapping(target = "groupLeaderRegistration", source = "groupLeaderRegistration", qualifiedByName = "eventCompetitionRegistrationId")
    @Mapping(target = "registeredByUserProfile", source = "registeredByUserProfile", qualifiedByName = "registeredByUserProfileId")
    EventCompetitionRegistrationDTO toDto(EventCompetitionRegistration s);

    @Named("eventDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventDetailsDTO toDtoEventDetailsId(com.nextjstemplate.domain.EventDetails entity);
    @Named("eventCompetitionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventCompetitionDTO toDtoEventCompetitionId(com.nextjstemplate.domain.EventCompetition entity);
    @Named("eventCompetitionParticipantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventCompetitionParticipantDTO toDtoEventCompetitionParticipantId(com.nextjstemplate.domain.EventCompetitionParticipant entity);
    @Named("eventCompetitionRegistrationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventCompetitionRegistrationDTO toDtoEventCompetitionRegistrationId(com.nextjstemplate.domain.EventCompetitionRegistration entity);
    @Named("registeredByUserProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoRegisteredByUserProfileId(com.nextjstemplate.domain.UserProfile entity);
}
