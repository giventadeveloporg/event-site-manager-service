package com.nextjstemplate.web.rest;

import com.nextjstemplate.domain.enumeration.ManualPaymentStatus;
import com.nextjstemplate.repository.ManualPaymentRequestRepository;
import com.nextjstemplate.security.TenantContext;
import com.nextjstemplate.service.ManualPaymentRequestQueryService;
import com.nextjstemplate.service.ManualPaymentRequestService;
import com.nextjstemplate.service.criteria.ManualPaymentRequestCriteria;
import com.nextjstemplate.service.dto.ManualPaymentRequestDTO;
import com.nextjstemplate.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.nextjstemplate.domain.ManualPaymentRequest}.
 */
@RestController
@RequestMapping("/api/manual-payments")
public class ManualPaymentRequestResource {

    private final Logger log = LoggerFactory.getLogger(ManualPaymentRequestResource.class);

    private static final String ENTITY_NAME = "manualPaymentRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ManualPaymentRequestService manualPaymentRequestService;
    private final ManualPaymentRequestRepository manualPaymentRequestRepository;
    private final ManualPaymentRequestQueryService manualPaymentRequestQueryService;

    public ManualPaymentRequestResource(
        ManualPaymentRequestService manualPaymentRequestService,
        ManualPaymentRequestRepository manualPaymentRequestRepository,
        ManualPaymentRequestQueryService manualPaymentRequestQueryService
    ) {
        this.manualPaymentRequestService = manualPaymentRequestService;
        this.manualPaymentRequestRepository = manualPaymentRequestRepository;
        this.manualPaymentRequestQueryService = manualPaymentRequestQueryService;
    }

    @PostMapping("")
    public ResponseEntity<ManualPaymentRequestDTO> createManualPayment(@Valid @RequestBody ManualPaymentRequestDTO dto)
        throws URISyntaxException {
        log.debug("REST request to save ManualPaymentRequest : {}", dto);
        if (dto.getId() != null) {
            throw new BadRequestAlertException("A new manualPaymentRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }

        String tenantId = requireTenantId();
        dto.setTenantId(tenantId);

        ManualPaymentRequestDTO result = manualPaymentRequestService.save(dto);
        return ResponseEntity
            .created(new URI("/api/manual-payments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ManualPaymentRequestDTO> partialUpdateManualPayment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ManualPaymentRequestDTO dto
    ) throws URISyntaxException {
        log.debug("REST request to partial update ManualPaymentRequest partially : {}, {}", id, dto);
        if (dto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!manualPaymentRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        // If status is being updated, run status workflow via service
        if (dto.getStatus() != null) {
            ManualPaymentRequestDTO updated = manualPaymentRequestService.updateStatus(
                id,
                requireTenantId(),
                dto.getStatus(),
                dto.getReceivedBy(),
                dto.getVoidReason()
            );
            return ResponseEntity.ok().body(updated);
        }

        Optional<ManualPaymentRequestDTO> result = manualPaymentRequestService.partialUpdate(dto);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dto.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<java.util.List<ManualPaymentRequestDTO>> getAllManualPayments(
        ManualPaymentRequestCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ManualPaymentRequests by criteria: {}", criteria);

        // Enforce tenant isolation
        String tenantId = requireTenantId();
        if (criteria == null) {
            criteria = new ManualPaymentRequestCriteria();
        }
        if (criteria.getTenantId() == null) {
            criteria.setTenantId(new tech.jhipster.service.filter.StringFilter());
            criteria.getTenantId().setEquals(tenantId);
        }

        Page<ManualPaymentRequestDTO> page = manualPaymentRequestQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countManualPayments(ManualPaymentRequestCriteria criteria) {
        log.debug("REST request to count ManualPaymentRequests by criteria: {}", criteria);
        String tenantId = requireTenantId();
        if (criteria == null) {
            criteria = new ManualPaymentRequestCriteria();
        }
        if (criteria.getTenantId() == null) {
            criteria.setTenantId(new tech.jhipster.service.filter.StringFilter());
            criteria.getTenantId().setEquals(tenantId);
        }
        return ResponseEntity.ok().body(manualPaymentRequestQueryService.countByCriteria(criteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManualPaymentRequestDTO> getManualPayment(@PathVariable Long id) {
        log.debug("REST request to get ManualPaymentRequest : {}", id);
        String tenantId = requireTenantId();
        Optional<ManualPaymentRequestDTO> dto = manualPaymentRequestService.findOneWithDetails(id);
        dto.ifPresent(dtoValue -> {
            if (dtoValue.getTenantId() != null && !dtoValue.getTenantId().equals(tenantId)) {
                throw new BadRequestAlertException(
                    "Manual payment request does not belong to the specified tenant",
                    ENTITY_NAME,
                    "tenantMismatch"
                );
            }
        });
        return ResponseUtil.wrapOrNotFound(dto);
    }

    @PostMapping("/{id}/send-confirmation-email")
    public ResponseEntity<Map<String, Object>> sendConfirmationEmail(@PathVariable Long id) {
        log.debug("REST request to send confirmation email for ManualPaymentRequest : {}", id);
        String tenantId = requireTenantId();
        ManualPaymentRequestDTO dto = manualPaymentRequestService
            .findOne(id)
            .orElseThrow(() -> new BadRequestAlertException("Manual payment request not found", ENTITY_NAME, "idnotfound"));
        if (dto.getTenantId() != null && !dto.getTenantId().equals(tenantId)) {
            throw new BadRequestAlertException(
                "Manual payment request does not belong to the specified tenant",
                ENTITY_NAME,
                "tenantMismatch"
            );
        }

        manualPaymentRequestService.triggerConfirmationEmail(id);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Confirmation email queued");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteManualPayment(@PathVariable Long id) {
        log.debug("REST request to delete ManualPaymentRequest : {}", id);
        manualPaymentRequestService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping(value = "/{id}/proof", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ManualPaymentRequestDTO> uploadProofOfPayment(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        log.debug("REST request to upload proof-of-payment for ManualPaymentRequest : {}", id);
        String tenantId = requireTenantId();
        ManualPaymentRequestDTO updated = manualPaymentRequestService.uploadProofOfPayment(id, tenantId, file);
        return ResponseEntity.ok(updated);
    }

    /**
     * Convenience endpoint to mark a request as RECEIVED.
     * (Optional helper for admin UIs; PATCH with status also works.)
     */
    @PostMapping("/{id}/mark-received")
    public ResponseEntity<ManualPaymentRequestDTO> markReceived(
        @PathVariable Long id,
        @RequestParam(value = "receivedBy", required = false) String receivedBy
    ) {
        ManualPaymentRequestDTO updated = manualPaymentRequestService.updateStatus(
            id,
            requireTenantId(),
            ManualPaymentStatus.RECEIVED,
            receivedBy,
            null
        );
        return ResponseEntity.ok(updated);
    }

    private String requireTenantId() {
        String tenantId = TenantContext.getCurrentTenant();
        if (tenantId == null || tenantId.isBlank()) {
            throw new BadRequestAlertException("Tenant ID is required", ENTITY_NAME, "tenantIdRequired");
        }
        return tenantId;
    }
}
