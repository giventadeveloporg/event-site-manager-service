package com.nextjstemplate.web.rest;

import com.nextjstemplate.security.TenantContext;
import com.nextjstemplate.service.ManualPaymentSummaryReportQueryService;
import com.nextjstemplate.service.criteria.ManualPaymentSummaryReportCriteria;
import com.nextjstemplate.service.dto.ManualPaymentSummaryReportDTO;
import com.nextjstemplate.web.rest.errors.BadRequestAlertException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

/**
 * REST controller for reading {@link com.nextjstemplate.domain.ManualPaymentSummaryReport}.
 *
 * This table is written by the batch job and is exposed read-only for dashboards.
 */
@RestController
@RequestMapping("/api/manual-payment-summary")
public class ManualPaymentSummaryReportResource {

    private final Logger log = LoggerFactory.getLogger(ManualPaymentSummaryReportResource.class);

    private static final String ENTITY_NAME = "manualPaymentSummaryReport";

    private final ManualPaymentSummaryReportQueryService manualPaymentSummaryReportQueryService;

    public ManualPaymentSummaryReportResource(ManualPaymentSummaryReportQueryService manualPaymentSummaryReportQueryService) {
        this.manualPaymentSummaryReportQueryService = manualPaymentSummaryReportQueryService;
    }

    @GetMapping("")
    public ResponseEntity<List<ManualPaymentSummaryReportDTO>> getManualPaymentSummary(
        ManualPaymentSummaryReportCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ManualPaymentSummaryReport by criteria: {}", criteria);

        // Wildcard: when no tenant in context, do not add tenant filter (global search for admin dashboard)
        String tenantId = TenantContext.getCurrentTenant();
        if (criteria == null) {
            criteria = new ManualPaymentSummaryReportCriteria();
        }
        if (tenantId != null && !tenantId.isEmpty() && criteria.getTenantId() == null) {
            criteria.setTenantId(new tech.jhipster.service.filter.StringFilter());
            criteria.getTenantId().setEquals(tenantId);
        }

        Page<ManualPaymentSummaryReportDTO> page = manualPaymentSummaryReportQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countManualPaymentSummary(ManualPaymentSummaryReportCriteria criteria) {
        log.debug("REST request to count ManualPaymentSummaryReport by criteria: {}", criteria);

        // Wildcard: when no tenant in context, do not add tenant filter (global count for admin dashboard)
        String tenantId = TenantContext.getCurrentTenant();
        if (criteria == null) {
            criteria = new ManualPaymentSummaryReportCriteria();
        }
        if (tenantId != null && !tenantId.isEmpty() && criteria.getTenantId() == null) {
            criteria.setTenantId(new tech.jhipster.service.filter.StringFilter());
            criteria.getTenantId().setEquals(tenantId);
        }

        return ResponseEntity.ok().body(manualPaymentSummaryReportQueryService.countByCriteria(criteria));
    }

    private String requireTenantId() {
        String tenantId = TenantContext.getCurrentTenant();
        if (tenantId == null || tenantId.isBlank()) {
            throw new BadRequestAlertException("Tenant ID is required", ENTITY_NAME, "tenantIdRequired");
        }
        return tenantId;
    }
}
