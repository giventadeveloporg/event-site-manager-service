package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.SatelliteDomain} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SatelliteDomainDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String satelliteKey;

    @NotNull
    @Size(max = 500)
    private String domain;

    @NotNull
    @Size(max = 255)
    private String hostname;

    @NotNull
    @Size(max = 255)
    private String displayName;

    @Size(max = 255)
    private String tenantId;

    @NotNull
    private Boolean enabled;

    private ZonedDateTime addedDate;

    @Size(max = 255)
    private String orgName;

    @Size(max = 500)
    private String fullName;

    @Size(max = 500)
    private String tagline;

    @Size(max = 50)
    private String logoType;

    @Size(max = 1024)
    private String logoUrl;

    @Size(max = 50)
    private String logoPrimaryColor;

    @Size(max = 50)
    private String logoSecondaryColor;

    @Size(max = 50)
    private String themePrimaryColor;

    @Size(max = 50)
    private String themeHoverColor;

    @Size(max = 50)
    private String themeActiveColor;

    @Size(max = 1024)
    private String contactAddress;

    @Size(max = 100)
    private String contactPhone;

    @Size(max = 100)
    private String contactTollFree;

    @Size(max = 255)
    private String contactEmail;

    @Size(max = 1024)
    private String socialFacebook;

    @Size(max = 1024)
    private String socialTwitter;

    @Size(max = 1024)
    private String socialLinkedin;

    @Size(max = 1024)
    private String socialYoutube;

    @NotNull
    private Boolean showOnAuthHeader;

    @NotNull
    private Boolean showOnAuthFooter;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSatelliteKey() {
        return satelliteKey;
    }

    public void setSatelliteKey(String satelliteKey) {
        this.satelliteKey = satelliteKey;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public ZonedDateTime getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(ZonedDateTime addedDate) {
        this.addedDate = addedDate;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getLogoType() {
        return logoType;
    }

    public void setLogoType(String logoType) {
        this.logoType = logoType;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getLogoPrimaryColor() {
        return logoPrimaryColor;
    }

    public void setLogoPrimaryColor(String logoPrimaryColor) {
        this.logoPrimaryColor = logoPrimaryColor;
    }

    public String getLogoSecondaryColor() {
        return logoSecondaryColor;
    }

    public void setLogoSecondaryColor(String logoSecondaryColor) {
        this.logoSecondaryColor = logoSecondaryColor;
    }

    public String getThemePrimaryColor() {
        return themePrimaryColor;
    }

    public void setThemePrimaryColor(String themePrimaryColor) {
        this.themePrimaryColor = themePrimaryColor;
    }

    public String getThemeHoverColor() {
        return themeHoverColor;
    }

    public void setThemeHoverColor(String themeHoverColor) {
        this.themeHoverColor = themeHoverColor;
    }

    public String getThemeActiveColor() {
        return themeActiveColor;
    }

    public void setThemeActiveColor(String themeActiveColor) {
        this.themeActiveColor = themeActiveColor;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactTollFree() {
        return contactTollFree;
    }

    public void setContactTollFree(String contactTollFree) {
        this.contactTollFree = contactTollFree;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getSocialFacebook() {
        return socialFacebook;
    }

    public void setSocialFacebook(String socialFacebook) {
        this.socialFacebook = socialFacebook;
    }

    public String getSocialTwitter() {
        return socialTwitter;
    }

    public void setSocialTwitter(String socialTwitter) {
        this.socialTwitter = socialTwitter;
    }

    public String getSocialLinkedin() {
        return socialLinkedin;
    }

    public void setSocialLinkedin(String socialLinkedin) {
        this.socialLinkedin = socialLinkedin;
    }

    public String getSocialYoutube() {
        return socialYoutube;
    }

    public void setSocialYoutube(String socialYoutube) {
        this.socialYoutube = socialYoutube;
    }

    public Boolean getShowOnAuthHeader() {
        return showOnAuthHeader;
    }

    public void setShowOnAuthHeader(Boolean showOnAuthHeader) {
        this.showOnAuthHeader = showOnAuthHeader;
    }

    public Boolean getShowOnAuthFooter() {
        return showOnAuthFooter;
    }

    public void setShowOnAuthFooter(Boolean showOnAuthFooter) {
        this.showOnAuthFooter = showOnAuthFooter;
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
        if (!(o instanceof SatelliteDomainDTO)) {
            return false;
        }

        SatelliteDomainDTO satelliteDomainDTO = (SatelliteDomainDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, satelliteDomainDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SatelliteDomainDTO{" +
                "id=" + getId() +
                ", satelliteKey='" + getSatelliteKey() + "'" +
                ", domain='" + getDomain() + "'" +
                ", hostname='" + getHostname() + "'" +
                ", displayName='" + getDisplayName() + "'" +
                ", tenantId='" + getTenantId() + "'" +
                ", enabled='" + getEnabled() + "'" +
                ", addedDate='" + getAddedDate() + "'" +
                ", orgName='" + getOrgName() + "'" +
                ", fullName='" + getFullName() + "'" +
                ", tagline='" + getTagline() + "'" +
                ", logoType='" + getLogoType() + "'" +
                ", logoUrl='" + getLogoUrl() + "'" +
                ", logoPrimaryColor='" + getLogoPrimaryColor() + "'" +
                ", logoSecondaryColor='" + getLogoSecondaryColor() + "'" +
                ", themePrimaryColor='" + getThemePrimaryColor() + "'" +
                ", themeHoverColor='" + getThemeHoverColor() + "'" +
                ", themeActiveColor='" + getThemeActiveColor() + "'" +
                ", contactAddress='" + getContactAddress() + "'" +
                ", contactPhone='" + getContactPhone() + "'" +
                ", contactTollFree='" + getContactTollFree() + "'" +
                ", contactEmail='" + getContactEmail() + "'" +
                ", socialFacebook='" + getSocialFacebook() + "'" +
                ", socialTwitter='" + getSocialTwitter() + "'" +
                ", socialLinkedin='" + getSocialLinkedin() + "'" +
                ", socialYoutube='" + getSocialYoutube() + "'" +
                ", showOnAuthHeader='" + getShowOnAuthHeader() + "'" +
                ", showOnAuthFooter='" + getShowOnAuthFooter() + "'" +
                ", createdAt='" + getCreatedAt() + "'" +
                ", updatedAt='" + getUpdatedAt() + "'" +
                "}";
    }
}
