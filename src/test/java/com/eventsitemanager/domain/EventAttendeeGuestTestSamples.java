package com.eventsitemanager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EventAttendeeGuestTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EventAttendeeGuest getEventAttendeeGuestSample1() {
        return new EventAttendeeGuest()
            .id(1L)
            .tenantId("tenantId1")
            .firstName("firstName1")
            .lastName("lastName1")
            .email("email1")
            .phone("phone1")
            .ageGroup("ageGroup1")
            .relationship("relationship1")
            .specialRequirements("specialRequirements1")
            .registrationStatus("registrationStatus1")
            .checkInStatus("checkInStatus1");
    }

    public static EventAttendeeGuest getEventAttendeeGuestSample2() {
        return new EventAttendeeGuest()
            .id(2L)
            .tenantId("tenantId2")
            .firstName("firstName2")
            .lastName("lastName2")
            .email("email2")
            .phone("phone2")
            .ageGroup("ageGroup2")
            .relationship("relationship2")
            .specialRequirements("specialRequirements2")
            .registrationStatus("registrationStatus2")
            .checkInStatus("checkInStatus2");
    }

    public static EventAttendeeGuest getEventAttendeeGuestRandomSampleGenerator() {
        return new EventAttendeeGuest()
            .id(longCount.incrementAndGet())
            .tenantId(UUID.randomUUID().toString())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString())
            .ageGroup(UUID.randomUUID().toString())
            .relationship(UUID.randomUUID().toString())
            .specialRequirements(UUID.randomUUID().toString())
            .registrationStatus(UUID.randomUUID().toString())
            .checkInStatus(UUID.randomUUID().toString());
    }
}
