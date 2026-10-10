package com.eventsitemanager.domain.enumeration;

/**
 * Lifecycle of a {@code tenant_onboarding_request}. Must match chk_tenant_onboarding_request__status.
 */
public enum OnboardingRequestStatus {
    PENDING,
    APPROVED,
    REJECTED,
    FAILED,
    CANCELLED,
}
