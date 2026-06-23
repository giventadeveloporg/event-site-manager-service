package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.ManualPaymentSummaryReport;
import com.eventsitemanager.service.dto.ManualPaymentSummaryReportDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link ManualPaymentSummaryReport} and its DTO {@link ManualPaymentSummaryReportDTO}.
 */
@Mapper(componentModel = "spring")
public interface ManualPaymentSummaryReportMapper extends EntityMapper<ManualPaymentSummaryReportDTO, ManualPaymentSummaryReport> {}
