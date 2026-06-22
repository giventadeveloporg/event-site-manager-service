package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.EventScoreCard;
import com.eventsitemanager.domain.EventScoreCardDetail;
import com.eventsitemanager.service.dto.EventScoreCardDTO;
import com.eventsitemanager.service.dto.EventScoreCardDetailDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventScoreCardDetail} and its DTO {@link EventScoreCardDetailDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventScoreCardDetailMapper extends EntityMapper<EventScoreCardDetailDTO, EventScoreCardDetail> {
    @Mapping(target = "scoreCard", source = "scoreCard", qualifiedByName = "eventScoreCardId")
    EventScoreCardDetailDTO toDto(EventScoreCardDetail s);

    @Named("eventScoreCardId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventScoreCardDTO toDtoEventScoreCardId(EventScoreCard eventScoreCard);
}
