package com.eventsitemanager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CommunicationCampaignTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CommunicationCampaign getCommunicationCampaignSample1() {
        return new CommunicationCampaign()
            .id(1L)
            .tenantId("tenantId1")
            .name("name1")
            .type("type1")
            .description("description1")
            .createdById(1L)
            .status("status1");
    }

    public static CommunicationCampaign getCommunicationCampaignSample2() {
        return new CommunicationCampaign()
            .id(2L)
            .tenantId("tenantId2")
            .name("name2")
            .type("type2")
            .description("description2")
            .createdById(2L)
            .status("status2");
    }

    public static CommunicationCampaign getCommunicationCampaignRandomSampleGenerator() {
        return new CommunicationCampaign()
            .id(longCount.incrementAndGet())
            .tenantId(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .createdById(longCount.incrementAndGet())
            .status(UUID.randomUUID().toString());
    }
}
