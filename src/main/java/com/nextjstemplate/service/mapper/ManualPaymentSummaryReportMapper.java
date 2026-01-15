package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.ManualPaymentSummaryReport;
import com.nextjstemplate.service.dto.ManualPaymentSummaryReportDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link ManualPaymentSummaryReport} and its DTO {@link ManualPaymentSummaryReportDTO}.
 */
@Mapper(componentModel = "spring")
public interface ManualPaymentSummaryReportMapper extends EntityMapper<ManualPaymentSummaryReportDTO, ManualPaymentSummaryReport> {}
