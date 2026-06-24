package com.eventsitemanager.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class BulkOperationLogMapperTest {

    private BulkOperationLogMapper bulkOperationLogMapper;

    @BeforeEach
    public void setUp() {
        bulkOperationLogMapper = new BulkOperationLogMapperImpl();
    }
}
