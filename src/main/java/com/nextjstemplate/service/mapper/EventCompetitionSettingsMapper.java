package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.EventCompetitionSettings;
import com.nextjstemplate.service.dto.EventCompetitionSettingsDTO;
import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.service.dto.EventDetailsDTO;
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
    EventDetailsDTO toDtoEventDetailsId(com.nextjstemplate.domain.EventDetails entity);
}
