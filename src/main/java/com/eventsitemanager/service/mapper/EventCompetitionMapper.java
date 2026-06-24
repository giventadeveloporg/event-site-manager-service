package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.EventCompetition;
import com.eventsitemanager.domain.EventCompetitionDay;
import com.eventsitemanager.domain.EventDetails;
import com.eventsitemanager.service.dto.EventCompetitionDTO;
import com.eventsitemanager.service.dto.EventCompetitionDayDTO;
import com.eventsitemanager.service.dto.EventDetailsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventCompetition} and its DTO {@link EventCompetitionDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventCompetitionMapper extends EntityMapper<EventCompetitionDTO, EventCompetition> {
    @Mapping(target = "event", source = "event", qualifiedByName = "eventDetailsId")
    @Mapping(target = "competitionDay", source = "competitionDay", qualifiedByName = "eventCompetitionDayId")
    EventCompetitionDTO toDto(EventCompetition s);

    @Named("eventDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventDetailsDTO toDtoEventDetailsId(com.eventsitemanager.domain.EventDetails entity);

    @Named("eventCompetitionDayId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventCompetitionDayDTO toDtoEventCompetitionDayId(com.eventsitemanager.domain.EventCompetitionDay entity);
}
