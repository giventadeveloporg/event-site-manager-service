package com.nextjstemplate.service.payment.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextjstemplate.domain.EventTicketTransaction;
import com.nextjstemplate.domain.UserPaymentTransaction;
import com.nextjstemplate.domain.enumeration.PaymentMethod;
import com.nextjstemplate.domain.enumeration.PaymentProvider;
import com.nextjstemplate.repository.EventTicketTransactionRepository;
import com.nextjstemplate.repository.UserPaymentTransactionRepository;
import com.nextjstemplate.service.payment.PaymentException;
import com.nextjstemplate.service.payment.PaymentService;
import com.nextjstemplate.service.payment.TicketGenerationService;
import com.nextjstemplate.service.payment.dto.PaymentItem;
import com.nextjstemplate.service.payment.dto.PaymentSessionRequest;
import com.nextjstemplate.service.payment.dto.PaymentSessionResponse;
import com.nextjstemplate.service.payment.dto.RefundRequest;
import com.nextjstemplate.service.payment.dto.RefundResponse;
import com.nextjstemplate.service.payment.event.PaymentSuccessEvent;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.net.Webhook;
import com.stripe.param.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * Stripe payment adapter implementing PaymentService interface.
 * Supports PaymentIntent, Checkout Sessions, Link/Wallet, and Instant Checkout (ACP).
 */
@Service
public class StripePaymentAdapter implements PaymentService {

    private static final Logger log = LoggerFactory.getLogger(StripePaymentAdapter.class);

    private final UserPaymentTransactionRepository transactionRepository;
    private final EventTicketTransactionRepository eventTicketTransactionRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final TicketGenerationService ticketGenerationService;
    private final ObjectMapper objectMapper;
    private final com.nextjstemplate.service.payment.config.PaymentProviderConfigService configService;

