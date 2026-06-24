package com.eventsitemanager.repository;

import com.eventsitemanager.domain.PaymentProviderConfig;
import com.eventsitemanager.domain.enumeration.PaymentProvider;
import com.eventsitemanager.domain.enumeration.PaymentUseCase;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PaymentProviderConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentProviderConfigRepository
    extends JpaRepository<PaymentProviderConfig, Long>, JpaSpecificationExecutor<PaymentProviderConfig> {
    Optional<PaymentProviderConfig> findByTenantIdAndProviderName(String tenantId, PaymentProvider providerName);

    List<PaymentProviderConfig> findByTenantIdAndIsActiveTrueOrderByFallbackOrderAsc(String tenantId);

    /**
     * Find active provider configs for a tenant and payment use case.
     * Matches configs with the exact payment_use_case OR NULL (general-purpose providers).
     */
    @Query(
        "SELECT p FROM PaymentProviderConfig p " +
        "WHERE p.tenantId = :tenantId " +
        "AND p.isActive = true " +
        "AND (p.paymentUseCase = :paymentUseCase OR p.paymentUseCase IS NULL) " +
        "ORDER BY p.fallbackOrder ASC"
    )
    List<PaymentProviderConfig> findByTenantIdAndPaymentUseCaseAndIsActiveTrueOrderByFallbackOrderAsc(
        @Param("tenantId") String tenantId,
        @Param("paymentUseCase") PaymentUseCase paymentUseCase
    );

    /**
     * Find payment provider config by tenant ID and Payment Method Domain ID.
     * Used for triple validation (tenantId, paymentMethodDomainId, webhookSecret).
     *
     * @param tenantId Tenant ID
     * @param paymentMethodDomainId Payment Method Domain ID (Stripe Payment Method Domain ID, e.g., pmd_*)
     * @return Optional containing the payment provider config if found
     */
    Optional<PaymentProviderConfig> findByTenantIdAndPaymentMethodDomainId(String tenantId, String paymentMethodDomainId);
}
