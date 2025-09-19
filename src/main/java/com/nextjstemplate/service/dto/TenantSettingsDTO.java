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
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", tenantOrganization=" + getTenantOrganization() +
            "}";
    }
}
