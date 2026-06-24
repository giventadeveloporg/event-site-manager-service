package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.EventCompetition;
import com.eventsitemanager.domain.EventCompetitionParticipant;
import com.eventsitemanager.domain.EventCompetitionRegistration;
import com.eventsitemanager.domain.EventCompetitionRegistration;
import com.eventsitemanager.domain.EventDetails;
import com.eventsitemanager.domain.UserProfile;
import com.eventsitemanager.service.dto.EventCompetitionDTO;
import com.eventsitemanager.service.dto.EventCompetitionParticipantDTO;
import com.eventsitemanager.service.dto.EventCompetitionRegistrationDTO;
import com.eventsitemanager.service.dto.EventCompetitionRegistrationDTO;
import com.eventsitemanager.service.dto.EventDetailsDTO;
import com.eventsitemanager.service.dto.UserProfileDTO;
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
    EventDetailsDTO toDtoEventDetailsId(com.eventsitemanager.domain.EventDetails entity);

    @Named("eventCompetitionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventCompetitionDTO toDtoEventCompetitionId(com.eventsitemanager.domain.EventCompetition entity);

    @Named("eventCompetitionParticipantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventCompetitionParticipantDTO toDtoEventCompetitionParticipantId(com.eventsitemanager.domain.EventCompetitionParticipant entity);

    @Named("eventCompetitionRegistrationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventCompetitionRegistrationDTO toDtoEventCompetitionRegistrationId(com.eventsitemanager.domain.EventCompetitionRegistration entity);

    @Named("registeredByUserProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoRegisteredByUserProfileId(com.eventsitemanager.domain.UserProfile entity);
}
