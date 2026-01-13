package com.nextjstemplate.web.rest;

import com.nextjstemplate.service.BatchJobService;
import com.nextjstemplate.service.dto.BatchJobRequest;
import com.nextjstemplate.service.dto.BatchJobResponse;
import com.nextjstemplate.service.dto.StripeFeesTaxUpdateRequest;
import com.nextjstemplate.service.dto.StripeFeesTaxUpdateResponse;
import com.nextjstemplate.web.rest.errors.BadRequestAlertException;
import com.nextjstemplate.web.rest.errors.BatchJobException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for triggering batch jobs.
 */
@RestController
@RequestMapping("/api/cron")
public class BatchJobResource {

    private static final Logger log = LoggerFactory.getLogger(BatchJobResource.class);

    private static final String ENTITY_NAME = "batchJob";
    private static final int DEFAULT_BATCH_SIZE = 100;
    private static final int DEFAULT_MAX_SUBSCRIPTIONS = 10000;

    private final BatchJobService batchJobService;

    @Value("${cron.secret:}")
    private String cronSecret;

    public BatchJobResource(BatchJobService batchJobService) {
        this.batchJobService = batchJobService;
    }

    /**
     * {@code POST  /subscription-renewal} : Trigger subscription renewal batch job.
     *
     * @param request the batch job request (optional)
     * @param authHeader the Authorization header
     * @param tenantIdHeader the X-Tenant-Id header (optional)
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the batch job response
     */
    @PostMapping("/subscription-renewal")
    public ResponseEntity<BatchJobResponse> triggerSubscriptionRenewal(
        @RequestBody(required = false) BatchJobRequest request,
        @RequestHeader(value = "Authorization", required = false) String authHeader,
        @RequestHeader(value = "X-Tenant-Id", required = false) String tenantIdHeader
    ) {
        log.debug("REST request to trigger subscription renewal batch job: {}", request);

        // 1. Authenticate request (JWT or cron secret)
        authenticateRequest(authHeader);

        // 2. Build request from body or headers
        BatchJobRequest jobRequest = buildRequest(request, tenantIdHeader);

        // 3. Validate request
        validateRequest(jobRequest);

        // 4. Submit job
        BatchJobResponse response = batchJobService.submitSubscriptionRenewalJob(jobRequest);

        return ResponseEntity.ok(response);
    }

    /**
     * Authenticate the request using either cron secret or JWT.
     *
     * @param authHeader the Authorization header
     */
    private void authenticateRequest(String authHeader) {
        // Check for cron secret authentication
        if (StringUtils.hasText(cronSecret) && StringUtils.hasText(authHeader)) {
            String expectedAuth = "Bearer " + cronSecret;
            if (authHeader.equals(expectedAuth)) {
                log.debug("Cron secret authentication successful");
                return;
            }
        }

        // Otherwise, check for JWT authentication (handled by existing filter)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("Unauthorized request to batch job endpoint");
            throw new BadRequestAlertException("Unauthorized", ENTITY_NAME, "unauthorized");
        }

