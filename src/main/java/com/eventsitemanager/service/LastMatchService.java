package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.LastMatchDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.eventsitemanager.domain.LastMatch}.
 */
public interface LastMatchService {
    /**
     * Save a lastMatch.
     *
     * @param lastMatchDTO the entity to save.
     * @return the persisted entity.
     */
    LastMatchDTO save(LastMatchDTO lastMatchDTO);

    /**
     * Updates a lastMatch.
     *
     * @param lastMatchDTO the entity to update.
     * @return the persisted entity.
     */
    LastMatchDTO update(LastMatchDTO lastMatchDTO);

    /**
     * Partially updates a lastMatch.
     *
     * @param lastMatchDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LastMatchDTO> partialUpdate(LastMatchDTO lastMatchDTO);

    /**
     * Get the "id" lastMatch.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LastMatchDTO> findOne(Long id);

    /**
     * Delete the "id" lastMatch.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
