package com.eventsitemanager.domain;

import com.eventsitemanager.domain.enumeration.PaymentProvider;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PlatformSettlement.
 * Aggregated settlement totals per tenant/provider/day for fee reconciliation.
 */
@Entity
@Table(name = "platform_settlement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlatformSettlement implements Serializable {

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
    @Enumerated(EnumType.STRING)
    @Column(name = "provider_name", length = 50, nullable = false)
    private PaymentProvider providerName;

    @NotNull
    @Column(name = "settlement_date", nullable = false)
    private LocalDate settlementDate;

    @NotNull
    @Column(name = "gross_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal grossAmount = BigDecimal.ZERO;

    @NotNull
    @Column(name = "processing_fee_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal processingFeeAmount = BigDecimal.ZERO;

    @NotNull
    @Column(name = "platform_fee_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal platformFeeAmount = BigDecimal.ZERO;

    @NotNull
    @Column(name = "net_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal netAmount = BigDecimal.ZERO;

    @NotNull
    @Column(name = "transaction_count", nullable = false)
    private Integer transactionCount = 0;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status = "PENDING";

    @Size(max = 255)
    @Column(name = "settlement_reference", length = 255)
    private String settlementReference;

    @Size(max = 255)
    @Column(name = "provider_settlement_id", length = 255)
    private String providerSettlementId;

    @Size(max = 2048)
    @Column(name = "settlement_file_url", length = 2048)
    private String settlementFileUrl;

    @Column(name = "notes", columnDefinition = "text")
    private String notes;

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

    public PlatformSettlement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public PlatformSettlement tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public PaymentProvider getProviderName() {
        return this.providerName;
    }

    public PlatformSettlement providerName(PaymentProvider providerName) {
        this.setProviderName(providerName);
        return this;
    }

    public void setProviderName(PaymentProvider providerName) {
        this.providerName = providerName;
    }

    public LocalDate getSettlementDate() {
        return this.settlementDate;
    }

    public PlatformSettlement settlementDate(LocalDate settlementDate) {
        this.setSettlementDate(settlementDate);
        return this;
    }

    public void setSettlementDate(LocalDate settlementDate) {
        this.settlementDate = settlementDate;
    }

    public BigDecimal getGrossAmount() {
        return this.grossAmount;
    }

    public PlatformSettlement grossAmount(BigDecimal grossAmount) {
        this.setGrossAmount(grossAmount);
        return this;
    }

    public void setGrossAmount(BigDecimal grossAmount) {
        this.grossAmount = grossAmount;
    }

    public BigDecimal getProcessingFeeAmount() {
        return this.processingFeeAmount;
    }

    public PlatformSettlement processingFeeAmount(BigDecimal processingFeeAmount) {
        this.setProcessingFeeAmount(processingFeeAmount);
        return this;
    }

    public void setProcessingFeeAmount(BigDecimal processingFeeAmount) {
        this.processingFeeAmount = processingFeeAmount;
    }

    public BigDecimal getPlatformFeeAmount() {
        return this.platformFeeAmount;
    }

    public PlatformSettlement platformFeeAmount(BigDecimal platformFeeAmount) {
        this.setPlatformFeeAmount(platformFeeAmount);
        return this;
    }

    public void setPlatformFeeAmount(BigDecimal platformFeeAmount) {
        this.platformFeeAmount = platformFeeAmount;
    }

    public BigDecimal getNetAmount() {
        return this.netAmount;
    }

    public PlatformSettlement netAmount(BigDecimal netAmount) {
        this.setNetAmount(netAmount);
        return this;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    public Integer getTransactionCount() {
        return this.transactionCount;
    }

    public PlatformSettlement transactionCount(Integer transactionCount) {
        this.setTransactionCount(transactionCount);
        return this;
    }

    public void setTransactionCount(Integer transactionCount) {
        this.transactionCount = transactionCount;
    }

    public String getStatus() {
        return this.status;
    }

    public PlatformSettlement status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSettlementReference() {
        return this.settlementReference;
    }

    public PlatformSettlement settlementReference(String settlementReference) {
        this.setSettlementReference(settlementReference);
        return this;
    }

    public void setSettlementReference(String settlementReference) {
        this.settlementReference = settlementReference;
    }

    public String getProviderSettlementId() {
        return this.providerSettlementId;
    }

    public PlatformSettlement providerSettlementId(String providerSettlementId) {
        this.setProviderSettlementId(providerSettlementId);
        return this;
    }

    public void setProviderSettlementId(String providerSettlementId) {
        this.providerSettlementId = providerSettlementId;
    }

    public String getSettlementFileUrl() {
        return this.settlementFileUrl;
    }

    public PlatformSettlement settlementFileUrl(String settlementFileUrl) {
        this.setSettlementFileUrl(settlementFileUrl);
        return this;
    }

    public void setSettlementFileUrl(String settlementFileUrl) {
        this.settlementFileUrl = settlementFileUrl;
    }

    public String getNotes() {
        return this.notes;
    }

    public PlatformSettlement notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public PlatformSettlement createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public PlatformSettlement updatedAt(ZonedDateTime updatedAt) {
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
        if (!(o instanceof PlatformSettlement)) {
            return false;
        }
        return getId() != null && getId().equals(((PlatformSettlement) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "PlatformSettlement{" +
            "id=" +
            getId() +
            ", tenantId='" +
            getTenantId() +
            "'" +
            ", providerName='" +
            getProviderName() +
            "'" +
            ", settlementDate='" +
            getSettlementDate() +
            "'" +
            ", grossAmount=" +
            getGrossAmount() +
            ", processingFeeAmount=" +
            getProcessingFeeAmount() +
            ", platformFeeAmount=" +
            getPlatformFeeAmount() +
            ", netAmount=" +
            getNetAmount() +
            ", transactionCount=" +
            getTransactionCount() +
            ", status='" +
            getStatus() +
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
