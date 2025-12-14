package com.nextjstemplate.repository;

import com.nextjstemplate.domain.PromotionEmailTemplate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PromotionEmailTemplate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PromotionEmailTemplateRepository
    extends JpaRepository<PromotionEmailTemplate, Long>, JpaSpecificationExecutor<PromotionEmailTemplate> {
    /**
     * Find templates by tenant ID and event ID.
     *
     * @param tenantId Tenant ID
     * @param eventId Event ID
     * @return List of templates
     */
    List<PromotionEmailTemplate> findByTenantIdAndEventId(String tenantId, Long eventId);

    /**
     * Find active templates by tenant ID.
     *
     * @param tenantId Tenant ID
     * @param isActive Active status
     * @return List of templates
     */
    List<PromotionEmailTemplate> findByTenantIdAndIsActive(String tenantId, Boolean isActive);

    /**
     * Find template by tenant ID, event ID, and template name (for uniqueness check).
     *
     * @param tenantId Tenant ID
     * @param eventId Event ID
     * @param templateName Template name
     * @return Optional template
     */
    Optional<PromotionEmailTemplate> findByTenantIdAndEventIdAndTemplateName(String tenantId, Long eventId, String templateName);
}

