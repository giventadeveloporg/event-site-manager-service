package com.eventsitemanager.service.dto;

import com.eventsitemanager.domain.enumeration.ManualPaymentMethodType;
import com.eventsitemanager.domain.enumeration.ManualPaymentStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link com.eventsitemanager.domain.ManualPaymentRequest} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ManualPaymentRequestDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String tenantId;

    private Long eventId;

    private Long ticketTransactionId;

    @Size(max = 255)
    private String requesterEmail;

    @Size(max = 255)
    private String requesterFirstName;

    @Size(max = 255)
    private String requesterLastName;

    @Size(max = 100)
    private String requesterPhone;

    @NotNull
    private BigDecimal amountDue;

    @NotNull
    private ManualPaymentMethodType paymentMethodType;

    @Size(max = 255)
    private String paymentHandle;

    private String paymentInstructions;

    @NotNull
    private ManualPaymentStatus status;

    @Size(max = 512)
    private String proofOfPaymentFileKey;

    @Size(max = 1024)
    private String proofOfPaymentFileUrl;

    private ZonedDateTime proofOfPaymentUploadedAt;

    private ZonedDateTime receivedAt;

    @Size(max = 255)
    private String receivedBy;

    private String voidReason;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    // Cart items for ticket creation (not persisted, only used in request)
    // Can be provided directly or reconstructed from selectedTickets
    private List<CartItemDTO> cart;

    // Alternative: Selected tickets in format: [{ ticketTypeId: number, quantity: number }]
    // This is a JSON string that can be parsed to reconstruct cart if cart is not provided
    // Frontend can send this if cart field is not available
    private String selectedTickets;

    // Nested data for response (not persisted)
    private EventTicketTransactionDTO ticketTransaction;
    private EventDetailsDTO event;

    public List<CartItemDTO> getCart() {
        return cart;
    }

    public void setCart(List<CartItemDTO> cart) {
        this.cart = cart;
    }

    public String getSelectedTickets() {
        return selectedTickets;
    }

    public void setSelectedTickets(String selectedTickets) {
        this.selectedTickets = selectedTickets;
    }

    public EventTicketTransactionDTO getTicketTransaction() {
        return ticketTransaction;
    }

    public void setTicketTransaction(EventTicketTransactionDTO ticketTransaction) {
        this.ticketTransaction = ticketTransaction;
    }

    public EventDetailsDTO getEvent() {
        return event;
    }

    public void setEvent(EventDetailsDTO event) {
        this.event = event;
    }

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

    public Long getTicketTransactionId() {
        return ticketTransactionId;
    }

    public void setTicketTransactionId(Long ticketTransactionId) {
        this.ticketTransactionId = ticketTransactionId;
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

    public ManualPaymentStatus getStatus() {
        return status;
    }

    public void setStatus(ManualPaymentStatus status) {
        this.status = status;
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
        if (!(o instanceof ManualPaymentRequestDTO)) {
            return false;
        }

        ManualPaymentRequestDTO other = (ManualPaymentRequestDTO) o;
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
            "ManualPaymentRequestDTO{" +
            "id=" +
            getId() +
            ", tenantId='" +
            getTenantId() +
            "'" +
            ", eventId=" +
            getEventId() +
            ", ticketTransactionId=" +
            getTicketTransactionId() +
            ", amountDue=" +
            getAmountDue() +
            ", paymentMethodType='" +
            getPaymentMethodType() +
            "'" +
            ", status='" +
            getStatus() +
            "'" +
            "}"
        );
    }
}
