package com.eventsitemanager.repository;

import com.eventsitemanager.domain.ManualPaymentSummaryReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ManualPaymentSummaryReport entity.
 */
@Repository
public interface ManualPaymentSummaryReportRepository
    extends JpaRepository<ManualPaymentSummaryReport, Long>, JpaSpecificationExecutor<ManualPaymentSummaryReport> {}
