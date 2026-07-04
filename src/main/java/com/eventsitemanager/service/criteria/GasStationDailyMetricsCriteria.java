package com.eventsitemanager.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GasStationDailyMetricsCriteria implements Serializable, Criteria {

    private LongFilter id;
    private StringFilter tenantId;
    private LongFilter stationId;
    private LocalDateFilter metricDate;

    private Boolean distinct;

    public GasStationDailyMetricsCriteria() {}

    public GasStationDailyMetricsCriteria(GasStationDailyMetricsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.stationId = other.stationId == null ? null : other.stationId.copy();
        this.metricDate = other.metricDate == null ? null : other.metricDate.copy();
        this.distinct = other.distinct;
    }

    @Override
    public GasStationDailyMetricsCriteria copy() {
        return new GasStationDailyMetricsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTenantId() {
        return tenantId;
    }

    public void setTenantId(StringFilter tenantId) {
        this.tenantId = tenantId;
    }

    public LongFilter getStationId() {
        return stationId;
    }

    public void setStationId(LongFilter stationId) {
        this.stationId = stationId;
    }

    public LocalDateFilter getMetricDate() {
        return metricDate;
    }

    public void setMetricDate(LocalDateFilter metricDate) {
        this.metricDate = metricDate;
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
        GasStationDailyMetricsCriteria that = (GasStationDailyMetricsCriteria) o;
        return Objects.equals(distinct, that.distinct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distinct);
    }
}
