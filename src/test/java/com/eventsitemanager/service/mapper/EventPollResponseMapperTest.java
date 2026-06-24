package com.eventsitemanager.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class EventPollResponseMapperTest {

    private EventPollResponseMapper eventPollResponseMapper;

    @BeforeEach
    public void setUp() {
        eventPollResponseMapper = new EventPollResponseMapperImpl();
    }
}
