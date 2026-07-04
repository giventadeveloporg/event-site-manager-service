package com.eventsitemanager.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "gas_station_location")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GasStationLocation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gasStationLocationSeq")
    @SequenceGenerator(name = "gasStationLocationSeq", sequenceName = "public.gas_station_location_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "tenant_id", length = 255, nullable = false)
    private String tenantId;

    @NotNull
    @Size(max = 255)
    @Column(name = "station_name", length = 255, nullable = false)
    private String stationName;

    @NotNull
    @Size(max = 64)
    @Column(name = "station_code", length = 64, nullable = false)
    private String stationCode;

    @Size(max = 255)
    @Column(name = "brand", length = 255)
    private String brand;

    @Size(max = 255)
    @Column(name = "region", length = 255)
    private String region;

    @Size(max = 255)
    @Column(name = "address_line_1", length = 255)
    private String addressLine1;

    @Size(max = 255)
    @Column(name = "address_line_2", length = 255)
    private String addressLine2;

    @Size(max = 255)
    @Column(name = "city", length = 255)
    private String city;

    @Size(max = 255)
    @Column(name = "state_province", length = 255)
    private String stateProvince;

    @Size(max = 20)
    @Column(name = "zip_code", length = 20)
    private String zipCode;

    @Size(max = 100)
    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "latitude", precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 10, scale = 7)
    private BigDecimal longitude;

    @NotNull
    @Size(max = 64)
    @Column(name = "timezone", length = 64, nullable = false)
    private String timezone = "America/New_York";

    @NotNull
    @Column(name = "sells_fuel", nullable = false)
    private Boolean sellsFuel = Boolean.TRUE;

    @Column(name = "fuel_dispenser_count")
    private Integer fuelDispenserCount;

    @NotNull
    @Column(name = "has_car_wash", nullable = false)
    private Boolean hasCarWash = Boolean.FALSE;

    @NotNull
    @Column(name = "has_foodservice", nullable = false)
    private Boolean hasFoodservice = Boolean.FALSE;

    @NotNull
    @Column(name = "has_lottery", nullable = false)
    private Boolean hasLottery = Boolean.FALSE;

    @NotNull
    @Column(name = "is_24_hours", nullable = false)
    private Boolean is24Hours = Boolean.FALSE;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = Boolean.TRUE;

    @NotNull
    @Column(name = "included_in_subscription", nullable = false)
    private Boolean includedInSubscription = Boolean.TRUE;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

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
        if (!(GasStationLocation.class.isInstance(o))) return false;
        return id != null && id.equals(((GasStationLocation) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
