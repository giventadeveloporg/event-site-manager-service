package com.nextjstemplate.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextjstemplate.config.BatchJobProperties;
import com.nextjstemplate.service.ManualPaymentBatchJobService;
import com.nextjstemplate.service.dto.ManualPaymentConfirmationEmailJobRequest;
import com.nextjstemplate.service.dto.ManualPaymentConfirmationEmailJobResponse;
import com.nextjstemplate.service.dto.ManualPaymentTicketEmailJobRequest;
import com.nextjstemplate.service.dto.ManualPaymentTicketEmailJobResponse;
import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Implementation of ManualPaymentBatchJobService using WebClient to call
 * the Event Site Manager Batch Jobs microservice.
 *
 * Follows the same pattern as {@link com.nextjstemplate.service.impl.ContactFormBatchJobServiceImpl}.
 */
@Service
public class ManualPaymentBatchJobServiceImpl implements ManualPaymentBatchJobService {

    private static final Logger log = LoggerFactory.getLogger(ManualPaymentBatchJobServiceImpl.class);

    private static final String MANUAL_PAYMENT_CONFIRMATION_EMAIL_ENDPOINT = "/api/batch-jobs/manual-payment-confirmation-email";
    private static final String MANUAL_PAYMENT_TICKET_EMAIL_ENDPOINT = "/api/batch-jobs/manual-payment-ticket-email";

    private final WebClient webClient;
    private final BatchJobProperties batchJobProperties;
    private final ObjectMapper objectMapper;

