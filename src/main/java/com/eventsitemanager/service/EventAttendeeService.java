package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.EventAttendeeDTO;
import com.eventsitemanager.service.dto.EventAttendeeSaveResult;
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
     * Create or update an event attendee for the registration flow.
     * If an attendee already exists for the same event + email + tenant, updates that record
     * and returns {@code created = false}; otherwise creates a new attendee and returns {@code created = true}.
     * Email matching is case-insensitive.
     *
     * @param eventAttendeeDTO the registration data (id must be null for create; eventId, email, tenantId required for lookup).
     * @return the persisted DTO and whether it was created (true) or updated (false).
     */
    EventAttendeeSaveResult createOrUpdateRegistration(EventAttendeeDTO eventAttendeeDTO);

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
