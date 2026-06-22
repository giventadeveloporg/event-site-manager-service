package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.EventPollDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.eventsitemanager.domain.EventPoll}.
 */
public interface EventPollService {
    /**
     * Save a eventPoll.
     *
     * @param eventPollDTO the entity to save.
     * @return the persisted entity.
     */
    EventPollDTO save(EventPollDTO eventPollDTO);

    /**
     * Updates a eventPoll.
     *
     * @param eventPollDTO the entity to update.
     * @return the persisted entity.
     */
    EventPollDTO update(EventPollDTO eventPollDTO);

    /**
     * Partially updates a eventPoll.
     *
     * @param eventPollDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EventPollDTO> partialUpdate(EventPollDTO eventPollDTO);

    /**
     * Get all the eventPolls.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EventPollDTO> findAll(Pageable pageable);

    /**
     * Get the "id" eventPoll.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EventPollDTO> findOne(Long id);

    /**
     * Delete the "id" eventPoll.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
