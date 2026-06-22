package com.eventsitemanager.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class EventCalendarEntryMapperTest {

    private EventCalendarEntryMapper eventCalendarEntryMapper;

    @BeforeEach
    public void setUp() {
        eventCalendarEntryMapper = new EventCalendarEntryMapperImpl();
    }
}
