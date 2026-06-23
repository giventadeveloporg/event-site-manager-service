package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.EventTicketTransactionItemDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing
 * {@link com.eventsitemanager.domain.EventTicketTransactionItem}.
 */
public interface EventTicketTransactionItemService {
    /**
     * Save a eventTicketTransactionItem.
     *
     * @param eventTicketTransactionItemDTO the entity to save.
     * @return the persisted entity.
     */
    EventTicketTransactionItemDTO save(EventTicketTransactionItemDTO eventTicketTransactionItemDTO);

    /**
     * Updates a eventTicketTransactionItem.
     *
     * @param eventTicketTransactionItemDTO the entity to update.
     * @return the persisted entity.
     */
    EventTicketTransactionItemDTO update(EventTicketTransactionItemDTO eventTicketTransactionItemDTO);

    /**
     * Partially updates a eventTicketTransactionItem.
     *
     * @param eventTicketTransactionItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EventTicketTransactionItemDTO> partialUpdate(EventTicketTransactionItemDTO eventTicketTransactionItemDTO);

    /**
     * Get all the eventTicketTransactionItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EventTicketTransactionItemDTO> findAll(Pageable pageable);

    /**
     * Get the "id" eventTicketTransactionItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EventTicketTransactionItemDTO> findOne(Long id);

    /**
     * Delete the "id" eventTicketTransactionItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Save a list of eventTicketTransactionItems.
     *
     * @param eventTicketTransactionItemDTOs the entities to save.
     * @return the list of persisted entities.
     */
    List<EventTicketTransactionItemDTO> saveAll(List<EventTicketTransactionItemDTO> eventTicketTransactionItemDTOs);
}
