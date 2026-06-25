package com.eventsitemanager.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MembershipPlan.
 * Subscription plan definitions for membership tiers.
 */
@Entity
@Table(name = "membership_plan")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MembershipPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "public.sequence_generator", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "tenant_id", length = 255, nullable = false)
    private String tenantId;

    @NotNull
    @Size(max = 255)
    @Column(name = "plan_name", length = 255, nullable = false)
    private String planName;

    @NotNull
    @Size(max = 100)
    @Column(name = "plan_code", length = 100, nullable = false)
    private String planCode;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @NotNull
    @Size(max = 50)
    @Column(name = "plan_type", length = 50, nullable = false)
    private String planType = "SUBSCRIPTION";

    @NotNull
    @Size(max = 20)
    @Column(name = "billing_interval", length = 20, nullable = false)
    private String billingInterval = "MONTHLY";

    @NotNull
    @Column(name = "price", precision = 21, scale = 2, nullable = false)
    private BigDecimal price;

    @NotNull
    @Size(max = 3)
    @Column(name = "currency", length = 3, nullable = false)
    private String currency = "USD";

    @Column(name = "trial_days")
    private Integer trialDays = 0;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "max_events_per_month")
    private Integer maxEventsPerMonth;

    @Column(name = "max_attendees_per_event")
    private Integer maxAttendeesPerEvent;

    @Column(name = "features_json", columnDefinition = "text")
    private String featuresJson;

    @Size(max = 255)
    @Column(name = "stripe_price_id", length = 255)
    private String stripePriceId;

    @Size(max = 255)
    @Column(name = "stripe_product_id", length = 255)
    private String stripeProductId;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MembershipPlan id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public MembershipPlan tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getPlanName() {
        return this.planName;
    }

    public MembershipPlan planName(String planName) {
        this.setPlanName(planName);
        return this;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanCode() {
        return this.planCode;
    }

    public MembershipPlan planCode(String planCode) {
        this.setPlanCode(planCode);
        return this;
    }

    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

    public String getDescription() {
        return this.description;
    }

    public MembershipPlan description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlanType() {
        return this.planType;
    }

    public MembershipPlan planType(String planType) {
        this.setPlanType(planType);
        return this;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getBillingInterval() {
        return this.billingInterval;
    }

    public MembershipPlan billingInterval(String billingInterval) {
        this.setBillingInterval(billingInterval);
        return this;
    }

    public void setBillingInterval(String billingInterval) {
        this.billingInterval = billingInterval;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public MembershipPlan price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCurrency() {
        return this.currency;
    }

    public MembershipPlan currency(String currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getTrialDays() {
        return this.trialDays;
    }

    public MembershipPlan trialDays(Integer trialDays) {
        this.setTrialDays(trialDays);
        return this;
    }

    public void setTrialDays(Integer trialDays) {
        this.trialDays = trialDays;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public MembershipPlan isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getMaxEventsPerMonth() {
        return this.maxEventsPerMonth;
    }

    public MembershipPlan maxEventsPerMonth(Integer maxEventsPerMonth) {
        this.setMaxEventsPerMonth(maxEventsPerMonth);
        return this;
    }

    public void setMaxEventsPerMonth(Integer maxEventsPerMonth) {
        this.maxEventsPerMonth = maxEventsPerMonth;
    }

    public Integer getMaxAttendeesPerEvent() {
        return this.maxAttendeesPerEvent;
    }

    public MembershipPlan maxAttendeesPerEvent(Integer maxAttendeesPerEvent) {
        this.setMaxAttendeesPerEvent(maxAttendeesPerEvent);
        return this;
    }

    public void setMaxAttendeesPerEvent(Integer maxAttendeesPerEvent) {
        this.maxAttendeesPerEvent = maxAttendeesPerEvent;
    }

    public String getFeaturesJson() {
        return this.featuresJson;
    }

    public MembershipPlan featuresJson(String featuresJson) {
        this.setFeaturesJson(featuresJson);
        return this;
    }

    public void setFeaturesJson(String featuresJson) {
        this.featuresJson = featuresJson;
    }

    public String getStripePriceId() {
        return this.stripePriceId;
    }

    public MembershipPlan stripePriceId(String stripePriceId) {
        this.setStripePriceId(stripePriceId);
        return this;
    }

    public void setStripePriceId(String stripePriceId) {
        this.stripePriceId = stripePriceId;
    }

    public String getStripeProductId() {
        return this.stripeProductId;
    }

    public MembershipPlan stripeProductId(String stripeProductId) {
        this.setStripeProductId(stripeProductId);
        return this;
    }

    public void setStripeProductId(String stripeProductId) {
        this.stripeProductId = stripeProductId;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public MembershipPlan createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public MembershipPlan updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MembershipPlan)) {
            return false;
        }
        return getId() != null && getId().equals(((MembershipPlan) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "MembershipPlan{" +
            "id=" +
            getId() +
            ", tenantId='" +
            getTenantId() +
            "'" +
            ", planName='" +
            getPlanName() +
            "'" +
            ", planCode='" +
            getPlanCode() +
            "'" +
            ", planType='" +
            getPlanType() +
            "'" +
            ", billingInterval='" +
            getBillingInterval() +
            "'" +
            ", price=" +
            getPrice() +
            ", currency='" +
            getCurrency() +
            "'" +
            ", isActive='" +
            getIsActive() +
            "'" +
            ", createdAt='" +
            getCreatedAt() +
            "'" +
            ", updatedAt='" +
            getUpdatedAt() +
            "'" +
            "}"
        );
    }
}
