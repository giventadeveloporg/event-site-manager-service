package com.nextjstemplate.web.rest;

import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.domain.UserPaymentTransaction;
import com.nextjstemplate.domain.enumeration.PaymentProvider;
import com.nextjstemplate.domain.enumeration.PaymentUseCase;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for donation proxy endpoints.
 * Provides endpoints for frontend to interact with donation transactions.
 */
@RestController
@RequestMapping("/api/proxy/donations")
public class DonationProxyController {

    private static final Logger log = LoggerFactory.getLogger(DonationProxyController.class);
    private static final String ENTITY_NAME = "donationTransaction";

    private final DonationTransactionService donationTransactionService;
    private final PaymentProviderConfigService configService;
    private final UserPaymentTransactionRepository paymentTransactionRepository;
    private final EventDetailsRepository eventDetailsRepository;
    private final Map<PaymentProvider, PaymentService> paymentServices;

    public DonationProxyController(
        DonationTransactionService donationTransactionService,
        PaymentProviderConfigService configService,
        UserPaymentTransactionRepository paymentTransactionRepository,
        EventDetailsRepository eventDetailsRepository,
        List<PaymentService> paymentServicesList
    ) {
        this.donationTransactionService = donationTransactionService;
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
     * {@code POST /api/proxy/donations/create-from-givebutter} : Create donation transaction from GiveButter data.
     *
     * @param request the request containing GiveButter donation ID and event details
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the donationTransactionDTO
     */
    @PostMapping("/create-from-givebutter")
    public ResponseEntity<DonationTransactionDTO> createDonationFromGiveButter(
        @Valid @RequestBody CreateDonationFromGiveButterRequest request
    ) {
        log.debug("REST request to create donation from GiveButter via proxy endpoint: {}", request.getGivebutterDonationId());

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
}
