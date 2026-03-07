package com.nextjstemplate.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextjstemplate.properties.BatchJobProperties;
import com.nextjstemplate.service.ContactFormBatchJobService;
import com.nextjstemplate.service.dto.ContactFormEmailJobRequest;
import com.nextjstemplate.service.dto.ContactFormEmailJobResponse;
import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Implementation of ContactFormBatchJobService using WebClient to call
 * the Event Site Manager Batch Jobs microservice.
 *
 * Follows the same pattern as {@link com.nextjstemplate.service.impl.BatchJobEmailServiceImpl}.
 */
@Service
public class ContactFormBatchJobServiceImpl implements ContactFormBatchJobService {

    private static final Logger log = LoggerFactory.getLogger(ContactFormBatchJobServiceImpl.class);

    private static final String CONTACT_FORM_EMAIL_ENDPOINT = "/api/batch-jobs/contact-form-email";

    private final WebClient webClient;
    private final BatchJobProperties batchJobProperties;
    private final ObjectMapper objectMapper;

    public ContactFormBatchJobServiceImpl(BatchJobProperties batchJobProperties, ObjectMapper objectMapper) {
        this.batchJobProperties = batchJobProperties;
        this.objectMapper = objectMapper;
        this.webClient =
            WebClient
                .builder()
                .baseUrl(batchJobProperties.getUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        log.info("ContactFormBatchJobService initialized with URL: {}", batchJobProperties.getUrl());
    }

    @Override
    public ContactFormEmailJobResponse triggerContactFormEmailJob(ContactFormEmailJobRequest request) {
        if (!batchJobProperties.getEnabled()) {
            log.warn("Batch job service is disabled. Skipping contact form email batch trigger.");
            ContactFormEmailJobResponse response = new ContactFormEmailJobResponse();
            response.setSuccess(false);
            response.setMessage("Batch job service is disabled");
            return response;
        }

        if (request == null || request.getTenantId() == null || request.getFromEmail() == null || request.getToEmail() == null) {
            log.error("Invalid ContactFormEmailJobRequest: {}", request);
            ContactFormEmailJobResponse response = new ContactFormEmailJobResponse();
            response.setSuccess(false);
            response.setMessage("TenantId, fromEmail, and toEmail are required");
            return response;
        }

        try {
            if (log.isDebugEnabled()) {
                try {
                    log.debug(
                        "Triggering contact form email batch job with request: {}",
                        objectMapper != null ? objectMapper.writeValueAsString(request) : request.toString()
                    );
                } catch (Exception e) {
                    log.debug("Failed to serialize ContactFormEmailJobRequest for logging: {}", e.getMessage());
                }
            } else {
                log.info(
                    "Triggering contact form email batch job - tenantId: {}, fromEmail: {}, toEmail: {}",
                    request.getTenantId(),
                    request.getFromEmail(),
                    request.getToEmail()
                );
            }

            ContactFormEmailJobResponse response = webClient
                .post()
                .uri(CONTACT_FORM_EMAIL_ENDPOINT)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ContactFormEmailJobResponse.class)
                .timeout(Duration.ofMillis(batchJobProperties.getTimeout()))
                .block();

            if (response != null && Boolean.TRUE.equals(response.getSuccess())) {
                log.info(
                    "Contact form email batch job triggered successfully. JobExecutionId: {}, ProcessedCount: {}",
                    response.getJobExecutionId(),
                    response.getProcessedCount()
                );
            } else {
                log.warn(
                    "Contact form email batch job triggered but response indicates failure: {}",
                    response != null ? response.getMessage() : "null response"
                );
            }

            return response != null ? response : createErrorResponse("Received null response from batch job service");
        } catch (WebClientResponseException e) {
            log.error("HTTP error triggering contact form email batch job: {} - {}", e.getStatusCode(), e.getResponseBodyAsString(), e);
            return createErrorResponse("HTTP " + e.getStatusCode() + ": " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error triggering contact form email batch job", e);
            return createErrorResponse("Failed to trigger contact form batch job: " + e.getMessage());
        }
    }

    private ContactFormEmailJobResponse createErrorResponse(String message) {
        ContactFormEmailJobResponse response = new ContactFormEmailJobResponse();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }
}
