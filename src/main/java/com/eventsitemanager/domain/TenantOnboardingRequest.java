package com.eventsitemanager.domain;

import com.eventsitemanager.domain.enumeration.DomainOwnership;
import com.eventsitemanager.domain.enumeration.OnboardingRequestSource;
import com.eventsitemanager.domain.enumeration.OnboardingRequestStatus;
import com.eventsitemanager.domain.enumeration.SiteType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * Platform-level request to onboard a new customer (tenant + satellite domain).
 * Not tenant-scoped: {@code assignedTenantId} is only set by SUPER_ADMIN at approval.
 */
@Entity
@Table(name = "tenant_onboarding_request")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TenantOnboardingRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tenantOnboardingRequestSeq")
    @SequenceGenerator(name = "tenantOnboardingRequestSeq", sequenceName = "public.tenant_onboarding_request_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 64)
    @Column(name = "request_code", length = 64, nullable = false, unique = true)
    private String requestCode;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30, nullable = false)
    private OnboardingRequestStatus status = OnboardingRequestStatus.PENDING;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "source", length = 32, nullable = false)
    private OnboardingRequestSource source = OnboardingRequestSource.PUBLIC_FORM;

    @NotNull
    @Size(max = 255)
    @Column(name = "organization_name", length = 255, nullable = false)
    private String organizationName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "site_type", length = 32, nullable = false)
    private SiteType siteType = SiteType.EVENT_ORG;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @Size(max = 255)
    @Column(name = "contact_first_name", length = 255)
    private String contactFirstName;

    @Size(max = 255)
    @Column(name = "contact_last_name", length = 255)
    private String contactLastName;

    @NotNull
    @Size(max = 255)
    @Column(name = "contact_email", length = 255, nullable = false)
    private String contactEmail;

    @Size(max = 50)
    @Column(name = "contact_phone", length = 50)
    private String contactPhone;

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

    @NotNull
    @Size(max = 255)
    @Column(name = "requested_hostname", length = 255, nullable = false)
    private String requestedHostname;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "domain_ownership", length = 32, nullable = false)
    private DomainOwnership domainOwnership = DomainOwnership.CUSTOMER_REGISTRAR;

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
    @Column(name = "wants_payments", nullable = false)
    private Boolean wantsPayments = false;

    @Column(name = "customer_notes", columnDefinition = "TEXT")
    private String customerNotes;

    @Size(max = 255)
    @Column(name = "assigned_tenant_id", length = 255, unique = true)
    private String assignedTenantId;

    @Size(max = 100)
    @Column(name = "satellite_key", length = 100)
    private String satelliteKey;

    @Size(max = 255)
    @Column(name = "noreply_email", length = 255)
    private String noreplyEmail;

    @Column(name = "admin_comments", columnDefinition = "TEXT")
    private String adminComments;

    @Size(max = 255)
    @Column(name = "reviewed_by_clerk_user_id", length = 255)
    private String reviewedByClerkUserId;

    @Size(max = 255)
    @Column(name = "reviewed_by_email", length = 255)
    private String reviewedByEmail;

    @Column(name = "reviewed_at")
    private ZonedDateTime reviewedAt;

    @Column(name = "approved_at")
    private ZonedDateTime approvedAt;

    @Column(name = "rejected_at")
    private ZonedDateTime rejectedAt;

    @Column(name = "provisioning_result", columnDefinition = "TEXT")
    private String provisioningResult;

    @Size(max = 64)
    @Column(name = "submitter_ip", length = 64)
    private String submitterIp;

    @Size(max = 512)
    @Column(name = "user_agent", length = 512)
    private String userAgent;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        ZonedDateTime now = ZonedDateTime.now();
        if (createdAt == null) {
            createdAt = now;
        }
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = ZonedDateTime.now();
    }

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
        if (!(o instanceof TenantOnboardingRequest)) {
            return false;
        }
        return getId() != null && getId().equals(((TenantOnboardingRequest) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "TenantOnboardingRequest{" +
            "id=" +
            getId() +
            ", requestCode='" +
            getRequestCode() +
            "'" +
            ", status='" +
            getStatus() +
            "'" +
            ", source='" +
            getSource() +
            "'" +
            ", organizationName='" +
            getOrganizationName() +
            "'" +
            ", siteType='" +
            getSiteType() +
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
