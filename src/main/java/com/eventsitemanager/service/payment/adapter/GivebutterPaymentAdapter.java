package com.eventsitemanager.service.payment.adapter;

import com.eventsitemanager.domain.enumeration.PaymentMethod;
import com.eventsitemanager.domain.enumeration.PaymentProvider;
import com.eventsitemanager.domain.enumeration.PaymentUseCase;
import com.eventsitemanager.repository.PaymentProviderConfigRepository;
import com.eventsitemanager.service.payment.PaymentException;
import com.eventsitemanager.service.payment.PaymentService;
import com.eventsitemanager.service.payment.adapter.givebutter.dto.GivebutterDonationRequest;
import com.eventsitemanager.service.payment.adapter.givebutter.dto.GivebutterDonationResponse;
import com.eventsitemanager.service.payment.dto.PaymentSessionRequest;
import com.eventsitemanager.service.payment.dto.PaymentSessionResponse;
import com.eventsitemanager.service.payment.dto.RefundRequest;
import com.eventsitemanager.service.payment.dto.RefundResponse;
import com.eventsitemanager.service.payment.encryption.PaymentCredentialEncryptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Givebutter payment adapter implementing PaymentService interface.
 * Supports zero-fee donations and Mass offerings through Givebutter API.
 */
@Service
public class GivebutterPaymentAdapter implements PaymentService {

    private static final Logger log = LoggerFactory.getLogger(GivebutterPaymentAdapter.class);
    private static final String GIVEBUTTER_API_BASE_URL = "https://api.givebutter.com/v1";

