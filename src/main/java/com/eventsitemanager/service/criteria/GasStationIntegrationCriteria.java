package com.eventsitemanager.service.criteria;

import com.eventsitemanager.domain.enumeration.GasStationConnectionMode;
import com.eventsitemanager.domain.enumeration.GasStationSystemType;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GasStationIntegrationCriteria implements Serializable, Criteria {

    private LongFilter id;
    private StringFilter tenantId;
    private LongFilter stationId;
    private GasStationSystemTypeFilter systemType;
    private GasStationConnectionModeFilter connectionMode;
    private BooleanFilter isEnabled;

    private Boolean distinct;

    public GasStationIntegrationCriteria() {}

    public GasStationIntegrationCriteria(GasStationIntegrationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.stationId = other.stationId == null ? null : other.stationId.copy();
        this.systemType = other.systemType == null ? null : other.systemType.copy();
        this.connectionMode = other.connectionMode == null ? null : other.connectionMode.copy();
        this.isEnabled = other.isEnabled == null ? null : other.isEnabled.copy();
        this.distinct = other.distinct;
    }

    @Override
    public GasStationIntegrationCriteria copy() {
        return new GasStationIntegrationCriteria(this);
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

    public GasStationSystemTypeFilter getSystemType() {
        return systemType;
    }

    public void setSystemType(GasStationSystemTypeFilter systemType) {
        this.systemType = systemType;
    }

    public GasStationConnectionModeFilter getConnectionMode() {
        return connectionMode;
    }

    public void setConnectionMode(GasStationConnectionModeFilter connectionMode) {
        this.connectionMode = connectionMode;
    }

    public BooleanFilter getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(BooleanFilter isEnabled) {
        this.isEnabled = isEnabled;
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
        GasStationIntegrationCriteria that = (GasStationIntegrationCriteria) o;
        return Objects.equals(distinct, that.distinct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distinct);
    }

    public static class GasStationSystemTypeFilter extends Filter<GasStationSystemType> {

        public GasStationSystemTypeFilter() {}

        public GasStationSystemTypeFilter(GasStationSystemTypeFilter filter) {
            super(filter);
        }

        @Override
        public GasStationSystemTypeFilter copy() {
            return new GasStationSystemTypeFilter(this);
        }
    }

    public static class GasStationConnectionModeFilter extends Filter<GasStationConnectionMode> {

        public GasStationConnectionModeFilter() {}

        public GasStationConnectionModeFilter(GasStationConnectionModeFilter filter) {
            super(filter);
        }

        @Override
        public GasStationConnectionModeFilter copy() {
            return new GasStationConnectionModeFilter(this);
        }
    }
}
