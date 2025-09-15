package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.domain.EventProgramDirectors;
import com.nextjstemplate.service.dto.EventDetailsDTO;
import com.nextjstemplate.service.dto.EventProgramDirectorsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventProgramDirectors} and its DTO
 * {@link EventProgramDirectorsDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventProgramDirectorsMapper extends EntityMapper<EventProgramDirectorsDTO, EventProgramDirectors> {
  @Mapping(target = "event", source = "event", qualifiedByName = "eventDetailsId")
  EventProgramDirectorsDTO toDto(EventProgramDirectors s);

  @Named("eventDetailsId")
  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id")
  EventDetailsDTO toDtoEventDetailsId(EventDetails eventDetails);
}
