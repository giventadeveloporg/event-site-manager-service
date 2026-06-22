package com.eventsitemanager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class EventTicketTransactionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static EventTicketTransaction getEventTicketTransactionSample1() {
        return new EventTicketTransaction()
            .id(1L)
            .tenantId("tenantId1")
            .email("email1")
            .firstName("firstName1")
            .lastName("lastName1")
            .quantity(1)
            .status("status1");
    }

    public static EventTicketTransaction getEventTicketTransactionSample2() {
        return new EventTicketTransaction()
            .id(2L)
            .tenantId("tenantId2")
            .email("email2")
            .firstName("firstName2")
            .lastName("lastName2")
            .quantity(2)
            .status("status2");
    }

    public static EventTicketTransaction getEventTicketTransactionRandomSampleGenerator() {
        return new EventTicketTransaction()
            .id(longCount.incrementAndGet())
            .tenantId(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .quantity(intCount.incrementAndGet())
            .status(UUID.randomUUID().toString());
    }
}
