package com.nextjstemplate.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EventCompetitionRegistrationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EventCompetitionRegistration getEventCompetitionRegistrationSample1() {
        return new EventCompetitionRegistration().id(1L).tenantId("tenantId1");
    }

    public static EventCompetitionRegistration getEventCompetitionRegistrationSample2() {
        return new EventCompetitionRegistration().id(2L).tenantId("tenantId2");
    }

    public static EventCompetitionRegistration getEventCompetitionRegistrationRandomSampleGenerator() {
        return new EventCompetitionRegistration().id(longCount.incrementAndGet()).tenantId(UUID.randomUUID().toString());
    }
}
