package com.nextjstemplate.service;

import com.nextjstemplate.domain.PaymentProviderConfig;
import com.nextjstemplate.errors.BadRequestAlertException;
import com.nextjstemplate.repository.PaymentProviderConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for validating payment provider triple combination (tenantId, paymentMethodDomainId, webhookSecret).
 */
@Service
public class PaymentProviderValidationService {

    private static final Logger log = LoggerFactory.getLogger(PaymentProviderValidationService.class);

    private final PaymentProviderConfigRepository paymentProviderConfigRepository;

    public PaymentProviderValidationService(PaymentProviderConfigRepository paymentProviderConfigRepository) {
        this.paymentProviderConfigRepository = paymentProviderConfigRepository;
    }

    /**
     * Validate triple combination (tenantId, paymentMethodDomainId, webhookSecret).
     * This ensures that the combination exists in payment_provider_config table.
     *
     * CRITICAL: This method MUST use the tenantId parameter from the DTO, NOT the tenant ID from TenantContext.
     * The tenantId parameter comes from the DTO and represents the actual tenant making the request.
     * The tenant context is set by TenantContextFilter based on request headers/query params and defaults
     * to tenant_demo_001 if not found. For triple validation, we MUST use the DTO's tenant ID.
     *
     * @param tenantId Tenant ID from DTO (MUST be used, not TenantContext)
     * @param paymentMethodDomainId Payment Method Domain ID
     * @param entityName Entity name for error messages (e.g., "eventTicketTransaction", "eventTicketTransactionItem")
     */
    @Transactional(readOnly = true)
    public void validateTripleCombination(String tenantId, String paymentMethodDomainId, String entityName) {
        log.debug(
            "Enter: validateTripleCombination() with tenantId={}, paymentMethodDomainId={}, entityName={}",
            tenantId,
            paymentMethodDomainId,
            entityName
        );

        // CRITICAL: Use tenantId parameter from DTO, NOT TenantContext
        // The tenantId parameter comes from the DTO and represents the actual tenant making the request
        if (tenantId == null || tenantId.isEmpty()) {
            log.error("Tenant ID is required for triple validation");
            throw new BadRequestAlertException("Tenant ID is required for triple validation", entityName, "error.missingTenantId");
        }

        if (paymentMethodDomainId == null || paymentMethodDomainId.isEmpty()) {
            log.error("Payment Method Domain ID is required for triple validation");
            throw new BadRequestAlertException(
                "Payment Method Domain ID is required for triple validation",
                entityName,
                "error.missingPaymentMethodDomainId"
            );
        }

        // Query database with tenantId from DTO (NOT from tenant context)
        // CRITICAL: Use tenantId parameter, NOT TenantContext.getCurrentTenant()
        var configOpt = paymentProviderConfigRepository.findByTenantIdAndPaymentMethodDomainId(tenantId, paymentMethodDomainId);

        if (configOpt.isEmpty()) {
            log.error(
                "Triple validation failed: No payment_provider_config found for tenantId={}, paymentMethodDomainId={}",
                tenantId,
                paymentMethodDomainId
            );
            throw new BadRequestAlertException(
                String.format(
                    "Invalid tenant/payment method domain combination: tenantId=%s, paymentMethodDomainId=%s",
                    tenantId,
                    paymentMethodDomainId
                ),
                entityName,
                "error.tripleValidationFailed"
            );
        }

        PaymentProviderConfig config = configOpt.orElseThrow();

        // Verify the combination matches
        if (config.getPaymentMethodDomainId() == null || !config.getPaymentMethodDomainId().equals(paymentMethodDomainId)) {
            log.error(
                "Triple validation failed: Payment Method Domain ID mismatch. Expected={}, Got={}",
                config.getPaymentMethodDomainId(),
                paymentMethodDomainId
            );
            throw new BadRequestAlertException("Payment Method Domain ID mismatch", entityName, "paymentMethodDomainIdMismatch");
        }

        // Optional: Verify webhook secret if needed (for webhook requests)
        // For frontend API calls, webhook secret validation is not required
        // But we can log if it exists for debugging
        if (config.getWebhookSecretEncrypted() != null && !config.getWebhookSecretEncrypted().isEmpty()) {
            log.debug(
                "Triple validation passed: tenantId={}, paymentMethodDomainId={}, webhookSecretExists=true",
                tenantId,
                paymentMethodDomainId
            );
        } else {
            log.debug(
                "Triple validation passed: tenantId={}, paymentMethodDomainId={}, webhookSecretExists=false",
                tenantId,
                paymentMethodDomainId
            );
        }
    }
}
