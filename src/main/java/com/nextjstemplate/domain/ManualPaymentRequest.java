package com.nextjstemplate.domain;

import com.nextjstemplate.domain.enumeration.ManualPaymentMethodType;
import com.nextjstemplate.domain.enumeration.ManualPaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ManualPaymentRequest.
 * Stores fee-free manual payment requests and proof-of-payment metadata.
 */
@Entity
@Table(name = "manual_payment_request")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ManualPaymentRequest implements Serializable {

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

    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "ticket_transaction_id")
    private Long ticketTransactionId;

    @Size(max = 255)
    @Column(name = "requester_email", length = 255)
    private String requesterEmail;

    @Size(max = 255)
    @Column(name = "requester_first_name", length = 255)
    private String requesterFirstName;

    @Size(max = 255)
    @Column(name = "requester_last_name", length = 255)
    private String requesterLastName;

    @Size(max = 100)
    @Column(name = "requester_phone", length = 100)
    private String requesterPhone;

    @NotNull
    @Column(name = "amount_due", precision = 21, scale = 2, nullable = false)
    private BigDecimal amountDue;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method_type", length = 50, nullable = false)
    private ManualPaymentMethodType paymentMethodType;

    @Size(max = 255)
    @Column(name = "payment_handle", length = 255)
    private String paymentHandle;

    @Column(name = "payment_instructions", columnDefinition = "TEXT")
    private String paymentInstructions;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30, nullable = false)
    private ManualPaymentStatus status = ManualPaymentStatus.REQUESTED;

    @Size(max = 512)
    @Column(name = "proof_of_payment_file_key", length = 512)
    private String proofOfPaymentFileKey;

    @Size(max = 1024)
    @Column(name = "proof_of_payment_file_url", length = 1024)
    private String proofOfPaymentFileUrl;

    @Column(name = "proof_of_payment_uploaded_at")
    private ZonedDateTime proofOfPaymentUploadedAt;

    @Column(name = "received_at")
    private ZonedDateTime receivedAt;

    @Size(max = 255)
    @Column(name = "received_by", length = 255)
    private String receivedBy;

    @Column(name = "void_reason", columnDefinition = "TEXT")
    private String voidReason;

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

    public void setId(Long id) {
        this.id = id;
    }

    public ManualPaymentRequest id(Long id) {
        this.setId(id);
        return this;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public ManualPaymentRequest tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public ManualPaymentRequest eventId(Long eventId) {
        this.setEventId(eventId);
        return this;
    }

    public Long getTicketTransactionId() {
        return ticketTransactionId;
    }

    public void setTicketTransactionId(Long ticketTransactionId) {
        this.ticketTransactionId = ticketTransactionId;
    }

    public ManualPaymentRequest ticketTransactionId(Long ticketTransactionId) {
        this.setTicketTransactionId(ticketTransactionId);
        return this;
    }

    public String getRequesterEmail() {
        return requesterEmail;
    }

    public void setRequesterEmail(String requesterEmail) {
        this.requesterEmail = requesterEmail;
    }

    public String getRequesterFirstName() {
        return requesterFirstName;
    }

    public void setRequesterFirstName(String requesterFirstName) {
        this.requesterFirstName = requesterFirstName;
    }

    public String getRequesterLastName() {
        return requesterLastName;
    }

    public void setRequesterLastName(String requesterLastName) {
        this.requesterLastName = requesterLastName;
    }

    public String getRequesterPhone() {
        return requesterPhone;
    }

    public void setRequesterPhone(String requesterPhone) {
        this.requesterPhone = requesterPhone;
    }

    public BigDecimal getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(BigDecimal amountDue) {
        this.amountDue = amountDue;
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

    public String getPaymentHandle() {
        return paymentHandle;
    }

    public void setPaymentHandle(String paymentHandle) {
        this.paymentHandle = paymentHandle;
    }

    public String getPaymentInstructions() {
        return paymentInstructions;
    }

    public void setPaymentInstructions(String paymentInstructions) {
        this.paymentInstructions = paymentInstructions;
    }

    public String getProofOfPaymentFileKey() {
        return proofOfPaymentFileKey;
    }

    public void setProofOfPaymentFileKey(String proofOfPaymentFileKey) {
        this.proofOfPaymentFileKey = proofOfPaymentFileKey;
    }

    public String getProofOfPaymentFileUrl() {
        return proofOfPaymentFileUrl;
    }

    public void setProofOfPaymentFileUrl(String proofOfPaymentFileUrl) {
        this.proofOfPaymentFileUrl = proofOfPaymentFileUrl;
    }

    public ZonedDateTime getProofOfPaymentUploadedAt() {
        return proofOfPaymentUploadedAt;
    }

    public void setProofOfPaymentUploadedAt(ZonedDateTime proofOfPaymentUploadedAt) {
        this.proofOfPaymentUploadedAt = proofOfPaymentUploadedAt;
    }

    public ZonedDateTime getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(ZonedDateTime receivedAt) {
        this.receivedAt = receivedAt;
    }

    public String getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(String receivedBy) {
        this.receivedBy = receivedBy;
    }

    public String getVoidReason() {
        return voidReason;
    }

    public void setVoidReason(String voidReason) {
        this.voidReason = voidReason;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ManualPaymentRequest)) {
            return false;
        }
        return getId() != null && getId().equals(((ManualPaymentRequest) o).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return (
            "ManualPaymentRequest{" +
            "id=" +
            getId() +
            ", tenantId='" +
            getTenantId() +
            "'" +
            ", eventId=" +
            getEventId() +
            ", ticketTransactionId=" +
            getTicketTransactionId() +
            ", requesterEmail='" +
            getRequesterEmail() +
            "'" +
            ", amountDue=" +
            getAmountDue() +
            ", paymentMethodType='" +
            getPaymentMethodType() +
            "'" +
            ", status='" +
            getStatus() +
            "'" +
            ", proofOfPaymentFileKey='" +
            getProofOfPaymentFileKey() +
            "'" +
            ", proofOfPaymentFileUrl='" +
            getProofOfPaymentFileUrl() +
            "'" +
            ", proofOfPaymentUploadedAt='" +
            getProofOfPaymentUploadedAt() +
            "'" +
            ", receivedAt='" +
            getReceivedAt() +
            "'" +
            ", receivedBy='" +
            getReceivedBy() +
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
