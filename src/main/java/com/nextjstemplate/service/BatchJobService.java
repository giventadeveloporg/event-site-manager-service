package com.nextjstemplate.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextjstemplate.errors.BatchJobException;
import com.nextjstemplate.properties.BatchJobProperties;
import com.nextjstemplate.service.dto.BatchJobExecutionDTO;
import com.nextjstemplate.service.dto.BatchJobExecutionPageResponseDTO;
import com.nextjstemplate.service.dto.BatchJobRequest;
import com.nextjstemplate.service.dto.BatchJobResponse;
import com.nextjstemplate.service.dto.BatchJobServiceRequest;
import com.nextjstemplate.service.dto.BatchJobServiceResponse;
import com.nextjstemplate.service.dto.BatchJobSummaryResponseDTO;
import com.nextjstemplate.service.dto.ConfiguredBatchJobResponseDTO;
import com.nextjstemplate.service.dto.ManualPaymentSummaryJobRequest;
import com.nextjstemplate.service.dto.ManualPaymentSummaryJobResponse;
import com.nextjstemplate.service.dto.StripeFeesTaxUpdateRequest;
import com.nextjstemplate.service.dto.StripeFeesTaxUpdateResponse;
import com.nextjstemplate.service.dto.StripeTicketBatchRefundRequest;
import com.nextjstemplate.service.dto.StripeTicketBatchRefundResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Service for submitting batch jobs to the batch job microservice via REST API.
 */
@Service
public class BatchJobService {

    private static final Logger log = LoggerFactory.getLogger(BatchJobService.class);

    private static final String SUBSCRIPTION_RENEWAL_ENDPOINT = "/api/batch-jobs/subscription-renewal";
    private static final String STRIPE_FEES_TAX_UPDATE_ENDPOINT = "/api/batch-jobs/stripe-fees-tax-update";
    private static final String MANUAL_PAYMENT_SUMMARY_ENDPOINT = "/api/batch-jobs/manual-payment-summary";
    private static final String STRIPE_TICKET_BATCH_REFUND_ENDPOINT = "/api/batch-jobs/stripe-ticket-batch-refund";
    private static final String MONITORING_EXECUTIONS_ENDPOINT = "/api/batch-jobs/executions";
    private static final String MONITORING_EXECUTION_BY_ID_ENDPOINT = "/api/batch-jobs/executions/{id}";
    private static final String MONITORING_FAILED_EXECUTIONS_ENDPOINT = "/api/batch-jobs/executions/failed";
    private static final String MONITORING_RUNNING_EXECUTIONS_ENDPOINT = "/api/batch-jobs/executions/running";
    private static final String MONITORING_SUMMARY_ENDPOINT = "/api/batch-jobs/summary";
    private static final String MONITORING_CONFIGURED_JOBS_ENDPOINT = "/api/batch-jobs/configured-jobs";
    private static final ParameterizedTypeReference<List<BatchJobExecutionDTO>> EXECUTION_LIST_TYPE = new ParameterizedTypeReference<>() {};
    private static final ParameterizedTypeReference<List<ConfiguredBatchJobResponseDTO>> CONFIGURED_JOBS_LIST_TYPE =
        new ParameterizedTypeReference<>() {};
    private static final int DEFAULT_BATCH_SIZE = 100;
    private static final int DEFAULT_MAX_SUBSCRIPTIONS = 10000;
    private static final String DEFAULT_ESTIMATED_DURATION = "15-30 minutes";

    private final WebClient webClient;
    private final BatchJobProperties batchJobProperties;
    private final ObjectMapper objectMapper;

    public BatchJobService(
        BatchJobProperties batchJobProperties,
        ObjectMapper objectMapper,
        @Qualifier("batchJobsWebClient") WebClient batchJobsWebClient
    ) {
        this.batchJobProperties = batchJobProperties;
        this.objectMapper = objectMapper;
        this.webClient = batchJobsWebClient;

        log.info("BatchJobService initialized with URL: {}", batchJobProperties.getUrl());
    }

