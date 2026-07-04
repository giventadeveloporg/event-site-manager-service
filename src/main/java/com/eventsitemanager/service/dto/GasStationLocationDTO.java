package com.eventsitemanager.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

@SuppressWarnings("common-java:DuplicatedBlocks")
public class GasStationLocationDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String tenantId;

    @NotNull
    @Size(max = 255)
    private String stationName;

    @NotNull
    @Size(max = 64)
    private String stationCode;

    @Size(max = 255)
    private String brand;

    @Size(max = 255)
    private String region;

    @Size(max = 255)
    private String addressLine1;

    @Size(max = 255)
    private String addressLine2;

    @Size(max = 255)
    private String city;

    @Size(max = 255)
    private String stateProvince;

    @Size(max = 20)
    private String zipCode;

    @Size(max = 100)
    private String country;

    private BigDecimal latitude;

    private BigDecimal longitude;

    @NotNull
    @Size(max = 64)
    private String timezone;

    private Boolean sellsFuel;

    private Integer fuelDispenserCount;

    private Boolean hasCarWash;

    private Boolean hasFoodservice;

    private Boolean hasLottery;

    private Boolean is24Hours;

    private Boolean isActive;

    private Boolean includedInSubscription;

    @NotNull
    private ZonedDateTime createdAt;

    @NotNull
    private ZonedDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Boolean getSellsFuel() {
        return sellsFuel;
    }

    public void setSellsFuel(Boolean sellsFuel) {
        this.sellsFuel = sellsFuel;
    }

    public Integer getFuelDispenserCount() {
        return fuelDispenserCount;
    }

    public void setFuelDispenserCount(Integer fuelDispenserCount) {
        this.fuelDispenserCount = fuelDispenserCount;
    }

    public Boolean getHasCarWash() {
        return hasCarWash;
    }

    public void setHasCarWash(Boolean hasCarWash) {
        this.hasCarWash = hasCarWash;
    }

    public Boolean getHasFoodservice() {
        return hasFoodservice;
    }

    public void setHasFoodservice(Boolean hasFoodservice) {
        this.hasFoodservice = hasFoodservice;
    }

    public Boolean getHasLottery() {
        return hasLottery;
    }

    public void setHasLottery(Boolean hasLottery) {
        this.hasLottery = hasLottery;
    }

    public Boolean getIs24Hours() {
        return is24Hours;
    }

    public void setIs24Hours(Boolean is24Hours) {
        this.is24Hours = is24Hours;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIncludedInSubscription() {
        return includedInSubscription;
    }

    public void setIncludedInSubscription(Boolean includedInSubscription) {
        this.includedInSubscription = includedInSubscription;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(GasStationLocationDTO.class.isInstance(o))) return false;
        GasStationLocationDTO other = (GasStationLocationDTO) o;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
