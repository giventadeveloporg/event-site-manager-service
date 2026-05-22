package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.EventCompetitionResult;
import com.nextjstemplate.service.dto.EventCompetitionResultDTO;
import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.service.dto.EventDetailsDTO;
import com.nextjstemplate.domain.EventCompetition;
import com.nextjstemplate.service.dto.EventCompetitionDTO;
import com.nextjstemplate.domain.EventCompetitionParticipant;
import com.nextjstemplate.service.dto.EventCompetitionParticipantDTO;
import com.nextjstemplate.domain.EventCompetitionRegistration;
import com.nextjstemplate.service.dto.EventCompetitionRegistrationDTO;
import com.nextjstemplate.domain.EventMedia;
import com.nextjstemplate.service.dto.EventMediaDTO;
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
    @Named("eventMediaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventMediaDTO toDtoEventMediaId(com.nextjstemplate.domain.EventMedia entity);
}
