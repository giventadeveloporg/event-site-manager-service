package com.eventsitemanager.service.dto;

import com.eventsitemanager.service.validation.TenantOrganizationProfileValidator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link com.eventsitemanager.domain.TenantSettings} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TenantSettingsDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String tenantId;

    private Boolean allowUserRegistration;

    private Boolean requireAdminApproval;

    private Boolean enableWhatsappIntegration;

    private Boolean enableEmailMarketing;

    private Boolean isMembershipSubscriptionEnabled;

    @Size(max = 500)
    private String whatsappApiKey;

    @Size(max = 2048)
    private String emailProviderConfig;

    private Integer maxEventsPerMonth;

    private Integer maxAttendeesPerEvent;

    private Boolean enableGuestRegistration;

    private Integer maxGuestsPerAttendee;

    private Integer defaultEventCapacity;

    private BigDecimal platformFeePercentage;

    @Size(max = 8192)
    private String customCss;

    @Size(max = 16384)
    private String customJs;

    private Boolean showEventsSectionInHomePage;

    private Boolean showTeamMembersSectionInHomePage;

    private Boolean showSponsorsSectionInHomePage;

    @Size(max = 500)
    private String twilioAccountSid;

    @Size(max = 1048)
    private String twilioAuthToken;

    @Size(max = 50)
    private String twilioWhatsappFrom;

    @Size(max = 1048)
    private String whatsappWebhookUrl;

    @Size(max = 1048)
    private String whatsappWebhookToken;

    @Size(max = 2048)
    private String emailFooterHtmlUrl;

    @Size(max = 2048)
    private String emailHeaderImageUrl;

    @Size(max = 2048)
    private String logoImageUrl;

    @Size(max = 255)
    @Schema(deprecated = true, description = "Deprecated v2.0 — use tenant-organization identity fields")
    private String addressLine1;

    @Size(max = 255)
    @Schema(deprecated = true, description = "Deprecated v2.0 — use tenant-organization identity fields")
    private String addressLine2;

    @Size(max = TenantOrganizationProfileValidator.MAX_DESCRIPTION_LENGTH)
    @Schema(deprecated = true, description = "Deprecated v2.0 — use tenant-organization identity fields")
    private String description;

    @Size(max = 255)
    @Schema(deprecated = true, description = "Deprecated v2.0 — use tenant-organization identity fields")
    private String city;

    @JsonIgnore
    private boolean descriptionSet;

    @JsonIgnore
    private boolean citySet;

    @JsonIgnore
    private boolean addressLine1Set;

    @JsonIgnore
    private boolean addressLine2Set;

    @JsonIgnore
    private boolean stateProvinceSet;

    @JsonIgnore
    private boolean zipCodeSet;

    @JsonIgnore
    private boolean countrySet;

    @Size(max = 50)
    private String phoneNumber;

    @Size(max = 20)
    @Schema(deprecated = true, description = "Deprecated v2.0 — use tenant-organization identity fields")
    private String zipCode;

    @Size(max = 100)
    @Schema(deprecated = true, description = "Deprecated v2.0 — use tenant-organization identity fields")
    private String country;

    @Size(max = 100)
    @Schema(deprecated = true, description = "Deprecated v2.0 — use tenant-organization identity fields")
    private String stateProvince;

    @Size(max = 255)
    private String email;

    @Size(max = 1024)
    private String facebookUrl;

    @Size(max = 1024)
    private String instagramUrl;

    @Size(max = 1024)
    private String twitterUrl;

    @Size(max = 1024)
    private String linkedinUrl;

    @Size(max = 1024)
    private String youtubeUrl;

    @Size(max = 1024)
    private String tiktokUrl;

    private Long homepageCacheVersion;

    @Schema(
        description = "JSON array of HTTPS URLs for default homepage hero slides",
        example = "[\"https://eventapp-media-bucket.s3.us-east-2.amazonaws.com/tenants/x/hero-defaults/slide-01.webp\"]"
    )
    @Size(max = 16384)
    private String defaultHeroImageUrlsJson;

    @Schema(
        description = "How tenant default hero URLs are presented on the homepage",
        example = "slideshow",
        allowableValues = { "slideshow", "random", "single" }
    )
    @Size(max = 32)
    private String defaultHeroDisplayMode;

    @Schema(description = "When true, tenant default slides may appear alongside event hero images", example = "true")
    private Boolean defaultHeroIncludeWithEvents;

    @Schema(description = "Maximum number of tenant default hero slides to show on the homepage; null means no limit", example = "5")
    private Integer defaultHeroMaxDisplayCount;

    @Schema(description = "When true, show event hero images on the homepage hero section", example = "true")
    private Boolean displayEventHeroImages;

    @Schema(description = "When true, render Google AdSense regions configured for this tenant", example = "false")
    private Boolean enableGoogleAdsense;

    @Schema(
        description = "AdSense publisher ID (ca-pub-...); required when enableGoogleAdsense is true",
        example = "ca-pub-1234567890123456"
    )
    @Size(max = 32)
    private String googleAdsensePublisherId;

    @Schema(
        description = "JSON map of layout region id to AdSense ad slot id",
        example = "{\"sidebar\":\"1234567890\",\"between_sections\":\"0987654321\",\"footer_strip\":\"1122334455\"}"
    )
    private String googleAdsensePlacementsJson;

    private Boolean showPublicProfileHeroSection;

    private Boolean showProfileWritingsSection;

    private Boolean showProfileAchievementsSection;

    private Boolean showProfileAffiliationsSection;

    private Boolean showProfileMediaDownloadsSection;

    private Boolean showProfileContactSection;

    private Boolean showProfileProjectsSection;

    /** Header menu visibility (null = app default: ON for legacy items) */
    private Boolean showHeaderHome;

    private Boolean showHeaderAbout;

    private Boolean showHeaderEvents;

    private Boolean showHeaderFeatures;

    private Boolean showHeaderCalendar;

    private Boolean showHeaderGallery;

    private Boolean showHeaderContact;

    /** Header menu: News / Perspectives (null = OFF) */
    private Boolean showHeaderNews;

    /** Header menu: Downloads (null = OFF) */
    private Boolean showHeaderDownloads;

    /** Header menu: Links (null = OFF) */
    private Boolean showHeaderLinks;

    private Boolean enableGasStationModule;

    @Size(max = 1024)
    private String gasAiEngineBaseUrl;

    @Size(max = 512)
    private String gasAiEngineApiKeyRef;

    @Size(max = 1048)
    private String gasAiEngineWebhookToken;

    private Integer gasDailyBriefHourLocal;

    @Schema(
        description = "Parsed default hero image URLs (GET only; computed from defaultHeroImageUrlsJson)",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<String> defaultHeroImageUrls;

    @NotNull
    private ZonedDateTime createdAt;

    @NotNull
    private ZonedDateTime updatedAt;

    private TenantOrganizationDTO tenantOrganization;

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

    public Boolean getAllowUserRegistration() {
        return allowUserRegistration;
    }

    public void setAllowUserRegistration(Boolean allowUserRegistration) {
        this.allowUserRegistration = allowUserRegistration;
    }

    public Boolean getRequireAdminApproval() {
        return requireAdminApproval;
    }

    public void setRequireAdminApproval(Boolean requireAdminApproval) {
        this.requireAdminApproval = requireAdminApproval;
    }

    public Boolean getEnableWhatsappIntegration() {
        return enableWhatsappIntegration;
    }

    public void setEnableWhatsappIntegration(Boolean enableWhatsappIntegration) {
        this.enableWhatsappIntegration = enableWhatsappIntegration;
    }

    public Boolean getEnableEmailMarketing() {
        return enableEmailMarketing;
    }

    public void setEnableEmailMarketing(Boolean enableEmailMarketing) {
        this.enableEmailMarketing = enableEmailMarketing;
    }

    public Boolean getIsMembershipSubscriptionEnabled() {
        return isMembershipSubscriptionEnabled;
    }

    public void setIsMembershipSubscriptionEnabled(Boolean isMembershipSubscriptionEnabled) {
        this.isMembershipSubscriptionEnabled = isMembershipSubscriptionEnabled;
    }

    public String getWhatsappApiKey() {
        return whatsappApiKey;
    }

    public void setWhatsappApiKey(String whatsappApiKey) {
        this.whatsappApiKey = whatsappApiKey;
    }

    public String getEmailProviderConfig() {
        return emailProviderConfig;
    }

    public void setEmailProviderConfig(String emailProviderConfig) {
        this.emailProviderConfig = emailProviderConfig;
    }

    public Integer getMaxEventsPerMonth() {
        return maxEventsPerMonth;
    }

    public void setMaxEventsPerMonth(Integer maxEventsPerMonth) {
        this.maxEventsPerMonth = maxEventsPerMonth;
    }

    public Integer getMaxAttendeesPerEvent() {
        return maxAttendeesPerEvent;
    }

    public void setMaxAttendeesPerEvent(Integer maxAttendeesPerEvent) {
        this.maxAttendeesPerEvent = maxAttendeesPerEvent;
    }

    public Boolean getEnableGuestRegistration() {
        return enableGuestRegistration;
    }

    public void setEnableGuestRegistration(Boolean enableGuestRegistration) {
        this.enableGuestRegistration = enableGuestRegistration;
    }

    public Integer getMaxGuestsPerAttendee() {
        return maxGuestsPerAttendee;
    }

    public void setMaxGuestsPerAttendee(Integer maxGuestsPerAttendee) {
        this.maxGuestsPerAttendee = maxGuestsPerAttendee;
    }

    public Integer getDefaultEventCapacity() {
        return defaultEventCapacity;
    }

    public void setDefaultEventCapacity(Integer defaultEventCapacity) {
        this.defaultEventCapacity = defaultEventCapacity;
    }

    public BigDecimal getPlatformFeePercentage() {
        return platformFeePercentage;
    }

    public void setPlatformFeePercentage(BigDecimal platformFeePercentage) {
        this.platformFeePercentage = platformFeePercentage;
    }

    public String getCustomCss() {
        return customCss;
    }

    public void setCustomCss(String customCss) {
        this.customCss = customCss;
    }

    public String getCustomJs() {
        return customJs;
    }

    public void setCustomJs(String customJs) {
        this.customJs = customJs;
    }

    public Boolean getShowEventsSectionInHomePage() {
        return showEventsSectionInHomePage;
    }

    public void setShowEventsSectionInHomePage(Boolean showEventsSectionInHomePage) {
        this.showEventsSectionInHomePage = showEventsSectionInHomePage;
    }

    public Boolean getShowTeamMembersSectionInHomePage() {
        return showTeamMembersSectionInHomePage;
    }

    public void setShowTeamMembersSectionInHomePage(Boolean showTeamMembersSectionInHomePage) {
        this.showTeamMembersSectionInHomePage = showTeamMembersSectionInHomePage;
    }

    public Boolean getShowSponsorsSectionInHomePage() {
        return showSponsorsSectionInHomePage;
    }

    public void setShowSponsorsSectionInHomePage(Boolean showSponsorsSectionInHomePage) {
        this.showSponsorsSectionInHomePage = showSponsorsSectionInHomePage;
    }

    public String getTwilioAccountSid() {
        return twilioAccountSid;
    }

    public void setTwilioAccountSid(String twilioAccountSid) {
        this.twilioAccountSid = twilioAccountSid;
    }

    public String getTwilioAuthToken() {
        return twilioAuthToken;
    }

    public void setTwilioAuthToken(String twilioAuthToken) {
        this.twilioAuthToken = twilioAuthToken;
    }

    public String getTwilioWhatsappFrom() {
        return twilioWhatsappFrom;
    }

    public void setTwilioWhatsappFrom(String twilioWhatsappFrom) {
        this.twilioWhatsappFrom = twilioWhatsappFrom;
    }

    public String getWhatsappWebhookUrl() {
        return whatsappWebhookUrl;
    }

    public void setWhatsappWebhookUrl(String whatsappWebhookUrl) {
        this.whatsappWebhookUrl = whatsappWebhookUrl;
    }

    public String getWhatsappWebhookToken() {
        return whatsappWebhookToken;
    }

    public void setWhatsappWebhookToken(String whatsappWebhookToken) {
        this.whatsappWebhookToken = whatsappWebhookToken;
    }

    public String getEmailFooterHtmlUrl() {
        return emailFooterHtmlUrl;
    }

    public void setEmailFooterHtmlUrl(String emailFooterHtmlUrl) {
        this.emailFooterHtmlUrl = emailFooterHtmlUrl;
    }

    public String getEmailHeaderImageUrl() {
        return emailHeaderImageUrl;
    }

    public void setEmailHeaderImageUrl(String emailHeaderImageUrl) {
        this.emailHeaderImageUrl = emailHeaderImageUrl;
    }

    public String getLogoImageUrl() {
        return logoImageUrl;
    }

    public void setLogoImageUrl(String logoImageUrl) {
        this.logoImageUrl = logoImageUrl;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public String getInstagramUrl() {
        return instagramUrl;
    }

    public void setInstagramUrl(String instagramUrl) {
        this.instagramUrl = instagramUrl;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public String getTiktokUrl() {
        return tiktokUrl;
    }

    public void setTiktokUrl(String tiktokUrl) {
        this.tiktokUrl = tiktokUrl;
    }

    public Long getHomepageCacheVersion() {
        return homepageCacheVersion;
    }

    public void setHomepageCacheVersion(Long homepageCacheVersion) {
        this.homepageCacheVersion = homepageCacheVersion;
    }

    public String getDefaultHeroImageUrlsJson() {
        return defaultHeroImageUrlsJson;
    }

    public void setDefaultHeroImageUrlsJson(String defaultHeroImageUrlsJson) {
        this.defaultHeroImageUrlsJson = defaultHeroImageUrlsJson;
    }

    public String getDefaultHeroDisplayMode() {
        return defaultHeroDisplayMode;
    }

    public void setDefaultHeroDisplayMode(String defaultHeroDisplayMode) {
        this.defaultHeroDisplayMode = defaultHeroDisplayMode;
    }

    public Boolean getDefaultHeroIncludeWithEvents() {
        return defaultHeroIncludeWithEvents;
    }

    public void setDefaultHeroIncludeWithEvents(Boolean defaultHeroIncludeWithEvents) {
        this.defaultHeroIncludeWithEvents = defaultHeroIncludeWithEvents;
    }

    public Integer getDefaultHeroMaxDisplayCount() {
        return defaultHeroMaxDisplayCount;
    }

    public void setDefaultHeroMaxDisplayCount(Integer defaultHeroMaxDisplayCount) {
        this.defaultHeroMaxDisplayCount = defaultHeroMaxDisplayCount;
    }

    public Boolean getDisplayEventHeroImages() {
        return displayEventHeroImages;
    }

    public void setDisplayEventHeroImages(Boolean displayEventHeroImages) {
        this.displayEventHeroImages = displayEventHeroImages;
    }

    public Boolean getEnableGoogleAdsense() {
        return enableGoogleAdsense;
    }

    public void setEnableGoogleAdsense(Boolean enableGoogleAdsense) {
        this.enableGoogleAdsense = enableGoogleAdsense;
    }

    public String getGoogleAdsensePublisherId() {
        return googleAdsensePublisherId;
    }

    public void setGoogleAdsensePublisherId(String googleAdsensePublisherId) {
        this.googleAdsensePublisherId = googleAdsensePublisherId;
    }

    public String getGoogleAdsensePlacementsJson() {
        return googleAdsensePlacementsJson;
    }

    public void setGoogleAdsensePlacementsJson(String googleAdsensePlacementsJson) {
        this.googleAdsensePlacementsJson = googleAdsensePlacementsJson;
    }

    public Boolean getShowPublicProfileHeroSection() {
        return showPublicProfileHeroSection;
    }

    public void setShowPublicProfileHeroSection(Boolean showPublicProfileHeroSection) {
        this.showPublicProfileHeroSection = showPublicProfileHeroSection;
    }

    public Boolean getShowProfileWritingsSection() {
        return showProfileWritingsSection;
    }

    public void setShowProfileWritingsSection(Boolean showProfileWritingsSection) {
        this.showProfileWritingsSection = showProfileWritingsSection;
    }

    public Boolean getShowProfileAchievementsSection() {
        return showProfileAchievementsSection;
    }

    public void setShowProfileAchievementsSection(Boolean showProfileAchievementsSection) {
        this.showProfileAchievementsSection = showProfileAchievementsSection;
    }

    public Boolean getShowProfileAffiliationsSection() {
        return showProfileAffiliationsSection;
    }

    public void setShowProfileAffiliationsSection(Boolean showProfileAffiliationsSection) {
        this.showProfileAffiliationsSection = showProfileAffiliationsSection;
    }

    public Boolean getShowProfileMediaDownloadsSection() {
        return showProfileMediaDownloadsSection;
    }

    public void setShowProfileMediaDownloadsSection(Boolean showProfileMediaDownloadsSection) {
        this.showProfileMediaDownloadsSection = showProfileMediaDownloadsSection;
    }

    public Boolean getShowProfileContactSection() {
        return showProfileContactSection;
    }

    public void setShowProfileContactSection(Boolean showProfileContactSection) {
        this.showProfileContactSection = showProfileContactSection;
    }

    public Boolean getShowProfileProjectsSection() {
        return showProfileProjectsSection;
    }

    public void setShowProfileProjectsSection(Boolean showProfileProjectsSection) {
        this.showProfileProjectsSection = showProfileProjectsSection;
    }

    public Boolean getShowHeaderHome() {
        return showHeaderHome;
    }

    public void setShowHeaderHome(Boolean showHeaderHome) {
        this.showHeaderHome = showHeaderHome;
    }

    public Boolean getShowHeaderAbout() {
        return showHeaderAbout;
    }

    public void setShowHeaderAbout(Boolean showHeaderAbout) {
        this.showHeaderAbout = showHeaderAbout;
    }

    public Boolean getShowHeaderEvents() {
        return showHeaderEvents;
    }

    public void setShowHeaderEvents(Boolean showHeaderEvents) {
        this.showHeaderEvents = showHeaderEvents;
    }

    public Boolean getShowHeaderFeatures() {
        return showHeaderFeatures;
    }

    public void setShowHeaderFeatures(Boolean showHeaderFeatures) {
        this.showHeaderFeatures = showHeaderFeatures;
    }

    public Boolean getShowHeaderCalendar() {
        return showHeaderCalendar;
    }

    public void setShowHeaderCalendar(Boolean showHeaderCalendar) {
        this.showHeaderCalendar = showHeaderCalendar;
    }

    public Boolean getShowHeaderGallery() {
        return showHeaderGallery;
    }

    public void setShowHeaderGallery(Boolean showHeaderGallery) {
        this.showHeaderGallery = showHeaderGallery;
    }

    public Boolean getShowHeaderContact() {
        return showHeaderContact;
    }

    public void setShowHeaderContact(Boolean showHeaderContact) {
        this.showHeaderContact = showHeaderContact;
    }

    public Boolean getShowHeaderNews() {
        return showHeaderNews;
    }

    public void setShowHeaderNews(Boolean showHeaderNews) {
        this.showHeaderNews = showHeaderNews;
    }

    public Boolean getShowHeaderDownloads() {
        return showHeaderDownloads;
    }

    public void setShowHeaderDownloads(Boolean showHeaderDownloads) {
        this.showHeaderDownloads = showHeaderDownloads;
    }

    public Boolean getShowHeaderLinks() {
        return showHeaderLinks;
    }

    public void setShowHeaderLinks(Boolean showHeaderLinks) {
        this.showHeaderLinks = showHeaderLinks;
    }

    public Boolean getEnableGasStationModule() {
        return enableGasStationModule;
    }

    public void setEnableGasStationModule(Boolean enableGasStationModule) {
        this.enableGasStationModule = enableGasStationModule;
    }

    public String getGasAiEngineBaseUrl() {
        return gasAiEngineBaseUrl;
    }

    public void setGasAiEngineBaseUrl(String gasAiEngineBaseUrl) {
        this.gasAiEngineBaseUrl = gasAiEngineBaseUrl;
    }

    public String getGasAiEngineApiKeyRef() {
        return gasAiEngineApiKeyRef;
    }

    public void setGasAiEngineApiKeyRef(String gasAiEngineApiKeyRef) {
        this.gasAiEngineApiKeyRef = gasAiEngineApiKeyRef;
    }

    public String getGasAiEngineWebhookToken() {
        return gasAiEngineWebhookToken;
    }

    public void setGasAiEngineWebhookToken(String gasAiEngineWebhookToken) {
        this.gasAiEngineWebhookToken = gasAiEngineWebhookToken;
    }

    public Integer getGasDailyBriefHourLocal() {
        return gasDailyBriefHourLocal;
    }

    public void setGasDailyBriefHourLocal(Integer gasDailyBriefHourLocal) {
        this.gasDailyBriefHourLocal = gasDailyBriefHourLocal;
    }

    public List<String> getDefaultHeroImageUrls() {
        return defaultHeroImageUrls;
    }

    public void setDefaultHeroImageUrls(List<String> defaultHeroImageUrls) {
        this.defaultHeroImageUrls = defaultHeroImageUrls;
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

    public TenantOrganizationDTO getTenantOrganization() {
        return tenantOrganization;
    }

    public void setTenantOrganization(TenantOrganizationDTO tenantOrganization) {
        this.tenantOrganization = tenantOrganization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TenantSettingsDTO)) {
            return false;
        }

        TenantSettingsDTO tenantSettingsDTO = (TenantSettingsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tenantSettingsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TenantSettingsDTO{" +
                "id=" + getId() +
                ", tenantId='" + getTenantId() + "'" +
                ", allowUserRegistration='" + getAllowUserRegistration() + "'" +
                ", requireAdminApproval='" + getRequireAdminApproval() + "'" +
                ", enableWhatsappIntegration='" + getEnableWhatsappIntegration() + "'" +
                ", enableEmailMarketing='" + getEnableEmailMarketing() + "'" +
                ", isMembershipSubscriptionEnabled='" + getIsMembershipSubscriptionEnabled() + "'" +
                ", whatsappApiKey='" + getWhatsappApiKey() + "'" +
                ", emailProviderConfig='" + getEmailProviderConfig() + "'" +
                ", maxEventsPerMonth=" + getMaxEventsPerMonth() +
                ", maxAttendeesPerEvent=" + getMaxAttendeesPerEvent() +
                ", enableGuestRegistration='" + getEnableGuestRegistration() + "'" +
                ", maxGuestsPerAttendee=" + getMaxGuestsPerAttendee() +
                ", defaultEventCapacity=" + getDefaultEventCapacity() +
                ", platformFeePercentage=" + getPlatformFeePercentage() +
                ", customCss='" + getCustomCss() + "'" +
                ", customJs='" + getCustomJs() + "'" +
                ", showEventsSectionInHomePage='" + getShowEventsSectionInHomePage() + "'" +
                ", showTeamMembersSectionInHomePage='" + getShowTeamMembersSectionInHomePage() + "'" +
                ", showSponsorsSectionInHomePage='" + getShowSponsorsSectionInHomePage() + "'" +
                ", twilioAccountSid='" + getTwilioAccountSid() + "'" +
                ", twilioAuthToken='" + getTwilioAuthToken() + "'" +
                ", twilioWhatsappFrom='" + getTwilioWhatsappFrom() + "'" +
                ", whatsappWebhookUrl='" + getWhatsappWebhookUrl() + "'" +
                ", whatsappWebhookToken='" + getWhatsappWebhookToken() + "'" +
                ", emailFooterHtmlUrl='" + getEmailFooterHtmlUrl() + "'" +
                ", emailHeaderImageUrl='" + getEmailHeaderImageUrl() + "'" +
                ", logoImageUrl='" + getLogoImageUrl() + "'" +
                ", addressLine1='" + getAddressLine1() + "'" +
                ", addressLine2='" + getAddressLine2() + "'" +
                ", description='" + getDescription() + "'" +
                ", city='" + getCity() + "'" +
                ", phoneNumber='" + getPhoneNumber() + "'" +
                ", zipCode='" + getZipCode() + "'" +
                ", country='" + getCountry() + "'" +
                ", stateProvince='" + getStateProvince() + "'" +
                ", email='" + getEmail() + "'" +
                ", facebookUrl='" + getFacebookUrl() + "'" +
                ", instagramUrl='" + getInstagramUrl() + "'" +
                ", twitterUrl='" + getTwitterUrl() + "'" +
                ", linkedinUrl='" + getLinkedinUrl() + "'" +
                ", youtubeUrl='" + getYoutubeUrl() + "'" +
                ", tiktokUrl='" + getTiktokUrl() + "'" +
                ", homepageCacheVersion=" + getHomepageCacheVersion() +
                ", defaultHeroImageUrlsJson='" + getDefaultHeroImageUrlsJson() + "'" +
                ", defaultHeroDisplayMode='" + getDefaultHeroDisplayMode() + "'" +
                ", defaultHeroIncludeWithEvents='" + getDefaultHeroIncludeWithEvents() + "'" +
                ", defaultHeroMaxDisplayCount=" + getDefaultHeroMaxDisplayCount() +
                ", displayEventHeroImages='" + getDisplayEventHeroImages() + "'" +
                ", enableGoogleAdsense='" + getEnableGoogleAdsense() + "'" +
                ", googleAdsensePublisherId='" + getGoogleAdsensePublisherId() + "'" +
                ", googleAdsensePlacementsJson='" + getGoogleAdsensePlacementsJson() + "'" +
                ", showHeaderHome='" + getShowHeaderHome() + "'" +
                ", showHeaderAbout='" + getShowHeaderAbout() + "'" +
                ", showHeaderEvents='" + getShowHeaderEvents() + "'" +
                ", showHeaderFeatures='" + getShowHeaderFeatures() + "'" +
                ", showHeaderCalendar='" + getShowHeaderCalendar() + "'" +
                ", showHeaderGallery='" + getShowHeaderGallery() + "'" +
                ", showHeaderContact='" + getShowHeaderContact() + "'" +
                ", showHeaderNews='" + getShowHeaderNews() + "'" +
                ", showHeaderDownloads='" + getShowHeaderDownloads() + "'" +
                ", showHeaderLinks='" + getShowHeaderLinks() + "'" +
                ", defaultHeroImageUrls=" + getDefaultHeroImageUrls() +
                ", createdAt='" + getCreatedAt() + "'" +
                ", updatedAt='" + getUpdatedAt() + "'" +
                ", tenantOrganization=" + getTenantOrganization() +
                "}";
    }
}
