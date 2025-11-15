package com.nextjstemplate.service.payment.orchestration;

import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.domain.UserPaymentTransaction;
import com.nextjstemplate.domain.enumeration.PaymentProvider;
import com.nextjstemplate.domain.enumeration.PaymentUseCase;
import com.nextjstemplate.repository.EventDetailsRepository;
import com.nextjstemplate.repository.UserPaymentTransactionRepository;
import com.nextjstemplate.service.payment.PaymentException;
import com.nextjstemplate.service.payment.PaymentService;
import com.nextjstemplate.service.payment.config.PaymentProviderConfigService;
import com.nextjstemplate.service.payment.dto.PaymentSessionRequest;
import com.nextjstemplate.service.payment.dto.PaymentSessionResponse;
import com.nextjstemplate.service.payment.dto.RefundRequest;
import com.nextjstemplate.service.payment.dto.RefundResponse;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Payment orchestration service that routes payment requests to appropriate provider adapters.
 * This is the entry point for all payment operations.
 */
@Service
@Transactional
public class PaymentOrchestrationService {

    private static final Logger log = LoggerFactory.getLogger(PaymentOrchestrationService.class);

    private final PaymentProviderConfigService configService;
    private final UserPaymentTransactionRepository transactionRepository;
    private final EventDetailsRepository eventDetailsRepository;
    private final Map<PaymentProvider, PaymentService> paymentServices;

    public PaymentOrchestrationService(
        PaymentProviderConfigService configService,
        UserPaymentTransactionRepository transactionRepository,
        EventDetailsRepository eventDetailsRepository,
        List<PaymentService> paymentServicesList
    ) {
        this.configService = configService;
        this.transactionRepository = transactionRepository;
        this.eventDetailsRepository = eventDetailsRepository;
        this.paymentServices = new java.util.HashMap<>();
        // Register all payment services (may be empty initially until adapters are implemented)
        if (paymentServicesList != null) {
            for (PaymentService service : paymentServicesList) {
                try {
                    PaymentProvider provider = PaymentProvider.valueOf(service.getProviderName().toUpperCase());
                    this.paymentServices.put(provider, service);
                    log.info("Registered payment service for provider: {}", provider);
                } catch (IllegalArgumentException e) {
                    log.warn("Unknown provider name: {}", service.getProviderName());
                }
            }
        }
    }

    /**
     * Initialize a payment session.
     * Selects the appropriate provider based on tenant configuration and fallback ordering.
     *
     * @param request Payment session request
     * @return Payment session response
     * @throws PaymentException if initialization fails
     */
    public PaymentSessionResponse initialize(PaymentSessionRequest request) throws PaymentException {
        log.info("Initializing payment session for tenant: {}, use case: {}", request.getTenantId(), request.getPaymentUseCase());

        // Get active provider configurations ordered by fallback priority
        List<Map<String, Object>> providerConfigs = configService.getActiveProviderConfigs(
            request.getTenantId(),
            request.getPaymentUseCase()
        );

        if (providerConfigs.isEmpty()) {
            throw new PaymentException(
                "NO_PROVIDER_CONFIGURED",
                "No active payment provider configured for tenant: " + request.getTenantId()
            );
        }

        // Try each provider in fallback order
        PaymentException lastException = null;
        for (Map<String, Object> providerConfig : providerConfigs) {
            PaymentProvider provider = (PaymentProvider) providerConfig.get("providerName");
            PaymentService paymentService = paymentServices.get(provider);

            if (paymentService == null) {
                log.warn("Payment service not found for provider: {}", provider);
                continue;
            }

            try {
                log.debug("Attempting payment initialization with provider: {}", provider);
                PaymentSessionResponse response = paymentService.initialize(request, providerConfig);

                // Create transaction record
                UserPaymentTransaction transaction = createTransaction(request, provider, response);
                transactionRepository.save(transaction);

                response.setTransactionId(transaction.getId().toString());
                log.info("Payment session initialized successfully with provider: {}, transactionId: {}", provider, transaction.getId());
                return response;
            } catch (PaymentException e) {
                log.warn("Provider {} failed: {}", provider, e.getMessage());
                lastException = e;
                // Continue to next provider
            }
        }

        // All providers failed
        throw new PaymentException(
            "ALL_PROVIDERS_FAILED",
            "All payment providers failed to initialize payment session",
            lastException != null ? Map.of("lastError", lastException.getMessage()) : null
        );
    }

