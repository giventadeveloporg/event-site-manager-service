package com.nextjstemplate.web.rest;

import com.nextjstemplate.repository.PromotionEmailTemplateRepository;
import com.nextjstemplate.service.*;
import com.nextjstemplate.service.criteria.PromotionEmailTemplateCriteria;
import com.nextjstemplate.service.dto.*;
import com.nextjstemplate.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.nextjstemplate.domain.PromotionEmailTemplate}.
 */
@RestController
@RequestMapping("/api/promotion-email-templates")
public class PromotionEmailTemplateResource {

    private final Logger log = LoggerFactory.getLogger(PromotionEmailTemplateResource.class);

    private static final String ENTITY_NAME = "promotionEmailTemplate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PromotionEmailTemplateService promotionEmailTemplateService;

    private final PromotionEmailTemplateRepository promotionEmailTemplateRepository;

    private final PromotionEmailTemplateQueryService promotionEmailTemplateQueryService;

    private final PromotionEmailService promotionEmailService;

    private final ClerkAuthService clerkAuthService;

    public PromotionEmailTemplateResource(
        PromotionEmailTemplateService promotionEmailTemplateService,
        PromotionEmailTemplateRepository promotionEmailTemplateRepository,
        PromotionEmailTemplateQueryService promotionEmailTemplateQueryService,
        PromotionEmailService promotionEmailService,
        ClerkAuthService clerkAuthService
    ) {
        this.promotionEmailTemplateService = promotionEmailTemplateService;
        this.promotionEmailTemplateRepository = promotionEmailTemplateRepository;
        this.promotionEmailTemplateQueryService = promotionEmailTemplateQueryService;
        this.promotionEmailService = promotionEmailService;
        this.clerkAuthService = clerkAuthService;
    }

