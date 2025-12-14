package com.nextjstemplate.domain;

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
 * A MembershipSubscriptionReconciliationLog.
 * Detailed audit trail for subscription reconciliation operations.
 */
@Entity
@Table(name = "membership_subscription_reconciliation_log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MembershipSubscriptionReconciliationLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {}, allowSetters = true)
    @JoinColumn(name = "subscription_id", nullable = false)
    private MembershipSubscription subscription;

    @NotNull
    @Size(max = 255)
    @Column(name = "tenant_id", length = 255, nullable = false)
    private String tenantId;

    @NotNull
    @Size(max = 50)
    @Column(name = "reconciliation_type", length = 50, nullable = false)
    private String reconciliationType;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Column(name = "local_period_start")
    private LocalDate localPeriodStart;

    @Column(name = "local_period_end")
    private LocalDate localPeriodEnd;

    @Column(name = "stripe_period_start")
    private LocalDate stripePeriodStart;

    @Column(name = "stripe_period_end")
    private LocalDate stripePeriodEnd;

    @Size(max = 20)
    @Column(name = "local_status", length = 20)
    private String localStatus;

    @Size(max = 20)
    @Column(name = "stripe_status", length = 20)
    private String stripeStatus;

    @Column(name = "changes_json", columnDefinition = "text")
    private String changesJson;

    @Column(name = "error_message", columnDefinition = "text")
    private String errorMessage;

    @NotNull
    @Column(name = "processed_at", nullable = false)
    private ZonedDateTime processedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MembershipSubscriptionReconciliationLog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MembershipSubscription getSubscription() {
        return this.subscription;
    }

    public void setSubscription(MembershipSubscription membershipSubscription) {
        this.subscription = membershipSubscription;
    }

    public MembershipSubscriptionReconciliationLog subscription(MembershipSubscription membershipSubscription) {
        this.setSubscription(membershipSubscription);
        return this;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public MembershipSubscriptionReconciliationLog tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getReconciliationType() {
        return this.reconciliationType;
    }

    public MembershipSubscriptionReconciliationLog reconciliationType(String reconciliationType) {
        this.setReconciliationType(reconciliationType);
        return this;
    }

    public void setReconciliationType(String reconciliationType) {
        this.reconciliationType = reconciliationType;
    }

    public String getStatus() {
        return this.status;
    }

    public MembershipSubscriptionReconciliationLog status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getLocalPeriodStart() {
        return this.localPeriodStart;
    }

    public MembershipSubscriptionReconciliationLog localPeriodStart(LocalDate localPeriodStart) {
        this.setLocalPeriodStart(localPeriodStart);
        return this;
    }

    public void setLocalPeriodStart(LocalDate localPeriodStart) {
        this.localPeriodStart = localPeriodStart;
    }

    public LocalDate getLocalPeriodEnd() {
        return this.localPeriodEnd;
    }

    public MembershipSubscriptionReconciliationLog localPeriodEnd(LocalDate localPeriodEnd) {
        this.setLocalPeriodEnd(localPeriodEnd);
        return this;
    }

    public void setLocalPeriodEnd(LocalDate localPeriodEnd) {
        this.localPeriodEnd = localPeriodEnd;
    }

    public LocalDate getStripePeriodStart() {
        return this.stripePeriodStart;
    }

    public MembershipSubscriptionReconciliationLog stripePeriodStart(LocalDate stripePeriodStart) {
        this.setStripePeriodStart(stripePeriodStart);
        return this;
    }

    public void setStripePeriodStart(LocalDate stripePeriodStart) {
        this.stripePeriodStart = stripePeriodStart;
    }

    public LocalDate getStripePeriodEnd() {
        return this.stripePeriodEnd;
    }

    public MembershipSubscriptionReconciliationLog stripePeriodEnd(LocalDate stripePeriodEnd) {
        this.setStripePeriodEnd(stripePeriodEnd);
        return this;
    }

    public void setStripePeriodEnd(LocalDate stripePeriodEnd) {
        this.stripePeriodEnd = stripePeriodEnd;
    }

    public String getLocalStatus() {
        return this.localStatus;
    }

    public MembershipSubscriptionReconciliationLog localStatus(String localStatus) {
        this.setLocalStatus(localStatus);
        return this;
    }

    public void setLocalStatus(String localStatus) {
        this.localStatus = localStatus;
    }

    public String getStripeStatus() {
        return this.stripeStatus;
    }

    public MembershipSubscriptionReconciliationLog stripeStatus(String stripeStatus) {
        this.setStripeStatus(stripeStatus);
        return this;
    }

    public void setStripeStatus(String stripeStatus) {
        this.stripeStatus = stripeStatus;
    }

    public String getChangesJson() {
        return this.changesJson;
    }

    public MembershipSubscriptionReconciliationLog changesJson(String changesJson) {
        this.setChangesJson(changesJson);
        return this;
    }

    public void setChangesJson(String changesJson) {
        this.changesJson = changesJson;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public MembershipSubscriptionReconciliationLog errorMessage(String errorMessage) {
        this.setErrorMessage(errorMessage);
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ZonedDateTime getProcessedAt() {
        return this.processedAt;
    }

    public MembershipSubscriptionReconciliationLog processedAt(ZonedDateTime processedAt) {
        this.setProcessedAt(processedAt);
        return this;
    }

    public void setProcessedAt(ZonedDateTime processedAt) {
        this.processedAt = processedAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MembershipSubscriptionReconciliationLog)) {
            return false;
        }
        return getId() != null && getId().equals(((MembershipSubscriptionReconciliationLog) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MembershipSubscriptionReconciliationLog{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", reconciliationType='" + getReconciliationType() + "'" +
            ", status='" + getStatus() + "'" +
            ", localPeriodStart='" + getLocalPeriodStart() + "'" +
            ", localPeriodEnd='" + getLocalPeriodEnd() + "'" +
            ", stripePeriodStart='" + getStripePeriodStart() + "'" +
            ", stripePeriodEnd='" + getStripePeriodEnd() + "'" +
            ", localStatus='" + getLocalStatus() + "'" +
            ", stripeStatus='" + getStripeStatus() + "'" +
            ", processedAt='" + getProcessedAt() + "'" +
            "}";
    }
}

