package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.EventCompetition;
import com.nextjstemplate.service.dto.EventCompetitionDTO;
import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.service.dto.EventDetailsDTO;
import com.nextjstemplate.domain.EventCompetitionDay;
import com.nextjstemplate.service.dto.EventCompetitionDayDTO;
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
    EventDetailsDTO toDtoEventDetailsId(com.nextjstemplate.domain.EventDetails entity);
    @Named("eventCompetitionDayId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventCompetitionDayDTO toDtoEventCompetitionDayId(com.nextjstemplate.domain.EventCompetitionDay entity);
}
