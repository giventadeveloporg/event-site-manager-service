package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.EventTicketType} entity. This class is used
 * in {@link com.nextjstemplate.web.rest.EventTicketTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /event-ticket-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventTicketTypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private StringFilter name;

    private StringFilter description;

    private BooleanFilter isServiceFeeIncluded;

    private BigDecimalFilter serviceFee;

    private BigDecimalFilter price;

    private StringFilter code;

    private IntegerFilter availableQuantity;

    private IntegerFilter soldQuantity;

    private IntegerFilter remainingQuantity;

    private BooleanFilter isActive;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private LongFilter eventId;

    private Boolean distinct;

    public EventTicketTypeCriteria() {}

    public EventTicketTypeCriteria(EventTicketTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.isServiceFeeIncluded = other.isServiceFeeIncluded == null ? null : other.isServiceFeeIncluded.copy();
        this.serviceFee = other.serviceFee == null ? null : other.serviceFee.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.availableQuantity = other.availableQuantity == null ? null : other.availableQuantity.copy();
        this.soldQuantity = other.soldQuantity == null ? null : other.soldQuantity.copy();
        this.remainingQuantity = other.remainingQuantity == null ? null : other.remainingQuantity.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EventTicketTypeCriteria copy() {
        return new EventTicketTypeCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public BooleanFilter getIsServiceFeeIncluded() {
        return isServiceFeeIncluded;
    }

    public BooleanFilter isServiceFeeIncluded() {
        if (isServiceFeeIncluded == null) {
            isServiceFeeIncluded = new BooleanFilter();
        }
        return isServiceFeeIncluded;
    }

    public void setIsServiceFeeIncluded(BooleanFilter isServiceFeeIncluded) {
        this.isServiceFeeIncluded = isServiceFeeIncluded;
    }

    public BigDecimalFilter getServiceFee() {
        return serviceFee;
    }

    public BigDecimalFilter serviceFee() {
        if (serviceFee == null) {
            serviceFee = new BigDecimalFilter();
        }
        return serviceFee;
    }

    public void setServiceFee(BigDecimalFilter serviceFee) {
        this.serviceFee = serviceFee;
    }

    public BigDecimalFilter getPrice() {
        return price;
    }

    public BigDecimalFilter price() {
        if (price == null) {
            price = new BigDecimalFilter();
        }
        return price;
    }

    public void setPrice(BigDecimalFilter price) {
        this.price = price;
    }

    public StringFilter getCode() {
        return code;
    }

    public StringFilter code() {
        if (code == null) {
            code = new StringFilter();
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public IntegerFilter getAvailableQuantity() {
        return availableQuantity;
    }

    public IntegerFilter availableQuantity() {
        if (availableQuantity == null) {
            availableQuantity = new IntegerFilter();
        }
        return availableQuantity;
    }

    public void setAvailableQuantity(IntegerFilter availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public IntegerFilter getSoldQuantity() {
        return soldQuantity;
    }

    public IntegerFilter soldQuantity() {
        if (soldQuantity == null) {
            soldQuantity = new IntegerFilter();
        }
        return soldQuantity;
    }

    public void setSoldQuantity(IntegerFilter soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    public IntegerFilter getRemainingQuantity() {
        return remainingQuantity;
    }

    public IntegerFilter remainingQuantity() {
        if (remainingQuantity == null) {
            remainingQuantity = new IntegerFilter();
        }
        return remainingQuantity;
    }

    public void setRemainingQuantity(IntegerFilter remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            isActive = new BooleanFilter();
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
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
        final EventTicketTypeCriteria that = (EventTicketTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(isServiceFeeIncluded, that.isServiceFeeIncluded) &&
            Objects.equals(serviceFee, that.serviceFee) &&
            Objects.equals(price, that.price) &&
            Objects.equals(code, that.code) &&
            Objects.equals(availableQuantity, that.availableQuantity) &&
            Objects.equals(soldQuantity, that.soldQuantity) &&
            Objects.equals(remainingQuantity, that.remainingQuantity) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(eventId, that.eventId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            tenantId,
            name,
            description,
            isServiceFeeIncluded,
            serviceFee,
            price,
            code,
            availableQuantity,
            soldQuantity,
            remainingQuantity,
            isActive,
            createdAt,
            updatedAt,
            eventId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventTicketTypeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (isServiceFeeIncluded != null ? "isServiceFeeIncluded=" + isServiceFeeIncluded + ", " : "") +
            (serviceFee != null ? "serviceFee=" + serviceFee + ", " : "") +
            (price != null ? "price=" + price + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (availableQuantity != null ? "availableQuantity=" + availableQuantity + ", " : "") +
            (soldQuantity != null ? "soldQuantity=" + soldQuantity + ", " : "") +
            (remainingQuantity != null ? "remainingQuantity=" + remainingQuantity + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (eventId != null ? "eventId=" + eventId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
