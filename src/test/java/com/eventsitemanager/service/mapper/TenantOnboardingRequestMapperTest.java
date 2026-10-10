package com.eventsitemanager.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.domain.TenantOnboardingRequest;
import com.eventsitemanager.domain.enumeration.OnboardingRequestStatus;
import com.eventsitemanager.service.dto.TenantOnboardingRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TenantOnboardingRequestMapperTest {

    private TenantOnboardingRequestMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new TenantOnboardingRequestMapperImpl();
    }

    @Test
    void partialUpdateDoesNotTouchWorkflowFields() {
        TenantOnboardingRequest entity = new TenantOnboardingRequest();
        entity.setRequestCode("ONB-20261010-AAAAA");
        entity.setStatus(OnboardingRequestStatus.PENDING);
        entity.setOrganizationName("Old");

        TenantOnboardingRequestDTO patch = new TenantOnboardingRequestDTO();
        patch.setOrganizationName("New");
        patch.setStatus(OnboardingRequestStatus.APPROVED);
        patch.setRequestCode("HACKED");
        patch.setProvisioningResult("{}");

        mapper.partialUpdate(entity, patch);

        assertThat(entity.getOrganizationName()).isEqualTo("New");
        assertThat(entity.getStatus()).isEqualTo(OnboardingRequestStatus.PENDING);
        assertThat(entity.getRequestCode()).isEqualTo("ONB-20261010-AAAAA");
        assertThat(entity.getProvisioningResult()).isNull();
    }
}
