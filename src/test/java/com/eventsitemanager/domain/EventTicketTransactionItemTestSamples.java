package com.eventsitemanager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class EventTicketTransactionItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static EventTicketTransactionItem getEventTicketTransactionItemSample1() {
        return new EventTicketTransactionItem().id(1L).tenantId("tenantId1").transactionId(1L).ticketTypeId(1L).quantity(1);
    }

    public static EventTicketTransactionItem getEventTicketTransactionItemSample2() {
        return new EventTicketTransactionItem().id(2L).tenantId("tenantId2").transactionId(2L).ticketTypeId(2L).quantity(2);
    }

    public static EventTicketTransactionItem getEventTicketTransactionItemRandomSampleGenerator() {
        return new EventTicketTransactionItem()
            .id(longCount.incrementAndGet())
            .tenantId(UUID.randomUUID().toString())
            .transactionId(longCount.incrementAndGet())
            .ticketTypeId(longCount.incrementAndGet())
            .quantity(intCount.incrementAndGet());
    }
}
