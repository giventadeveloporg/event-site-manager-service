package com.nextjstemplate.service.payment;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextjstemplate.domain.DiscountCode;
import com.nextjstemplate.domain.EventTicketTransaction;
import com.nextjstemplate.domain.EventTicketTransactionItem;
import com.nextjstemplate.domain.EventTicketType;
import com.nextjstemplate.domain.UserPaymentTransaction;
import com.nextjstemplate.repository.DiscountCodeRepository;
import com.nextjstemplate.repository.EventTicketTransactionItemRepository;
import com.nextjstemplate.repository.EventTicketTransactionRepository;
import com.nextjstemplate.repository.EventTicketTypeRepository;
import com.nextjstemplate.security.TenantContext;
import com.nextjstemplate.service.QRCodeService;
import com.nextjstemplate.service.payment.adapter.StripePaymentAdapter;
import com.nextjstemplate.service.payment.event.PaymentSuccessEvent;
import com.nextjstemplate.web.rest.QRCodeResource;
import com.stripe.model.PaymentIntent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service that handles automatic ticket generation, QR code creation, and email sending
 * when a payment succeeds for ticket purchases.
 */
@Service
public class TicketGenerationService {

    private static final Logger log = LoggerFactory.getLogger(TicketGenerationService.class);

    private final EventTicketTransactionRepository eventTicketTransactionRepository;
    private final EventTicketTransactionItemRepository eventTicketTransactionItemRepository;
    private final EventTicketTypeRepository eventTicketTypeRepository;
    private final DiscountCodeRepository discountCodeRepository;
    private final QRCodeService qrCodeService;
    private final QRCodeResource qrCodeResource;
    private final Environment environment;
    private final ObjectMapper objectMapper;
    private final StripePaymentAdapter stripePaymentAdapter;

    @Value("${email.host.url-prefix:${NEXT_PUBLIC_APP_URL:${EMAIL_HOST_URL_PREFIX:http://localhost:3000}}}")
    private String emailHostUrlPrefix;

