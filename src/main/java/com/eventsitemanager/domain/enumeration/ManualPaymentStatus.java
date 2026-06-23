package com.eventsitemanager.domain.enumeration;

/**
 * The ManualPaymentStatus enumeration.
 * Matches check constraints on {@code manual_payment_request.status} and {@code manual_payment_summary_report.status}.
 */
public enum ManualPaymentStatus {
    REQUESTED,
    RECEIVED,
    VOIDED,
    REFUNDED,
}
