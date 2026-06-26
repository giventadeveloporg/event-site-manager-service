package com.eventsitemanager.web.rest;

import com.eventsitemanager.domain.GivebutterWebhookEvent;
import com.eventsitemanager.domain.PaymentProviderConfig;
import com.eventsitemanager.domain.enumeration.PaymentProvider;
import com.eventsitemanager.repository.GivebutterWebhookEventRepository;
import com.eventsitemanager.repository.PaymentProviderConfigRepository;
import com.eventsitemanager.service.payment.PaymentException;
import com.eventsitemanager.service.payment.encryption.PaymentCredentialEncryptionService;
import com.eventsitemanager.service.payment.orchestration.PaymentOrchestrationService;
import com.eventsitemanager.service.payment.webhook.GivebutterWebhookRateLimitService;
import com.eventsitemanager.service.webhook.InboundWebhookGuard;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling Givebutter webhook events.
 */
@RestController
@RequestMapping("/api/webhooks/givebutter")
public class GivebutterWebhookController {

    private static final Logger log = LoggerFactory.getLogger(GivebutterWebhookController.class);

    private final PaymentProviderConfigRepository configRepository;
    private final PaymentCredentialEncryptionService encryptionService;
    private final PaymentOrchestrationService orchestrationService;
    private final GivebutterWebhookEventRepository webhookEventRepository;
    private final GivebutterWebhookRateLimitService rateLimitService;
    private final ObjectMapper objectMapper;
    private final InboundWebhookGuard inboundWebhookGuard;

    public GivebutterWebhookController(
        PaymentProviderConfigRepository configRepository,
        PaymentCredentialEncryptionService encryptionService,
        PaymentOrchestrationService orchestrationService,
        GivebutterWebhookEventRepository webhookEventRepository,
        GivebutterWebhookRateLimitService rateLimitService,
        ObjectMapper objectMapper,
        InboundWebhookGuard inboundWebhookGuard
    ) {
        this.configRepository = configRepository;
        this.encryptionService = encryptionService;
        this.orchestrationService = orchestrationService;
        this.webhookEventRepository = webhookEventRepository;
        this.rateLimitService = rateLimitService;
        this.objectMapper = objectMapper;
        this.inboundWebhookGuard = inboundWebhookGuard;
    }

