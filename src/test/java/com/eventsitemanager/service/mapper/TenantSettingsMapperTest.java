package com.eventsitemanager.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class TenantSettingsMapperTest {

    private TenantSettingsMapper tenantSettingsMapper;

    @BeforeEach
    public void setUp() {
        tenantSettingsMapper = new TenantSettingsMapperImpl();
    }
}
