package com.eventsitemanager.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.eventsitemanager.domain.DiscountCode} entity. This class is used
 * in {@link com.eventsitemanager.web.rest.DiscountCodeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /discount-codes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DiscountCodeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter description;

    private StringFilter discountType;

    private BigDecimalFilter discountValue;

    private IntegerFilter maxUses;

    private IntegerFilter usesCount;

    private ZonedDateTimeFilter validFrom;

    private ZonedDateTimeFilter validTo;

    private BooleanFilter isActive;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private LongFilter eventId;

    private StringFilter tenantId;

    private Boolean distinct;

    public DiscountCodeCriteria() {}

    public DiscountCodeCriteria(DiscountCodeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.discountType = other.discountType == null ? null : other.discountType.copy();
        this.discountValue = other.discountValue == null ? null : other.discountValue.copy();
        this.maxUses = other.maxUses == null ? null : other.maxUses.copy();
        this.usesCount = other.usesCount == null ? null : other.usesCount.copy();
        this.validFrom = other.validFrom == null ? null : other.validFrom.copy();
        this.validTo = other.validTo == null ? null : other.validTo.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DiscountCodeCriteria copy() {
        return new DiscountCodeCriteria(this);
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

    public StringFilter getDiscountType() {
        return discountType;
    }

    public StringFilter discountType() {
        if (discountType == null) {
            discountType = new StringFilter();
        }
        return discountType;
    }

    public void setDiscountType(StringFilter discountType) {
        this.discountType = discountType;
    }

    public BigDecimalFilter getDiscountValue() {
        return discountValue;
    }

    public BigDecimalFilter discountValue() {
        if (discountValue == null) {
            discountValue = new BigDecimalFilter();
        }
        return discountValue;
    }

    public void setDiscountValue(BigDecimalFilter discountValue) {
        this.discountValue = discountValue;
    }

    public IntegerFilter getMaxUses() {
        return maxUses;
    }

    public IntegerFilter maxUses() {
        if (maxUses == null) {
            maxUses = new IntegerFilter();
        }
        return maxUses;
    }

    public void setMaxUses(IntegerFilter maxUses) {
        this.maxUses = maxUses;
    }

    public IntegerFilter getUsesCount() {
        return usesCount;
    }

    public IntegerFilter usesCount() {
        if (usesCount == null) {
            usesCount = new IntegerFilter();
        }
        return usesCount;
    }

    public void setUsesCount(IntegerFilter usesCount) {
        this.usesCount = usesCount;
    }

    public ZonedDateTimeFilter getValidFrom() {
        return validFrom;
    }

    public ZonedDateTimeFilter validFrom() {
        if (validFrom == null) {
            validFrom = new ZonedDateTimeFilter();
        }
        return validFrom;
    }

    public void setValidFrom(ZonedDateTimeFilter validFrom) {
        this.validFrom = validFrom;
    }

    public ZonedDateTimeFilter getValidTo() {
        return validTo;
    }

    public ZonedDateTimeFilter validTo() {
        if (validTo == null) {
            validTo = new ZonedDateTimeFilter();
        }
        return validTo;
    }

    public void setValidTo(ZonedDateTimeFilter validTo) {
        this.validTo = validTo;
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
        final DiscountCodeCriteria that = (DiscountCodeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(description, that.description) &&
            Objects.equals(discountType, that.discountType) &&
            Objects.equals(discountValue, that.discountValue) &&
            Objects.equals(maxUses, that.maxUses) &&
            Objects.equals(usesCount, that.usesCount) &&
            Objects.equals(validFrom, that.validFrom) &&
            Objects.equals(validTo, that.validTo) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(eventId, that.eventId) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            code,
            description,
            discountType,
            discountValue,
            maxUses,
            usesCount,
            validFrom,
            validTo,
            isActive,
            createdAt,
            updatedAt,
            eventId,
            tenantId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DiscountCodeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (discountType != null ? "discountType=" + discountType + ", " : "") +
            (discountValue != null ? "discountValue=" + discountValue + ", " : "") +
            (maxUses != null ? "maxUses=" + maxUses + ", " : "") +
            (usesCount != null ? "usesCount=" + usesCount + ", " : "") +
            (validFrom != null ? "validFrom=" + validFrom + ", " : "") +
            (validTo != null ? "validTo=" + validTo + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (eventId != null ? "eventId=" + eventId + ", " : "") +
            (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
