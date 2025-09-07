package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.EventTicketTransactionItem} entity. This class is used
 * in {@link com.nextjstemplate.web.rest.EventTicketTransactionItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /event-ticket-transaction-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventTicketTransactionItemCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private LongFilter transactionId;

    private LongFilter ticketTypeId;

    private IntegerFilter quantity;

    private BigDecimalFilter pricePerUnit;

    private BigDecimalFilter totalAmount;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private Boolean distinct;

    public EventTicketTransactionItemCriteria() {}

    public EventTicketTransactionItemCriteria(EventTicketTransactionItemCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.transactionId = other.transactionId == null ? null : other.transactionId.copy();
        this.ticketTypeId = other.ticketTypeId == null ? null : other.ticketTypeId.copy();
        this.quantity = other.quantity == null ? null : other.quantity.copy();
        this.pricePerUnit = other.pricePerUnit == null ? null : other.pricePerUnit.copy();
        this.totalAmount = other.totalAmount == null ? null : other.totalAmount.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.transactionId = other.transactionId == null ? null : other.transactionId.copy();
        this.ticketTypeId = other.ticketTypeId == null ? null : other.ticketTypeId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EventTicketTransactionItemCriteria copy() {
        return new EventTicketTransactionItemCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTenantId() {
        return tenantId;
    }

    public StringFilter tenantId() {
        if (tenantId == null) {
            tenantId = new StringFilter();
        }
        return tenantId;
    }

    public void setTenantId(StringFilter tenantId) {
        this.tenantId = tenantId;
    }

    public LongFilter getTransactionId() {
        return transactionId;
    }

    public LongFilter transactionId() {
        if (transactionId == null) {
            transactionId = new LongFilter();
        }
        return transactionId;
    }

    public void setTransactionId(LongFilter transactionId) {
        this.transactionId = transactionId;
    }

    public LongFilter getTicketTypeId() {
        return ticketTypeId;
    }

    public LongFilter ticketTypeId() {
        if (ticketTypeId == null) {
            ticketTypeId = new LongFilter();
        }
        return ticketTypeId;
    }

    public void setTicketTypeId(LongFilter ticketTypeId) {
        this.ticketTypeId = ticketTypeId;
    }

    public IntegerFilter getQuantity() {
        return quantity;
    }

    public IntegerFilter quantity() {
        if (quantity == null) {
            quantity = new IntegerFilter();
        }
        return quantity;
    }

    public void setQuantity(IntegerFilter quantity) {
        this.quantity = quantity;
    }

    public BigDecimalFilter getPricePerUnit() {
        return pricePerUnit;
    }

    public BigDecimalFilter pricePerUnit() {
        if (pricePerUnit == null) {
            pricePerUnit = new BigDecimalFilter();
        }
        return pricePerUnit;
    }

    public void setPricePerUnit(BigDecimalFilter pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public BigDecimalFilter getTotalAmount() {
        return totalAmount;
    }

    public BigDecimalFilter totalAmount() {
        if (totalAmount == null) {
            totalAmount = new BigDecimalFilter();
        }
        return totalAmount;
    }

    public void setTotalAmount(BigDecimalFilter totalAmount) {
        this.totalAmount = totalAmount;
    }

    public ZonedDateTimeFilter getCreatedAt() {
        return createdAt;
    }

    public ZonedDateTimeFilter createdAt() {
        if (createdAt == null) {
            createdAt = new ZonedDateTimeFilter();
        }
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTimeFilter createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTimeFilter getUpdatedAt() {
        return updatedAt;
    }

    public ZonedDateTimeFilter updatedAt() {
        if (updatedAt == null) {
            updatedAt = new ZonedDateTimeFilter();
        }
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTimeFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EventTicketTransactionItemCriteria that = (EventTicketTransactionItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(transactionId, that.transactionId) &&
            Objects.equals(ticketTypeId, that.ticketTypeId) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(pricePerUnit, that.pricePerUnit) &&
            Objects.equals(totalAmount, that.totalAmount) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(transactionId, that.transactionId) &&
            Objects.equals(ticketTypeId, that.ticketTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            tenantId,
            transactionId,
            ticketTypeId,
            quantity,
            pricePerUnit,
            totalAmount,
            createdAt,
            updatedAt,
            transactionId,
            ticketTypeId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventTicketTransactionItemCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
            (transactionId != null ? "transactionId=" + transactionId + ", " : "") +
            (ticketTypeId != null ? "ticketTypeId=" + ticketTypeId + ", " : "") +
            (quantity != null ? "quantity=" + quantity + ", " : "") +
            (pricePerUnit != null ? "pricePerUnit=" + pricePerUnit + ", " : "") +
            (totalAmount != null ? "totalAmount=" + totalAmount + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (transactionId != null ? "transactionId=" + transactionId + ", " : "") +
            (ticketTypeId != null ? "ticketTypeId=" + ticketTypeId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