    /**
     * Submit a subscription renewal batch job to the batch job microservice.
     *
     * @param request the batch job request
     * @return the batch job response
     * @throws BatchJobException if job submission fails
     */
    public BatchJobResponse submitSubscriptionRenewalJob(BatchJobRequest request) {
        if (!batchJobProperties.getEnabled()) {
            log.warn("Batch job service is disabled. Skipping subscription renewal batch trigger.");
            throw new BatchJobException("Batch job service is disabled", "batchjobdisabled");
        }

        try {
            // Build request for batch job service
            BatchJobServiceRequest serviceRequest = new BatchJobServiceRequest(
                request.getTenantId(),
                request.getBatchSize() != null ? request.getBatchSize() : DEFAULT_BATCH_SIZE,
                request.getMaxSubscriptions() != null ? request.getMaxSubscriptions() : DEFAULT_MAX_SUBSCRIPTIONS
            );

            // Pass stripeSubscriptionId if provided (for testing specific subscriptions)
            if (request.getStripeSubscriptionId() != null && !request.getStripeSubscriptionId().isEmpty()) {
                serviceRequest.setStripeSubscriptionId(request.getStripeSubscriptionId());
                log.info("Passing stripeSubscriptionId to batch job for testing: {}", request.getStripeSubscriptionId());
            }

            log.info(
                "Triggering subscription renewal batch job - tenantId: {}, batchSize: {}, maxSubscriptions: {}, stripeSubscriptionId: {}",
                serviceRequest.getTenantId(),
                serviceRequest.getBatchSize(),
                serviceRequest.getMaxSubscriptions(),
                serviceRequest.getStripeSubscriptionId() != null ? serviceRequest.getStripeSubscriptionId() : "none"
            );

            // Call batch job service
            BatchJobServiceResponse serviceResponse = webClient
                .post()
                .uri(SUBSCRIPTION_RENEWAL_ENDPOINT)
                .bodyValue(serviceRequest)
                .retrieve()
                .bodyToMono(BatchJobServiceResponse.class)
                .timeout(Duration.ofMillis(batchJobProperties.getTimeout()))
                .block();

            if (serviceResponse == null) {
                throw new BatchJobException("Received null response from batch job service", "nullresponse");
            }

            if (Boolean.FALSE.equals(serviceResponse.getSuccess())) {
                log.error("Batch job service returned failure: {}", serviceResponse.getMessage());
                throw new BatchJobException("Batch job service returned failure: " + serviceResponse.getMessage(), "batchjobfailed");
            }

            log.info(
                "Subscription renewal batch job triggered successfully. JobExecutionId: {}, ProcessedCount: {}",
                serviceResponse.getJobExecutionId(),
                serviceResponse.getProcessedCount()
            );

            // Build job name with timestamp
            String jobName = "subscription-renewal-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"));

            // Map service response to backend response
            BatchJobResponse batchJobResponse = new BatchJobResponse();
            batchJobResponse.setStatus("success");
            batchJobResponse.setJobId(serviceResponse.getJobExecutionId() != null ? serviceResponse.getJobExecutionId().toString() : null);
            batchJobResponse.setJobName(jobName);
            batchJobResponse.setMessage(
                serviceResponse.getMessage() != null ? serviceResponse.getMessage() : "Batch job submitted successfully"
            );
            batchJobResponse.setEstimatedDuration(DEFAULT_ESTIMATED_DURATION);
            batchJobResponse.setRequest(request);

