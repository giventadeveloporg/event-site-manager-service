package com.eventsitemanager.service.criteria;

import com.eventsitemanager.domain.enumeration.ManualPaymentMethodType;
import com.eventsitemanager.domain.enumeration.ManualPaymentStatus;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.eventsitemanager.domain.ManualPaymentRequest} entity.
 * Used to receive filtering options from HTTP GET request parameters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ManualPaymentRequestCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private LongFilter eventId;

    private LongFilter ticketTransactionId;

    private StringFilter requesterEmail;

    private RangeFilter<java.math.BigDecimal> amountDue;

    private Filter<ManualPaymentMethodType> paymentMethodType;

    private Filter<ManualPaymentStatus> status;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private Boolean distinct;

    public ManualPaymentRequestCriteria() {}

    public ManualPaymentRequestCriteria(ManualPaymentRequestCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
        this.ticketTransactionId = other.ticketTransactionId == null ? null : other.ticketTransactionId.copy();
        this.requesterEmail = other.requesterEmail == null ? null : other.requesterEmail.copy();
        this.amountDue = other.amountDue == null ? null : other.amountDue.copy();
        this.paymentMethodType = other.paymentMethodType == null ? null : other.paymentMethodType.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ManualPaymentRequestCriteria copy() {
        return new ManualPaymentRequestCriteria(this);
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

    public StringFilter getRequesterEmail() {
        return requesterEmail;
    }

    public StringFilter requesterEmail() {
        if (requesterEmail == null) {
            requesterEmail = new StringFilter();
        }
        return requesterEmail;
    }

    public void setRequesterEmail(StringFilter requesterEmail) {
        this.requesterEmail = requesterEmail;
    }

    public RangeFilter<java.math.BigDecimal> getAmountDue() {
        return amountDue;
    }

    public RangeFilter<java.math.BigDecimal> amountDue() {
        if (amountDue == null) {
            amountDue = new RangeFilter<>();
        }
        return amountDue;
    }

    public void setAmountDue(RangeFilter<java.math.BigDecimal> amountDue) {
        this.amountDue = amountDue;
    }

    public Filter<ManualPaymentMethodType> getPaymentMethodType() {
        return paymentMethodType;
    }

    public Filter<ManualPaymentMethodType> paymentMethodType() {
        if (paymentMethodType == null) {
            paymentMethodType = new Filter<>();
        }
        return paymentMethodType;
    }

    public void setPaymentMethodType(Filter<ManualPaymentMethodType> paymentMethodType) {
        this.paymentMethodType = paymentMethodType;
    }

    public Filter<ManualPaymentStatus> getStatus() {
        return status;
    }

    public Filter<ManualPaymentStatus> status() {
        if (status == null) {
            status = new Filter<>();
        }
        return status;
    }

    public void setStatus(Filter<ManualPaymentStatus> status) {
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

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ManualPaymentRequestCriteria that = (ManualPaymentRequestCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(eventId, that.eventId) &&
            Objects.equals(ticketTransactionId, that.ticketTransactionId) &&
            Objects.equals(requesterEmail, that.requesterEmail) &&
            Objects.equals(amountDue, that.amountDue) &&
            Objects.equals(paymentMethodType, that.paymentMethodType) &&
            Objects.equals(status, that.status) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            tenantId,
            eventId,
            ticketTransactionId,
            requesterEmail,
            amountDue,
            paymentMethodType,
            status,
            createdAt,
            updatedAt,
            distinct
        );
    }

    @Override
    public String toString() {
        return (
            "ManualPaymentRequestCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
            (eventId != null ? "eventId=" + eventId + ", " : "") +
            (ticketTransactionId != null ? "ticketTransactionId=" + ticketTransactionId + ", " : "") +
            (requesterEmail != null ? "requesterEmail=" + requesterEmail + ", " : "") +
            (amountDue != null ? "amountDue=" + amountDue + ", " : "") +
            (paymentMethodType != null ? "paymentMethodType=" + paymentMethodType + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (distinct != null ? "distinct=" + distinct : "") +
            "}"
        );
    }
}
