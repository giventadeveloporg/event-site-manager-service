package com.eventsitemanager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class WhatsAppLogTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static WhatsAppLog getWhatsAppLogSample1() {
        return new WhatsAppLog()
            .id(1L)
            .tenantId("tenantId1")
            .recipientPhone("recipientPhone1")
            .status("status1")
            .type("type1")
            .campaignId(1L);
    }

    public static WhatsAppLog getWhatsAppLogSample2() {
        return new WhatsAppLog()
            .id(2L)
            .tenantId("tenantId2")
            .recipientPhone("recipientPhone2")
            .status("status2")
            .type("type2")
            .campaignId(2L);
    }

    public static WhatsAppLog getWhatsAppLogRandomSampleGenerator() {
        return new WhatsAppLog()
            .id(longCount.incrementAndGet())
            .tenantId(UUID.randomUUID().toString())
            .recipientPhone(UUID.randomUUID().toString())
            .status(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString())
            .campaignId(longCount.incrementAndGet());
    }
}
