package com.eventsitemanager.web.rest;

import com.eventsitemanager.domain.enumeration.PaymentProvider;
import com.eventsitemanager.service.dto.GiveButterDonationStatusDTO;
import com.eventsitemanager.service.payment.PaymentException;
import com.eventsitemanager.service.payment.PaymentProviderConfigService;
import com.eventsitemanager.service.payment.PaymentService;
import com.eventsitemanager.service.payment.dto.PaymentSessionResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for GiveButter donation status polling.
 * Provides endpoints for frontend to poll GiveButter donation status.
 */
@RestController
@RequestMapping("/api/proxy/givebutter")
public class GiveButterStatusController {

    private static final Logger log = LoggerFactory.getLogger(GiveButterStatusController.class);

    private final PaymentProviderConfigService configService;
    private final Map<PaymentProvider, PaymentService> paymentServices;

    public GiveButterStatusController(PaymentProviderConfigService configService, List<PaymentService> paymentServicesList) {
        this.configService = configService;
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
     * {@code GET /api/proxy/givebutter/donations/{donationId}/status} : Get GiveButter donation status.
     *
     * @param donationId the GiveButter donation ID
     * @param tenantId optional tenant ID (if not provided, will try to get from context)
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and donation status in body
     */
    @GetMapping("/donations/{donationId}/status")
    public ResponseEntity<GiveButterDonationStatusDTO> getDonationStatus(
        @PathVariable String donationId,
        @RequestParam(required = false) String tenantId
    ) {
        log.debug("REST request to get GiveButter donation status: {} for tenant: {}", donationId, tenantId);

        try {
            if (tenantId == null || tenantId.isEmpty()) {
                log.error("Tenant ID is required for GiveButter status check");
                return ResponseEntity.badRequest().build();
            }

            // Get GiveButter config
            Map<String, Object> providerConfig = configService.getProviderConfig(tenantId, PaymentProvider.GIVEBUTTER).orElse(null);

            if (providerConfig == null) {
                log.error("GiveButter provider not configured for tenant: {}", tenantId);
                return ResponseEntity.status(500).build();
            }

            // Get GiveButter payment service
            PaymentService paymentService = paymentServices.get(PaymentProvider.GIVEBUTTER);
            if (paymentService == null) {
                log.error("GiveButter payment service not available");
                return ResponseEntity.status(500).build();
            }

            // Call GiveButter API to get donation status
            PaymentSessionResponse response = paymentService.getStatus(donationId, providerConfig);

            // Map to status DTO
            GiveButterDonationStatusDTO statusDTO = new GiveButterDonationStatusDTO();
            statusDTO.setDonationId(donationId);
            statusDTO.setStatus(mapGiveButterStatus(response.getStatus()));
            statusDTO.setAmount(response.getAmount());
            // Note: Email and createdAt are not available in PaymentSessionResponse
            // They would need to be fetched from the full donation data if needed

            return ResponseEntity.ok(statusDTO);
        } catch (PaymentException e) {
            log.error("Error fetching GiveButter donation status: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        } catch (Exception e) {
            log.error("Unexpected error fetching GiveButter donation status", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Map GiveButter status to internal status.
     */
    private String mapGiveButterStatus(String gbStatus) {
        if (gbStatus == null) {
            return "PENDING";
        }

        return switch (gbStatus.toUpperCase()) {
            case "COMPLETED", "SUCCEEDED", "PAID" -> "COMPLETED";
            case "PENDING", "PROCESSING" -> "PENDING";
            case "FAILED", "CANCELLED" -> "FAILED";
            case "REFUNDED" -> "REFUNDED";
            default -> "PENDING";
        };
    }
}