    /**
     * Confirm a payment.
     *
     * @param transactionId Transaction ID
     * @param confirmationData Provider-specific confirmation data
     * @return Updated payment session response
     * @throws PaymentException if confirmation fails
     */
    public PaymentSessionResponse confirm(String transactionId, Map<String, Object> confirmationData) throws PaymentException {
        log.info("Confirming payment transaction: {}", transactionId);

        UserPaymentTransaction transaction = transactionRepository
            .findById(Long.parseLong(transactionId))
            .orElseThrow(() -> new PaymentException("TRANSACTION_NOT_FOUND", "Transaction not found: " + transactionId));

        PaymentProvider provider = extractProviderFromTransaction(transaction);
        Map<String, Object> providerConfig = configService
            .getProviderConfig(transaction.getTenantId(), provider)
            .orElseThrow(() -> new PaymentException("PROVIDER_CONFIG_NOT_FOUND", "Provider config not found"));

        PaymentService paymentService = paymentServices.get(provider);
        if (paymentService == null) {
            throw new PaymentException("PROVIDER_NOT_AVAILABLE", "Payment service not available for provider: " + provider);
        }

        PaymentSessionResponse response = paymentService.confirm(transactionId, confirmationData, providerConfig);

        // Update transaction
        updateTransactionFromResponse(transaction, response);
        transactionRepository.save(transaction);

        return response;
    }

    /**
     * Cancel a payment.
     *
     * @param transactionId Transaction ID
     * @throws PaymentException if cancellation fails
     */
    public void cancel(String transactionId) throws PaymentException {
        log.info("Cancelling payment transaction: {}", transactionId);

        UserPaymentTransaction transaction = transactionRepository
            .findById(Long.parseLong(transactionId))
            .orElseThrow(() -> new PaymentException("TRANSACTION_NOT_FOUND", "Transaction not found: " + transactionId));

        PaymentProvider provider = extractProviderFromTransaction(transaction);
        Map<String, Object> providerConfig = configService
            .getProviderConfig(transaction.getTenantId(), provider)
            .orElseThrow(() -> new PaymentException("PROVIDER_CONFIG_NOT_FOUND", "Provider config not found"));

        PaymentService paymentService = paymentServices.get(provider);
        if (paymentService == null) {
            throw new PaymentException("PROVIDER_NOT_AVAILABLE", "Payment service not available for provider: " + provider);
        }

        paymentService.cancel(transactionId, providerConfig);

        // Update transaction status
        transaction.setStatus("CANCELLED");
        transaction.setUpdatedAt(ZonedDateTime.now());
        transactionRepository.save(transaction);
    }

    /**
     * Process a refund.
     *
     * @param request Refund request
     * @return Refund response
     * @throws PaymentException if refund fails
     */
    public RefundResponse refund(RefundRequest request) throws PaymentException {
        log.info("Processing refund for transaction: {}", request.getTransactionId());

        UserPaymentTransaction transaction = transactionRepository
            .findById(Long.parseLong(request.getTransactionId()))
            .orElseThrow(() -> new PaymentException("TRANSACTION_NOT_FOUND", "Transaction not found"));

        PaymentProvider provider = extractProviderFromTransaction(transaction);
        Map<String, Object> providerConfig = configService
            .getProviderConfig(transaction.getTenantId(), provider)
            .orElseThrow(() -> new PaymentException("PROVIDER_CONFIG_NOT_FOUND", "Provider config not found"));

        PaymentService paymentService = paymentServices.get(provider);
        if (paymentService == null) {
            throw new PaymentException("PROVIDER_NOT_AVAILABLE", "Payment service not available for provider: " + provider);
        }

        RefundResponse response = paymentService.refund(request, providerConfig);

        // Update transaction
        transaction.setStatus("REFUNDED");
        if (transaction.getMetadata() != null) {
            // Append refund info to metadata
            String metadata = transaction.getMetadata() + "\nRefund: " + response.getRefundId() + " - " + response.getAmount();
            transaction.setMetadata(metadata);
        }
        transaction.setUpdatedAt(ZonedDateTime.now());
        transactionRepository.save(transaction);

        return response;
    }

    /**
     * Get payment status.
     *
     * @param transactionId Transaction ID
     * @return Payment session response with current status
     * @throws PaymentException if status retrieval fails
     */
    @Transactional(readOnly = true)
    public PaymentSessionResponse getStatus(String transactionId) throws PaymentException {
        log.debug("Getting payment status for transaction: {}", transactionId);

        UserPaymentTransaction transaction = transactionRepository
            .findById(Long.parseLong(transactionId))
            .orElseThrow(() -> new PaymentException("TRANSACTION_NOT_FOUND", "Transaction not found: " + transactionId));

        PaymentProvider provider = extractProviderFromTransaction(transaction);
        Map<String, Object> providerConfig = configService
            .getProviderConfig(transaction.getTenantId(), provider)
            .orElseThrow(() -> new PaymentException("PROVIDER_CONFIG_NOT_FOUND", "Provider config not found"));

        PaymentService paymentService = paymentServices.get(provider);
        if (paymentService == null) {
            throw new PaymentException("PROVIDER_NOT_AVAILABLE", "Payment service not available for provider: " + provider);
        }

        return paymentService.getStatus(transactionId, providerConfig);
    }

