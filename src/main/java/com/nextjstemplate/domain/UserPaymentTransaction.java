package com.nextjstemplate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserPaymentTransaction.
 */
@Entity
@Table(name = "user_payment_transaction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserPaymentTransaction implements Serializable {

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

    @Size(max = 20)
    @Column(name = "transaction_type", length = 20)
    private String transactionType;

    @NotNull
    @Column(name = "amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal amount;

    @NotNull
    @Size(max = 3)
    @Column(name = "currency", length = 3, nullable = false)
    private String currency;

    @Size(max = 255)
    @Column(name = "stripe_payment_intent_id", length = 255)
    private String stripePaymentIntentId;

    @Size(max = 255)
    @Column(name = "stripe_transfer_group", length = 255)
    private String stripeTransferGroup;

    @Column(name = "platform_fee_amount", precision = 21, scale = 2)
    private BigDecimal platformFeeAmount;

    @Column(name = "tenant_amount", precision = 21, scale = 2)
    private BigDecimal tenantAmount;

    @Size(max = 20)
    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "metadata", columnDefinition = "text")
    private String metadata;

    @Size(max = 255)
    @Column(name = "external_transaction_id", length = 255)
    private String externalTransactionId;

    @Size(max = 100)
    @Column(name = "payment_method", length = 100)
    private String paymentMethod;

    @Column(name = "failure_reason", columnDefinition = "text")
    private String failureReason;

    @Column(name = "processing_fee", precision = 21, scale = 2)
    private BigDecimal processingFee;

    @Column(name = "reconciliation_date")
    private java.time.LocalDate reconciliationDate;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "createdBy", "eventType" }, allowSetters = true)
    private EventDetails event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "event", "ticketType", "user" }, allowSetters = true)
    private EventTicketTransaction ticketTransaction;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserPaymentTransaction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public UserPaymentTransaction tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTransactionType() {
        return this.transactionType;
    }

    public UserPaymentTransaction transactionType(String transactionType) {
        this.setTransactionType(transactionType);
        return this;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public UserPaymentTransaction amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return this.currency;
    }

    public UserPaymentTransaction currency(String currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStripePaymentIntentId() {
        return this.stripePaymentIntentId;
    }

    public UserPaymentTransaction stripePaymentIntentId(String stripePaymentIntentId) {
        this.setStripePaymentIntentId(stripePaymentIntentId);
        return this;
    }

    public void setStripePaymentIntentId(String stripePaymentIntentId) {
        this.stripePaymentIntentId = stripePaymentIntentId;
    }

    public String getStripeTransferGroup() {
        return this.stripeTransferGroup;
    }

    public UserPaymentTransaction stripeTransferGroup(String stripeTransferGroup) {
        this.setStripeTransferGroup(stripeTransferGroup);
        return this;
    }

    public void setStripeTransferGroup(String stripeTransferGroup) {
        this.stripeTransferGroup = stripeTransferGroup;
    }

    public BigDecimal getPlatformFeeAmount() {
        return this.platformFeeAmount;
    }

    public UserPaymentTransaction platformFeeAmount(BigDecimal platformFeeAmount) {
        this.setPlatformFeeAmount(platformFeeAmount);
        return this;
    }

    public void setPlatformFeeAmount(BigDecimal platformFeeAmount) {
        this.platformFeeAmount = platformFeeAmount;
    }

    public BigDecimal getTenantAmount() {
        return this.tenantAmount;
    }

    public UserPaymentTransaction tenantAmount(BigDecimal tenantAmount) {
        this.setTenantAmount(tenantAmount);
        return this;
    }

    public void setTenantAmount(BigDecimal tenantAmount) {
        this.tenantAmount = tenantAmount;
    }

    public String getStatus() {
        return this.status;
    }

    public UserPaymentTransaction status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMetadata() {
        return this.metadata;
    }

    public UserPaymentTransaction metadata(String metadata) {
        this.setMetadata(metadata);
        return this;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getExternalTransactionId() {
        return this.externalTransactionId;
    }

    public UserPaymentTransaction externalTransactionId(String externalTransactionId) {
        this.setExternalTransactionId(externalTransactionId);
        return this;
    }

    public void setExternalTransactionId(String externalTransactionId) {
        this.externalTransactionId = externalTransactionId;
    }

    public String getPaymentMethod() {
        return this.paymentMethod;
    }

    public UserPaymentTransaction paymentMethod(String paymentMethod) {
        this.setPaymentMethod(paymentMethod);
        return this;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getFailureReason() {
        return this.failureReason;
    }

    public UserPaymentTransaction failureReason(String failureReason) {
        this.setFailureReason(failureReason);
        return this;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public BigDecimal getProcessingFee() {
        return this.processingFee;
    }

    public UserPaymentTransaction processingFee(BigDecimal processingFee) {
        this.setProcessingFee(processingFee);
        return this;
    }

    public void setProcessingFee(BigDecimal processingFee) {
        this.processingFee = processingFee;
    }

    public java.time.LocalDate getReconciliationDate() {
        return this.reconciliationDate;
    }

    public UserPaymentTransaction reconciliationDate(java.time.LocalDate reconciliationDate) {
        this.setReconciliationDate(reconciliationDate);
        return this;
    }

    public void setReconciliationDate(java.time.LocalDate reconciliationDate) {
        this.reconciliationDate = reconciliationDate;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public UserPaymentTransaction createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public UserPaymentTransaction updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public EventDetails getEvent() {
        return this.event;
    }

    public void setEvent(EventDetails eventDetails) {
        this.event = eventDetails;
    }

    public UserPaymentTransaction event(EventDetails eventDetails) {
        this.setEvent(eventDetails);
        return this;
    }

    public EventTicketTransaction getTicketTransaction() {
        return this.ticketTransaction;
    }

    public void setTicketTransaction(EventTicketTransaction eventTicketTransaction) {
        this.ticketTransaction = eventTicketTransaction;
    }

    public UserPaymentTransaction ticketTransaction(EventTicketTransaction eventTicketTransaction) {
        this.setTicketTransaction(eventTicketTransaction);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserPaymentTransaction)) {
            return false;
        }
        return getId() != null && getId().equals(((UserPaymentTransaction) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserPaymentTransaction{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", transactionType='" + getTransactionType() + "'" +
            ", amount=" + getAmount() +
            ", currency='" + getCurrency() + "'" +
            ", stripePaymentIntentId='" + getStripePaymentIntentId() + "'" +
            ", stripeTransferGroup='" + getStripeTransferGroup() + "'" +
            ", platformFeeAmount=" + getPlatformFeeAmount() +
            ", tenantAmount=" + getTenantAmount() +
            ", status='" + getStatus() + "'" +
            ", metadata='" + getMetadata() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
