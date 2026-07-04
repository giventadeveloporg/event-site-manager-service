package com.eventsitemanager.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GasStationLocationCriteria implements Serializable, Criteria {

    private LongFilter id;
    private StringFilter tenantId;
    private StringFilter stationName;
    private StringFilter stationCode;
    private StringFilter brand;
    private StringFilter region;
    private StringFilter city;
    private BooleanFilter isActive;

    private Boolean distinct;

    public GasStationLocationCriteria() {}

    public GasStationLocationCriteria(GasStationLocationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.stationName = other.stationName == null ? null : other.stationName.copy();
        this.stationCode = other.stationCode == null ? null : other.stationCode.copy();
        this.brand = other.brand == null ? null : other.brand.copy();
        this.region = other.region == null ? null : other.region.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.distinct = other.distinct;
    }

    @Override
    public GasStationLocationCriteria copy() {
        return new GasStationLocationCriteria(this);
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

    public StringFilter getStationName() {
        return stationName;
    }

    public void setStationName(StringFilter stationName) {
        this.stationName = stationName;
    }

    public StringFilter getStationCode() {
        return stationCode;
    }

    public void setStationCode(StringFilter stationCode) {
        this.stationCode = stationCode;
    }

    public StringFilter getBrand() {
        return brand;
    }

    public void setBrand(StringFilter brand) {
        this.brand = brand;
    }

    public StringFilter getRegion() {
        return region;
    }

    public void setRegion(StringFilter region) {
        this.region = region;
    }

    public StringFilter getCity() {
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
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
        GasStationLocationCriteria that = (GasStationLocationCriteria) o;
        return Objects.equals(distinct, that.distinct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distinct);
    }
}
