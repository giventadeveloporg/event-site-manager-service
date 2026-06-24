package com.eventsitemanager.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class TenantOrganizationMapperTest {

    private TenantOrganizationMapper tenantOrganizationMapper;

    @BeforeEach
    public void setUp() {
        tenantOrganizationMapper = new TenantOrganizationMapperImpl();
    }
}
