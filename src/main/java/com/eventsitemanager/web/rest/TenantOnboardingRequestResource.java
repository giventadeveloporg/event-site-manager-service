package com.eventsitemanager.web.rest;

import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.TenantOnboardingRequestRepository;
import com.eventsitemanager.service.TenantOnboardingNotificationService;
import com.eventsitemanager.service.TenantOnboardingProvisioningService;
import com.eventsitemanager.service.TenantOnboardingRequestQueryService;
import com.eventsitemanager.service.TenantOnboardingRequestService;
import com.eventsitemanager.service.criteria.TenantOnboardingRequestCriteria;
import com.eventsitemanager.service.dto.TenantOnboardingApproveDTO;
import com.eventsitemanager.service.dto.TenantOnboardingRejectDTO;
import com.eventsitemanager.service.dto.TenantOnboardingRequestDTO;
import com.eventsitemanager.service.dto.TenantOnboardingSubmitDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
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
 * REST controller for {@link com.eventsitemanager.domain.TenantOnboardingRequest}.
 * <p>
 * Like every {@code /api/**} endpoint this only requires the service JWT. Platform SUPER_ADMIN authorization is
 * enforced by the admin hub server actions; the public submit is rate limited by the hub's public route.
 * There is intentionally no DELETE endpoint: requests are an audit trail.
 */
@RestController
@RequestMapping("/api/tenant-onboarding-requests")
public class TenantOnboardingRequestResource {

    private static final Logger LOG = LoggerFactory.getLogger(TenantOnboardingRequestResource.class);

    private static final String ENTITY_NAME = "tenantOnboardingRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TenantOnboardingRequestService requestService;
    private final TenantOnboardingRequestQueryService queryService;
    private final TenantOnboardingRequestRepository requestRepository;
    private final TenantOnboardingProvisioningService provisioningService;
    private final TenantOnboardingNotificationService notificationService;

    public TenantOnboardingRequestResource(
        TenantOnboardingRequestService requestService,
        TenantOnboardingRequestQueryService queryService,
        TenantOnboardingRequestRepository requestRepository,
        TenantOnboardingProvisioningService provisioningService,
        TenantOnboardingNotificationService notificationService
    ) {
        this.requestService = requestService;
        this.queryService = queryService;
        this.requestRepository = requestRepository;
        this.provisioningService = provisioningService;
        this.notificationService = notificationService;
    }

    /**
     * {@code POST /tenant-onboarding-requests/submit} : create a PENDING request (public form or admin manual entry).
     */
    @PostMapping("/submit")
    public ResponseEntity<TenantOnboardingRequestDTO> submit(@Valid @RequestBody TenantOnboardingSubmitDTO submitDTO)
        throws URISyntaxException {
        LOG.debug("REST request to submit TenantOnboardingRequest for {}", submitDTO.getRequestedHostname());
        TenantOnboardingRequestDTO result = requestService.submit(submitDTO);
        notificationService.onSubmitted(result);
        return ResponseEntity
            .created(new URI("/api/tenant-onboarding-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH /tenant-onboarding-requests/:id} : edit request content while PENDING or FAILED.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TenantOnboardingRequestDTO> partialUpdate(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TenantOnboardingRequestDTO dto
    ) {
        LOG.debug("REST request to partial update TenantOnboardingRequest : {}", id);
        if (dto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!requestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Optional<TenantOnboardingRequestDTO> result = requestService.partialUpdate(dto);
        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()));
    }

    /**
     * {@code POST /tenant-onboarding-requests/:id/approve} : provision the tenant in one transaction.
     */
    @PostMapping("/{id}/approve")
    public ResponseEntity<TenantOnboardingRequestDTO> approve(
        @PathVariable("id") Long id,
        @Valid @RequestBody TenantOnboardingApproveDTO approveDTO
    ) {
        LOG.info("REST request to approve TenantOnboardingRequest {} as tenant {}", id, approveDTO.getTenantId());
        TenantOnboardingRequestDTO result = provisioningService.approve(id, approveDTO);
        if (Boolean.TRUE.equals(approveDTO.getNotifySubmitter())) {
            notificationService.onApproved(result);
        }
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .body(result);
    }

    /**
     * {@code POST /tenant-onboarding-requests/:id/reject} : reject a PENDING or FAILED request.
     */
    @PostMapping("/{id}/reject")
    public ResponseEntity<TenantOnboardingRequestDTO> reject(
        @PathVariable("id") Long id,
        @Valid @RequestBody TenantOnboardingRejectDTO rejectDTO
    ) {
        LOG.info("REST request to reject TenantOnboardingRequest {}", id);
        TenantOnboardingRequestDTO result = requestService.reject(id, rejectDTO);
        if (Boolean.TRUE.equals(rejectDTO.getNotifySubmitter())) {
            notificationService.onRejected(result);
        }
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .body(result);
    }

    /**
     * {@code GET /tenant-onboarding-requests} : list requests with criteria and pagination.
     */
    @GetMapping("")
    public ResponseEntity<List<TenantOnboardingRequestDTO>> getAll(
        TenantOnboardingRequestCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get TenantOnboardingRequests by criteria: {}", criteria);
        Page<TenantOnboardingRequestDTO> page = queryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET /tenant-onboarding-requests/count} : count requests matching the criteria.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> count(TenantOnboardingRequestCriteria criteria) {
        return ResponseEntity.ok().body(queryService.countByCriteria(criteria));
    }

    /**
     * {@code GET /tenant-onboarding-requests/:id} : get one request.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TenantOnboardingRequestDTO> getOne(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TenantOnboardingRequest : {}", id);
        return ResponseUtil.wrapOrNotFound(requestService.findOne(id));
    }
}
