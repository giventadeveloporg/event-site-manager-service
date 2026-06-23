package com.eventsitemanager.service.impl;

import com.eventsitemanager.properties.BatchJobProperties;
import com.eventsitemanager.service.PromotionTestEmailBatchJobService;
import com.eventsitemanager.service.dto.PromotionTestEmailJobRequest;
import com.eventsitemanager.service.dto.PromotionTestEmailJobResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Implementation of PromotionTestEmailBatchJobService using WebClient to call
 * the Event Site Manager Batch Jobs microservice.
 *
 * Mirrors {@link ContactFormBatchJobServiceImpl} pattern.
 */
@Service
public class PromotionTestEmailBatchJobServiceImpl implements PromotionTestEmailBatchJobService {

    private static final Logger log = LoggerFactory.getLogger(PromotionTestEmailBatchJobServiceImpl.class);

    private static final String PROMOTION_TEST_EMAIL_ENDPOINT = "/api/batch-jobs/promotion-test-email";

    private final WebClient webClient;
    private final BatchJobProperties batchJobProperties;
    private final ObjectMapper objectMapper;

    public PromotionTestEmailBatchJobServiceImpl(
        BatchJobProperties batchJobProperties,
        ObjectMapper objectMapper,
        @Qualifier("batchJobsWebClient") WebClient batchJobsWebClient
    ) {
        this.batchJobProperties = batchJobProperties;
        this.objectMapper = objectMapper;
        this.webClient = batchJobsWebClient;

        log.info("PromotionTestEmailBatchJobService initialized with URL: {}", batchJobProperties.getUrl());
    }

    @Override
    public PromotionTestEmailJobResponse triggerPromotionTestEmailJob(PromotionTestEmailJobRequest request) {
        if (!batchJobProperties.getEnabled()) {
            log.warn("Batch job service is disabled. Skipping promotion test email batch trigger.");
            PromotionTestEmailJobResponse response = new PromotionTestEmailJobResponse();
            response.setSuccess(false);
            response.setMessage("Batch job service is disabled");
            return response;
        }

        if (request == null || request.getTenantId() == null || request.getTemplateId() == null || request.getRecipientEmail() == null) {
            log.error("Invalid PromotionTestEmailJobRequest: {}", request);
            PromotionTestEmailJobResponse response = new PromotionTestEmailJobResponse();
            response.setSuccess(false);
            response.setMessage("tenantId, templateId, and recipientEmail are required");
            return response;
        }

        try {
            if (log.isDebugEnabled()) {
                try {
                    log.debug(
                        "Triggering promotion test email job with request: {}",
                        objectMapper != null ? objectMapper.writeValueAsString(request) : request.toString()
                    );
                } catch (Exception e) {
                    log.debug("Failed to serialize PromotionTestEmailJobRequest for logging: {}", e.getMessage());
                }
            } else {
                log.info(
                    "Triggering promotion test email job - tenantId: {}, templateId: {}, recipientEmail: {}",
                    request.getTenantId(),
                    request.getTemplateId(),
                    request.getRecipientEmail()
                );
            }

            PromotionTestEmailJobResponse response = webClient
                .post()
                .uri(PROMOTION_TEST_EMAIL_ENDPOINT)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(PromotionTestEmailJobResponse.class)
                .timeout(Duration.ofMillis(batchJobProperties.getTimeout()))
                .block();

            if (response != null && Boolean.TRUE.equals(response.getSuccess())) {
                log.info("Promotion test email job triggered successfully. JobExecutionId: {}", response.getJobExecutionId());
            } else {
                log.warn(
                    "Promotion test email job triggered but response indicates failure: {}",
                    response != null ? response.getMessage() : "null response"
                );
            }

            return response != null ? response : createErrorResponse("Received null response from batch job service");
        } catch (WebClientResponseException e) {
            log.error("HTTP error triggering promotion test email job: {} - {}", e.getStatusCode(), e.getResponseBodyAsString(), e);
            return createErrorResponse("HTTP " + e.getStatusCode() + ": " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error triggering promotion test email job", e);
            return createErrorResponse("Failed to trigger promotion test email job: " + e.getMessage());
        }
    }

    private PromotionTestEmailJobResponse createErrorResponse(String message) {
        PromotionTestEmailJobResponse response = new PromotionTestEmailJobResponse();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }
}
