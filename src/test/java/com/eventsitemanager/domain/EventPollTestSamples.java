package com.eventsitemanager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EventPollTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EventPoll getEventPollSample1() {
        return new EventPoll().id(1L).tenantId("tenantId1").title("title1").description("description1");
    }

    public static EventPoll getEventPollSample2() {
        return new EventPoll().id(2L).tenantId("tenantId2").title("title2").description("description2");
    }

    public static EventPoll getEventPollRandomSampleGenerator() {
        return new EventPoll()
            .id(longCount.incrementAndGet())
            .tenantId(UUID.randomUUID().toString())
            .title(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