    private final PaymentProviderConfigRepository configRepository;
    private final PaymentCredentialEncryptionService encryptionService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public GivebutterPaymentAdapter(
        PaymentProviderConfigRepository configRepository,
        PaymentCredentialEncryptionService encryptionService
    ) {
        this.configRepository = configRepository;
        this.encryptionService = encryptionService;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String getProviderName() {
        return PaymentProvider.GIVEBUTTER.name();
    }

    @Override
    public PaymentSessionResponse initialize(PaymentSessionRequest request, Map<String, Object> providerConfig) throws PaymentException {
        log.info("Initializing Givebutter payment for tenant: {}, amount: {}", request.getTenantId(), request.getAmount());

        try {
            // Get API key from config
            String apiKey = (String) providerConfig.get("apiKey");
            if (apiKey == null || apiKey.isEmpty()) {
                throw new PaymentException("GIVEBUTTER_CONFIG_ERROR", "Givebutter API key not configured");
            }

            // Extract campaign ID - first try event metadata, then fallback to provider config
            // According to Givebutter API documentation, campaign ID is REQUIRED for creating donations
            // The endpoint /donations does not support POST - must use /campaigns/{campaignId}/donations
            String campaignId = extractCampaignId(request, providerConfig);
            if (campaignId == null || campaignId.isEmpty()) {
                log.error("Givebutter campaign ID is required but not configured for tenant {}", request.getTenantId());
                throw new PaymentException(
                    "GIVEBUTTER_CONFIG_ERROR",
                    "Givebutter campaign ID is required to create donations. Please configure it in event metadata (donationConfig.givebutterCampaignId) or provider configuration."
                );
            }
            log.debug("Using Givebutter campaign ID: {}", campaignId);

            // Build Givebutter donation request
            GivebutterDonationRequest gbRequest = buildDonationRequest(request, campaignId, providerConfig);

            // Call Givebutter API
            // IMPORTANT: The /v1/donations endpoint does NOT support POST (returns 405 Method Not Allowed)
            // Must use /v1/campaigns/{campaignId}/donations endpoint instead
            // Campaign ID must be in the URL path, not just in the request body
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            String apiUrl = GIVEBUTTER_API_BASE_URL + "/campaigns/" + campaignId + "/donations";
            log.info("Calling Givebutter API endpoint: {} with campaign ID in URL path: {}", apiUrl, campaignId);
            try {
                String requestBody = objectMapper.writeValueAsString(gbRequest);
                log.debug("Givebutter API request body: {}", requestBody);
            } catch (Exception e) {
                log.warn("Failed to serialize request body for logging", e);
            }

            HttpEntity<GivebutterDonationRequest> entity = new HttpEntity<>(gbRequest, headers);

            ResponseEntity<GivebutterDonationResponse> response;
            try {
                response = restTemplate.postForEntity(apiUrl, entity, GivebutterDonationResponse.class);
            } catch (org.springframework.web.client.HttpClientErrorException e) {
                log.error("Givebutter API call failed with client error: {} - URL: {}", e.getStatusCode(), apiUrl, e);
                if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                    throw new PaymentException("GIVEBUTTER_AUTH_ERROR", "Invalid Givebutter API key", e);
                } else if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                    throw new PaymentException("GIVEBUTTER_NOT_FOUND", "Givebutter resource not found: " + e.getMessage(), e);
                } else if (e.getStatusCode() == HttpStatus.METHOD_NOT_ALLOWED) {
                    // 405 Method Not Allowed - This may indicate:
                    // 1. The API endpoint structure has changed
                    // 2. The campaign ID format is incorrect (may need numeric ID instead of slug)
                    // 3. API key permissions issue
                    String errorMsg = "Givebutter API returned 405 Method Not Allowed. ";
                    errorMsg += "Endpoint: " + apiUrl + ". ";
                    errorMsg += "Please verify: (1) Campaign ID format (may need numeric ID instead of slug '" + campaignId + "'), ";
                    errorMsg +=
                    "(2) API key has donation creation permissions, (3) Check Givebutter API documentation for latest endpoint structure.";
                    throw new PaymentException("GIVEBUTTER_API_ERROR", errorMsg, e);
                } else {
                    throw new PaymentException("GIVEBUTTER_API_ERROR", "Givebutter API error: " + e.getMessage(), e);
                }
            } catch (org.springframework.web.client.HttpServerErrorException e) {
                log.error("Givebutter API call failed with server error: {}", e.getStatusCode(), e);
                throw new PaymentException("GIVEBUTTER_API_ERROR", "Givebutter API server error: " + e.getMessage(), e);
            } catch (RestClientException e) {
                log.error("Givebutter API call failed", e);
                throw new PaymentException("GIVEBUTTER_API_ERROR", "Failed to create donation: " + e.getMessage(), e);
            }

            if (response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
                log.error("Givebutter API returned unexpected status: {}", response.getStatusCode());
                throw new PaymentException("GIVEBUTTER_API_ERROR", "Givebutter API returned status: " + response.getStatusCode());
            }

            GivebutterDonationResponse gbResponse = response.getBody();
            if (gbResponse == null) {
                throw new PaymentException("GIVEBUTTER_API_ERROR", "Empty response from Givebutter API");
            }

            // Build payment session response
            PaymentSessionResponse sessionResponse = new PaymentSessionResponse();
            sessionResponse.setProvider(PaymentProvider.GIVEBUTTER);
            sessionResponse.setAmount(request.getAmount());
            sessionResponse.setCurrency(request.getCurrency());
            sessionResponse.setStatus("PENDING");
            sessionResponse.setSessionUrl(gbResponse.getCheckoutUrl());
            sessionResponse.setTransactionId(gbResponse.getDonationId());

            Map<String, Object> providerMetadata = new HashMap<>();
            providerMetadata.put("givebutterDonationId", gbResponse.getDonationId());
            // Only include campaign ID in metadata if it was provided
            if (campaignId != null && !campaignId.isEmpty()) {
                providerMetadata.put("campaignId", campaignId);
            }
            providerMetadata.put("externalTransactionId", gbResponse.getDonationId());
            sessionResponse.setProviderMetadata(providerMetadata);

            // Set supported payment methods
            List<PaymentMethod> supportedMethods = new ArrayList<>();
            supportedMethods.add(PaymentMethod.CARD);
            supportedMethods.add(PaymentMethod.APPLE_PAY);
            supportedMethods.add(PaymentMethod.GOOGLE_PAY);
            sessionResponse.setSupportedMethods(supportedMethods);

            return sessionResponse;
        } catch (PaymentException e) {
            throw e;
        } catch (Exception e) {
            log.error("Givebutter payment initialization failed", e);
            throw new PaymentException("GIVEBUTTER_ERROR", "Failed to initialize Givebutter payment: " + e.getMessage(), e);
        }
    }

    @Override
    public PaymentSessionResponse confirm(String transactionId, Map<String, Object> confirmationData, Map<String, Object> providerConfig)
        throws PaymentException {
        log.info("Confirming Givebutter payment: {}", transactionId);
        // Givebutter payments are confirmed via webhook, so this is mainly for status check
        return getStatus(transactionId, providerConfig);
    }

    @Override
    public void cancel(String transactionId, Map<String, Object> providerConfig) throws PaymentException {
        log.info("Cancelling Givebutter donation: {}", transactionId);
        // Givebutter donations cannot be cancelled once created
        log.warn("Givebutter donations cannot be cancelled via API");
    }

    @Override
    public RefundResponse refund(RefundRequest request, Map<String, Object> providerConfig) throws PaymentException {
        log.info("Processing Givebutter refund for transaction: {}", request.getTransactionId());

        try {
            String apiKey = (String) providerConfig.get("apiKey");
            if (apiKey == null || apiKey.isEmpty()) {
                throw new PaymentException("GIVEBUTTER_CONFIG_ERROR", "Givebutter API key not configured");
            }

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Build refund request
            Map<String, Object> refundRequest = new HashMap<>();
            if (request.getAmount() != null) {
                refundRequest.put("amount", request.getAmount().multiply(BigDecimal.valueOf(100)).intValue()); // Convert to cents
            }
            if (request.getReason() != null) {
                refundRequest.put("reason", request.getReason());
            }

            String apiUrl = GIVEBUTTER_API_BASE_URL + "/donations/" + request.getTransactionId() + "/refund";
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(refundRequest, headers);

            ResponseEntity<Map> response;
            try {
                response = restTemplate.postForEntity(apiUrl, entity, Map.class);
            } catch (RestClientException e) {
                log.error("Givebutter refund API call failed", e);
                throw new PaymentException("GIVEBUTTER_API_ERROR", "Failed to process refund: " + e.getMessage(), e);
            }

            if (response.getStatusCode() != HttpStatus.OK) {
                throw new PaymentException("GIVEBUTTER_API_ERROR", "Givebutter refund API returned status: " + response.getStatusCode());
            }

            RefundResponse refundResponse = new RefundResponse();
            refundResponse.setRefundId((String) response.getBody().get("id"));
            refundResponse.setTransactionId(request.getTransactionId());
            refundResponse.setAmount(request.getAmount());
            refundResponse.setCurrency(request.getCurrency());
            refundResponse.setStatus("COMPLETED");
            refundResponse.setReason(request.getReason());
            refundResponse.setProcessedAt(ZonedDateTime.now());

            return refundResponse;
        } catch (PaymentException e) {
            throw e;
        } catch (Exception e) {
            log.error("Givebutter refund failed", e);
            throw new PaymentException("GIVEBUTTER_ERROR", "Failed to process refund: " + e.getMessage(), e);
        }
    }

    @Override
    public PaymentSessionResponse getStatus(String transactionId, Map<String, Object> providerConfig) throws PaymentException {
        log.debug("Getting Givebutter donation status: {}", transactionId);

        try {
            String apiKey = (String) providerConfig.get("apiKey");
            if (apiKey == null || apiKey.isEmpty()) {
                throw new PaymentException("GIVEBUTTER_CONFIG_ERROR", "Givebutter API key not configured");
            }

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiKey);

            String apiUrl = GIVEBUTTER_API_BASE_URL + "/donations/" + transactionId;
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response;
            try {
                response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, Map.class);
            } catch (RestClientException e) {
                log.error("Givebutter status API call failed", e);
                throw new PaymentException("GIVEBUTTER_API_ERROR", "Failed to get donation status: " + e.getMessage(), e);
            }

            if (response.getStatusCode() != HttpStatus.OK) {
                throw new PaymentException("GIVEBUTTER_API_ERROR", "Givebutter status API returned status: " + response.getStatusCode());
            }

            Map<String, Object> donationData = response.getBody();
            if (donationData == null) {
                throw new PaymentException("GIVEBUTTER_API_ERROR", "Empty response from Givebutter API");
            }

            PaymentSessionResponse sessionResponse = new PaymentSessionResponse();
            sessionResponse.setProvider(PaymentProvider.GIVEBUTTER);
            sessionResponse.setTransactionId(transactionId);
            sessionResponse.setStatus(mapGivebutterStatus((String) donationData.get("status")));

            Object amountObj = donationData.get("amount");
            if (amountObj != null) {
                if (amountObj instanceof Number) {
                    sessionResponse.setAmount(BigDecimal.valueOf(((Number) amountObj).doubleValue()).divide(BigDecimal.valueOf(100)));
                }
            }

            Object currencyObj = donationData.get("currency");
            if (currencyObj != null) {
                sessionResponse.setCurrency(currencyObj.toString().toUpperCase());
            }

            return sessionResponse;
        } catch (PaymentException e) {
            throw e;
        } catch (Exception e) {
            log.error("Givebutter status retrieval failed", e);
            throw new PaymentException("GIVEBUTTER_ERROR", "Failed to get donation status: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> handleWebhook(String payload, String signature, Map<String, Object> providerConfig) throws PaymentException {
        log.info("Handling Givebutter webhook");

        try {
            // Parse webhook payload
            com.eventsitemanager.service.payment.adapter.givebutter.dto.GivebutterWebhookEvent event = objectMapper.readValue(
                payload,
                com.eventsitemanager.service.payment.adapter.givebutter.dto.GivebutterWebhookEvent.class
            );

            Map<String, Object> result = new HashMap<>();
            result.put("processed", true);
            result.put("eventType", event.getType());
            if (event.getDonation() != null) {
                result.put("donationId", event.getDonation().getId());
            }

            return result;
        } catch (Exception e) {
            log.error("Givebutter webhook processing failed", e);
            throw new PaymentException("WEBHOOK_ERROR", "Failed to process webhook: " + e.getMessage(), e);
        }
    }

    @Override
    public List<PaymentMethod> supportedMethods(Map<String, Object> providerConfig) {
        List<PaymentMethod> methods = new ArrayList<>();
        methods.add(PaymentMethod.CARD);
        methods.add(PaymentMethod.APPLE_PAY);
        methods.add(PaymentMethod.GOOGLE_PAY);
        return methods;
    }

    /**
     * Build Givebutter donation request from payment session request.
     */
    private GivebutterDonationRequest buildDonationRequest(
        PaymentSessionRequest request,
        String campaignId,
        Map<String, Object> providerConfig
    ) {
        GivebutterDonationRequest gbRequest = new GivebutterDonationRequest();
        gbRequest.setAmount(request.getAmount());
        // Only set campaign ID if provided (it's optional)
        if (campaignId != null && !campaignId.isEmpty()) {
            gbRequest.setCampaignId(campaignId);
        }
        gbRequest.setDonorEmail(request.getCustomerEmail());
        gbRequest.setDonorName(request.getCustomerName());

        // Extract metadata from request
        Map<String, Object> requestMetadata = request.getMetadata();
        if (requestMetadata != null) {
            // Check for anonymous flag
            Object anonymousObj = requestMetadata.get("anonymous");
            if (anonymousObj instanceof Boolean) {
                gbRequest.setAnonymous((Boolean) anonymousObj);
            }

            // Check for recurring flag
            Object recurringObj = requestMetadata.get("recurring");
            if (recurringObj instanceof Boolean && (Boolean) recurringObj) {
                gbRequest.setRecurring(true);
                Object frequencyObj = requestMetadata.get("recurringFrequency");
                if (frequencyObj != null) {
                    gbRequest.setRecurringFrequency(frequencyObj.toString());
                } else {
                    gbRequest.setRecurringFrequency("monthly"); // Default
                }
            }

            // Extract prayer intention as custom field
            Object prayerIntentionObj = requestMetadata.get("prayerIntention");
            if (prayerIntentionObj != null) {
                Map<String, String> customFields = new HashMap<>();
                customFields.put("prayer_intention", prayerIntentionObj.toString());
                gbRequest.setCustomFields(customFields);
            }
        }

        // Build metadata for Givebutter
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("tenant_id", request.getTenantId());
        metadata.put("payment_use_case", request.getPaymentUseCase().name());
        if (request.getEventId() != null) {
            metadata.put("event_id", request.getEventId().toString());
        }
        gbRequest.setMetadata(metadata);

        return gbRequest;
    }

    /**
     * Extract campaign ID from event metadata or provider configuration.
     * Priority: 1) Event metadata, 2) Provider configuration JSON, 3) Provider config direct key
     */
    private String extractCampaignId(PaymentSessionRequest request, Map<String, Object> providerConfig) {
        // First try event-specific campaign ID from event metadata
        if (request.getEventId() != null) {
            try {
                // Note: This requires EventDetailsRepository injection if we want to fetch event
                // For now, we'll rely on the event metadata being passed via request metadata
                Map<String, Object> requestMetadata = request.getMetadata();
                if (requestMetadata != null) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> eventMetadata = (Map<String, Object>) requestMetadata.get("eventMetadata");
                    if (eventMetadata != null) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> donationConfig = (Map<String, Object>) eventMetadata.get("donationConfig");
                        if (donationConfig != null) {
                            Object campaignIdObj = donationConfig.get("givebutterCampaignId");
                            if (campaignIdObj != null) {
                                String campaignId = campaignIdObj.toString();
                                log.debug("Extracted campaign ID from event metadata: {}", campaignId);
                                return campaignId;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.warn("Failed to extract campaign ID from event metadata", e);
            }
        }

        // Fallback: try provider configuration JSON
        String configJson = (String) providerConfig.get("configurationJson");
        if (configJson != null && !configJson.isEmpty()) {
            try {
                Map<String, Object> config = objectMapper.readValue(configJson, Map.class);
                Object campaignIdObj = config.get("campaignId");
                if (campaignIdObj != null) {
                    String campaignId = campaignIdObj.toString();
                    log.debug("Extracted campaign ID from provider config JSON: {}", campaignId);
                    return campaignId;
                }
            } catch (Exception e) {
                log.warn("Failed to parse configuration JSON", e);
            }
        }

        // Final fallback: try direct key
        Object campaignIdObj = providerConfig.get("campaignId");
        if (campaignIdObj != null) {
            String campaignId = campaignIdObj.toString();
            log.debug("Extracted campaign ID from provider config direct key: {}", campaignId);
            return campaignId;
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
}
