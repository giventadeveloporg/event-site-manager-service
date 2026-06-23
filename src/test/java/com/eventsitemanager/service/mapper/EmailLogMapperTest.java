package com.eventsitemanager.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class EmailLogMapperTest {

    private EmailLogMapper emailLogMapper;

    @BeforeEach
    public void setUp() {
        emailLogMapper = new EmailLogMapperImpl();
    }
}
