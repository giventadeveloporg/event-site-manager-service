package com.eventsitemanager.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * Body for {@code POST /api/tenant-onboarding-requests/{id}/approve}. Values chosen by the SUPER_ADMIN on the
 * review screen; anything left blank falls back to the request row or a derived default.
 */
public class TenantOnboardingApproveDTO implements Serializable {

    @NotBlank
    @Size(max = 255)
    @Pattern(regexp = "^[a-z0-9][a-z0-9_]*[a-z0-9]$", message = "tenantId must be lowercase letters, digits and underscores")
    private String tenantId;

    @NotBlank
    @Size(max = 100)
    @Pattern(regexp = "^[a-z0-9][a-z0-9-]*[a-z0-9]$", message = "satelliteKey must be lowercase letters, digits and hyphens")
    private String satelliteKey;

    /** Overrides {@code requestedHostname} when set (e.g. normalising to the www. form). */
    @Size(max = 255)
    private String hostname;

    /** {@code tenant_organization.domain}; defaults to the hostname without a leading www. */
    @Size(max = 255)
    private String organizationDomain;

    @Size(max = 255)
    private String displayName;

    @Email
    @Size(max = 255)
    private String contactEmail;

    @Email
    @Size(max = 255)
    private String infoEmail;

    @Email
    @Size(max = 255)
    private String noreplyEmail;

    /** Tenant whose ADMIN / SUPER_ADMIN profiles are cloned into the new tenant. */
    @Size(max = 255)
    private String adminSourceTenantId;

    private Boolean cloneAdmins;

    private String adminComments;

    @Size(max = 255)
    private String reviewedByClerkUserId;

    @Size(max = 255)
    private String reviewedByEmail;

    private Boolean notifySubmitter;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getSatelliteKey() {
        return satelliteKey;
    }

    public void setSatelliteKey(String satelliteKey) {
        this.satelliteKey = satelliteKey;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getOrganizationDomain() {
        return organizationDomain;
    }

    public void setOrganizationDomain(String organizationDomain) {
        this.organizationDomain = organizationDomain;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getInfoEmail() {
        return infoEmail;
    }

    public void setInfoEmail(String infoEmail) {
        this.infoEmail = infoEmail;
    }

    public String getNoreplyEmail() {
        return noreplyEmail;
    }

    public void setNoreplyEmail(String noreplyEmail) {
        this.noreplyEmail = noreplyEmail;
    }

    public String getAdminSourceTenantId() {
        return adminSourceTenantId;
    }

    public void setAdminSourceTenantId(String adminSourceTenantId) {
        this.adminSourceTenantId = adminSourceTenantId;
    }

    public Boolean getCloneAdmins() {
        return cloneAdmins;
    }

    public void setCloneAdmins(Boolean cloneAdmins) {
        this.cloneAdmins = cloneAdmins;
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

    public Boolean getNotifySubmitter() {
        return notifySubmitter;
    }

    public void setNotifySubmitter(Boolean notifySubmitter) {
        this.notifySubmitter = notifySubmitter;
    }
}
