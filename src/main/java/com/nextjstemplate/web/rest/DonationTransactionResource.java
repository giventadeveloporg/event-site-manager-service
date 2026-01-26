package com.nextjstemplate.web.rest;

import com.nextjstemplate.domain.DonationTransaction;
import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.domain.UserPaymentTransaction;
import com.nextjstemplate.domain.enumeration.PaymentProvider;
import com.nextjstemplate.domain.enumeration.PaymentUseCase;
import com.nextjstemplate.repository.DonationTransactionRepository;
import com.nextjstemplate.repository.EventDetailsRepository;
import com.nextjstemplate.repository.UserPaymentTransactionRepository;
import com.nextjstemplate.service.DonationTransactionService;
import com.nextjstemplate.service.dto.CreateDonationFromGiveButterRequest;
import com.nextjstemplate.service.dto.DonationTransactionDTO;
import com.nextjstemplate.service.payment.PaymentException;
import com.nextjstemplate.service.payment.PaymentService;
import com.nextjstemplate.service.payment.config.PaymentProviderConfigService;
import com.nextjstemplate.service.payment.dto.PaymentSessionResponse;
import com.nextjstemplate.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing DonationTransaction.
 */
@RestController
@RequestMapping("/api/donation-transactions")
public class DonationTransactionResource {

    private static final Logger log = LoggerFactory.getLogger(DonationTransactionResource.class);
    private static final String ENTITY_NAME = "donationTransaction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Value("${app.email-host-url-prefix:}")
    private String emailHostUrlPrefix;

    private final DonationTransactionService donationTransactionService;
    private final DonationTransactionRepository donationTransactionRepository;
    private final PaymentProviderConfigService configService;
    private final UserPaymentTransactionRepository paymentTransactionRepository;
    private final EventDetailsRepository eventDetailsRepository;
    private final Map<PaymentProvider, PaymentService> paymentServices;

    public DonationTransactionResource(
        DonationTransactionService donationTransactionService,
        DonationTransactionRepository donationTransactionRepository,
        PaymentProviderConfigService configService,
        UserPaymentTransactionRepository paymentTransactionRepository,
        EventDetailsRepository eventDetailsRepository,
        List<PaymentService> paymentServicesList
    ) {
        this.donationTransactionService = donationTransactionService;
        this.donationTransactionRepository = donationTransactionRepository;
        this.configService = configService;
        this.paymentTransactionRepository = paymentTransactionRepository;
        this.eventDetailsRepository = eventDetailsRepository;
        this.paymentServices = new HashMap<>();
        // Register all payment services
        if (paymentServicesList != null) {
            for (PaymentService service : paymentServicesList) {
                try {
                    PaymentProvider provider = PaymentProvider.valueOf(service.getProviderName().toUpperCase());
                    this.paymentServices.put(provider, service);
                    log.debug("Registered payment service for provider: {}", provider);
                } catch (IllegalArgumentException e) {
                    log.warn("Unknown provider name: {}", service.getProviderName());
                }
            }
        }
    }

    /**
     * {@code GET /api/donation-transactions/{id}} : get the "id" donationTransaction.
     *
     * @param id the id of the donationTransactionDTO to retrieve.
     * @param eventId optional event ID for validation
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the donationTransactionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DonationTransactionDTO> getDonationTransaction(
        @PathVariable Long id,
        @RequestParam(required = false) Long eventId
    ) {
        log.debug("REST request to get DonationTransaction : {}", id);
        DonationTransactionDTO donation = donationTransactionService.findOne(id).orElse(null);

        if (donation == null) {
            return ResponseEntity.notFound().build();
        }

        // Verify event ID matches if provided
        if (eventId != null && donation.getEventId() != null && !eventId.equals(donation.getEventId())) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(donation);
    }

    /**
     * {@code GET /api/events/{eventId}/donations} : get all donations for an event.
     *
     * @param eventId the event ID
     * @param tenantId the tenant ID
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of donations in body.
     */
    @GetMapping("/events/{eventId}/donations")
    public ResponseEntity<List<DonationTransactionDTO>> getDonationsForEvent(@PathVariable Long eventId, @RequestParam String tenantId) {
        log.debug("REST request to get donations for event : {}", eventId);
        List<DonationTransactionDTO> donations = donationTransactionService.findByEventIdAndTenantId(eventId, tenantId);
        return ResponseEntity.ok(donations);
    }

