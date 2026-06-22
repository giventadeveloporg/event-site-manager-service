package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.EventAttendeeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.eventsitemanager.domain.EventAttendee}.
 */
public interface EventAttendeeService {
    /**
     * Save a eventAttendee.
     *
     * @param eventAttendeeDTO the entity to save.
     * @return the persisted entity.
     */
    EventAttendeeDTO save(EventAttendeeDTO eventAttendeeDTO);

    /**
     * Updates a eventAttendee.
     *
     * @param eventAttendeeDTO the entity to update.
     * @return the persisted entity.
     */
    EventAttendeeDTO update(EventAttendeeDTO eventAttendeeDTO);

    /**
     * Partially updates a eventAttendee.
     *
     * @param eventAttendeeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EventAttendeeDTO> partialUpdate(EventAttendeeDTO eventAttendeeDTO);

    /**
     * Get all the eventAttendees.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EventAttendeeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" eventAttendee.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EventAttendeeDTO> findOne(Long id);

    /**
     * Delete the "id" eventAttendee.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