    /**
     * Handle webhook event.
     *
     * @param provider Provider name
     * @param payload Webhook payload
     * @param signature Webhook signature
     * @param tenantId Tenant ID
     * @return Processed event data
     * @throws PaymentException if webhook processing fails
     */
    public Map<String, Object> handleWebhook(PaymentProvider provider, String payload, String signature, String tenantId)
        throws PaymentException {
        log.info("Handling webhook for provider: {}, tenant: {}", provider, tenantId);

        Map<String, Object> providerConfig = configService
            .getProviderConfig(tenantId, provider)
            .orElseThrow(() -> new PaymentException("PROVIDER_CONFIG_NOT_FOUND", "Provider config not found"));

        PaymentService paymentService = paymentServices.get(provider);
        if (paymentService == null) {
            throw new PaymentException("PROVIDER_NOT_AVAILABLE", "Payment service not available for provider: " + provider);
        }

        return paymentService.handleWebhook(payload, signature, providerConfig);
    }

    /**
     * Create transaction record from payment request and response.
     */
    private UserPaymentTransaction createTransaction(
        PaymentSessionRequest request,
        PaymentProvider provider,
        PaymentSessionResponse response
    ) {
        UserPaymentTransaction transaction = new UserPaymentTransaction();
        transaction.setTenantId(request.getTenantId());
        transaction.setTransactionType(request.getPaymentUseCase().name());
        transaction.setAmount(request.getAmount());
        transaction.setCurrency(request.getCurrency());
        transaction.setStatus(response.getStatus() != null ? response.getStatus() : "PENDING");
        transaction.setCreatedAt(ZonedDateTime.now());
        transaction.setUpdatedAt(ZonedDateTime.now());

        // CRITICAL: Set event if eventId is provided in the request
        // This is essential for ticket generation to work correctly
        if (request.getEventId() != null) {
            eventDetailsRepository
                .findById(request.getEventId())
                .ifPresentOrElse(
                    event -> {
                        transaction.setEvent(event);
                        log.info("Set event {} on payment transaction for tenant {}", request.getEventId(), request.getTenantId());
                    },
                    () ->
                        log.warn(
                            "Event {} not found in database, transaction will be created without event association",
                            request.getEventId()
                        )
                );
        } else {
            log.debug("No eventId provided in payment request, transaction will be created without event association");
        }

        // CRITICAL: Store customer email, name, and other customer info in transaction metadata
        // This ensures the email is available during ticket generation
        Map<String, Object> transactionMetadata = new HashMap<>();

        // Add customer email if provided
        if (request.getCustomerEmail() != null && !request.getCustomerEmail().isEmpty()) {
            transactionMetadata.put("email", request.getCustomerEmail());
            transactionMetadata.put("customerEmail", request.getCustomerEmail());
            log.info("Stored customer email {} in payment transaction metadata during initialization", request.getCustomerEmail());
        } else {
            log.warn("Payment initialization request missing customerEmail - email may not be available for ticket generation");
        }

        // Add customer name if provided
        if (request.getCustomerName() != null && !request.getCustomerName().isEmpty()) {
            transactionMetadata.put("customerName", request.getCustomerName());
        }

        // Merge with provider-specific metadata from response
        if (response.getProviderMetadata() != null) {
            transactionMetadata.putAll(response.getProviderMetadata());
        }

        // Convert metadata map to JSON string
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            transaction.setMetadata(mapper.writeValueAsString(transactionMetadata));
        } catch (Exception e) {
            log.warn("Failed to serialize transaction metadata", e);
        }

        // Store provider transaction ID if available
        if (response.getProviderMetadata() != null) {
            Object externalId = response.getProviderMetadata().get("externalTransactionId");
            if (externalId != null) {
                transaction.setExternalTransactionId(externalId.toString());
            }

            // Store Stripe payment intent ID if available (for Stripe payments)
            Object stripePaymentIntentId = response.getProviderMetadata().get("stripePaymentIntentId");
            if (stripePaymentIntentId != null && provider == PaymentProvider.STRIPE) {
                transaction.setStripePaymentIntentId(stripePaymentIntentId.toString());
            }
        }

        return transaction;
    }

    /**
     * Update transaction from payment response.
     */
    private void updateTransactionFromResponse(UserPaymentTransaction transaction, PaymentSessionResponse response) {
        if (response.getStatus() != null) {
            transaction.setStatus(response.getStatus());
        }
        transaction.setUpdatedAt(ZonedDateTime.now());

        if (response.getProviderMetadata() != null) {
            try {
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                transaction.setMetadata(mapper.writeValueAsString(response.getProviderMetadata()));
            } catch (Exception e) {
                log.warn("Failed to serialize provider metadata", e);
            }
        }
    }

    /**
     * Extract provider from transaction metadata or use default.
     */
    private PaymentProvider extractProviderFromTransaction(UserPaymentTransaction transaction) {
        // Try to extract from metadata, fallback to STRIPE for legacy transactions
        if (transaction.getMetadata() != null) {
            try {
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                Map<String, Object> metadata = mapper.readValue(transaction.getMetadata(), Map.class);
                Object provider = metadata.get("provider");
                if (provider != null) {
                    return PaymentProvider.valueOf(provider.toString().toUpperCase());
                }
            } catch (Exception e) {
                log.debug("Could not extract provider from metadata", e);
            }
        }

        // Default to STRIPE for legacy transactions
        return PaymentProvider.STRIPE;
    }
}
