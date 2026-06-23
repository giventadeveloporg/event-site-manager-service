package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.EventScoreCardDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.eventsitemanager.domain.EventScoreCard}.
 */
public interface EventScoreCardService {
    /**
     * Save a eventScoreCard.
     *
     * @param eventScoreCardDTO the entity to save.
     * @return the persisted entity.
     */
    EventScoreCardDTO save(EventScoreCardDTO eventScoreCardDTO);

    /**
     * Updates a eventScoreCard.
     *
     * @param eventScoreCardDTO the entity to update.
     * @return the persisted entity.
     */
    EventScoreCardDTO update(EventScoreCardDTO eventScoreCardDTO);

    /**
     * Partially updates a eventScoreCard.
     *
     * @param eventScoreCardDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EventScoreCardDTO> partialUpdate(EventScoreCardDTO eventScoreCardDTO);

    /**
     * Get all the eventScoreCards.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EventScoreCardDTO> findAll(Pageable pageable);

    /**
     * Get the "id" eventScoreCard.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EventScoreCardDTO> findOne(Long id);

    /**
     * Delete the "id" eventScoreCard.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
