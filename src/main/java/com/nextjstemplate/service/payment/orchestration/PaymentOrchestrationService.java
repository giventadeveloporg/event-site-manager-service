package com.nextjstemplate.service.payment.orchestration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.domain.EventTicketTransaction;
import com.nextjstemplate.domain.UserPaymentTransaction;
import com.nextjstemplate.domain.enumeration.PaymentProvider;
import com.nextjstemplate.domain.enumeration.PaymentUseCase;
import com.nextjstemplate.repository.EventDetailsRepository;
import com.nextjstemplate.repository.EventTicketTransactionRepository;
import com.nextjstemplate.repository.UserPaymentTransactionRepository;
import com.nextjstemplate.service.payment.PaymentException;
import com.nextjstemplate.service.payment.PaymentProviderConfigService;
import com.nextjstemplate.service.payment.PaymentService;
import com.nextjstemplate.service.payment.TicketGenerationService;
import com.nextjstemplate.service.payment.dto.PaymentSessionRequest;
import com.nextjstemplate.service.payment.dto.PaymentSessionResponse;
import com.nextjstemplate.service.payment.dto.RefundRequest;
import com.nextjstemplate.service.payment.dto.RefundResponse;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
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
    private final EventTicketTransactionRepository eventTicketTransactionRepository;
    private final TicketGenerationService ticketGenerationService;
    private final ObjectMapper objectMapper;
    private final Map<PaymentProvider, PaymentService> paymentServices;

    public PaymentOrchestrationService(
        PaymentProviderConfigService configService,
        UserPaymentTransactionRepository transactionRepository,
        EventDetailsRepository eventDetailsRepository,
        EventTicketTransactionRepository eventTicketTransactionRepository,
        TicketGenerationService ticketGenerationService,
        List<PaymentService> paymentServicesList
    ) {
        this.configService = configService;
        this.transactionRepository = transactionRepository;
        this.eventDetailsRepository = eventDetailsRepository;
        this.eventTicketTransactionRepository = eventTicketTransactionRepository;
        this.ticketGenerationService = ticketGenerationService;
        this.objectMapper = new ObjectMapper();
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
        log.debug(
            "Determining provider for request: eventId={}, paymentUseCase={}, tenantId={}",
            request.getEventId(),
            request.getPaymentUseCase(),
            request.getTenantId()
        );

        // Get active provider configurations ordered by fallback priority
        // Create a mutable copy since getActiveProviderConfigs() returns an immutable list
        List<Map<String, Object>> providerConfigs = new ArrayList<>(
            configService.getActiveProviderConfigs(request.getTenantId(), request.getPaymentUseCase())
        );

        if (providerConfigs.isEmpty()) {
            throw new PaymentException(
                "NO_PROVIDER_CONFIGURED",
                "No active payment provider configured for tenant: " + request.getTenantId()
            );
        }

        // CRITICAL: Check explicit request parameters (HIGHEST PRIORITY)
        // Frontend sends paymentProvider: "GIVEBUTTER" OR paymentType: "DONATION_ZERO_FEE" to explicitly request GiveButter
        // This must be checked BEFORE event metadata to ensure frontend's explicit request is honored
        // This ensures donation-based checkout flows are routed correctly without disrupting Stripe flows
        // IMPORTANT: Even when items array is present (ticketed fundraisers), route to GiveButter if explicitly requested
        Map<String, Object> requestMetadata = request.getMetadata();
        boolean shouldRouteToGiveButter = false;
        String requestedProvider = null;

        // Check request metadata for explicit paymentProvider (frontend may send this)
        if (requestMetadata != null) {
            Object providerObj = requestMetadata.get("paymentProvider");
            if (providerObj != null) {
                requestedProvider = providerObj.toString().toUpperCase();
                log.debug("Request explicitly specifies paymentProvider: {}", requestedProvider);
                if ("GIVEBUTTER".equals(requestedProvider)) {
                    shouldRouteToGiveButter = true;
                }
            }
        }

        // Check paymentUseCase: DONATION_ZERO_FEE also indicates GiveButter routing (as per document)
        // This is equivalent to paymentType: "DONATION_ZERO_FEE" in frontend request
        if (request.getPaymentUseCase() == PaymentUseCase.DONATION_ZERO_FEE) {
            shouldRouteToGiveButter = true;
            log.debug("Request paymentUseCase is DONATION_ZERO_FEE - indicates GiveButter routing");
        }

        // Also check returnUrl pattern - donation-based checkouts use /donation/success
        // This is a separate control path indicator (doesn't interfere with Stripe flows)
        if (request.getReturnUrl() != null && request.getReturnUrl().contains("/donation/success")) {
            shouldRouteToGiveButter = true;
            log.debug("Request returnUrl indicates donation-based checkout flow: {}", request.getReturnUrl());
        }

        // If request explicitly requests GiveButter (via any of the above indicators),
        // prioritize GiveButter immediately (donation-based checkout flow - separate from Stripe)
        // This applies even when items array is present (ticketed fundraisers)
        // CRITICAL: Query GiveButter config directly from database (not filtered by paymentUseCase)
        // because GiveButter config may have payment_use_case = DONATION_ZERO_FEE while request has TICKET_SALE
        if (shouldRouteToGiveButter) {
            // Query GiveButter config directly from database (bypasses paymentUseCase filter)
            // This ensures we find GiveButter config even if payment_use_case doesn't match request
            Optional<Map<String, Object>> givebutterConfigOpt = configService.getProviderConfig(
                request.getTenantId(),
                PaymentProvider.GIVEBUTTER
            );

            final String finalRequestedProvider = requestedProvider; // Final copy for lambda
            final PaymentUseCase finalPaymentUseCase = request.getPaymentUseCase(); // Final copy for lambda
            final String finalReturnUrl = request.getReturnUrl(); // Final copy for lambda
            givebutterConfigOpt.ifPresentOrElse(
                config -> {
                    // Verify the config is active
                    Object isActive = config.get("isActive");
                    if (Boolean.TRUE.equals(isActive)) {
                        // Route to GiveButter immediately (separate control path from Stripe)
                        // Remove all other providers to ensure only GiveButter is used
                        providerConfigs.clear();
                        providerConfigs.add(config);
                        log.info(
                            "Request explicitly requests GiveButter (paymentProvider={}, paymentUseCase={}, returnUrl={}) - routing to GiveButter immediately (donation-based checkout flow, separate from Stripe)",
                            finalRequestedProvider,
                            finalPaymentUseCase,
                            finalReturnUrl
                        );
                    } else {
                        log.warn(
                            "Request specifies GiveButter but provider config is not active for tenant {} - falling back to other providers",
                            request.getTenantId()
                        );
                    }
                },
                () ->
                    log.warn(
                        "Request specifies GiveButter but provider not configured for tenant {} - falling back to other providers",
                        request.getTenantId()
                    )
            );
        }

        // Prioritize Givebutter for OFFERING use case (if not already prioritized above)
        if (!shouldRouteToGiveButter && request.getPaymentUseCase() == PaymentUseCase.OFFERING) {
            // Check if Givebutter is configured
            Optional<Map<String, Object>> givebutterConfig = providerConfigs
                .stream()
                .filter(config -> PaymentProvider.GIVEBUTTER.equals(config.get("providerName")))
                .findFirst();

            givebutterConfig.ifPresent(config -> {
                // Move Givebutter to the front of the list
                providerConfigs.remove(config);
                providerConfigs.add(0, config);
                log.debug("Prioritizing Givebutter for use case: {}", request.getPaymentUseCase());
            });
        }

        // For TICKET_SALE and DONATION use cases, check if event has zero-fee provider configured
        // Also add event metadata to request metadata for adapter access
        // NOTE: This only applies if request doesn't explicitly request GiveButter (separate control path)
        // This ensures normal Stripe checkout flows are not disrupted
        // IMPORTANT: This check is SECONDARY - explicit request parameters take priority
        if (request.getEventId() != null && !shouldRouteToGiveButter) {
            Optional<EventDetails> eventOptional = eventDetailsRepository.findById(request.getEventId());
            log.debug("Event found: {}, eventId: {}", eventOptional.isPresent(), request.getEventId());

            eventOptional.ifPresent(event -> {
                Map<String, Object> eventMetadata = event.getMetadataAsMap();
                log.debug("Event metadata: {}", eventMetadata != null && !eventMetadata.isEmpty() ? "present" : "null or empty");

                // Add event metadata to request metadata for adapter access
                if (request.getMetadata() == null) {
                    request.setMetadata(new HashMap<>());
                }
                if (eventMetadata != null && !eventMetadata.isEmpty()) {
                    request.getMetadata().put("eventMetadata", eventMetadata);
                }

                // CRITICAL: Check donationMetadata field first (separate column), then fallback to metadata.donationConfig
                // This ensures donation-based checkout flows route to GiveButter when configured
                Map<String, Object> donationMetadataMap = parseDonationMetadata(event);
                Boolean isFundraiserEvent = null;
                Boolean isCharityEvent = null;
                String zeroFeeProvider = null;

                if (donationMetadataMap != null && !donationMetadataMap.isEmpty()) {
                    // Use donationMetadata field (priority 1)
                    isFundraiserEvent = (Boolean) donationMetadataMap.get("isFundraiserEvent");
                    isCharityEvent = (Boolean) donationMetadataMap.get("isCharityEvent");
                    zeroFeeProvider = (String) donationMetadataMap.get("zeroFeeProvider");
                    log.debug(
                        "Event {} donationMetadata check: isFundraiserEvent={}, isCharityEvent={}, zeroFeeProvider={}",
                        request.getEventId(),
                        isFundraiserEvent,
                        isCharityEvent,
                        zeroFeeProvider
                    );
                } else if (eventMetadata != null && !eventMetadata.isEmpty()) {
                    // Fallback to metadata.donationConfig (priority 2 - backward compatibility)
                    isFundraiserEvent = (Boolean) eventMetadata.get("isFundraiserEvent");
                    isCharityEvent = (Boolean) eventMetadata.get("isCharityEvent");
                    @SuppressWarnings("unchecked")
                    Map<String, Object> donationConfig = (Map<String, Object>) eventMetadata.get("donationConfig");
                    if (donationConfig != null) {
                        zeroFeeProvider = (String) donationConfig.get("zeroFeeProvider");
                    }
                    log.debug(
                        "Event {} metadata.donationConfig check: isFundraiserEvent={}, isCharityEvent={}, zeroFeeProvider={}",
                        request.getEventId(),
                        isFundraiserEvent,
                        isCharityEvent,
                        zeroFeeProvider
                    );
                }

                // For TICKET_SALE: Check if fundraiser event with GiveButter configured
                // Route to GiveButter for donation-based checkout flows (separate control path from Stripe)
                if (request.getPaymentUseCase() == PaymentUseCase.TICKET_SALE) {
                    log.debug("Processing TICKET_SALE routing for event {}", request.getEventId());

                    // Check if event is configured as fundraiser/charity with GiveButter as zero-fee provider
                    if (
                        (Boolean.TRUE.equals(isFundraiserEvent) || Boolean.TRUE.equals(isCharityEvent)) &&
                        "GIVEBUTTER".equals(zeroFeeProvider)
                    ) {
                        // Check if GiveButter is configured for tenant
                        Optional<Map<String, Object>> givebutterConfig = providerConfigs
                            .stream()
                            .filter(config -> PaymentProvider.GIVEBUTTER.equals(config.get("providerName")))
                            .findFirst();

                        givebutterConfig.ifPresentOrElse(
                            config -> {
                                // Route ticketed fundraiser event to GiveButter (donation-based checkout flow)
                                // This is a SEPARATE control path from normal Stripe checkout
                                providerConfigs.remove(config);
                                providerConfigs.add(0, config);
                                log.info(
                                    "Event {} is fundraiser/charity event with GiveButter configured - routing to GiveButter (donation-based checkout flow)",
                                    request.getEventId()
                                );
                            },
                            () ->
                                log.warn(
                                    "Event {} is configured for GiveButter but provider not available for tenant {} - falling back to other providers",
                                    request.getEventId(),
                                    request.getTenantId()
                                )
                        );
                    } else {
                        // Normal ticket sale - continue with existing Stripe flow (no changes)
                        log.debug(
                            "Event {} is not configured for GiveButter donation flow - using standard provider selection (Stripe)",
                            request.getEventId()
                        );
                    }
                }

                // For DONATION use case: Check if zero-fee provider (GiveButter) is configured
                if (
                    request.getPaymentUseCase() == PaymentUseCase.DONATION ||
                    request.getPaymentUseCase() == PaymentUseCase.DONATION_ZERO_FEE
                ) {
                    // Check if GiveButter is configured as zero-fee provider
                    if ("GIVEBUTTER".equals(zeroFeeProvider)) {
                        Optional<Map<String, Object>> givebutterConfig = providerConfigs
                            .stream()
                            .filter(config -> PaymentProvider.GIVEBUTTER.equals(config.get("providerName")))
                            .findFirst();

                        givebutterConfig.ifPresentOrElse(
                            config -> {
                                // Prioritize GiveButter for donations when configured
                                providerConfigs.remove(config);
                                providerConfigs.add(0, config);
                                log.info(
                                    "Routing donation (use case: {}) to GiveButter (zero-fee provider configured)",
                                    request.getPaymentUseCase()
                                );
                            },
                            () ->
                                log.warn(
                                    "GiveButter configured in event metadata but not available for tenant {} - falling back to other providers",
                                    request.getTenantId()
                                )
                        );
                    } else {
                        // No zero-fee provider configured, prioritize Stripe (nonprofit discount eligible)
                        Optional<Map<String, Object>> stripeConfig = providerConfigs
                            .stream()
                            .filter(config -> PaymentProvider.STRIPE.equals(config.get("providerName")))
                            .findFirst();

                        stripeConfig.ifPresent(config -> {
                            providerConfigs.remove(config);
                            providerConfigs.add(0, config);
                            log.info(
                                "Routing donation (use case: {}) to Stripe (no zero-fee provider configured, nonprofit discount eligible)",
                                request.getPaymentUseCase()
                            );
                        });
                    }
                }
            });
        }

        // Log final provider selection order
        log.debug(
            "Provider selection order: {}",
            providerConfigs.stream().map(config -> ((PaymentProvider) config.get("providerName")).name()).toList()
        );

        // Try each provider in fallback order
        PaymentException lastException = null;
        for (Map<String, Object> providerConfig : providerConfigs) {
            PaymentProvider provider = (PaymentProvider) providerConfig.get("providerName");
            PaymentService paymentService = paymentServices.get(provider);

            if (paymentService == null) {
                log.warn("Payment service not found for provider: {}", provider);
                continue;
            }

            // Note: GiveButter adapter supports donation creation via API
            // The frontend handles webhook-like behavior through polling (no backend webhook listeners)
            // For TICKET_SALE with fundraiser events configured for GiveButter, route to GiveButter
            // This is a separate control path from normal Stripe checkout flows

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

        UserPaymentTransaction transaction;
        try {
            transaction =
                transactionRepository
                    .findById(Long.parseLong(transactionId))
                    .orElseThrow(() -> new PaymentException("TRANSACTION_NOT_FOUND", "Transaction not found: " + transactionId));
        } catch (org.springframework.orm.jpa.JpaSystemException e) {
            if (
                e.getCause() instanceof org.hibernate.HibernateException &&
                e.getCause().getMessage() != null &&
                e.getCause().getMessage().contains("Duplicate row was found")
            ) {
                log.error(
                    "Duplicate primary key found for transaction ID: {}. This indicates database integrity issue. Please check and fix duplicate rows in user_payment_transaction table.",
                    transactionId
                );
                // Try to get transaction using native query workaround
                // Note: This is a temporary workaround. The database should be fixed to remove duplicate primary keys.
                transaction =
                    transactionRepository
                        .findByIdWithDuplicateHandling(Long.parseLong(transactionId))
                        .orElseThrow(() -> new PaymentException("TRANSACTION_NOT_FOUND", "Transaction not found: " + transactionId));
                log.warn(
                    "Using first transaction found with duplicate ID: {}. Database integrity issue detected - please investigate and fix duplicate rows.",
                    transactionId
                );
            } else {
                throw e;
            }
        }

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
     * Process Givebutter webhook event.
     * Updates payment transaction status based on webhook event type.
     *
     * @param event Givebutter webhook event
     * @param tenantId Tenant ID
     * @throws PaymentException if processing fails
     */
    public void processGivebutterWebhook(
        com.nextjstemplate.service.payment.adapter.givebutter.dto.GivebutterWebhookEvent event,
        String tenantId
    ) throws PaymentException {
        log.info("Processing Givebutter webhook event: {} for tenant: {}", event.getType(), tenantId);

        if (event.getDonation() == null || event.getDonation().getId() == null) {
            throw new PaymentException("INVALID_WEBHOOK", "Donation ID not found in webhook event");
        }

        String donationId = event.getDonation().getId();
        String eventType = event.getType();

        // Find transaction by external transaction ID (Givebutter donation ID)
        UserPaymentTransaction transaction = transactionRepository
            .findAll()
            .stream()
            .filter(t -> donationId.equals(t.getExternalTransactionId()) && tenantId.equals(t.getTenantId()))
            .findFirst()
            .orElse(null);

        if (transaction == null) {
            log.warn("Transaction not found for Givebutter donation ID: {} for tenant: {}", donationId, tenantId);
            // Don't throw exception - webhook might be for a donation created outside our system
            return;
        }

        // Process based on event type
        switch (eventType) {
            case "donation.completed":
                transaction.setStatus("SUCCEEDED");
                transaction.setUpdatedAt(ZonedDateTime.now());
                transactionRepository.save(transaction);
                log.info("Updated transaction {} status to SUCCEEDED for Givebutter donation: {}", transaction.getId(), donationId);

                // CRITICAL: Create ticket transaction if this is a ticket purchase
                createTicketTransactionForGivebutter(transaction, event);
                break;
            case "donation.updated":
                // Update status based on donation status
                String donationStatus = event.getDonation().getStatus();
                if (donationStatus != null) {
                    String mappedStatus = mapGivebutterStatus(donationStatus);
                    transaction.setStatus(mappedStatus);
                    transaction.setUpdatedAt(ZonedDateTime.now());
                    transactionRepository.save(transaction);
                    log.info(
                        "Updated transaction {} status to {} for Givebutter donation: {}",
                        transaction.getId(),
                        mappedStatus,
                        donationId
                    );

                    // If status is SUCCEEDED, ensure ticket transaction exists
                    if ("SUCCEEDED".equals(mappedStatus)) {
                        createTicketTransactionForGivebutter(transaction, event);
                    }
                }
                break;
            case "donation.refunded":
                transaction.setStatus("REFUNDED");
                transaction.setUpdatedAt(ZonedDateTime.now());
                transactionRepository.save(transaction);
                log.info("Updated transaction {} status to REFUNDED for Givebutter donation: {}", transaction.getId(), donationId);
                break;
            default:
                log.debug("Unhandled Givebutter webhook event type: {}", eventType);
        }
    }

    /**
     * Create ticket transaction for Givebutter payment.
     * This is called when a Givebutter payment succeeds for a ticket purchase.
     *
     * @param paymentTransaction The payment transaction
     * @param event The Givebutter webhook event
     */
    private void createTicketTransactionForGivebutter(
        UserPaymentTransaction paymentTransaction,
        com.nextjstemplate.service.payment.adapter.givebutter.dto.GivebutterWebhookEvent event
    ) {
        // Check if this is a ticket purchase
        Long eventId = paymentTransaction.getEvent() != null ? paymentTransaction.getEvent().getId() : null;
        boolean isTicketPurchase = eventId != null || PaymentUseCase.TICKET_SALE.name().equals(paymentTransaction.getTransactionType());

        if (!isTicketPurchase) {
            log.debug("Givebutter payment {} is not a ticket purchase, skipping ticket transaction creation", paymentTransaction.getId());
            return;
        }

        if (eventId == null) {
            log.warn("Givebutter payment transaction {} has no eventId, cannot create ticket transaction", paymentTransaction.getId());
            return;
        }

        log.info("Creating ticket transaction for Givebutter payment {} for event {}", paymentTransaction.getId(), eventId);

        try {
            // Extract donation information
            com.nextjstemplate.service.payment.adapter.givebutter.dto.GivebutterDonation donation = event.getDonation();
            String donationId = donation != null ? donation.getId() : paymentTransaction.getExternalTransactionId();

            // CRITICAL: Check if ticket transaction already exists (cross-tenant check first)
            // This prevents the same payment from creating tickets for multiple tenants
            // For Givebutter, we store donation ID in stripePaymentIntentId field for compatibility
            Optional<EventTicketTransaction> existingTicket = Optional.empty();
            if (donationId != null && !donationId.isEmpty()) {
                // Check if ANY transaction exists for this donation ID (cross-tenant)
                if (eventTicketTransactionRepository.existsByStripePaymentIntentId(donationId)) {
                    // Try to find by stripePaymentIntentId AND tenantId
                    existingTicket =
                        eventTicketTransactionRepository.findByStripePaymentIntentIdAndTenantId(
                            donationId,
                            paymentTransaction.getTenantId()
                        );

                    if (existingTicket.isEmpty()) {
                        // Transaction exists but for a DIFFERENT tenant - this is a cross-tenant duplicate!
                        log.warn(
                            "DUPLICATE PREVENTION: Ticket transaction already exists for Givebutter donation {} " +
                            "but for a different tenant. Current tenant: {}. Skipping duplicate creation.",
                            donationId,
                            paymentTransaction.getTenantId()
                        );
                        return;
                    }
                }
            }

            // Also try by transaction reference (payment transaction ID)
            if (existingTicket.isEmpty()) {
                List<EventTicketTransaction> ticketsByRef = eventTicketTransactionRepository.findByTransactionReference(
                    String.valueOf(paymentTransaction.getId())
                );
                if (!ticketsByRef.isEmpty()) {
                    existingTicket = Optional.of(ticketsByRef.get(0));
                }
            }

            // Process existing ticket transaction if found, otherwise create new one
            final boolean[] ticketProcessed = { false };
            existingTicket.ifPresent(existing -> {
                ticketProcessed[0] = true;
                log.info("Ticket transaction {} already exists for Givebutter payment {}", existing.getId(), paymentTransaction.getId());

                // Update status to COMPLETED if not already
                if (!"COMPLETED".equals(existing.getStatus())) {
                    existing.setStatus("COMPLETED");
                    existing.setUpdatedAt(ZonedDateTime.now());
                    eventTicketTransactionRepository.save(existing);
                }

                // Trigger ticket generation (QR code and email) if not already done
                try {
                    // Use donation ID as payment reference for TicketGenerationService compatibility
                    // Note: correctTenantId is null for Givebutter donations (no PaymentIntent metadata)
                    ticketGenerationService.processTicketGenerationSync(paymentTransaction, donationId, null);
                } catch (Exception e) {
                    log.error(
                        "Failed to process ticket generation for existing ticket transaction {}: {}",
                        existing.getId(),
                        e.getMessage(),
                        e
                    );
                }
            });

            // If ticket transaction already exists, return early
            if (ticketProcessed[0]) {
                return;
            }

            // Create new ticket transaction
            EventTicketTransaction ticketTransaction = new EventTicketTransaction();
            ticketTransaction.setTenantId(paymentTransaction.getTenantId());
            ticketTransaction.setEventId(eventId);
            ticketTransaction.setTransactionReference(String.valueOf(paymentTransaction.getId()));

            // Extract email from donation or transaction metadata
            String email = null;
            String firstName = null;
            String lastName = null;
            String phone = null;

            if (donation != null) {
                email = donation.getDonorEmail();
                String donorName = donation.getDonorName();
                if (donorName != null && !donorName.isEmpty()) {
                    String[] nameParts = donorName.split(" ", 2);
                    firstName = nameParts.length > 0 ? nameParts[0] : null;
                    lastName = nameParts.length > 1 ? nameParts[1] : null;
                }
            }

            // Fallback to transaction metadata if donation info not available
            if (email == null || email.isEmpty()) {
                email = extractEmailFromTransactionMetadata(paymentTransaction);
            }
            if (firstName == null) {
                firstName = extractFirstNameFromTransactionMetadata(paymentTransaction);
            }
            if (lastName == null) {
                lastName = extractLastNameFromTransactionMetadata(paymentTransaction);
            }
            if (phone == null) {
                phone = extractPhoneFromTransactionMetadata(paymentTransaction);
            }

            if (email == null || email.isEmpty()) {
                log.warn("No email found for Givebutter payment {}, cannot create ticket transaction", paymentTransaction.getId());
                return;
            }

            ticketTransaction.setEmail(email);
            ticketTransaction.setFirstName(firstName);
            ticketTransaction.setLastName(lastName);
            ticketTransaction.setPhone(phone);

            // Set amount fields
            BigDecimal amount = paymentTransaction.getAmount();
            ticketTransaction.setPricePerUnit(amount);
            ticketTransaction.setTotalAmount(amount);
            ticketTransaction.setFinalAmount(amount);
            ticketTransaction.setQuantity(1); // Default to 1, can be updated from metadata

            // Set status and payment info
            ticketTransaction.setStatus("COMPLETED");
            ticketTransaction.setPaymentMethod("GIVEBUTTER");
            ticketTransaction.setPaymentReference(donationId != null ? donationId : paymentTransaction.getExternalTransactionId());

            // CRITICAL: Store Givebutter donation ID in stripePaymentIntentId field for compatibility
            // This allows existing queries to find ticket transactions by payment reference
            ticketTransaction.setStripePaymentIntentId(donationId != null ? donationId : paymentTransaction.getExternalTransactionId());
            ticketTransaction.setStripePaymentStatus("succeeded");
            ticketTransaction.setStripePaymentCurrency(paymentTransaction.getCurrency());

            // Set dates
            ticketTransaction.setPurchaseDate(
                paymentTransaction.getCreatedAt() != null ? paymentTransaction.getCreatedAt() : ZonedDateTime.now()
            );
            ticketTransaction.setCreatedAt(ZonedDateTime.now());
            ticketTransaction.setUpdatedAt(ZonedDateTime.now());

            // Save ticket transaction
            EventTicketTransaction savedTicket = eventTicketTransactionRepository.save(ticketTransaction);
            log.info(
                "Created ticket transaction {} for Givebutter payment {} (donation ID: {})",
                savedTicket.getId(),
                paymentTransaction.getId(),
                donationId
            );

            // Trigger ticket generation (QR code and email)
            try {
                // Use donation ID as payment reference for TicketGenerationService compatibility
                // Note: correctTenantId is null for Givebutter donations (no PaymentIntent metadata)
                ticketGenerationService.processTicketGenerationSync(
                    paymentTransaction,
                    donationId != null ? donationId : paymentTransaction.getExternalTransactionId(),
                    null
                );
                log.info("Successfully processed ticket generation for Givebutter payment {}", paymentTransaction.getId());
            } catch (Exception e) {
                log.error(
                    "Failed to process ticket generation for Givebutter payment {}: {}",
                    paymentTransaction.getId(),
                    e.getMessage(),
                    e
                );
                // Don't fail the webhook - ticket generation can be retried manually
            }
        } catch (Exception e) {
            log.error("Error creating ticket transaction for Givebutter payment {}: {}", paymentTransaction.getId(), e.getMessage(), e);
            // Don't fail the webhook - ticket transaction creation can be retried
        }
    }

    /**
     * Extract email from transaction metadata.
     */
    private String extractEmailFromTransactionMetadata(UserPaymentTransaction transaction) {
        if (transaction.getMetadata() == null || transaction.getMetadata().isEmpty()) {
            return null;
        }
        try {
            Map<String, Object> metadata = objectMapper.readValue(transaction.getMetadata(), Map.class);
            Object email = metadata.get("email");
            if (email != null && !email.toString().isEmpty()) {
                return email.toString();
            }
            Object customerEmail = metadata.get("customerEmail");
            if (customerEmail != null && !customerEmail.toString().isEmpty()) {
                return customerEmail.toString();
            }
        } catch (Exception e) {
            log.debug("Failed to parse transaction metadata JSON: {}", e.getMessage());
        }
        return null;
    }

    /**
     * Extract first name from transaction metadata.
     */
    private String extractFirstNameFromTransactionMetadata(UserPaymentTransaction transaction) {
        if (transaction.getMetadata() == null || transaction.getMetadata().isEmpty()) {
            return null;
        }
        try {
            Map<String, Object> metadata = objectMapper.readValue(transaction.getMetadata(), Map.class);
            Object firstName = metadata.get("firstName");
            if (firstName != null) {
                return firstName.toString();
            }
            Object customerName = metadata.get("customerName");
            if (customerName != null) {
                String[] nameParts = customerName.toString().split(" ", 2);
                return nameParts.length > 0 ? nameParts[0] : null;
            }
        } catch (Exception e) {
            log.debug("Failed to parse transaction metadata JSON: {}", e.getMessage());
        }
        return null;
    }

    /**
     * Extract last name from transaction metadata.
     */
    private String extractLastNameFromTransactionMetadata(UserPaymentTransaction transaction) {
        if (transaction.getMetadata() == null || transaction.getMetadata().isEmpty()) {
            return null;
        }
        try {
            Map<String, Object> metadata = objectMapper.readValue(transaction.getMetadata(), Map.class);
            Object lastName = metadata.get("lastName");
            if (lastName != null) {
                return lastName.toString();
            }
            Object customerName = metadata.get("customerName");
            if (customerName != null) {
                String[] nameParts = customerName.toString().split(" ", 2);
                return nameParts.length > 1 ? nameParts[1] : null;
            }
        } catch (Exception e) {
            log.debug("Failed to parse transaction metadata JSON: {}", e.getMessage());
        }
        return null;
    }

    /**
     * Extract phone from transaction metadata.
     */
    private String extractPhoneFromTransactionMetadata(UserPaymentTransaction transaction) {
        if (transaction.getMetadata() == null || transaction.getMetadata().isEmpty()) {
            return null;
        }
        try {
            Map<String, Object> metadata = objectMapper.readValue(transaction.getMetadata(), Map.class);
            Object phone = metadata.get("phone");
            if (phone != null) {
                return phone.toString();
            }
        } catch (Exception e) {
            log.debug("Failed to parse transaction metadata JSON: {}", e.getMessage());
        }
        return null;
    }

    /**
     * Map Givebutter status to internal status.
     */
    private String mapGivebutterStatus(String givebutterStatus) {
        if (givebutterStatus == null) {
            return "PENDING";
        }

        return switch (givebutterStatus.toUpperCase()) {
            case "COMPLETED", "SUCCEEDED", "PAID" -> "SUCCEEDED";
            case "PENDING", "PROCESSING" -> "PENDING";
            case "FAILED", "CANCELLED" -> "FAILED";
            case "REFUNDED" -> "REFUNDED";
            default -> "PENDING";
        };
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

        // CRITICAL: Store provider name in metadata for later retrieval
        transactionMetadata.put("provider", provider.name());

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

            // For Givebutter, store donation ID in stripePaymentIntentId field for compatibility
            // This allows existing queries to find transactions by payment reference
            if (provider == PaymentProvider.GIVEBUTTER && externalId != null) {
                transaction.setStripePaymentIntentId(externalId.toString());
                log.debug("Stored Givebutter donation ID {} in stripePaymentIntentId field for compatibility", externalId);
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
     * Parse donationMetadata JSON string from EventDetails.
     * Priority: 1) donationMetadata field, 2) metadata.donationConfig (backward compatibility)
     *
     * @param event EventDetails entity
     * @return Map containing parsed donation metadata, or null if not found
     */
    private Map<String, Object> parseDonationMetadata(EventDetails event) {
        // Priority 1: Check donationMetadata field (separate column)
        if (event.getDonationMetadata() != null && !event.getDonationMetadata().trim().isEmpty()) {
            try {
                Map<String, Object> donationMetadata = objectMapper.readValue(event.getDonationMetadata(), Map.class);
                if (donationMetadata != null && !donationMetadata.isEmpty()) {
                    log.debug("Parsed donationMetadata from donation_metadata column for event {}", event.getId());
                    return donationMetadata;
                }
            } catch (Exception e) {
                log.warn("Failed to parse donationMetadata JSON for event {}: {}", event.getId(), e.getMessage());
            }
        }

        // Priority 2: Fallback to metadata.donationConfig (backward compatibility)
        Map<String, Object> eventMetadata = event.getMetadataAsMap();
        if (eventMetadata != null && !eventMetadata.isEmpty()) {
            @SuppressWarnings("unchecked")
            Map<String, Object> donationConfig = (Map<String, Object>) eventMetadata.get("donationConfig");
            if (donationConfig != null && !donationConfig.isEmpty()) {
                log.debug("Using donationConfig from metadata column for event {} (backward compatibility)", event.getId());
                // Build donationMetadata map from donationConfig
                Map<String, Object> donationMetadata = new HashMap<>();
                donationMetadata.put("isFundraiserEvent", eventMetadata.get("isFundraiserEvent"));
                donationMetadata.put("isCharityEvent", eventMetadata.get("isCharityEvent"));
                donationMetadata.put("zeroFeeProvider", donationConfig.get("zeroFeeProvider"));
                donationMetadata.put("givebutterCampaignId", donationConfig.get("givebutterCampaignId"));
                return donationMetadata;
            }
        }

        return null;
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
