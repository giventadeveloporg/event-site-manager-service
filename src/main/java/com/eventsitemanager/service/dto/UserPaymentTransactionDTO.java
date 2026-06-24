package com.eventsitemanager.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.eventsitemanager.domain.UserPaymentTransaction} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserPaymentTransactionDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String tenantId;

    @Size(max = 20)
    private String transactionType;

    @NotNull
    private BigDecimal amount;

    @NotNull
    @Size(max = 3)
    private String currency;

    @Size(max = 255)
    private String stripePaymentIntentId;

    @Size(max = 255)
    private String stripeTransferGroup;

    private BigDecimal platformFeeAmount;

    private BigDecimal tenantAmount;

    @Size(max = 20)
    private String status;

    @NotNull
    private ZonedDateTime createdAt;

    @NotNull
    private ZonedDateTime updatedAt;

    private EventDetailsDTO event;

    private EventTicketTransactionDTO ticketTransaction;

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

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStripePaymentIntentId() {
        return stripePaymentIntentId;
    }

    public void setStripePaymentIntentId(String stripePaymentIntentId) {
        this.stripePaymentIntentId = stripePaymentIntentId;
    }

    public String getStripeTransferGroup() {
        return stripeTransferGroup;
    }

    public void setStripeTransferGroup(String stripeTransferGroup) {
        this.stripeTransferGroup = stripeTransferGroup;
    }

    public BigDecimal getPlatformFeeAmount() {
        return platformFeeAmount;
    }

    public void setPlatformFeeAmount(BigDecimal platformFeeAmount) {
        this.platformFeeAmount = platformFeeAmount;
    }

    public BigDecimal getTenantAmount() {
        return tenantAmount;
    }

    public void setTenantAmount(BigDecimal tenantAmount) {
        this.tenantAmount = tenantAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public EventDetailsDTO getEvent() {
        return event;
    }

    public void setEvent(EventDetailsDTO event) {
        this.event = event;
    }

    public EventTicketTransactionDTO getTicketTransaction() {
        return ticketTransaction;
    }

    public void setTicketTransaction(EventTicketTransactionDTO ticketTransaction) {
        this.ticketTransaction = ticketTransaction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserPaymentTransactionDTO)) {
            return false;
        }

        UserPaymentTransactionDTO userPaymentTransactionDTO = (UserPaymentTransactionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userPaymentTransactionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserPaymentTransactionDTO{" +
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
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", event=" + getEvent() +
            ", ticketTransaction=" + getTicketTransaction() +
            "}";
    }
}
