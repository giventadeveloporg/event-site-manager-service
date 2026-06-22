package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.EventAdminDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.eventsitemanager.domain.EventAdmin}.
 */
public interface EventAdminService {
    /**
     * Save a eventAdmin.
     *
     * @param eventAdminDTO the entity to save.
     * @return the persisted entity.
     */
    EventAdminDTO save(EventAdminDTO eventAdminDTO);

    /**
     * Updates a eventAdmin.
     *
     * @param eventAdminDTO the entity to update.
     * @return the persisted entity.
     */
    EventAdminDTO update(EventAdminDTO eventAdminDTO);

    /**
     * Partially updates a eventAdmin.
     *
     * @param eventAdminDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EventAdminDTO> partialUpdate(EventAdminDTO eventAdminDTO);

    /**
     * Get all the eventAdmins.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EventAdminDTO> findAll(Pageable pageable);

    /**
     * Get the "id" eventAdmin.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EventAdminDTO> findOne(Long id);

    /**
     * Delete the "id" eventAdmin.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
