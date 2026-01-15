package com.nextjstemplate.service.dto;

import com.nextjstemplate.domain.enumeration.PaymentProvider;

/**
 * DTO representing an enabled manual payment method for a tenant.
 *
 * Source of truth is {@link com.nextjstemplate.domain.PaymentProviderConfig}.
 */
public class ManualPaymentMethodDTO {

    private PaymentProvider providerName;

    private Boolean isActive;

    /**
     * Arbitrary JSON/text configuration for manual instructions/handles.
     * Stored as TEXT in payment_provider_config.configuration_json.
     */
    private String configurationJson;

    public PaymentProvider getProviderName() {
        return providerName;
    }

    public void setProviderName(PaymentProvider providerName) {
        this.providerName = providerName;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getConfigurationJson() {
        return configurationJson;
    }

    public void setConfigurationJson(String configurationJson) {
        this.configurationJson = configurationJson;
    }
}
