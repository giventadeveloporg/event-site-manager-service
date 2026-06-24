package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.DonationTransaction;
import com.eventsitemanager.service.dto.DonationTransactionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DonationTransaction} and its DTO {@link DonationTransactionDTO}.
 */
@Mapper(componentModel = "spring")
public interface DonationTransactionMapper extends EntityMapper<DonationTransactionDTO, DonationTransaction> {
    DonationTransactionDTO toDto(DonationTransaction s);
}
