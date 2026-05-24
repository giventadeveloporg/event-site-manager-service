package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.EventCompetitionContentBlock;
import com.nextjstemplate.service.dto.EventCompetitionContentBlockDTO;
import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.service.dto.EventDetailsDTO;
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
    EventDetailsDTO toDtoEventDetailsId(com.nextjstemplate.domain.EventDetails entity);
}
