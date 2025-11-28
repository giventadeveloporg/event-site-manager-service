package com.nextjstemplate.service.payment.event;

import com.nextjstemplate.domain.UserPaymentTransaction;
import org.springframework.context.ApplicationEvent;

/**
 * Event published when a payment transaction succeeds.
 * This event triggers automatic ticket generation, QR code creation, and email sending for ticket purchases.
 *
 * IMPORTANT: This event carries the tenantId explicitly because async event handlers
 * run in separate threads where TenantContext (thread-local) is not propagated.
 * The handler MUST use the tenantId from this event, not from TenantContext.
 */
public class PaymentSuccessEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private final UserPaymentTransaction paymentTransaction;
    private final String stripePaymentIntentId;
    private final String tenantId;

    public PaymentSuccessEvent(Object source, UserPaymentTransaction paymentTransaction, String stripePaymentIntentId, String tenantId) {
        super(source);
        this.paymentTransaction = paymentTransaction;
        this.stripePaymentIntentId = stripePaymentIntentId;
        this.tenantId = tenantId;
    }

    public UserPaymentTransaction getPaymentTransaction() {
        return paymentTransaction;
    }

    public String getStripePaymentIntentId() {
        return stripePaymentIntentId;
    }

    /**
     * Returns the tenant ID associated with this payment.
     * CRITICAL: Use this value to set TenantContext in async handlers,
     * as thread-local context is NOT propagated to async threads.
     */
    public String getTenantId() {
        return tenantId;
    }
}
