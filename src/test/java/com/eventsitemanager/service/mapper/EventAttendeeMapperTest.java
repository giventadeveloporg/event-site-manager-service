package com.eventsitemanager.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class EventAttendeeMapperTest {

    private EventAttendeeMapper eventAttendeeMapper;

    @BeforeEach
    public void setUp() {
        eventAttendeeMapper = new EventAttendeeMapperImpl();
    }
}
