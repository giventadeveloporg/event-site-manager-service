package com.nextjstemplate.web.rest;

import com.nextjstemplate.service.PromotionEmailSentLogQueryService;
import com.nextjstemplate.service.PromotionEmailSentLogService;
import com.nextjstemplate.service.criteria.PromotionEmailSentLogCriteria;
import com.nextjstemplate.service.dto.PromotionEmailSentLogDTO;
import com.nextjstemplate.web.rest.errors.BadRequestAlertException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.nextjstemplate.domain.PromotionEmailSentLog}.
 */
@RestController
@RequestMapping("/api/promotion-email-sent-logs")
public class PromotionEmailSentLogResource {

    private final Logger log = LoggerFactory.getLogger(PromotionEmailSentLogResource.class);

    private static final String ENTITY_NAME = "promotionEmailSentLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PromotionEmailSentLogService promotionEmailSentLogService;

    private final PromotionEmailSentLogQueryService promotionEmailSentLogQueryService;

    public PromotionEmailSentLogResource(
        PromotionEmailSentLogService promotionEmailSentLogService,
        PromotionEmailSentLogQueryService promotionEmailSentLogQueryService
    ) {
        this.promotionEmailSentLogService = promotionEmailSentLogService;
        this.promotionEmailSentLogQueryService = promotionEmailSentLogQueryService;
    }

    /**
     * {@code GET  /promotion-email-sent-logs} : get all the promotion email sent logs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of promotion email sent logs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PromotionEmailSentLogDTO>> getAllPromotionEmailSentLogs(
        PromotionEmailSentLogCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get PromotionEmailSentLogs by criteria: {}", criteria);

        // Wildcard: when no tenant in context, do not add tenant filter (global search for admin dashboard)
        String tenantId = com.nextjstemplate.security.TenantContext.getCurrentTenant();
        if (criteria == null) {
            criteria = new PromotionEmailSentLogCriteria();
        }
        if (tenantId != null && !tenantId.isEmpty() && criteria.getTenantId() == null) {
            criteria.setTenantId(new tech.jhipster.service.filter.StringFilter());
            criteria.getTenantId().setEquals(tenantId);
        }

        Page<PromotionEmailSentLogDTO> page = promotionEmailSentLogQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /promotion-email-sent-logs/count} : count all the promotion email sent logs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPromotionEmailSentLogs(PromotionEmailSentLogCriteria criteria) {
        log.debug("REST request to count PromotionEmailSentLogs by criteria: {}", criteria);

        // Wildcard: when no tenant in context, do not add tenant filter (global count for admin dashboard)
        String tenantId = com.nextjstemplate.security.TenantContext.getCurrentTenant();
        if (criteria == null) {
            criteria = new PromotionEmailSentLogCriteria();
        }
        if (tenantId != null && !tenantId.isEmpty() && criteria.getTenantId() == null) {
            criteria.setTenantId(new tech.jhipster.service.filter.StringFilter());
            criteria.getTenantId().setEquals(tenantId);
        }

        return ResponseEntity.ok().body(promotionEmailSentLogQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /promotion-email-sent-logs/:id} : get the "id" promotion email sent log.
     *
     * @param id the id of the promotionEmailSentLogDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the promotionEmailSentLogDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PromotionEmailSentLogDTO> getPromotionEmailSentLog(@PathVariable Long id) {
        log.debug("REST request to get PromotionEmailSentLog : {}", id);

        String tenantId = getTenantId();
        Optional<PromotionEmailSentLogDTO> promotionEmailSentLogDTO = promotionEmailSentLogService.findOne(id, tenantId);
        return ResponseUtil.wrapOrNotFound(promotionEmailSentLogDTO);
    }

    /**
     * {@code GET  /promotion-email-sent-logs/statistics/:eventId} : get email statistics for an event.
     *
     * @param eventId the event ID.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and statistics in body.
     */
    @GetMapping("/statistics/{eventId}")
    public ResponseEntity<Map<String, Object>> getEmailStatistics(@PathVariable Long eventId) {
        log.debug("REST request to get email statistics for event: {}", eventId);

        String tenantId = getTenantId();
        Map<String, Object> statistics = promotionEmailSentLogService.getEmailStatistics(eventId, tenantId);
        return ResponseEntity.ok(statistics);
    }

    /**
     * Get tenant ID from context.
     */
    private String getTenantId() {
        try {
            String tenantId = com.nextjstemplate.security.TenantContext.getCurrentTenant();
            if (tenantId == null || tenantId.isEmpty()) {
                throw new BadRequestAlertException("Tenant ID is required", ENTITY_NAME, "tenantIdRequired");
            }
            return tenantId;
        } catch (Exception e) {
            log.warn("Could not get tenant ID from context: {}", e.getMessage());
            throw new BadRequestAlertException("Tenant ID is required", ENTITY_NAME, "tenantIdRequired");
        }
    }
}
