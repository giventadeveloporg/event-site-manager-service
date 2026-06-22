package com.eventsitemanager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class EventScoreCardDetailTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static EventScoreCardDetail getEventScoreCardDetailSample1() {
        return new EventScoreCardDetail().id(1L).teamName("teamName1").playerName("playerName1").points(1).remarks("remarks1");
    }

    public static EventScoreCardDetail getEventScoreCardDetailSample2() {
        return new EventScoreCardDetail().id(2L).teamName("teamName2").playerName("playerName2").points(2).remarks("remarks2");
    }

    public static EventScoreCardDetail getEventScoreCardDetailRandomSampleGenerator() {
        return new EventScoreCardDetail()
            .id(longCount.incrementAndGet())
            .teamName(UUID.randomUUID().toString())
            .playerName(UUID.randomUUID().toString())
            .points(intCount.incrementAndGet())
            .remarks(UUID.randomUUID().toString());
    }
}
