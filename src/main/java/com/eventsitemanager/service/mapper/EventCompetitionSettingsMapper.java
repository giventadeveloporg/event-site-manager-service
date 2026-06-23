package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.EventCompetitionSettings;
import com.eventsitemanager.domain.EventDetails;
import com.eventsitemanager.service.dto.EventCompetitionSettingsDTO;
import com.eventsitemanager.service.dto.EventDetailsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventCompetitionSettings} and its DTO {@link EventCompetitionSettingsDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventCompetitionSettingsMapper extends EntityMapper<EventCompetitionSettingsDTO, EventCompetitionSettings> {
    @Mapping(target = "event", source = "event", qualifiedByName = "eventDetailsId")
    EventCompetitionSettingsDTO toDto(EventCompetitionSettings s);

    @Named("eventDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventDetailsDTO toDtoEventDetailsId(com.eventsitemanager.domain.EventDetails entity);
}
