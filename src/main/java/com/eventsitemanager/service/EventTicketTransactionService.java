package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.EventTicketTransactionDTO;
import com.eventsitemanager.service.dto.EventTicketTransactionStatisticsDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing
 * {@link com.eventsitemanager.domain.EventTicketTransaction}.
 */
public interface EventTicketTransactionService {
    /**
     * Save a eventTicketTransaction.
     *
     * @param eventTicketTransactionDTO the entity to save.
     * @return the persisted entity.
     */
    EventTicketTransactionDTO save(EventTicketTransactionDTO eventTicketTransactionDTO);

    /**
     * Updates a eventTicketTransaction.
     *
     * @param eventTicketTransactionDTO the entity to update.
     * @return the persisted entity.
     */
    EventTicketTransactionDTO update(EventTicketTransactionDTO eventTicketTransactionDTO);

    /**
     * Partially updates a eventTicketTransaction.
     *
     * @param eventTicketTransactionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EventTicketTransactionDTO> partialUpdate(EventTicketTransactionDTO eventTicketTransactionDTO);

    /**
     * Get all the eventTicketTransactions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EventTicketTransactionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" eventTicketTransaction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EventTicketTransactionDTO> findOne(Long id);

    /**
     * Delete the "id" eventTicketTransaction.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get statistics for an event by eventId.
     *
     * @param eventId the id of the event.
     * @return the statistics DTO.
     */
    EventTicketTransactionStatisticsDTO getEventStatistics(Long eventId);
}
