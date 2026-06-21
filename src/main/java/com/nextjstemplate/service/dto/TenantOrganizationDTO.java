package com.nextjstemplate.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nextjstemplate.service.validation.TenantOrganizationProfileValidator;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.TenantOrganization} entity.
 */
@Schema(description = "TenantOrganization and TenantSettings JDL Entities\nGenerated from Java domain classes")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TenantOrganizationDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String tenantId;

    @NotNull
    @Size(max = 255)
    private String organizationName;

    @Size(max = 255)
    private String domain;

    @Size(max = 7)
    private String primaryColor;

    @Size(max = 7)
    private String secondaryColor;

    @Size(max = 1024)
    private String logoUrl;

    @NotNull
    @Size(max = 255)
    private String contactEmail;

    @Size(max = 50)
    private String contactPhone;

    @Size(max = TenantOrganizationProfileValidator.MAX_DESCRIPTION_LENGTH)
    private String description;

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

    @Size(max = 1024)
    private String websiteUrl;

    @JsonIgnore
    private boolean descriptionSet;

    @JsonIgnore
    private boolean addressLine1Set;

    @JsonIgnore
    private boolean addressLine2Set;

    @JsonIgnore
    private boolean citySet;

    @JsonIgnore
    private boolean stateProvinceSet;

    @JsonIgnore
    private boolean zipCodeSet;

    @JsonIgnore
    private boolean countrySet;

    @JsonIgnore
    private boolean websiteUrlSet;

    @Size(max = 20)
    private String subscriptionPlan;

    @Size(max = 20)
    private String subscriptionStatus;

    private LocalDate subscriptionStartDate;

    private LocalDate subscriptionEndDate;

    private BigDecimal monthlyFeeUsd;

    @Size(max = 255)
    private String stripeCustomerId;

    private Boolean isActive;

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

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public String getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(String secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        this.descriptionSet = true;
    }

    @JsonIgnore
    public boolean isDescriptionSet() {
        return descriptionSet;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
        this.addressLine1Set = true;
    }

    @JsonIgnore
    public boolean isAddressLine1Set() {
        return addressLine1Set;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
        this.addressLine2Set = true;
    }

    @JsonIgnore
    public boolean isAddressLine2Set() {
        return addressLine2Set;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
        this.citySet = true;
    }

    @JsonIgnore
    public boolean isCitySet() {
        return citySet;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
        this.stateProvinceSet = true;
    }

    @JsonIgnore
    public boolean isStateProvinceSet() {
        return stateProvinceSet;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
        this.zipCodeSet = true;
    }

    @JsonIgnore
    public boolean isZipCodeSet() {
        return zipCodeSet;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
        this.countrySet = true;
    }

    @JsonIgnore
    public boolean isCountrySet() {
        return countrySet;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
        this.websiteUrlSet = true;
    }

    @JsonIgnore
    public boolean isWebsiteUrlSet() {
        return websiteUrlSet;
    }

    public String getSubscriptionPlan() {
        return subscriptionPlan;
    }

    public void setSubscriptionPlan(String subscriptionPlan) {
        this.subscriptionPlan = subscriptionPlan;
    }

    public String getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public void setSubscriptionStatus(String subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public LocalDate getSubscriptionStartDate() {
        return subscriptionStartDate;
    }

    public void setSubscriptionStartDate(LocalDate subscriptionStartDate) {
        this.subscriptionStartDate = subscriptionStartDate;
    }

    public LocalDate getSubscriptionEndDate() {
        return subscriptionEndDate;
    }

    public void setSubscriptionEndDate(LocalDate subscriptionEndDate) {
        this.subscriptionEndDate = subscriptionEndDate;
    }

    public BigDecimal getMonthlyFeeUsd() {
        return monthlyFeeUsd;
    }

    public void setMonthlyFeeUsd(BigDecimal monthlyFeeUsd) {
        this.monthlyFeeUsd = monthlyFeeUsd;
    }

    public String getStripeCustomerId() {
        return stripeCustomerId;
    }

    public void setStripeCustomerId(String stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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
        if (this == o) {
            return true;
        }
        if (!(o instanceof TenantOrganizationDTO)) {
            return false;
        }

        TenantOrganizationDTO tenantOrganizationDTO = (TenantOrganizationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tenantOrganizationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TenantOrganizationDTO{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", organizationName='" + getOrganizationName() + "'" +
            ", domain='" + getDomain() + "'" +
            ", primaryColor='" + getPrimaryColor() + "'" +
            ", secondaryColor='" + getSecondaryColor() + "'" +
            ", logoUrl='" + getLogoUrl() + "'" +
            ", contactEmail='" + getContactEmail() + "'" +
            ", contactPhone='" + getContactPhone() + "'" +
            ", description='" + getDescription() + "'" +
            ", addressLine1='" + getAddressLine1() + "'" +
            ", city='" + getCity() + "'" +
            ", stateProvince='" + getStateProvince() + "'" +
            ", zipCode='" + getZipCode() + "'" +
            ", country='" + getCountry() + "'" +
            ", websiteUrl='" + getWebsiteUrl() + "'" +
            ", subscriptionPlan='" + getSubscriptionPlan() + "'" +
            ", subscriptionStatus='" + getSubscriptionStatus() + "'" +
            ", subscriptionStartDate='" + getSubscriptionStartDate() + "'" +
            ", subscriptionEndDate='" + getSubscriptionEndDate() + "'" +
            ", monthlyFeeUsd=" + getMonthlyFeeUsd() +
            ", stripeCustomerId='" + getStripeCustomerId() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
