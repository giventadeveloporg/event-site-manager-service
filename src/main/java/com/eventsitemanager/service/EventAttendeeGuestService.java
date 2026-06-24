package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.EventAttendeeGuestDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.eventsitemanager.domain.EventAttendeeGuest}.
 */
public interface EventAttendeeGuestService {
    /**
     * Save a eventAttendeeGuest.
     *
     * @param eventAttendeeGuestDTO the entity to save.
     * @return the persisted entity.
     */
    EventAttendeeGuestDTO save(EventAttendeeGuestDTO eventAttendeeGuestDTO);

    /**
     * Updates a eventAttendeeGuest.
     *
     * @param eventAttendeeGuestDTO the entity to update.
     * @return the persisted entity.
     */
    EventAttendeeGuestDTO update(EventAttendeeGuestDTO eventAttendeeGuestDTO);

    /**
     * Partially updates a eventAttendeeGuest.
     *
     * @param eventAttendeeGuestDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EventAttendeeGuestDTO> partialUpdate(EventAttendeeGuestDTO eventAttendeeGuestDTO);

    /**
     * Get all the eventAttendeeGuests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EventAttendeeGuestDTO> findAll(Pageable pageable);

    /**
     * Get the "id" eventAttendeeGuest.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EventAttendeeGuestDTO> findOne(Long id);

    /**
     * Delete the "id" eventAttendeeGuest.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
