package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.EventTypeDetailsDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.eventsitemanager.domain.EventTypeDetails}.
 */
public interface EventTypeDetailsService {
    /**
     * Save a eventTypeDetails.
     *
     * @param eventTypeDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    EventTypeDetailsDTO save(EventTypeDetailsDTO eventTypeDetailsDTO);

    /**
     * Updates a eventTypeDetails.
     *
     * @param eventTypeDetailsDTO the entity to update.
     * @return the persisted entity.
     */
    EventTypeDetailsDTO update(EventTypeDetailsDTO eventTypeDetailsDTO);

    /**
     * Partially updates a eventTypeDetails.
     *
     * @param eventTypeDetailsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EventTypeDetailsDTO> partialUpdate(EventTypeDetailsDTO eventTypeDetailsDTO);

    /**
     * Get all the eventTypeDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EventTypeDetailsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" eventTypeDetails.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EventTypeDetailsDTO> findOne(Long id);

    /**
     * Delete the "id" eventTypeDetails.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
