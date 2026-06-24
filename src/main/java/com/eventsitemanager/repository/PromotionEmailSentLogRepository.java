package com.eventsitemanager.repository;

import com.eventsitemanager.domain.PromotionEmailSentLog;
import com.eventsitemanager.domain.enumeration.EmailStatus;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PromotionEmailSentLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PromotionEmailSentLogRepository
    extends JpaRepository<PromotionEmailSentLog, Long>, JpaSpecificationExecutor<PromotionEmailSentLog> {
    /**
     * Find logs by tenant ID and event ID.
     *
     * @param tenantId Tenant ID
     * @param eventId Event ID
     * @return List of logs
     */
    List<PromotionEmailSentLog> findByTenantIdAndEventId(String tenantId, Long eventId);

    /**
     * Find logs by tenant ID and template ID.
     *
     * @param tenantId Tenant ID
     * @param templateId Template ID
     * @return List of logs
     */
    List<PromotionEmailSentLog> findByTenantIdAndTemplateId(String tenantId, Long templateId);

    /**
     * Find logs by tenant ID and date range.
     *
     * @param tenantId Tenant ID
     * @param startDate Start date
     * @param endDate End date
     * @return List of logs
     */
    List<PromotionEmailSentLog> findByTenantIdAndSentAtBetween(String tenantId, ZonedDateTime startDate, ZonedDateTime endDate);

    /**
     * Find logs by tenant ID and email status.
     *
     * @param tenantId Tenant ID
     * @param emailStatus Email status
     * @return List of logs
     */
    List<PromotionEmailSentLog> findByTenantIdAndEmailStatus(String tenantId, EmailStatus emailStatus);
}
