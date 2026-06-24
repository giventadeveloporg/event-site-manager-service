package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.EventCompetitionDay;
import com.eventsitemanager.domain.EventDetails;
import com.eventsitemanager.service.dto.EventCompetitionDayDTO;
import com.eventsitemanager.service.dto.EventDetailsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventCompetitionDay} and its DTO {@link EventCompetitionDayDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventCompetitionDayMapper extends EntityMapper<EventCompetitionDayDTO, EventCompetitionDay> {
    @Mapping(target = "event", source = "event", qualifiedByName = "eventDetailsId")
    EventCompetitionDayDTO toDto(EventCompetitionDay s);

    @Named("eventDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventDetailsDTO toDtoEventDetailsId(com.eventsitemanager.domain.EventDetails entity);
}
