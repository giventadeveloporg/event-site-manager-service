package com.eventsitemanager.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class UserPaymentTransactionMapperTest {

    private UserPaymentTransactionMapper userPaymentTransactionMapper;

    @BeforeEach
    public void setUp() {
        userPaymentTransactionMapper = new UserPaymentTransactionMapperImpl();
    }
}
