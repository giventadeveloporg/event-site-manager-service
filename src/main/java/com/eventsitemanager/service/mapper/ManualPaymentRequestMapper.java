package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.ManualPaymentRequest;
import com.eventsitemanager.service.dto.ManualPaymentRequestDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ManualPaymentRequest} and its DTO {@link ManualPaymentRequestDTO}.
 */
@Mapper(componentModel = "spring")
public interface ManualPaymentRequestMapper extends EntityMapper<ManualPaymentRequestDTO, ManualPaymentRequest> {
    // MapStruct will generate implementation; keep partialUpdate consistent with other mappers.
    void partialUpdate(@MappingTarget ManualPaymentRequest entity, ManualPaymentRequestDTO dto);
}
