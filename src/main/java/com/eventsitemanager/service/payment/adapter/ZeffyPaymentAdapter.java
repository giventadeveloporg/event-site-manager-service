package com.eventsitemanager.service.payment.adapter;

import com.eventsitemanager.domain.enumeration.PaymentMethod;
import com.eventsitemanager.domain.enumeration.PaymentProvider;
import com.eventsitemanager.service.payment.PaymentException;
import com.eventsitemanager.service.payment.PaymentService;
import com.eventsitemanager.service.payment.dto.PaymentSessionRequest;
import com.eventsitemanager.service.payment.dto.PaymentSessionResponse;
import com.eventsitemanager.service.payment.dto.RefundRequest;
import com.eventsitemanager.service.payment.dto.RefundResponse;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Zeffy payment adapter implementing PaymentService interface.
 * Zeffy is a zero-fee donation platform. This adapter creates donation campaigns
 * and provides embedded URLs for donation flows.
 */
@Service
public class ZeffyPaymentAdapter implements PaymentService {

    private static final Logger log = LoggerFactory.getLogger(ZeffyPaymentAdapter.class);

    @Override
    public String getProviderName() {
        return PaymentProvider.ZEFFY.name();
    }

    @Override
    public PaymentSessionResponse initialize(PaymentSessionRequest request, Map<String, Object> providerConfig) throws PaymentException {
        log.info("Initializing Zeffy donation for tenant: {}, amount: {}", request.getTenantId(), request.getAmount());

        try {
            String apiKey = (String) providerConfig.get("apiKey");
            if (apiKey == null || apiKey.isEmpty()) {
                throw new PaymentException("ZEFFY_CONFIG_ERROR", "Zeffy API key not configured");
            }

            // Zeffy typically uses embedded donation forms
            // For now, we'll create a pending transaction and return campaign URL
            String campaignId = (String) providerConfig.get("campaignId");
            String baseUrl = (String) providerConfig.getOrDefault("baseUrl", "https://www.zeffy.com");

            PaymentSessionResponse response = new PaymentSessionResponse();
            response.setProvider(PaymentProvider.ZEFFY);
            response.setAmount(request.getAmount());
            response.setCurrency(request.getCurrency());
            response.setStatus("PENDING");

            // Generate campaign URL or embedded URL
            String campaignUrl = baseUrl + "/donation/" + (campaignId != null ? campaignId : "default");
            response.setSessionUrl(campaignUrl);

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("zeffyCampaignId", campaignId);
            metadata.put("campaignUrl", campaignUrl);
            metadata.put("externalTransactionId", UUID.randomUUID().toString());
            response.setProviderMetadata(metadata);

            List<PaymentMethod> supportedMethods = new ArrayList<>();
            supportedMethods.add(PaymentMethod.CARD);
            supportedMethods.add(PaymentMethod.BANK_TRANSFER);
            response.setSupportedMethods(supportedMethods);

            return response;
        } catch (Exception e) {
            log.error("Zeffy payment initialization failed", e);
            throw new PaymentException("ZEFFY_ERROR", "Failed to initialize Zeffy donation: " + e.getMessage(), e);
        }
    }

    @Override
    public PaymentSessionResponse confirm(String transactionId, Map<String, Object> confirmationData, Map<String, Object> providerConfig)
        throws PaymentException {
        // Zeffy donations are typically confirmed via webhook or CSV import
        // This method would be called after manual verification
        log.info("Confirming Zeffy donation: {}", transactionId);

        PaymentSessionResponse response = new PaymentSessionResponse();
        response.setProvider(PaymentProvider.ZEFFY);
        response.setTransactionId(transactionId);
        response.setStatus("COMPLETED");

        return response;
    }

    @Override
    public void cancel(String transactionId, Map<String, Object> providerConfig) throws PaymentException {
        log.info("Cancelling Zeffy donation: {}", transactionId);
        // Zeffy donations cannot be cancelled once initiated
        log.warn("Zeffy donations cannot be cancelled via API");
    }

    @Override
    public RefundResponse refund(RefundRequest request, Map<String, Object> providerConfig) throws PaymentException {
        log.info("Processing Zeffy refund for transaction: {}", request.getTransactionId());
        // Zeffy refunds are typically handled manually
        throw new PaymentException("ZEFFY_REFUND_NOT_SUPPORTED", "Zeffy refunds must be processed manually");
    }

    @Override
    public PaymentSessionResponse getStatus(String transactionId, Map<String, Object> providerConfig) throws PaymentException {
        log.debug("Getting Zeffy donation status: {}", transactionId);

        PaymentSessionResponse response = new PaymentSessionResponse();
        response.setProvider(PaymentProvider.ZEFFY);
        response.setTransactionId(transactionId);
        response.setStatus("PENDING"); // Status would be updated via CSV import or webhook

        return response;
    }

    @Override
    public Map<String, Object> handleWebhook(String payload, String signature, Map<String, Object> providerConfig) throws PaymentException {
        log.info("Handling Zeffy webhook");

        try {
            // Parse webhook payload (format depends on Zeffy API)
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            Map<String, Object> event = mapper.readValue(payload, Map.class);

            Map<String, Object> result = new HashMap<>();
            result.put("processed", true);
            result.put("eventType", event.get("event_type"));
            result.put("donationId", event.get("donation_id"));

            return result;
        } catch (Exception e) {
            log.error("Zeffy webhook processing failed", e);
            throw new PaymentException("WEBHOOK_ERROR", "Failed to process webhook: " + e.getMessage(), e);
        }
    }

    @Override
    public List<PaymentMethod> supportedMethods(Map<String, Object> providerConfig) {
        List<PaymentMethod> methods = new ArrayList<>();
        methods.add(PaymentMethod.CARD);
        methods.add(PaymentMethod.BANK_TRANSFER);
        return methods;
    }
}
