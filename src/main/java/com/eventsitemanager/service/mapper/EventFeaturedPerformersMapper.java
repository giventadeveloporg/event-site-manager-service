package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.EventDetails;
import com.eventsitemanager.domain.EventFeaturedPerformers;
import com.eventsitemanager.service.dto.EventDetailsDTO;
import com.eventsitemanager.service.dto.EventFeaturedPerformersDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventFeaturedPerformers} and its DTO
 * {@link EventFeaturedPerformersDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventFeaturedPerformersMapper extends EntityMapper<EventFeaturedPerformersDTO, EventFeaturedPerformers> {
    @Mapping(target = "event", source = "event", qualifiedByName = "eventDetailsId")
    EventFeaturedPerformersDTO toDto(EventFeaturedPerformers s);

    @Named("eventDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventDetailsDTO toDtoEventDetailsId(EventDetails eventDetails);
}
