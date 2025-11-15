package com.nextjstemplate.repository;

import com.nextjstemplate.domain.PlatformSettlement;
import com.nextjstemplate.domain.enumeration.PaymentProvider;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PlatformSettlement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlatformSettlementRepository
    extends JpaRepository<PlatformSettlement, Long>, JpaSpecificationExecutor<PlatformSettlement> {
    Optional<PlatformSettlement> findByTenantIdAndProviderNameAndSettlementDate(
        String tenantId,
        PaymentProvider providerName,
        LocalDate settlementDate
    );

    List<PlatformSettlement> findByTenantIdOrderBySettlementDateDesc(String tenantId);

    List<PlatformSettlement> findByTenantIdAndStatusOrderBySettlementDateDesc(String tenantId, String status);
}
