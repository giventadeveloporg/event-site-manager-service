package com.nextjstemplate.service.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextjstemplate.domain.EventTicketTransaction;
import com.nextjstemplate.domain.UserPaymentTransaction;
import com.nextjstemplate.repository.EventTicketTransactionRepository;
import com.nextjstemplate.service.QRCodeService;
import com.nextjstemplate.service.payment.event.PaymentSuccessEvent;
import com.nextjstemplate.web.rest.QRCodeResource;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service that handles automatic ticket generation, QR code creation, and email sending
 * when a payment succeeds for ticket purchases.
 */
@Service
public class TicketGenerationService {

    private static final Logger log = LoggerFactory.getLogger(TicketGenerationService.class);

    private final EventTicketTransactionRepository eventTicketTransactionRepository;
    private final QRCodeService qrCodeService;
    private final QRCodeResource qrCodeResource;
    private final Environment environment;
    private final ObjectMapper objectMapper;

    @Value("${email.host.url-prefix:${NEXT_PUBLIC_APP_URL:${EMAIL_HOST_URL_PREFIX:http://localhost:3000}}}")
    private String emailHostUrlPrefix;

    @Autowired
    public TicketGenerationService(
        EventTicketTransactionRepository eventTicketTransactionRepository,
        QRCodeService qrCodeService,
        QRCodeResource qrCodeResource,
        Environment environment
    ) {
        this.eventTicketTransactionRepository = eventTicketTransactionRepository;
        this.qrCodeService = qrCodeService;
        this.qrCodeResource = qrCodeResource;
        this.environment = environment;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Synchronously process ticket generation for a payment transaction.
     * This method can be called directly from webhook handlers to ensure immediate ticket creation.
     *
     * @param paymentTransaction the payment transaction that succeeded
     * @param stripePaymentIntentId the Stripe payment intent ID
     */
    @Transactional
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
            EventTicketTransaction ticketTransaction = findOrCreateTicketTransaction(paymentTransaction, eventId);
            if (ticketTransaction == null || ticketTransaction.getId() == null) {
                log.error("Failed to create ticket transaction for payment {}", paymentTransaction.getId());
                return;
            }

            log.info("Ticket transaction {} created/found for payment {}", ticketTransaction.getId(), paymentTransaction.getId());

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

        log.info("Processing payment success event for transaction: {}", paymentTransaction.getId());

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
            EventTicketTransaction ticketTransaction = findOrCreateTicketTransaction(paymentTransaction, eventId);
            if (ticketTransaction == null || ticketTransaction.getId() == null) {
                log.error("Failed to create ticket transaction for payment {}", paymentTransaction.getId());
                return;
            }

            log.info("Ticket transaction {} created/found for payment {}", ticketTransaction.getId(), paymentTransaction.getId());

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
     */
    private EventTicketTransaction findOrCreateTicketTransaction(UserPaymentTransaction paymentTransaction, Long eventId) {
        // Try to find existing ticket transaction by Stripe payment intent ID
        Optional<EventTicketTransaction> existingOpt = Optional.empty();
        if (paymentTransaction.getStripePaymentIntentId() != null && !paymentTransaction.getStripePaymentIntentId().isEmpty()) {
            existingOpt = eventTicketTransactionRepository.findByStripePaymentIntentId(paymentTransaction.getStripePaymentIntentId());
        }

        return existingOpt
            .map(existing -> {
                log.info("Found existing ticket transaction {} for payment {}", existing.getId(), paymentTransaction.getId());

                // Update status to COMPLETED if not already
                if (!"COMPLETED".equals(existing.getStatus())) {
                    existing.setStatus("COMPLETED");
                    existing.setUpdatedAt(ZonedDateTime.now());
                    eventTicketTransactionRepository.save(existing);
                }

                return existing;
            })
            .orElseGet(() -> {
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

                // Set status and payment info
                ticketTransaction.setStatus("COMPLETED");
                ticketTransaction.setPaymentMethod(paymentTransaction.getPaymentMethod());
                ticketTransaction.setPaymentReference(paymentTransaction.getExternalTransactionId());
                ticketTransaction.setStripePaymentIntentId(paymentTransaction.getStripePaymentIntentId());
                ticketTransaction.setStripePaymentStatus("succeeded");
                ticketTransaction.setStripePaymentCurrency(paymentTransaction.getCurrency());

                // Set dates
                ticketTransaction.setPurchaseDate(paymentTransaction.getCreatedAt());
                ticketTransaction.setCreatedAt(ZonedDateTime.now());
                ticketTransaction.setUpdatedAt(ZonedDateTime.now());

                return eventTicketTransactionRepository.save(ticketTransaction);
            });
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
     * Extract first name from payment transaction metadata.
     */
    private String extractFirstNameFromPayment(UserPaymentTransaction paymentTransaction) {
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
        return null;
    }

    /**
     * Extract last name from payment transaction metadata.
     */
    private String extractLastNameFromPayment(UserPaymentTransaction paymentTransaction) {
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
        return null;
    }

    /**
     * Extract phone from payment transaction metadata.
     */
    private String extractPhoneFromPayment(UserPaymentTransaction paymentTransaction) {
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
}
