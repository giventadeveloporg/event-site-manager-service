package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.EventCompetitionContentBlock;
import com.eventsitemanager.domain.EventDetails;
import com.eventsitemanager.service.dto.EventCompetitionContentBlockDTO;
import com.eventsitemanager.service.dto.EventDetailsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventCompetitionContentBlock} and its DTO {@link EventCompetitionContentBlockDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventCompetitionContentBlockMapper extends EntityMapper<EventCompetitionContentBlockDTO, EventCompetitionContentBlock> {
    @Mapping(target = "event", source = "event", qualifiedByName = "eventDetailsId")
    EventCompetitionContentBlockDTO toDto(EventCompetitionContentBlock s);

    @Named("eventDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventDetailsDTO toDtoEventDetailsId(com.eventsitemanager.domain.EventDetails entity);
}
