package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.TeamGroupDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.eventsitemanager.domain.TeamGroup}.
 */
public interface TeamGroupService {
    /**
     * Save a teamGroup.
     *
     * @param teamGroupDTO the entity to save.
     * @return the persisted entity.
     */
    TeamGroupDTO save(TeamGroupDTO teamGroupDTO);

    /**
     * Updates a teamGroup.
     *
     * @param teamGroupDTO the entity to update.
     * @return the persisted entity.
     */
    TeamGroupDTO update(TeamGroupDTO teamGroupDTO);

    /**
     * Partially updates a teamGroup.
     *
     * @param teamGroupDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TeamGroupDTO> partialUpdate(TeamGroupDTO teamGroupDTO);

    /**
     * Get the "id" teamGroup.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TeamGroupDTO> findOne(Long id);

    /**
     * Delete the "id" teamGroup.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
