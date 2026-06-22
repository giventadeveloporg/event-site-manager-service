package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.EventDetails;
import com.eventsitemanager.domain.EventScoreCard;
import com.eventsitemanager.service.dto.EventDetailsDTO;
import com.eventsitemanager.service.dto.EventScoreCardDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventScoreCard} and its DTO {@link EventScoreCardDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventScoreCardMapper extends EntityMapper<EventScoreCardDTO, EventScoreCard> {
    @Mapping(target = "event", source = "event", qualifiedByName = "eventDetailsId")
    EventScoreCardDTO toDto(EventScoreCard s);

    @Named("eventDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventDetailsDTO toDtoEventDetailsId(EventDetails eventDetails);
}
