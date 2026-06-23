package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.EventDetails;
import com.eventsitemanager.domain.EventTicketTransaction;
import com.eventsitemanager.domain.EventTicketType;
import com.eventsitemanager.domain.UserProfile;
import com.eventsitemanager.service.dto.EventDetailsDTO;
import com.eventsitemanager.service.dto.EventTicketTransactionDTO;
import com.eventsitemanager.service.dto.EventTicketTypeDTO;
import com.eventsitemanager.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventTicketTransaction} and its DTO {@link EventTicketTransactionDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventTicketTransactionMapper extends EntityMapper<EventTicketTransactionDTO, EventTicketTransaction> {
    /* @Mapping(target = "event", source = "event", qualifiedByName = "eventDetailsId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userProfileId")*/
    EventTicketTransactionDTO toDto(EventTicketTransaction s);

    @Named("eventDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventDetailsDTO toDtoEventDetailsId(EventDetails eventDetails);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
