package com.eventsitemanager.domain;

import com.eventsitemanager.domain.enumeration.ManualPaymentMethodType;
import com.eventsitemanager.domain.enumeration.ManualPaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * A ManualPaymentSummaryReport.
 * Daily summary of manual payments grouped by event, status, and method.
 */
@Entity
@Table(name = "manual_payment_summary_report")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ManualPaymentSummaryReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "manualPaymentSummaryReportSeq")
    @SequenceGenerator(
        name = "manualPaymentSummaryReportSeq",
        sequenceName = "public.manual_payment_summary_report_id_seq",
        allocationSize = 1
    )
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "tenant_id", length = 255, nullable = false)
    private String tenantId;

    @NotNull
    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method_type", length = 80, nullable = false)
    private ManualPaymentMethodType paymentMethodType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30, nullable = false)
    private ManualPaymentStatus status;

    @NotNull
    @Column(name = "total_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @NotNull
    @Column(name = "transaction_count", nullable = false)
    private Integer transactionCount = 0;

    @NotNull
    @Column(name = "snapshot_date", nullable = false)
    private LocalDate snapshotDate;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
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
        if (!(o instanceof ManualPaymentSummaryReport)) {
            return false;
        }
        return getId() != null && getId().equals(((ManualPaymentSummaryReport) o).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return (
            "ManualPaymentSummaryReport{" +
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
            ", totalAmount=" +
            getTotalAmount() +
            ", transactionCount=" +
            getTransactionCount() +
            ", snapshotDate='" +
            getSnapshotDate() +
            "'" +
            ", createdAt='" +
            getCreatedAt() +
            "'" +
            "}"
        );
    }
}
