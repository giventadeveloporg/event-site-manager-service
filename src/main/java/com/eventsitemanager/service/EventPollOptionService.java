package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.EventPollOptionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.eventsitemanager.domain.EventPollOption}.
 */
public interface EventPollOptionService {
    /**
     * Save a eventPollOption.
     *
     * @param eventPollOptionDTO the entity to save.
     * @return the persisted entity.
     */
    EventPollOptionDTO save(EventPollOptionDTO eventPollOptionDTO);

    /**
     * Updates a eventPollOption.
     *
     * @param eventPollOptionDTO the entity to update.
     * @return the persisted entity.
     */
    EventPollOptionDTO update(EventPollOptionDTO eventPollOptionDTO);

    /**
     * Partially updates a eventPollOption.
     *
     * @param eventPollOptionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EventPollOptionDTO> partialUpdate(EventPollOptionDTO eventPollOptionDTO);

    /**
     * Get all the eventPollOptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EventPollOptionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" eventPollOption.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EventPollOptionDTO> findOne(Long id);

    /**
     * Delete the "id" eventPollOption.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
