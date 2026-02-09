package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.TenantSettings} entity.
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
    private String addressLine1;

    @Size(max = 255)
    private String addressLine2;

    @Size(max = 50)
    private String phoneNumber;

    @Size(max = 20)
    private String zipCode;

    @Size(max = 100)
    private String country;

    @Size(max = 100)
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
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
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
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
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
                ", createdAt='" + getCreatedAt() + "'" +
                ", updatedAt='" + getUpdatedAt() + "'" +
                ", tenantOrganization=" + getTenantOrganization() +
                "}";
    }
}
