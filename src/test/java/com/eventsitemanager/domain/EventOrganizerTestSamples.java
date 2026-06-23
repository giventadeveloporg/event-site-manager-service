package com.eventsitemanager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EventOrganizerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EventOrganizer getEventOrganizerSample1() {
        return new EventOrganizer()
            .id(1L)
            .tenantId("tenantId1")
            .title("title1")
            .designation("designation1")
            .contactEmail("contactEmail1")
            .contactPhone("contactPhone1");
    }

    public static EventOrganizer getEventOrganizerSample2() {
        return new EventOrganizer()
            .id(2L)
            .tenantId("tenantId2")
            .title("title2")
            .designation("designation2")
            .contactEmail("contactEmail2")
            .contactPhone("contactPhone2");
    }

    public static EventOrganizer getEventOrganizerRandomSampleGenerator() {
        return new EventOrganizer()
            .id(longCount.incrementAndGet())
            .tenantId(UUID.randomUUID().toString())
            .title(UUID.randomUUID().toString())
            .designation(UUID.randomUUID().toString())
            .contactEmail(UUID.randomUUID().toString())
            .contactPhone(UUID.randomUUID().toString());
    }
}
