package com.nextjstemplate.domain;

import static com.nextjstemplate.domain.TenantOrganizationTestSamples.*;
import static com.nextjstemplate.domain.TenantSettingsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nextjstemplate.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TenantOrganizationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TenantOrganization.class);
        TenantOrganization tenantOrganization1 = getTenantOrganizationSample1();
        TenantOrganization tenantOrganization2 = new TenantOrganization();
        assertThat(tenantOrganization1).isNotEqualTo(tenantOrganization2);

        tenantOrganization2.setId(tenantOrganization1.getId());
        assertThat(tenantOrganization1).isEqualTo(tenantOrganization2);

        tenantOrganization2 = getTenantOrganizationSample2();
        assertThat(tenantOrganization1).isNotEqualTo(tenantOrganization2);
    }
    /*  @Test
    void tenantSettingsTest() throws Exception {
        TenantOrganization tenantOrganization = getTenantOrganizationRandomSampleGenerator();
        TenantSettings tenantSettingsBack = getTenantSettingsRandomSampleGenerator();

        tenantOrganization.setTenantSettings(tenantSettingsBack);
        assertThat(tenantOrganization.getTenantSettings()).isEqualTo(tenantSettingsBack);
        assertThat(tenantSettingsBack.getTenantOrganization()).isEqualTo(tenantOrganization);

        tenantOrganization.tenantSettings(null);
        assertThat(tenantOrganization.getTenantSettings()).isNull();
        assertThat(tenantSettingsBack.getTenantOrganization()).isNull();
    }*/
}
