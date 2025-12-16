package com.nextjstemplate.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextjstemplate.config.BatchJobProperties;
import com.nextjstemplate.service.dto.BatchJobRequest;
import com.nextjstemplate.service.dto.BatchJobResponse;
import com.nextjstemplate.service.dto.BatchJobServiceRequest;
import com.nextjstemplate.service.dto.BatchJobServiceResponse;
import com.nextjstemplate.web.rest.errors.BatchJobException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Service for submitting batch jobs to the batch job microservice via REST API.
 */
@Service
public class BatchJobService {

    private static final Logger log = LoggerFactory.getLogger(BatchJobService.class);

    private static final String SUBSCRIPTION_RENEWAL_ENDPOINT = "/api/batch-jobs/subscription-renewal";
    private static final int DEFAULT_BATCH_SIZE = 100;
    private static final int DEFAULT_MAX_SUBSCRIPTIONS = 10000;
    private static final String DEFAULT_ESTIMATED_DURATION = "15-30 minutes";

    private final WebClient webClient;
    private final BatchJobProperties batchJobProperties;
    private final ObjectMapper objectMapper;

    public BatchJobService(BatchJobProperties batchJobProperties, ObjectMapper objectMapper) {
        this.batchJobProperties = batchJobProperties;
        this.objectMapper = objectMapper;
        this.webClient =
            WebClient
                .builder()
                .baseUrl(batchJobProperties.getUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

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
}