    public StripePaymentAdapter(
        UserPaymentTransactionRepository transactionRepository,
        EventTicketTransactionRepository eventTicketTransactionRepository,
        ApplicationEventPublisher eventPublisher,
        TicketGenerationService ticketGenerationService,
        com.nextjstemplate.service.payment.config.PaymentProviderConfigService configService
    ) {
        this.transactionRepository = transactionRepository;
        this.eventTicketTransactionRepository = eventTicketTransactionRepository;
        this.eventPublisher = eventPublisher;
        this.ticketGenerationService = ticketGenerationService;
        this.configService = configService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String getProviderName() {
        return PaymentProvider.STRIPE.name();
    }

    @Override
    public PaymentSessionResponse initialize(PaymentSessionRequest request, Map<String, Object> providerConfig) throws PaymentException {
        log.info("Initializing Stripe payment for tenant: {}, amount: {}", request.getTenantId(), request.getAmount());

        try {
            // Set Stripe API key from config
            String apiKey = (String) providerConfig.get("secretKey");
            if (apiKey == null || apiKey.isEmpty()) {
                throw new PaymentException("STRIPE_CONFIG_ERROR", "Stripe secret key not configured");
            }
            Stripe.apiKey = apiKey;

            Boolean supportsAcp = (Boolean) providerConfig.getOrDefault("supportsAcp", false);
            String publishableKey = (String) providerConfig.get("publishableKey");
            if (publishableKey == null || publishableKey.isEmpty()) {
                log.warn("Stripe publishable key not configured for tenant: {}", request.getTenantId());
                throw new PaymentException("STRIPE_CONFIG_ERROR", "Stripe publishable key not configured");
            }

            PaymentSessionResponse response = new PaymentSessionResponse();
            response.setProvider(PaymentProvider.STRIPE);
            response.setAmount(request.getAmount());
            response.setCurrency(request.getCurrency());
            response.setPublishableKey(publishableKey);

            // Create PaymentIntent (Checkout Sessions require additional SDK dependencies)
            PaymentIntentCreateParams.Builder intentParamsBuilder = PaymentIntentCreateParams
                .builder()
                .setAmount(convertToStripeAmount(request.getAmount(), request.getCurrency()))
                .setCurrency(request.getCurrency().toLowerCase())
                .setAutomaticPaymentMethods(PaymentIntentCreateParams.AutomaticPaymentMethods.builder().setEnabled(true).build());

            if (request.getDescription() != null) {
                intentParamsBuilder.setDescription(request.getDescription());
            }

            // Add metadata
            Map<String, String> metadata = new HashMap<>();
            metadata.put("tenantId", request.getTenantId());
            metadata.put("paymentUseCase", request.getPaymentUseCase().name());
            if (request.getEventId() != null) {
                metadata.put("eventId", request.getEventId().toString());
            }
            // CRITICAL: Store customer email in Stripe metadata for webhook processing
            if (request.getCustomerEmail() != null && !request.getCustomerEmail().isEmpty()) {
                metadata.put("customerEmail", request.getCustomerEmail());
            }

            // Store customer name and phone in metadata if provided
            if (request.getCustomerName() != null && !request.getCustomerName().isEmpty()) {
                metadata.put("customerName", request.getCustomerName());
            }
            if (request.getCustomerPhone() != null && !request.getCustomerPhone().isEmpty()) {
                metadata.put("customerPhone", request.getCustomerPhone());
            }

            // Add discount code ID if provided
            if (request.getDiscountCode() != null && !request.getDiscountCode().isEmpty()) {
                metadata.put("discountCodeId", request.getDiscountCode());
            }

            // CRITICAL: Extract items from request and convert to cart metadata format
            // This is required for TicketGenerationService to create transaction items
            if (request.getItems() != null && !request.getItems().isEmpty()) {
                try {
                    List<Map<String, Object>> cartMetadata = new ArrayList<>();
                    for (PaymentItem item : request.getItems()) {
                        // Only include TICKET items in cart metadata
                        if ("TICKET".equals(item.getItemType()) && item.getItemId() != null && item.getQuantity() != null) {
                            Map<String, Object> cartItem = new HashMap<>();
                            cartItem.put("ticketTypeId", item.getItemId());
                            cartItem.put("quantity", item.getQuantity());
                            cartMetadata.add(cartItem);
                        }
                    }

                    // Add cart to metadata as JSON string
                    if (!cartMetadata.isEmpty()) {
                        String cartJson = objectMapper.writeValueAsString(cartMetadata);
                        metadata.put("cart", cartJson);
                        log.info("Added cart metadata to Payment Intent: {} items", cartMetadata.size());
                    }
                } catch (JsonProcessingException e) {
                    log.error("Failed to serialize cart metadata", e);
                    // Continue without cart metadata - transaction items won't be created
                }
            }

            intentParamsBuilder.putAllMetadata(metadata);

            // CRITICAL: Set receipt email in Stripe PaymentIntent
            // This ensures Stripe can send receipts and we can extract email from PaymentIntent later
            if (request.getCustomerEmail() != null && !request.getCustomerEmail().isEmpty()) {
                intentParamsBuilder.setReceiptEmail(request.getCustomerEmail());
            }

            PaymentIntent paymentIntent = PaymentIntent.create(intentParamsBuilder.build());
            response.setClientSecret(paymentIntent.getClientSecret());
            response.setStatus(mapStripeStatus(paymentIntent.getStatus()));
            response.setTransactionId(paymentIntent.getId());

            Map<String, Object> providerMetadata = new HashMap<>();
            providerMetadata.put("stripePaymentIntentId", paymentIntent.getId());
            providerMetadata.put("externalTransactionId", paymentIntent.getId());
            response.setProviderMetadata(providerMetadata);

            // Check if action is required
            if (paymentIntent.getNextAction() != null) {
                response.setRequiresAction(true);
                response.setActionType(paymentIntent.getNextAction().getType());
            }

            // If return URL is provided, we can use Stripe's hosted checkout
            if (request.getReturnUrl() != null) {
                // Note: For full Checkout Session support, additional Stripe SDK dependencies are needed
                // For now, we use PaymentIntent with client-side confirmation
                log.debug("Return URL provided, but using PaymentIntent flow. For full Checkout Session support, upgrade Stripe SDK.");
            }

            // Set supported methods
            List<PaymentMethod> supportedMethods = new ArrayList<>();
            supportedMethods.add(PaymentMethod.CARD);
            supportedMethods.add(PaymentMethod.LINK);
            if (supportsAcp) {
                supportedMethods.add(PaymentMethod.WALLET);
                supportedMethods.add(PaymentMethod.APPLE_PAY);
                supportedMethods.add(PaymentMethod.GOOGLE_PAY);
            }
            response.setSupportedMethods(supportedMethods);

            return response;
        } catch (StripeException e) {
            log.error("Stripe payment initialization failed", e);
            throw new PaymentException("STRIPE_ERROR", "Failed to initialize Stripe payment: " + e.getMessage(), e);
        }
    }

    @Override
    public PaymentSessionResponse confirm(String transactionId, Map<String, Object> confirmationData, Map<String, Object> providerConfig)
        throws PaymentException {
        log.info("Confirming Stripe payment: {}", transactionId);

        try {
            String apiKey = (String) providerConfig.get("secretKey");
            if (apiKey == null || apiKey.isEmpty()) {
                throw new PaymentException("STRIPE_CONFIG_ERROR", "Stripe secret key not configured");
            }
            Stripe.apiKey = apiKey;

            PaymentIntent paymentIntent = PaymentIntent.retrieve(transactionId);

            // If payment method ID is provided, confirm the payment intent
            if (confirmationData != null && confirmationData.containsKey("paymentMethodId")) {
                String paymentMethodId = (String) confirmationData.get("paymentMethodId");
                PaymentIntentConfirmParams confirmParams = PaymentIntentConfirmParams.builder().setPaymentMethod(paymentMethodId).build();
                paymentIntent = paymentIntent.confirm(confirmParams);
            }

            String publishableKey = (String) providerConfig.get("publishableKey");
            if (publishableKey == null || publishableKey.isEmpty()) {
                log.warn("Stripe publishable key not configured for transaction: {}", transactionId);
                throw new PaymentException("STRIPE_CONFIG_ERROR", "Stripe publishable key not configured");
            }

            PaymentSessionResponse response = new PaymentSessionResponse();
            response.setProvider(PaymentProvider.STRIPE);
            response.setTransactionId(paymentIntent.getId());
            response.setStatus(mapStripeStatus(paymentIntent.getStatus()));
            response.setAmount(BigDecimal.valueOf(paymentIntent.getAmount()).divide(BigDecimal.valueOf(100)));
            response.setCurrency(paymentIntent.getCurrency().toUpperCase());
            response.setPublishableKey(publishableKey);

            if (paymentIntent.getNextAction() != null) {
                response.setRequiresAction(true);
                response.setActionType(paymentIntent.getNextAction().getType());
            }

            if (paymentIntent.getLastPaymentError() != null) {
                response.setFailureReason(paymentIntent.getLastPaymentError().getMessage());
            }

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("stripePaymentIntentId", paymentIntent.getId());
            metadata.put("externalTransactionId", paymentIntent.getId());
            response.setProviderMetadata(metadata);

            return response;
        } catch (StripeException e) {
            log.error("Stripe payment confirmation failed", e);
            throw new PaymentException("STRIPE_ERROR", "Failed to confirm Stripe payment: " + e.getMessage(), e);
        }
    }

    @Override
    public void cancel(String transactionId, Map<String, Object> providerConfig) throws PaymentException {
        log.info("Cancelling Stripe payment: {}", transactionId);

        try {
            String apiKey = (String) providerConfig.get("secretKey");
            if (apiKey == null || apiKey.isEmpty()) {
                throw new PaymentException("STRIPE_CONFIG_ERROR", "Stripe secret key not configured");
            }
            Stripe.apiKey = apiKey;

            PaymentIntent paymentIntent = PaymentIntent.retrieve(transactionId);
            PaymentIntentCancelParams cancelParams = PaymentIntentCancelParams.builder().build();
            paymentIntent.cancel(cancelParams);
        } catch (StripeException e) {
            log.error("Stripe payment cancellation failed", e);
            throw new PaymentException("STRIPE_ERROR", "Failed to cancel Stripe payment: " + e.getMessage(), e);
        }
    }

    @Override
    public RefundResponse refund(RefundRequest request, Map<String, Object> providerConfig) throws PaymentException {
        log.info("Processing Stripe refund for transaction: {}", request.getTransactionId());

        try {
            String apiKey = (String) providerConfig.get("secretKey");
            if (apiKey == null || apiKey.isEmpty()) {
                throw new PaymentException("STRIPE_CONFIG_ERROR", "Stripe secret key not configured");
            }
            Stripe.apiKey = apiKey;

            // Retrieve payment intent to get charge ID
            PaymentIntent paymentIntent = PaymentIntent.retrieve(request.getTransactionId());
            String chargeId = paymentIntent.getLatestCharge();

            if (chargeId == null) {
                throw new PaymentException("NO_CHARGE", "No charge found for payment intent");
            }

            RefundCreateParams.Builder refundParamsBuilder = RefundCreateParams.builder().setCharge(chargeId);

            if (request.getAmount() != null) {
                // Partial refund - use currency from payment intent if not provided in request
                String currency = request.getCurrency() != null ? request.getCurrency() : paymentIntent.getCurrency();
                refundParamsBuilder.setAmount(convertToStripeAmount(request.getAmount(), currency));
            }

            if (request.getReason() != null) {
                try {
                    refundParamsBuilder.setReason(RefundCreateParams.Reason.valueOf(request.getReason().toUpperCase()));
                } catch (IllegalArgumentException e) {
                    log.warn("Invalid refund reason: {}, using default", request.getReason());
                    // Use default reason if invalid
                }
            }

            Refund refund = Refund.create(refundParamsBuilder.build());

            RefundResponse response = new RefundResponse();
            response.setRefundId(refund.getId());
            response.setTransactionId(request.getTransactionId());
            response.setAmount(BigDecimal.valueOf(refund.getAmount()).divide(BigDecimal.valueOf(100)));
            response.setCurrency(paymentIntent.getCurrency().toUpperCase());
            response.setStatus(refund.getStatus());
            response.setReason(request.getReason());
            response.setProcessedAt(ZonedDateTime.now());

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("stripeRefundId", refund.getId());
            response.setProviderMetadata(metadata);

            return response;
        } catch (StripeException e) {
            log.error("Stripe refund failed", e);
            throw new PaymentException("STRIPE_ERROR", "Failed to process Stripe refund: " + e.getMessage(), e);
        }
    }

    @Override
    public PaymentSessionResponse getStatus(String transactionId, Map<String, Object> providerConfig) throws PaymentException {
        log.debug("Getting Stripe payment status for internal transaction ID: {}", transactionId);

        try {
            // Step 1: Look up the internal payment transaction
            Long internalTransactionId;
            try {
                internalTransactionId = Long.parseLong(transactionId);
            } catch (NumberFormatException e) {
                throw new PaymentException("INVALID_TRANSACTION_ID", "Invalid transaction ID format: " + transactionId);
            }

            UserPaymentTransaction transaction = transactionRepository
                .findById(internalTransactionId)
                .orElseThrow(() -> {
                    log.warn("Payment transaction not found: {}", transactionId);
                    return new PaymentException("TRANSACTION_NOT_FOUND", "Payment transaction not found: " + transactionId);
                });

            log.debug("Found transaction {} for tenant: {}", transactionId, transaction.getTenantId());

            // Step 2: Extract the Stripe payment intent ID
            String stripePaymentIntentId = transaction.getStripePaymentIntentId();
            if (stripePaymentIntentId == null || stripePaymentIntentId.isEmpty()) {
                log.info(
                    "Transaction {} does not have a Stripe payment intent ID. Returning database status: {}",
                    transactionId,
                    transaction.getStatus()
                );

                // CRITICAL: Return transaction status from database directly, don't throw error
                // This allows frontend polling to see status changes (PENDING -> SUCCEEDED)
                return buildResponseFromTransaction(transaction, transactionId, providerConfig);
            }

            log.debug("Using Stripe payment intent ID: {} for transaction: {}", stripePaymentIntentId, transactionId);

            // Step 3: Set up Stripe API key
            String apiKey = (String) providerConfig.get("secretKey");
            if (apiKey == null || apiKey.isEmpty()) {
                throw new PaymentException("STRIPE_CONFIG_ERROR", "Stripe secret key not configured");
            }
            Stripe.apiKey = apiKey;

            // Step 4: Use the Stripe payment intent ID to query Stripe
            PaymentIntent paymentIntent = PaymentIntent.retrieve(stripePaymentIntentId);

            log.debug("Retrieved Stripe payment intent {} with status: {}", stripePaymentIntentId, paymentIntent.getStatus());

            // Step 5: Map Stripe response to PaymentSessionResponse
            PaymentSessionResponse response = new PaymentSessionResponse();
            response.setProvider(PaymentProvider.STRIPE);
            response.setTransactionId(transactionId); // Use internal transaction ID

            // CRITICAL: Map Stripe status (lowercase) to uppercase enum for frontend
            String stripeStatus = paymentIntent.getStatus(); // e.g., "succeeded" (lowercase)
            String mappedStatus = mapStripeStatus(stripeStatus); // e.g., "SUCCEEDED" (uppercase)
            response.setStatus(mappedStatus);

            // CRITICAL: Update database transaction status if it changed
            // This ensures future calls return the correct status even if Stripe API is unavailable
            String oldStatus = transaction.getStatus();
            boolean statusChanged = !mappedStatus.equals(oldStatus);
            if (statusChanged) {
                transaction.setStatus(mappedStatus);
                transaction.setUpdatedAt(ZonedDateTime.now());

                // CRITICAL: Store customer email from PaymentIntent if payment succeeded
                // This ensures ticket generation has email available
                if ("SUCCEEDED".equals(mappedStatus)) {
                    // Create final copy of customerEmail before using in lambda
                    final String customerEmailFinal;
                    if (paymentIntent.getReceiptEmail() != null && !paymentIntent.getReceiptEmail().isEmpty()) {
                        customerEmailFinal = paymentIntent.getReceiptEmail();
                    } else {
                        customerEmailFinal = null;
                    }

                    if (customerEmailFinal != null && !customerEmailFinal.isEmpty()) {
                        try {
                            Map<String, Object> metadataMap = new HashMap<>();
                            if (transaction.getMetadata() != null && !transaction.getMetadata().isEmpty()) {
                                try {
                                    metadataMap = objectMapper.readValue(transaction.getMetadata(), Map.class);
                                } catch (Exception e) {
                                    log.debug("Failed to parse existing metadata, creating new metadata map");
                                }
                            }
                            metadataMap.put("email", customerEmailFinal);
                            metadataMap.put("customerEmail", customerEmailFinal);
                            transaction.setMetadata(objectMapper.writeValueAsString(metadataMap));
                            log.info(
                                "Stored customer email {} in transaction {} metadata (from polling)",
                                customerEmailFinal,
                                transactionId
                            );
                        } catch (Exception e) {
                            log.warn("Failed to store customer email in transaction metadata: {}", e.getMessage());
                        }
                    }

                    // Create final copies for lambda usage
                    final UserPaymentTransaction transactionFinal = transaction;
                    final String stripePaymentIntentIdFinal = stripePaymentIntentId;

                    transactionRepository.save(transaction);
                    log.info("Updated transaction {} status from {} to {}", transactionId, oldStatus, mappedStatus);

                    // NOTE: Ticket generation should NOT be triggered synchronously from getStatus() because:
                    // 1. getStatus() runs in a read-only transaction (@Transactional(readOnly = true))
                    // 2. Ticket generation requires write operations (saving EventTicketTransaction)
                    // 3. This causes "cannot execute nextval() in a read-only transaction" errors
                    // Instead, publish an event for async processing:
                    // - The async PaymentSuccessEvent listener will handle ticket generation in its own transaction
                    // - Webhook handlers also handle ticket generation synchronously in writable transactions
                    if ("SUCCEEDED".equals(mappedStatus) && !"SUCCEEDED".equals(oldStatus)) {
                        Long eventId = transactionFinal.getEvent() != null ? transactionFinal.getEvent().getId() : null;
                        if (eventId != null) {
                            log.info(
                                "Payment {} just succeeded via polling. Publishing PaymentSuccessEvent for async ticket generation",
                                transactionId
                            );
                            try {
                                eventPublisher.publishEvent(new PaymentSuccessEvent(this, transactionFinal, stripePaymentIntentIdFinal));
                                log.debug("Published PaymentSuccessEvent for transaction {} (async ticket generation)", transactionId);
                            } catch (Exception e) {
                                log.warn(
                                    "Failed to publish PaymentSuccessEvent for transaction {}: {}. Ticket generation will be handled by webhook.",
                                    transactionId,
                                    e.getMessage()
                                );
                            }
                        }
                    }
                } else {
                    transactionRepository.save(transaction);
                    log.info("Updated transaction {} status from {} to {}", transactionId, oldStatus, mappedStatus);
                }
            }

            response.setAmount(BigDecimal.valueOf(paymentIntent.getAmount()).divide(BigDecimal.valueOf(100)));
            response.setCurrency(paymentIntent.getCurrency().toUpperCase());

            // Set publishable key if available
            String publishableKey = (String) providerConfig.get("publishableKey");
            if (publishableKey != null && !publishableKey.isEmpty()) {
                response.setPublishableKey(publishableKey);
            }

            if (paymentIntent.getNextAction() != null) {
                response.setRequiresAction(true);
                response.setActionType(paymentIntent.getNextAction().getType());
            }

            if (paymentIntent.getLastPaymentError() != null) {
                response.setFailureReason(paymentIntent.getLastPaymentError().getMessage());
            }

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("stripePaymentIntentId", paymentIntent.getId());
            metadata.put("externalTransactionId", paymentIntent.getId());
            response.setProviderMetadata(metadata);

            // CRITICAL: Set stripePaymentIntentId as top-level field for frontend ticket polling
            response.setStripePaymentIntentId(stripePaymentIntentId);
            log.debug("Included stripePaymentIntentId in response: {}", stripePaymentIntentId);

            // CRITICAL: If payment succeeded and it's a ticket purchase, include ticket data
            populateTicketData(response, transaction, mappedStatus);

            return response;
        } catch (PaymentException e) {
            // Re-throw PaymentException as-is
            throw e;
        } catch (StripeException e) {
            log.warn(
                "Stripe API error while getting payment status for transaction: {}. Falling back to database status. Error: {}",
                transactionId,
                e.getMessage()
            );

            // If Stripe API fails, fall back to database status
            // This allows frontend polling to continue working even if Stripe API is temporarily unavailable
            try {
                UserPaymentTransaction transaction = transactionRepository
                    .findById(Long.parseLong(transactionId))
                    .orElseThrow(() -> new PaymentException("TRANSACTION_NOT_FOUND", "Payment transaction not found: " + transactionId));

                return buildResponseFromTransaction(transaction, transactionId, providerConfig);
            } catch (PaymentException pe) {
                // Re-throw PaymentException as-is
                throw pe;
            } catch (Exception ex) {
                log.error("Failed to retrieve transaction {} for fallback status", transactionId, ex);
                throw new PaymentException("STRIPE_ERROR", "Failed to get payment status from Stripe: " + e.getMessage(), e);
            }
        } catch (Exception e) {
            log.error("Unexpected error while getting Stripe payment status for transaction: {}", transactionId, e);
            throw new PaymentException("UNEXPECTED_ERROR", "Unexpected error while getting payment status: " + e.getMessage(), e);
        }
    }

    /**
     * Builds a PaymentSessionResponse from the database transaction entity.
     * Used when Stripe payment intent ID is missing or Stripe API fails.
     * This allows frontend polling to see status changes from database.
     *
     * @param transaction the payment transaction entity
     * @param transactionId the transaction ID as string
     * @param providerConfig the provider configuration map
     * @return PaymentSessionResponse with data from database transaction
     */
    private PaymentSessionResponse buildResponseFromTransaction(
        UserPaymentTransaction transaction,
        String transactionId,
        Map<String, Object> providerConfig
    ) {
        PaymentSessionResponse response = new PaymentSessionResponse();
        response.setProvider(PaymentProvider.STRIPE);
        response.setTransactionId(transactionId);

        // Set status from database (PENDING, SUCCEEDED, FAILED, etc.)
        response.setStatus(transaction.getStatus() != null ? transaction.getStatus() : "PENDING");

        // Set amount and currency from transaction
        response.setAmount(transaction.getAmount());
        response.setCurrency(transaction.getCurrency() != null ? transaction.getCurrency().toUpperCase() : "USD");

        // Set publishable key if available (needed for frontend)
        String publishableKey = (String) providerConfig.get("publishableKey");
        if (publishableKey != null && !publishableKey.isEmpty()) {
            response.setPublishableKey(publishableKey);
        }

        // Set failure reason if available
        if (transaction.getFailureReason() != null && !transaction.getFailureReason().isEmpty()) {
            response.setFailureReason(transaction.getFailureReason());
        }

        // Set metadata with transaction details
        Map<String, Object> metadata = new HashMap<>();
        if (transaction.getStripePaymentIntentId() != null) {
            metadata.put("stripePaymentIntentId", transaction.getStripePaymentIntentId());
        }
        if (transaction.getExternalTransactionId() != null) {
            metadata.put("externalTransactionId", transaction.getExternalTransactionId());
        }
        if (transaction.getPaymentMethod() != null) {
            metadata.put("paymentMethod", transaction.getPaymentMethod());
        }
        if (transaction.getTransactionType() != null) {
            metadata.put("transactionType", transaction.getTransactionType());
        }
        if (transaction.getEvent() != null) {
            metadata.put("eventId", transaction.getEvent().getId());
        }
        response.setProviderMetadata(metadata);

        // CRITICAL: Set stripePaymentIntentId as top-level field for frontend ticket polling
        if (transaction.getStripePaymentIntentId() != null) {
            response.setStripePaymentIntentId(transaction.getStripePaymentIntentId());
            log.debug("Included stripePaymentIntentId from database in response: {}", transaction.getStripePaymentIntentId());
        }

        // CRITICAL: If payment succeeded and it's a ticket purchase, include ticket data
        populateTicketData(response, transaction, transaction.getStatus() != null ? transaction.getStatus() : "PENDING");

        // Set requiresAction based on status
        // If status is PENDING, frontend should continue polling
        if ("PENDING".equalsIgnoreCase(transaction.getStatus())) {
            response.setRequiresAction(false); // No action required, just polling
        }

        log.debug(
            "Built response from database transaction {}: status={}, amount={}, currency={}",
            transactionId,
            response.getStatus(),
            response.getAmount(),
            response.getCurrency()
        );

        return response;
    }

    @Override
    public Map<String, Object> handleWebhook(String payload, String signature, Map<String, Object> providerConfig) throws PaymentException {
        log.info("Handling Stripe webhook");

        try {
            String webhookSecret = (String) providerConfig.get("webhookSecret");
            if (webhookSecret == null || webhookSecret.isEmpty()) {
                throw new PaymentException("STRIPE_CONFIG_ERROR", "Stripe webhook secret not configured");
            }

            Event event = Webhook.constructEvent(payload, signature, webhookSecret);
            String eventType = event.getType();

            Map<String, Object> result = new HashMap<>();
            result.put("eventId", event.getId());
            result.put("eventType", eventType);
            result.put("processed", true);

            // Handle different event types
            switch (eventType) {
                case "payment_intent.succeeded":
                    handlePaymentIntentSucceeded(event, result);
                    break;
                case "payment_intent.payment_failed":
                    handlePaymentIntentFailed(event, result);
                    break;
                case "checkout.session.completed":
                    handleCheckoutSessionCompleted(event, result);
                    break;
                case "charge.refunded":
                    handleRefund(event, result);
                    break;
                default:
                    log.debug("Unhandled Stripe webhook event type: {}", eventType);
                    result.put("processed", false);
            }

            return result;
        } catch (com.stripe.exception.SignatureVerificationException e) {
            log.error("Stripe webhook signature verification failed", e);
            throw new PaymentException("WEBHOOK_SIGNATURE_INVALID", "Invalid webhook signature", e);
        } catch (Exception e) {
            log.error("Stripe webhook processing failed", e);
            throw new PaymentException("WEBHOOK_ERROR", "Failed to process webhook: " + e.getMessage(), e);
        }
    }

    @Override
    public List<PaymentMethod> supportedMethods(Map<String, Object> providerConfig) {
        List<PaymentMethod> methods = new ArrayList<>();
        methods.add(PaymentMethod.CARD);
        methods.add(PaymentMethod.LINK);

        Boolean supportsAcp = (Boolean) providerConfig.getOrDefault("supportsAcp", false);
        if (supportsAcp) {
            methods.add(PaymentMethod.WALLET);
            methods.add(PaymentMethod.APPLE_PAY);
            methods.add(PaymentMethod.GOOGLE_PAY);
        }

        return methods;
    }

    /**
     * Populates ticket-related data (QR code URL, ticket transaction ID, email sent status)
     * in the payment status response.
     *
     * This method queries the ticket transaction by Stripe payment intent ID and filters by eventId
     * to ensure we get the correct ticket transaction. If not found, it falls back to querying by
     * transaction_reference.
     *
     * @param response the payment status response to populate
     * @param transaction the payment transaction entity
     * @param status the payment status (must be "SUCCEEDED" for ticket data to be populated)
     */
    private void populateTicketData(PaymentSessionResponse response, UserPaymentTransaction transaction, String status) {
        // Only populate ticket data if payment succeeded and it's a ticket purchase
        if (!"SUCCEEDED".equals(status)) {
            return;
        }

        // Check if this is a ticket purchase (has eventId)
        Long eventId = transaction.getEvent() != null ? transaction.getEvent().getId() : null;
        if (eventId == null) {
            log.debug("Payment transaction {} is not a ticket purchase (no eventId), skipping ticket data population", transaction.getId());
            return;
        }

        response.setEventId(eventId);

        try {
            // Find the EventTicketTransaction associated with this payment
            String stripePaymentIntentId = transaction.getStripePaymentIntentId();
            Optional<EventTicketTransaction> ticketTransactionOpt = Optional.empty();

            // CRITICAL: Query by stripePaymentIntentId first (primary method)
            // This is the most reliable way since ticket transactions store stripe_payment_intent_id
            if (stripePaymentIntentId != null && !stripePaymentIntentId.isEmpty()) {
                ticketTransactionOpt =
                    eventTicketTransactionRepository
                        .findByStripePaymentIntentId(stripePaymentIntentId)
                        .filter(t -> eventId.equals(t.getEventId())); // Ensure it's for the correct event

                if (ticketTransactionOpt.isPresent()) {
                    log.debug(
                        "Found ticket transaction by stripePaymentIntentId {} for payment {}",
                        stripePaymentIntentId,
                        transaction.getId()
                    );
                }
            }

            // Fallback: If not found by stripePaymentIntentId, try by transaction_reference
            // Note: transaction_reference may be set by database trigger, but it's read-only in entity
            if (ticketTransactionOpt.isEmpty()) {
                log.debug(
                    "Ticket transaction not found by stripePaymentIntentId {}, trying transaction_reference fallback",
                    stripePaymentIntentId
                );

                // Query ticket transactions by transaction_reference and filter by eventId
                List<EventTicketTransaction> ticketsByReference = eventTicketTransactionRepository
                    .findByTransactionReference(String.valueOf(transaction.getId()))
                    .stream()
                    .filter(t -> eventId.equals(t.getEventId()))
                    .toList();

                if (!ticketsByReference.isEmpty()) {
                    ticketTransactionOpt = Optional.of(ticketsByReference.get(0));
                    log.info(
                        "Found ticket transaction by transaction_reference {} for payment {}",
                        transaction.getId(),
                        transaction.getId()
                    );
                }
            }

            // Populate ticket data if found
            ticketTransactionOpt.ifPresent(ticketTransaction -> {
                response.setTicketTransactionId(ticketTransaction.getId());

                // Get QR code URL from ticket transaction
                String qrCodeUrl = ticketTransaction.getQrCodeImageUrl();
                if (qrCodeUrl != null && !qrCodeUrl.isEmpty()) {
                    response.setQrCodeUrl(qrCodeUrl);
                    log.info("Included QR code URL in payment status response for transaction {}: {}", transaction.getId(), qrCodeUrl);
                } else {
                    log.warn("Ticket transaction {} exists but has no QR code URL", ticketTransaction.getId());
                }

                // Check if email was sent (stored in confirmationSentAt field)
                Boolean emailSent = ticketTransaction.getConfirmationSentAt() != null;
                response.setEmailSent(emailSent);

                log.info(
                    "Populated ticket data for payment {}: ticketTransactionId={}, qrCodeUrl={}, emailSent={}",
                    transaction.getId(),
                    ticketTransaction.getId(),
                    qrCodeUrl != null ? "present" : "missing",
                    emailSent
                );
            });

            if (ticketTransactionOpt.isEmpty()) {
                log.debug(
                    "No ticket transaction found for payment {} with Stripe payment intent ID {} and eventId {}",
                    transaction.getId(),
                    stripePaymentIntentId,
                    eventId
                );
            }
        } catch (Exception e) {
            log.error("Failed to populate ticket data for payment {}: {}", transaction.getId(), e.getMessage(), e);
            // Don't fail the entire response - just log the error
        }
    }

    /**
     * Convert BigDecimal amount to Stripe amount (cents).
     */
    private Long convertToStripeAmount(BigDecimal amount, String currency) {
        // Stripe amounts are in smallest currency unit (cents for USD)
        return amount.multiply(BigDecimal.valueOf(100)).longValue();
    }

    /**
     * Maps Stripe payment intent status (lowercase) to our PaymentStatus enum (uppercase).
     * CRITICAL: Frontend expects uppercase enum values (SUCCEEDED, FAILED, etc.)
     *
     * @param stripeStatus Stripe payment intent status (e.g., "succeeded", "processing")
     * @return Uppercase status enum (e.g., "SUCCEEDED", "PROCESSING")
     */
    private String mapStripeStatus(String stripeStatus) {
        if (stripeStatus == null || stripeStatus.isEmpty()) {
            return "PENDING";
        }

        // Normalize to lowercase for case-insensitive comparison
        String normalizedStatus = stripeStatus.toLowerCase().trim();

        return switch (normalizedStatus) {
            case "succeeded" -> "SUCCEEDED"; // CRITICAL: Uppercase for frontend enum
            case "processing" -> "PROCESSING";
            case "requires_payment_method", "requires_confirmation", "requires_action", "requires_capture" -> "PENDING";
            case "canceled", "cancelled" -> "CANCELLED";
            case "payment_failed", "failed" -> "FAILED";
            default -> {
                log.warn("Unknown Stripe payment status: {}, defaulting to PENDING", stripeStatus);
                yield "PENDING";
            }
        };
    }

    /**
     * Handles payment_intent.succeeded webhook event from Stripe.
     *
     * CRITICAL: This method MUST create EventTicketTransaction for ticket purchases.
     *
     * Implementation matches BACKEND_WEBHOOK_IMPLEMENTATION_REQUIRED.md specification.
     *
     * @param event the Stripe webhook event
     * @param result the result map to populate with webhook response data
     */
    private void handlePaymentIntentSucceeded(Event event, Map<String, Object> result) {
        PaymentIntent paymentIntent = (PaymentIntent) event
            .getDataObjectDeserializer()
            .getObject()
            .orElseThrow(() -> new RuntimeException("Failed to deserialize PaymentIntent from webhook event"));

        String stripePaymentIntentId = paymentIntent.getId();
        log.info("[StripePaymentAdapter] Processing payment_intent.succeeded webhook for payment intent: {}", stripePaymentIntentId);

        // Find the payment transaction by Stripe payment intent ID
        Optional<UserPaymentTransaction> transactionOpt = transactionRepository.findByStripePaymentIntentId(stripePaymentIntentId);

        // Fallback: Try to find by external transaction ID if not found by payment intent ID
        if (transactionOpt.isEmpty()) {
            transactionOpt =
                transactionRepository
                    .findAll()
                    .stream()
                    .filter(t -> stripePaymentIntentId.equals(t.getExternalTransactionId()))
                    .findFirst();
        }

        UserPaymentTransaction transaction = transactionOpt.orElseThrow(() ->
            new RuntimeException("Payment transaction not found for Stripe payment intent: " + stripePaymentIntentId)
        );

        log.info(
            "[StripePaymentAdapter] Found payment transaction: id={}, eventId={}, transactionType={}",
            transaction.getId(),
            transaction.getEvent() != null ? transaction.getEvent().getId() : null,
            transaction.getTransactionType()
        );

        // Update transaction status to SUCCEEDED
        String oldStatus = transaction.getStatus();
        transaction.setStatus("SUCCEEDED");
        transaction.setUpdatedAt(ZonedDateTime.now());

        // Ensure stripe_payment_intent_id is stored (should already be there, but ensure it)
        if (transaction.getStripePaymentIntentId() == null || transaction.getStripePaymentIntentId().isEmpty()) {
            transaction.setStripePaymentIntentId(stripePaymentIntentId);
            log.info(
                "[StripePaymentAdapter] Stored Stripe payment intent ID {} in transaction {}",
                stripePaymentIntentId,
                transaction.getId()
            );
        }

        // Store payment method if available
        if (paymentIntent.getPaymentMethod() != null) {
            String paymentMethod = paymentIntent.getPaymentMethod().toString();
            transaction.setPaymentMethod(paymentMethod);
        }

        // CRITICAL: Extract and store customer email from PaymentIntent
        String customerEmail = paymentIntent.getReceiptEmail();
        if (customerEmail == null || customerEmail.isEmpty()) {
            // Try to get from metadata
            Map<String, String> metadata = paymentIntent.getMetadata();
            if (metadata != null) {
                customerEmail = metadata.get("customerEmail");
                if (customerEmail == null || customerEmail.isEmpty()) {
                    customerEmail = metadata.get("email");
                }
            }
        }

        // Store customer email in transaction metadata
        if (customerEmail != null && !customerEmail.isEmpty()) {
            try {
                Map<String, Object> metadataMap = new HashMap<>();
                if (transaction.getMetadata() != null && !transaction.getMetadata().isEmpty()) {
                    try {
                        metadataMap = objectMapper.readValue(transaction.getMetadata(), Map.class);
                    } catch (Exception e) {
                        log.debug("[StripePaymentAdapter] Failed to parse existing metadata, creating new metadata map");
                    }
                }
                metadataMap.put("email", customerEmail);
                metadataMap.put("customerEmail", customerEmail);
                transaction.setMetadata(objectMapper.writeValueAsString(metadataMap));
                log.info("[StripePaymentAdapter] Stored customer email {} in transaction {} metadata", customerEmail, transaction.getId());
            } catch (Exception e) {
                log.warn("[StripePaymentAdapter] Failed to store customer email in transaction metadata: {}", e.getMessage());
            }
        }

        // Save updated transaction
        transactionRepository.save(transaction);
        log.info("[StripePaymentAdapter] Updated transaction {} status from {} to SUCCEEDED", transaction.getId(), oldStatus);

        // CRITICAL: If this is a ticket purchase, create EventTicketTransaction and generate QR code
        Long eventId = transaction.getEvent() != null ? transaction.getEvent().getId() : null;
        if (eventId != null || "TICKET_SALE".equals(transaction.getTransactionType())) {
            log.info("[StripePaymentAdapter] This is a ticket purchase (eventId={}), triggering ticket generation", eventId);

            try {
                // Call synchronous ticket generation service
                // This will:
                // 1. Create or find EventTicketTransaction
                // 2. Generate QR code
                // 3. Send ticket email
                ticketGenerationService.processTicketGenerationSync(transaction, stripePaymentIntentId);

                log.info("[StripePaymentAdapter] Successfully processed ticket generation for transaction {}", transaction.getId());
            } catch (Exception e) {
                log.error(
                    "[StripePaymentAdapter] Failed to process ticket generation for transaction {}: {}",
                    transaction.getId(),
                    e.getMessage(),
                    e
                );
                // Don't fail the webhook - ticket generation can be retried
                // But log the error for monitoring
            }
        } else {
            log.debug(
                "[StripePaymentAdapter] Payment {} succeeded but is not a ticket purchase (no eventId), skipping ticket generation",
                transaction.getId()
            );
        }

        // Also publish payment success event for async processing (backup/retry mechanism)
        try {
            eventPublisher.publishEvent(new PaymentSuccessEvent(this, transaction, stripePaymentIntentId));
            log.debug("[StripePaymentAdapter] Published PaymentSuccessEvent for transaction {} (async backup)", transaction.getId());
        } catch (Exception e) {
            log.error(
                "[StripePaymentAdapter] Failed to publish PaymentSuccessEvent for transaction {}: {}",
                transaction.getId(),
                e.getMessage(),
                e
            );
            // Don't fail webhook processing if event publishing fails
        }

        // Populate result map for webhook response
        result.put("paymentIntentId", stripePaymentIntentId);
        result.put("transactionId", transaction.getId());
        result.put("amount", paymentIntent.getAmount());
        result.put("currency", paymentIntent.getCurrency());
        result.put("status", "SUCCEEDED");

        log.info("[StripePaymentAdapter] Completed processing payment_intent.succeeded webhook for transaction {}", transaction.getId());
    }

    /**
     * Handle payment_intent.payment_failed webhook event.
     * Updates the transaction status to FAILED and stores the failure reason.
     */
    private void handlePaymentIntentFailed(Event event, Map<String, Object> result) {
        PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);
        if (paymentIntent == null) {
            log.warn("PaymentIntent is null in payment_intent.payment_failed webhook event");
            return;
        }

        String stripePaymentIntentId = paymentIntent.getId();
        String failureReason = paymentIntent.getLastPaymentError() != null
            ? paymentIntent.getLastPaymentError().getMessage()
            : "Payment failed";

        log.info(
            "Processing payment_intent.payment_failed webhook for payment intent: {}, reason: {}",
            stripePaymentIntentId,
            failureReason
        );

        // Find transaction by Stripe payment intent ID
        Optional<UserPaymentTransaction> transactionOpt = transactionRepository.findByStripePaymentIntentId(stripePaymentIntentId);

        boolean transactionUpdated = transactionOpt
            .map(transaction -> {
                log.info("Found transaction {} for failed payment intent {}", transaction.getId(), stripePaymentIntentId);

                // Update transaction status to FAILED
                transaction.setStatus("FAILED");
                transaction.setFailureReason(failureReason);
                transaction.setUpdatedAt(ZonedDateTime.now());

                // Ensure stripe_payment_intent_id is stored
                if (transaction.getStripePaymentIntentId() == null || transaction.getStripePaymentIntentId().isEmpty()) {
                    transaction.setStripePaymentIntentId(stripePaymentIntentId);
                }

                // Save updated transaction
                transactionRepository.save(transaction);
                log.info("Updated transaction {} status to FAILED", transaction.getId());

                result.put("paymentIntentId", stripePaymentIntentId);
                result.put("transactionId", transaction.getId());
                result.put("failureReason", failureReason);
                result.put("status", "FAILED");

                return true;
            })
            .orElse(false);

        if (!transactionUpdated) {
            // Try to find by external transaction ID (fallback)
            Optional<UserPaymentTransaction> transactionByExternalId = transactionRepository
                .findAll()
                .stream()
                .filter(t -> stripePaymentIntentId.equals(t.getExternalTransactionId()))
                .findFirst();

            transactionByExternalId
                .map(transaction -> {
                    log.info(
                        "Found transaction {} by external transaction ID for failed payment intent {}",
                        transaction.getId(),
                        stripePaymentIntentId
                    );

                    transaction.setStatus("FAILED");
                    transaction.setFailureReason(failureReason);
                    transaction.setStripePaymentIntentId(stripePaymentIntentId);
                    transaction.setUpdatedAt(ZonedDateTime.now());

                    transactionRepository.save(transaction);
                    log.info("Updated transaction {} status to FAILED (found by external ID)", transaction.getId());

                    result.put("paymentIntentId", stripePaymentIntentId);
                    result.put("transactionId", transaction.getId());
                    result.put("failureReason", failureReason);
                    result.put("status", "FAILED");

                    return true;
                })
                .orElseGet(() -> {
                    log.warn("No transaction found for failed Stripe payment intent: {}", stripePaymentIntentId);
                    result.put("paymentIntentId", stripePaymentIntentId);
                    result.put("failureReason", failureReason);
                    result.put("warning", "Transaction not found for payment intent");
                    return false;
                });
        }
    }

