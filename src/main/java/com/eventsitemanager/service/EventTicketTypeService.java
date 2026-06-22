package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.EventTicketTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.eventsitemanager.domain.EventTicketType}.
 */
public interface EventTicketTypeService {
    /**
     * Save a eventTicketType.
     *
     * @param eventTicketTypeDTO the entity to save.
     * @return the persisted entity.
     */
    EventTicketTypeDTO save(EventTicketTypeDTO eventTicketTypeDTO);

    /**
     * Updates a eventTicketType.
     *
     * @param eventTicketTypeDTO the entity to update.
     * @return the persisted entity.
     */
    EventTicketTypeDTO update(EventTicketTypeDTO eventTicketTypeDTO);

    /**
     * Partially updates a eventTicketType.
     *
     * @param eventTicketTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EventTicketTypeDTO> partialUpdate(EventTicketTypeDTO eventTicketTypeDTO);

    /**
     * Get all the eventTicketTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EventTicketTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" eventTicketType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EventTicketTypeDTO> findOne(Long id);

    /**
     * Delete the "id" eventTicketType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
