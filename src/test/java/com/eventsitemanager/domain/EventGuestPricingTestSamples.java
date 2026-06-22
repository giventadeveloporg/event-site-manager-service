package com.eventsitemanager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EventGuestPricingTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EventGuestPricing getEventGuestPricingSample1() {
        return new EventGuestPricing().id(1L).tenantId("tenantId1").ageGroup("ageGroup1").description("description1");
    }

    public static EventGuestPricing getEventGuestPricingSample2() {
        return new EventGuestPricing().id(2L).tenantId("tenantId2").ageGroup("ageGroup2").description("description2");
    }

    public static EventGuestPricing getEventGuestPricingRandomSampleGenerator() {
        return new EventGuestPricing()
            .id(longCount.incrementAndGet())
            .tenantId(UUID.randomUUID().toString())
            .ageGroup(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
