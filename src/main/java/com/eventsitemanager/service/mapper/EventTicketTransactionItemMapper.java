package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.EventTicketTransaction;
import com.eventsitemanager.domain.EventTicketTransactionItem;
import com.eventsitemanager.domain.EventTicketType;
import com.eventsitemanager.service.dto.EventTicketTransactionDTO;
import com.eventsitemanager.service.dto.EventTicketTransactionItemDTO;
import com.eventsitemanager.service.dto.EventTicketTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventTicketTransactionItem} and its DTO {@link EventTicketTransactionItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventTicketTransactionItemMapper extends EntityMapper<EventTicketTransactionItemDTO, EventTicketTransactionItem> {
    /*@Mapping(target = "transaction", source = "transaction", qualifiedByName = "eventTicketTransactionId")
    @Mapping(target = "ticketType", source = "ticketType", qualifiedByName = "eventTicketTypeId")*/
    @Mapping(target = "tenantId", source = "tenantId") // Explicit mapping to ensure tenantId is mapped
    EventTicketTransactionItemDTO toDto(EventTicketTransactionItem s);

    @Named("eventTicketTransactionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventTicketTransactionDTO toDtoEventTicketTransactionId(EventTicketTransaction eventTicketTransaction);

    @Named("eventTicketTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventTicketTypeDTO toDtoEventTicketTypeId(EventTicketType eventTicketType);
}
