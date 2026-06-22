package com.eventsitemanager.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.eventsitemanager.domain.QrCodeUsage} entity. This class is used
 * in {@link com.eventsitemanager.web.rest.QrCodeUsageResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /qr-code-usages?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QrCodeUsageCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private StringFilter qrCodeData;

    private ZonedDateTimeFilter generatedAt;

    private ZonedDateTimeFilter usedAt;

    private IntegerFilter usageCount;

    private StringFilter lastScannedBy;

    private ZonedDateTimeFilter createdAt;

    private LongFilter attendeeId;

    private Boolean distinct;

    public QrCodeUsageCriteria() {}

    public QrCodeUsageCriteria(QrCodeUsageCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.qrCodeData = other.qrCodeData == null ? null : other.qrCodeData.copy();
        this.generatedAt = other.generatedAt == null ? null : other.generatedAt.copy();
        this.usedAt = other.usedAt == null ? null : other.usedAt.copy();
        this.usageCount = other.usageCount == null ? null : other.usageCount.copy();
        this.lastScannedBy = other.lastScannedBy == null ? null : other.lastScannedBy.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.attendeeId = other.attendeeId == null ? null : other.attendeeId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public QrCodeUsageCriteria copy() {
        return new QrCodeUsageCriteria(this);
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

    public StringFilter getQrCodeData() {
        return qrCodeData;
    }

    public StringFilter qrCodeData() {
        if (qrCodeData == null) {
            qrCodeData = new StringFilter();
        }
        return qrCodeData;
    }

    public void setQrCodeData(StringFilter qrCodeData) {
        this.qrCodeData = qrCodeData;
    }

    public ZonedDateTimeFilter getGeneratedAt() {
        return generatedAt;
    }

    public ZonedDateTimeFilter generatedAt() {
        if (generatedAt == null) {
            generatedAt = new ZonedDateTimeFilter();
        }
        return generatedAt;
    }

    public void setGeneratedAt(ZonedDateTimeFilter generatedAt) {
        this.generatedAt = generatedAt;
    }

    public ZonedDateTimeFilter getUsedAt() {
        return usedAt;
    }

    public ZonedDateTimeFilter usedAt() {
        if (usedAt == null) {
            usedAt = new ZonedDateTimeFilter();
        }
        return usedAt;
    }

    public void setUsedAt(ZonedDateTimeFilter usedAt) {
        this.usedAt = usedAt;
    }

    public IntegerFilter getUsageCount() {
        return usageCount;
    }

    public IntegerFilter usageCount() {
        if (usageCount == null) {
            usageCount = new IntegerFilter();
        }
        return usageCount;
    }

    public void setUsageCount(IntegerFilter usageCount) {
        this.usageCount = usageCount;
    }

    public StringFilter getLastScannedBy() {
        return lastScannedBy;
    }

    public StringFilter lastScannedBy() {
        if (lastScannedBy == null) {
            lastScannedBy = new StringFilter();
        }
        return lastScannedBy;
    }

    public void setLastScannedBy(StringFilter lastScannedBy) {
        this.lastScannedBy = lastScannedBy;
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

    public LongFilter getAttendeeId() {
        return attendeeId;
    }

    public LongFilter attendeeId() {
        if (attendeeId == null) {
            attendeeId = new LongFilter();
        }
        return attendeeId;
    }

    public void setAttendeeId(LongFilter attendeeId) {
        this.attendeeId = attendeeId;
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
        final QrCodeUsageCriteria that = (QrCodeUsageCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(qrCodeData, that.qrCodeData) &&
            Objects.equals(generatedAt, that.generatedAt) &&
            Objects.equals(usedAt, that.usedAt) &&
            Objects.equals(usageCount, that.usageCount) &&
            Objects.equals(lastScannedBy, that.lastScannedBy) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(attendeeId, that.attendeeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, qrCodeData, generatedAt, usedAt, usageCount, lastScannedBy, createdAt, attendeeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QrCodeUsageCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
            (qrCodeData != null ? "qrCodeData=" + qrCodeData + ", " : "") +
            (generatedAt != null ? "generatedAt=" + generatedAt + ", " : "") +
            (usedAt != null ? "usedAt=" + usedAt + ", " : "") +
            (usageCount != null ? "usageCount=" + usageCount + ", " : "") +
            (lastScannedBy != null ? "lastScannedBy=" + lastScannedBy + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (attendeeId != null ? "attendeeId=" + attendeeId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
