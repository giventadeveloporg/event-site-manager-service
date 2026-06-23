package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.EventPoll;
import com.eventsitemanager.domain.EventPollOption;
import com.eventsitemanager.service.dto.EventPollDTO;
import com.eventsitemanager.service.dto.EventPollOptionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventPollOption} and its DTO {@link EventPollOptionDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventPollOptionMapper extends EntityMapper<EventPollOptionDTO, EventPollOption> {
    @Mapping(target = "poll", source = "poll", qualifiedByName = "eventPollId")
    EventPollOptionDTO toDto(EventPollOption s);

    @Named("eventPollId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventPollDTO toDtoEventPollId(EventPoll eventPoll);
}
