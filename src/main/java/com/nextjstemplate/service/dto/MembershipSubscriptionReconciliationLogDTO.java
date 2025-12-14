package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.MembershipSubscriptionReconciliationLog} entity.
 * Detailed audit trail for subscription reconciliation operations.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MembershipSubscriptionReconciliationLogDTO implements Serializable {

    private Long id;

    @NotNull
    private Long subscriptionId;

    @NotNull
    @Size(max = 255)
    private String tenantId;

    @NotNull
    @Size(max = 50)
    @Pattern(regexp = "BATCH_RENEWAL|DAILY_RECONCILIATION|WEBHOOK")
    private String reconciliationType;

    @NotNull
    @Size(max = 20)
    @Pattern(regexp = "SUCCESS|FAILED|SKIPPED")
    private String status;

    private LocalDate localPeriodStart;

    private LocalDate localPeriodEnd;

    private LocalDate stripePeriodStart;

    private LocalDate stripePeriodEnd;

    @Size(max = 20)
    private String localStatus;

    @Size(max = 20)
    private String stripeStatus;

    private String changesJson;

    private String errorMessage;

    @NotNull
    private ZonedDateTime processedAt;

    // Optional related DTOs for expanded responses
    private MembershipSubscriptionDTO subscription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getReconciliationType() {
        return reconciliationType;
    }

    public void setReconciliationType(String reconciliationType) {
        this.reconciliationType = reconciliationType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getLocalPeriodStart() {
        return localPeriodStart;
    }

    public void setLocalPeriodStart(LocalDate localPeriodStart) {
        this.localPeriodStart = localPeriodStart;
    }

    public LocalDate getLocalPeriodEnd() {
        return localPeriodEnd;
    }

    public void setLocalPeriodEnd(LocalDate localPeriodEnd) {
        this.localPeriodEnd = localPeriodEnd;
    }

    public LocalDate getStripePeriodStart() {
        return stripePeriodStart;
    }

    public void setStripePeriodStart(LocalDate stripePeriodStart) {
        this.stripePeriodStart = stripePeriodStart;
    }

    public LocalDate getStripePeriodEnd() {
        return stripePeriodEnd;
    }

    public void setStripePeriodEnd(LocalDate stripePeriodEnd) {
        this.stripePeriodEnd = stripePeriodEnd;
    }

    public String getLocalStatus() {
        return localStatus;
    }

    public void setLocalStatus(String localStatus) {
        this.localStatus = localStatus;
    }

    public String getStripeStatus() {
        return stripeStatus;
    }

    public void setStripeStatus(String stripeStatus) {
        this.stripeStatus = stripeStatus;
    }

    public String getChangesJson() {
        return changesJson;
    }

    public void setChangesJson(String changesJson) {
        this.changesJson = changesJson;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ZonedDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(ZonedDateTime processedAt) {
        this.processedAt = processedAt;
    }

    public MembershipSubscriptionDTO getSubscription() {
        return subscription;
    }

    public void setSubscription(MembershipSubscriptionDTO subscription) {
        this.subscription = subscription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MembershipSubscriptionReconciliationLogDTO)) {
            return false;
        }

        MembershipSubscriptionReconciliationLogDTO membershipSubscriptionReconciliationLogDTO =
            (MembershipSubscriptionReconciliationLogDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, membershipSubscriptionReconciliationLogDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MembershipSubscriptionReconciliationLogDTO{" +
            "id=" + getId() +
            ", subscriptionId=" + getSubscriptionId() +
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