    /**
     * Handle checkout.session.completed webhook event.
     * This event is fired when a Stripe Checkout Session is completed successfully.
     * For ticket purchases, this should also trigger ticket generation.
     */
    private void handleCheckoutSessionCompleted(Event event, Map<String, Object> result) {
        log.info("[StripePaymentAdapter] Processing checkout.session.completed webhook event: {}", event.getId());

        com.stripe.model.checkout.Session session = (com.stripe.model.checkout.Session) event
            .getDataObjectDeserializer()
            .getObject()
            .orElse(null);

        if (session == null) {
            log.warn("[StripePaymentAdapter] Checkout session is null in checkout.session.completed webhook event");
            return;
        }

        String sessionId = session.getId();
        String paymentIntentId = session.getPaymentIntent();

        log.info("[StripePaymentAdapter] Checkout session completed: sessionId={}, paymentIntentId={}", sessionId, paymentIntentId);

        // Extract customer email from checkout session
        String customerEmailTemp = session.getCustomerEmail();
        if (customerEmailTemp == null || customerEmailTemp.isEmpty()) {
            // Try to get from customer details
            if (session.getCustomerDetails() != null) {
                customerEmailTemp = session.getCustomerDetails().getEmail();
            }
        }
        final String customerEmail = customerEmailTemp;

        // If payment intent ID is available, find the transaction and trigger ticket generation
        if (paymentIntentId != null && !paymentIntentId.isEmpty()) {
            log.info("[StripePaymentAdapter] Processing checkout session with payment intent: {}", paymentIntentId);

            // Find transaction by payment intent ID
            Optional<UserPaymentTransaction> transactionOpt = transactionRepository.findByStripePaymentIntentId(paymentIntentId);

            final String stripePaymentIntentIdFinal = paymentIntentId;
            final String sessionIdFinal = sessionId;

            transactionOpt.ifPresent(transaction -> {
                log.info(
                    "[StripePaymentAdapter] Found payment transaction: id={}, eventId={}, transactionType={}",
                    transaction.getId(),
                    transaction.getEvent() != null ? transaction.getEvent().getId() : null,
                    transaction.getTransactionType()
                );

                // Create final copies for lambda usage
                final UserPaymentTransaction transactionFinal = transaction;
                final String customerEmailFinal = customerEmail;

                // Update transaction status if not already SUCCEEDED
                if (!"SUCCEEDED".equals(transactionFinal.getStatus())) {
                    transactionFinal.setStatus("SUCCEEDED");
                    transactionFinal.setUpdatedAt(ZonedDateTime.now());
                    transactionRepository.save(transactionFinal);
                    log.info(
                        "[StripePaymentAdapter] Updated transaction {} status to SUCCEEDED via checkout session",
                        transactionFinal.getId()
                    );
                }

                // Store customer email if available
                if (customerEmailFinal != null && !customerEmailFinal.isEmpty()) {
                    try {
                        Map<String, Object> metadata = new HashMap<>();
                        if (transactionFinal.getMetadata() != null && !transactionFinal.getMetadata().isEmpty()) {
                            try {
                                metadata = objectMapper.readValue(transactionFinal.getMetadata(), Map.class);
                            } catch (Exception e) {
                                log.debug("Failed to parse existing metadata, creating new metadata map");
                            }
                        }
                        metadata.put("email", customerEmailFinal);
                        metadata.put("customerEmail", customerEmailFinal);
                        transactionFinal.setMetadata(objectMapper.writeValueAsString(metadata));
                        transactionRepository.save(transactionFinal);
                        log.info(
                            "[StripePaymentAdapter] Stored customer email {} in transaction {} metadata (from checkout session)",
                            customerEmailFinal,
                            transactionFinal.getId()
                        );
                    } catch (Exception e) {
                        log.warn("Failed to store customer email in transaction metadata: {}", e.getMessage());
                    }
                }

                // CRITICAL: If this is a ticket purchase, trigger ticket generation
                Long eventId = transactionFinal.getEvent() != null ? transactionFinal.getEvent().getId() : null;
                if (eventId != null || "TICKET_SALE".equals(transactionFinal.getTransactionType())) {
                    log.info(
                        "[StripePaymentAdapter] This is a ticket purchase (eventId={}), triggering ticket generation via checkout session",
                        eventId
                    );

                    try {
                        ticketGenerationService.processTicketGenerationSync(transactionFinal, stripePaymentIntentIdFinal);
                        log.info(
                            "[StripePaymentAdapter] Successfully processed ticket generation for transaction {} via checkout session",
                            transactionFinal.getId()
                        );
                    } catch (Exception e) {
                        log.error(
                            "[StripePaymentAdapter] Failed to process ticket generation for transaction {} via checkout session: {}",
                            transactionFinal.getId(),
                            e.getMessage(),
                            e
                        );
                        // Don't fail the webhook - ticket generation can be retried
                    }
                } else {
                    log.debug(
                        "[StripePaymentAdapter] Checkout session {} completed but is not a ticket purchase (no eventId), skipping ticket generation",
                        sessionIdFinal
                    );
                }
            });

            if (transactionOpt.isEmpty()) {
                log.warn(
                    "[StripePaymentAdapter] No payment transaction found for payment intent {} from checkout session {}",
                    paymentIntentId,
                    sessionId
                );
            }
        } else {
            log.warn("[StripePaymentAdapter] Checkout session {} has no payment intent ID", sessionId);
        }

        result.put("sessionId", sessionId);
        result.put("paymentIntentId", paymentIntentId);
        result.put("amount", session.getAmountTotal());
        result.put("currency", session.getCurrency());
        result.put("status", "completed");
    }

