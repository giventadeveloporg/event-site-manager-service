package com.eventsitemanager.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.eventsitemanager.domain.UserPaymentTransaction} entity. This class is used
 * in {@link com.eventsitemanager.web.rest.UserPaymentTransactionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-payment-transactions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserPaymentTransactionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private StringFilter transactionType;

    private BigDecimalFilter amount;

    private StringFilter currency;

    private StringFilter stripePaymentIntentId;

    private StringFilter stripeTransferGroup;

    private BigDecimalFilter platformFeeAmount;

    private BigDecimalFilter tenantAmount;

    private StringFilter status;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private LongFilter eventId;

    private LongFilter ticketTransactionId;

    private Boolean distinct;

    public UserPaymentTransactionCriteria() {}

    public UserPaymentTransactionCriteria(UserPaymentTransactionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.transactionType = other.transactionType == null ? null : other.transactionType.copy();
        this.amount = other.amount == null ? null : other.amount.copy();
        this.currency = other.currency == null ? null : other.currency.copy();
        this.stripePaymentIntentId = other.stripePaymentIntentId == null ? null : other.stripePaymentIntentId.copy();
        this.stripeTransferGroup = other.stripeTransferGroup == null ? null : other.stripeTransferGroup.copy();
        this.platformFeeAmount = other.platformFeeAmount == null ? null : other.platformFeeAmount.copy();
        this.tenantAmount = other.tenantAmount == null ? null : other.tenantAmount.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
        this.ticketTransactionId = other.ticketTransactionId == null ? null : other.ticketTransactionId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public UserPaymentTransactionCriteria copy() {
        return new UserPaymentTransactionCriteria(this);
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

    public StringFilter getTransactionType() {
        return transactionType;
    }

    public StringFilter transactionType() {
        if (transactionType == null) {
            transactionType = new StringFilter();
        }
        return transactionType;
    }

    public void setTransactionType(StringFilter transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimalFilter getAmount() {
        return amount;
    }

    public BigDecimalFilter amount() {
        if (amount == null) {
            amount = new BigDecimalFilter();
        }
        return amount;
    }

    public void setAmount(BigDecimalFilter amount) {
        this.amount = amount;
    }

    public StringFilter getCurrency() {
        return currency;
    }

    public StringFilter currency() {
        if (currency == null) {
            currency = new StringFilter();
        }
        return currency;
    }

    public void setCurrency(StringFilter currency) {
        this.currency = currency;
    }

    public StringFilter getStripePaymentIntentId() {
        return stripePaymentIntentId;
    }

    public StringFilter stripePaymentIntentId() {
        if (stripePaymentIntentId == null) {
            stripePaymentIntentId = new StringFilter();
        }
        return stripePaymentIntentId;
    }

    public void setStripePaymentIntentId(StringFilter stripePaymentIntentId) {
        this.stripePaymentIntentId = stripePaymentIntentId;
    }

    public StringFilter getStripeTransferGroup() {
        return stripeTransferGroup;
    }

    public StringFilter stripeTransferGroup() {
        if (stripeTransferGroup == null) {
            stripeTransferGroup = new StringFilter();
        }
        return stripeTransferGroup;
    }

    public void setStripeTransferGroup(StringFilter stripeTransferGroup) {
        this.stripeTransferGroup = stripeTransferGroup;
    }

    public BigDecimalFilter getPlatformFeeAmount() {
        return platformFeeAmount;
    }

    public BigDecimalFilter platformFeeAmount() {
        if (platformFeeAmount == null) {
            platformFeeAmount = new BigDecimalFilter();
        }
        return platformFeeAmount;
    }

    public void setPlatformFeeAmount(BigDecimalFilter platformFeeAmount) {
        this.platformFeeAmount = platformFeeAmount;
    }

    public BigDecimalFilter getTenantAmount() {
        return tenantAmount;
    }

    public BigDecimalFilter tenantAmount() {
        if (tenantAmount == null) {
            tenantAmount = new BigDecimalFilter();
        }
        return tenantAmount;
    }

    public void setTenantAmount(BigDecimalFilter tenantAmount) {
        this.tenantAmount = tenantAmount;
    }

    public StringFilter getStatus() {
        return status;
    }

    public StringFilter status() {
        if (status == null) {
            status = new StringFilter();
        }
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
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

    public LongFilter getEventId() {
        return eventId;
    }

    public LongFilter eventId() {
        if (eventId == null) {
            eventId = new LongFilter();
        }
        return eventId;
    }

    public void setEventId(LongFilter eventId) {
        this.eventId = eventId;
    }

    public LongFilter getTicketTransactionId() {
        return ticketTransactionId;
    }

    public LongFilter ticketTransactionId() {
        if (ticketTransactionId == null) {
            ticketTransactionId = new LongFilter();
        }
        return ticketTransactionId;
    }

    public void setTicketTransactionId(LongFilter ticketTransactionId) {
        this.ticketTransactionId = ticketTransactionId;
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
        final UserPaymentTransactionCriteria that = (UserPaymentTransactionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(transactionType, that.transactionType) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(currency, that.currency) &&
            Objects.equals(stripePaymentIntentId, that.stripePaymentIntentId) &&
            Objects.equals(stripeTransferGroup, that.stripeTransferGroup) &&
            Objects.equals(platformFeeAmount, that.platformFeeAmount) &&
            Objects.equals(tenantAmount, that.tenantAmount) &&
            Objects.equals(status, that.status) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(eventId, that.eventId) &&
            Objects.equals(ticketTransactionId, that.ticketTransactionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            tenantId,
            transactionType,
            amount,
            currency,
            stripePaymentIntentId,
            stripeTransferGroup,
            platformFeeAmount,
            tenantAmount,
            status,
            createdAt,
            updatedAt,
            eventId,
            ticketTransactionId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserPaymentTransactionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
            (transactionType != null ? "transactionType=" + transactionType + ", " : "") +
            (amount != null ? "amount=" + amount + ", " : "") +
            (currency != null ? "currency=" + currency + ", " : "") +
            (stripePaymentIntentId != null ? "stripePaymentIntentId=" + stripePaymentIntentId + ", " : "") +
            (stripeTransferGroup != null ? "stripeTransferGroup=" + stripeTransferGroup + ", " : "") +
            (platformFeeAmount != null ? "platformFeeAmount=" + platformFeeAmount + ", " : "") +
            (tenantAmount != null ? "tenantAmount=" + tenantAmount + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (eventId != null ? "eventId=" + eventId + ", " : "") +
            (ticketTransactionId != null ? "ticketTransactionId=" + ticketTransactionId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
