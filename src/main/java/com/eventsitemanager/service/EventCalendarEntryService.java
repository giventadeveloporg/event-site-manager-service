package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.EventCalendarEntryDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.eventsitemanager.domain.EventCalendarEntry}.
 */
public interface EventCalendarEntryService {
    /**
     * Save a eventCalendarEntry.
     *
     * @param eventCalendarEntryDTO the entity to save.
     * @return the persisted entity.
     */
    EventCalendarEntryDTO save(EventCalendarEntryDTO eventCalendarEntryDTO);

    /**
     * Updates a eventCalendarEntry.
     *
     * @param eventCalendarEntryDTO the entity to update.
     * @return the persisted entity.
     */
    EventCalendarEntryDTO update(EventCalendarEntryDTO eventCalendarEntryDTO);

    /**
     * Partially updates a eventCalendarEntry.
     *
     * @param eventCalendarEntryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EventCalendarEntryDTO> partialUpdate(EventCalendarEntryDTO eventCalendarEntryDTO);

    /**
     * Get all the eventCalendarEntries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EventCalendarEntryDTO> findAll(Pageable pageable);

    /**
     * Get the "id" eventCalendarEntry.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EventCalendarEntryDTO> findOne(Long id);

    /**
     * Delete the "id" eventCalendarEntry.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
