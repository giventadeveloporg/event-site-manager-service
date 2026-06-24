package com.eventsitemanager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BulkOperationLogTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static BulkOperationLog getBulkOperationLogSample1() {
        return new BulkOperationLog()
            .id(1L)
            .tenantId("tenantId1")
            .operationType("operationType1")
            .targetCount(1)
            .successCount(1)
            .errorCount(1);
    }

    public static BulkOperationLog getBulkOperationLogSample2() {
        return new BulkOperationLog()
            .id(2L)
            .tenantId("tenantId2")
            .operationType("operationType2")
            .targetCount(2)
            .successCount(2)
            .errorCount(2);
    }

    public static BulkOperationLog getBulkOperationLogRandomSampleGenerator() {
        return new BulkOperationLog()
            .id(longCount.incrementAndGet())
            .tenantId(UUID.randomUUID().toString())
            .operationType(UUID.randomUUID().toString())
            .targetCount(intCount.incrementAndGet())
            .successCount(intCount.incrementAndGet())
            .errorCount(intCount.incrementAndGet());
    }
}
