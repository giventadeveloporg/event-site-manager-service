package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.domain.EventTicketTransaction;
import com.nextjstemplate.domain.EventTicketType;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.service.dto.EventDetailsDTO;
import com.nextjstemplate.service.dto.EventTicketTransactionDTO;
import com.nextjstemplate.service.dto.EventTicketTypeDTO;
import com.nextjstemplate.service.dto.UserProfileDTO;
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