    /**
     * POST /api/webhooks/givebutter : Handle Givebutter webhook events.
     *
     * @param signature the X-Givebutter-Signature header
     * @param payload   the raw webhook payload
     * @param request   the HTTP request
     * @return the ResponseEntity with status 200 (OK) if successful
     */
    @PostMapping
    public ResponseEntity<?> handleWebhook(
        @RequestHeader(value = "X-Givebutter-Signature", required = false) String signature,
        @RequestBody String payload,
        HttpServletRequest request
    ) {
        if (!inboundWebhookGuard.isPaymentInboundEnabled()) {
            log.info("Payment inbound webhooks are disabled; rejecting Givebutter webhook");
            return inboundWebhookGuard.paymentInboundDisabledResponse();
        }

        log.info("Received Givebutter webhook");

        // Rate limiting check
        String clientIp = getClientIpAddress(request);
        try {
            rateLimitService.checkRateLimit(clientIp);
        } catch (GivebutterWebhookRateLimitService.RateLimitExceededException e) {
            log.warn("Rate limit exceeded for IP: {}", clientIp);
            return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .header("Retry-After", "60")
                .body("Rate limit exceeded. Please try again later.");
        }

        com.eventsitemanager.service.payment.adapter.givebutter.dto.GivebutterWebhookEvent event = null;
        GivebutterWebhookEvent webhookEvent = null;
        String eventId = null;

        try {
            // Parse webhook payload to extract tenant_id
            event =
                objectMapper.readValue(payload, com.eventsitemanager.service.payment.adapter.givebutter.dto.GivebutterWebhookEvent.class);

            // Generate event ID for idempotency (use donation ID + timestamp if available, otherwise generate UUID)
            eventId = generateEventId(event);

            // Check idempotency - have we already processed this event?
            if (webhookEventRepository.isEventProcessed(eventId)) {
                log.info("Givebutter webhook event {} already processed, skipping", eventId);
                return ResponseEntity.ok().build();
            }

            // Save webhook event to database (audit trail)
            webhookEvent = saveWebhookEvent(eventId, event, payload);

            // Extract tenant ID from webhook payload
            String tenantId = extractTenantId(event);
            if (tenantId == null || tenantId.isEmpty()) {
                log.warn("Could not extract tenant ID from Givebutter webhook payload");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tenant ID not found in webhook payload");
            }

            // Retrieve webhook secret from database (per tenant)
            PaymentProviderConfig config = configRepository
                .findByTenantIdAndProviderName(tenantId, PaymentProvider.GIVEBUTTER)
                .orElse(null);

            if (config == null) {
                log.warn("Givebutter not configured for tenant: {}", tenantId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Givebutter not configured for tenant");
            }

            // Decrypt webhook secret
            String webhookSecret = null;
            if (config.getWebhookSecretEncrypted() != null) {
                try {
                    webhookSecret = encryptionService.decrypt(config.getWebhookSecretEncrypted());
                } catch (Exception e) {
                    log.error("Failed to decrypt webhook secret for tenant: {}", tenantId, e);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to decrypt webhook secret");
                }
            }

            // Verify webhook signature
            if (signature == null || signature.isEmpty()) {
                log.warn("Missing webhook signature header");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid signature");
            }

            if (webhookSecret != null && !verifySignature(payload, signature, webhookSecret)) {
                log.warn("Invalid webhook signature for tenant: {}", tenantId);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid signature");
            }

            // Process webhook event
            try {
                orchestrationService.processGivebutterWebhook(event, tenantId);

                // Mark as processed
                webhookEvent.setProcessed(true);
                webhookEvent.setProcessedAt(ZonedDateTime.now());
                webhookEventRepository.save(webhookEvent);

                log.info("Successfully processed Givebutter webhook event: {} for tenant: {}", event.getType(), tenantId);
                return ResponseEntity.ok().build();
            } catch (PaymentException e) {
                log.error("Failed to process Givebutter webhook event", e);
                webhookEvent.setErrorMessage(e.getMessage());
                webhookEvent.setRetryCount(webhookEvent.getRetryCount() + 1);
                webhookEventRepository.save(webhookEvent);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process webhook: " + e.getMessage());
            }
        } catch (Exception e) {
            log.error("Givebutter webhook processing failed", e);
            // Try to save error event if we have event ID
            try {
                if (eventId != null && webhookEvent != null) {
                    webhookEvent.setErrorMessage(e.getMessage());
                    webhookEvent.setRetryCount(webhookEvent.getRetryCount() + 1);
                    webhookEventRepository.save(webhookEvent);
                }
            } catch (Exception saveError) {
                log.error("Failed to save error event", saveError);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid webhook payload: " + e.getMessage());
        }
    }

    /**
     * Generate event ID for idempotency check.
     */
    private String generateEventId(com.eventsitemanager.service.payment.adapter.givebutter.dto.GivebutterWebhookEvent event) {
        if (event.getDonation() != null && event.getDonation().getId() != null) {
            // Use donation ID + event type + timestamp for uniqueness
            return event.getDonation().getId() + "_" + event.getType() + "_" + System.currentTimeMillis();
        }
        // Fallback to UUID if donation ID not available
        return UUID.randomUUID().toString();
    }

    /**
     * Save webhook event to database for audit trail and idempotency.
     */
    private GivebutterWebhookEvent saveWebhookEvent(
        String eventId,
        com.eventsitemanager.service.payment.adapter.givebutter.dto.GivebutterWebhookEvent eventDTO,
        String payload
    ) {
        GivebutterWebhookEvent webhookEvent = new GivebutterWebhookEvent();
        webhookEvent.setEventId(eventId);
        webhookEvent.setEventType(eventDTO.getType());
        webhookEvent.setPayload(payload);
        webhookEvent.setReceivedAt(ZonedDateTime.now());
        webhookEvent.setCreatedAt(ZonedDateTime.now());
        webhookEvent.setProcessed(false);
        webhookEvent.setRetryCount(0);

        if (eventDTO.getDonation() != null) {
            webhookEvent.setDonationId(eventDTO.getDonation().getId());
        }

        String tenantId = extractTenantId(eventDTO);
        if (tenantId != null) {
            webhookEvent.setTenantId(tenantId);
        }

        return webhookEventRepository.save(webhookEvent);
    }

    /**
     * Extract tenant ID from Givebutter webhook event.
     * Tries to get it from donation metadata, campaign metadata, or donation custom fields.
     */
    private String extractTenantId(com.eventsitemanager.service.payment.adapter.givebutter.dto.GivebutterWebhookEvent event) {
        if (event.getDonation() != null) {
            // Try donation metadata first
            Map<String, Object> donationMetadata = event.getDonation().getMetadata();
            if (donationMetadata != null) {
                Object tenantIdObj = donationMetadata.get("tenant_id");
                if (tenantIdObj != null) {
                    return tenantIdObj.toString();
                }
            }

            // Try custom fields
            Map<String, String> customFields = event.getDonation().getCustomFields();
            if (customFields != null) {
                String tenantId = customFields.get("tenant_id");
                if (tenantId != null && !tenantId.isEmpty()) {
                    return tenantId;
                }
            }
        }

        // Try campaign metadata
        if (event.getCampaign() != null) {
            Map<String, Object> campaignMetadata = event.getCampaign().getMetadata();
            if (campaignMetadata != null) {
                Object tenantIdObj = campaignMetadata.get("tenant_id");
                if (tenantIdObj != null) {
                    return tenantIdObj.toString();
                }
            }
        }

        return null;
    }

    /**
     * Verify Givebutter webhook signature using HMAC-SHA256.
     */
    private boolean verifySignature(String payload, String signature, String webhookSecret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(webhookSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            String computedSignature = bytesToHex(hash);

            // Givebutter may send signature in different formats, try both
            return signature.equals(computedSignature) || signature.equals("sha256=" + computedSignature);
        } catch (Exception e) {
            log.error("Failed to verify webhook signature", e);
            return false;
        }
    }

    /**
     * Convert bytes to hexadecimal string.
     */
    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    /**
     * Extract client IP address from request.
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            // X-Forwarded-For can contain multiple IPs, take the first one
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }
}
