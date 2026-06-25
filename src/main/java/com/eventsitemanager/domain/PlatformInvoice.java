package com.eventsitemanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
 * A PlatformInvoice.
 * Invoices for outstanding platform fees linked to settlements.
 */
@Entity
@Table(name = "platform_invoice")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlatformInvoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "public.sequence_generator", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "tenant_id", length = 255, nullable = false)
    private String tenantId;

    @NotNull
    @Size(max = 100)
    @Column(name = "invoice_number", length = 100, nullable = false, unique = true)
    private String invoiceNumber;

    @NotNull
    @Column(name = "invoice_date", nullable = false)
    private LocalDate invoiceDate;

    @NotNull
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @NotNull
    @Column(name = "total_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @NotNull
    @Column(name = "paid_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @NotNull
    @Column(name = "outstanding_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal outstandingAmount;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status = "DRAFT";

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {}, allowSetters = true)
    private PlatformSettlement settlementBatch;

    @Size(max = 50)
    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @Size(max = 255)
    @Column(name = "payment_reference", length = 255)
    private String paymentReference;

    @Column(name = "paid_at")
    private ZonedDateTime paidAt;

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

    public PlatformInvoice id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public PlatformInvoice tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getInvoiceNumber() {
        return this.invoiceNumber;
    }

    public PlatformInvoice invoiceNumber(String invoiceNumber) {
        this.setInvoiceNumber(invoiceNumber);
        return this;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public LocalDate getInvoiceDate() {
        return this.invoiceDate;
    }

    public PlatformInvoice invoiceDate(LocalDate invoiceDate) {
        this.setInvoiceDate(invoiceDate);
        return this;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public LocalDate getDueDate() {
        return this.dueDate;
    }

    public PlatformInvoice dueDate(LocalDate dueDate) {
        this.setDueDate(dueDate);
        return this;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public PlatformInvoice totalAmount(BigDecimal totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getPaidAmount() {
        return this.paidAmount;
    }

    public PlatformInvoice paidAmount(BigDecimal paidAmount) {
        this.setPaidAmount(paidAmount);
        return this;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public BigDecimal getOutstandingAmount() {
        return this.outstandingAmount;
    }

    public PlatformInvoice outstandingAmount(BigDecimal outstandingAmount) {
        this.setOutstandingAmount(outstandingAmount);
        return this;
    }

    public void setOutstandingAmount(BigDecimal outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    public String getStatus() {
        return this.status;
    }

    public PlatformInvoice status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PlatformSettlement getSettlementBatch() {
        return this.settlementBatch;
    }

    public void setSettlementBatch(PlatformSettlement platformSettlement) {
        this.settlementBatch = platformSettlement;
    }

    public PlatformInvoice settlementBatch(PlatformSettlement platformSettlement) {
        this.setSettlementBatch(platformSettlement);
        return this;
    }

    public String getPaymentMethod() {
        return this.paymentMethod;
    }

    public PlatformInvoice paymentMethod(String paymentMethod) {
        this.setPaymentMethod(paymentMethod);
        return this;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentReference() {
        return this.paymentReference;
    }

    public PlatformInvoice paymentReference(String paymentReference) {
        this.setPaymentReference(paymentReference);
        return this;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public ZonedDateTime getPaidAt() {
        return this.paidAt;
    }

    public PlatformInvoice paidAt(ZonedDateTime paidAt) {
        this.setPaidAt(paidAt);
        return this;
    }

    public void setPaidAt(ZonedDateTime paidAt) {
        this.paidAt = paidAt;
    }

    public String getNotes() {
        return this.notes;
    }

    public PlatformInvoice notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public PlatformInvoice createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public PlatformInvoice updatedAt(ZonedDateTime updatedAt) {
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
        if (!(o instanceof PlatformInvoice)) {
            return false;
        }
        return getId() != null && getId().equals(((PlatformInvoice) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "PlatformInvoice{" +
            "id=" +
            getId() +
            ", tenantId='" +
            getTenantId() +
            "'" +
            ", invoiceNumber='" +
            getInvoiceNumber() +
            "'" +
            ", invoiceDate='" +
            getInvoiceDate() +
            "'" +
            ", dueDate='" +
            getDueDate() +
            "'" +
            ", totalAmount=" +
            getTotalAmount() +
            ", paidAmount=" +
            getPaidAmount() +
            ", outstandingAmount=" +
            getOutstandingAmount() +
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
