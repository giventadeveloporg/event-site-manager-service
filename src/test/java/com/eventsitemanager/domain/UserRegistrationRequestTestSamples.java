package com.eventsitemanager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserRegistrationRequestTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserRegistrationRequest getUserRegistrationRequestSample1() {
        return new UserRegistrationRequest()
            .id(1L)
            .tenantId("tenantId1")
            .requestId("requestId1")
            .userId("userId1")
            .firstName("firstName1")
            .lastName("lastName1")
            .email("email1")
            .phone("phone1")
            .addressLine1("addressLine11")
            .addressLine2("addressLine21")
            .city("city1")
            .state("state1")
            .zipCode("zipCode1")
            .country("country1")
            .familyName("familyName1")
            .cityTown("cityTown1")
            .district("district1")
            .educationalInstitution("educationalInstitution1")
            .profileImageUrl("profileImageUrl1")
            .requestReason("requestReason1")
            .status("status1")
            .adminComments("adminComments1");
    }

    public static UserRegistrationRequest getUserRegistrationRequestSample2() {
        return new UserRegistrationRequest()
            .id(2L)
            .tenantId("tenantId2")
            .requestId("requestId2")
            .userId("userId2")
            .firstName("firstName2")
            .lastName("lastName2")
            .email("email2")
            .phone("phone2")
            .addressLine1("addressLine12")
            .addressLine2("addressLine22")
            .city("city2")
            .state("state2")
            .zipCode("zipCode2")
            .country("country2")
            .familyName("familyName2")
            .cityTown("cityTown2")
            .district("district2")
            .educationalInstitution("educationalInstitution2")
            .profileImageUrl("profileImageUrl2")
            .requestReason("requestReason2")
            .status("status2")
            .adminComments("adminComments2");
    }

    public static UserRegistrationRequest getUserRegistrationRequestRandomSampleGenerator() {
        return new UserRegistrationRequest()
            .id(longCount.incrementAndGet())
            .tenantId(UUID.randomUUID().toString())
            .requestId(UUID.randomUUID().toString())
            .userId(UUID.randomUUID().toString())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString())
            .addressLine1(UUID.randomUUID().toString())
            .addressLine2(UUID.randomUUID().toString())
            .city(UUID.randomUUID().toString())
            .state(UUID.randomUUID().toString())
            .zipCode(UUID.randomUUID().toString())
            .country(UUID.randomUUID().toString())
            .familyName(UUID.randomUUID().toString())
            .cityTown(UUID.randomUUID().toString())
            .district(UUID.randomUUID().toString())
            .educationalInstitution(UUID.randomUUID().toString())
            .profileImageUrl(UUID.randomUUID().toString())
            .requestReason(UUID.randomUUID().toString())
            .status(UUID.randomUUID().toString())
            .adminComments(UUID.randomUUID().toString());
    }
}
