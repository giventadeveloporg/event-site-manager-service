package com.eventsitemanager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EventAdminAuditLogTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EventAdminAuditLog getEventAdminAuditLogSample1() {
        return new EventAdminAuditLog().id(1L).tenantId("tenantId1").action("action1").tableName("tableName1").recordId("recordId1");
    }

    public static EventAdminAuditLog getEventAdminAuditLogSample2() {
        return new EventAdminAuditLog().id(2L).tenantId("tenantId2").action("action2").tableName("tableName2").recordId("recordId2");
    }

    public static EventAdminAuditLog getEventAdminAuditLogRandomSampleGenerator() {
        return new EventAdminAuditLog()
            .id(longCount.incrementAndGet())
            .tenantId(UUID.randomUUID().toString())
            .action(UUID.randomUUID().toString())
            .tableName(UUID.randomUUID().toString())
            .recordId(UUID.randomUUID().toString());
    }
}
