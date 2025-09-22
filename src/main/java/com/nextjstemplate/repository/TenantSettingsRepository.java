package com.nextjstemplate.repository;

import com.nextjstemplate.domain.TenantSettings;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Spring Data JPA repository for the TenantSettings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TenantSettingsRepository
    extends JpaRepository<TenantSettings, Long>, JpaSpecificationExecutor<TenantSettings> {

  /**
   * Find tenant settings by tenant ID.
   *
   * @param tenantId the tenant ID
   * @return the tenant settings
   */
  Optional<TenantSettings> findByTenantId(String tenantId);
}
