package com.eventsitemanager.service.dto;

import com.eventsitemanager.domain.enumeration.DomainOwnership;
import com.eventsitemanager.domain.enumeration.OnboardingRequestSource;
import com.eventsitemanager.domain.enumeration.SiteType;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * Body for {@code POST /api/tenant-onboarding-requests/submit}. Status, request code and review
 * fields are always set server-side; they are intentionally absent here.
 */
public class TenantOnboardingSubmitDTO implements Serializable {

    @NotBlank
    @Size(max = 255)
    private String organizationName;

    private SiteType siteType;

    @Size(max = 1000)
    private String description;

    @Size(max = 255)
    private String contactFirstName;

    @Size(max = 255)
    private String contactLastName;

    @NotBlank
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

    @NotBlank
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

    @Size(max = 4000)
    private String customerNotes;

    /** PUBLIC_FORM (default) or ADMIN_MANUAL when a SUPER_ADMIN enters a request on behalf of a customer. */
    private OnboardingRequestSource source;

    @Size(max = 64)
    private String submitterIp;

    @Size(max = 512)
    private String userAgent;

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

    public OnboardingRequestSource getSource() {
        return source;
    }

    public void setSource(OnboardingRequestSource source) {
        this.source = source;
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
}
