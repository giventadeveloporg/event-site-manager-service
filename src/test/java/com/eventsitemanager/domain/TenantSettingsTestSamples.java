package com.eventsitemanager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TenantSettingsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TenantSettings getTenantSettingsSample1() {
        return new TenantSettings().id(1L).tenantId("tenantId1").whatsappApiKey("whatsappApiKey1");
    }

    public static TenantSettings getTenantSettingsSample2() {
        return new TenantSettings().id(2L).tenantId("tenantId2").whatsappApiKey("whatsappApiKey2");
    }

    public static TenantSettings getTenantSettingsRandomSampleGenerator() {
        return new TenantSettings()
            .id(longCount.incrementAndGet())
            .tenantId(UUID.randomUUID().toString())
            .whatsappApiKey(UUID.randomUUID().toString());
    }
}
