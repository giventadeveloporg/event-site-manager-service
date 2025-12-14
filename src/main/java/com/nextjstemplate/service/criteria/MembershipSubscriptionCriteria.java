package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.MembershipSubscription} entity.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MembershipSubscriptionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;
    private StringFilter tenantId;
    private LongFilter userProfileId;
    private LongFilter membershipPlanId;
    private StringFilter subscriptionStatus;
    private LocalDateFilter currentPeriodStart;
    private LocalDateFilter currentPeriodEnd;
    private LocalDateFilter trialStart;
    private LocalDateFilter trialEnd;
    private BooleanFilter cancelAtPeriodEnd;
    private ZonedDateTimeFilter cancelledAt;
    private StringFilter stripeSubscriptionId;
    private StringFilter stripeCustomerId;
    private LongFilter paymentProviderConfigId;
    private ZonedDateTimeFilter createdAt;
    private ZonedDateTimeFilter updatedAt;
    private Boolean distinct;

    public MembershipSubscriptionCriteria() {}

    public MembershipSubscriptionCriteria(MembershipSubscriptionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.userProfileId = other.userProfileId == null ? null : other.userProfileId.copy();
        this.membershipPlanId = other.membershipPlanId == null ? null : other.membershipPlanId.copy();
        this.subscriptionStatus = other.subscriptionStatus == null ? null : other.subscriptionStatus.copy();
        this.currentPeriodStart = other.currentPeriodStart == null ? null : other.currentPeriodStart.copy();
        this.currentPeriodEnd = other.currentPeriodEnd == null ? null : other.currentPeriodEnd.copy();
        this.trialStart = other.trialStart == null ? null : other.trialStart.copy();
        this.trialEnd = other.trialEnd == null ? null : other.trialEnd.copy();
        this.cancelAtPeriodEnd = other.cancelAtPeriodEnd == null ? null : other.cancelAtPeriodEnd.copy();
        this.cancelledAt = other.cancelledAt == null ? null : other.cancelledAt.copy();
        this.stripeSubscriptionId = other.stripeSubscriptionId == null ? null : other.stripeSubscriptionId.copy();
        this.stripeCustomerId = other.stripeCustomerId == null ? null : other.stripeCustomerId.copy();
        this.paymentProviderConfigId = other.paymentProviderConfigId == null ? null : other.paymentProviderConfigId.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MembershipSubscriptionCriteria copy() {
        return new MembershipSubscriptionCriteria(this);
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

    public LongFilter getUserProfileId() {
        return userProfileId;
    }

    public LongFilter userProfileId() {
        if (userProfileId == null) {
            userProfileId = new LongFilter();
        }
        return userProfileId;
    }

    public void setUserProfileId(LongFilter userProfileId) {
        this.userProfileId = userProfileId;
    }

    public LongFilter getMembershipPlanId() {
        return membershipPlanId;
    }

    public LongFilter membershipPlanId() {
        if (membershipPlanId == null) {
            membershipPlanId = new LongFilter();
        }
        return membershipPlanId;
    }

    public void setMembershipPlanId(LongFilter membershipPlanId) {
        this.membershipPlanId = membershipPlanId;
    }

    public StringFilter getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public StringFilter subscriptionStatus() {
        if (subscriptionStatus == null) {
            subscriptionStatus = new StringFilter();
        }
        return subscriptionStatus;
    }

    public void setSubscriptionStatus(StringFilter subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public LocalDateFilter getCurrentPeriodStart() {
        return currentPeriodStart;
    }

    public LocalDateFilter currentPeriodStart() {
        if (currentPeriodStart == null) {
            currentPeriodStart = new LocalDateFilter();
        }
        return currentPeriodStart;
    }

    public void setCurrentPeriodStart(LocalDateFilter currentPeriodStart) {
        this.currentPeriodStart = currentPeriodStart;
    }

    public LocalDateFilter getCurrentPeriodEnd() {
        return currentPeriodEnd;
    }

    public LocalDateFilter currentPeriodEnd() {
        if (currentPeriodEnd == null) {
            currentPeriodEnd = new LocalDateFilter();
        }
        return currentPeriodEnd;
    }

    public void setCurrentPeriodEnd(LocalDateFilter currentPeriodEnd) {
        this.currentPeriodEnd = currentPeriodEnd;
    }

    public LocalDateFilter getTrialStart() {
        return trialStart;
    }

    public LocalDateFilter trialStart() {
        if (trialStart == null) {
            trialStart = new LocalDateFilter();
        }
        return trialStart;
    }

    public void setTrialStart(LocalDateFilter trialStart) {
        this.trialStart = trialStart;
    }

    public LocalDateFilter getTrialEnd() {
        return trialEnd;
    }

    public LocalDateFilter trialEnd() {
        if (trialEnd == null) {
            trialEnd = new LocalDateFilter();
        }
        return trialEnd;
    }

    public void setTrialEnd(LocalDateFilter trialEnd) {
        this.trialEnd = trialEnd;
    }

    public BooleanFilter getCancelAtPeriodEnd() {
        return cancelAtPeriodEnd;
    }

    public BooleanFilter cancelAtPeriodEnd() {
        if (cancelAtPeriodEnd == null) {
            cancelAtPeriodEnd = new BooleanFilter();
        }
        return cancelAtPeriodEnd;
    }

    public void setCancelAtPeriodEnd(BooleanFilter cancelAtPeriodEnd) {
        this.cancelAtPeriodEnd = cancelAtPeriodEnd;
    }

    public ZonedDateTimeFilter getCancelledAt() {
        return cancelledAt;
    }

    public ZonedDateTimeFilter cancelledAt() {
        if (cancelledAt == null) {
            cancelledAt = new ZonedDateTimeFilter();
        }
        return cancelledAt;
    }

    public void setCancelledAt(ZonedDateTimeFilter cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public StringFilter getStripeSubscriptionId() {
        return stripeSubscriptionId;
    }

    public StringFilter stripeSubscriptionId() {
        if (stripeSubscriptionId == null) {
            stripeSubscriptionId = new StringFilter();
        }
        return stripeSubscriptionId;
    }

    public void setStripeSubscriptionId(StringFilter stripeSubscriptionId) {
        this.stripeSubscriptionId = stripeSubscriptionId;
    }

    public StringFilter getStripeCustomerId() {
        return stripeCustomerId;
    }

    public StringFilter stripeCustomerId() {
        if (stripeCustomerId == null) {
            stripeCustomerId = new StringFilter();
        }
        return stripeCustomerId;
    }

    public void setStripeCustomerId(StringFilter stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }

    public LongFilter getPaymentProviderConfigId() {
        return paymentProviderConfigId;
    }

    public LongFilter paymentProviderConfigId() {
        if (paymentProviderConfigId == null) {
            paymentProviderConfigId = new LongFilter();
        }
        return paymentProviderConfigId;
    }

    public void setPaymentProviderConfigId(LongFilter paymentProviderConfigId) {
        this.paymentProviderConfigId = paymentProviderConfigId;
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
        final MembershipSubscriptionCriteria that = (MembershipSubscriptionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(userProfileId, that.userProfileId) &&
            Objects.equals(membershipPlanId, that.membershipPlanId) &&
            Objects.equals(subscriptionStatus, that.subscriptionStatus) &&
            Objects.equals(currentPeriodStart, that.currentPeriodStart) &&
            Objects.equals(currentPeriodEnd, that.currentPeriodEnd) &&
            Objects.equals(trialStart, that.trialStart) &&
            Objects.equals(trialEnd, that.trialEnd) &&
            Objects.equals(cancelAtPeriodEnd, that.cancelAtPeriodEnd) &&
            Objects.equals(cancelledAt, that.cancelledAt) &&
            Objects.equals(stripeSubscriptionId, that.stripeSubscriptionId) &&
            Objects.equals(stripeCustomerId, that.stripeCustomerId) &&
            Objects.equals(paymentProviderConfigId, that.paymentProviderConfigId) &&
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
            userProfileId,
            membershipPlanId,
            subscriptionStatus,
            currentPeriodStart,
            currentPeriodEnd,
            trialStart,
            trialEnd,
            cancelAtPeriodEnd,
            cancelledAt,
            stripeSubscriptionId,
            stripeCustomerId,
            paymentProviderConfigId,
            createdAt,
            updatedAt,
            distinct
        );
    }

    @Override
    public String toString() {
        return (
            "MembershipSubscriptionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
            (userProfileId != null ? "userProfileId=" + userProfileId + ", " : "") +
            (membershipPlanId != null ? "membershipPlanId=" + membershipPlanId + ", " : "") +
            (subscriptionStatus != null ? "subscriptionStatus=" + subscriptionStatus + ", " : "") +
            (stripeSubscriptionId != null ? "stripeSubscriptionId=" + stripeSubscriptionId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}"
        );
    }
}

