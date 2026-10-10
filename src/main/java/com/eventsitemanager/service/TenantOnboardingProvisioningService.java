package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.TenantOnboardingApproveDTO;
import com.eventsitemanager.service.dto.TenantOnboardingRequestDTO;

/**
 * Approves a {@link com.eventsitemanager.domain.TenantOnboardingRequest} by creating, in one database transaction:
 * tenant_organization, tenant_settings, CONTACT / INFO / NOREPLY tenant_email_addresses, satellite_domain and
 * cloned ADMIN / SUPER_ADMIN user_profile rows. Either everything is created or nothing is.
 */
public interface TenantOnboardingProvisioningService {
    TenantOnboardingRequestDTO approve(Long requestId, TenantOnboardingApproveDTO approveDTO);
}
