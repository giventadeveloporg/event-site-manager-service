package com.eventsitemanager.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.eventsitemanager.domain.TenantOrganization} entity. This class is used
 * in {@link com.eventsitemanager.web.rest.TenantOrganizationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tenant-organizations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TenantOrganizationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private StringFilter organizationName;

    private StringFilter domain;

    private StringFilter primaryColor;

    private StringFilter secondaryColor;

    private StringFilter logoUrl;

    private StringFilter contactEmail;

    private StringFilter contactPhone;

    private StringFilter subscriptionPlan;

    private StringFilter subscriptionStatus;

    private LocalDateFilter subscriptionStartDate;

    private LocalDateFilter subscriptionEndDate;

    private BigDecimalFilter monthlyFeeUsd;

    private StringFilter stripeCustomerId;

    private BooleanFilter isActive;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private LongFilter tenantSettingsId;

    private Boolean distinct;

    public TenantOrganizationCriteria() {}

    public TenantOrganizationCriteria(TenantOrganizationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.organizationName = other.organizationName == null ? null : other.organizationName.copy();
        this.domain = other.domain == null ? null : other.domain.copy();
        this.primaryColor = other.primaryColor == null ? null : other.primaryColor.copy();
        this.secondaryColor = other.secondaryColor == null ? null : other.secondaryColor.copy();
        this.logoUrl = other.logoUrl == null ? null : other.logoUrl.copy();
        this.contactEmail = other.contactEmail == null ? null : other.contactEmail.copy();
        this.contactPhone = other.contactPhone == null ? null : other.contactPhone.copy();
        this.subscriptionPlan = other.subscriptionPlan == null ? null : other.subscriptionPlan.copy();
        this.subscriptionStatus = other.subscriptionStatus == null ? null : other.subscriptionStatus.copy();
        this.subscriptionStartDate = other.subscriptionStartDate == null ? null : other.subscriptionStartDate.copy();
        this.subscriptionEndDate = other.subscriptionEndDate == null ? null : other.subscriptionEndDate.copy();
        this.monthlyFeeUsd = other.monthlyFeeUsd == null ? null : other.monthlyFeeUsd.copy();
        this.stripeCustomerId = other.stripeCustomerId == null ? null : other.stripeCustomerId.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.tenantSettingsId = other.tenantSettingsId == null ? null : other.tenantSettingsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TenantOrganizationCriteria copy() {
        return new TenantOrganizationCriteria(this);
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

    public StringFilter getOrganizationName() {
        return organizationName;
    }

    public StringFilter organizationName() {
        if (organizationName == null) {
            organizationName = new StringFilter();
        }
        return organizationName;
    }

    public void setOrganizationName(StringFilter organizationName) {
        this.organizationName = organizationName;
    }

    public StringFilter getDomain() {
        return domain;
    }

    public StringFilter domain() {
        if (domain == null) {
            domain = new StringFilter();
        }
        return domain;
    }

    public void setDomain(StringFilter domain) {
        this.domain = domain;
    }

    public StringFilter getPrimaryColor() {
        return primaryColor;
    }

    public StringFilter primaryColor() {
        if (primaryColor == null) {
            primaryColor = new StringFilter();
        }
        return primaryColor;
    }

    public void setPrimaryColor(StringFilter primaryColor) {
        this.primaryColor = primaryColor;
    }

    public StringFilter getSecondaryColor() {
        return secondaryColor;
    }

    public StringFilter secondaryColor() {
        if (secondaryColor == null) {
            secondaryColor = new StringFilter();
        }
        return secondaryColor;
    }

    public void setSecondaryColor(StringFilter secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public StringFilter getLogoUrl() {
        return logoUrl;
    }

    public StringFilter logoUrl() {
        if (logoUrl == null) {
            logoUrl = new StringFilter();
        }
        return logoUrl;
    }

    public void setLogoUrl(StringFilter logoUrl) {
        this.logoUrl = logoUrl;
    }

    public StringFilter getContactEmail() {
        return contactEmail;
    }

    public StringFilter contactEmail() {
        if (contactEmail == null) {
            contactEmail = new StringFilter();
        }
        return contactEmail;
    }

    public void setContactEmail(StringFilter contactEmail) {
        this.contactEmail = contactEmail;
    }

    public StringFilter getContactPhone() {
        return contactPhone;
    }

    public StringFilter contactPhone() {
        if (contactPhone == null) {
            contactPhone = new StringFilter();
        }
        return contactPhone;
    }

    public void setContactPhone(StringFilter contactPhone) {
        this.contactPhone = contactPhone;
    }

    public StringFilter getSubscriptionPlan() {
        return subscriptionPlan;
    }

    public StringFilter subscriptionPlan() {
        if (subscriptionPlan == null) {
            subscriptionPlan = new StringFilter();
        }
        return subscriptionPlan;
    }

    public void setSubscriptionPlan(StringFilter subscriptionPlan) {
        this.subscriptionPlan = subscriptionPlan;
    }

    public StringFilter getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public StringFilter subscriptionStatus() {
        if (subscriptionStatus == null) {
            subscriptionStatus = new StringFilter();
        }
        return subscriptionStatus;
    }

    public void setSubscriptionStatus(StringFilter subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public LocalDateFilter getSubscriptionStartDate() {
        return subscriptionStartDate;
    }

    public LocalDateFilter subscriptionStartDate() {
        if (subscriptionStartDate == null) {
            subscriptionStartDate = new LocalDateFilter();
        }
        return subscriptionStartDate;
    }

    public void setSubscriptionStartDate(LocalDateFilter subscriptionStartDate) {
        this.subscriptionStartDate = subscriptionStartDate;
    }

    public LocalDateFilter getSubscriptionEndDate() {
        return subscriptionEndDate;
    }

    public LocalDateFilter subscriptionEndDate() {
        if (subscriptionEndDate == null) {
            subscriptionEndDate = new LocalDateFilter();
        }
        return subscriptionEndDate;
    }

    public void setSubscriptionEndDate(LocalDateFilter subscriptionEndDate) {
        this.subscriptionEndDate = subscriptionEndDate;
    }

    public BigDecimalFilter getMonthlyFeeUsd() {
        return monthlyFeeUsd;
    }

    public BigDecimalFilter monthlyFeeUsd() {
        if (monthlyFeeUsd == null) {
            monthlyFeeUsd = new BigDecimalFilter();
        }
        return monthlyFeeUsd;
    }

    public void setMonthlyFeeUsd(BigDecimalFilter monthlyFeeUsd) {
        this.monthlyFeeUsd = monthlyFeeUsd;
    }

    public StringFilter getStripeCustomerId() {
        return stripeCustomerId;
    }

    public StringFilter stripeCustomerId() {
        if (stripeCustomerId == null) {
            stripeCustomerId = new StringFilter();
        }
        return stripeCustomerId;
    }

    public void setStripeCustomerId(StringFilter stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            isActive = new BooleanFilter();
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public ZonedDateTimeFilter getCreatedAt() {
        return createdAt;
    }

    public ZonedDateTimeFilter createdAt() {
        if (createdAt == null) {
            createdAt = new ZonedDateTimeFilter();
        }
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTimeFilter createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTimeFilter getUpdatedAt() {
        return updatedAt;
    }

    public ZonedDateTimeFilter updatedAt() {
        if (updatedAt == null) {
            updatedAt = new ZonedDateTimeFilter();
        }
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTimeFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LongFilter getTenantSettingsId() {
        return tenantSettingsId;
    }

    public LongFilter tenantSettingsId() {
        if (tenantSettingsId == null) {
            tenantSettingsId = new LongFilter();
        }
        return tenantSettingsId;
    }

    public void setTenantSettingsId(LongFilter tenantSettingsId) {
        this.tenantSettingsId = tenantSettingsId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TenantOrganizationCriteria that = (TenantOrganizationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(organizationName, that.organizationName) &&
            Objects.equals(domain, that.domain) &&
            Objects.equals(primaryColor, that.primaryColor) &&
            Objects.equals(secondaryColor, that.secondaryColor) &&
            Objects.equals(logoUrl, that.logoUrl) &&
            Objects.equals(contactEmail, that.contactEmail) &&
            Objects.equals(contactPhone, that.contactPhone) &&
            Objects.equals(subscriptionPlan, that.subscriptionPlan) &&
            Objects.equals(subscriptionStatus, that.subscriptionStatus) &&
            Objects.equals(subscriptionStartDate, that.subscriptionStartDate) &&
            Objects.equals(subscriptionEndDate, that.subscriptionEndDate) &&
            Objects.equals(monthlyFeeUsd, that.monthlyFeeUsd) &&
            Objects.equals(stripeCustomerId, that.stripeCustomerId) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(tenantSettingsId, that.tenantSettingsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            tenantId,
            organizationName,
            domain,
            primaryColor,
            secondaryColor,
            logoUrl,
            contactEmail,
            contactPhone,
            subscriptionPlan,
            subscriptionStatus,
            subscriptionStartDate,
            subscriptionEndDate,
            monthlyFeeUsd,
            stripeCustomerId,
            isActive,
            createdAt,
            updatedAt,
            tenantSettingsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TenantOrganizationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
            (organizationName != null ? "organizationName=" + organizationName + ", " : "") +
            (domain != null ? "domain=" + domain + ", " : "") +
            (primaryColor != null ? "primaryColor=" + primaryColor + ", " : "") +
            (secondaryColor != null ? "secondaryColor=" + secondaryColor + ", " : "") +
            (logoUrl != null ? "logoUrl=" + logoUrl + ", " : "") +
            (contactEmail != null ? "contactEmail=" + contactEmail + ", " : "") +
            (contactPhone != null ? "contactPhone=" + contactPhone + ", " : "") +
            (subscriptionPlan != null ? "subscriptionPlan=" + subscriptionPlan + ", " : "") +
            (subscriptionStatus != null ? "subscriptionStatus=" + subscriptionStatus + ", " : "") +
            (subscriptionStartDate != null ? "subscriptionStartDate=" + subscriptionStartDate + ", " : "") +
            (subscriptionEndDate != null ? "subscriptionEndDate=" + subscriptionEndDate + ", " : "") +
            (monthlyFeeUsd != null ? "monthlyFeeUsd=" + monthlyFeeUsd + ", " : "") +
            (stripeCustomerId != null ? "stripeCustomerId=" + stripeCustomerId + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (tenantSettingsId != null ? "tenantSettingsId=" + tenantSettingsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
