package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.DonationTransaction;
import com.nextjstemplate.service.dto.DonationTransactionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DonationTransaction} and its DTO {@link DonationTransactionDTO}.
 */
@Mapper(componentModel = "spring")
public interface DonationTransactionMapper extends EntityMapper<DonationTransactionDTO, DonationTransaction> {
    DonationTransactionDTO toDto(DonationTransaction s);
}
