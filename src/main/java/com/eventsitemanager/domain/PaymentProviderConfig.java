package com.eventsitemanager.domain;

import com.eventsitemanager.domain.enumeration.PaymentProvider;
import com.eventsitemanager.domain.enumeration.PaymentUseCase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PaymentProviderConfig.
 * Stores tenant-level payment provider configurations and feature flags.
 */
@Entity
@Table(name = "payment_provider_config")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentProviderConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "public.sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "tenant_id", length = 255, nullable = false)
    private String tenantId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "provider_name", length = 50, nullable = false)
    private PaymentProvider providerName;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_use_case", length = 50)
    private PaymentUseCase paymentUseCase;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @NotNull
    @Column(name = "supports_acp", nullable = false)
    private Boolean supportsAcp = false;

    @NotNull
    @Column(name = "supports_zeffy", nullable = false)
    private Boolean supportsZeffy = false;

    @NotNull
    @Column(name = "supports_zelle", nullable = false)
    private Boolean supportsZelle = false;

    @NotNull
    @Column(name = "supports_revolut", nullable = false)
    private Boolean supportsRevolut = false;

    @Column(name = "provider_api_key_encrypted", columnDefinition = "text")
    private String providerApiKeyEncrypted;

    @Column(name = "provider_secret_key_encrypted", columnDefinition = "text")
    private String providerSecretKeyEncrypted;

    @Column(name = "webhook_secret_encrypted", columnDefinition = "text")
    private String webhookSecretEncrypted;

    @Size(max = 500)
    @Column(name = "publishable_key", length = 500)
    private String publishableKey;

    @Size(max = 255)
    @Column(name = "payment_method_domain_id", length = 255)
    private String paymentMethodDomainId;

    @Column(name = "fallback_order")
    private Integer fallbackOrder = 0;

    @Column(name = "configuration_json", columnDefinition = "text")
    private String configurationJson;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PaymentProviderConfig id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public PaymentProviderConfig tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public PaymentProvider getProviderName() {
        return this.providerName;
    }

    public PaymentProviderConfig providerName(PaymentProvider providerName) {
        this.setProviderName(providerName);
        return this;
    }

    public void setProviderName(PaymentProvider providerName) {
        this.providerName = providerName;
    }

    public PaymentUseCase getPaymentUseCase() {
        return this.paymentUseCase;
    }

    public PaymentProviderConfig paymentUseCase(PaymentUseCase paymentUseCase) {
        this.setPaymentUseCase(paymentUseCase);
        return this;
    }

    public void setPaymentUseCase(PaymentUseCase paymentUseCase) {
        this.paymentUseCase = paymentUseCase;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public PaymentProviderConfig isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getSupportsAcp() {
        return this.supportsAcp;
    }

    public PaymentProviderConfig supportsAcp(Boolean supportsAcp) {
        this.setSupportsAcp(supportsAcp);
        return this;
    }

    public void setSupportsAcp(Boolean supportsAcp) {
        this.supportsAcp = supportsAcp;
    }

    public Boolean getSupportsZeffy() {
        return this.supportsZeffy;
    }

    public PaymentProviderConfig supportsZeffy(Boolean supportsZeffy) {
        this.setSupportsZeffy(supportsZeffy);
        return this;
    }

    public void setSupportsZeffy(Boolean supportsZeffy) {
        this.supportsZeffy = supportsZeffy;
    }

    public Boolean getSupportsZelle() {
        return this.supportsZelle;
    }

    public PaymentProviderConfig supportsZelle(Boolean supportsZelle) {
        this.setSupportsZelle(supportsZelle);
        return this;
    }

    public void setSupportsZelle(Boolean supportsZelle) {
        this.supportsZelle = supportsZelle;
    }

    public Boolean getSupportsRevolut() {
        return this.supportsRevolut;
    }

    public PaymentProviderConfig supportsRevolut(Boolean supportsRevolut) {
        this.setSupportsRevolut(supportsRevolut);
        return this;
    }

    public void setSupportsRevolut(Boolean supportsRevolut) {
        this.supportsRevolut = supportsRevolut;
    }

    public String getProviderApiKeyEncrypted() {
        return this.providerApiKeyEncrypted;
    }

    public PaymentProviderConfig providerApiKeyEncrypted(String providerApiKeyEncrypted) {
        this.setProviderApiKeyEncrypted(providerApiKeyEncrypted);
        return this;
    }

    public void setProviderApiKeyEncrypted(String providerApiKeyEncrypted) {
        this.providerApiKeyEncrypted = providerApiKeyEncrypted;
    }

    public String getProviderSecretKeyEncrypted() {
        return this.providerSecretKeyEncrypted;
    }

    public PaymentProviderConfig providerSecretKeyEncrypted(String providerSecretKeyEncrypted) {
        this.setProviderSecretKeyEncrypted(providerSecretKeyEncrypted);
        return this;
    }

    public void setProviderSecretKeyEncrypted(String providerSecretKeyEncrypted) {
        this.providerSecretKeyEncrypted = providerSecretKeyEncrypted;
    }

    public String getWebhookSecretEncrypted() {
        return this.webhookSecretEncrypted;
    }

    public PaymentProviderConfig webhookSecretEncrypted(String webhookSecretEncrypted) {
        this.setWebhookSecretEncrypted(webhookSecretEncrypted);
        return this;
    }

    public void setWebhookSecretEncrypted(String webhookSecretEncrypted) {
        this.webhookSecretEncrypted = webhookSecretEncrypted;
    }

    public String getPublishableKey() {
        return this.publishableKey;
    }

    public PaymentProviderConfig publishableKey(String publishableKey) {
        this.setPublishableKey(publishableKey);
        return this;
    }

    public void setPublishableKey(String publishableKey) {
        this.publishableKey = publishableKey;
    }

    public String getPaymentMethodDomainId() {
        return this.paymentMethodDomainId;
    }

    public PaymentProviderConfig paymentMethodDomainId(String paymentMethodDomainId) {
        this.setPaymentMethodDomainId(paymentMethodDomainId);
        return this;
    }

    public void setPaymentMethodDomainId(String paymentMethodDomainId) {
        this.paymentMethodDomainId = paymentMethodDomainId;
    }

    public Integer getFallbackOrder() {
        return this.fallbackOrder;
    }

    public PaymentProviderConfig fallbackOrder(Integer fallbackOrder) {
        this.setFallbackOrder(fallbackOrder);
        return this;
    }

    public void setFallbackOrder(Integer fallbackOrder) {
        this.fallbackOrder = fallbackOrder;
    }

    public String getConfigurationJson() {
        return this.configurationJson;
    }

    public PaymentProviderConfig configurationJson(String configurationJson) {
        this.setConfigurationJson(configurationJson);
        return this;
    }

    public void setConfigurationJson(String configurationJson) {
        this.configurationJson = configurationJson;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public PaymentProviderConfig createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public PaymentProviderConfig updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentProviderConfig)) {
            return false;
        }
        return getId() != null && getId().equals(((PaymentProviderConfig) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "PaymentProviderConfig{" +
            "id=" +
            getId() +
            ", tenantId='" +
            getTenantId() +
            "'" +
            ", providerName='" +
            getProviderName() +
            "'" +
            ", paymentUseCase='" +
            getPaymentUseCase() +
            "'" +
            ", isActive='" +
            getIsActive() +
            "'" +
            ", supportsAcp='" +
            getSupportsAcp() +
            "'" +
            ", supportsZeffy='" +
            getSupportsZeffy() +
            "'" +
            ", supportsZelle='" +
            getSupportsZelle() +
            "'" +
            ", supportsRevolut='" +
            getSupportsRevolut() +
            "'" +
            ", fallbackOrder=" +
            getFallbackOrder() +
            ", createdAt='" +
            getCreatedAt() +
            "'" +
            ", updatedAt='" +
            getUpdatedAt() +
            "'" +
            "}"
        );
    }
}
