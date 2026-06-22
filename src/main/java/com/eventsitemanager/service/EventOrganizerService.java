package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.EventOrganizerDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.eventsitemanager.domain.EventOrganizer}.
 */
public interface EventOrganizerService {
    /**
     * Save a eventOrganizer.
     *
     * @param eventOrganizerDTO the entity to save.
     * @return the persisted entity.
     */
    EventOrganizerDTO save(EventOrganizerDTO eventOrganizerDTO);

    /**
     * Updates a eventOrganizer.
     *
     * @param eventOrganizerDTO the entity to update.
     * @return the persisted entity.
     */
    EventOrganizerDTO update(EventOrganizerDTO eventOrganizerDTO);

    /**
     * Partially updates a eventOrganizer.
     *
     * @param eventOrganizerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EventOrganizerDTO> partialUpdate(EventOrganizerDTO eventOrganizerDTO);

    /**
     * Get all the eventOrganizers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EventOrganizerDTO> findAll(Pageable pageable);

    /**
     * Get the "id" eventOrganizer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EventOrganizerDTO> findOne(Long id);

    /**
     * Delete the "id" eventOrganizer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
