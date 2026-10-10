package com.eventsitemanager.domain.enumeration;

/**
 * Who registers / controls DNS for the requested hostname. Must match chk_tenant_onboarding_request__domain_ownership.
 */
public enum DomainOwnership {
    PLATFORM_REGISTERS,
    CUSTOMER_REGISTRAR,
    ALREADY_IN_PLATFORM,
}
