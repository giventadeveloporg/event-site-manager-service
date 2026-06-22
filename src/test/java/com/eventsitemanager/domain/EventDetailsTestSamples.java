package com.eventsitemanager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class EventDetailsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static EventDetails getEventDetailsSample1() {
        return new EventDetails()
            .id(1L)
            .tenantId("tenantId1")
            .title("title1")
            .caption("caption1")
            .description("description1")
            .startTime("startTime1")
            .endTime("endTime1")
            .location("location1")
            .directionsToVenue("directionsToVenue1")
            .capacity(1)
            .admissionType("admissionType1")
            .maxGuestsPerAttendee(1);
    }

    public static EventDetails getEventDetailsSample2() {
        return new EventDetails()
            .id(2L)
            .tenantId("tenantId2")
            .title("title2")
            .caption("caption2")
            .description("description2")
            .startTime("startTime2")
            .endTime("endTime2")
            .location("location2")
            .directionsToVenue("directionsToVenue2")
            .capacity(2)
            .admissionType("admissionType2")
            .maxGuestsPerAttendee(2);
    }

    public static EventDetails getEventDetailsRandomSampleGenerator() {
        return new EventDetails()
            .id(longCount.incrementAndGet())
            .tenantId(UUID.randomUUID().toString())
            .title(UUID.randomUUID().toString())
            .caption(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .startTime(UUID.randomUUID().toString())
            .endTime(UUID.randomUUID().toString())
            .location(UUID.randomUUID().toString())
            .directionsToVenue(UUID.randomUUID().toString())
            .capacity(intCount.incrementAndGet())
            .admissionType(UUID.randomUUID().toString())
            .maxGuestsPerAttendee(intCount.incrementAndGet());
    }
}
