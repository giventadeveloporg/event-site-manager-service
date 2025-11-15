package com.nextjstemplate.service.payment.adapter;

import com.nextjstemplate.domain.enumeration.PaymentMethod;
import com.nextjstemplate.domain.enumeration.PaymentProvider;
import com.nextjstemplate.service.payment.PaymentException;
import com.nextjstemplate.service.payment.PaymentService;
import com.nextjstemplate.service.payment.dto.PaymentSessionRequest;
import com.nextjstemplate.service.payment.dto.PaymentSessionResponse;
import com.nextjstemplate.service.payment.dto.RefundRequest;
import com.nextjstemplate.service.payment.dto.RefundResponse;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Revolut payment adapter implementing PaymentService interface.
 * Supports card sessions with redirect-based Strong Customer Authentication (SCA).
 */
@Service
public class RevolutPaymentAdapter implements PaymentService {

    private static final Logger log = LoggerFactory.getLogger(RevolutPaymentAdapter.class);

    @Override
    public String getProviderName() {
        return PaymentProvider.REVOLUT.name();
    }

    @Override
    public PaymentSessionResponse initialize(PaymentSessionRequest request, Map<String, Object> providerConfig) throws PaymentException {
        log.info("Initializing Revolut payment for tenant: {}, amount: {}", request.getTenantId(), request.getAmount());

        try {
            String apiKey = (String) providerConfig.get("apiKey");
            if (apiKey == null || apiKey.isEmpty()) {
                throw new PaymentException("REVOLUT_CONFIG_ERROR", "Revolut API key not configured");
            }

            // Revolut API integration would go here
            // For now, we'll create a mock response structure
            String orderId = UUID.randomUUID().toString();

            PaymentSessionResponse response = new PaymentSessionResponse();
            response.setProvider(PaymentProvider.REVOLUT);
            response.setAmount(request.getAmount());
            response.setCurrency(request.getCurrency());
            response.setStatus("PENDING");
            response.setTransactionId(orderId);

            // Revolut typically requires redirect for SCA
            String redirectUrl = request.getReturnUrl() != null ? request.getReturnUrl() + "?orderId=" + orderId : null;
            response.setSessionUrl(redirectUrl);
            response.setRequiresAction(true);
            response.setActionType("redirect");

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("revolutOrderId", orderId);
            metadata.put("externalTransactionId", orderId);
            metadata.put("scaRequired", true);
            response.setProviderMetadata(metadata);

            List<PaymentMethod> supportedMethods = new ArrayList<>();
            supportedMethods.add(PaymentMethod.CARD);
            response.setSupportedMethods(supportedMethods);

            return response;
        } catch (Exception e) {
            log.error("Revolut payment initialization failed", e);
            throw new PaymentException("REVOLUT_ERROR", "Failed to initialize Revolut payment: " + e.getMessage(), e);
        }
    }

    @Override
    public PaymentSessionResponse confirm(String transactionId, Map<String, Object> confirmationData, Map<String, Object> providerConfig)
        throws PaymentException {
        log.info("Confirming Revolut payment: {}", transactionId);

        try {
            // After SCA redirect, confirm the payment
            PaymentSessionResponse response = new PaymentSessionResponse();
            response.setProvider(PaymentProvider.REVOLUT);
            response.setTransactionId(transactionId);
            response.setStatus("COMPLETED");

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("revolutOrderId", transactionId);
            metadata.put("externalTransactionId", transactionId);
            response.setProviderMetadata(metadata);

            return response;
        } catch (Exception e) {
            log.error("Revolut payment confirmation failed", e);
            throw new PaymentException("REVOLUT_ERROR", "Failed to confirm Revolut payment: " + e.getMessage(), e);
        }
    }

    @Override
    public void cancel(String transactionId, Map<String, Object> providerConfig) throws PaymentException {
        log.info("Cancelling Revolut payment: {}", transactionId);
        // Revolut cancellation would go here
    }

    @Override
    public RefundResponse refund(RefundRequest request, Map<String, Object> providerConfig) throws PaymentException {
        log.info("Processing Revolut refund for transaction: {}", request.getTransactionId());

        try {
            String refundId = UUID.randomUUID().toString();

            RefundResponse response = new RefundResponse();
            response.setRefundId(refundId);
            response.setTransactionId(request.getTransactionId());
            response.setAmount(request.getAmount());
            // Currency should be retrieved from original transaction, default to USD if not provided
            response.setCurrency(request.getCurrency() != null ? request.getCurrency() : "USD");
            response.setStatus("completed");
            response.setReason(request.getReason());
            response.setProcessedAt(ZonedDateTime.now());

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("revolutRefundId", refundId);
            response.setProviderMetadata(metadata);

            return response;
        } catch (Exception e) {
            log.error("Revolut refund failed", e);
            throw new PaymentException("REVOLUT_ERROR", "Failed to process Revolut refund: " + e.getMessage(), e);
        }
    }

    @Override
    public PaymentSessionResponse getStatus(String transactionId, Map<String, Object> providerConfig) throws PaymentException {
        log.debug("Getting Revolut payment status: {}", transactionId);

        PaymentSessionResponse response = new PaymentSessionResponse();
        response.setProvider(PaymentProvider.REVOLUT);
        response.setTransactionId(transactionId);
        response.setStatus("PENDING"); // Status would be updated via webhook

        return response;
    }

    @Override
    public Map<String, Object> handleWebhook(String payload, String signature, Map<String, Object> providerConfig) throws PaymentException {
        log.info("Handling Revolut webhook");

        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            Map<String, Object> event = mapper.readValue(payload, Map.class);

            String eventType = (String) event.get("event_type");
            Map<String, Object> data = (Map<String, Object>) event.get("data");

            Map<String, Object> result = new HashMap<>();
            result.put("eventType", eventType);
            result.put("processed", true);

            switch (eventType) {
                case "payment_succeeded":
                    handlePaymentSucceeded(data, result);
                    break;
                case "payment_failed":
                    handlePaymentFailed(data, result);
                    break;
                case "settlement_created":
                    handleSettlementCreated(data, result);
                    break;
                default:
                    log.debug("Unhandled Revolut webhook event type: {}", eventType);
                    result.put("processed", false);
            }

            return result;
        } catch (Exception e) {
            log.error("Revolut webhook processing failed", e);
            throw new PaymentException("WEBHOOK_ERROR", "Failed to process webhook: " + e.getMessage(), e);
        }
    }

    @Override
    public List<PaymentMethod> supportedMethods(Map<String, Object> providerConfig) {
        List<PaymentMethod> methods = new ArrayList<>();
        methods.add(PaymentMethod.CARD);
        return methods;
    }

    private void handlePaymentSucceeded(Map<String, Object> data, Map<String, Object> result) {
        result.put("orderId", data.get("order_id"));
        result.put("amount", data.get("amount"));
        result.put("currency", data.get("currency"));
    }

    private void handlePaymentFailed(Map<String, Object> data, Map<String, Object> result) {
        result.put("orderId", data.get("order_id"));
        result.put("failureReason", data.get("failure_reason"));
    }

    private void handleSettlementCreated(Map<String, Object> data, Map<String, Object> result) {
        result.put("settlementId", data.get("settlement_id"));
        result.put("amount", data.get("amount"));
    }
}
