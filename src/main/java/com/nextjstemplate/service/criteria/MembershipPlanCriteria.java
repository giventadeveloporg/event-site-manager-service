package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.MembershipPlan} entity.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MembershipPlanCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;
    private StringFilter tenantId;
    private StringFilter planName;
    private StringFilter planCode;
    private StringFilter planType;
    private StringFilter billingInterval;
    private BigDecimalFilter price;
    private StringFilter currency;
    private IntegerFilter trialDays;
    private BooleanFilter isActive;
    private IntegerFilter maxEventsPerMonth;
    private IntegerFilter maxAttendeesPerEvent;
    private StringFilter stripePriceId;
    private StringFilter stripeProductId;
    private ZonedDateTimeFilter createdAt;
    private ZonedDateTimeFilter updatedAt;
    private Boolean distinct;

    public MembershipPlanCriteria() {}

    public MembershipPlanCriteria(MembershipPlanCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.planName = other.planName == null ? null : other.planName.copy();
        this.planCode = other.planCode == null ? null : other.planCode.copy();
        this.planType = other.planType == null ? null : other.planType.copy();
        this.billingInterval = other.billingInterval == null ? null : other.billingInterval.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.currency = other.currency == null ? null : other.currency.copy();
        this.trialDays = other.trialDays == null ? null : other.trialDays.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.maxEventsPerMonth = other.maxEventsPerMonth == null ? null : other.maxEventsPerMonth.copy();
        this.maxAttendeesPerEvent = other.maxAttendeesPerEvent == null ? null : other.maxAttendeesPerEvent.copy();
        this.stripePriceId = other.stripePriceId == null ? null : other.stripePriceId.copy();
        this.stripeProductId = other.stripeProductId == null ? null : other.stripeProductId.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MembershipPlanCriteria copy() {
        return new MembershipPlanCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTenantId() {
        return tenantId;
    }

    public StringFilter tenantId() {
        if (tenantId == null) {
            tenantId = new StringFilter();
        }
        return tenantId;
    }

    public void setTenantId(StringFilter tenantId) {
        this.tenantId = tenantId;
    }

    public StringFilter getPlanName() {
        return planName;
    }

    public StringFilter planName() {
        if (planName == null) {
            planName = new StringFilter();
        }
        return planName;
    }

    public void setPlanName(StringFilter planName) {
        this.planName = planName;
    }

    public StringFilter getPlanCode() {
        return planCode;
    }

    public StringFilter planCode() {
        if (planCode == null) {
            planCode = new StringFilter();
        }
        return planCode;
    }

    public void setPlanCode(StringFilter planCode) {
        this.planCode = planCode;
    }

    public StringFilter getPlanType() {
        return planType;
    }

    public StringFilter planType() {
        if (planType == null) {
            planType = new StringFilter();
        }
        return planType;
    }

    public void setPlanType(StringFilter planType) {
        this.planType = planType;
    }

    public StringFilter getBillingInterval() {
        return billingInterval;
    }

    public StringFilter billingInterval() {
        if (billingInterval == null) {
            billingInterval = new StringFilter();
        }
        return billingInterval;
    }

    public void setBillingInterval(StringFilter billingInterval) {
        this.billingInterval = billingInterval;
    }

    public BigDecimalFilter getPrice() {
        return price;
    }

    public BigDecimalFilter price() {
        if (price == null) {
            price = new BigDecimalFilter();
        }
        return price;
    }

    public void setPrice(BigDecimalFilter price) {
        this.price = price;
    }

    public StringFilter getCurrency() {
        return currency;
    }

    public StringFilter currency() {
        if (currency == null) {
            currency = new StringFilter();
        }
        return currency;
    }

    public void setCurrency(StringFilter currency) {
        this.currency = currency;
    }

    public IntegerFilter getTrialDays() {
        return trialDays;
    }

    public IntegerFilter trialDays() {
        if (trialDays == null) {
            trialDays = new IntegerFilter();
        }
        return trialDays;
    }

    public void setTrialDays(IntegerFilter trialDays) {
        this.trialDays = trialDays;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            isActive = new BooleanFilter();
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public IntegerFilter getMaxEventsPerMonth() {
        return maxEventsPerMonth;
    }

    public IntegerFilter maxEventsPerMonth() {
        if (maxEventsPerMonth == null) {
            maxEventsPerMonth = new IntegerFilter();
        }
        return maxEventsPerMonth;
    }

    public void setMaxEventsPerMonth(IntegerFilter maxEventsPerMonth) {
        this.maxEventsPerMonth = maxEventsPerMonth;
    }

    public IntegerFilter getMaxAttendeesPerEvent() {
        return maxAttendeesPerEvent;
    }

    public IntegerFilter maxAttendeesPerEvent() {
        if (maxAttendeesPerEvent == null) {
            maxAttendeesPerEvent = new IntegerFilter();
        }
        return maxAttendeesPerEvent;
    }

    public void setMaxAttendeesPerEvent(IntegerFilter maxAttendeesPerEvent) {
        this.maxAttendeesPerEvent = maxAttendeesPerEvent;
    }

    public StringFilter getStripePriceId() {
        return stripePriceId;
    }

    public StringFilter stripePriceId() {
        if (stripePriceId == null) {
            stripePriceId = new StringFilter();
        }
        return stripePriceId;
    }

    public void setStripePriceId(StringFilter stripePriceId) {
        this.stripePriceId = stripePriceId;
    }

    public StringFilter getStripeProductId() {
        return stripeProductId;
    }

    public StringFilter stripeProductId() {
        if (stripeProductId == null) {
            stripeProductId = new StringFilter();
        }
        return stripeProductId;
    }

    public void setStripeProductId(StringFilter stripeProductId) {
        this.stripeProductId = stripeProductId;
    }

    public ZonedDateTimeFilter getCreatedAt() {
        return createdAt;
    }

    public ZonedDateTimeFilter createdAt() {
        if (createdAt == null) {
            createdAt = new ZonedDateTimeFilter();
        }
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTimeFilter createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTimeFilter getUpdatedAt() {
        return updatedAt;
    }

    public ZonedDateTimeFilter updatedAt() {
        if (updatedAt == null) {
            updatedAt = new ZonedDateTimeFilter();
        }
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTimeFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MembershipPlanCriteria that = (MembershipPlanCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(planName, that.planName) &&
            Objects.equals(planCode, that.planCode) &&
            Objects.equals(planType, that.planType) &&
            Objects.equals(billingInterval, that.billingInterval) &&
            Objects.equals(price, that.price) &&
            Objects.equals(currency, that.currency) &&
            Objects.equals(trialDays, that.trialDays) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(maxEventsPerMonth, that.maxEventsPerMonth) &&
            Objects.equals(maxAttendeesPerEvent, that.maxAttendeesPerEvent) &&
            Objects.equals(stripePriceId, that.stripePriceId) &&
            Objects.equals(stripeProductId, that.stripeProductId) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            tenantId,
            planName,
            planCode,
            planType,
            billingInterval,
            price,
            currency,
            trialDays,
            isActive,
            maxEventsPerMonth,
            maxAttendeesPerEvent,
            stripePriceId,
            stripeProductId,
            createdAt,
            updatedAt,
            distinct
        );
    }

    @Override
    public String toString() {
        return (
            "MembershipPlanCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
            (planName != null ? "planName=" + planName + ", " : "") +
            (planCode != null ? "planCode=" + planCode + ", " : "") +
            (planType != null ? "planType=" + planType + ", " : "") +
            (billingInterval != null ? "billingInterval=" + billingInterval + ", " : "") +
            (price != null ? "price=" + price + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}"
        );
    }
}

