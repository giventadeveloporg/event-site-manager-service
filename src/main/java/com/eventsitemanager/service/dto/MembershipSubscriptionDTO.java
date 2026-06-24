package com.eventsitemanager.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.eventsitemanager.domain.MembershipSubscription} entity.
 * User subscription enrollments to membership plans.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MembershipSubscriptionDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String tenantId;

    @NotNull
    private Long userProfileId;

    @NotNull
    private Long membershipPlanId;

    @NotNull
    @Pattern(regexp = "ACTIVE|TRIAL|CANCELLED|PAST_DUE|EXPIRED|SUSPENDED")
    private String subscriptionStatus = "ACTIVE";

    @NotNull
    private LocalDate currentPeriodStart;

    @NotNull
    private LocalDate currentPeriodEnd;

    private LocalDate trialStart;

    private LocalDate trialEnd;

    @NotNull
    private Boolean cancelAtPeriodEnd = false;

    private ZonedDateTime cancelledAt;

    private String cancellationReason;

    @Size(max = 255)
    private String stripeSubscriptionId;

    @Size(max = 255)
    private String stripeCustomerId;

    private Long paymentProviderConfigId;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    private ZonedDateTime lastReconciliationAt;

    private ZonedDateTime lastStripeSyncAt;

    @Size(max = 20)
    @Pattern(regexp = "PENDING|SUCCESS|FAILED|SKIPPED")
    private String reconciliationStatus = "PENDING";

    private String reconciliationError;

    // Optional related DTOs for expanded responses
    private MembershipPlanDTO membershipPlan;

    private UserProfileDTO userProfile;

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

    public Long getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(Long userProfileId) {
        this.userProfileId = userProfileId;
    }

    public Long getMembershipPlanId() {
        return membershipPlanId;
    }

    public void setMembershipPlanId(Long membershipPlanId) {
        this.membershipPlanId = membershipPlanId;
    }

    public String getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public void setSubscriptionStatus(String subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public LocalDate getCurrentPeriodStart() {
        return currentPeriodStart;
    }

    public void setCurrentPeriodStart(LocalDate currentPeriodStart) {
        this.currentPeriodStart = currentPeriodStart;
    }

    public LocalDate getCurrentPeriodEnd() {
        return currentPeriodEnd;
    }

    public void setCurrentPeriodEnd(LocalDate currentPeriodEnd) {
        this.currentPeriodEnd = currentPeriodEnd;
    }

    public LocalDate getTrialStart() {
        return trialStart;
    }

    public void setTrialStart(LocalDate trialStart) {
        this.trialStart = trialStart;
    }

    public LocalDate getTrialEnd() {
        return trialEnd;
    }

    public void setTrialEnd(LocalDate trialEnd) {
        this.trialEnd = trialEnd;
    }

    public Boolean getCancelAtPeriodEnd() {
        return cancelAtPeriodEnd;
    }

    public void setCancelAtPeriodEnd(Boolean cancelAtPeriodEnd) {
        this.cancelAtPeriodEnd = cancelAtPeriodEnd;
    }

    public ZonedDateTime getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(ZonedDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public String getStripeSubscriptionId() {
        return stripeSubscriptionId;
    }

    public void setStripeSubscriptionId(String stripeSubscriptionId) {
        this.stripeSubscriptionId = stripeSubscriptionId;
    }

    public String getStripeCustomerId() {
        return stripeCustomerId;
    }

    public void setStripeCustomerId(String stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }

    public Long getPaymentProviderConfigId() {
        return paymentProviderConfigId;
    }

    public void setPaymentProviderConfigId(Long paymentProviderConfigId) {
        this.paymentProviderConfigId = paymentProviderConfigId;
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

    public ZonedDateTime getLastReconciliationAt() {
        return lastReconciliationAt;
    }

    public void setLastReconciliationAt(ZonedDateTime lastReconciliationAt) {
        this.lastReconciliationAt = lastReconciliationAt;
    }

    public ZonedDateTime getLastStripeSyncAt() {
        return lastStripeSyncAt;
    }

    public void setLastStripeSyncAt(ZonedDateTime lastStripeSyncAt) {
        this.lastStripeSyncAt = lastStripeSyncAt;
    }

    public String getReconciliationStatus() {
        return reconciliationStatus;
    }

    public void setReconciliationStatus(String reconciliationStatus) {
        this.reconciliationStatus = reconciliationStatus;
    }

    public String getReconciliationError() {
        return reconciliationError;
    }

    public void setReconciliationError(String reconciliationError) {
        this.reconciliationError = reconciliationError;
    }

    public MembershipPlanDTO getMembershipPlan() {
        return membershipPlan;
    }

    public void setMembershipPlan(MembershipPlanDTO membershipPlan) {
        this.membershipPlan = membershipPlan;
    }

    public UserProfileDTO getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfileDTO userProfile) {
        this.userProfile = userProfile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MembershipSubscriptionDTO)) {
            return false;
        }

        MembershipSubscriptionDTO membershipSubscriptionDTO = (MembershipSubscriptionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, membershipSubscriptionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MembershipSubscriptionDTO{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", userProfileId=" + getUserProfileId() +
            ", membershipPlanId=" + getMembershipPlanId() +
            ", subscriptionStatus='" + getSubscriptionStatus() + "'" +
            ", currentPeriodStart='" + getCurrentPeriodStart() + "'" +
            ", currentPeriodEnd='" + getCurrentPeriodEnd() + "'" +
            ", trialStart='" + getTrialStart() + "'" +
            ", trialEnd='" + getTrialEnd() + "'" +
            ", cancelAtPeriodEnd='" + getCancelAtPeriodEnd() + "'" +
            ", cancelledAt='" + getCancelledAt() + "'" +
            ", stripeSubscriptionId='" + getStripeSubscriptionId() + "'" +
            ", stripeCustomerId='" + getStripeCustomerId() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", lastReconciliationAt='" + getLastReconciliationAt() + "'" +
            ", lastStripeSyncAt='" + getLastStripeSyncAt() + "'" +
            ", reconciliationStatus='" + getReconciliationStatus() + "'" +
            "}";
    }
}
