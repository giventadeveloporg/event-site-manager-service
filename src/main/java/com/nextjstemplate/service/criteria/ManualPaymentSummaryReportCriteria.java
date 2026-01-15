package com.nextjstemplate.service.criteria;

import com.nextjstemplate.domain.enumeration.ManualPaymentMethodType;
import com.nextjstemplate.domain.enumeration.ManualPaymentStatus;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.ManualPaymentSummaryReport} entity.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ManualPaymentSummaryReportCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private LongFilter eventId;

    private Filter<ManualPaymentMethodType> paymentMethodType;

    private Filter<ManualPaymentStatus> status;

    private LocalDateFilter snapshotDate;

    private Boolean distinct;

    public ManualPaymentSummaryReportCriteria() {}

    public ManualPaymentSummaryReportCriteria(ManualPaymentSummaryReportCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
        this.paymentMethodType = other.paymentMethodType == null ? null : other.paymentMethodType.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.snapshotDate = other.snapshotDate == null ? null : other.snapshotDate.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ManualPaymentSummaryReportCriteria copy() {
        return new ManualPaymentSummaryReportCriteria(this);
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

    public LocalDateFilter getSnapshotDate() {
        return snapshotDate;
    }

    public LocalDateFilter snapshotDate() {
        if (snapshotDate == null) {
            snapshotDate = new LocalDateFilter();
        }
        return snapshotDate;
    }

    public void setSnapshotDate(LocalDateFilter snapshotDate) {
        this.snapshotDate = snapshotDate;
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
        final ManualPaymentSummaryReportCriteria that = (ManualPaymentSummaryReportCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(eventId, that.eventId) &&
            Objects.equals(paymentMethodType, that.paymentMethodType) &&
            Objects.equals(status, that.status) &&
            Objects.equals(snapshotDate, that.snapshotDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, eventId, paymentMethodType, status, snapshotDate, distinct);
    }

    @Override
    public String toString() {
        return (
            "ManualPaymentSummaryReportCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
            (eventId != null ? "eventId=" + eventId + ", " : "") +
            (paymentMethodType != null ? "paymentMethodType=" + paymentMethodType + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (snapshotDate != null ? "snapshotDate=" + snapshotDate + ", " : "") +
            (distinct != null ? "distinct=" + distinct : "") +
            "}"
        );
    }
}
