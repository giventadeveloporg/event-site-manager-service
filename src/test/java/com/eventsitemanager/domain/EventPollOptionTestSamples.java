package com.eventsitemanager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EventPollOptionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EventPollOption getEventPollOptionSample1() {
        return new EventPollOption().id(1L).tenantId("tenantId1").optionText("optionText1");
    }

    public static EventPollOption getEventPollOptionSample2() {
        return new EventPollOption().id(2L).tenantId("tenantId2").optionText("optionText2");
    }

    public static EventPollOption getEventPollOptionRandomSampleGenerator() {
        return new EventPollOption()
            .id(longCount.incrementAndGet())
            .tenantId(UUID.randomUUID().toString())
            .optionText(UUID.randomUUID().toString());
    }
}
