package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.EmailLogDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.nextjstemplate.domain.EmailLog}.
 */
public interface EmailLogService {
    /**
     * Save a emailLog.
     *
     * @param emailLogDTO the entity to save.
     * @return the persisted entity.
     */
    EmailLogDTO save(EmailLogDTO emailLogDTO);

    /**
     * Updates a emailLog.
     *
     * @param emailLogDTO the entity to update.
     * @return the persisted entity.
     */
    EmailLogDTO update(EmailLogDTO emailLogDTO);

    /**
     * Partially updates a emailLog.
     *
     * @param emailLogDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EmailLogDTO> partialUpdate(EmailLogDTO emailLogDTO);

    /**
     * Get the "id" emailLog.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmailLogDTO> findOne(Long id);

    /**
     * Delete the "id" emailLog.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
