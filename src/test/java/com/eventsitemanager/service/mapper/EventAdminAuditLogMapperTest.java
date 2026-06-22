package com.eventsitemanager.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class EventAdminAuditLogMapperTest {

    private EventAdminAuditLogMapper eventAdminAuditLogMapper;

    @BeforeEach
    public void setUp() {
        eventAdminAuditLogMapper = new EventAdminAuditLogMapperImpl();
    }
}
