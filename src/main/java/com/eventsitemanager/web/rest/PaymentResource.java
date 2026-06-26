package com.eventsitemanager.web.rest;

import com.eventsitemanager.config.InboundWebhookGuard;
import com.eventsitemanager.domain.enumeration.PaymentProvider;
import com.eventsitemanager.service.payment.PaymentException;
import com.eventsitemanager.service.payment.dto.PaymentSessionRequest;
import com.eventsitemanager.service.payment.dto.PaymentSessionResponse;
import com.eventsitemanager.service.payment.dto.RefundRequest;
import com.eventsitemanager.service.payment.dto.RefundResponse;
import com.eventsitemanager.service.payment.orchestration.PaymentOrchestrationService;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for payment operations.
 * This is the entry point for all payment-related API endpoints.
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentResource {

    private static final Logger log = LoggerFactory.getLogger(PaymentResource.class);

    private final PaymentOrchestrationService paymentOrchestrationService;
    private final InboundWebhookGuard inboundWebhookGuard;

    public PaymentResource(PaymentOrchestrationService paymentOrchestrationService, InboundWebhookGuard inboundWebhookGuard) {
        this.paymentOrchestrationService = paymentOrchestrationService;
        this.inboundWebhookGuard = inboundWebhookGuard;
    }

    /**
     * {@code POST  /api/payments/initialize} : Initialize a payment session.
     *
     * @param request the payment session request
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the payment session response
     */
    @PostMapping("/initialize")
    public ResponseEntity<PaymentSessionResponse> initializePayment(@Valid @RequestBody PaymentSessionRequest request) {
        log.debug("REST request to initialize payment : {}", request);
        try {
            PaymentSessionResponse response = paymentOrchestrationService.initialize(request);
            return ResponseEntity.ok(response);
        } catch (PaymentException e) {
            log.error("Payment initialization failed", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * {@code GET  /api/payments/{transactionId}} : Get payment status.
     *
     * @param transactionId the transaction ID
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the payment session response,
     *         or error response with appropriate HTTP status code
     */
    @GetMapping("/{transactionId}")
    public ResponseEntity<?> getPaymentStatus(@PathVariable String transactionId) {
        log.debug("REST request to get payment status : {}", transactionId);
        try {
            PaymentSessionResponse response = paymentOrchestrationService.getStatus(transactionId);
            return ResponseEntity.ok(response);
        } catch (PaymentException e) {
            log.error("Failed to get payment status for transaction: {}", transactionId, e);

            // Create error response map
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            errorResponse.put("errorCode", e.getErrorCode());
            errorResponse.put("transactionId", transactionId);

            // Add error details if available
            if (e.getErrorDetails() != null) {
                errorResponse.put("details", e.getErrorDetails());
            }

            // Map error codes to appropriate HTTP status codes
            HttpStatus status = mapErrorCodeToHttpStatus(e.getErrorCode(), e.getMessage());

            return ResponseEntity.status(status).body(errorResponse);
        } catch (Exception e) {
            log.error("Unexpected error while getting payment status for transaction: {}", transactionId, e);

            // Create error response for unexpected errors
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to get payment status");
            errorResponse.put("errorCode", "INTERNAL_ERROR");
            errorResponse.put("transactionId", transactionId);
            errorResponse.put("details", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Maps PaymentException error codes to appropriate HTTP status codes.
     *
     * @param errorCode the error code from PaymentException
     * @param message the error message
     * @return the appropriate HTTP status code
     */
    private HttpStatus mapErrorCodeToHttpStatus(String errorCode, String message) {
        // Transaction not found errors - only return 404 if transaction truly doesn't exist
        if (errorCode.equals("TRANSACTION_NOT_FOUND") || (message != null && message.toLowerCase().contains("transaction not found"))) {
            return HttpStatus.NOT_FOUND;
        }

        // Invalid transaction ID format
        if (errorCode.equals("INVALID_TRANSACTION_ID")) {
            return HttpStatus.BAD_REQUEST;
        }

        // Note: STRIPE_PAYMENT_INTENT_NOT_FOUND should no longer occur since we return database status
        // But if it does, treat as internal error since it's unexpected
        if (errorCode.equals("STRIPE_PAYMENT_INTENT_NOT_FOUND")) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        // Configuration errors
        if (
            errorCode.equals("STRIPE_CONFIG_ERROR") ||
            errorCode.equals("GIVEBUTTER_CONFIG_ERROR") ||
            errorCode.equals("PROVIDER_CONFIG_NOT_FOUND")
        ) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        // Provider not available
        if (errorCode.equals("PROVIDER_NOT_AVAILABLE")) {
            return HttpStatus.SERVICE_UNAVAILABLE;
        }

        // Stripe API errors - should rarely occur since we fall back to database status
        if (errorCode.equals("STRIPE_ERROR")) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        // Givebutter API errors
        if (errorCode.equals("GIVEBUTTER_API_ERROR") || errorCode.equals("GIVEBUTTER_ERROR")) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        // Default to internal server error for other payment errors
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    /**
     * {@code POST  /api/payments/{transactionId}/confirm} : Confirm a payment.
     *
     * @param transactionId the transaction ID
     * @param confirmationData the confirmation data
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the payment session response
     */
    @PostMapping("/{transactionId}/confirm")
    public ResponseEntity<PaymentSessionResponse> confirmPayment(
        @PathVariable String transactionId,
        @RequestBody Map<String, Object> confirmationData
    ) {
        log.debug("REST request to confirm payment : {}", transactionId);
        try {
            PaymentSessionResponse response = paymentOrchestrationService.confirm(transactionId, confirmationData);
            return ResponseEntity.ok(response);
        } catch (PaymentException e) {
            log.error("Payment confirmation failed", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * {@code POST  /api/payments/{transactionId}/cancel} : Cancel a payment.
     *
     * @param transactionId the transaction ID
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}
     */
    @PostMapping("/{transactionId}/cancel")
    public ResponseEntity<Void> cancelPayment(@PathVariable String transactionId) {
        log.debug("REST request to cancel payment : {}", transactionId);
        try {
            paymentOrchestrationService.cancel(transactionId);
            return ResponseEntity.noContent().build();
        } catch (PaymentException e) {
            log.error("Payment cancellation failed", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * {@code POST  /api/payments/{transactionId}/refund} : Process a refund.
     *
     * @param transactionId the transaction ID
     * @param request the refund request
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the refund response
     */
    @PostMapping("/{transactionId}/refund")
    public ResponseEntity<RefundResponse> refundPayment(@PathVariable String transactionId, @Valid @RequestBody RefundRequest request) {
        log.debug("REST request to refund payment : {}", transactionId);
        request.setTransactionId(transactionId);
        try {
            RefundResponse response = paymentOrchestrationService.refund(request);
            return ResponseEntity.ok(response);
        } catch (PaymentException e) {
            log.error("Refund failed", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * {@code POST  /api/webhooks/payments/{provider}} : Handle payment webhook.
     *
     * Supports both Stripe standard header (Stripe-Signature) and custom header (X-Webhook-Signature).
     * For Stripe, the webhook endpoint should be configured as: POST /api/payments/webhooks/stripe
     *
     * @param provider the payment provider
     * @param payload the webhook payload
     * @param stripeSignature the Stripe webhook signature (Stripe-Signature header)
     * @param customSignature the custom webhook signature (X-Webhook-Signature header)
     * @param tenantId the tenant ID (from header or query param)
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the processed event data
     */
    @PostMapping("/webhooks/{provider}")
    public ResponseEntity<Map<String, Object>> handleWebhook(
        @PathVariable String provider,
        @RequestBody String payload,
        @RequestHeader(value = "Stripe-Signature", required = false) String stripeSignature,
        @RequestHeader(value = "X-Webhook-Signature", required = false) String customSignature,
        @RequestHeader(value = "X-Tenant-Id", required = false) String tenantId,
        @RequestParam(value = "tenantId", required = false) String tenantIdParam
    ) {
        if (!inboundWebhookGuard.isPaymentInboundEnabled()) {
            log.info("[PaymentResource] Payment inbound webhooks are disabled; rejecting provider: {}", provider);
            return inboundWebhookGuard.paymentInboundDisabledResponse();
        }

        log.info("[PaymentResource] Received webhook for provider: {}", provider);
        String effectiveTenantId = tenantId != null ? tenantId : tenantIdParam;
        if (effectiveTenantId == null) {
            log.warn("[PaymentResource] Missing tenantId in webhook request");
            return ResponseEntity.badRequest().build();
        }

        // Use Stripe signature if available, otherwise fall back to custom signature
        String signature = stripeSignature != null ? stripeSignature : customSignature;
        if (signature == null || signature.isEmpty()) {
            log.warn("[PaymentResource] Missing webhook signature header");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            PaymentProvider paymentProvider = PaymentProvider.valueOf(provider.toUpperCase());
            Map<String, Object> result = paymentOrchestrationService.handleWebhook(paymentProvider, payload, signature, effectiveTenantId);
            log.info("[PaymentResource] Webhook processed successfully for provider: {}, eventType: {}", provider, result.get("eventType"));
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            log.error("[PaymentResource] Invalid provider: {}", provider);
            return ResponseEntity.badRequest().build();
        } catch (PaymentException e) {
            log.error("[PaymentResource] Webhook processing failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * {@code POST  /api/payments/providers/stripe/instant-checkout} : Create Stripe Instant Checkout (ACP) session.
     *
     * @param request the payment session request
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the payment session response
     */
    @PostMapping("/providers/stripe/instant-checkout")
    public ResponseEntity<PaymentSessionResponse> createStripeInstantCheckout(@Valid @RequestBody PaymentSessionRequest request) {
        log.debug("REST request to create Stripe Instant Checkout session : {}", request);
        try {
            // Check if ACP is supported for this tenant
            // This would be checked via PaymentProviderConfigService
            PaymentSessionResponse response = paymentOrchestrationService.initialize(request);
            return ResponseEntity.ok(response);
        } catch (PaymentException e) {
            log.error("Stripe Instant Checkout creation failed", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * {@code POST  /api/payments/zeffy/initialize} : Initialize Zeffy donation.
     *
     * @param request the payment session request
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the payment session response
     */
    @PostMapping("/zeffy/initialize")
    public ResponseEntity<PaymentSessionResponse> initializeZeffy(@Valid @RequestBody PaymentSessionRequest request) {
        log.debug("REST request to initialize Zeffy donation : {}", request);
        try {
            PaymentSessionResponse response = paymentOrchestrationService.initialize(request);
            return ResponseEntity.ok(response);
        } catch (PaymentException e) {
            log.error("Zeffy initialization failed", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * {@code POST  /api/payments/zelle/manual} : Create Zelle manual payment transaction.
     *
     * @param request the payment session request
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the payment session response
     */
    @PostMapping("/zelle/manual")
    public ResponseEntity<PaymentSessionResponse> createZelleManual(@Valid @RequestBody PaymentSessionRequest request) {
        log.debug("REST request to create Zelle manual payment : {}", request);
        try {
            PaymentSessionResponse response = paymentOrchestrationService.initialize(request);
            return ResponseEntity.ok(response);
        } catch (PaymentException e) {
            log.error("Zelle manual payment creation failed", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
