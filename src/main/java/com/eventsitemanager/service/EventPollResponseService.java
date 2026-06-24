package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.EventPollResponseDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.eventsitemanager.domain.EventPollResponse}.
 */
public interface EventPollResponseService {
    /**
     * Save a eventPollResponse.
     *
     * @param eventPollResponseDTO the entity to save.
     * @return the persisted entity.
     */
    EventPollResponseDTO save(EventPollResponseDTO eventPollResponseDTO);

    /**
     * Updates a eventPollResponse.
     *
     * @param eventPollResponseDTO the entity to update.
     * @return the persisted entity.
     */
    EventPollResponseDTO update(EventPollResponseDTO eventPollResponseDTO);

    /**
     * Partially updates a eventPollResponse.
     *
     * @param eventPollResponseDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EventPollResponseDTO> partialUpdate(EventPollResponseDTO eventPollResponseDTO);

    /**
     * Get all the eventPollResponses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EventPollResponseDTO> findAll(Pageable pageable);

    /**
     * Get the "id" eventPollResponse.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EventPollResponseDTO> findOne(Long id);

    /**
     * Delete the "id" eventPollResponse.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
