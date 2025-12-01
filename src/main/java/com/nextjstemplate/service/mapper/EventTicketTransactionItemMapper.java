package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.EventTicketTransaction;
import com.nextjstemplate.domain.EventTicketTransactionItem;
import com.nextjstemplate.domain.EventTicketType;
import com.nextjstemplate.service.dto.EventTicketTransactionDTO;
import com.nextjstemplate.service.dto.EventTicketTransactionItemDTO;
import com.nextjstemplate.service.dto.EventTicketTypeDTO;
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
