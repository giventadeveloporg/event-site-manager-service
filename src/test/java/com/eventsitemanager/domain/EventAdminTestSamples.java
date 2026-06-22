package com.eventsitemanager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EventAdminTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EventAdmin getEventAdminSample1() {
        return new EventAdmin().id(1L).tenantId("tenantId1").role("role1");
    }

    public static EventAdmin getEventAdminSample2() {
        return new EventAdmin().id(2L).tenantId("tenantId2").role("role2");
    }

    public static EventAdmin getEventAdminRandomSampleGenerator() {
        return new EventAdmin().id(longCount.incrementAndGet()).tenantId(UUID.randomUUID().toString()).role(UUID.randomUUID().toString());
    }
}
