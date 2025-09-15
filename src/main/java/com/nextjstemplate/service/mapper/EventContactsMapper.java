package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.EventContacts;
import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.service.dto.EventContactsDTO;
import com.nextjstemplate.service.dto.EventDetailsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventContacts} and its DTO
 * {@link EventContactsDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventContactsMapper extends EntityMapper<EventContactsDTO, EventContacts> {
  @Mapping(target = "event", source = "event", qualifiedByName = "eventDetailsId")
  EventContactsDTO toDto(EventContacts s);

  @Named("eventDetailsId")
  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id")
  EventDetailsDTO toDtoEventDetailsId(EventDetails eventDetails);
}
