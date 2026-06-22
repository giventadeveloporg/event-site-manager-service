package com.eventsitemanager.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.eventsitemanager.domain.TenantOrganization} entity.
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
