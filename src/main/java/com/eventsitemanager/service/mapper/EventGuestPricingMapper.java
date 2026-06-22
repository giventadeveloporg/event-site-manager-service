package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.EventDetails;
import com.eventsitemanager.domain.EventGuestPricing;
import com.eventsitemanager.service.dto.EventDetailsDTO;
import com.eventsitemanager.service.dto.EventGuestPricingDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventGuestPricing} and its DTO {@link EventGuestPricingDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventGuestPricingMapper extends EntityMapper<EventGuestPricingDTO, EventGuestPricing> {
    @Mapping(target = "event", source = "event", qualifiedByName = "eventDetailsId")
    EventGuestPricingDTO toDto(EventGuestPricing s);

    @Named("eventDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventDetailsDTO toDtoEventDetailsId(EventDetails eventDetails);
}
