package com.eventsitemanager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class EventScoreCardTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static EventScoreCard getEventScoreCardSample1() {
        return new EventScoreCard().id(1L).teamAName("teamAName1").teamBName("teamBName1").teamAScore(1).teamBScore(1).remarks("remarks1");
    }

    public static EventScoreCard getEventScoreCardSample2() {
        return new EventScoreCard().id(2L).teamAName("teamAName2").teamBName("teamBName2").teamAScore(2).teamBScore(2).remarks("remarks2");
    }

    public static EventScoreCard getEventScoreCardRandomSampleGenerator() {
        return new EventScoreCard()
            .id(longCount.incrementAndGet())
            .teamAName(UUID.randomUUID().toString())
            .teamBName(UUID.randomUUID().toString())
            .teamAScore(intCount.incrementAndGet())
            .teamBScore(intCount.incrementAndGet())
            .remarks(UUID.randomUUID().toString());
    }
}
