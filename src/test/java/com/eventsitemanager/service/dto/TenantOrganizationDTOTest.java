package com.eventsitemanager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TenantOrganizationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TenantOrganizationDTO.class);
        TenantOrganizationDTO tenantOrganizationDTO1 = new TenantOrganizationDTO();
        tenantOrganizationDTO1.setId(1L);
        TenantOrganizationDTO tenantOrganizationDTO2 = new TenantOrganizationDTO();
        assertThat(tenantOrganizationDTO1).isNotEqualTo(tenantOrganizationDTO2);
        tenantOrganizationDTO2.setId(tenantOrganizationDTO1.getId());
        assertThat(tenantOrganizationDTO1).isEqualTo(tenantOrganizationDTO2);
        tenantOrganizationDTO2.setId(2L);
        assertThat(tenantOrganizationDTO1).isNotEqualTo(tenantOrganizationDTO2);
        tenantOrganizationDTO1.setId(null);
        assertThat(tenantOrganizationDTO1).isNotEqualTo(tenantOrganizationDTO2);
    }
}
