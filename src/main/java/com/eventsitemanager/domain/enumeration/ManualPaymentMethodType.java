package com.eventsitemanager.domain.enumeration;

/**
 * The ManualPaymentMethodType enumeration.
 * Matches PostgreSQL enum {@code manual_payment_method_type}.
 */
public enum ManualPaymentMethodType {
    ZELLE_MANUAL,
    VENMO_MANUAL,
    CASH_APP_MANUAL,
    CASH,
    CHECK,
    OTHER_MANUAL,
}
