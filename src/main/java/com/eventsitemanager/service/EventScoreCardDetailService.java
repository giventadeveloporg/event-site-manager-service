package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.EventScoreCardDetailDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.eventsitemanager.domain.EventScoreCardDetail}.
 */
public interface EventScoreCardDetailService {
    /**
     * Save a eventScoreCardDetail.
     *
     * @param eventScoreCardDetailDTO the entity to save.
     * @return the persisted entity.
     */
    EventScoreCardDetailDTO save(EventScoreCardDetailDTO eventScoreCardDetailDTO);

    /**
     * Updates a eventScoreCardDetail.
     *
     * @param eventScoreCardDetailDTO the entity to update.
     * @return the persisted entity.
     */
    EventScoreCardDetailDTO update(EventScoreCardDetailDTO eventScoreCardDetailDTO);

    /**
     * Partially updates a eventScoreCardDetail.
     *
     * @param eventScoreCardDetailDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EventScoreCardDetailDTO> partialUpdate(EventScoreCardDetailDTO eventScoreCardDetailDTO);

    /**
     * Get all the eventScoreCardDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EventScoreCardDetailDTO> findAll(Pageable pageable);

    /**
     * Get the "id" eventScoreCardDetail.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EventScoreCardDetailDTO> findOne(Long id);

    /**
     * Delete the "id" eventScoreCardDetail.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
