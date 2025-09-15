package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.domain.EventEmails;
import com.nextjstemplate.service.dto.EventDetailsDTO;
import com.nextjstemplate.service.dto.EventEmailsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventEmails} and its DTO {@link EventEmailsDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventEmailsMapper extends EntityMapper<EventEmailsDTO, EventEmails> {
  @Mapping(target = "event", source = "event", qualifiedByName = "eventDetailsId")
  EventEmailsDTO toDto(EventEmails s);

  @Named("eventDetailsId")
  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id")
  EventDetailsDTO toDtoEventDetailsId(EventDetails eventDetails);
}
