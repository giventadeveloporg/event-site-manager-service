package com.nextjstemplate.domain.enumeration;

/**
 * The PaymentFlowMode enumeration.
 * Event-level configuration for enabling Stripe and/or Manual flows.
 */
public enum PaymentFlowMode {
    STRIPE_ONLY,
    MANUAL_ONLY,
    HYBRID,
}
