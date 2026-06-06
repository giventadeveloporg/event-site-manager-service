package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.EventCompetitionGroupMember;
import com.nextjstemplate.domain.EventCompetitionParticipant;
import com.nextjstemplate.domain.EventCompetitionRegistration;
import com.nextjstemplate.service.dto.EventCompetitionGroupMemberDTO;
import com.nextjstemplate.service.dto.EventCompetitionParticipantDTO;
import com.nextjstemplate.service.dto.EventCompetitionRegistrationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventCompetitionGroupMember} and its DTO {@link EventCompetitionGroupMemberDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventCompetitionGroupMemberMapper extends EntityMapper<EventCompetitionGroupMemberDTO, EventCompetitionGroupMember> {
    @Mapping(target = "registration", source = "registration", qualifiedByName = "eventCompetitionRegistrationId")
    @Mapping(target = "participantProfile", source = "participantProfile", qualifiedByName = "eventCompetitionParticipantId")
    EventCompetitionGroupMemberDTO toDto(EventCompetitionGroupMember s);

    @Named("eventCompetitionRegistrationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventCompetitionRegistrationDTO toDtoEventCompetitionRegistrationId(EventCompetitionRegistration entity);

    @Named("eventCompetitionParticipantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventCompetitionParticipantDTO toDtoEventCompetitionParticipantId(EventCompetitionParticipant entity);
}
