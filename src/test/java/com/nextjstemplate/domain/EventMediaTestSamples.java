package com.nextjstemplate.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class EventMediaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static EventMedia getEventMediaSample1() {
        return new EventMedia()
            .id(1L)
            .tenantId("tenantId1")
            .title("title1")
            .description("description1")
            .eventMediaType("eventMediaType1")
            .storageType("storageType1")
            .fileUrl("fileUrl1")
            //            .fileDataContentType("fileDataContentType1")
            .fileSize(1)
            .preSignedUrl("preSignedUrl1");
    }

    public static EventMedia getEventMediaSample2() {
        return new EventMedia()
            .id(2L)
            .tenantId("tenantId2")
            .title("title2")
            .description("description2")
            .eventMediaType("eventMediaType2")
            .storageType("storageType2")
            .fileUrl("fileUrl2")
            //            .fileDataContentType("fileDataContentType2")
            .fileSize(2)
            .preSignedUrl("preSignedUrl2");
    }

    public static EventMedia getEventMediaRandomSampleGenerator() {
        return new EventMedia()
            .id(longCount.incrementAndGet())
            .tenantId(UUID.randomUUID().toString())
            .title(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .eventMediaType(UUID.randomUUID().toString())
            .storageType(UUID.randomUUID().toString())
            .fileUrl(UUID.randomUUID().toString())
            //            .fileDataContentType(UUID.randomUUID().toString())
            .fileSize(intCount.incrementAndGet())
            .preSignedUrl(UUID.randomUUID().toString());
    }
}
