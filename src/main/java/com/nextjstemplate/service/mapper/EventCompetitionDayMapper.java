package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.EventCompetitionDay;
import com.nextjstemplate.service.dto.EventCompetitionDayDTO;
import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.service.dto.EventDetailsDTO;
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
    EventDetailsDTO toDtoEventDetailsId(com.nextjstemplate.domain.EventDetails entity);
}
