package com.nextjstemplate.service.payment.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextjstemplate.domain.enumeration.PaymentMethod;
import com.nextjstemplate.domain.enumeration.PaymentProvider;
import com.nextjstemplate.service.payment.PaymentException;
import com.nextjstemplate.service.payment.PaymentService;
import com.nextjstemplate.service.payment.dto.PaymentSessionRequest;
import com.nextjstemplate.service.payment.dto.PaymentSessionResponse;
import com.nextjstemplate.service.payment.dto.RefundRequest;
import com.nextjstemplate.service.payment.dto.RefundResponse;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * PayPal payment adapter implementing PaymentService interface.
 * Uses PayPal Orders API via direct HTTP calls for payment processing.
 */
@Service
public class PayPalPaymentAdapter implements PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PayPalPaymentAdapter.class);
    private static final String PAYPAL_SANDBOX_BASE_URL = "https://api.sandbox.paypal.com";
    private static final String PAYPAL_LIVE_BASE_URL = "https://api.paypal.com";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public PayPalPaymentAdapter() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String getProviderName() {
        return PaymentProvider.PAYPAL.name();
    }

    @Override
    public PaymentSessionResponse initialize(PaymentSessionRequest request, Map<String, Object> providerConfig) throws PaymentException {
        log.info("Initializing PayPal payment for tenant: {}, amount: {}", request.getTenantId(), request.getAmount());

        try {
            String accessToken = getAccessToken(providerConfig);
            String baseUrl = getBaseUrl(providerConfig);

            // Create PayPal Order
            Map<String, Object> orderRequest = new HashMap<>();
            orderRequest.put("intent", "CAPTURE");

            Map<String, Object> applicationContext = new HashMap<>();
            applicationContext.put("return_url", request.getReturnUrl() != null ? request.getReturnUrl() : "https://example.com/return");
            applicationContext.put("cancel_url", request.getCancelUrl() != null ? request.getCancelUrl() : "https://example.com/cancel");
            orderRequest.put("application_context", applicationContext);

            List<Map<String, Object>> purchaseUnits = new ArrayList<>();
            Map<String, Object> purchaseUnit = new HashMap<>();
            purchaseUnit.put("reference_id", request.getTenantId() + "-" + System.currentTimeMillis());

            Map<String, Object> amount = new HashMap<>();
            amount.put("currency_code", request.getCurrency());
            amount.put("value", String.valueOf(request.getAmount()));

            Map<String, Object> amountBreakdown = new HashMap<>();
            amountBreakdown.put("amount", amount);
            purchaseUnit.put("amount", amountBreakdown);

            if (request.getDescription() != null) {
                purchaseUnit.put("description", request.getDescription());
            }

            purchaseUnits.add(purchaseUnit);
            orderRequest.put("purchase_units", purchaseUnits);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(orderRequest, headers);
            ResponseEntity<Map> response = restTemplate.exchange(baseUrl + "/v2/checkout/orders", HttpMethod.POST, entity, Map.class);

            Map<String, Object> order = response.getBody();
            if (order == null) {
                throw new PaymentException("PAYPAL_ERROR", "Failed to create PayPal order");
            }

            PaymentSessionResponse paymentResponse = new PaymentSessionResponse();
            paymentResponse.setProvider(PaymentProvider.PAYPAL);
            paymentResponse.setAmount(request.getAmount());
            paymentResponse.setCurrency(request.getCurrency());
            paymentResponse.setStatus("PENDING");
            paymentResponse.setTransactionId((String) order.get("id"));

            // Find approval URL
            String approvalUrl = null;
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> links = (List<Map<String, Object>>) order.get("links");
            if (links != null) {
                for (Map<String, Object> link : links) {
                    if ("approve".equals(link.get("rel"))) {
                        approvalUrl = (String) link.get("href");
                        break;
                    }
                }
            }

            paymentResponse.setSessionUrl(approvalUrl);

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("paypalOrderId", order.get("id"));
            metadata.put("externalTransactionId", order.get("id"));
            metadata.put("status", order.get("status"));
            paymentResponse.setProviderMetadata(metadata);

            List<PaymentMethod> supportedMethods = new ArrayList<>();
            supportedMethods.add(PaymentMethod.CARD);
            supportedMethods.add(PaymentMethod.BANK_TRANSFER);
            paymentResponse.setSupportedMethods(supportedMethods);

            return paymentResponse;
        } catch (Exception e) {
            log.error("PayPal payment initialization failed", e);
            throw new PaymentException("PAYPAL_ERROR", "Failed to initialize PayPal payment: " + e.getMessage(), e);
        }
    }

    @Override
    public PaymentSessionResponse confirm(String transactionId, Map<String, Object> confirmationData, Map<String, Object> providerConfig)
        throws PaymentException {
        log.info("Confirming PayPal payment: {}", transactionId);

        try {
            String accessToken = getAccessToken(providerConfig);
            String baseUrl = getBaseUrl(providerConfig);

            // Capture the order
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                baseUrl + "/v2/checkout/orders/" + transactionId + "/capture",
                HttpMethod.POST,
                entity,
                Map.class
            );

            Map<String, Object> order = response.getBody();
            if (order == null) {
                throw new PaymentException("PAYPAL_ERROR", "Failed to capture PayPal order");
            }

            PaymentSessionResponse paymentResponse = new PaymentSessionResponse();
            paymentResponse.setProvider(PaymentProvider.PAYPAL);
            paymentResponse.setTransactionId((String) order.get("id"));
            paymentResponse.setStatus(mapPayPalStatus((String) order.get("status")));

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> purchaseUnits = (List<Map<String, Object>>) order.get("purchase_units");
            if (purchaseUnits != null && !purchaseUnits.isEmpty()) {
                Map<String, Object> purchaseUnit = purchaseUnits.get(0);
                Map<String, Object> amountMap = (Map<String, Object>) purchaseUnit.get("amount");
                if (amountMap != null) {
                    paymentResponse.setAmount(new BigDecimal((String) amountMap.get("value")));
                    paymentResponse.setCurrency((String) amountMap.get("currency_code"));
                }
            }

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("paypalOrderId", order.get("id"));
            metadata.put("externalTransactionId", order.get("id"));
            metadata.put("status", order.get("status"));
            paymentResponse.setProviderMetadata(metadata);

            return paymentResponse;
        } catch (Exception e) {
            log.error("PayPal payment confirmation failed", e);
            throw new PaymentException("PAYPAL_ERROR", "Failed to confirm PayPal payment: " + e.getMessage(), e);
        }
    }

    @Override
    public void cancel(String transactionId, Map<String, Object> providerConfig) throws PaymentException {
        log.info("Cancelling PayPal payment: {}", transactionId);
        // PayPal orders cannot be cancelled via API once created
        // They expire automatically if not approved
        log.warn("PayPal orders cannot be cancelled via API. Order will expire if not approved.");
    }

    @Override
    public RefundResponse refund(RefundRequest request, Map<String, Object> providerConfig) throws PaymentException {
        log.info("Processing PayPal refund for transaction: {}", request.getTransactionId());

        try {
            String accessToken = getAccessToken(providerConfig);
            String baseUrl = getBaseUrl(providerConfig);

            // First, get the capture ID from the order
            Map<String, Object> order = getOrder(request.getTransactionId(), accessToken, baseUrl);

            String captureId = null;
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> purchaseUnits = (List<Map<String, Object>>) order.get("purchase_units");
            if (purchaseUnits != null && !purchaseUnits.isEmpty()) {
                Map<String, Object> purchaseUnit = purchaseUnits.get(0);
                Map<String, Object> payments = (Map<String, Object>) purchaseUnit.get("payments");
                if (payments != null) {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> captures = (List<Map<String, Object>>) payments.get("captures");
                    if (captures != null && !captures.isEmpty()) {
                        captureId = (String) captures.get(0).get("id");
                    }
                }
            }

            if (captureId == null) {
                throw new PaymentException("NO_CAPTURE", "No capture found for PayPal order");
            }

            // Create refund request
            Map<String, Object> refundRequest = new HashMap<>();
            if (request.getAmount() != null) {
                Map<String, Object> amount = new HashMap<>();
                amount.put("currency_code", request.getCurrency() != null ? request.getCurrency() : "USD");
                amount.put("value", String.valueOf(request.getAmount()));
                refundRequest.put("amount", amount);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(refundRequest, headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                baseUrl + "/v2/payments/captures/" + captureId + "/refund",
                HttpMethod.POST,
                entity,
                Map.class
            );

            Map<String, Object> refund = response.getBody();
            if (refund == null) {
                throw new PaymentException("PAYPAL_ERROR", "Failed to create PayPal refund");
            }

            RefundResponse refundResponse = new RefundResponse();
            refundResponse.setRefundId((String) refund.get("id"));
            refundResponse.setTransactionId(request.getTransactionId());

            Map<String, Object> refundAmount = (Map<String, Object>) refund.get("amount");
            if (refundAmount != null) {
                refundResponse.setAmount(new BigDecimal((String) refundAmount.get("value")));
                refundResponse.setCurrency((String) refundAmount.get("currency_code"));
            }

            refundResponse.setStatus((String) refund.get("status"));
            refundResponse.setReason(request.getReason());
            refundResponse.setProcessedAt(ZonedDateTime.now());

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("paypalRefundId", refund.get("id"));
            metadata.put("paypalCaptureId", captureId);
            refundResponse.setProviderMetadata(metadata);

            return refundResponse;
        } catch (Exception e) {
            log.error("PayPal refund failed", e);
            throw new PaymentException("PAYPAL_ERROR", "Failed to process PayPal refund: " + e.getMessage(), e);
        }
    }

    @Override
    public PaymentSessionResponse getStatus(String transactionId, Map<String, Object> providerConfig) throws PaymentException {
        log.debug("Getting PayPal payment status: {}", transactionId);

        try {
            String accessToken = getAccessToken(providerConfig);
            String baseUrl = getBaseUrl(providerConfig);

            Map<String, Object> order = getOrder(transactionId, accessToken, baseUrl);

            PaymentSessionResponse response = new PaymentSessionResponse();
            response.setProvider(PaymentProvider.PAYPAL);
            response.setTransactionId((String) order.get("id"));
            response.setStatus(mapPayPalStatus((String) order.get("status")));

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> purchaseUnits = (List<Map<String, Object>>) order.get("purchase_units");
            if (purchaseUnits != null && !purchaseUnits.isEmpty()) {
                Map<String, Object> purchaseUnit = purchaseUnits.get(0);
                Map<String, Object> amountMap = (Map<String, Object>) purchaseUnit.get("amount");
                if (amountMap != null) {
                    response.setAmount(new BigDecimal((String) amountMap.get("value")));
                    response.setCurrency((String) amountMap.get("currency_code"));
                }
            }

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("paypalOrderId", order.get("id"));
            metadata.put("externalTransactionId", order.get("id"));
            metadata.put("status", order.get("status"));
            response.setProviderMetadata(metadata);

            return response;
        } catch (Exception e) {
            log.error("Failed to get PayPal payment status", e);
            throw new PaymentException("PAYPAL_ERROR", "Failed to get payment status: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> handleWebhook(String payload, String signature, Map<String, Object> providerConfig) throws PaymentException {
        log.info("Handling PayPal webhook");

        try {
            Map<String, Object> event = objectMapper.readValue(payload, Map.class);

            String eventType = (String) event.get("event_type");
            @SuppressWarnings("unchecked")
            Map<String, Object> resource = (Map<String, Object>) event.get("resource");

            Map<String, Object> result = new HashMap<>();
            result.put("eventType", eventType);
            result.put("processed", true);

            // Handle different event types
            switch (eventType) {
                case "PAYMENT.CAPTURE.COMPLETED":
                    handleCaptureCompleted(resource, result);
                    break;
                case "PAYMENT.CAPTURE.DENIED":
                    handleCaptureDenied(resource, result);
                    break;
                case "PAYMENT.CAPTURE.REFUNDED":
                    handleRefunded(resource, result);
                    break;
                default:
                    log.debug("Unhandled PayPal webhook event type: {}", eventType);
                    result.put("processed", false);
            }

            return result;
        } catch (Exception e) {
            log.error("PayPal webhook processing failed", e);
            throw new PaymentException("WEBHOOK_ERROR", "Failed to process webhook: " + e.getMessage(), e);
        }
    }

    @Override
    public List<PaymentMethod> supportedMethods(Map<String, Object> providerConfig) {
        List<PaymentMethod> methods = new ArrayList<>();
        methods.add(PaymentMethod.CARD);
        methods.add(PaymentMethod.BANK_TRANSFER);
        return methods;
    }

    /**
     * Get PayPal access token for API authentication.
     */
    private String getAccessToken(Map<String, Object> providerConfig) throws PaymentException {
        String clientId = (String) providerConfig.get("apiKey");
        String clientSecret = (String) providerConfig.get("secretKey");

        if (clientId == null || clientSecret == null) {
            throw new PaymentException("PAYPAL_CONFIG_ERROR", "PayPal credentials not configured");
        }

        Boolean isSandbox = (Boolean) providerConfig.getOrDefault("sandbox", true);
        String baseUrl = isSandbox ? PAYPAL_SANDBOX_BASE_URL : PAYPAL_LIVE_BASE_URL;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, clientSecret);

        HttpEntity<String> entity = new HttpEntity<>("grant_type=client_credentials", headers);
        ResponseEntity<Map> response = restTemplate.exchange(baseUrl + "/v1/oauth2/token", HttpMethod.POST, entity, Map.class);

        Map<String, Object> tokenResponse = response.getBody();
        if (tokenResponse == null || !tokenResponse.containsKey("access_token")) {
            throw new PaymentException("PAYPAL_AUTH_ERROR", "Failed to obtain PayPal access token");
        }

        return (String) tokenResponse.get("access_token");
    }

    /**
     * Get base URL based on environment.
     */
    private String getBaseUrl(Map<String, Object> providerConfig) {
        Boolean isSandbox = (Boolean) providerConfig.getOrDefault("sandbox", true);
        return isSandbox ? PAYPAL_SANDBOX_BASE_URL : PAYPAL_LIVE_BASE_URL;
    }

    /**
     * Get PayPal order by ID.
     */
    private Map<String, Object> getOrder(String orderId, String accessToken, String baseUrl) throws PaymentException {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(baseUrl + "/v2/checkout/orders/" + orderId, HttpMethod.GET, entity, Map.class);

        Map<String, Object> order = response.getBody();
        if (order == null) {
            throw new PaymentException("PAYPAL_ERROR", "Failed to retrieve PayPal order");
        }

        return order;
    }

    /**
     * Map PayPal order status to our status.
     */
    private String mapPayPalStatus(String paypalStatus) {
        return switch (paypalStatus) {
            case "COMPLETED" -> "COMPLETED";
            case "APPROVED" -> "PENDING";
            case "CREATED" -> "PENDING";
            case "SAVED" -> "PENDING";
            case "VOIDED" -> "CANCELLED";
            default -> "PENDING";
        };
    }

    /**
     * Handle PAYMENT.CAPTURE.COMPLETED webhook event.
     */
    private void handleCaptureCompleted(Map<String, Object> resource, Map<String, Object> result) {
        result.put("captureId", resource.get("id"));
        if (resource.get("amount") != null) {
            @SuppressWarnings("unchecked")
            Map<String, Object> amount = (Map<String, Object>) resource.get("amount");
            result.put("amount", amount.get("value"));
            result.put("currency", amount.get("currency_code"));
        }
    }

    /**
     * Handle PAYMENT.CAPTURE.DENIED webhook event.
     */
    private void handleCaptureDenied(Map<String, Object> resource, Map<String, Object> result) {
        result.put("captureId", resource.get("id"));
        result.put("failureReason", resource.get("reason_code"));
    }

    /**
     * Handle PAYMENT.CAPTURE.REFUNDED webhook event.
     */
    private void handleRefunded(Map<String, Object> resource, Map<String, Object> result) {
        result.put("captureId", resource.get("id"));
        if (resource.get("amount") != null) {
            @SuppressWarnings("unchecked")
            Map<String, Object> amount = (Map<String, Object>) resource.get("amount");
            result.put("refundAmount", amount.get("value"));
        }
    }
}
