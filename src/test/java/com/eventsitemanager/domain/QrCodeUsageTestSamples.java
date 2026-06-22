package com.eventsitemanager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class QrCodeUsageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static QrCodeUsage getQrCodeUsageSample1() {
        return new QrCodeUsage().id(1L).tenantId("tenantId1").qrCodeData("qrCodeData1").usageCount(1).lastScannedBy("lastScannedBy1");
    }

    public static QrCodeUsage getQrCodeUsageSample2() {
        return new QrCodeUsage().id(2L).tenantId("tenantId2").qrCodeData("qrCodeData2").usageCount(2).lastScannedBy("lastScannedBy2");
    }

    public static QrCodeUsage getQrCodeUsageRandomSampleGenerator() {
        return new QrCodeUsage()
            .id(longCount.incrementAndGet())
            .tenantId(UUID.randomUUID().toString())
            .qrCodeData(UUID.randomUUID().toString())
            .usageCount(intCount.incrementAndGet())
            .lastScannedBy(UUID.randomUUID().toString());
    }
}
