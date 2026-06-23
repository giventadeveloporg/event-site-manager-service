package com.eventsitemanager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class EventLiveUpdateTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static EventLiveUpdate getEventLiveUpdateSample1() {
        return new EventLiveUpdate()
            .id(1L)
            .updateType("updateType1")
            .contentText("contentText1")
            .contentImageUrl("contentImageUrl1")
            .contentVideoUrl("contentVideoUrl1")
            .contentLinkUrl("contentLinkUrl1")
            .displayOrder(1);
    }

    public static EventLiveUpdate getEventLiveUpdateSample2() {
        return new EventLiveUpdate()
            .id(2L)
            .updateType("updateType2")
            .contentText("contentText2")
            .contentImageUrl("contentImageUrl2")
            .contentVideoUrl("contentVideoUrl2")
            .contentLinkUrl("contentLinkUrl2")
            .displayOrder(2);
    }

    public static EventLiveUpdate getEventLiveUpdateRandomSampleGenerator() {
        return new EventLiveUpdate()
            .id(longCount.incrementAndGet())
            .updateType(UUID.randomUUID().toString())
            .contentText(UUID.randomUUID().toString())
            .contentImageUrl(UUID.randomUUID().toString())
            .contentVideoUrl(UUID.randomUUID().toString())
            .contentLinkUrl(UUID.randomUUID().toString())
            .displayOrder(intCount.incrementAndGet());
    }
}