    /**
     * {@code POST  /promotion-email-templates} : Create a new promotion email template.
     *
     * @param formDTO the formDTO to create.
     * @param authentication the authentication object.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new promotionEmailTemplateDTO, or with status {@code 400 (Bad Request)} if the template has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PromotionEmailTemplateDTO> createPromotionEmailTemplate(
        @Valid @RequestBody PromotionEmailTemplateFormDTO formDTO,
        Authentication authentication
    ) throws URISyntaxException {
        log.debug("REST request to save PromotionEmailTemplate : {}", formDTO);

        String tenantId = getTenantId();
        Long userId = getCurrentUserId(authentication);

        PromotionEmailTemplateDTO result = promotionEmailTemplateService.createTemplate(formDTO, tenantId, userId);
        return ResponseEntity
            .created(new URI("/api/promotion-email-templates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /promotion-email-templates/:id} : Updates an existing promotion email template.
     *
     * @param id the id of the template to update.
     * @param formDTO the formDTO to update.
     * @param authentication the authentication object.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated promotionEmailTemplateDTO,
     * or with status {@code 400 (Bad Request)} if the formDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the template couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PromotionEmailTemplateDTO> updatePromotionEmailTemplate(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PromotionEmailTemplateFormDTO formDTO,
        Authentication authentication
    ) throws URISyntaxException {
        log.debug("REST request to update PromotionEmailTemplate : {}, {}", id, formDTO);

        String tenantId = getTenantId();

        PromotionEmailTemplateDTO result = promotionEmailTemplateService.updateTemplate(id, formDTO, tenantId);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /promotion-email-templates/:id} : Partial updates given fields of an existing promotion email template.
     *
     * @param id the id of the template to update.
     * @param templateDTO the templateDTO to update.
     * @param authentication the authentication object.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated promotionEmailTemplateDTO,
     * or with status {@code 400 (Bad Request)} if the templateDTO is not valid,
     * or with status {@code 404 (Not Found)} if the template is not found,
     * or with status {@code 500 (Internal Server Error)} if the template couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PromotionEmailTemplateDTO> partialUpdatePromotionEmailTemplate(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PromotionEmailTemplateDTO templateDTO,
        Authentication authentication
    ) throws URISyntaxException {
        log.debug("REST request to partial update PromotionEmailTemplate partially : {}, {}", id, templateDTO);
        if (templateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, templateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!promotionEmailTemplateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PromotionEmailTemplateDTO> result = promotionEmailTemplateService.partialUpdate(id, templateDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, templateDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /promotion-email-templates} : get all the promotion email templates.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of promotion email templates in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PromotionEmailTemplateDTO>> getAllPromotionEmailTemplates(
        PromotionEmailTemplateCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get PromotionEmailTemplates by criteria: {}", criteria);

        String tenantId = getTenantId();
        // Add tenant filter to criteria if not already set
        if (criteria == null) {
            criteria = new PromotionEmailTemplateCriteria();
        }
        if (criteria.getTenantId() == null) {
            criteria.setTenantId(new tech.jhipster.service.filter.StringFilter());
            criteria.getTenantId().setEquals(tenantId);
        }

        Page<PromotionEmailTemplateDTO> page = promotionEmailTemplateQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /promotion-email-templates/count} : count all the promotion email templates.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPromotionEmailTemplates(PromotionEmailTemplateCriteria criteria) {
        log.debug("REST request to count PromotionEmailTemplates by criteria: {}", criteria);

        String tenantId = getTenantId();
        // Add tenant filter to criteria if not already set
        if (criteria == null) {
            criteria = new PromotionEmailTemplateCriteria();
        }
        if (criteria.getTenantId() == null) {
            criteria.setTenantId(new tech.jhipster.service.filter.StringFilter());
            criteria.getTenantId().setEquals(tenantId);
        }

        return ResponseEntity.ok().body(promotionEmailTemplateQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /promotion-email-templates/:id} : get the "id" promotion email template.
     *
     * @param id the id of the promotionEmailTemplateDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the promotionEmailTemplateDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PromotionEmailTemplateDTO> getPromotionEmailTemplate(@PathVariable Long id) {
        log.debug("REST request to get PromotionEmailTemplate : {}", id);

        String tenantId = getTenantId();
        Optional<PromotionEmailTemplateDTO> promotionEmailTemplateDTO = promotionEmailTemplateService.findOne(id, tenantId);
        return ResponseUtil.wrapOrNotFound(promotionEmailTemplateDTO);
    }

    /**
     * {@code DELETE  /promotion-email-templates/:id} : delete the "id" promotion email template.
     *
     * @param id the id of the promotionEmailTemplateDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePromotionEmailTemplate(@PathVariable Long id) {
        log.debug("REST request to delete PromotionEmailTemplate : {}", id);

        String tenantId = getTenantId();
        promotionEmailTemplateService.delete(id, tenantId);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code POST  /promotion-email-templates/:id/send-test} : Send a test email using the template.
     *
     * @param id the id of the template.
     * @param request the send email request (optional recipientEmail).
     * @param authentication the authentication object.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and response body.
     */
    @PostMapping("/{id}/send-test")
    public ResponseEntity<Map<String, Object>> sendTestEmail(
        @PathVariable Long id,
        @RequestBody(required = false) Map<String, String> request,
        Authentication authentication
    ) {
        log.debug("REST request to send test email for template: {}", id);

        String tenantId = getTenantId();
        // Don't require Clerk auth lookup - userId can be null
        Long userId = null;
        String recipientEmail = request != null ? request.get("recipientEmail") : null;

        Map<String, Object> result = promotionEmailService.sendTestEmail(id, recipientEmail, tenantId, userId);
        if (Boolean.TRUE.equals(result.get("success"))) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(result);
        }
        return ResponseEntity.badRequest().body(result);
    }

    /**
     * {@code POST  /promotion-email-templates/:id/send-bulk} : Send bulk emails using the template.
     *
     * @param id the id of the template.
     * @param request the send email request (optional recipientEmails array).
     * @param authentication the authentication object.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and response body.
     */
    @PostMapping("/{id}/send-bulk")
    public ResponseEntity<Map<String, Object>> sendBulkEmail(
        @PathVariable Long id,
        @RequestBody(required = false) SendPromotionEmailDTO request,
        Authentication authentication
    ) {
        log.debug("REST request to send bulk email for template: {}", id);

        String tenantId = getTenantId();
        // Don't require Clerk auth lookup - userId can be null
        Long userId = null;
        List<String> recipientEmails = request != null ? request.getRecipientEmails() : null;

        Map<String, Object> result = promotionEmailService.sendBulkEmail(id, recipientEmails, tenantId, userId);
        return ResponseEntity.ok(result);
    }

    /**
     * {@code POST  /promotion-email-templates/:id/send-to-subscribed} : Send bulk emails to all subscribed members of the tenant.
     *
     * @param id the id of the template.
     * @param authentication the authentication object.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and response body.
     */
    @PostMapping("/{id}/send-to-subscribed")
    public ResponseEntity<Map<String, Object>> sendBulkEmailToSubscribedMembers(@PathVariable Long id, Authentication authentication) {
        log.debug("REST request to send bulk email to subscribed members for template: {}", id);

        String tenantId = getTenantId();
        // Don't require Clerk auth lookup - userId can be null
        Long userId = null;

        Map<String, Object> result = promotionEmailService.sendBulkEmailToSubscribedMembers(id, tenantId, userId);
        return ResponseEntity.ok(result);
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

    /**
     * Get current user ID from authentication.
     * Note: Not used for promotion email endpoints - they don't require Clerk auth lookup.
     * Kept for potential future use in other endpoints.
     */
    @SuppressWarnings("unused")
    private Long getCurrentUserId(Authentication authentication) {
        // Not used - promotion email endpoints don't require Clerk auth lookup
        return null;
    }
}
