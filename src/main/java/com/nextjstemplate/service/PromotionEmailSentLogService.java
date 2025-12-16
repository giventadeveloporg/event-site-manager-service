package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.PromotionEmailSentLogDTO;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for querying promotion email sent logs.
 */
public interface PromotionEmailSentLogService {
    /**
     * Get all logs with pagination and filtering.
     *
     * @param pageable the pagination information
     * @param tenantId the tenant ID
     * @return the list of entities
     */
    Page<PromotionEmailSentLogDTO> findAll(Pageable pageable, String tenantId);

    /**
     * Get the "id" log entry.
     *
     * @param id the id of the entity
     * @param tenantId the tenant ID
     * @return the entity
     */
    Optional<PromotionEmailSentLogDTO> findOne(Long id, String tenantId);

    /**
     * Get email statistics for an event.
     *
     * @param eventId the event ID
     * @param tenantId the tenant ID
     * @return Map containing statistics (totalSent, totalFailed, totalBounced, etc.)
     */
    Map<String, Object> getEmailStatistics(Long eventId, String tenantId);
}
