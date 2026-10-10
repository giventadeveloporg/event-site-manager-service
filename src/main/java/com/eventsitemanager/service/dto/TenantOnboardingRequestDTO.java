package com.eventsitemanager.service.dto;

import com.eventsitemanager.domain.enumeration.DomainOwnership;
import com.eventsitemanager.domain.enumeration.OnboardingRequestSource;
import com.eventsitemanager.domain.enumeration.OnboardingRequestStatus;
import com.eventsitemanager.domain.enumeration.SiteType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.eventsitemanager.domain.TenantOnboardingRequest} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TenantOnboardingRequestDTO implements Serializable {

    private Long id;

    @Size(max = 64)
    private String requestCode;

    private OnboardingRequestStatus status;

    private OnboardingRequestSource source;

    @Size(max = 255)
    private String organizationName;

    private SiteType siteType;

    @Size(max = 1000)
    private String description;

    @Size(max = 255)
    private String contactFirstName;

    @Size(max = 255)
    private String contactLastName;

    @Email
    @Size(max = 255)
    private String contactEmail;

    @Size(max = 50)
    private String contactPhone;

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

    @Size(max = 255)
    private String requestedHostname;

    private DomainOwnership domainOwnership;

    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$")
    private String primaryColor;

    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$")
    private String secondaryColor;

    @Size(max = 1024)
    private String logoUrl;

    private Boolean wantsPayments;

    private String customerNotes;

    @Size(max = 255)
    private String assignedTenantId;

    @Size(max = 100)
    private String satelliteKey;

    @Email
    @Size(max = 255)
    private String noreplyEmail;

    private String adminComments;

    @Size(max = 255)
    private String reviewedByClerkUserId;

    @Size(max = 255)
    private String reviewedByEmail;

    private ZonedDateTime reviewedAt;

    private ZonedDateTime approvedAt;

    private ZonedDateTime rejectedAt;

    private String provisioningResult;

    @Size(max = 64)
    private String submitterIp;

    @Size(max = 512)
    private String userAgent;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public OnboardingRequestStatus getStatus() {
        return status;
    }

    public void setStatus(OnboardingRequestStatus status) {
        this.status = status;
    }

    public OnboardingRequestSource getSource() {
        return source;
    }

    public void setSource(OnboardingRequestSource source) {
        this.source = source;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public SiteType getSiteType() {
        return siteType;
    }

    public void setSiteType(SiteType siteType) {
        this.siteType = siteType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContactFirstName() {
        return contactFirstName;
    }

    public void setContactFirstName(String contactFirstName) {
        this.contactFirstName = contactFirstName;
    }

    public String getContactLastName() {
        return contactLastName;
    }

    public void setContactLastName(String contactLastName) {
        this.contactLastName = contactLastName;
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

    public String getRequestedHostname() {
        return requestedHostname;
    }

    public void setRequestedHostname(String requestedHostname) {
        this.requestedHostname = requestedHostname;
    }

    public DomainOwnership getDomainOwnership() {
        return domainOwnership;
    }

    public void setDomainOwnership(DomainOwnership domainOwnership) {
        this.domainOwnership = domainOwnership;
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

    public Boolean getWantsPayments() {
        return wantsPayments;
    }

    public void setWantsPayments(Boolean wantsPayments) {
        this.wantsPayments = wantsPayments;
    }

    public String getCustomerNotes() {
        return customerNotes;
    }

    public void setCustomerNotes(String customerNotes) {
        this.customerNotes = customerNotes;
    }

    public String getAssignedTenantId() {
        return assignedTenantId;
    }

    public void setAssignedTenantId(String assignedTenantId) {
        this.assignedTenantId = assignedTenantId;
    }

    public String getSatelliteKey() {
        return satelliteKey;
    }

    public void setSatelliteKey(String satelliteKey) {
        this.satelliteKey = satelliteKey;
    }

    public String getNoreplyEmail() {
        return noreplyEmail;
    }

    public void setNoreplyEmail(String noreplyEmail) {
        this.noreplyEmail = noreplyEmail;
    }

    public String getAdminComments() {
        return adminComments;
    }

    public void setAdminComments(String adminComments) {
        this.adminComments = adminComments;
    }

    public String getReviewedByClerkUserId() {
        return reviewedByClerkUserId;
    }

    public void setReviewedByClerkUserId(String reviewedByClerkUserId) {
        this.reviewedByClerkUserId = reviewedByClerkUserId;
    }

    public String getReviewedByEmail() {
        return reviewedByEmail;
    }

    public void setReviewedByEmail(String reviewedByEmail) {
        this.reviewedByEmail = reviewedByEmail;
    }

    public ZonedDateTime getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(ZonedDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public ZonedDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(ZonedDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public ZonedDateTime getRejectedAt() {
        return rejectedAt;
    }

    public void setRejectedAt(ZonedDateTime rejectedAt) {
        this.rejectedAt = rejectedAt;
    }

    public String getProvisioningResult() {
        return provisioningResult;
    }

    public void setProvisioningResult(String provisioningResult) {
        this.provisioningResult = provisioningResult;
    }

    public String getSubmitterIp() {
        return submitterIp;
    }

    public void setSubmitterIp(String submitterIp) {
        this.submitterIp = submitterIp;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
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
        if (!(o instanceof TenantOnboardingRequestDTO)) {
            return false;
        }
        TenantOnboardingRequestDTO other = (TenantOnboardingRequestDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return (
            "TenantOnboardingRequestDTO{" +
            "id=" +
            getId() +
            ", requestCode='" +
            getRequestCode() +
            "'" +
            ", status='" +
            getStatus() +
            "'" +
            ", organizationName='" +
            getOrganizationName() +
            "'" +
            ", requestedHostname='" +
            getRequestedHostname() +
            "'" +
            ", assignedTenantId='" +
            getAssignedTenantId() +
            "'" +
            "}"
        );
    }
}