        log.debug("JWT authentication successful for user: {}", authentication.getName());
    }

    /**
     * Build the batch job request from body or headers.
     *
     * @param request the request body (may be null)
     * @param tenantIdHeader the X-Tenant-Id header (may be null)
     * @return the batch job request
     */
    private BatchJobRequest buildRequest(BatchJobRequest request, String tenantIdHeader) {
        BatchJobRequest jobRequest = new BatchJobRequest();

        if (request != null) {
            // Use values from request body
            jobRequest.setTenantId(request.getTenantId());
            jobRequest.setStripeSubscriptionId(request.getStripeSubscriptionId());
            jobRequest.setBatchSize(request.getBatchSize());
            jobRequest.setMaxSubscriptions(request.getMaxSubscriptions());
        }

        // Override tenant ID from header if provided
        if (StringUtils.hasText(tenantIdHeader)) {
            jobRequest.setTenantId(tenantIdHeader);
        }

        // Set defaults if not provided
        if (jobRequest.getBatchSize() == null) {
            jobRequest.setBatchSize(DEFAULT_BATCH_SIZE);
        }
        if (jobRequest.getMaxSubscriptions() == null) {
            jobRequest.setMaxSubscriptions(DEFAULT_MAX_SUBSCRIPTIONS);
        }

        return jobRequest;
    }

    /**
     * Validate the batch job request.
     *
     * @param request the batch job request
     */
    private void validateRequest(BatchJobRequest request) {
        if (request.getBatchSize() != null && request.getBatchSize() < 1) {
            throw new BadRequestAlertException("Batch size must be at least 1", ENTITY_NAME, "invalidbatchsize");
        }
        if (request.getMaxSubscriptions() != null && request.getMaxSubscriptions() < 1) {
            throw new BadRequestAlertException("Max subscriptions must be at least 1", ENTITY_NAME, "invalidmaxsubscriptions");
        }
    }

    /**
     * {@code POST  /stripe-fees-tax-update} : Trigger Stripe fees and tax update batch job.
     *
     * @param request the Stripe fees tax update request (optional)
     * @param authHeader the Authorization header
     * @param tenantIdHeader the X-Tenant-Id header (optional)
     * @return the {@link ResponseEntity} with status {@code 202 (Accepted)} and the batch job response
     */
    @PostMapping("/stripe-fees-tax-update")
    public ResponseEntity<StripeFeesTaxUpdateResponse> triggerStripeFeesTaxUpdate(
        @RequestBody(required = false) StripeFeesTaxUpdateRequest request,
        @RequestHeader(value = "Authorization", required = false) String authHeader,
        @RequestHeader(value = "X-Tenant-Id", required = false) String tenantIdHeader
    ) {
        log.debug("REST request to trigger Stripe fees and tax update batch job: {}", request);

        try {
            // 1. Authenticate request (JWT or cron secret)
            authenticateRequest(authHeader);

            // 2. Build request from body or headers
            StripeFeesTaxUpdateRequest jobRequest = buildStripeFeesTaxUpdateRequest(request, tenantIdHeader);

            // 3. Validate request
            validateStripeFeesTaxUpdateRequest(jobRequest);

            // 4. Submit job
            StripeFeesTaxUpdateResponse response = batchJobService.triggerStripeFeesTaxUpdate(jobRequest);

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid request: {}", e.getMessage());
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "invalidrequest");
        } catch (BatchJobException e) {
            log.error("Batch job exception: {}", e.getMessage(), e);
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, e.getErrorKey());
        } catch (Exception e) {
            log.error("Failed to trigger Stripe fees and tax update batch job: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Build the Stripe fees tax update request from body or headers.
     *
     * @param request the request body (may be null)
     * @param tenantIdHeader the X-Tenant-Id header (may be null)
     * @return the Stripe fees tax update request
     */
    private StripeFeesTaxUpdateRequest buildStripeFeesTaxUpdateRequest(StripeFeesTaxUpdateRequest request, String tenantIdHeader) {
        StripeFeesTaxUpdateRequest jobRequest = new StripeFeesTaxUpdateRequest();

        if (request != null) {
            // Use values from request body
            jobRequest.setTenantId(request.getTenantId());
            jobRequest.setStartDate(request.getStartDate());
            jobRequest.setEndDate(request.getEndDate());
            jobRequest.setForceUpdate(request.getForceUpdate());
        }

        // Override tenant ID from header if provided
        if (StringUtils.hasText(tenantIdHeader)) {
            jobRequest.setTenantId(tenantIdHeader);
        }

        // Set default for forceUpdate if not provided
        if (jobRequest.getForceUpdate() == null) {
            jobRequest.setForceUpdate(false);
        }

        return jobRequest;
    }

    /**
     * Validate the Stripe fees tax update request.
     *
     * @param request the Stripe fees tax update request
     */
    private void validateStripeFeesTaxUpdateRequest(StripeFeesTaxUpdateRequest request) {
        if (request.getStartDate() != null && request.getEndDate() != null) {
            if (request.getStartDate().isAfter(request.getEndDate())) {
                throw new BadRequestAlertException("startDate must be before or equal to endDate", ENTITY_NAME, "invaliddaterange");
            }
        }
    }
}
