package com.nextjstemplate.service.payment.event;

import com.nextjstemplate.domain.UserPaymentTransaction;
import org.springframework.context.ApplicationEvent;

/**
 * Event published when a payment transaction succeeds.
 * This event triggers automatic ticket generation, QR code creation, and email sending for ticket purchases.
 */
public class PaymentSuccessEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private final UserPaymentTransaction paymentTransaction;
    private final String stripePaymentIntentId;

    public PaymentSuccessEvent(Object source, UserPaymentTransaction paymentTransaction, String stripePaymentIntentId) {
        super(source);
        this.paymentTransaction = paymentTransaction;
        this.stripePaymentIntentId = stripePaymentIntentId;
    }

    public UserPaymentTransaction getPaymentTransaction() {
        return paymentTransaction;
    }

    public String getStripePaymentIntentId() {
        return stripePaymentIntentId;
    }
}
