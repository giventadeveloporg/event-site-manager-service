package com.eventsitemanager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class EventTicketTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static EventTicketType getEventTicketTypeSample1() {
        return new EventTicketType()
            .id(1L)
            .tenantId("tenantId1")
            .name("name1")
            .description("description1")
            .code("code1")
            .availableQuantity(1)
            .soldQuantity(1);
    }

    public static EventTicketType getEventTicketTypeSample2() {
        return new EventTicketType()
            .id(2L)
            .tenantId("tenantId2")
            .name("name2")
            .description("description2")
            .code("code2")
            .availableQuantity(2)
            .soldQuantity(2);
    }

    public static EventTicketType getEventTicketTypeRandomSampleGenerator() {
        return new EventTicketType()
            .id(longCount.incrementAndGet())
            .tenantId(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .code(UUID.randomUUID().toString())
            .availableQuantity(intCount.incrementAndGet())
            .soldQuantity(intCount.incrementAndGet());
    }
}
