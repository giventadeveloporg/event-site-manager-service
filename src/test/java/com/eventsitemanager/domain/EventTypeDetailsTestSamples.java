package com.eventsitemanager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EventTypeDetailsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EventTypeDetails getEventTypeDetailsSample1() {
        return new EventTypeDetails().id(1L).tenantId("tenantId1").name("name1").description("description1");
    }

    public static EventTypeDetails getEventTypeDetailsSample2() {
        return new EventTypeDetails().id(2L).tenantId("tenantId2").name("name2").description("description2");
    }

    public static EventTypeDetails getEventTypeDetailsRandomSampleGenerator() {
        return new EventTypeDetails()
            .id(longCount.incrementAndGet())
            .tenantId(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
