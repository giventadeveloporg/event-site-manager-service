package com.eventsitemanager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EventCalendarEntryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EventCalendarEntry getEventCalendarEntrySample1() {
        return new EventCalendarEntry()
            .id(1L)
            .tenantId("tenantId1")
            .calendarProvider("calendarProvider1")
            .externalEventId("externalEventId1")
            .calendarLink("calendarLink1");
    }

    public static EventCalendarEntry getEventCalendarEntrySample2() {
        return new EventCalendarEntry()
            .id(2L)
            .tenantId("tenantId2")
            .calendarProvider("calendarProvider2")
            .externalEventId("externalEventId2")
            .calendarLink("calendarLink2");
    }

    public static EventCalendarEntry getEventCalendarEntryRandomSampleGenerator() {
        return new EventCalendarEntry()
            .id(longCount.incrementAndGet())
            .tenantId(UUID.randomUUID().toString())
            .calendarProvider(UUID.randomUUID().toString())
            .externalEventId(UUID.randomUUID().toString())
            .calendarLink(UUID.randomUUID().toString());
    }
}
