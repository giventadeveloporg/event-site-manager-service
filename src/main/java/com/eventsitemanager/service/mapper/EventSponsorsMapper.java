package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.EventSponsors;
import com.eventsitemanager.service.dto.EventSponsorsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventSponsors} and its DTO
 * {@link EventSponsorsDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventSponsorsMapper extends EntityMapper<EventSponsorsDTO, EventSponsors> {
    EventSponsorsDTO toDto(EventSponsors s);
}
