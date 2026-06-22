package com.eventsitemanager.domain;

import static com.eventsitemanager.domain.TenantOrganizationTestSamples.*;
import static com.eventsitemanager.domain.TenantSettingsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.eventsitemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TenantSettingsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TenantSettings.class);
        TenantSettings tenantSettings1 = getTenantSettingsSample1();
        TenantSettings tenantSettings2 = new TenantSettings();
        assertThat(tenantSettings1).isNotEqualTo(tenantSettings2);

        tenantSettings2.setId(tenantSettings1.getId());
        assertThat(tenantSettings1).isEqualTo(tenantSettings2);

        tenantSettings2 = getTenantSettingsSample2();
        assertThat(tenantSettings1).isNotEqualTo(tenantSettings2);
    }
    /* @Test
    void tenantOrganizationTest() throws Exception {
        TenantSettings tenantSettings = getTenantSettingsRandomSampleGenerator();
        TenantOrganization tenantOrganizationBack = getTenantOrganizationRandomSampleGenerator();

        tenantSettings.setTenantOrganization(tenantOrganizationBack);
        assertThat(tenantSettings.getTenantOrganization()).isEqualTo(tenantOrganizationBack);

        tenantSettings.tenantOrganization(null);
        assertThat(tenantSettings.getTenantOrganization()).isNull();
    }*/
}
