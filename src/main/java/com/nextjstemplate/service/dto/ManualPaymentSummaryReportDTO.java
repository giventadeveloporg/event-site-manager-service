package com.nextjstemplate.service.dto;

import com.nextjstemplate.domain.enumeration.ManualPaymentMethodType;
import com.nextjstemplate.domain.enumeration.ManualPaymentStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.ManualPaymentSummaryReport} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ManualPaymentSummaryReportDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String tenantId;

    @NotNull
    private Long eventId;

    @NotNull
    private ManualPaymentMethodType paymentMethodType;

    @NotNull
    private ManualPaymentStatus status;

    @NotNull
    private BigDecimal totalAmount;

    @NotNull
    private Integer transactionCount;

    @NotNull
    private LocalDate snapshotDate;

    @NotNull
    private ZonedDateTime createdAt;

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

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public ManualPaymentMethodType getPaymentMethodType() {
        return paymentMethodType;
    }

    public void setPaymentMethodType(ManualPaymentMethodType paymentMethodType) {
        this.paymentMethodType = paymentMethodType;
    }

    public ManualPaymentStatus getStatus() {
        return status;
    }

    public void setStatus(ManualPaymentStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(Integer transactionCount) {
        this.transactionCount = transactionCount;
    }

    public LocalDate getSnapshotDate() {
        return snapshotDate;
    }

    public void setSnapshotDate(LocalDate snapshotDate) {
        this.snapshotDate = snapshotDate;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ManualPaymentSummaryReportDTO)) {
            return false;
        }

        ManualPaymentSummaryReportDTO other = (ManualPaymentSummaryReportDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return (
            "ManualPaymentSummaryReportDTO{" +
            "id=" +
            getId() +
            ", tenantId='" +
            getTenantId() +
            "'" +
            ", eventId=" +
            getEventId() +
            ", paymentMethodType='" +
            getPaymentMethodType() +
            "'" +
            ", status='" +
            getStatus() +
            "'" +
            ", snapshotDate='" +
            getSnapshotDate() +
            "'" +
            "}"
        );
    }
}