    /**
     * Handle charge.refunded webhook event.
     */
    private void handleRefund(Event event, Map<String, Object> result) {
        Charge charge = (Charge) event.getDataObjectDeserializer().getObject().orElse(null);
        if (charge != null) {
            result.put("chargeId", charge.getId());
            result.put("refunded", charge.getRefunded());
        }
    }

    /**
     * Retrieve Payment Intent from Stripe.
     * This method can be used by other services to retrieve Payment Intent metadata.
     *
     * @param paymentIntentId Stripe Payment Intent ID
     * @param tenantId Tenant ID to get Stripe configuration
     * @return PaymentIntent object, or null if not found or error occurs
     */
    public PaymentIntent retrievePaymentIntent(String paymentIntentId, String tenantId) {
        try {
            // Get provider config for tenant
            Map<String, Object> providerConfig = configService.getProviderConfig(tenantId, PaymentProvider.STRIPE).orElse(null);

            if (providerConfig == null) {
                log.warn("Stripe provider config not found for tenant: {}", tenantId);
                return null;
            }

            // Set Stripe API key
            String apiKey = (String) providerConfig.get("secretKey");
            if (apiKey == null || apiKey.isEmpty()) {
                log.warn("Stripe secret key not configured for tenant: {}", tenantId);
                return null;
            }

            Stripe.apiKey = apiKey;

            // Retrieve Payment Intent
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            log.debug("Retrieved Payment Intent {} from Stripe", paymentIntentId);
            return paymentIntent;
        } catch (StripeException e) {
            log.error("Failed to retrieve Payment Intent {}: {}", paymentIntentId, e.getMessage(), e);
            return null;
        } catch (Exception e) {
            log.error("Unexpected error retrieving Payment Intent {}: {}", paymentIntentId, e.getMessage(), e);
            return null;
        }
    }
}
