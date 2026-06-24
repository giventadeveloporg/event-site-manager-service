package com.eventsitemanager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DiscountCodeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static DiscountCode getDiscountCodeSample1() {
        return new DiscountCode()
            .id(1L)
            .code("code1")
            .description("description1")
            .discountType("discountType1")
            .maxUses(1)
            .usesCount(1)
            .eventId(1L)
            .tenantId("tenantId1");
    }

    public static DiscountCode getDiscountCodeSample2() {
        return new DiscountCode()
            .id(2L)
            .code("code2")
            .description("description2")
            .discountType("discountType2")
            .maxUses(2)
            .usesCount(2)
            .eventId(2L)
            .tenantId("tenantId2");
    }

    public static DiscountCode getDiscountCodeRandomSampleGenerator() {
        return new DiscountCode()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .discountType(UUID.randomUUID().toString())
            .maxUses(intCount.incrementAndGet())
            .usesCount(intCount.incrementAndGet())
            .eventId(longCount.incrementAndGet())
            .tenantId(UUID.randomUUID().toString());
    }
}
