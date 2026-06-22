package com.eventsitemanager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class EventLiveUpdateAttachmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static EventLiveUpdateAttachment getEventLiveUpdateAttachmentSample1() {
        return new EventLiveUpdateAttachment().id(1L).attachmentType("attachmentType1").attachmentUrl("attachmentUrl1").displayOrder(1);
    }

    public static EventLiveUpdateAttachment getEventLiveUpdateAttachmentSample2() {
        return new EventLiveUpdateAttachment().id(2L).attachmentType("attachmentType2").attachmentUrl("attachmentUrl2").displayOrder(2);
    }

    public static EventLiveUpdateAttachment getEventLiveUpdateAttachmentRandomSampleGenerator() {
        return new EventLiveUpdateAttachment()
            .id(longCount.incrementAndGet())
            .attachmentType(UUID.randomUUID().toString())
            .attachmentUrl(UUID.randomUUID().toString())
            .displayOrder(intCount.incrementAndGet());
    }
}
