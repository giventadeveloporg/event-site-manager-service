package com.nextjstemplate.web.rest;

import com.nextjstemplate.service.BatchJobService;
import com.nextjstemplate.service.dto.BatchJobRequest;
import com.nextjstemplate.service.dto.BatchJobResponse;
import com.nextjstemplate.service.dto.ManualPaymentSummaryJobRequest;
import com.nextjstemplate.service.dto.ManualPaymentSummaryJobResponse;
import com.nextjstemplate.service.dto.StripeFeesTaxUpdateRequest;
import com.nextjstemplate.service.dto.StripeFeesTaxUpdateResponse;
import com.nextjstemplate.service.dto.StripeTicketBatchRefundRequest;
import com.nextjstemplate.service.dto.StripeTicketBatchRefundResponse;
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

    /**
     * {@code POST  /manual-payment-summary} : Trigger Manual Payment Summary aggregation job.
     *
     * This proxies to batch-jobs: {@code POST /api/batch-jobs/manual-payment-summary}
     * Optional filters: tenantId, eventId, snapshotDate (yyyy-MM-dd).
     */
    @PostMapping("/manual-payment-summary")
    public ResponseEntity<ManualPaymentSummaryJobResponse> triggerManualPaymentSummary(
        @RequestBody(required = false) ManualPaymentSummaryJobRequest request,
        @RequestHeader(value = "Authorization", required = false) String authHeader,
        @RequestHeader(value = "X-Tenant-Id", required = false) String tenantIdHeader
    ) {
        log.debug("REST request to trigger manual payment summary batch job: {}", request);

        try {
            authenticateRequest(authHeader);

            ManualPaymentSummaryJobRequest jobRequest = new ManualPaymentSummaryJobRequest();
            if (request != null) {
                jobRequest.setTenantId(request.getTenantId());
                jobRequest.setEventId(request.getEventId());
                jobRequest.setSnapshotDate(request.getSnapshotDate());
            }
            if (StringUtils.hasText(tenantIdHeader)) {
                jobRequest.setTenantId(tenantIdHeader);
            }

            ManualPaymentSummaryJobResponse response = batchJobService.triggerManualPaymentSummary(jobRequest);

            if (response != null && Boolean.TRUE.equals(response.getSuccess())) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
            }
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            log.error("Failed to trigger manual payment summary batch job: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * {@code POST  /stripe-ticket-batch-refund} : Trigger Stripe ticket batch refund job.
     *
     * This proxies to batch-jobs: {@code POST /api/batch-jobs/stripe-ticket-batch-refund}
     * Processes eligible tickets for an event and creates Stripe refunds.
     *
     * @param request the Stripe ticket batch refund request
     * @param authHeader the Authorization header
     * @param tenantIdHeader the X-Tenant-Id header (optional)
     * @return the {@link ResponseEntity} with status {@code 202 (Accepted)} and the batch job response
     */
    @PostMapping("/stripe-ticket-batch-refund")
    public ResponseEntity<StripeTicketBatchRefundResponse> triggerStripeTicketBatchRefund(
        @Valid @RequestBody StripeTicketBatchRefundRequest request,
        @RequestHeader(value = "Authorization", required = false) String authHeader,
        @RequestHeader(value = "X-Tenant-Id", required = false) String tenantIdHeader
    ) {
        log.debug("REST request to trigger Stripe ticket batch refund job: {}", request);

        try {
            // 1. Authenticate request (JWT or cron secret)
            authenticateRequest(authHeader);

            // 2. Build request from body or headers
            StripeTicketBatchRefundRequest jobRequest = buildStripeTicketBatchRefundRequest(request, tenantIdHeader);

            // 3. Validate request
            validateStripeTicketBatchRefundRequest(jobRequest);

            // 4. Generate job ID if not provided
            if (jobRequest.getJobId() == null || jobRequest.getJobId().isEmpty()) {
                String jobId =
                    "stripe-batch-refund-" +
                    java.time.ZonedDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")) +
                    "-" +
                    jobRequest.getEventId();
                jobRequest.setJobId(jobId);
            }

            // 5. Submit job
            StripeTicketBatchRefundResponse response = batchJobService.triggerStripeTicketBatchRefund(jobRequest);

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid request: {}", e.getMessage());
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "invalidrequest");
        } catch (BatchJobException e) {
            log.error("Batch job exception: {}", e.getMessage(), e);
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, e.getErrorKey());
        } catch (Exception e) {
            log.error("Failed to trigger Stripe ticket batch refund job: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Build the Stripe ticket batch refund request from body or headers.
     *
     * @param request the request body (may be null)
     * @param tenantIdHeader the X-Tenant-Id header (may be null)
     * @return the Stripe ticket batch refund request
     */
    private StripeTicketBatchRefundRequest buildStripeTicketBatchRefundRequest(
        StripeTicketBatchRefundRequest request,
        String tenantIdHeader
    ) {
        StripeTicketBatchRefundRequest jobRequest = new StripeTicketBatchRefundRequest();

        if (request != null) {
            // Use values from request body
            jobRequest.setEventId(request.getEventId());
            jobRequest.setTenantId(request.getTenantId());
            jobRequest.setJobId(request.getJobId());
            jobRequest.setStartDate(request.getStartDate());
            jobRequest.setEndDate(request.getEndDate());
        }

        // Override tenant ID from header if provided
        if (StringUtils.hasText(tenantIdHeader)) {
            jobRequest.setTenantId(tenantIdHeader);
        }

        return jobRequest;
    }

    /**
     * Validate the Stripe ticket batch refund request.
     *
     * @param request the Stripe ticket batch refund request
     */
    private void validateStripeTicketBatchRefundRequest(StripeTicketBatchRefundRequest request) {
        if (request.getEventId() == null) {
            throw new BadRequestAlertException("eventId is required", ENTITY_NAME, "eventidrequired");
        }

        if (request.getTenantId() == null || request.getTenantId().isEmpty()) {
            throw new BadRequestAlertException("tenantId is required", ENTITY_NAME, "tenantidrequired");
        }

        if (request.getStartDate() != null && request.getEndDate() != null) {
            if (request.getStartDate().isAfter(request.getEndDate())) {
                throw new BadRequestAlertException("startDate must be before or equal to endDate", ENTITY_NAME, "invaliddaterange");
            }
        }
    }
}
