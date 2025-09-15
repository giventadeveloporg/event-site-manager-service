package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.EventSponsors;
import com.nextjstemplate.service.dto.EventSponsorsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventSponsors} and its DTO
 * {@link EventSponsorsDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventSponsorsMapper extends EntityMapper<EventSponsorsDTO, EventSponsors> {
  EventSponsorsDTO toDto(EventSponsors s);
}
