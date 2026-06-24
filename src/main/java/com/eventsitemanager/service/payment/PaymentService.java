package com.eventsitemanager.service.payment;

import com.eventsitemanager.domain.enumeration.PaymentMethod;
import com.eventsitemanager.service.payment.dto.PaymentSessionRequest;
import com.eventsitemanager.service.payment.dto.PaymentSessionResponse;
import com.eventsitemanager.service.payment.dto.RefundRequest;
import com.eventsitemanager.service.payment.dto.RefundResponse;
import java.util.List;
import java.util.Map;

/**
 * Core interface for payment provider adapters.
 * All payment providers (Stripe, PayPal, Zeffy, etc.) must implement this interface.
 */
public interface PaymentService {
    /**
     * Initialize a payment session.
     * Creates a payment session with the provider and returns session details.
     *
     * @param request Payment session request containing amount, currency, metadata, etc.
     * @param providerConfig Provider configuration for the tenant
     * @return Payment session response with clientSecret, sessionUrl, or other provider-specific data
     * @throws PaymentException if initialization fails
     */
    PaymentSessionResponse initialize(PaymentSessionRequest request, Map<String, Object> providerConfig) throws PaymentException;

    /**
     * Confirm a payment.
     * Used for payments that require confirmation after initialization.
     *
     * @param transactionId The payment transaction ID
     * @param confirmationData Provider-specific confirmation data (e.g., payment method ID, 3DS result)
     * @param providerConfig Provider configuration for the tenant
     * @return Updated payment session response
     * @throws PaymentException if confirmation fails
     */
    PaymentSessionResponse confirm(String transactionId, Map<String, Object> confirmationData, Map<String, Object> providerConfig)
        throws PaymentException;

    /**
     * Cancel a payment.
     * Cancels a pending payment before it's confirmed.
     *
     * @param transactionId The payment transaction ID
     * @param providerConfig Provider configuration for the tenant
     * @throws PaymentException if cancellation fails
     */
    void cancel(String transactionId, Map<String, Object> providerConfig) throws PaymentException;

    /**
     * Process a refund.
     * Issues a full or partial refund for a completed payment.
     *
     * @param request Refund request containing transaction ID, amount, reason, etc.
     * @param providerConfig Provider configuration for the tenant
     * @return Refund response with refund ID and status
     * @throws PaymentException if refund fails
     */
    RefundResponse refund(RefundRequest request, Map<String, Object> providerConfig) throws PaymentException;

    /**
     * Get payment status.
     * Retrieves the current status of a payment transaction.
     *
     * @param transactionId The payment transaction ID
     * @param providerConfig Provider configuration for the tenant
     * @return Payment session response with current status
     * @throws PaymentException if status retrieval fails
     */
    PaymentSessionResponse getStatus(String transactionId, Map<String, Object> providerConfig) throws PaymentException;

    /**
     * Handle webhook event.
     * Processes webhook events from the payment provider.
     *
     * @param payload Raw webhook payload
     * @param signature Webhook signature for verification
     * @param providerConfig Provider configuration for the tenant
     * @return Map containing processed event data
     * @throws PaymentException if webhook processing fails
     */
    Map<String, Object> handleWebhook(String payload, String signature, Map<String, Object> providerConfig) throws PaymentException;

    /**
     * Get supported payment methods.
     * Returns list of payment methods supported by this provider.
     *
     * @param providerConfig Provider configuration for the tenant
     * @return List of supported payment methods
     */
    List<PaymentMethod> supportedMethods(Map<String, Object> providerConfig);

    /**
     * Get provider name.
     * Returns the name of this payment provider.
     *
     * @return Provider name
     */
    String getProviderName();
}
