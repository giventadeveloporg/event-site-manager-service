package com.eventsitemanager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserPaymentTransactionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserPaymentTransaction getUserPaymentTransactionSample1() {
        return new UserPaymentTransaction()
            .id(1L)
            .tenantId("tenantId1")
            .transactionType("transactionType1")
            .currency("currency1")
            .stripePaymentIntentId("stripePaymentIntentId1")
            .stripeTransferGroup("stripeTransferGroup1")
            .status("status1");
    }

    public static UserPaymentTransaction getUserPaymentTransactionSample2() {
        return new UserPaymentTransaction()
            .id(2L)
            .tenantId("tenantId2")
            .transactionType("transactionType2")
            .currency("currency2")
            .stripePaymentIntentId("stripePaymentIntentId2")
            .stripeTransferGroup("stripeTransferGroup2")
            .status("status2");
    }

    public static UserPaymentTransaction getUserPaymentTransactionRandomSampleGenerator() {
        return new UserPaymentTransaction()
            .id(longCount.incrementAndGet())
            .tenantId(UUID.randomUUID().toString())
            .transactionType(UUID.randomUUID().toString())
            .currency(UUID.randomUUID().toString())
            .stripePaymentIntentId(UUID.randomUUID().toString())
            .stripeTransferGroup(UUID.randomUUID().toString())
            .status(UUID.randomUUID().toString());
    }
}
