package com.eventsitemanager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserTaskTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserTask getUserTaskSample1() {
        return new UserTask()
            .id(1L)
            .tenantId("tenantId1")
            .title("title1")
            .status("status1")
            .priority("priority1")
            .assigneeName("assigneeName1")
            .assigneeContactPhone("assigneeContactPhone1")
            .assigneeContactEmail("assigneeContactEmail1");
    }

    public static UserTask getUserTaskSample2() {
        return new UserTask()
            .id(2L)
            .tenantId("tenantId2")
            .title("title2")
            .status("status2")
            .priority("priority2")
            .assigneeName("assigneeName2")
            .assigneeContactPhone("assigneeContactPhone2")
            .assigneeContactEmail("assigneeContactEmail2");
    }

    public static UserTask getUserTaskRandomSampleGenerator() {
        return new UserTask()
            .id(longCount.incrementAndGet())
            .tenantId(UUID.randomUUID().toString())
            .title(UUID.randomUUID().toString())
            .status(UUID.randomUUID().toString())
            .priority(UUID.randomUUID().toString())
            .assigneeName(UUID.randomUUID().toString())
            .assigneeContactPhone(UUID.randomUUID().toString())
            .assigneeContactEmail(UUID.randomUUID().toString());
    }
}
