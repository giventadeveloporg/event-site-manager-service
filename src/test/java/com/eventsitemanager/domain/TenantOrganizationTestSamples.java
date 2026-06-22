package com.eventsitemanager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TenantOrganizationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TenantOrganization getTenantOrganizationSample1() {
        return new TenantOrganization()
            .id(1L)
            .tenantId("tenantId1")
            .organizationName("organizationName1")
            .domain("domain1")
            .primaryColor("primaryColor1")
            .secondaryColor("secondaryColor1")
            .logoUrl("logoUrl1")
            .contactEmail("contactEmail1")
            .contactPhone("contactPhone1")
            .subscriptionPlan("subscriptionPlan1")
            .subscriptionStatus("subscriptionStatus1")
            .stripeCustomerId("stripeCustomerId1");
    }

    public static TenantOrganization getTenantOrganizationSample2() {
        return new TenantOrganization()
            .id(2L)
            .tenantId("tenantId2")
            .organizationName("organizationName2")
            .domain("domain2")
            .primaryColor("primaryColor2")
            .secondaryColor("secondaryColor2")
            .logoUrl("logoUrl2")
            .contactEmail("contactEmail2")
            .contactPhone("contactPhone2")
            .subscriptionPlan("subscriptionPlan2")
            .subscriptionStatus("subscriptionStatus2")
            .stripeCustomerId("stripeCustomerId2");
    }

    public static TenantOrganization getTenantOrganizationRandomSampleGenerator() {
        return new TenantOrganization()
            .id(longCount.incrementAndGet())
            .tenantId(UUID.randomUUID().toString())
            .organizationName(UUID.randomUUID().toString())
            .domain(UUID.randomUUID().toString())
            .primaryColor(UUID.randomUUID().toString())
            .secondaryColor(UUID.randomUUID().toString())
            .logoUrl(UUID.randomUUID().toString())
            .contactEmail(UUID.randomUUID().toString())
            .contactPhone(UUID.randomUUID().toString())
            .subscriptionPlan(UUID.randomUUID().toString())
            .subscriptionStatus(UUID.randomUUID().toString())
            .stripeCustomerId(UUID.randomUUID().toString());
    }
}
