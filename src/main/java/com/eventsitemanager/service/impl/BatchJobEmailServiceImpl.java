package com.eventsitemanager.service.impl;

import com.eventsitemanager.properties.BatchJobProperties;
import com.eventsitemanager.service.BatchJobEmailService;
import com.eventsitemanager.service.dto.BatchJobEmailRequest;
import com.eventsitemanager.service.dto.BatchJobEmailResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Implementation of BatchJobEmailService for triggering batch email jobs via the batch jobs microservice.
 */
@Service
public class BatchJobEmailServiceImpl implements BatchJobEmailService {

    private static final Logger log = LoggerFactory.getLogger(BatchJobEmailServiceImpl.class);
    private static final String EMAIL_BATCH_ENDPOINT = "/api/batch-jobs/email";
    private static final Integer DEFAULT_BATCH_SIZE = 50;
    private static final Integer DEFAULT_MAX_EMAILS = 10000;

    private final WebClient webClient;
    private final BatchJobProperties batchJobProperties;
    private final ObjectMapper objectMapper;

    public BatchJobEmailServiceImpl(
        BatchJobProperties batchJobProperties,
        ObjectMapper objectMapper,
        @Qualifier("batchJobsWebClient") WebClient batchJobsWebClient
    ) {
        this.batchJobProperties = batchJobProperties;
        this.objectMapper = objectMapper;
        this.webClient = batchJobsWebClient;

        log.info("BatchJobEmailService initialized with URL: {}", batchJobProperties.getUrl());
    }

    @Override
    public BatchJobEmailResponse triggerEmailBatch(
        String tenantId,
        Long templateId,
        Integer batchSize,
        Integer maxEmails,
        List<String> recipientEmails,
        Long userId,
        String recipientType
    ) {
        if (!batchJobProperties.getEnabled()) {
            log.warn("Batch job service is disabled. Skipping email batch trigger.");
            return createDisabledResponse();
        }

        if (tenantId == null || templateId == null) {
            log.error("TenantId and TemplateId are required for batch email job");
            return createErrorResponse("TenantId and TemplateId are required");
        }

        if (recipientType == null || recipientType.isEmpty()) {
            log.error("RecipientType is required for batch email job");
            return createErrorResponse("RecipientType is required. Use RecipientType.EVENT_ATTENDEES or RecipientType.SUBSCRIBED_MEMBERS");
        }

        BatchJobEmailRequest request = new BatchJobEmailRequest(
            tenantId,
            templateId,
            batchSize != null ? batchSize : DEFAULT_BATCH_SIZE,
            maxEmails != null ? maxEmails : DEFAULT_MAX_EMAILS,
            recipientEmails,
            userId,
            recipientType
        );

        log.info(
            "Triggering email batch job - tenantId: {}, templateId: {}, batchSize: {}, maxEmails: {}, recipientCount: {}, userId: {}, recipientType: {}",
            tenantId,
            templateId,
            request.getBatchSize(),
            request.getMaxEmails(),
            recipientEmails != null ? recipientEmails.size() : "null",
            userId,
            recipientType
        );

        try {
            BatchJobEmailResponse response = webClient
                .post()
                .uri(EMAIL_BATCH_ENDPOINT)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(BatchJobEmailResponse.class)
                .timeout(Duration.ofMillis(batchJobProperties.getTimeout()))
                .block();

            if (response != null && Boolean.TRUE.equals(response.getSuccess())) {
                log.info(
                    "Email batch job triggered successfully. JobExecutionId: {}, ProcessedCount: {}",
                    response.getJobExecutionId(),
                    response.getProcessedCount()
                );
            } else {
                log.warn(
                    "Email batch job triggered but response indicates failure: {}",
                    response != null ? response.getMessage() : "null response"
                );
            }

            return response != null ? response : createErrorResponse("Received null response from batch job service");
        } catch (WebClientResponseException e) {
            log.error("HTTP error triggering email batch job: {} - {}", e.getStatusCode(), e.getResponseBodyAsString(), e);
            return createErrorResponse("HTTP " + e.getStatusCode() + ": " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error triggering email batch job", e);
            return createErrorResponse("Failed to trigger batch job: " + e.getMessage());
        }
    }

    @Override
    public BatchJobEmailResponse triggerEmailBatch(String tenantId, Long templateId, Long userId, String recipientType) {
        return triggerEmailBatch(tenantId, templateId, DEFAULT_BATCH_SIZE, DEFAULT_MAX_EMAILS, null, userId, recipientType);
    }

    @Override
    public BatchJobEmailResponse triggerEmailBatch(
        String tenantId,
        Long templateId,
        List<String> recipientEmails,
        Long userId,
        String recipientType
    ) {
        return triggerEmailBatch(tenantId, templateId, DEFAULT_BATCH_SIZE, DEFAULT_MAX_EMAILS, recipientEmails, userId, recipientType);
    }

    private BatchJobEmailResponse createDisabledResponse() {
        BatchJobEmailResponse response = new BatchJobEmailResponse();
        response.setSuccess(false);
        response.setMessage("Batch job service is disabled");
        return response;
    }

    private BatchJobEmailResponse createErrorResponse(String message) {
        BatchJobEmailResponse response = new BatchJobEmailResponse();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }
}
