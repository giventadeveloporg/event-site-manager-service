package com.eventsitemanager.repository;

import com.eventsitemanager.domain.TenantSettings;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TenantSettings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TenantSettingsRepository extends JpaRepository<TenantSettings, Long>, JpaSpecificationExecutor<TenantSettings> {
    /**
     * Find tenant settings by tenant ID.
     *
     * @param tenantId the tenant ID
     * @return the tenant settings
     */
    Optional<TenantSettings> findByTenantId(String tenantId);
}
