package com.eventsitemanager.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GasStationUserStationAssignmentCriteria implements Serializable, Criteria {

    private LongFilter id;
    private StringFilter tenantId;
    private LongFilter userProfileId;
    private LongFilter stationId;

    private Boolean distinct;

    public GasStationUserStationAssignmentCriteria() {}

    public GasStationUserStationAssignmentCriteria(GasStationUserStationAssignmentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.userProfileId = other.userProfileId == null ? null : other.userProfileId.copy();
        this.stationId = other.stationId == null ? null : other.stationId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public GasStationUserStationAssignmentCriteria copy() {
        return new GasStationUserStationAssignmentCriteria(this);
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

    public LongFilter getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(LongFilter userProfileId) {
        this.userProfileId = userProfileId;
    }

    public LongFilter getStationId() {
        return stationId;
    }

    public void setStationId(LongFilter stationId) {
        this.stationId = stationId;
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
        GasStationUserStationAssignmentCriteria that = (GasStationUserStationAssignmentCriteria) o;
        return Objects.equals(distinct, that.distinct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distinct);
    }
}
