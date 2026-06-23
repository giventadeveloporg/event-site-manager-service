package com.eventsitemanager.repository;

import com.eventsitemanager.domain.PlatformInvoice;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PlatformInvoice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlatformInvoiceRepository extends JpaRepository<PlatformInvoice, Long>, JpaSpecificationExecutor<PlatformInvoice> {
    Optional<PlatformInvoice> findByInvoiceNumber(String invoiceNumber);

    List<PlatformInvoice> findByTenantIdOrderByInvoiceDateDesc(String tenantId);

    List<PlatformInvoice> findByTenantIdAndStatusOrderByInvoiceDateDesc(String tenantId, String status);
}
