package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.EventAdminAuditLogDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.nextjstemplate.domain.EventAdminAuditLog}.
 */
public interface EventAdminAuditLogService {
    /**
     * Save a eventAdminAuditLog.
     *
     * @param eventAdminAuditLogDTO the entity to save.
     * @return the persisted entity.
     */
    EventAdminAuditLogDTO save(EventAdminAuditLogDTO eventAdminAuditLogDTO);

    /**
     * Updates a eventAdminAuditLog.
     *
     * @param eventAdminAuditLogDTO the entity to update.
     * @return the persisted entity.
     */
    EventAdminAuditLogDTO update(EventAdminAuditLogDTO eventAdminAuditLogDTO);

    /**
     * Partially updates a eventAdminAuditLog.
     *
     * @param eventAdminAuditLogDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EventAdminAuditLogDTO> partialUpdate(EventAdminAuditLogDTO eventAdminAuditLogDTO);

    /**
     * Get the "id" eventAdminAuditLog.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EventAdminAuditLogDTO> findOne(Long id);

    /**
     * Delete the "id" eventAdminAuditLog.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
