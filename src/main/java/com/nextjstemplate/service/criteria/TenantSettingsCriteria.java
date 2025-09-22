package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.TenantSettings}
 * entity. This class is used
 * in {@link com.nextjstemplate.web.rest.TenantSettingsResource} to receive all
 * the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tenant-settings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific
 * {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TenantSettingsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private BooleanFilter allowUserRegistration;

    private BooleanFilter requireAdminApproval;

    private BooleanFilter enableWhatsappIntegration;

    private BooleanFilter enableEmailMarketing;

    private StringFilter whatsappApiKey;

    private StringFilter emailProviderConfig;

    private IntegerFilter maxEventsPerMonth;

    private IntegerFilter maxAttendeesPerEvent;

    private BooleanFilter enableGuestRegistration;

    private IntegerFilter maxGuestsPerAttendee;

    private IntegerFilter defaultEventCapacity;

    private BigDecimalFilter platformFeePercentage;

    private StringFilter customCss;

    private StringFilter customJs;

    private BooleanFilter showEventsSectionInHomePage;

    private BooleanFilter showTeamMembersSectionInHomePage;

    private BooleanFilter showSponsorsSectionInHomePage;

    private StringFilter twilioAccountSid;

    private StringFilter twilioAuthToken;

    private StringFilter twilioWhatsappFrom;

    private StringFilter whatsappWebhookUrl;

    private StringFilter whatsappWebhookToken;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private LongFilter tenantOrganizationId;

    private Boolean distinct;

    public TenantSettingsCriteria() {
    }

    public TenantSettingsCriteria(TenantSettingsCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.tenantId = other.optionalTenantId().map(StringFilter::copy).orElse(null);
        this.allowUserRegistration = other.optionalAllowUserRegistration().map(BooleanFilter::copy).orElse(null);
        this.requireAdminApproval = other.optionalRequireAdminApproval().map(BooleanFilter::copy).orElse(null);
        this.enableWhatsappIntegration = other.optionalEnableWhatsappIntegration().map(BooleanFilter::copy)
                .orElse(null);
        this.enableEmailMarketing = other.optionalEnableEmailMarketing().map(BooleanFilter::copy).orElse(null);
        this.whatsappApiKey = other.optionalWhatsappApiKey().map(StringFilter::copy).orElse(null);
        this.emailProviderConfig = other.optionalEmailProviderConfig().map(StringFilter::copy).orElse(null);
        this.maxEventsPerMonth = other.optionalMaxEventsPerMonth().map(IntegerFilter::copy).orElse(null);
        this.maxAttendeesPerEvent = other.optionalMaxAttendeesPerEvent().map(IntegerFilter::copy).orElse(null);
        this.enableGuestRegistration = other.optionalEnableGuestRegistration().map(BooleanFilter::copy).orElse(null);
        this.maxGuestsPerAttendee = other.optionalMaxGuestsPerAttendee().map(IntegerFilter::copy).orElse(null);
        this.defaultEventCapacity = other.optionalDefaultEventCapacity().map(IntegerFilter::copy).orElse(null);
        this.platformFeePercentage = other.optionalPlatformFeePercentage().map(BigDecimalFilter::copy).orElse(null);
        this.customCss = other.optionalCustomCss().map(StringFilter::copy).orElse(null);
        this.customJs = other.optionalCustomJs().map(StringFilter::copy).orElse(null);
        this.showEventsSectionInHomePage = other.optionalShowEventsSectionInHomePage().map(BooleanFilter::copy)
                .orElse(null);
        this.showTeamMembersSectionInHomePage = other.optionalShowTeamMembersSectionInHomePage()
                .map(BooleanFilter::copy).orElse(null);
        this.showSponsorsSectionInHomePage = other.optionalShowSponsorsSectionInHomePage().map(BooleanFilter::copy)
                .orElse(null);
        this.twilioAccountSid = other.optionalTwilioAccountSid().map(StringFilter::copy).orElse(null);
        this.twilioAuthToken = other.optionalTwilioAuthToken().map(StringFilter::copy).orElse(null);
        this.twilioWhatsappFrom = other.optionalTwilioWhatsappFrom().map(StringFilter::copy).orElse(null);
        this.whatsappWebhookUrl = other.optionalWhatsappWebhookUrl().map(StringFilter::copy).orElse(null);
        this.whatsappWebhookToken = other.optionalWhatsappWebhookToken().map(StringFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.tenantOrganizationId = other.optionalTenantOrganizationId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TenantSettingsCriteria copy() {
        return new TenantSettingsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTenantId() {
        return tenantId;
    }

    public Optional<StringFilter> optionalTenantId() {
        return Optional.ofNullable(tenantId);
    }

    public StringFilter tenantId() {
        if (tenantId == null) {
            setTenantId(new StringFilter());
        }
        return tenantId;
    }

    public void setTenantId(StringFilter tenantId) {
        this.tenantId = tenantId;
    }

    public BooleanFilter getAllowUserRegistration() {
        return allowUserRegistration;
    }

    public Optional<BooleanFilter> optionalAllowUserRegistration() {
        return Optional.ofNullable(allowUserRegistration);
    }

    public BooleanFilter allowUserRegistration() {
        if (allowUserRegistration == null) {
            setAllowUserRegistration(new BooleanFilter());
        }
        return allowUserRegistration;
    }

    public void setAllowUserRegistration(BooleanFilter allowUserRegistration) {
        this.allowUserRegistration = allowUserRegistration;
    }

    public BooleanFilter getRequireAdminApproval() {
        return requireAdminApproval;
    }

    public Optional<BooleanFilter> optionalRequireAdminApproval() {
        return Optional.ofNullable(requireAdminApproval);
    }

    public BooleanFilter requireAdminApproval() {
        if (requireAdminApproval == null) {
            setRequireAdminApproval(new BooleanFilter());
        }
        return requireAdminApproval;
    }

    public void setRequireAdminApproval(BooleanFilter requireAdminApproval) {
        this.requireAdminApproval = requireAdminApproval;
    }

    public BooleanFilter getEnableWhatsappIntegration() {
        return enableWhatsappIntegration;
    }

    public Optional<BooleanFilter> optionalEnableWhatsappIntegration() {
        return Optional.ofNullable(enableWhatsappIntegration);
    }

    public BooleanFilter enableWhatsappIntegration() {
        if (enableWhatsappIntegration == null) {
            setEnableWhatsappIntegration(new BooleanFilter());
        }
        return enableWhatsappIntegration;
    }

    public void setEnableWhatsappIntegration(BooleanFilter enableWhatsappIntegration) {
        this.enableWhatsappIntegration = enableWhatsappIntegration;
    }

    public BooleanFilter getEnableEmailMarketing() {
        return enableEmailMarketing;
    }

    public Optional<BooleanFilter> optionalEnableEmailMarketing() {
        return Optional.ofNullable(enableEmailMarketing);
    }

    public BooleanFilter enableEmailMarketing() {
        if (enableEmailMarketing == null) {
            setEnableEmailMarketing(new BooleanFilter());
        }
        return enableEmailMarketing;
    }

    public void setEnableEmailMarketing(BooleanFilter enableEmailMarketing) {
        this.enableEmailMarketing = enableEmailMarketing;
    }

    public StringFilter getWhatsappApiKey() {
        return whatsappApiKey;
    }

    public Optional<StringFilter> optionalWhatsappApiKey() {
        return Optional.ofNullable(whatsappApiKey);
    }

    public StringFilter whatsappApiKey() {
        if (whatsappApiKey == null) {
            setWhatsappApiKey(new StringFilter());
        }
        return whatsappApiKey;
    }

    public void setWhatsappApiKey(StringFilter whatsappApiKey) {
        this.whatsappApiKey = whatsappApiKey;
    }

    public StringFilter getEmailProviderConfig() {
        return emailProviderConfig;
    }

    public Optional<StringFilter> optionalEmailProviderConfig() {
        return Optional.ofNullable(emailProviderConfig);
    }

    public StringFilter emailProviderConfig() {
        if (emailProviderConfig == null) {
            setEmailProviderConfig(new StringFilter());
        }
        return emailProviderConfig;
    }

    public void setEmailProviderConfig(StringFilter emailProviderConfig) {
        this.emailProviderConfig = emailProviderConfig;
    }

    public IntegerFilter getMaxEventsPerMonth() {
        return maxEventsPerMonth;
    }

    public Optional<IntegerFilter> optionalMaxEventsPerMonth() {
        return Optional.ofNullable(maxEventsPerMonth);
    }

    public IntegerFilter maxEventsPerMonth() {
        if (maxEventsPerMonth == null) {
            setMaxEventsPerMonth(new IntegerFilter());
        }
        return maxEventsPerMonth;
    }

    public void setMaxEventsPerMonth(IntegerFilter maxEventsPerMonth) {
        this.maxEventsPerMonth = maxEventsPerMonth;
    }

    public IntegerFilter getMaxAttendeesPerEvent() {
        return maxAttendeesPerEvent;
    }

    public Optional<IntegerFilter> optionalMaxAttendeesPerEvent() {
        return Optional.ofNullable(maxAttendeesPerEvent);
    }

    public IntegerFilter maxAttendeesPerEvent() {
        if (maxAttendeesPerEvent == null) {
            setMaxAttendeesPerEvent(new IntegerFilter());
        }
        return maxAttendeesPerEvent;
    }

    public void setMaxAttendeesPerEvent(IntegerFilter maxAttendeesPerEvent) {
        this.maxAttendeesPerEvent = maxAttendeesPerEvent;
    }

    public BooleanFilter getEnableGuestRegistration() {
        return enableGuestRegistration;
    }

    public Optional<BooleanFilter> optionalEnableGuestRegistration() {
        return Optional.ofNullable(enableGuestRegistration);
    }

    public BooleanFilter enableGuestRegistration() {
        if (enableGuestRegistration == null) {
            setEnableGuestRegistration(new BooleanFilter());
        }
        return enableGuestRegistration;
    }

    public void setEnableGuestRegistration(BooleanFilter enableGuestRegistration) {
        this.enableGuestRegistration = enableGuestRegistration;
    }

    public IntegerFilter getMaxGuestsPerAttendee() {
        return maxGuestsPerAttendee;
    }

    public Optional<IntegerFilter> optionalMaxGuestsPerAttendee() {
        return Optional.ofNullable(maxGuestsPerAttendee);
    }

    public IntegerFilter maxGuestsPerAttendee() {
        if (maxGuestsPerAttendee == null) {
            setMaxGuestsPerAttendee(new IntegerFilter());
        }
        return maxGuestsPerAttendee;
    }

    public void setMaxGuestsPerAttendee(IntegerFilter maxGuestsPerAttendee) {
        this.maxGuestsPerAttendee = maxGuestsPerAttendee;
    }

    public IntegerFilter getDefaultEventCapacity() {
        return defaultEventCapacity;
    }

    public Optional<IntegerFilter> optionalDefaultEventCapacity() {
        return Optional.ofNullable(defaultEventCapacity);
    }

    public IntegerFilter defaultEventCapacity() {
        if (defaultEventCapacity == null) {
            setDefaultEventCapacity(new IntegerFilter());
        }
        return defaultEventCapacity;
    }

    public void setDefaultEventCapacity(IntegerFilter defaultEventCapacity) {
        this.defaultEventCapacity = defaultEventCapacity;
    }

    public BigDecimalFilter getPlatformFeePercentage() {
        return platformFeePercentage;
    }

    public Optional<BigDecimalFilter> optionalPlatformFeePercentage() {
        return Optional.ofNullable(platformFeePercentage);
    }

    public BigDecimalFilter platformFeePercentage() {
        if (platformFeePercentage == null) {
            setPlatformFeePercentage(new BigDecimalFilter());
        }
        return platformFeePercentage;
    }

    public void setPlatformFeePercentage(BigDecimalFilter platformFeePercentage) {
        this.platformFeePercentage = platformFeePercentage;
    }

    public StringFilter getCustomCss() {
        return customCss;
    }

    public Optional<StringFilter> optionalCustomCss() {
        return Optional.ofNullable(customCss);
    }

    public StringFilter customCss() {
        if (customCss == null) {
            setCustomCss(new StringFilter());
        }
        return customCss;
    }

    public void setCustomCss(StringFilter customCss) {
        this.customCss = customCss;
    }

    public StringFilter getCustomJs() {
        return customJs;
    }

    public Optional<StringFilter> optionalCustomJs() {
        return Optional.ofNullable(customJs);
    }

    public StringFilter customJs() {
        if (customJs == null) {
            setCustomJs(new StringFilter());
        }
        return customJs;
    }

    public void setCustomJs(StringFilter customJs) {
        this.customJs = customJs;
    }

    public BooleanFilter getShowEventsSectionInHomePage() {
        return showEventsSectionInHomePage;
    }

    public Optional<BooleanFilter> optionalShowEventsSectionInHomePage() {
        return Optional.ofNullable(showEventsSectionInHomePage);
    }

    public BooleanFilter showEventsSectionInHomePage() {
        if (showEventsSectionInHomePage == null) {
            setShowEventsSectionInHomePage(new BooleanFilter());
        }
        return showEventsSectionInHomePage;
    }

    public void setShowEventsSectionInHomePage(BooleanFilter showEventsSectionInHomePage) {
        this.showEventsSectionInHomePage = showEventsSectionInHomePage;
    }

    public BooleanFilter getShowTeamMembersSectionInHomePage() {
        return showTeamMembersSectionInHomePage;
    }

    public Optional<BooleanFilter> optionalShowTeamMembersSectionInHomePage() {
        return Optional.ofNullable(showTeamMembersSectionInHomePage);
    }

    public BooleanFilter showTeamMembersSectionInHomePage() {
        if (showTeamMembersSectionInHomePage == null) {
            setShowTeamMembersSectionInHomePage(new BooleanFilter());
        }
        return showTeamMembersSectionInHomePage;
    }

    public void setShowTeamMembersSectionInHomePage(BooleanFilter showTeamMembersSectionInHomePage) {
        this.showTeamMembersSectionInHomePage = showTeamMembersSectionInHomePage;
    }

    public BooleanFilter getShowSponsorsSectionInHomePage() {
        return showSponsorsSectionInHomePage;
    }

    public Optional<BooleanFilter> optionalShowSponsorsSectionInHomePage() {
        return Optional.ofNullable(showSponsorsSectionInHomePage);
    }

    public BooleanFilter showSponsorsSectionInHomePage() {
        if (showSponsorsSectionInHomePage == null) {
            setShowSponsorsSectionInHomePage(new BooleanFilter());
        }
        return showSponsorsSectionInHomePage;
    }

    public void setShowSponsorsSectionInHomePage(BooleanFilter showSponsorsSectionInHomePage) {
        this.showSponsorsSectionInHomePage = showSponsorsSectionInHomePage;
    }

    public StringFilter getTwilioAccountSid() {
        return twilioAccountSid;
    }

    public Optional<StringFilter> optionalTwilioAccountSid() {
        return Optional.ofNullable(twilioAccountSid);
    }

    public StringFilter twilioAccountSid() {
        if (twilioAccountSid == null) {
            setTwilioAccountSid(new StringFilter());
        }
        return twilioAccountSid;
    }

    public void setTwilioAccountSid(StringFilter twilioAccountSid) {
        this.twilioAccountSid = twilioAccountSid;
    }

    public StringFilter getTwilioAuthToken() {
        return twilioAuthToken;
    }

    public Optional<StringFilter> optionalTwilioAuthToken() {
        return Optional.ofNullable(twilioAuthToken);
    }

    public StringFilter twilioAuthToken() {
        if (twilioAuthToken == null) {
            setTwilioAuthToken(new StringFilter());
        }
        return twilioAuthToken;
    }

    public void setTwilioAuthToken(StringFilter twilioAuthToken) {
        this.twilioAuthToken = twilioAuthToken;
    }

    public StringFilter getTwilioWhatsappFrom() {
        return twilioWhatsappFrom;
    }

    public Optional<StringFilter> optionalTwilioWhatsappFrom() {
        return Optional.ofNullable(twilioWhatsappFrom);
    }

    public StringFilter twilioWhatsappFrom() {
        if (twilioWhatsappFrom == null) {
            setTwilioWhatsappFrom(new StringFilter());
        }
        return twilioWhatsappFrom;
    }

    public void setTwilioWhatsappFrom(StringFilter twilioWhatsappFrom) {
        this.twilioWhatsappFrom = twilioWhatsappFrom;
    }

    public StringFilter getWhatsappWebhookUrl() {
        return whatsappWebhookUrl;
    }

    public Optional<StringFilter> optionalWhatsappWebhookUrl() {
        return Optional.ofNullable(whatsappWebhookUrl);
    }

    public StringFilter whatsappWebhookUrl() {
        if (whatsappWebhookUrl == null) {
            setWhatsappWebhookUrl(new StringFilter());
        }
        return whatsappWebhookUrl;
    }

    public void setWhatsappWebhookUrl(StringFilter whatsappWebhookUrl) {
        this.whatsappWebhookUrl = whatsappWebhookUrl;
    }

    public StringFilter getWhatsappWebhookToken() {
        return whatsappWebhookToken;
    }

    public Optional<StringFilter> optionalWhatsappWebhookToken() {
        return Optional.ofNullable(whatsappWebhookToken);
    }

    public StringFilter whatsappWebhookToken() {
        if (whatsappWebhookToken == null) {
            setWhatsappWebhookToken(new StringFilter());
        }
        return whatsappWebhookToken;
    }

    public void setWhatsappWebhookToken(StringFilter whatsappWebhookToken) {
        this.whatsappWebhookToken = whatsappWebhookToken;
    }

    public ZonedDateTimeFilter getCreatedAt() {
        return createdAt;
    }

    public Optional<ZonedDateTimeFilter> optionalCreatedAt() {
        return Optional.ofNullable(createdAt);
    }

    public ZonedDateTimeFilter createdAt() {
        if (createdAt == null) {
            setCreatedAt(new ZonedDateTimeFilter());
        }
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTimeFilter createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTimeFilter getUpdatedAt() {
        return updatedAt;
    }

    public Optional<ZonedDateTimeFilter> optionalUpdatedAt() {
        return Optional.ofNullable(updatedAt);
    }

    public ZonedDateTimeFilter updatedAt() {
        if (updatedAt == null) {
            setUpdatedAt(new ZonedDateTimeFilter());
        }
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTimeFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LongFilter getTenantOrganizationId() {
        return tenantOrganizationId;
    }

    public Optional<LongFilter> optionalTenantOrganizationId() {
        return Optional.ofNullable(tenantOrganizationId);
    }

    public LongFilter tenantOrganizationId() {
        if (tenantOrganizationId == null) {
            setTenantOrganizationId(new LongFilter());
        }
        return tenantOrganizationId;
    }

    public void setTenantOrganizationId(LongFilter tenantOrganizationId) {
        this.tenantOrganizationId = tenantOrganizationId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
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
        final TenantSettingsCriteria that = (TenantSettingsCriteria) o;
        return (Objects.equals(id, that.id) &&
                Objects.equals(tenantId, that.tenantId) &&
                Objects.equals(allowUserRegistration, that.allowUserRegistration) &&
                Objects.equals(requireAdminApproval, that.requireAdminApproval) &&
                Objects.equals(enableWhatsappIntegration, that.enableWhatsappIntegration) &&
                Objects.equals(enableEmailMarketing, that.enableEmailMarketing) &&
                Objects.equals(whatsappApiKey, that.whatsappApiKey) &&
                Objects.equals(emailProviderConfig, that.emailProviderConfig) &&
                Objects.equals(maxEventsPerMonth, that.maxEventsPerMonth) &&
                Objects.equals(maxAttendeesPerEvent, that.maxAttendeesPerEvent) &&
                Objects.equals(enableGuestRegistration, that.enableGuestRegistration) &&
                Objects.equals(maxGuestsPerAttendee, that.maxGuestsPerAttendee) &&
                Objects.equals(defaultEventCapacity, that.defaultEventCapacity) &&
                Objects.equals(platformFeePercentage, that.platformFeePercentage) &&
                Objects.equals(customCss, that.customCss) &&
                Objects.equals(customJs, that.customJs) &&
                Objects.equals(showEventsSectionInHomePage, that.showEventsSectionInHomePage) &&
                Objects.equals(showTeamMembersSectionInHomePage, that.showTeamMembersSectionInHomePage) &&
                Objects.equals(showSponsorsSectionInHomePage, that.showSponsorsSectionInHomePage) &&
                Objects.equals(twilioAccountSid, that.twilioAccountSid) &&
                Objects.equals(twilioAuthToken, that.twilioAuthToken) &&
                Objects.equals(twilioWhatsappFrom, that.twilioWhatsappFrom) &&
                Objects.equals(whatsappWebhookUrl, that.whatsappWebhookUrl) &&
                Objects.equals(whatsappWebhookToken, that.whatsappWebhookToken) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt) &&
                Objects.equals(tenantOrganizationId, that.tenantOrganizationId) &&
                Objects.equals(distinct, that.distinct));
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                tenantId,
                allowUserRegistration,
                requireAdminApproval,
                enableWhatsappIntegration,
                enableEmailMarketing,
                whatsappApiKey,
                emailProviderConfig,
                maxEventsPerMonth,
                maxAttendeesPerEvent,
                enableGuestRegistration,
                maxGuestsPerAttendee,
                defaultEventCapacity,
                platformFeePercentage,
                customCss,
                customJs,
                showEventsSectionInHomePage,
                showTeamMembersSectionInHomePage,
                showSponsorsSectionInHomePage,
                twilioAccountSid,
                twilioAuthToken,
                twilioWhatsappFrom,
                whatsappWebhookUrl,
                whatsappWebhookToken,
                createdAt,
                updatedAt,
                tenantOrganizationId,
                distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TenantSettingsCriteria{" +
                optionalId().map(f -> "id=" + f + ", ").orElse("") +
                optionalTenantId().map(f -> "tenantId=" + f + ", ").orElse("") +
                optionalAllowUserRegistration().map(f -> "allowUserRegistration=" + f + ", ").orElse("") +
                optionalRequireAdminApproval().map(f -> "requireAdminApproval=" + f + ", ").orElse("") +
                optionalEnableWhatsappIntegration().map(f -> "enableWhatsappIntegration=" + f + ", ").orElse("") +
                optionalEnableEmailMarketing().map(f -> "enableEmailMarketing=" + f + ", ").orElse("") +
                optionalWhatsappApiKey().map(f -> "whatsappApiKey=" + f + ", ").orElse("") +
                optionalEmailProviderConfig().map(f -> "emailProviderConfig=" + f + ", ").orElse("") +
                optionalMaxEventsPerMonth().map(f -> "maxEventsPerMonth=" + f + ", ").orElse("") +
                optionalMaxAttendeesPerEvent().map(f -> "maxAttendeesPerEvent=" + f + ", ").orElse("") +
                optionalEnableGuestRegistration().map(f -> "enableGuestRegistration=" + f + ", ").orElse("") +
                optionalMaxGuestsPerAttendee().map(f -> "maxGuestsPerAttendee=" + f + ", ").orElse("") +
                optionalDefaultEventCapacity().map(f -> "defaultEventCapacity=" + f + ", ").orElse("") +
                optionalPlatformFeePercentage().map(f -> "platformFeePercentage=" + f + ", ").orElse("") +
                optionalCustomCss().map(f -> "customCss=" + f + ", ").orElse("") +
                optionalCustomJs().map(f -> "customJs=" + f + ", ").orElse("") +
                optionalShowEventsSectionInHomePage().map(f -> "showEventsSectionInHomePage=" + f + ", ").orElse("") +
                optionalShowTeamMembersSectionInHomePage().map(f -> "showTeamMembersSectionInHomePage=" + f + ", ")
                        .orElse("")
                +
                optionalShowSponsorsSectionInHomePage().map(f -> "showSponsorsSectionInHomePage=" + f + ", ").orElse("")
                +
                optionalTwilioAccountSid().map(f -> "twilioAccountSid=" + f + ", ").orElse("") +
                optionalTwilioAuthToken().map(f -> "twilioAuthToken=" + f + ", ").orElse("") +
                optionalTwilioWhatsappFrom().map(f -> "twilioWhatsappFrom=" + f + ", ").orElse("") +
                optionalWhatsappWebhookUrl().map(f -> "whatsappWebhookUrl=" + f + ", ").orElse("") +
                optionalWhatsappWebhookToken().map(f -> "whatsappWebhookToken=" + f + ", ").orElse("") +
                optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
                optionalUpdatedAt().map(f -> "updatedAt=" + f + ", ").orElse("") +
                optionalTenantOrganizationId().map(f -> "tenantOrganizationId=" + f + ", ").orElse("") +
                optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
                "}";
    }
}
