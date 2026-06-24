package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.DiscountCode;
import com.eventsitemanager.domain.EventDetails;
import com.eventsitemanager.domain.EventTypeDetails;
import com.eventsitemanager.domain.UserProfile;
import com.eventsitemanager.service.dto.DiscountCodeDTO;
import com.eventsitemanager.service.dto.EventDetailsDTO;
import com.eventsitemanager.service.dto.EventTypeDetailsDTO;
import com.eventsitemanager.service.dto.UserProfileDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventDetails} and its DTO {@link EventDetailsDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventDetailsMapper extends EntityMapper<EventDetailsDTO, EventDetails> {
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "userProfileId")
    @Mapping(target = "eventType", source = "eventType", qualifiedByName = "eventTypeDetailsId")
    @Mapping(target = "discountCodes", source = "discountCodes", qualifiedByName = "discountCodeIdSet")
    @Mapping(target = "parentEventId", source = "parentEvent.id")
    EventDetailsDTO toDto(EventDetails s);

    @Mapping(target = "removeDiscountCodes", ignore = true)
    @Mapping(target = "parentEvent", ignore = true)
    @Mapping(target = "childEvents", ignore = true)
    EventDetails toEntity(EventDetailsDTO eventDetailsDTO);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);

    @Named("eventTypeDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventTypeDetailsDTO toDtoEventTypeDetailsId(EventTypeDetails eventTypeDetails);

    @Named("discountCodeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DiscountCodeDTO toDtoDiscountCodeId(DiscountCode discountCode);

    @Named("discountCodeIdSet")
    default Set<DiscountCodeDTO> toDtoDiscountCodeIdSet(Set<DiscountCode> discountCode) {
        return discountCode.stream().map(this::toDtoDiscountCodeId).collect(Collectors.toSet());
    }
}
