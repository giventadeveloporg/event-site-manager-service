package com.eventsitemanager.web.rest;

import com.eventsitemanager.domain.PaymentProviderConfig;
import com.eventsitemanager.domain.enumeration.PaymentProvider;
import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.PaymentProviderConfigRepository;
import com.eventsitemanager.security.TenantContext;
import com.eventsitemanager.service.dto.ManualPaymentMethodDTO;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for listing manual payment methods enabled for a tenant.
 */
@RestController
@RequestMapping("/api/manual-payment-methods")
public class ManualPaymentMethodResource {

    private final Logger log = LoggerFactory.getLogger(ManualPaymentMethodResource.class);

    private static final String ENTITY_NAME = "manualPaymentMethod";

    private static final EnumSet<PaymentProvider> MANUAL_PROVIDERS = EnumSet.of(
        PaymentProvider.ZELLE_MANUAL,
        PaymentProvider.VENMO_MANUAL,
        PaymentProvider.CASH_APP_MANUAL,
        PaymentProvider.CASH,
        PaymentProvider.CHECK,
        PaymentProvider.OTHER_MANUAL
    );

    private final PaymentProviderConfigRepository paymentProviderConfigRepository;

    public ManualPaymentMethodResource(PaymentProviderConfigRepository paymentProviderConfigRepository) {
        this.paymentProviderConfigRepository = paymentProviderConfigRepository;
    }

    @GetMapping("")
    public ResponseEntity<List<ManualPaymentMethodDTO>> getManualPaymentMethods() {
        String tenantId = requireTenantId();
        log.debug("REST request to get manual payment methods for tenant {}", tenantId);

        List<PaymentProviderConfig> configs = paymentProviderConfigRepository.findByTenantIdAndIsActiveTrueOrderByFallbackOrderAsc(
            tenantId
        );

        List<ManualPaymentMethodDTO> result = configs
            .stream()
            .filter(cfg -> cfg.getProviderName() != null && MANUAL_PROVIDERS.contains(cfg.getProviderName()))
            .map(cfg -> {
                ManualPaymentMethodDTO dto = new ManualPaymentMethodDTO();
                dto.setProviderName(cfg.getProviderName());
                dto.setIsActive(cfg.getIsActive());
                dto.setConfigurationJson(cfg.getConfigurationJson());
                return dto;
            })
            .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    private String requireTenantId() {
        String tenantId = TenantContext.getCurrentTenant();
        if (tenantId == null || tenantId.isBlank()) {
            throw new BadRequestAlertException("Tenant ID is required", ENTITY_NAME, "tenantIdRequired");
        }
        return tenantId;
    }
}
