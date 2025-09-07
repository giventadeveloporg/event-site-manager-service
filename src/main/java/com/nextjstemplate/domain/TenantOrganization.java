package com.nextjstemplate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * TenantOrganization and TenantSettings JDL Entities
 * Generated from Java domain classes
 */
@Entity
@Table(name = "tenant_organization")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TenantOrganization implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "tenant_id", length = 255, nullable = false, unique = true)
    private String tenantId;

    @NotNull
    @Size(max = 255)
    @Column(name = "organization_name", length = 255, nullable = false)
    private String organizationName;

    @Size(max = 255)
    @Column(name = "domain", length = 255, unique = true)
    private String domain;

    @Size(max = 7)
    @Column(name = "primary_color", length = 7)
    private String primaryColor;

    @Size(max = 7)
    @Column(name = "secondary_color", length = 7)
    private String secondaryColor;

    @Size(max = 1024)
    @Column(name = "logo_url", length = 1024)
    private String logoUrl;

    @NotNull
    @Size(max = 255)
    @Column(name = "contact_email", length = 255, nullable = false)
    private String contactEmail;

    @Size(max = 50)
    @Column(name = "contact_phone", length = 50)
    private String contactPhone;

    @Size(max = 20)
    @Column(name = "subscription_plan", length = 20)
    private String subscriptionPlan;

    @Size(max = 20)
    @Column(name = "subscription_status", length = 20)
    private String subscriptionStatus;

    @Column(name = "subscription_start_date")
    private LocalDate subscriptionStartDate;

    @Column(name = "subscription_end_date")
    private LocalDate subscriptionEndDate;

    @Column(name = "monthly_fee_usd", precision = 21, scale = 2)
    private BigDecimal monthlyFeeUsd;

    @Size(max = 255)
    @Column(name = "stripe_customer_id", length = 255)
    private String stripeCustomerId;

    @Column(name = "is_active")
    private Boolean isActive;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    /* @JsonIgnoreProperties(value = { "tenantOrganization" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "tenantOrganization")
    private TenantSettings tenantSettings;*/

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TenantOrganization id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public TenantOrganization tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getOrganizationName() {
        return this.organizationName;
    }

    public TenantOrganization organizationName(String organizationName) {
        this.setOrganizationName(organizationName);
        return this;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getDomain() {
        return this.domain;
    }

    public TenantOrganization domain(String domain) {
        this.setDomain(domain);
        return this;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPrimaryColor() {
        return this.primaryColor;
    }

    public TenantOrganization primaryColor(String primaryColor) {
        this.setPrimaryColor(primaryColor);
        return this;
    }

    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public String getSecondaryColor() {
        return this.secondaryColor;
    }

    public TenantOrganization secondaryColor(String secondaryColor) {
        this.setSecondaryColor(secondaryColor);
        return this;
    }

    public void setSecondaryColor(String secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public String getLogoUrl() {
        return this.logoUrl;
    }

    public TenantOrganization logoUrl(String logoUrl) {
        this.setLogoUrl(logoUrl);
        return this;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getContactEmail() {
        return this.contactEmail;
    }

    public TenantOrganization contactEmail(String contactEmail) {
        this.setContactEmail(contactEmail);
        return this;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return this.contactPhone;
    }

    public TenantOrganization contactPhone(String contactPhone) {
        this.setContactPhone(contactPhone);
        return this;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getSubscriptionPlan() {
        return this.subscriptionPlan;
    }

    public TenantOrganization subscriptionPlan(String subscriptionPlan) {
        this.setSubscriptionPlan(subscriptionPlan);
        return this;
    }

    public void setSubscriptionPlan(String subscriptionPlan) {
        this.subscriptionPlan = subscriptionPlan;
    }

    public String getSubscriptionStatus() {
        return this.subscriptionStatus;
    }

    public TenantOrganization subscriptionStatus(String subscriptionStatus) {
        this.setSubscriptionStatus(subscriptionStatus);
        return this;
    }

    public void setSubscriptionStatus(String subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public LocalDate getSubscriptionStartDate() {
        return this.subscriptionStartDate;
    }

    public TenantOrganization subscriptionStartDate(LocalDate subscriptionStartDate) {
        this.setSubscriptionStartDate(subscriptionStartDate);
        return this;
    }

    public void setSubscriptionStartDate(LocalDate subscriptionStartDate) {
        this.subscriptionStartDate = subscriptionStartDate;
    }

    public LocalDate getSubscriptionEndDate() {
        return this.subscriptionEndDate;
    }

    public TenantOrganization subscriptionEndDate(LocalDate subscriptionEndDate) {
        this.setSubscriptionEndDate(subscriptionEndDate);
        return this;
    }

    public void setSubscriptionEndDate(LocalDate subscriptionEndDate) {
        this.subscriptionEndDate = subscriptionEndDate;
    }

    public BigDecimal getMonthlyFeeUsd() {
        return this.monthlyFeeUsd;
    }

    public TenantOrganization monthlyFeeUsd(BigDecimal monthlyFeeUsd) {
        this.setMonthlyFeeUsd(monthlyFeeUsd);
        return this;
    }

    public void setMonthlyFeeUsd(BigDecimal monthlyFeeUsd) {
        this.monthlyFeeUsd = monthlyFeeUsd;
    }

    public String getStripeCustomerId() {
        return this.stripeCustomerId;
    }

    public TenantOrganization stripeCustomerId(String stripeCustomerId) {
        this.setStripeCustomerId(stripeCustomerId);
        return this;
    }

    public void setStripeCustomerId(String stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public TenantOrganization isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public TenantOrganization createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public TenantOrganization updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /*  public TenantSettings getTenantSettings() {
        return this.tenantSettings;
    }*/

    /*  public void setTenantSettings(TenantSettings tenantSettings) {
        if (this.tenantSettings != null) {
            this.tenantSettings.setTenantOrganization(null);
        }
        if (tenantSettings != null) {
            tenantSettings.setTenantOrganization(this);
        }
        this.tenantSettings = tenantSettings;
    }*/

    /* public TenantOrganization tenantSettings(TenantSettings tenantSettings) {
        this.setTenantSettings(tenantSettings);
        return this;
    }*/

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TenantOrganization)) {
            return false;
        }
        return getId() != null && getId().equals(((TenantOrganization) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TenantOrganization{" +
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