    public ManualPaymentBatchJobServiceImpl(BatchJobProperties batchJobProperties, ObjectMapper objectMapper) {
        this.batchJobProperties = batchJobProperties;
        this.objectMapper = objectMapper;
        this.webClient =
            WebClient
                .builder()
                .baseUrl(batchJobProperties.getUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        log.info("ManualPaymentBatchJobService initialized with URL: {}", batchJobProperties.getUrl());
    }

    @Override
    public ManualPaymentConfirmationEmailJobResponse triggerConfirmationEmailJob(ManualPaymentConfirmationEmailJobRequest request) {
        if (!batchJobProperties.getEnabled()) {
            log.warn("Batch job service is disabled. Skipping manual payment confirmation email batch trigger.");
            ManualPaymentConfirmationEmailJobResponse response = new ManualPaymentConfirmationEmailJobResponse();
            response.setSuccess(false);
            response.setMessage("Batch job service is disabled");
            return response;
        }

        if (
            request == null || request.getTenantId() == null || request.getPaymentRequestId() == null || request.getRecipientEmail() == null
        ) {
            log.error("Invalid ManualPaymentConfirmationEmailJobRequest: {}", request);
            ManualPaymentConfirmationEmailJobResponse response = new ManualPaymentConfirmationEmailJobResponse();
            response.setSuccess(false);
            response.setMessage("TenantId, paymentRequestId, and recipientEmail are required");
            return response;
        }

        try {
            if (log.isDebugEnabled()) {
                try {
                    log.debug(
                        "Triggering manual payment confirmation email batch job with request: {}",
                        objectMapper != null ? objectMapper.writeValueAsString(request) : request.toString()
                    );
                } catch (Exception e) {
                    log.debug("Failed to serialize ManualPaymentConfirmationEmailJobRequest for logging: {}", e.getMessage());
                }
            } else {
                log.info(
                    "Triggering manual payment confirmation email batch job - tenantId: {}, paymentRequestId: {}, recipientEmail: {}",
                    request.getTenantId(),
                    request.getPaymentRequestId(),
                    request.getRecipientEmail()
                );
            }

            ManualPaymentConfirmationEmailJobResponse response = webClient
                .post()
                .uri(MANUAL_PAYMENT_CONFIRMATION_EMAIL_ENDPOINT)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ManualPaymentConfirmationEmailJobResponse.class)
                .timeout(Duration.ofMillis(batchJobProperties.getTimeout()))
                .block();

            if (response != null && Boolean.TRUE.equals(response.getSuccess())) {
                log.info(
                    "Manual payment confirmation email batch job triggered successfully. JobExecutionId: {}, ProcessedCount: {}",
                    response.getJobExecutionId(),
                    response.getProcessedCount()
                );
            } else {
                log.warn(
                    "Manual payment confirmation email batch job triggered but response indicates failure: {}",
                    response != null ? response.getMessage() : "null response"
                );
            }

            return response != null ? response : createErrorConfirmationResponse("Received null response from batch job service");
        } catch (WebClientResponseException e) {
            log.error(
                "HTTP error triggering manual payment confirmation email batch job: {} - {}",
                e.getStatusCode(),
                e.getResponseBodyAsString(),
                e
            );
            return createErrorConfirmationResponse("HTTP " + e.getStatusCode() + ": " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error triggering manual payment confirmation email batch job", e);
            return createErrorConfirmationResponse("Failed to trigger confirmation email batch job: " + e.getMessage());
        }
    }

    @Override
    public ManualPaymentTicketEmailJobResponse triggerTicketEmailJob(ManualPaymentTicketEmailJobRequest request) {
        if (!batchJobProperties.getEnabled()) {
            log.warn("Batch job service is disabled. Skipping manual payment ticket email batch trigger.");
            ManualPaymentTicketEmailJobResponse response = new ManualPaymentTicketEmailJobResponse();
            response.setSuccess(false);
            response.setMessage("Batch job service is disabled");
            return response;
        }

        if (
            request == null ||
            request.getTenantId() == null ||
            request.getPaymentRequestId() == null ||
            request.getTicketTransactionId() == null ||
            request.getRecipientEmail() == null
        ) {
            log.error("Invalid ManualPaymentTicketEmailJobRequest: {}", request);
            ManualPaymentTicketEmailJobResponse response = new ManualPaymentTicketEmailJobResponse();
            response.setSuccess(false);
            response.setMessage("TenantId, paymentRequestId, ticketTransactionId, and recipientEmail are required");
            return response;
        }

        try {
            if (log.isDebugEnabled()) {
                try {
                    log.debug(
                        "Triggering manual payment ticket email batch job with request: {}",
                        objectMapper != null ? objectMapper.writeValueAsString(request) : request.toString()
                    );
                } catch (Exception e) {
                    log.debug("Failed to serialize ManualPaymentTicketEmailJobRequest for logging: {}", e.getMessage());
                }
            } else {
                log.info(
                    "Triggering manual payment ticket email batch job - tenantId: {}, paymentRequestId: {}, ticketTransactionId: {}, recipientEmail: {}",
                    request.getTenantId(),
                    request.getPaymentRequestId(),
                    request.getTicketTransactionId(),
                    request.getRecipientEmail()
                );
            }

            ManualPaymentTicketEmailJobResponse response = webClient
                .post()
                .uri(MANUAL_PAYMENT_TICKET_EMAIL_ENDPOINT)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ManualPaymentTicketEmailJobResponse.class)
                .timeout(Duration.ofMillis(batchJobProperties.getTimeout()))
                .block();

            if (response != null && Boolean.TRUE.equals(response.getSuccess())) {
                log.info(
                    "Manual payment ticket email batch job triggered successfully. JobExecutionId: {}, ProcessedCount: {}",
                    response.getJobExecutionId(),
                    response.getProcessedCount()
                );
            } else {
                log.warn(
                    "Manual payment ticket email batch job triggered but response indicates failure: {}",
                    response != null ? response.getMessage() : "null response"
                );
            }

            return response != null ? response : createErrorTicketResponse("Received null response from batch job service");
        } catch (WebClientResponseException e) {
            log.error(
                "HTTP error triggering manual payment ticket email batch job: {} - {}",
                e.getStatusCode(),
                e.getResponseBodyAsString(),
                e
            );
            return createErrorTicketResponse("HTTP " + e.getStatusCode() + ": " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error triggering manual payment ticket email batch job", e);
            return createErrorTicketResponse("Failed to trigger ticket email batch job: " + e.getMessage());
        }
    }

    private ManualPaymentConfirmationEmailJobResponse createErrorConfirmationResponse(String message) {
        ManualPaymentConfirmationEmailJobResponse response = new ManualPaymentConfirmationEmailJobResponse();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }

    private ManualPaymentTicketEmailJobResponse createErrorTicketResponse(String message) {
        ManualPaymentTicketEmailJobResponse response = new ManualPaymentTicketEmailJobResponse();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }
}
