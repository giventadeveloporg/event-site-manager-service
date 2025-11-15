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
 * Zelle manual payment adapter implementing PaymentService interface.
 * Zelle payments are manual transfers that require admin confirmation.
 * This adapter creates pending transactions and provides instructions for manual payment.
 */
@Service
public class ZelleManualAdapter implements PaymentService {

    private static final Logger log = LoggerFactory.getLogger(ZelleManualAdapter.class);

    @Override
    public String getProviderName() {
        return PaymentProvider.ZELLE_MANUAL.name();
    }

    @Override
    public PaymentSessionResponse initialize(PaymentSessionRequest request, Map<String, Object> providerConfig) throws PaymentException {
        log.info("Initializing Zelle manual payment for tenant: {}, amount: {}", request.getTenantId(), request.getAmount());

        // Get Zelle account details from config
        String zelleEmail = (String) providerConfig.get("zelleEmail");
        String zelleName = (String) providerConfig.get("zelleName");
        String memo = request.getDescription() != null ? request.getDescription() : "Payment";

        if (zelleEmail == null || zelleEmail.isEmpty()) {
            throw new PaymentException("ZELLE_CONFIG_ERROR", "Zelle email not configured");
        }

        PaymentSessionResponse response = new PaymentSessionResponse();
        response.setProvider(PaymentProvider.ZELLE_MANUAL);
        response.setAmount(request.getAmount());
        response.setCurrency(request.getCurrency());
        response.setStatus("PENDING");

        // Generate transaction ID
        String transactionId = UUID.randomUUID().toString();
        response.setTransactionId(transactionId);

        // Create payment instructions
        Map<String, Object> instructions = new HashMap<>();
        instructions.put("zelleEmail", zelleEmail);
        instructions.put("zelleName", zelleName);
        instructions.put("amount", request.getAmount());
        instructions.put("currency", request.getCurrency());
        instructions.put("memo", memo);
        instructions.put("customerEmail", request.getCustomerEmail());
        instructions.put("instructions", "Send payment via Zelle to " + zelleEmail + " with memo: " + memo);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("externalTransactionId", transactionId);
        metadata.put("instructions", instructions);
        metadata.put("paymentType", "MANUAL_ZELLE");
        response.setProviderMetadata(metadata);
        response.setActionData(instructions);

        List<PaymentMethod> supportedMethods = new ArrayList<>();
        supportedMethods.add(PaymentMethod.MANUAL);
        response.setSupportedMethods(supportedMethods);

        return response;
    }

    @Override
    public PaymentSessionResponse confirm(String transactionId, Map<String, Object> confirmationData, Map<String, Object> providerConfig)
        throws PaymentException {
        log.info("Confirming Zelle manual payment: {}", transactionId);

        // Admin confirmation would update the transaction status
        PaymentSessionResponse response = new PaymentSessionResponse();
        response.setProvider(PaymentProvider.ZELLE_MANUAL);
        response.setTransactionId(transactionId);
        response.setStatus("COMPLETED");

        if (confirmationData != null) {
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("confirmedBy", confirmationData.get("adminId"));
            metadata.put("confirmedAt", ZonedDateTime.now().toString());
            metadata.put("confirmationReference", confirmationData.get("reference"));
            response.setProviderMetadata(metadata);
        }

        return response;
    }

    @Override
    public void cancel(String transactionId, Map<String, Object> providerConfig) throws PaymentException {
        log.info("Cancelling Zelle manual payment: {}", transactionId);
        // Manual payments can be cancelled before confirmation
    }

    @Override
    public RefundResponse refund(RefundRequest request, Map<String, Object> providerConfig) throws PaymentException {
        log.info("Processing Zelle manual refund for transaction: {}", request.getTransactionId());
        // Zelle refunds are manual and require admin processing
        throw new PaymentException("ZELLE_REFUND_MANUAL", "Zelle refunds must be processed manually by admin");
    }

    @Override
    public PaymentSessionResponse getStatus(String transactionId, Map<String, Object> providerConfig) throws PaymentException {
        log.debug("Getting Zelle manual payment status: {}", transactionId);

        PaymentSessionResponse response = new PaymentSessionResponse();
        response.setProvider(PaymentProvider.ZELLE_MANUAL);
        response.setTransactionId(transactionId);
        response.setStatus("PENDING"); // Status would be updated by admin confirmation

        return response;
    }

    @Override
    public Map<String, Object> handleWebhook(String payload, String signature, Map<String, Object> providerConfig) throws PaymentException {
        log.info("Handling Zelle webhook");
        // Zelle manual payments don't have webhooks
        throw new PaymentException("WEBHOOK_NOT_SUPPORTED", "Zelle manual payments do not support webhooks");
    }

    @Override
    public List<PaymentMethod> supportedMethods(Map<String, Object> providerConfig) {
        List<PaymentMethod> methods = new ArrayList<>();
        methods.add(PaymentMethod.MANUAL);
        return methods;
    }
}