    @Autowired
    public TicketGenerationService(
        EventTicketTransactionRepository eventTicketTransactionRepository,
        EventTicketTransactionItemRepository eventTicketTransactionItemRepository,
        EventTicketTypeRepository eventTicketTypeRepository,
        DiscountCodeRepository discountCodeRepository,
        QRCodeService qrCodeService,
        QRCodeResource qrCodeResource,
        Environment environment,
        @Lazy StripePaymentAdapter stripePaymentAdapter
    ) {
        this.eventTicketTransactionRepository = eventTicketTransactionRepository;
        this.eventTicketTransactionItemRepository = eventTicketTransactionItemRepository;
        this.eventTicketTypeRepository = eventTicketTypeRepository;
        this.discountCodeRepository = discountCodeRepository;
        this.qrCodeService = qrCodeService;
        this.qrCodeResource = qrCodeResource;
        this.environment = environment;
        this.stripePaymentAdapter = stripePaymentAdapter;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Synchronously process ticket generation for a payment transaction.
     * This method can be called directly from webhook handlers to ensure immediate ticket creation.
     *
     * CRITICAL: Uses Propagation.REQUIRES_NEW to ensure a fresh read-write transaction.
     * This is necessary because this method may be called from:
     * 1. StripePaymentAdapter.getStatus() - which is called from PaymentOrchestrationService.getStatus()
     *    with @Transactional(readOnly = true)
     * 2. Webhook handlers that need to create/update database records
     *
     * Without REQUIRES_NEW, this method would inherit the parent's read-only transaction
     * and fail with "cannot execute nextval() in a read-only transaction" error.
     *
     * @param paymentTransaction the payment transaction that succeeded
     * @param stripePaymentIntentId the Stripe payment intent ID
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processTicketGenerationSync(UserPaymentTransaction paymentTransaction, String stripePaymentIntentId) {
        log.info("Processing ticket generation synchronously for transaction: {}", paymentTransaction.getId());

        try {
            // Step 1: Check if this is a ticket purchase
            if (!isTicketPurchase(paymentTransaction)) {
                log.debug("Payment {} is not a ticket purchase, skipping ticket generation", paymentTransaction.getId());
                return;
            }

            Long eventId = paymentTransaction.getEvent() != null ? paymentTransaction.getEvent().getId() : null;
            if (eventId == null) {
                log.warn("Payment transaction {} has no eventId, cannot generate ticket", paymentTransaction.getId());
                return;
            }

            log.info(
                "Payment {} is a ticket purchase for event {}, proceeding with ticket generation",
                paymentTransaction.getId(),
                eventId
            );

            // Step 2: Find or create EventTicketTransaction
            EventTicketTransaction ticketTransaction = findOrCreateTicketTransaction(paymentTransaction, eventId, false);
            if (ticketTransaction == null || ticketTransaction.getId() == null) {
                log.error("Failed to create ticket transaction for payment {}", paymentTransaction.getId());
                return;
            }

            log.info("Ticket transaction {} created/found for payment {}", ticketTransaction.getId(), paymentTransaction.getId());

            // Step 2.5: Extract cart from Payment Intent and create transaction items
            // Use parameter if provided, otherwise fall back to payment transaction
            String effectivePaymentIntentId = stripePaymentIntentId != null && !stripePaymentIntentId.isEmpty()
                ? stripePaymentIntentId
                : paymentTransaction.getStripePaymentIntentId();
            if (effectivePaymentIntentId != null && !effectivePaymentIntentId.isEmpty()) {
                try {
                    List<CartItem> cartItems = extractCartFromPaymentIntent(effectivePaymentIntentId, paymentTransaction);
                    if (cartItems != null && !cartItems.isEmpty()) {
                        List<EventTicketTransactionItem> createdItems = createTransactionItems(
                            ticketTransaction,
                            cartItems,
                            paymentTransaction.getTenantId()
                        );

                        // Update transaction quantity to sum of all cart items
                        int totalQuantity = cartItems.stream().mapToInt(CartItem::getQuantity).sum();
                        ticketTransaction.setQuantity(totalQuantity);

                        // Calculate totalAmount from transaction items
                        BigDecimal totalAmount = createdItems
                            .stream()
                            .map(EventTicketTransactionItem::getTotalAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                        ticketTransaction.setTotalAmount(totalAmount);

                        // Extract and apply discount code if present
                        try {
                            PaymentIntent paymentIntent = stripePaymentAdapter.retrievePaymentIntent(
                                effectivePaymentIntentId,
                                paymentTransaction.getTenantId()
                            );
                            applyDiscountToTransaction(ticketTransaction, paymentIntent, paymentTransaction, totalAmount);
                        } catch (Exception e) {
                            log.warn("Failed to apply discount to transaction {}: {}", ticketTransaction.getId(), e.getMessage());
                            // Continue without discount - payment already succeeded
                        }

                        // CRITICAL: Set status to COMPLETED AFTER items are inserted
                        // Since items were inserted when status was PENDING, trigger didn't update sold_quantity.
                        // We need to manually update sold_quantity now.
                        String previousStatus = ticketTransaction.getStatus();
                        ticketTransaction.setStatus("COMPLETED");
                        ticketTransaction.setUpdatedAt(ZonedDateTime.now());
                        eventTicketTransactionRepository.save(ticketTransaction);

                        // Update sold_quantity manually since items were inserted when status was PENDING
                        if (!"COMPLETED".equals(previousStatus) && !createdItems.isEmpty()) {
                            updateTicketTypeQuantities(createdItems, ticketTransaction.getEventId());
                        }

                        log.info(
                            "Successfully created {} transaction items for transaction {}",
                            cartItems.size(),
                            ticketTransaction.getId()
                        );
                    } else {
                        log.warn("No cart items found for Payment Intent {}, transaction items not created", effectivePaymentIntentId);
                        // Set status to COMPLETED even if no items (fallback case)
                        ticketTransaction.setStatus("COMPLETED");
                        ticketTransaction.setUpdatedAt(ZonedDateTime.now());
                        eventTicketTransactionRepository.save(ticketTransaction);
                    }
                } catch (Exception e) {
                    log.error("Failed to create transaction items for transaction {}: {}", ticketTransaction.getId(), e.getMessage(), e);
                    // Set status to COMPLETED even if item creation fails (payment already succeeded)
                    ticketTransaction.setStatus("COMPLETED");
                    ticketTransaction.setUpdatedAt(ZonedDateTime.now());
                    eventTicketTransactionRepository.save(ticketTransaction);
                }
            } else {
                log.warn("Payment transaction {} has no Stripe payment intent ID, cannot extract cart", paymentTransaction.getId());
                // Set status to COMPLETED even if no payment intent ID
                ticketTransaction.setStatus("COMPLETED");
                ticketTransaction.setUpdatedAt(ZonedDateTime.now());
                eventTicketTransactionRepository.save(ticketTransaction);
            }

            // Step 3: Generate QR code if not already generated
            if (ticketTransaction.getQrCodeImageUrl() == null || ticketTransaction.getQrCodeImageUrl().isEmpty()) {
                try {
                    generateQrCodeForTicket(ticketTransaction, eventId);
                    log.info("QR code generated for ticket transaction {}", ticketTransaction.getId());
                } catch (Exception e) {
                    log.error("Failed to generate QR code for ticket transaction {}: {}", ticketTransaction.getId(), e.getMessage(), e);
                    // Continue even if QR generation fails - email can still be sent
                }
            } else {
                log.debug("QR code already exists for ticket transaction {}", ticketTransaction.getId());
            }

            // Step 4: Send ticket email if email is available
            String email = extractEmailFromPayment(paymentTransaction);
            if (email != null && !email.isEmpty()) {
                try {
                    sendTicketEmail(ticketTransaction, eventId, email);
                    log.info("Ticket email sent for transaction {} to {}", ticketTransaction.getId(), email);
                } catch (Exception e) {
                    log.error("Failed to send ticket email for transaction {}: {}", ticketTransaction.getId(), e.getMessage(), e);
                    // Log error but don't fail - payment is still successful
                }
            } else {
                log.warn("No email found for payment transaction {}, cannot send ticket email", paymentTransaction.getId());
            }

            log.info("Successfully processed ticket generation for payment transaction {}", paymentTransaction.getId());
        } catch (Exception e) {
            log.error(
                "Unexpected error processing ticket generation for payment transaction {}: {}",
                paymentTransaction.getId(),
                e.getMessage(),
                e
            );
            // Don't throw exception - payment is still successful, ticket generation can be retried manually
        }
    }

    /**
     * Listen for payment success events and automatically generate tickets, QR codes, and send emails
     * for ticket purchases.
     */
    @EventListener
    @Async
    @Transactional
    public void handlePaymentSuccess(PaymentSuccessEvent event) {
        UserPaymentTransaction paymentTransaction = event.getPaymentTransaction();
        String stripePaymentIntentId = event.getStripePaymentIntentId();
        String eventTenantId = event.getTenantId();

        // CRITICAL: Set TenantContext from the event because @Async methods run in separate threads
        // where thread-local TenantContext is NOT propagated from the original request thread.
        // Without this, the default tenant would be used, causing cross-tenant duplicates.
        if (eventTenantId != null && !eventTenantId.isEmpty()) {
            TenantContext.setCurrentTenant(eventTenantId);
            log.info("Set TenantContext to '{}' for async payment success event processing", eventTenantId);
        } else {
            log.warn("PaymentSuccessEvent missing tenantId! Using paymentTransaction.getTenantId(): {}", paymentTransaction.getTenantId());
            if (paymentTransaction.getTenantId() != null) {
                TenantContext.setCurrentTenant(paymentTransaction.getTenantId());
            }
        }

        log.info(
            "Processing payment success event for transaction: {} with tenant: {}",
            paymentTransaction.getId(),
            TenantContext.getCurrentTenant()
        );

        try {
            // Step 1: Check if this is a ticket purchase
            if (!isTicketPurchase(paymentTransaction)) {
                log.debug("Payment {} is not a ticket purchase, skipping ticket generation", paymentTransaction.getId());
                return;
            }

            Long eventId = paymentTransaction.getEvent() != null ? paymentTransaction.getEvent().getId() : null;
            if (eventId == null) {
                log.warn("Payment transaction {} has no eventId, cannot generate ticket", paymentTransaction.getId());
                return;
            }

            log.info(
                "Payment {} is a ticket purchase for event {}, proceeding with ticket generation",
                paymentTransaction.getId(),
                eventId
            );

            // Step 2: Find or create EventTicketTransaction
            // CRITICAL: Create with PENDING status first to avoid trigger inventory check during item insertion
            EventTicketTransaction ticketTransaction = findOrCreateTicketTransaction(paymentTransaction, eventId, false);
            if (ticketTransaction == null || ticketTransaction.getId() == null) {
                log.error("Failed to create ticket transaction for payment {}", paymentTransaction.getId());
                return;
            }

            log.info("Ticket transaction {} created/found for payment {}", ticketTransaction.getId(), paymentTransaction.getId());

            // Step 2.5: Extract cart from Payment Intent and create transaction items
            if (stripePaymentIntentId != null && !stripePaymentIntentId.isEmpty()) {
                try {
                    List<CartItem> cartItems = extractCartFromPaymentIntent(stripePaymentIntentId, paymentTransaction);
                    if (cartItems != null && !cartItems.isEmpty()) {
                        List<EventTicketTransactionItem> createdItems = createTransactionItems(
                            ticketTransaction,
                            cartItems,
                            paymentTransaction.getTenantId()
                        );

                        // Update transaction quantity to sum of all cart items
                        int totalQuantity = cartItems.stream().mapToInt(CartItem::getQuantity).sum();
                        ticketTransaction.setQuantity(totalQuantity);

                        // Calculate totalAmount from transaction items
                        BigDecimal totalAmount = createdItems
                            .stream()
                            .map(EventTicketTransactionItem::getTotalAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                        ticketTransaction.setTotalAmount(totalAmount);

                        // Extract and apply discount code if present
                        try {
                            PaymentIntent paymentIntent = stripePaymentAdapter.retrievePaymentIntent(
                                stripePaymentIntentId,
                                paymentTransaction.getTenantId()
                            );
                            applyDiscountToTransaction(ticketTransaction, paymentIntent, paymentTransaction, totalAmount);
                        } catch (Exception e) {
                            log.warn("Failed to apply discount to transaction {}: {}", ticketTransaction.getId(), e.getMessage());
                            // Continue without discount - payment already succeeded
                        }

                        // CRITICAL: Set status to COMPLETED AFTER items are inserted
                        // Since items were inserted when status was PENDING, trigger didn't update sold_quantity.
                        // We need to manually update sold_quantity now.
                        String previousStatus = ticketTransaction.getStatus();
                        ticketTransaction.setStatus("COMPLETED");
                        ticketTransaction.setUpdatedAt(ZonedDateTime.now());
                        eventTicketTransactionRepository.save(ticketTransaction);

                        // Update sold_quantity manually since items were inserted when status was PENDING
                        if (!"COMPLETED".equals(previousStatus) && !createdItems.isEmpty()) {
                            updateTicketTypeQuantities(createdItems, ticketTransaction.getEventId());
                        }

                        log.info(
                            "Successfully created {} transaction items for transaction {}",
                            cartItems.size(),
                            ticketTransaction.getId()
                        );
                    } else {
                        log.warn("No cart items found for Payment Intent {}, transaction items not created", stripePaymentIntentId);
                        // Set status to COMPLETED even if no items (fallback case)
                        ticketTransaction.setStatus("COMPLETED");
                        ticketTransaction.setUpdatedAt(ZonedDateTime.now());
                        eventTicketTransactionRepository.save(ticketTransaction);
                    }
                } catch (Exception e) {
                    log.error("Failed to create transaction items for transaction {}: {}", ticketTransaction.getId(), e.getMessage(), e);
                    // Set status to COMPLETED even if item creation fails (payment already succeeded)
                    ticketTransaction.setStatus("COMPLETED");
                    ticketTransaction.setUpdatedAt(ZonedDateTime.now());
                    eventTicketTransactionRepository.save(ticketTransaction);
                }
            } else {
                log.warn("Payment transaction {} has no Stripe payment intent ID, cannot extract cart", paymentTransaction.getId());
                // Set status to COMPLETED even if no payment intent ID
                ticketTransaction.setStatus("COMPLETED");
                ticketTransaction.setUpdatedAt(ZonedDateTime.now());
                eventTicketTransactionRepository.save(ticketTransaction);
            }

            // Step 3: Generate QR code if not already generated
            if (ticketTransaction.getQrCodeImageUrl() == null || ticketTransaction.getQrCodeImageUrl().isEmpty()) {
                try {
                    generateQrCodeForTicket(ticketTransaction, eventId);
                    log.info("QR code generated for ticket transaction {}", ticketTransaction.getId());
                } catch (Exception e) {
                    log.error("Failed to generate QR code for ticket transaction {}: {}", ticketTransaction.getId(), e.getMessage(), e);
                    // Continue even if QR generation fails - email can still be sent
                }
            } else {
                log.debug("QR code already exists for ticket transaction {}", ticketTransaction.getId());
            }

            // Step 4: Send ticket email if email is available
            String email = extractEmailFromPayment(paymentTransaction);
            if (email != null && !email.isEmpty()) {
                try {
                    sendTicketEmail(ticketTransaction, eventId, email);
                    log.info("Ticket email sent for transaction {} to {}", ticketTransaction.getId(), email);
                } catch (Exception e) {
                    log.error("Failed to send ticket email for transaction {}: {}", ticketTransaction.getId(), e.getMessage(), e);
                    // Log error but don't fail - payment is still successful
                }
            } else {
                log.warn("No email found for payment transaction {}, cannot send ticket email", paymentTransaction.getId());
            }

            log.info("Successfully processed ticket generation for payment transaction {}", paymentTransaction.getId());
        } catch (Exception e) {
            log.error(
                "Unexpected error processing ticket generation for payment transaction {}: {}",
                paymentTransaction.getId(),
                e.getMessage(),
                e
            );
            // Don't throw exception - payment is still successful, ticket generation can be retried manually
        } finally {
            // CRITICAL: Always clear TenantContext after async processing
            // This prevents tenant context leaking to other threads in the pool
            TenantContext.clear();
            log.debug("Cleared TenantContext after async payment success processing");
        }
    }

    /**
     * Check if the payment transaction is for ticket purchase.
     */
    private boolean isTicketPurchase(UserPaymentTransaction paymentTransaction) {
        // Check transaction type
        String transactionType = paymentTransaction.getTransactionType();
        if (transactionType != null && transactionType.equals("TICKET_SALE")) {
            return true;
        }

        // Check if eventId is present (indicates ticket purchase)
        if (paymentTransaction.getEvent() != null && paymentTransaction.getEvent().getId() != null) {
            return true;
        }

        return false;
    }

    /**
     * Find existing ticket transaction or create a new one from payment transaction.
     *
     * CRITICAL: This method implements GLOBAL duplicate prevention.
     * Only ONE ticket transaction can exist per stripe_payment_intent_id, regardless of tenant.
     * If a transaction exists for a different tenant, we return that existing transaction
     * (after logging a warning) instead of creating a duplicate.
     *
     * @param paymentTransaction Payment transaction
     * @param eventId Event ID
     * @param setCompletedStatus If true, set status to COMPLETED immediately. If false, set to PENDING (for item insertion first).
     */
    private EventTicketTransaction findOrCreateTicketTransaction(
        UserPaymentTransaction paymentTransaction,
        Long eventId,
        boolean setCompletedStatus
    ) {
        String stripePaymentIntentId = paymentTransaction.getStripePaymentIntentId();
        String tenantId = paymentTransaction.getTenantId();

        // CRITICAL: First check if ANY ticket transaction exists for this payment intent (GLOBAL check)
        // This prevents the same payment from creating multiple tickets, even across tenants
        if (stripePaymentIntentId != null && !stripePaymentIntentId.isEmpty()) {
            // Find ANY existing transaction for this payment intent (regardless of tenant)
            // Use map/orElse pattern to avoid isPresent()+get() which modernizer flags
            EventTicketTransaction existingTransaction = eventTicketTransactionRepository
                .findByStripePaymentIntentId(stripePaymentIntentId)
                .map(existing -> {
                    if (tenantId.equals(existing.getTenantId())) {
                        // Same tenant - update and return
                        log.info(
                            "Found existing ticket transaction {} for payment intent {} and tenant {}",
                            existing.getId(),
                            stripePaymentIntentId,
                            tenantId
                        );
                        return updateExistingTransaction(existing, paymentTransaction, setCompletedStatus);
                    } else {
                        // DIFFERENT tenant - this means the first request used wrong tenant (likely default)
                        // Log warning but RETURN the existing transaction instead of creating duplicate
                        log.warn(
                            "DUPLICATE PREVENTION: Ticket transaction {} already exists for payment intent {} " +
                            "with tenant '{}', but current request has tenant '{}'. " +
                            "Returning existing transaction to prevent duplicate. " +
                            "This may indicate a tenant context issue in the first request.",
                            existing.getId(),
                            stripePaymentIntentId,
                            existing.getTenantId(),
                            tenantId
                        );

                        // Return the existing transaction - caller should handle this appropriately
                        // We return it instead of null so QR code generation and email can still work
                        return existing;
                    }
                })
                .orElse(null);

            if (existingTransaction != null) {
                return existingTransaction;
            }
        }

        // No existing transaction found - create a new one
        log.info(
            "No existing ticket transaction found for payment intent {}, creating new one for tenant {}",
            stripePaymentIntentId,
            tenantId
        );

        try {
            return createNewTicketTransaction(paymentTransaction, eventId, setCompletedStatus);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            // CRITICAL: Handle unique constraint violation (race condition)
            // Another thread created a record between our check and insert
            // This is expected in high-concurrency scenarios
            log.warn(
                "Unique constraint violation when creating ticket for payment intent {} - " +
                "another thread likely created it. Fetching existing record.",
                stripePaymentIntentId
            );

            // Fetch and return the existing record that was created by the other thread
            return eventTicketTransactionRepository
                .findByStripePaymentIntentId(stripePaymentIntentId)
                .map(existing -> {
                    log.info(
                        "Retrieved existing ticket transaction {} created by concurrent thread for payment intent {}",
                        existing.getId(),
                        stripePaymentIntentId
                    );
                    return updateExistingTransaction(existing, paymentTransaction, setCompletedStatus);
                })
                .orElseThrow(() -> {
                    // This should never happen - constraint violation implies record exists
                    log.error(
                        "CRITICAL: Unique constraint violated but no record found for payment intent {}. " +
                        "This indicates a serious data integrity issue.",
                        stripePaymentIntentId
                    );
                    return new IllegalStateException(
                        "Unique constraint violated but no record found for payment intent: " + stripePaymentIntentId
                    );
                });
        }
    }

    /**
     * Create a new ticket transaction from payment transaction.
     */
    private EventTicketTransaction createNewTicketTransaction(
        UserPaymentTransaction paymentTransaction,
        Long eventId,
        boolean setCompletedStatus
    ) {
        // Create new ticket transaction
        log.info("Creating new ticket transaction for payment {}", paymentTransaction.getId());

        EventTicketTransaction ticketTransaction = new EventTicketTransaction();
        ticketTransaction.setTenantId(paymentTransaction.getTenantId());
        ticketTransaction.setEventId(eventId);
        ticketTransaction.setEmail(extractEmailFromPayment(paymentTransaction));
        ticketTransaction.setFirstName(extractFirstNameFromPayment(paymentTransaction));
        ticketTransaction.setLastName(extractLastNameFromPayment(paymentTransaction));
        ticketTransaction.setPhone(extractPhoneFromPayment(paymentTransaction));

        // Set amount fields
        ticketTransaction.setPricePerUnit(paymentTransaction.getAmount());
        ticketTransaction.setTotalAmount(paymentTransaction.getAmount());
        ticketTransaction.setFinalAmount(paymentTransaction.getAmount());
        ticketTransaction.setQuantity(1); // Default to 1, can be updated from metadata

        // Set status: PENDING initially to allow item insertion without inventory check,
        // then will be set to COMPLETED after items are inserted
        ticketTransaction.setStatus(setCompletedStatus ? "COMPLETED" : "PENDING");

        // Set payment method - try from paymentTransaction first, then Payment Intent as fallback
        String paymentMethod = paymentTransaction.getPaymentMethod();
        if (paymentMethod == null || paymentMethod.isEmpty()) {
            paymentMethod = extractPaymentMethodFromPaymentIntent(paymentTransaction);
        }
        ticketTransaction.setPaymentMethod(paymentMethod);

        ticketTransaction.setPaymentReference(paymentTransaction.getExternalTransactionId());
        ticketTransaction.setStripePaymentIntentId(paymentTransaction.getStripePaymentIntentId());
        ticketTransaction.setStripePaymentStatus("succeeded");
        ticketTransaction.setStripePaymentCurrency(paymentTransaction.getCurrency());

        // Set dates
        ticketTransaction.setPurchaseDate(paymentTransaction.getCreatedAt());
        ticketTransaction.setCreatedAt(ZonedDateTime.now());
        ticketTransaction.setUpdatedAt(ZonedDateTime.now());

        return eventTicketTransactionRepository.save(ticketTransaction);
    }

    /**
     * Update an existing ticket transaction with payment information.
     *
     * @param existing The existing ticket transaction
     * @param paymentTransaction The payment transaction with updated info
     * @param setCompletedStatus If true, set status to COMPLETED
     * @return The updated ticket transaction
     */
    private EventTicketTransaction updateExistingTransaction(
        EventTicketTransaction existing,
        UserPaymentTransaction paymentTransaction,
        boolean setCompletedStatus
    ) {
        log.info("Found existing ticket transaction {} for payment {}", existing.getId(), paymentTransaction.getId());

        // Update payment method if not already set
        if (existing.getPaymentMethod() == null || existing.getPaymentMethod().isEmpty()) {
            String paymentMethod = paymentTransaction.getPaymentMethod();
            if (paymentMethod == null || paymentMethod.isEmpty()) {
                // Try to extract from Payment Intent as fallback
                paymentMethod = extractPaymentMethodFromPaymentIntent(paymentTransaction);
            }
            if (paymentMethod != null && !paymentMethod.isEmpty()) {
                existing.setPaymentMethod(paymentMethod);
                existing.setUpdatedAt(ZonedDateTime.now());
                log.info("Updated payment method for transaction {}: {}", existing.getId(), paymentMethod);
            }
        }

        // Update status to COMPLETED if requested and not already
        if (setCompletedStatus && !"COMPLETED".equals(existing.getStatus())) {
            existing.setStatus("COMPLETED");
            existing.setUpdatedAt(ZonedDateTime.now());
        }

        if (existing.getUpdatedAt() != null) {
            eventTicketTransactionRepository.save(existing);
        }

        return existing;
    }

    /**
     * Generate QR code for ticket transaction.
     */
    private void generateQrCodeForTicket(EventTicketTransaction ticketTransaction, Long eventId) throws Exception {
        log.info("Enter: generateQrCodeForTicket() for transactionId={}, eventId={}", ticketTransaction.getId(), eventId);

        // CRITICAL: Log emailHostUrlPrefix to diagnose production issues
        log.info("emailHostUrlPrefix configuration value: '{}'", emailHostUrlPrefix);
        if (emailHostUrlPrefix == null || emailHostUrlPrefix.isEmpty()) {
            log.error(
                "CRITICAL: emailHostUrlPrefix is NULL or EMPTY! QR code generation will fail. Check app.email-host-url-prefix or EMAIL_HOST_URL_PREFIX environment variable."
            );
            throw new IllegalStateException(
                "emailHostUrlPrefix is not configured. Set app.email-host-url-prefix or EMAIL_HOST_URL_PREFIX environment variable."
            );
        }

        String qrScanUrlContent =
            emailHostUrlPrefix + "/qrcode-scan/tickets" + "/events/" + eventId + "/transactions/" + ticketTransaction.getId();

        log.info("QR scan URL content: {}", qrScanUrlContent);

        String qrCodeImageUrl = qrCodeService.generateAndUploadQRCode(
            qrScanUrlContent,
            eventId,
            String.valueOf(ticketTransaction.getId()),
            ticketTransaction.getTenantId()
        );

        if (qrCodeImageUrl == null || qrCodeImageUrl.isEmpty()) {
            log.error("CRITICAL: QR code generation returned NULL or EMPTY URL for transactionId={}", ticketTransaction.getId());
            throw new IllegalStateException("QR code generation returned empty URL");
        }

        log.info("QR code image URL generated: {}", qrCodeImageUrl);
        ticketTransaction.setQrCodeImageUrl(qrCodeImageUrl);
        ticketTransaction.setUpdatedAt(ZonedDateTime.now());
        eventTicketTransactionRepository.save(ticketTransaction);

        log.info("QR code generated and saved for ticket transaction {}: {}", ticketTransaction.getId(), qrCodeImageUrl);
    }

    /**
     * Send ticket email for the transaction.
     * Stores email sent status by setting confirmationSentAt timestamp.
     * The populateTicketData() method in StripePaymentAdapter checks this field to determine emailSent status.
     */
    private void sendTicketEmail(EventTicketTransaction ticketTransaction, Long eventId, String email) {
        try {
            // Encode email host URL prefix (if needed by QRCodeResource)
            String encodedEmailHostUrlPrefix = encodeEmailHostUrlPrefix(emailHostUrlPrefix);

            // Call the email sending method from QRCodeResource
            qrCodeResource.sendTicketEmail(eventId, ticketTransaction.getId(), email, encodedEmailHostUrlPrefix);

            // CRITICAL: Update confirmation sent timestamp to indicate email was sent successfully
            // The populateTicketData() method in StripePaymentAdapter checks confirmationSentAt != null
            // to determine emailSent status in payment status response
            ticketTransaction.setConfirmationSentAt(ZonedDateTime.now());
            ticketTransaction.setUpdatedAt(ZonedDateTime.now());
            eventTicketTransactionRepository.save(ticketTransaction);

            log.info("Ticket email sent successfully for transaction {} to {}", ticketTransaction.getId(), email);
        } catch (Exception e) {
            log.error("Error sending ticket email for transaction {}: {}", ticketTransaction.getId(), e.getMessage(), e);
            // Don't throw exception - payment is still successful, email can be retried manually
            // Note: confirmationSentAt will remain null, indicating email was not sent
            throw e;
        }
    }

    /**
     * Extract email from payment transaction metadata or Stripe customer email.
     */
    private String extractEmailFromPayment(UserPaymentTransaction paymentTransaction) {
        // Try to extract from metadata JSON
        if (paymentTransaction.getMetadata() != null && !paymentTransaction.getMetadata().isEmpty()) {
            try {
                Map<String, Object> metadata = objectMapper.readValue(paymentTransaction.getMetadata(), Map.class);
                Object email = metadata.get("email");
                if (email != null && !email.toString().isEmpty()) {
                    log.debug("Extracted email from payment transaction metadata: {}", email);
                    return email.toString();
                }

                // Also check for customerEmail in metadata
                Object customerEmail = metadata.get("customerEmail");
                if (customerEmail != null && !customerEmail.toString().isEmpty()) {
                    log.debug("Extracted customerEmail from payment transaction metadata: {}", customerEmail);
                    return customerEmail.toString();
                }
            } catch (Exception e) {
                log.debug("Failed to parse metadata JSON: {}", e.getMessage());
            }
        }

        // Try to get email from Stripe customer email field (if stored in transaction)
        // Note: This might not be available if transaction was created before payment completed
        // The webhook handler should extract email from PaymentIntent and store it in metadata

        log.warn("No email found in payment transaction {} metadata", paymentTransaction.getId());
        return null;
    }

    /**
     * Extract first name from payment transaction metadata or Payment Intent metadata.
     * Checks both sources: payment transaction metadata and Payment Intent metadata.
     */
    private String extractFirstNameFromPayment(UserPaymentTransaction paymentTransaction) {
        // First check payment transaction metadata
        if (paymentTransaction.getMetadata() != null && !paymentTransaction.getMetadata().isEmpty()) {
            try {
                Map<String, Object> metadata = objectMapper.readValue(paymentTransaction.getMetadata(), Map.class);
                Object firstName = metadata.get("firstName");
                if (firstName != null) {
                    return firstName.toString();
                }
            } catch (Exception e) {
                log.debug("Failed to parse metadata JSON: {}", e.getMessage());
            }
        }

        // Also check Payment Intent metadata if available
        String stripePaymentIntentId = paymentTransaction.getStripePaymentIntentId();
        if (stripePaymentIntentId != null && !stripePaymentIntentId.isEmpty()) {
            try {
                com.stripe.model.PaymentIntent paymentIntent = stripePaymentAdapter.retrievePaymentIntent(
                    stripePaymentIntentId,
                    paymentTransaction.getTenantId()
                );
                if (paymentIntent != null && paymentIntent.getMetadata() != null) {
                    String customerName = paymentIntent.getMetadata().get("customerName");
                    if (customerName != null && !customerName.isEmpty()) {
                        // Parse customerName to extract first name (assumes format "FirstName LastName" or just "FirstName")
                        String[] nameParts = customerName.trim().split("\\s+", 2);
                        if (nameParts.length > 0) {
                            log.debug("Extracted firstName from Payment Intent customerName: {}", nameParts[0]);
                            return nameParts[0];
                        }
                    }
                }
            } catch (Exception e) {
                log.debug("Failed to extract firstName from Payment Intent metadata: {}", e.getMessage());
            }
        }

        return null;
    }

    /**
     * Extract last name from payment transaction metadata or Payment Intent metadata.
     * Checks both sources: payment transaction metadata and Payment Intent metadata.
     */
    private String extractLastNameFromPayment(UserPaymentTransaction paymentTransaction) {
        // First check payment transaction metadata
        if (paymentTransaction.getMetadata() != null && !paymentTransaction.getMetadata().isEmpty()) {
            try {
                Map<String, Object> metadata = objectMapper.readValue(paymentTransaction.getMetadata(), Map.class);
                Object lastName = metadata.get("lastName");
                if (lastName != null) {
                    return lastName.toString();
                }
            } catch (Exception e) {
                log.debug("Failed to parse metadata JSON: {}", e.getMessage());
            }
        }

        // Also check Payment Intent metadata if available
        String stripePaymentIntentId = paymentTransaction.getStripePaymentIntentId();
        if (stripePaymentIntentId != null && !stripePaymentIntentId.isEmpty()) {
            try {
                com.stripe.model.PaymentIntent paymentIntent = stripePaymentAdapter.retrievePaymentIntent(
                    stripePaymentIntentId,
                    paymentTransaction.getTenantId()
                );
                if (paymentIntent != null && paymentIntent.getMetadata() != null) {
                    String customerName = paymentIntent.getMetadata().get("customerName");
                    if (customerName != null && !customerName.isEmpty()) {
                        // Parse customerName to extract last name (assumes format "FirstName LastName")
                        String[] nameParts = customerName.trim().split("\\s+", 2);
                        if (nameParts.length > 1) {
                            log.debug("Extracted lastName from Payment Intent customerName: {}", nameParts[1]);
                            return nameParts[1];
                        }
                    }
                }
            } catch (Exception e) {
                log.debug("Failed to extract lastName from Payment Intent metadata: {}", e.getMessage());
            }
        }

        return null;
    }

    /**
     * Extract phone from payment transaction metadata or Payment Intent metadata.
     * Checks both sources: payment transaction metadata and Payment Intent metadata.
     */
    private String extractPhoneFromPayment(UserPaymentTransaction paymentTransaction) {
        // First check payment transaction metadata
        if (paymentTransaction.getMetadata() != null && !paymentTransaction.getMetadata().isEmpty()) {
            try {
                Map<String, Object> metadata = objectMapper.readValue(paymentTransaction.getMetadata(), Map.class);
                Object phone = metadata.get("phone");
                if (phone != null) {
                    return phone.toString();
                }
            } catch (Exception e) {
                log.debug("Failed to parse metadata JSON: {}", e.getMessage());
            }
        }

        // Also check Payment Intent metadata if available
        String stripePaymentIntentId = paymentTransaction.getStripePaymentIntentId();
        if (stripePaymentIntentId != null && !stripePaymentIntentId.isEmpty()) {
            try {
                com.stripe.model.PaymentIntent paymentIntent = stripePaymentAdapter.retrievePaymentIntent(
                    stripePaymentIntentId,
                    paymentTransaction.getTenantId()
                );
                if (paymentIntent != null && paymentIntent.getMetadata() != null) {
                    String customerPhone = paymentIntent.getMetadata().get("customerPhone");
                    if (customerPhone != null && !customerPhone.isEmpty()) {
                        log.debug("Extracted phone from Payment Intent metadata: {}", customerPhone);
                        return customerPhone;
                    }
                }
            } catch (Exception e) {
                log.debug("Failed to extract phone from Payment Intent metadata: {}", e.getMessage());
            }
        }

        return null;
    }

    /**
     * Encode email host URL prefix using Base64 encoding (as required by QRCodeResource).
     */
    private String encodeEmailHostUrlPrefix(String urlPrefix) {
        try {
            return Base64.getEncoder().encodeToString(urlPrefix.getBytes());
        } catch (Exception e) {
            log.warn("Failed to encode email host URL prefix, using original: {}", e.getMessage());
            return urlPrefix;
        }
    }

    /**
     * Extract cart items from Payment Intent metadata.
     *
     * @param stripePaymentIntentId Payment Intent ID
     * @param paymentTransaction Payment transaction (for tenant ID)
     * @return List of cart items, or null if not available
     */
    private List<CartItem> extractCartFromPaymentIntent(String stripePaymentIntentId, UserPaymentTransaction paymentTransaction) {
        try {
            // Retrieve Payment Intent from Stripe
            PaymentIntent paymentIntent = stripePaymentAdapter.retrievePaymentIntent(
                stripePaymentIntentId,
                paymentTransaction.getTenantId()
            );

            if (paymentIntent == null) {
                log.warn("Payment Intent {} not found in Stripe", stripePaymentIntentId);
                return null;
            }

            // Extract cart from metadata
            Map<String, String> metadata = paymentIntent.getMetadata();
            if (metadata == null || metadata.isEmpty()) {
                log.warn("Payment Intent {} has no metadata", stripePaymentIntentId);
                return null;
            }

            String cartJson = metadata.get("cart");
            if (cartJson == null || cartJson.isEmpty()) {
                log.warn("Payment Intent {} metadata has no 'cart' field", stripePaymentIntentId);
                return null;
            }

            // Parse cart JSON
            List<Map<String, Object>> cartArray = objectMapper.readValue(cartJson, new TypeReference<List<Map<String, Object>>>() {});

            // Convert to CartItem objects
            List<CartItem> cartItems = new ArrayList<>();
            for (Map<String, Object> item : cartArray) {
                CartItem cartItem = new CartItem();
                cartItem.setTicketTypeId(getLongValue(item.get("ticketTypeId")));
                cartItem.setQuantity(getIntValue(item.get("quantity")));
                cartItems.add(cartItem);
            }

            log.info("Extracted {} cart items from Payment Intent {} metadata", cartItems.size(), stripePaymentIntentId);
            return cartItems;
        } catch (Exception e) {
            log.error("Error extracting cart from Payment Intent {}: {}", stripePaymentIntentId, e.getMessage(), e);
            return null;
        }
    }

    /**
     * Helper to safely extract Long value from Object
     */
    private Long getLongValue(Object value) {
        if (value == null) return null;
        if (value instanceof Long) return (Long) value;
        if (value instanceof Integer) return ((Integer) value).longValue();
        if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * Helper to safely extract Integer value from Object
     */
    private Integer getIntValue(Object value) {
        if (value == null) return null;
        if (value instanceof Integer) return (Integer) value;
        if (value instanceof Long) return ((Long) value).intValue();
        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * Create transaction items for each cart item.
     *
     * @param ticketTransaction Parent transaction
     * @param cartItems List of cart items
     * @param tenantId Tenant ID
     */
    private List<EventTicketTransactionItem> createTransactionItems(
        EventTicketTransaction ticketTransaction,
        List<CartItem> cartItems,
        String tenantId
    ) {
        List<EventTicketTransactionItem> itemsToSave = new ArrayList<>();
        ZonedDateTime now = ZonedDateTime.now();

        for (CartItem cartItem : cartItems) {
            if (cartItem.getTicketTypeId() == null || cartItem.getQuantity() == null || cartItem.getQuantity() <= 0) {
                log.warn("Skipping invalid cart item: {}", cartItem);
                continue;
            }

            // Fetch ticket type to get current price
            eventTicketTypeRepository
                .findById(cartItem.getTicketTypeId())
                .ifPresentOrElse(
                    ticketType -> {
                        // Create transaction item
                        EventTicketTransactionItem item = new EventTicketTransactionItem();
                        item.setTenantId(tenantId);
                        item.setTransactionId(ticketTransaction.getId());
                        item.setTicketTypeId(cartItem.getTicketTypeId());
                        item.setQuantity(cartItem.getQuantity());
                        item.setPricePerUnit(ticketType.getPrice());
                        item.setTotalAmount(ticketType.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
                        item.setCreatedAt(now);
                        item.setUpdatedAt(now);

                        itemsToSave.add(item);

                        log.debug(
                            "Created transaction item: ticketTypeId={}, quantity={}, pricePerUnit={}, totalAmount={}",
                            cartItem.getTicketTypeId(),
                            cartItem.getQuantity(),
                            ticketType.getPrice(),
                            item.getTotalAmount()
                        );
                    },
                    () -> log.error("Ticket type {} not found, skipping cart item", cartItem.getTicketTypeId())
                );
        }

        // Bulk save transaction items
        // NOTE: If transaction status is PENDING, the trigger won't update sold_quantity.
        // We'll update sold_quantity manually when status is changed to COMPLETED.
        if (!itemsToSave.isEmpty()) {
            eventTicketTransactionItemRepository.saveAll(itemsToSave);
            log.info("Successfully created {} transaction items for transaction {}", itemsToSave.size(), ticketTransaction.getId());
            // If transaction status is COMPLETED, trigger will have updated sold_quantity automatically.
            // If status is PENDING, we'll update sold_quantity when status changes to COMPLETED.
        } else {
            log.warn("No valid transaction items to create for transaction {}", ticketTransaction.getId());
        }

        return itemsToSave;
    }

    /**
     * Update ticket type quantities (sold_quantity and remaining_quantity) based on transaction items.
     * This method recalculates sold quantity by summing quantities from COMPLETED transactions
     * and updates remaining quantity accordingly.
     *
     * @param transactionItems List of transaction items that were just created
     * @param eventId Event ID to filter transactions
     */
    private void updateTicketTypeQuantities(List<EventTicketTransactionItem> transactionItems, Long eventId) {
        try {
            // Get unique ticket type IDs from the transaction items
            java.util.Set<Long> ticketTypeIds = transactionItems
                .stream()
                .map(EventTicketTransactionItem::getTicketTypeId)
                .collect(java.util.stream.Collectors.toSet());

            // Update quantities for each ticket type
            for (Long ticketTypeId : ticketTypeIds) {
                updateTicketTypeQuantityForEvent(ticketTypeId, eventId);
            }
        } catch (Exception e) {
            log.error("Failed to update ticket type quantities: {}", e.getMessage(), e);
        }
    }

    /**
     * Update sold and remaining quantities for a specific ticket type.
     * This method calculates sold quantity by summing quantities from COMPLETED transactions
     * and updates remaining quantity as availableQuantity - soldQuantity.
     *
     * @param ticketTypeId Ticket type ID to update
     * @param eventId Event ID to filter transactions
     */
    private void updateTicketTypeQuantityForEvent(Long ticketTypeId, Long eventId) {
        try {
            eventTicketTypeRepository
                .findById(ticketTypeId)
                .ifPresent(ticketType -> {
                    // Calculate sold quantity by summing quantities from COMPLETED transactions
                    // Query all transaction items for this ticket type across all COMPLETED transactions for this event
                    Integer soldQuantity = eventTicketTransactionItemRepository
                        .findAll()
                        .stream()
                        .filter(item -> item.getTicketTypeId().equals(ticketTypeId))
                        .filter(item -> {
                            // Check if the transaction is COMPLETED and belongs to this event
                            Optional<EventTicketTransaction> transactionOpt = eventTicketTransactionRepository.findById(
                                item.getTransactionId()
                            );
                            return transactionOpt
                                .map(transaction -> "COMPLETED".equals(transaction.getStatus()) && eventId.equals(transaction.getEventId()))
                                .orElse(false);
                        })
                        .mapToInt(EventTicketTransactionItem::getQuantity)
                        .sum();

                    // Calculate remaining quantity
                    Integer availableQuantity = ticketType.getAvailableQuantity();
                    Integer remainingQuantity = availableQuantity != null ? Math.max(0, availableQuantity - soldQuantity) : null;

                    // Update the ticket type
                    ticketType.setSoldQuantity(soldQuantity);
                    ticketType.setRemainingQuantity(remainingQuantity);
                    ticketType.setUpdatedAt(ZonedDateTime.now());

                    eventTicketTypeRepository.save(ticketType);

                    log.info("Updated ticket type {} quantities: sold={}, remaining={}", ticketTypeId, soldQuantity, remainingQuantity);
                });
        } catch (Exception e) {
            log.error("Failed to update quantities for ticket type {}: {}", ticketTypeId, e.getMessage(), e);
        }
    }

    /**
     * Extract payment method from Payment Intent.
     *
     * @param paymentTransaction Payment transaction (for tenant ID and Payment Intent ID)
     * @return Payment method, or null if not found
     */
    private String extractPaymentMethodFromPaymentIntent(UserPaymentTransaction paymentTransaction) {
        String stripePaymentIntentId = paymentTransaction.getStripePaymentIntentId();
        if (stripePaymentIntentId != null && !stripePaymentIntentId.isEmpty()) {
            try {
                PaymentIntent paymentIntent = stripePaymentAdapter.retrievePaymentIntent(
                    stripePaymentIntentId,
                    paymentTransaction.getTenantId()
                );
                if (paymentIntent != null && paymentIntent.getPaymentMethod() != null) {
                    String paymentMethod = paymentIntent.getPaymentMethod().toString();
                    log.debug("Extracted payment method from Payment Intent: {}", paymentMethod);
                    return paymentMethod;
                }
            } catch (Exception e) {
                log.debug("Failed to extract payment method from Payment Intent: {}", e.getMessage());
            }
        }
        return null;
    }

    /**
     * Extract discount code ID from Payment Intent metadata or payment transaction metadata.
     *
     * @param paymentIntent Payment Intent (primary source)
     * @param paymentTransaction Payment transaction (fallback source)
     * @return Discount code ID, or null if not found
     */
    private Long extractDiscountCodeIdFromPayment(PaymentIntent paymentIntent, UserPaymentTransaction paymentTransaction) {
        // First check Payment Intent metadata (primary source)
        if (paymentIntent != null && paymentIntent.getMetadata() != null) {
            String discountCodeIdStr = paymentIntent.getMetadata().get("discountCodeId");
            if (discountCodeIdStr != null && !discountCodeIdStr.isEmpty()) {
                try {
                    return Long.parseLong(discountCodeIdStr);
                } catch (NumberFormatException e) {
                    log.warn("Invalid discountCodeId format in Payment Intent metadata: {}", discountCodeIdStr);
                }
            }
        }

        // Fallback: Check payment transaction metadata (for backward compatibility)
        if (paymentTransaction != null && paymentTransaction.getMetadata() != null && !paymentTransaction.getMetadata().isEmpty()) {
            try {
                Map<String, Object> metadata = objectMapper.readValue(paymentTransaction.getMetadata(), Map.class);
                Object discountCodeIdObj = metadata.get("discountCodeId");
                if (discountCodeIdObj != null) {
                    try {
                        if (discountCodeIdObj instanceof String) {
                            return Long.parseLong((String) discountCodeIdObj);
                        } else if (discountCodeIdObj instanceof Number) {
                            return ((Number) discountCodeIdObj).longValue();
                        }
                    } catch (Exception e) {
                        log.warn("Failed to parse discountCodeId from payment transaction metadata: {}", e.getMessage());
                    }
                }
            } catch (Exception e) {
                log.debug("Failed to parse payment transaction metadata JSON: {}", e.getMessage());
            }
        }

        return null;
    }

    /**
     * Apply discount code to transaction if present.
     *
     * @param ticketTransaction Ticket transaction to apply discount to
     * @param paymentIntent Payment Intent (for extracting discount code ID)
     * @param paymentTransaction Payment transaction (fallback source)
     * @param totalAmount Total amount before discount
     */
    private void applyDiscountToTransaction(
        EventTicketTransaction ticketTransaction,
        PaymentIntent paymentIntent,
        UserPaymentTransaction paymentTransaction,
        BigDecimal totalAmount
    ) {
        // Extract discount code ID from Payment Intent metadata
        Long discountCodeId = extractDiscountCodeIdFromPayment(paymentIntent, paymentTransaction);

        // If discount code is present, look up details and calculate discount
        BigDecimal discountAmount = BigDecimal.ZERO;
        if (discountCodeId != null) {
            try {
                Optional<DiscountCode> discountCodeOpt = discountCodeRepository.findById(discountCodeId);
                discountCodeOpt.ifPresentOrElse(
                    discountCode -> {
                        // Calculate discount amount based on type
                        BigDecimal calculatedDiscountAmount = BigDecimal.ZERO;
                        if (totalAmount != null && totalAmount.compareTo(BigDecimal.ZERO) > 0) {
                            if ("PERCENTAGE".equals(discountCode.getDiscountType())) {
                                // Percentage discount: discountAmount = totalAmount * (discountValue / 100)
                                BigDecimal discountPercent = discountCode
                                    .getDiscountValue()
                                    .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
                                calculatedDiscountAmount = totalAmount.multiply(discountPercent).setScale(2, RoundingMode.HALF_UP);
                            } else if ("FIXED_AMOUNT".equals(discountCode.getDiscountType())) {
                                // Fixed amount discount: discountAmount = min(totalAmount, discountValue)
                                calculatedDiscountAmount =
                                    discountCode.getDiscountValue().min(totalAmount).setScale(2, RoundingMode.HALF_UP);
                            }
                        }

                        // Set discount fields on transaction
                        ticketTransaction.setDiscountCodeId(discountCodeId);
                        ticketTransaction.setDiscountAmount(calculatedDiscountAmount);

                        // Update finalAmount = totalAmount - discountAmount
                        BigDecimal finalAmount = totalAmount.subtract(calculatedDiscountAmount).setScale(2, RoundingMode.HALF_UP);
                        ticketTransaction.setFinalAmount(finalAmount);

                        // Increment uses_count for the discount code
                        Integer currentUsesCount = discountCode.getUsesCount();
                        if (currentUsesCount == null) {
                            currentUsesCount = 0;
                        }
                        discountCode.setUsesCount(currentUsesCount + 1);
                        discountCode.setUpdatedAt(ZonedDateTime.now());
                        discountCodeRepository.save(discountCode);

                        log.info(
                            "Applied discount code {} to transaction {}: amount={}, type={}, finalAmount={}, uses_count={}",
                            discountCodeId,
                            ticketTransaction.getId(),
                            calculatedDiscountAmount,
                            discountCode.getDiscountType(),
                            finalAmount,
                            discountCode.getUsesCount()
                        );
                    },
                    () -> {
                        log.warn("Discount code {} not found in database for transaction {}", discountCodeId, ticketTransaction.getId());
                    }
                );
            } catch (Exception e) {
                log.error(
                    "Failed to apply discount code {} to transaction {}: {}",
                    discountCodeId,
                    ticketTransaction.getId(),
                    e.getMessage(),
                    e
                );
                // Continue without discount rather than failing transaction
            }
        }
    }

    /**
     * Internal class to represent cart item
     */
    private static class CartItem {

        private Long ticketTypeId;
        private Integer quantity;

        public Long getTicketTypeId() {
            return ticketTypeId;
        }

        public void setTicketTypeId(Long ticketTypeId) {
            this.ticketTypeId = ticketTypeId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        @Override
        public String toString() {
            return String.format("CartItem{ticketTypeId=%d, quantity=%d}", ticketTypeId, quantity);
        }
    }
}
