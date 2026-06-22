package com.eventsitemanager.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class EventPollMapperTest {

    private EventPollMapper eventPollMapper;

    @BeforeEach
    public void setUp() {
        eventPollMapper = new EventPollMapperImpl();
    }
}
