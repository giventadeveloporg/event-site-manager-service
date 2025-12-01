package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.EventTicketTransactionItem} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventTicketTransactionItemDTO implements Serializable {

    private Long id;

    @Size(max = 255)
    private String tenantId;

    @Size(max = 255)
    private String paymentMethodDomainId;

    @NotNull
    private Long transactionId;

    @NotNull
    private Long ticketTypeId;

    @NotNull
    @Min(value = 1)
    private Integer quantity;

    @NotNull
    private BigDecimal pricePerUnit;

    @NotNull
    private BigDecimal totalAmount;

    @NotNull
    private ZonedDateTime createdAt;

    @NotNull
    private ZonedDateTime updatedAt;

    private EventTicketTransactionDTO transaction;

    private EventTicketTypeDTO ticketType;

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

    public String getPaymentMethodDomainId() {
        return paymentMethodDomainId;
    }

    public void setPaymentMethodDomainId(String paymentMethodDomainId) {
        this.paymentMethodDomainId = paymentMethodDomainId;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getTicketTypeId() {
        return ticketTypeId;
    }

    public void setTicketTypeId(Long ticketTypeId) {
        this.ticketTypeId = ticketTypeId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(BigDecimal pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
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

    public EventTicketTransactionDTO getTransaction() {
        return transaction;
    }

    public void setTransaction(EventTicketTransactionDTO transaction) {
        this.transaction = transaction;
    }

    public EventTicketTypeDTO getTicketType() {
        return ticketType;
    }

    public void setTicketType(EventTicketTypeDTO ticketType) {
        this.ticketType = ticketType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventTicketTransactionItemDTO)) {
            return false;
        }

        EventTicketTransactionItemDTO eventTicketTransactionItemDTO = (EventTicketTransactionItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eventTicketTransactionItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventTicketTransactionItemDTO{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", transactionId=" + getTransactionId() +
            ", ticketTypeId=" + getTicketTypeId() +
            ", quantity=" + getQuantity() +
            ", pricePerUnit=" + getPricePerUnit() +
            ", totalAmount=" + getTotalAmount() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", transaction=" + getTransaction() +
            ", ticketType=" + getTicketType() +
            "}";
    }
}
