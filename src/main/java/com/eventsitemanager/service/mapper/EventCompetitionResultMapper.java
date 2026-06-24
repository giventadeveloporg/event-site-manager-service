package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.EventCompetition;
import com.eventsitemanager.domain.EventCompetitionParticipant;
import com.eventsitemanager.domain.EventCompetitionRegistration;
import com.eventsitemanager.domain.EventCompetitionResult;
import com.eventsitemanager.domain.EventDetails;
import com.eventsitemanager.domain.EventMedia;
import com.eventsitemanager.service.dto.EventCompetitionDTO;
import com.eventsitemanager.service.dto.EventCompetitionParticipantDTO;
import com.eventsitemanager.service.dto.EventCompetitionRegistrationDTO;
import com.eventsitemanager.service.dto.EventCompetitionResultDTO;
import com.eventsitemanager.service.dto.EventDetailsDTO;
import com.eventsitemanager.service.dto.EventMediaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventCompetitionResult} and its DTO {@link EventCompetitionResultDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventCompetitionResultMapper extends EntityMapper<EventCompetitionResultDTO, EventCompetitionResult> {
    @Mapping(target = "event", source = "event", qualifiedByName = "eventDetailsId")
    @Mapping(target = "competition", source = "competition", qualifiedByName = "eventCompetitionId")
    @Mapping(target = "participantProfile", source = "participantProfile", qualifiedByName = "eventCompetitionParticipantId")
    @Mapping(target = "registration", source = "registration", qualifiedByName = "eventCompetitionRegistrationId")
    @Mapping(target = "winnerMedia", source = "winnerMedia", qualifiedByName = "eventMediaId")
    EventCompetitionResultDTO toDto(EventCompetitionResult s);

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

    @Named("eventMediaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventMediaDTO toDtoEventMediaId(com.eventsitemanager.domain.EventMedia entity);
}
