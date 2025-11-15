package com.nextjstemplate.domain.enumeration;

/**
 * The PaymentUseCase enumeration.
 * Defines the use cases for payment transactions.
 *
 * <p>Valid values:</p>
 * <ul>
 *   <li>{@code TICKET_SALE} - For ticket sales/event admissions</li>
 *   <li>{@code DONATION} - For general donations</li>
 *   <li>{@code DONATION_CEFI} - For CEFI-specific donations</li>
 *   <li>{@code DONATION_ZERO_FEE} - For zero-fee donations</li>
 *   <li>{@code OFFERING} - For offerings</li>
 *   <li>{@code MEMBERSHIP_SUBSCRIPTION} - For membership subscriptions</li>
 * </ul>
 *
 * <p>Note: A {@code NULL} value in the database indicates a general-purpose provider
 * that can handle all payment use cases.</p>
 */
public enum PaymentUseCase {
    /** Payment use case for ticket sales and event admissions */
    TICKET_SALE,

    /** Payment use case for general donations */
    DONATION,

    /** Payment use case for CEFI-specific donations */
    DONATION_CEFI,

    /** Payment use case for zero-fee donations */
    DONATION_ZERO_FEE,

    /** Payment use case for offerings */
    OFFERING,

    /** Payment use case for membership subscriptions */
    MEMBERSHIP_SUBSCRIPTION,
}
