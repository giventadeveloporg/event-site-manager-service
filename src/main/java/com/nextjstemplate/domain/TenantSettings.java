package com.nextjstemplate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TenantSettings.
 */
@Entity
@Table(name = "tenant_settings")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TenantSettings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "tenant_id", length = 255, nullable = false, unique = true)
    private String tenantId;

    @Column(name = "allow_user_registration")
    private Boolean allowUserRegistration;

    @Column(name = "require_admin_approval")
    private Boolean requireAdminApproval;

    @Column(name = "enable_whatsapp_integration")
    private Boolean enableWhatsappIntegration;

    @Column(name = "enable_email_marketing")
    private Boolean enableEmailMarketing;

    @Size(max = 500)
    @Column(name = "whatsapp_api_key", length = 500)
    private String whatsappApiKey;

    @Size(max = 2048)
    @Column(name = "email_provider_config", length = 2048)
    private String emailProviderConfig;

    @Column(name = "max_events_per_month")
    private Integer maxEventsPerMonth;

    @Column(name = "max_attendees_per_event")
    private Integer maxAttendeesPerEvent;

    @Column(name = "enable_guest_registration")
    private Boolean enableGuestRegistration;

    @Column(name = "max_guests_per_attendee")
    private Integer maxGuestsPerAttendee;

    @Column(name = "default_event_capacity")
    private Integer defaultEventCapacity;

    @Column(name = "platform_fee_percentage", precision = 21, scale = 2)
    private BigDecimal platformFeePercentage;

    @Size(max = 8192)
    @Column(name = "custom_css", length = 8192)
    private String customCss;

    @Size(max = 16384)
    @Column(name = "custom_js", length = 16384)
    private String customJs;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @JsonIgnoreProperties(value = { "tenantSettings" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_organization_id", referencedColumnName = "id")
    private TenantOrganization tenantOrganization;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TenantSettings id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public TenantSettings tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Boolean getAllowUserRegistration() {
        return this.allowUserRegistration;
    }

    public TenantSettings allowUserRegistration(Boolean allowUserRegistration) {
        this.setAllowUserRegistration(allowUserRegistration);
        return this;
    }

    public void setAllowUserRegistration(Boolean allowUserRegistration) {
        this.allowUserRegistration = allowUserRegistration;
    }

    public Boolean getRequireAdminApproval() {
        return this.requireAdminApproval;
    }

    public TenantSettings requireAdminApproval(Boolean requireAdminApproval) {
        this.setRequireAdminApproval(requireAdminApproval);
        return this;
    }

    public void setRequireAdminApproval(Boolean requireAdminApproval) {
        this.requireAdminApproval = requireAdminApproval;
    }

    public Boolean getEnableWhatsappIntegration() {
        return this.enableWhatsappIntegration;
    }

    public TenantSettings enableWhatsappIntegration(Boolean enableWhatsappIntegration) {
        this.setEnableWhatsappIntegration(enableWhatsappIntegration);
        return this;
    }

    public void setEnableWhatsappIntegration(Boolean enableWhatsappIntegration) {
        this.enableWhatsappIntegration = enableWhatsappIntegration;
    }

    public Boolean getEnableEmailMarketing() {
        return this.enableEmailMarketing;
    }

    public TenantSettings enableEmailMarketing(Boolean enableEmailMarketing) {
        this.setEnableEmailMarketing(enableEmailMarketing);
        return this;
    }

    public void setEnableEmailMarketing(Boolean enableEmailMarketing) {
        this.enableEmailMarketing = enableEmailMarketing;
    }

    public String getWhatsappApiKey() {
        return this.whatsappApiKey;
    }

    public TenantSettings whatsappApiKey(String whatsappApiKey) {
        this.setWhatsappApiKey(whatsappApiKey);
        return this;
    }

    public void setWhatsappApiKey(String whatsappApiKey) {
        this.whatsappApiKey = whatsappApiKey;
    }

    public String getEmailProviderConfig() {
        return this.emailProviderConfig;
    }

    public TenantSettings emailProviderConfig(String emailProviderConfig) {
        this.setEmailProviderConfig(emailProviderConfig);
        return this;
    }

    public void setEmailProviderConfig(String emailProviderConfig) {
        this.emailProviderConfig = emailProviderConfig;
    }

    public Integer getMaxEventsPerMonth() {
        return this.maxEventsPerMonth;
    }

    public TenantSettings maxEventsPerMonth(Integer maxEventsPerMonth) {
        this.setMaxEventsPerMonth(maxEventsPerMonth);
        return this;
    }

    public void setMaxEventsPerMonth(Integer maxEventsPerMonth) {
        this.maxEventsPerMonth = maxEventsPerMonth;
    }

    public Integer getMaxAttendeesPerEvent() {
        return this.maxAttendeesPerEvent;
    }

    public TenantSettings maxAttendeesPerEvent(Integer maxAttendeesPerEvent) {
        this.setMaxAttendeesPerEvent(maxAttendeesPerEvent);
        return this;
    }

    public void setMaxAttendeesPerEvent(Integer maxAttendeesPerEvent) {
        this.maxAttendeesPerEvent = maxAttendeesPerEvent;
    }

    public Boolean getEnableGuestRegistration() {
        return this.enableGuestRegistration;
    }

    public TenantSettings enableGuestRegistration(Boolean enableGuestRegistration) {
        this.setEnableGuestRegistration(enableGuestRegistration);
        return this;
    }

    public void setEnableGuestRegistration(Boolean enableGuestRegistration) {
        this.enableGuestRegistration = enableGuestRegistration;
    }

    public Integer getMaxGuestsPerAttendee() {
        return this.maxGuestsPerAttendee;
    }

    public TenantSettings maxGuestsPerAttendee(Integer maxGuestsPerAttendee) {
        this.setMaxGuestsPerAttendee(maxGuestsPerAttendee);
        return this;
    }

    public void setMaxGuestsPerAttendee(Integer maxGuestsPerAttendee) {
        this.maxGuestsPerAttendee = maxGuestsPerAttendee;
    }

    public Integer getDefaultEventCapacity() {
        return this.defaultEventCapacity;
    }

    public TenantSettings defaultEventCapacity(Integer defaultEventCapacity) {
        this.setDefaultEventCapacity(defaultEventCapacity);
        return this;
    }

    public void setDefaultEventCapacity(Integer defaultEventCapacity) {
        this.defaultEventCapacity = defaultEventCapacity;
    }

    public BigDecimal getPlatformFeePercentage() {
        return this.platformFeePercentage;
    }

    public TenantSettings platformFeePercentage(BigDecimal platformFeePercentage) {
        this.setPlatformFeePercentage(platformFeePercentage);
        return this;
    }

    public void setPlatformFeePercentage(BigDecimal platformFeePercentage) {
        this.platformFeePercentage = platformFeePercentage;
    }

    public String getCustomCss() {
        return this.customCss;
    }

    public TenantSettings customCss(String customCss) {
        this.setCustomCss(customCss);
        return this;
    }

    public void setCustomCss(String customCss) {
        this.customCss = customCss;
    }

    public String getCustomJs() {
        return this.customJs;
    }

    public TenantSettings customJs(String customJs) {
        this.setCustomJs(customJs);
        return this;
    }

    public void setCustomJs(String customJs) {
        this.customJs = customJs;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public TenantSettings createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public TenantSettings updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public TenantOrganization getTenantOrganization() {
        return this.tenantOrganization;
    }

    public void setTenantOrganization(TenantOrganization tenantOrganization) {
        this.tenantOrganization = tenantOrganization;
    }

    public TenantSettings tenantOrganization(TenantOrganization tenantOrganization) {
        this.setTenantOrganization(tenantOrganization);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TenantSettings)) {
            return false;
        }
        return getId() != null && getId().equals(((TenantSettings) o).getId());
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
        return "TenantSettings{" +
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
                ", createdAt='" + getCreatedAt() + "'" +
                ", updatedAt='" + getUpdatedAt() + "'" +
                "}";
    }
}
