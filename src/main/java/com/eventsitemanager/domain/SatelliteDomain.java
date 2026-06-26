package com.eventsitemanager.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SatelliteDomain.
 */
@Entity
@Table(name = "satellite_domain")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SatelliteDomain implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "satelliteDomainSeq")
    @SequenceGenerator(name = "satelliteDomainSeq", sequenceName = "public.satellite_domain_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "satellite_key", length = 100, nullable = false, unique = true)
    private String satelliteKey;

    @NotNull
    @Size(max = 500)
    @Column(name = "domain", length = 500, nullable = false)
    private String domain;

    @NotNull
    @Size(max = 255)
    @Column(name = "hostname", length = 255, nullable = false, unique = true)
    private String hostname;

    @NotNull
    @Size(max = 255)
    @Column(name = "display_name", length = 255, nullable = false)
    private String displayName;

    @Size(max = 255)
    @Column(name = "tenant_id", length = 255)
    private String tenantId;

    @NotNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

    @Column(name = "added_date")
    private ZonedDateTime addedDate;

    @Size(max = 255)
    @Column(name = "org_name", length = 255)
    private String orgName;

    @Size(max = 500)
    @Column(name = "full_name", length = 500)
    private String fullName;

    @Size(max = 500)
    @Column(name = "tagline", length = 500)
    private String tagline;

    @Size(max = 50)
    @Column(name = "logo_type", length = 50)
    private String logoType = "text";

    @Size(max = 1024)
    @Column(name = "logo_url", length = 1024)
    private String logoUrl;

    @Size(max = 50)
    @Column(name = "logo_primary_color", length = 50)
    private String logoPrimaryColor;

    @Size(max = 50)
    @Column(name = "logo_secondary_color", length = 50)
    private String logoSecondaryColor;

    @Size(max = 50)
    @Column(name = "theme_primary_color", length = 50)
    private String themePrimaryColor;

    @Size(max = 50)
    @Column(name = "theme_hover_color", length = 50)
    private String themeHoverColor;

    @Size(max = 50)
    @Column(name = "theme_active_color", length = 50)
    private String themeActiveColor;

    @Size(max = 1024)
    @Column(name = "contact_address", length = 1024)
    private String contactAddress;

    @Size(max = 100)
    @Column(name = "contact_phone", length = 100)
    private String contactPhone;

    @Size(max = 100)
    @Column(name = "contact_toll_free", length = 100)
    private String contactTollFree;

    @Size(max = 255)
    @Column(name = "contact_email", length = 255)
    private String contactEmail;

    @Size(max = 1024)
    @Column(name = "social_facebook", length = 1024)
    private String socialFacebook;

    @Size(max = 1024)
    @Column(name = "social_twitter", length = 1024)
    private String socialTwitter;

    @Size(max = 1024)
    @Column(name = "social_linkedin", length = 1024)
    private String socialLinkedin;

    @Size(max = 1024)
    @Column(name = "social_youtube", length = 1024)
    private String socialYoutube;

    @NotNull
    @Column(name = "show_on_auth_header", nullable = false)
    private Boolean showOnAuthHeader = true;

    @NotNull
    @Column(name = "show_on_auth_footer", nullable = false)
    private Boolean showOnAuthFooter = true;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SatelliteDomain id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSatelliteKey() {
        return this.satelliteKey;
    }

    public SatelliteDomain satelliteKey(String satelliteKey) {
        this.setSatelliteKey(satelliteKey);
        return this;
    }

    public void setSatelliteKey(String satelliteKey) {
        this.satelliteKey = satelliteKey;
    }

    public String getDomain() {
        return this.domain;
    }

    public SatelliteDomain domain(String domain) {
        this.setDomain(domain);
        return this;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getHostname() {
        return this.hostname;
    }

    public SatelliteDomain hostname(String hostname) {
        this.setHostname(hostname);
        return this;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public SatelliteDomain displayName(String displayName) {
        this.setDisplayName(displayName);
        return this;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public SatelliteDomain tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Boolean getEnabled() {
        return this.enabled;
    }

    public SatelliteDomain enabled(Boolean enabled) {
        this.setEnabled(enabled);
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public ZonedDateTime getAddedDate() {
        return this.addedDate;
    }

    public SatelliteDomain addedDate(ZonedDateTime addedDate) {
        this.setAddedDate(addedDate);
        return this;
    }

    public void setAddedDate(ZonedDateTime addedDate) {
        this.addedDate = addedDate;
    }

    public String getOrgName() {
        return this.orgName;
    }

    public SatelliteDomain orgName(String orgName) {
        this.setOrgName(orgName);
        return this;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getFullName() {
        return this.fullName;
    }

    public SatelliteDomain fullName(String fullName) {
        this.setFullName(fullName);
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTagline() {
        return this.tagline;
    }

    public SatelliteDomain tagline(String tagline) {
        this.setTagline(tagline);
        return this;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getLogoType() {
        return this.logoType;
    }

    public SatelliteDomain logoType(String logoType) {
        this.setLogoType(logoType);
        return this;
    }

    public void setLogoType(String logoType) {
        this.logoType = logoType;
    }

    public String getLogoUrl() {
        return this.logoUrl;
    }

    public SatelliteDomain logoUrl(String logoUrl) {
        this.setLogoUrl(logoUrl);
        return this;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getLogoPrimaryColor() {
        return this.logoPrimaryColor;
    }

    public SatelliteDomain logoPrimaryColor(String logoPrimaryColor) {
        this.setLogoPrimaryColor(logoPrimaryColor);
        return this;
    }

    public void setLogoPrimaryColor(String logoPrimaryColor) {
        this.logoPrimaryColor = logoPrimaryColor;
    }

    public String getLogoSecondaryColor() {
        return this.logoSecondaryColor;
    }

    public SatelliteDomain logoSecondaryColor(String logoSecondaryColor) {
        this.setLogoSecondaryColor(logoSecondaryColor);
        return this;
    }

    public void setLogoSecondaryColor(String logoSecondaryColor) {
        this.logoSecondaryColor = logoSecondaryColor;
    }

    public String getThemePrimaryColor() {
        return this.themePrimaryColor;
    }

    public SatelliteDomain themePrimaryColor(String themePrimaryColor) {
        this.setThemePrimaryColor(themePrimaryColor);
        return this;
    }

    public void setThemePrimaryColor(String themePrimaryColor) {
        this.themePrimaryColor = themePrimaryColor;
    }

    public String getThemeHoverColor() {
        return this.themeHoverColor;
    }

    public SatelliteDomain themeHoverColor(String themeHoverColor) {
        this.setThemeHoverColor(themeHoverColor);
        return this;
    }

    public void setThemeHoverColor(String themeHoverColor) {
        this.themeHoverColor = themeHoverColor;
    }

    public String getThemeActiveColor() {
        return this.themeActiveColor;
    }

    public SatelliteDomain themeActiveColor(String themeActiveColor) {
        this.setThemeActiveColor(themeActiveColor);
        return this;
    }

    public void setThemeActiveColor(String themeActiveColor) {
        this.themeActiveColor = themeActiveColor;
    }

    public String getContactAddress() {
        return this.contactAddress;
    }

    public SatelliteDomain contactAddress(String contactAddress) {
        this.setContactAddress(contactAddress);
        return this;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public String getContactPhone() {
        return this.contactPhone;
    }

    public SatelliteDomain contactPhone(String contactPhone) {
        this.setContactPhone(contactPhone);
        return this;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactTollFree() {
        return this.contactTollFree;
    }

    public SatelliteDomain contactTollFree(String contactTollFree) {
        this.setContactTollFree(contactTollFree);
        return this;
    }

    public void setContactTollFree(String contactTollFree) {
        this.contactTollFree = contactTollFree;
    }

    public String getContactEmail() {
        return this.contactEmail;
    }

    public SatelliteDomain contactEmail(String contactEmail) {
        this.setContactEmail(contactEmail);
        return this;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getSocialFacebook() {
        return this.socialFacebook;
    }

    public SatelliteDomain socialFacebook(String socialFacebook) {
        this.setSocialFacebook(socialFacebook);
        return this;
    }

    public void setSocialFacebook(String socialFacebook) {
        this.socialFacebook = socialFacebook;
    }

    public String getSocialTwitter() {
        return this.socialTwitter;
    }

    public SatelliteDomain socialTwitter(String socialTwitter) {
        this.setSocialTwitter(socialTwitter);
        return this;
    }

    public void setSocialTwitter(String socialTwitter) {
        this.socialTwitter = socialTwitter;
    }

    public String getSocialLinkedin() {
        return this.socialLinkedin;
    }

    public SatelliteDomain socialLinkedin(String socialLinkedin) {
        this.setSocialLinkedin(socialLinkedin);
        return this;
    }

    public void setSocialLinkedin(String socialLinkedin) {
        this.socialLinkedin = socialLinkedin;
    }

    public String getSocialYoutube() {
        return this.socialYoutube;
    }

    public SatelliteDomain socialYoutube(String socialYoutube) {
        this.setSocialYoutube(socialYoutube);
        return this;
    }

    public void setSocialYoutube(String socialYoutube) {
        this.socialYoutube = socialYoutube;
    }

    public Boolean getShowOnAuthHeader() {
        return this.showOnAuthHeader;
    }

    public SatelliteDomain showOnAuthHeader(Boolean showOnAuthHeader) {
        this.setShowOnAuthHeader(showOnAuthHeader);
        return this;
    }

    public void setShowOnAuthHeader(Boolean showOnAuthHeader) {
        this.showOnAuthHeader = showOnAuthHeader;
    }

    public Boolean getShowOnAuthFooter() {
        return this.showOnAuthFooter;
    }

    public SatelliteDomain showOnAuthFooter(Boolean showOnAuthFooter) {
        this.setShowOnAuthFooter(showOnAuthFooter);
        return this;
    }

    public void setShowOnAuthFooter(Boolean showOnAuthFooter) {
        this.showOnAuthFooter = showOnAuthFooter;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public SatelliteDomain createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public SatelliteDomain updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SatelliteDomain)) {
            return false;
        }
        return getId() != null && getId().equals(((SatelliteDomain) o).getId());
    }

    @Override
    public int hashCode() {
        // see
        // https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SatelliteDomain{" +
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
