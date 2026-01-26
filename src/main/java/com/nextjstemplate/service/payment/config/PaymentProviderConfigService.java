package com.nextjstemplate.service.payment.config;

import com.nextjstemplate.domain.PaymentProviderConfig;
import com.nextjstemplate.domain.enumeration.PaymentProvider;
import com.nextjstemplate.domain.enumeration.PaymentUseCase;
import com.nextjstemplate.repository.PaymentProviderConfigRepository;
import com.nextjstemplate.service.payment.encryption.PaymentCredentialEncryptionService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing payment provider configurations.
 */
@Service
@Transactional
public class PaymentProviderConfigService {

    private static final Logger log = LoggerFactory.getLogger(PaymentProviderConfigService.class);

    private final PaymentProviderConfigRepository paymentProviderConfigRepository;
    private final PaymentCredentialEncryptionService encryptionService;

    public PaymentProviderConfigService(
        PaymentProviderConfigRepository paymentProviderConfigRepository,
        PaymentCredentialEncryptionService encryptionService
    ) {
        this.paymentProviderConfigRepository = paymentProviderConfigRepository;
        this.encryptionService = encryptionService;
    }

    /**
     * Get provider configuration for a tenant and provider.
     *
     * @param tenantId Tenant ID
     * @param provider Provider name
     * @return Provider configuration as Map, or empty if not found
     */
    @Transactional(readOnly = true)
    public Optional<Map<String, Object>> getProviderConfig(String tenantId, PaymentProvider provider) {
        return paymentProviderConfigRepository.findByTenantIdAndProviderName(tenantId, provider).map(this::toConfigMap);
    }

    /**
     * Get active provider configurations for a tenant, ordered by fallback order.
     *
     * @param tenantId Tenant ID
     * @param paymentUseCase Optional payment use case filter
     * @return List of provider configurations ordered by fallback priority
     */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getActiveProviderConfigs(String tenantId, PaymentUseCase paymentUseCase) {
        List<PaymentProviderConfig> configs;
        if (paymentUseCase != null) {
            configs =
                paymentProviderConfigRepository.findByTenantIdAndPaymentUseCaseAndIsActiveTrueOrderByFallbackOrderAsc(
                    tenantId,
                    paymentUseCase
                );
        } else {
            configs = paymentProviderConfigRepository.findByTenantIdAndIsActiveTrueOrderByFallbackOrderAsc(tenantId);
        }

        return configs.stream().map(this::toConfigMap).toList();
    }

    /**
     * Convert PaymentProviderConfig entity to configuration map for use by payment adapters.
     *
     * @param config PaymentProviderConfig entity
     * @return Configuration map with decrypted credentials
     */
    private Map<String, Object> toConfigMap(PaymentProviderConfig config) {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put("id", config.getId());
        configMap.put("tenantId", config.getTenantId());
        configMap.put("providerName", config.getProviderName());
        configMap.put("paymentUseCase", config.getPaymentUseCase());
        configMap.put("isActive", config.getIsActive());
        configMap.put("supportsAcp", config.getSupportsAcp());
        configMap.put("supportsZeffy", config.getSupportsZeffy());
        configMap.put("supportsZelle", config.getSupportsZelle());
        configMap.put("supportsRevolut", config.getSupportsRevolut());
        configMap.put("publishableKey", config.getPublishableKey());
        configMap.put("fallbackOrder", config.getFallbackOrder());

        // Decrypt credentials
        if (config.getProviderApiKeyEncrypted() != null) {
            try {
                configMap.put("apiKey", encryptionService.decrypt(config.getProviderApiKeyEncrypted()));
            } catch (Exception e) {
                log.error("Failed to decrypt API key for provider {}", config.getProviderName(), e);
            }
        }

        if (config.getProviderSecretKeyEncrypted() != null) {
            try {
                configMap.put("secretKey", encryptionService.decrypt(config.getProviderSecretKeyEncrypted()));
            } catch (Exception e) {
                log.error("Failed to decrypt secret key for provider {}", config.getProviderName(), e);
            }
        }

        if (config.getWebhookSecretEncrypted() != null) {
            try {
                configMap.put("webhookSecret", encryptionService.decrypt(config.getWebhookSecretEncrypted()));
            } catch (Exception e) {
                log.error("Failed to decrypt webhook secret for provider {}", config.getProviderName(), e);
            }
        }

        // Add configuration JSON if present
        if (config.getConfigurationJson() != null) {
            configMap.put("configurationJson", config.getConfigurationJson());
            // Parse JSON and extract common fields (like campaignId for GiveButter)
            try {
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                Map<String, Object> configJsonMap = mapper.readValue(config.getConfigurationJson(), Map.class);
                // Extract campaignId if present (for GiveButter)
                if (configJsonMap.containsKey("campaignId")) {
                    configMap.put("campaignId", configJsonMap.get("campaignId"));
                }
                // Add other common fields as needed
            } catch (Exception e) {
                log.debug("Failed to parse configuration JSON for provider {}: {}", config.getProviderName(), e.getMessage());
                // Continue without parsing - adapter will handle raw JSON string
            }
        }

        return configMap;
    }

    /**
     * Save or update provider configuration.
     *
     * @param config PaymentProviderConfig entity
     * @return Saved entity
     */
    public PaymentProviderConfig save(PaymentProviderConfig config) {
        return paymentProviderConfigRepository.save(config);
    }

    /**
     * Check if provider supports a specific feature for a tenant.
     *
     * @param tenantId Tenant ID
     * @param provider Provider name
     * @param feature Feature name (ACP, ZEFFY, ZELLE, REVOLUT)
     * @return true if feature is supported
     */
    @Transactional(readOnly = true)
    public boolean supportsFeature(String tenantId, PaymentProvider provider, String feature) {
        return paymentProviderConfigRepository
            .findByTenantIdAndProviderName(tenantId, provider)
            .map(config -> {
                return switch (feature.toUpperCase()) {
                    case "ACP" -> config.getSupportsAcp();
                    case "ZEFFY" -> config.getSupportsZeffy();
                    case "ZELLE" -> config.getSupportsZelle();
                    case "REVOLUT" -> config.getSupportsRevolut();
                    default -> false;
                };
            })
            .orElse(false);
    }
}
