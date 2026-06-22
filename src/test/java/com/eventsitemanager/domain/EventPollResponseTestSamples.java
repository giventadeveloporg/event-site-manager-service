package com.eventsitemanager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EventPollResponseTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EventPollResponse getEventPollResponseSample1() {
        return new EventPollResponse().id(1L).tenantId("tenantId1").comment("comment1");
    }

    public static EventPollResponse getEventPollResponseSample2() {
        return new EventPollResponse().id(2L).tenantId("tenantId2").comment("comment2");
    }

    public static EventPollResponse getEventPollResponseRandomSampleGenerator() {
        return new EventPollResponse()
            .id(longCount.incrementAndGet())
            .tenantId(UUID.randomUUID().toString())
            .comment(UUID.randomUUID().toString());
    }
}
