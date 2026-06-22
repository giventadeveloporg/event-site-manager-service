package com.eventsitemanager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EmailLogTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EmailLog getEmailLogSample1() {
        return new EmailLog()
            .id(1L)
            .tenantId("tenantId1")
            .recipientEmail("recipientEmail1")
            .subject("subject1")
            .status("status1")
            .type("type1")
            .campaignId(1L);
    }

    public static EmailLog getEmailLogSample2() {
        return new EmailLog()
            .id(2L)
            .tenantId("tenantId2")
            .recipientEmail("recipientEmail2")
            .subject("subject2")
            .status("status2")
            .type("type2")
            .campaignId(2L);
    }

    public static EmailLog getEmailLogRandomSampleGenerator() {
        return new EmailLog()
            .id(longCount.incrementAndGet())
            .tenantId(UUID.randomUUID().toString())
            .recipientEmail(UUID.randomUUID().toString())
            .subject(UUID.randomUUID().toString())
            .status(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString())
            .campaignId(longCount.incrementAndGet());
    }
}