            return batchJobResponse;
        } catch (WebClientResponseException e) {
            log.error("HTTP error triggering subscription renewal batch job: {} - {}", e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new BatchJobException("HTTP " + e.getStatusCode() + ": " + e.getMessage(), "batchjobhttperror");
        } catch (BatchJobException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error triggering subscription renewal batch job", e);
            throw new BatchJobException("Failed to trigger batch job: " + e.getMessage(), "batchjobsubmissionfailed");
        }
    }

    /**
     * Trigger manual payment summary aggregation job in batch jobs microservice.
     */
    public ManualPaymentSummaryJobResponse triggerManualPaymentSummary(ManualPaymentSummaryJobRequest request) {
        if (!batchJobProperties.getEnabled()) {
            log.warn("Batch job service is disabled. Skipping manual payment summary trigger.");
            ManualPaymentSummaryJobResponse response = new ManualPaymentSummaryJobResponse();
            response.setSuccess(false);
            response.setMessage("Batch job service is disabled");
            return response;
        }

        try {
            log.info(
                "Triggering manual payment summary job - tenantId: {}, eventId: {}, snapshotDate: {}",
                request != null ? request.getTenantId() : null,
                request != null ? request.getEventId() : null,
                request != null ? request.getSnapshotDate() : null
            );

            ManualPaymentSummaryJobResponse response = webClient
                .post()
                .uri(MANUAL_PAYMENT_SUMMARY_ENDPOINT)
                .bodyValue(request != null ? request : new ManualPaymentSummaryJobRequest())
                .retrieve()
                .bodyToMono(ManualPaymentSummaryJobResponse.class)
                .timeout(Duration.ofMillis(batchJobProperties.getTimeout()))
                .block();

            return response != null ? response : createManualPaymentSummaryError("Received null response from batch job service");
        } catch (WebClientResponseException e) {
            log.error("HTTP error triggering manual payment summary job: {} - {}", e.getStatusCode(), e.getResponseBodyAsString(), e);
            return createManualPaymentSummaryError("HTTP " + e.getStatusCode() + ": " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error triggering manual payment summary job", e);
            return createManualPaymentSummaryError("Failed to trigger batch job: " + e.getMessage());
        }
    }

    private ManualPaymentSummaryJobResponse createManualPaymentSummaryError(String message) {
        ManualPaymentSummaryJobResponse response = new ManualPaymentSummaryJobResponse();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }

    /**
     * Trigger Stripe fees and tax update batch job.
     *
     * @param request the Stripe fees tax update request
     * @return the batch job response
     * @throws BatchJobException if job submission fails
     */
    public StripeFeesTaxUpdateResponse triggerStripeFeesTaxUpdate(StripeFeesTaxUpdateRequest request) {
        if (!batchJobProperties.getEnabled()) {
            log.warn("Batch job service is disabled. Skipping Stripe fees tax update batch trigger.");
            throw new BatchJobException("Batch job service is disabled", "batchjobdisabled");
        }

        // Validate request
        if (request == null) {
            request = new StripeFeesTaxUpdateRequest();
        }

        if (request.getStartDate() != null && request.getEndDate() != null) {
            if (request.getStartDate().isAfter(request.getEndDate())) {
                throw new IllegalArgumentException("startDate must be before or equal to endDate");
            }
        }

        try {
            log.info(
                "Triggering Stripe fees and tax update batch job - tenantId: {}, startDate: {}, endDate: {}, forceUpdate: {}",
                request.getTenantId(),
                request.getStartDate(),
                request.getEndDate(),
                request.getForceUpdate()
            );

            // Call batch job service
            StripeFeesTaxUpdateResponse response = webClient
                .post()
                .uri(STRIPE_FEES_TAX_UPDATE_ENDPOINT)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(StripeFeesTaxUpdateResponse.class)
                .timeout(Duration.ofMillis(batchJobProperties.getTimeout()))
                .block();

            if (response == null) {
                throw new BatchJobException("Received null response from batch job service", "nullresponse");
            }

            log.info(
                "Stripe fees and tax update batch job triggered successfully. JobId: {}, Status: {}, EstimatedRecords: {}",
                response.getJobId(),
                response.getStatus(),
                response.getEstimatedRecords()
            );

            return response;
        } catch (WebClientResponseException.BadRequest e) {
            log.warn("Bad request to batch job service: {}", e.getResponseBodyAsString(), e);
            throw new IllegalArgumentException("Invalid request: " + e.getResponseBodyAsString(), e);
        } catch (WebClientResponseException e) {
            log.error("HTTP error triggering Stripe fees tax update batch job: {} - {}", e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new BatchJobException("HTTP " + e.getStatusCode() + ": " + e.getMessage(), "batchjobhttperror");
        } catch (org.springframework.web.reactive.function.client.WebClientRequestException e) {
            log.error("Connection error to batch job service: {}", e.getMessage(), e);
            throw new BatchJobException("Batch job service is not available", "batchjobunavailable");
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (BatchJobException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error triggering Stripe fees tax update batch job", e);
            throw new BatchJobException("Failed to trigger batch job: " + e.getMessage(), "batchjobsubmissionfailed");
        }
    }

    /**
     * Trigger Stripe fees and tax update for a specific tenant.
     *
     * @param tenantId Tenant ID
     * @return Response with job ID and status
     */
    public StripeFeesTaxUpdateResponse triggerStripeFeesTaxUpdate(String tenantId) {
        StripeFeesTaxUpdateRequest request = new StripeFeesTaxUpdateRequest();
        request.setTenantId(tenantId);
        return triggerStripeFeesTaxUpdate(request);
    }

    /**
     * Trigger Stripe fees and tax update for all tenants.
     *
     * @return Response with job ID and status
     */
    public StripeFeesTaxUpdateResponse triggerStripeFeesTaxUpdate() {
        return triggerStripeFeesTaxUpdate(new StripeFeesTaxUpdateRequest());
    }

    /**
     * Trigger Stripe ticket batch refund job.
     *
     * @param request the Stripe ticket batch refund request
     * @return the batch job response
     * @throws BatchJobException if job submission fails
     */
    public StripeTicketBatchRefundResponse triggerStripeTicketBatchRefund(StripeTicketBatchRefundRequest request) {
        if (!batchJobProperties.getEnabled()) {
            log.warn("Batch job service is disabled. Skipping Stripe ticket batch refund trigger.");
            throw new BatchJobException("Batch job service is disabled", "batchjobdisabled");
        }

        // Validate request
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        if (request.getEventId() == null) {
            throw new IllegalArgumentException("eventId is required");
        }

        if (request.getTenantId() == null || request.getTenantId().isEmpty()) {
            throw new IllegalArgumentException("tenantId is required");
        }

        if (request.getJobId() == null || request.getJobId().isEmpty()) {
            throw new IllegalArgumentException("jobId is required");
        }

        if (request.getStartDate() != null && request.getEndDate() != null) {
            if (request.getStartDate().isAfter(request.getEndDate())) {
                throw new IllegalArgumentException("startDate must be before or equal to endDate");
            }
        }

        try {
            log.info(
                "Triggering Stripe ticket batch refund job - jobId: {}, eventId: {}, tenantId: {}, startDate: {}, endDate: {}",
                request.getJobId(),
                request.getEventId(),
                request.getTenantId(),
                request.getStartDate(),
                request.getEndDate()
            );

            // Call batch job service
            StripeTicketBatchRefundResponse response = webClient
                .post()
                .uri(STRIPE_TICKET_BATCH_REFUND_ENDPOINT)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(StripeTicketBatchRefundResponse.class)
                .timeout(Duration.ofMillis(batchJobProperties.getTimeout()))
                .block();

            if (response == null) {
                throw new BatchJobException("Received null response from batch job service", "nullresponse");
            }

            log.info(
                "Stripe ticket batch refund job triggered successfully. JobId: {}, Status: {}, TotalEligibleTickets: {}",
                response.getJobId(),
                response.getStatus(),
                response.getTotalEligibleTickets()
            );

            return response;
        } catch (WebClientResponseException.BadRequest e) {
            log.warn("Bad request to batch job service: {}", e.getResponseBodyAsString(), e);
            throw new IllegalArgumentException("Invalid request: " + e.getResponseBodyAsString(), e);
        } catch (WebClientResponseException e) {
            log.error("HTTP error triggering Stripe ticket batch refund job: {} - {}", e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new BatchJobException("HTTP " + e.getStatusCode() + ": " + e.getMessage(), "batchjobhttperror");
        } catch (org.springframework.web.reactive.function.client.WebClientRequestException e) {
            log.error("Connection error to batch job service: {}", e.getMessage(), e);
            throw new BatchJobException("Batch job service is not available", "batchjobunavailable");
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (BatchJobException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error triggering Stripe ticket batch refund job", e);
            throw new BatchJobException("Failed to trigger batch job: " + e.getMessage(), "batchjobsubmissionfailed");
        }
    }

    public BatchJobExecutionPageResponseDTO getExecutions(
        String status,
        String jobName,
        String tenantId,
        ZonedDateTime startedAfter,
        ZonedDateTime startedBefore,
        Integer page,
        Integer size,
        String sort,
        String authHeader
    ) {
        requireBatchJobsEnabled();
        try {
            BatchJobExecutionPageResponseDTO response = withForwardedAuth(
                webClient
                    .get()
                    .uri(uriBuilder -> {
                        uriBuilder.path(MONITORING_EXECUTIONS_ENDPOINT);
                        if (StringUtils.hasText(status)) {
                            uriBuilder.queryParam("status", status);
                        }
                        if (StringUtils.hasText(jobName)) {
                            uriBuilder.queryParam("jobName", jobName);
                        }
                        if (StringUtils.hasText(tenantId)) {
                            uriBuilder.queryParam("tenantId", tenantId);
                        }
                        if (startedAfter != null) {
                            uriBuilder.queryParam("startedAfter", startedAfter);
                        }
                        if (startedBefore != null) {
                            uriBuilder.queryParam("startedBefore", startedBefore);
                        }
                        if (page != null) {
                            uriBuilder.queryParam("page", page);
                        }
                        if (size != null) {
                            uriBuilder.queryParam("size", size);
                        }
                        if (StringUtils.hasText(sort)) {
                            uriBuilder.queryParam("sort", sort);
                        }
                        return uriBuilder.build();
                    }),
                authHeader
            )
                .retrieve()
                .bodyToMono(BatchJobExecutionPageResponseDTO.class)
                .timeout(Duration.ofMillis(batchJobProperties.getTimeout()))
                .block();

            if (response == null) {
                throw new BatchJobException("Received null response from batch job service", "nullresponse");
            }
            return response;
        } catch (WebClientResponseException e) {
            log.error("HTTP error fetching batch job executions: {} - {}", e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new BatchJobException("HTTP " + e.getStatusCode() + ": " + e.getMessage(), "batchjobhttperror");
        } catch (BatchJobException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error fetching batch job executions", e);
            throw new BatchJobException("Failed to fetch batch job executions: " + e.getMessage(), "batchjobfetchfailed");
        }
    }

    public BatchJobExecutionDTO getExecution(Long id, String authHeader) {
        requireBatchJobsEnabled();
        try {
            BatchJobExecutionDTO response = withForwardedAuth(webClient.get().uri(MONITORING_EXECUTION_BY_ID_ENDPOINT, id), authHeader)
                .retrieve()
                .bodyToMono(BatchJobExecutionDTO.class)
                .timeout(Duration.ofMillis(batchJobProperties.getTimeout()))
                .block();

            if (response == null) {
                throw new BatchJobException("Received null response from batch job service", "nullresponse");
            }
            return response;
        } catch (WebClientResponseException.NotFound e) {
            throw new BatchJobException("Batch job execution not found: " + id, "batchjobnotfound");
        } catch (WebClientResponseException e) {
            log.error("HTTP error fetching batch job execution {}: {} - {}", id, e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new BatchJobException("HTTP " + e.getStatusCode() + ": " + e.getMessage(), "batchjobhttperror");
        } catch (BatchJobException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error fetching batch job execution {}", id, e);
            throw new BatchJobException("Failed to fetch batch job execution: " + e.getMessage(), "batchjobfetchfailed");
        }
    }

    public List<BatchJobExecutionDTO> getFailedExecutions(Integer limit, String authHeader) {
        requireBatchJobsEnabled();
        return fetchExecutionList(MONITORING_FAILED_EXECUTIONS_ENDPOINT, limit, authHeader);
    }

    public List<BatchJobExecutionDTO> getRunningExecutions(Integer limit, String authHeader) {
        requireBatchJobsEnabled();
        return fetchExecutionList(MONITORING_RUNNING_EXECUTIONS_ENDPOINT, limit, authHeader);
    }

    public BatchJobSummaryResponseDTO getSummary(
        String tenantId,
        ZonedDateTime startedAfter,
        ZonedDateTime startedBefore,
        String authHeader
    ) {
        requireBatchJobsEnabled();
        try {
            BatchJobSummaryResponseDTO response = withForwardedAuth(
                webClient
                    .get()
                    .uri(uriBuilder -> {
                        uriBuilder.path(MONITORING_SUMMARY_ENDPOINT);
                        if (StringUtils.hasText(tenantId)) {
                            uriBuilder.queryParam("tenantId", tenantId);
                        }
                        if (startedAfter != null) {
                            uriBuilder.queryParam("startedAfter", startedAfter);
                        }
                        if (startedBefore != null) {
                            uriBuilder.queryParam("startedBefore", startedBefore);
                        }
                        return uriBuilder.build();
                    }),
                authHeader
            )
                .retrieve()
                .bodyToMono(BatchJobSummaryResponseDTO.class)
                .timeout(Duration.ofMillis(batchJobProperties.getTimeout()))
                .block();

            if (response == null) {
                throw new BatchJobException("Received null response from batch job service", "nullresponse");
            }
            return response;
        } catch (WebClientResponseException e) {
            log.error("HTTP error fetching batch job summary: {} - {}", e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new BatchJobException("HTTP " + e.getStatusCode() + ": " + e.getMessage(), "batchjobhttperror");
        } catch (BatchJobException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error fetching batch job summary", e);
            throw new BatchJobException("Failed to fetch batch job summary: " + e.getMessage(), "batchjobfetchfailed");
        }
    }

    public List<ConfiguredBatchJobResponseDTO> getConfiguredJobs(String authHeader) {
        requireBatchJobsEnabled();
        try {
            List<ConfiguredBatchJobResponseDTO> response = withForwardedAuth(
                webClient.get().uri(MONITORING_CONFIGURED_JOBS_ENDPOINT),
                authHeader
            )
                .retrieve()
                .bodyToMono(CONFIGURED_JOBS_LIST_TYPE)
                .timeout(Duration.ofMillis(batchJobProperties.getTimeout()))
                .block();

            if (response == null) {
                throw new BatchJobException("Received null response from batch job service", "nullresponse");
            }
            return response;
        } catch (WebClientResponseException e) {
            log.error("HTTP error fetching configured batch jobs: {} - {}", e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new BatchJobException("HTTP " + e.getStatusCode() + ": " + e.getMessage(), "batchjobhttperror");
        } catch (BatchJobException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error fetching configured batch jobs", e);
            throw new BatchJobException("Failed to fetch configured batch jobs: " + e.getMessage(), "batchjobfetchfailed");
        }
    }

    private List<BatchJobExecutionDTO> fetchExecutionList(String endpoint, Integer limit, String authHeader) {
        try {
            List<BatchJobExecutionDTO> response = withForwardedAuth(
                webClient
                    .get()
                    .uri(uriBuilder -> {
                        uriBuilder.path(endpoint);
                        if (limit != null) {
                            uriBuilder.queryParam("limit", limit);
                        }
                        return uriBuilder.build();
                    }),
                authHeader
            )
                .retrieve()
                .bodyToMono(EXECUTION_LIST_TYPE)
                .timeout(Duration.ofMillis(batchJobProperties.getTimeout()))
                .block();

            if (response == null) {
                throw new BatchJobException("Received null response from batch job service", "nullresponse");
            }
            return response;
        } catch (WebClientResponseException e) {
            log.error(
                "HTTP error fetching batch job executions from {}: {} - {}",
                endpoint,
                e.getStatusCode(),
                e.getResponseBodyAsString(),
                e
            );
            throw new BatchJobException("HTTP " + e.getStatusCode() + ": " + e.getMessage(), "batchjobhttperror");
        } catch (BatchJobException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error fetching batch job executions from {}", endpoint, e);
            throw new BatchJobException("Failed to fetch batch job executions: " + e.getMessage(), "batchjobfetchfailed");
        }
    }

    private void requireBatchJobsEnabled() {
        if (!batchJobProperties.getEnabled()) {
            throw new BatchJobException("Batch job service is disabled", "batchjobdisabled");
        }
    }

    private WebClient.RequestHeadersSpec<?> withForwardedAuth(WebClient.RequestHeadersSpec<?> spec, String authHeader) {
        if (StringUtils.hasText(authHeader)) {
            return spec.header(HttpHeaders.AUTHORIZATION, authHeader);
        }
        return spec;
    }
}