    /**
     * {@code POST /api/donation-transactions/create-from-givebutter} : Create donation transaction from GiveButter data.
     * Note: This endpoint is also available at /api/proxy/donations/create-from-givebutter via DonationProxyController.
     *
     * @param request the request containing GiveButter donation ID and event details
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the donationTransactionDTO
     */
    @PostMapping("/create-from-givebutter")
    public ResponseEntity<DonationTransactionDTO> createDonationFromGiveButter(
        @Valid @RequestBody CreateDonationFromGiveButterRequest request
    ) {
        log.debug("REST request to create donation from GiveButter : {}", request.getGivebutterDonationId());

        try {
            // Get GiveButter config
            Map<String, Object> providerConfig = configService
                .getProviderConfig(request.getTenantId(), PaymentProvider.GIVEBUTTER)
                .orElseThrow(() ->
                    new BadRequestAlertException("GiveButter provider not configured for tenant", ENTITY_NAME, "providernotconfigured")
                );

            // Get GiveButter payment service
            PaymentService paymentService = paymentServices.get(PaymentProvider.GIVEBUTTER);
            if (paymentService == null) {
                throw new BadRequestAlertException("GiveButter payment service not available", ENTITY_NAME, "servicenotavailable");
            }

            // Fetch full donation data from GiveButter
            PaymentSessionResponse gbResponse = paymentService.getStatus(request.getGivebutterDonationId(), providerConfig);

            // Check if donation transaction already exists
            Optional<DonationTransactionDTO> existingOpt = donationTransactionService.findByGivebutterDonationIdAndTenantId(
                request.getGivebutterDonationId(),
                request.getTenantId()
            );

            DonationTransactionDTO existing = existingOpt.orElse(null);
            if (existing != null) {
                log.info("Donation transaction already exists for GiveButter donation ID: {}", request.getGivebutterDonationId());
                return ResponseEntity.ok(existing);
            }

            // Create payment transaction
            UserPaymentTransaction paymentTransaction = new UserPaymentTransaction();
            paymentTransaction.setTenantId(request.getTenantId());
            if (request.getEventId() != null) {
                eventDetailsRepository.findById(request.getEventId()).ifPresent(paymentTransaction::setEvent);
            }
            // Store provider in metadata (UserPaymentTransaction doesn't have providerName field)
            paymentTransaction.setTransactionType(PaymentUseCase.DONATION_ZERO_FEE.name());
            paymentTransaction.setAmount(gbResponse.getAmount() != null ? gbResponse.getAmount() : BigDecimal.ZERO);
            paymentTransaction.setStatus("COMPLETED");
            paymentTransaction.setExternalTransactionId(request.getGivebutterDonationId());
            paymentTransaction.setStripePaymentIntentId(request.getGivebutterDonationId()); // Store for compatibility
            paymentTransaction.setCreatedAt(ZonedDateTime.now());
            paymentTransaction.setUpdatedAt(ZonedDateTime.now());

            // Store metadata
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("provider", PaymentProvider.GIVEBUTTER.name());
            metadata.put("givebutterDonationId", request.getGivebutterDonationId());
            try {
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                paymentTransaction.setMetadata(mapper.writeValueAsString(metadata));
            } catch (Exception e) {
                log.warn("Failed to serialize payment transaction metadata", e);
            }

            paymentTransaction = paymentTransactionRepository.save(paymentTransaction);

            // Create donation transaction
            DonationTransactionDTO donationDTO = new DonationTransactionDTO();
            donationDTO.setTenantId(request.getTenantId());
            donationDTO.setEventId(request.getEventId());
            donationDTO.setPaymentTransactionId(paymentTransaction.getId());
            donationDTO.setTransactionReference("GB-" + paymentTransaction.getId());
            donationDTO.setGivebutterDonationId(request.getGivebutterDonationId());
            donationDTO.setAmount(gbResponse.getAmount() != null ? gbResponse.getAmount() : BigDecimal.ZERO);
            donationDTO.setStatus("COMPLETED");
            donationDTO.setEmailSent(false);
            donationDTO.setIsRecurring(false);
            donationDTO.setIsAnonymous(false);
            donationDTO.setCreatedAt(ZonedDateTime.now());
            donationDTO.setUpdatedAt(ZonedDateTime.now());

            // Note: Email, name, phone, and custom fields would need to be extracted from GiveButter API response
            // For now, we'll set defaults - these can be updated when full donation data is available
            // The GiveButter API response in PaymentSessionResponse doesn't include donor details
            // You may need to enhance the GivebutterPaymentAdapter.getStatus() to return full donation data

            DonationTransactionDTO savedDonation = donationTransactionService.save(donationDTO);

            return ResponseEntity.ok(savedDonation);
        } catch (PaymentException e) {
            log.error("Error creating donation from GiveButter: {}", e.getMessage(), e);
            throw new BadRequestAlertException("Failed to create donation: " + e.getMessage(), ENTITY_NAME, "creationfailed");
        } catch (Exception e) {
            log.error("Unexpected error creating donation from GiveButter", e);
            throw new BadRequestAlertException("Failed to create donation: " + e.getMessage(), ENTITY_NAME, "creationfailed");
        }
    }

    /**
     * {@code POST /api/events/{eventId}/donations/{transactionId}/qrcode} : Generate QR code for donation.
     *
     * @param eventId the event ID
     * @param transactionId the donation transaction ID
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and QR code URL in body
     */
    @PostMapping("/events/{eventId}/donations/{transactionId}/qrcode")
    public ResponseEntity<Map<String, String>> generateQrCode(@PathVariable Long eventId, @PathVariable Long transactionId) {
        log.debug("REST request to generate QR code for donation transaction: {} for event: {}", transactionId, eventId);

        String qrCodeUrl = donationTransactionService.generateQrCode(eventId, transactionId, emailHostUrlPrefix);

        Map<String, String> response = new HashMap<>();
        response.put("qrCodeUrl", qrCodeUrl);

        return ResponseEntity.ok(response);
    }

    /**
     * {@code POST /api/events/{eventId}/donations/{transactionId}/send-email} : Send donation confirmation email.
     *
     * @param eventId the event ID
     * @param transactionId the donation transaction ID
     * @param to the recipient email address
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and email sent status in body
     */
    @PostMapping("/events/{eventId}/donations/{transactionId}/send-email")
    public ResponseEntity<Map<String, Boolean>> sendEmail(
        @PathVariable Long eventId,
        @PathVariable Long transactionId,
        @RequestParam String to
    ) {
        log.debug("REST request to send email for donation transaction: {} for event: {} to: {}", transactionId, eventId, to);

        boolean sent = donationTransactionService.sendConfirmationEmail(eventId, transactionId, to);

        Map<String, Boolean> response = new HashMap<>();
        response.put("emailSent", sent);

        return ResponseEntity.ok(response);
    }
}
