package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.domain.EventFeaturedPerformers;
import com.nextjstemplate.service.dto.EventDetailsDTO;
import com.nextjstemplate.service.dto.EventFeaturedPerformersDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventFeaturedPerformers} and its DTO
 * {@link EventFeaturedPerformersDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventFeaturedPerformersMapper
    extends EntityMapper<EventFeaturedPerformersDTO, EventFeaturedPerformers> {
  @Mapping(target = "event", source = "event", qualifiedByName = "eventDetailsId")
  EventFeaturedPerformersDTO toDto(EventFeaturedPerformers s);

  @Named("eventDetailsId")
  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id")
  EventDetailsDTO toDtoEventDetailsId(EventDetails eventDetails);
}
