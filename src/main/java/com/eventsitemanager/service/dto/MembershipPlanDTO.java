package com.eventsitemanager.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.eventsitemanager.domain.MembershipPlan} entity.
 * Subscription plan definitions for membership tiers.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MembershipPlanDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String tenantId;

    @NotBlank
    @Size(max = 255)
    private String planName;

    @NotBlank
    @Size(max = 100)
    private String planCode;

    private String description;

    @NotNull
    @Pattern(regexp = "SUBSCRIPTION|ONE_TIME|FREEMIUM")
    private String planType = "SUBSCRIPTION";

    @NotNull
    @Pattern(regexp = "MONTHLY|QUARTERLY|YEARLY|ONE_TIME")
    private String billingInterval = "MONTHLY";

    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal price;

    @NotNull
    @Size(min = 3, max = 3)
    private String currency = "USD";

    @Min(0)
    private Integer trialDays = 0;

    @NotNull
    private Boolean isActive = true;

    private Integer maxEventsPerMonth;

    private Integer maxAttendeesPerEvent;

    private String featuresJson;

    @Size(max = 255)
    private String stripePriceId;

    @Size(max = 255)
    private String stripeProductId;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanCode() {
        return planCode;
    }

    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getBillingInterval() {
        return billingInterval;
    }

    public void setBillingInterval(String billingInterval) {
        this.billingInterval = billingInterval;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getTrialDays() {
        return trialDays;
    }

    public void setTrialDays(Integer trialDays) {
        this.trialDays = trialDays;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getMaxEventsPerMonth() {
        return maxEventsPerMonth;
    }

    public void setMaxEventsPerMonth(Integer maxEventsPerMonth) {
        this.maxEventsPerMonth = maxEventsPerMonth;
    }

    public Integer getMaxAttendeesPerEvent() {
        return maxAttendeesPerEvent;
    }

    public void setMaxAttendeesPerEvent(Integer maxAttendeesPerEvent) {
        this.maxAttendeesPerEvent = maxAttendeesPerEvent;
    }

    public String getFeaturesJson() {
        return featuresJson;
    }

    public void setFeaturesJson(String featuresJson) {
        this.featuresJson = featuresJson;
    }

    public String getStripePriceId() {
        return stripePriceId;
    }

    public void setStripePriceId(String stripePriceId) {
        this.stripePriceId = stripePriceId;
    }

    public String getStripeProductId() {
        return stripeProductId;
    }

    public void setStripeProductId(String stripeProductId) {
        this.stripeProductId = stripeProductId;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MembershipPlanDTO)) {
            return false;
        }

        MembershipPlanDTO membershipPlanDTO = (MembershipPlanDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, membershipPlanDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MembershipPlanDTO{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", planName='" + getPlanName() + "'" +
            ", planCode='" + getPlanCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", planType='" + getPlanType() + "'" +
            ", billingInterval='" + getBillingInterval() + "'" +
            ", price=" + getPrice() +
            ", currency='" + getCurrency() + "'" +
            ", trialDays=" + getTrialDays() +
            ", isActive='" + getIsActive() + "'" +
            ", maxEventsPerMonth=" + getMaxEventsPerMonth() +
            ", maxAttendeesPerEvent=" + getMaxAttendeesPerEvent() +
            ", stripePriceId='" + getStripePriceId() + "'" +
            ", stripeProductId='" + getStripeProductId() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
