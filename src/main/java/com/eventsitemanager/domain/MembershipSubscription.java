package com.eventsitemanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MembershipSubscription.
 * User subscription enrollments to membership plans.
 */
@Entity
@Table(name = "membership_subscription")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MembershipSubscription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "public.sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "tenant_id", length = 255, nullable = false)
    private String tenantId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {}, allowSetters = true)
    private UserProfile userProfile;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {}, allowSetters = true)
    private MembershipPlan membershipPlan;

    @NotNull
    @Size(max = 20)
    @Column(name = "subscription_status", length = 20, nullable = false)
    private String subscriptionStatus = "ACTIVE";

    @NotNull
    @Column(name = "current_period_start", nullable = false)
    private LocalDate currentPeriodStart;

    @NotNull
    @Column(name = "current_period_end", nullable = false)
    private LocalDate currentPeriodEnd;

    @Column(name = "trial_start")
    private LocalDate trialStart;

    @Column(name = "trial_end")
    private LocalDate trialEnd;

    @NotNull
    @Column(name = "cancel_at_period_end", nullable = false)
    private Boolean cancelAtPeriodEnd = false;

    @Column(name = "cancelled_at")
    private ZonedDateTime cancelledAt;

    @Column(name = "cancellation_reason", columnDefinition = "text")
    private String cancellationReason;

    @Size(max = 255)
    @Column(name = "stripe_subscription_id", length = 255)
    private String stripeSubscriptionId;

    @Size(max = 255)
    @Column(name = "stripe_customer_id", length = 255)
    private String stripeCustomerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {}, allowSetters = true)
    private PaymentProviderConfig paymentProviderConfig;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @Column(name = "last_reconciliation_at")
    private ZonedDateTime lastReconciliationAt;

    @Column(name = "last_stripe_sync_at")
    private ZonedDateTime lastStripeSyncAt;

    @Size(max = 20)
    @Column(name = "reconciliation_status", length = 20)
    private String reconciliationStatus = "PENDING";

    @Column(name = "reconciliation_error", columnDefinition = "text")
    private String reconciliationError;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MembershipSubscription id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public MembershipSubscription tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public UserProfile getUserProfile() {
        return this.userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public MembershipSubscription userProfile(UserProfile userProfile) {
        this.setUserProfile(userProfile);
        return this;
    }

    public MembershipPlan getMembershipPlan() {
        return this.membershipPlan;
    }

    public void setMembershipPlan(MembershipPlan membershipPlan) {
        this.membershipPlan = membershipPlan;
    }

    public MembershipSubscription membershipPlan(MembershipPlan membershipPlan) {
        this.setMembershipPlan(membershipPlan);
        return this;
    }

    public String getSubscriptionStatus() {
        return this.subscriptionStatus;
    }

    public MembershipSubscription subscriptionStatus(String subscriptionStatus) {
        this.setSubscriptionStatus(subscriptionStatus);
        return this;
    }

    public void setSubscriptionStatus(String subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public LocalDate getCurrentPeriodStart() {
        return this.currentPeriodStart;
    }

    public MembershipSubscription currentPeriodStart(LocalDate currentPeriodStart) {
        this.setCurrentPeriodStart(currentPeriodStart);
        return this;
    }

    public void setCurrentPeriodStart(LocalDate currentPeriodStart) {
        this.currentPeriodStart = currentPeriodStart;
    }

    public LocalDate getCurrentPeriodEnd() {
        return this.currentPeriodEnd;
    }

    public MembershipSubscription currentPeriodEnd(LocalDate currentPeriodEnd) {
        this.setCurrentPeriodEnd(currentPeriodEnd);
        return this;
    }

    public void setCurrentPeriodEnd(LocalDate currentPeriodEnd) {
        this.currentPeriodEnd = currentPeriodEnd;
    }

    public LocalDate getTrialStart() {
        return this.trialStart;
    }

    public MembershipSubscription trialStart(LocalDate trialStart) {
        this.setTrialStart(trialStart);
        return this;
    }

    public void setTrialStart(LocalDate trialStart) {
        this.trialStart = trialStart;
    }

    public LocalDate getTrialEnd() {
        return this.trialEnd;
    }

    public MembershipSubscription trialEnd(LocalDate trialEnd) {
        this.setTrialEnd(trialEnd);
        return this;
    }

    public void setTrialEnd(LocalDate trialEnd) {
        this.trialEnd = trialEnd;
    }

    public Boolean getCancelAtPeriodEnd() {
        return this.cancelAtPeriodEnd;
    }

    public MembershipSubscription cancelAtPeriodEnd(Boolean cancelAtPeriodEnd) {
        this.setCancelAtPeriodEnd(cancelAtPeriodEnd);
        return this;
    }

    public void setCancelAtPeriodEnd(Boolean cancelAtPeriodEnd) {
        this.cancelAtPeriodEnd = cancelAtPeriodEnd;
    }

    public ZonedDateTime getCancelledAt() {
        return this.cancelledAt;
    }

    public MembershipSubscription cancelledAt(ZonedDateTime cancelledAt) {
        this.setCancelledAt(cancelledAt);
        return this;
    }

    public void setCancelledAt(ZonedDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public String getCancellationReason() {
        return this.cancellationReason;
    }

    public MembershipSubscription cancellationReason(String cancellationReason) {
        this.setCancellationReason(cancellationReason);
        return this;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public String getStripeSubscriptionId() {
        return this.stripeSubscriptionId;
    }

    public MembershipSubscription stripeSubscriptionId(String stripeSubscriptionId) {
        this.setStripeSubscriptionId(stripeSubscriptionId);
        return this;
    }

    public void setStripeSubscriptionId(String stripeSubscriptionId) {
        this.stripeSubscriptionId = stripeSubscriptionId;
    }

    public String getStripeCustomerId() {
        return this.stripeCustomerId;
    }

    public MembershipSubscription stripeCustomerId(String stripeCustomerId) {
        this.setStripeCustomerId(stripeCustomerId);
        return this;
    }

    public void setStripeCustomerId(String stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }

    public PaymentProviderConfig getPaymentProviderConfig() {
        return this.paymentProviderConfig;
    }

    public void setPaymentProviderConfig(PaymentProviderConfig paymentProviderConfig) {
        this.paymentProviderConfig = paymentProviderConfig;
    }

    public MembershipSubscription paymentProviderConfig(PaymentProviderConfig paymentProviderConfig) {
        this.setPaymentProviderConfig(paymentProviderConfig);
        return this;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public MembershipSubscription createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public MembershipSubscription updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ZonedDateTime getLastReconciliationAt() {
        return this.lastReconciliationAt;
    }

    public MembershipSubscription lastReconciliationAt(ZonedDateTime lastReconciliationAt) {
        this.setLastReconciliationAt(lastReconciliationAt);
        return this;
    }

    public void setLastReconciliationAt(ZonedDateTime lastReconciliationAt) {
        this.lastReconciliationAt = lastReconciliationAt;
    }

    public ZonedDateTime getLastStripeSyncAt() {
        return this.lastStripeSyncAt;
    }

    public MembershipSubscription lastStripeSyncAt(ZonedDateTime lastStripeSyncAt) {
        this.setLastStripeSyncAt(lastStripeSyncAt);
        return this;
    }

    public void setLastStripeSyncAt(ZonedDateTime lastStripeSyncAt) {
        this.lastStripeSyncAt = lastStripeSyncAt;
    }

    public String getReconciliationStatus() {
        return this.reconciliationStatus;
    }

    public MembershipSubscription reconciliationStatus(String reconciliationStatus) {
        this.setReconciliationStatus(reconciliationStatus);
        return this;
    }

    public void setReconciliationStatus(String reconciliationStatus) {
        this.reconciliationStatus = reconciliationStatus;
    }

    public String getReconciliationError() {
        return this.reconciliationError;
    }

    public MembershipSubscription reconciliationError(String reconciliationError) {
        this.setReconciliationError(reconciliationError);
        return this;
    }

    public void setReconciliationError(String reconciliationError) {
        this.reconciliationError = reconciliationError;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MembershipSubscription)) {
            return false;
        }
        return getId() != null && getId().equals(((MembershipSubscription) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "MembershipSubscription{" +
            "id=" +
            getId() +
            ", tenantId='" +
            getTenantId() +
            "'" +
            ", subscriptionStatus='" +
            getSubscriptionStatus() +
            "'" +
            ", currentPeriodStart='" +
            getCurrentPeriodStart() +
            "'" +
            ", currentPeriodEnd='" +
            getCurrentPeriodEnd() +
            "'" +
            ", cancelAtPeriodEnd='" +
            getCancelAtPeriodEnd() +
            "'" +
            ", createdAt='" +
            getCreatedAt() +
            "'" +
            ", updatedAt='" +
            getUpdatedAt() +
            "'" +
            ", lastReconciliationAt='" +
            getLastReconciliationAt() +
            "'" +
            ", lastStripeSyncAt='" +
            getLastStripeSyncAt() +
            "'" +
            ", reconciliationStatus='" +
            getReconciliationStatus() +
            "'" +
            "}"
        );
    }
}
