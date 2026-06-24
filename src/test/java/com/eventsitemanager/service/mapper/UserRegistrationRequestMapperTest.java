package com.eventsitemanager.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class UserRegistrationRequestMapperTest {

    private UserRegistrationRequestMapper userRegistrationRequestMapper;

    @BeforeEach
    public void setUp() {
        userRegistrationRequestMapper = new UserRegistrationRequestMapperImpl();
    }
}
