package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.EventLiveUpdateDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.nextjstemplate.domain.EventLiveUpdate}.
 */
public interface EventLiveUpdateService {
    /**
     * Save a eventLiveUpdate.
     *
     * @param eventLiveUpdateDTO the entity to save.
     * @return the persisted entity.
     */
    EventLiveUpdateDTO save(EventLiveUpdateDTO eventLiveUpdateDTO);

    /**
     * Updates a eventLiveUpdate.
     *
     * @param eventLiveUpdateDTO the entity to update.
     * @return the persisted entity.
     */
    EventLiveUpdateDTO update(EventLiveUpdateDTO eventLiveUpdateDTO);

    /**
     * Partially updates a eventLiveUpdate.
     *
     * @param eventLiveUpdateDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EventLiveUpdateDTO> partialUpdate(EventLiveUpdateDTO eventLiveUpdateDTO);

    /**
     * Get the "id" eventLiveUpdate.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EventLiveUpdateDTO> findOne(Long id);

    /**
     * Delete the "id" eventLiveUpdate.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
