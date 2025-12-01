package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.EventTicketTransactionDTO;
import com.nextjstemplate.service.dto.EventTicketTransactionStatisticsDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing
 * {@link com.nextjstemplate.domain.EventTicketTransaction}.
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

    /**
     * Find a transaction by Stripe payment intent ID.
     * Used for idempotent transaction creation to prevent duplicates.
     *
     * @param stripePaymentIntentId the Stripe payment intent ID.
     * @return the entity if found.
     */
    Optional<EventTicketTransactionDTO> findByStripePaymentIntentId(String stripePaymentIntentId);

    /**
     * Find a transaction by Stripe payment intent ID and tenant ID.
     * Used for idempotent transaction creation to prevent duplicates with tenant isolation.
     * CRITICAL: This ensures tenant isolation - same payment intent can exist for different tenants.
     *
     * @param stripePaymentIntentId the Stripe payment intent ID.
     * @param tenantId the tenant ID.
     * @return the entity if found for the specific tenant.
     */
    Optional<EventTicketTransactionDTO> findByStripePaymentIntentIdAndTenantId(String stripePaymentIntentId, String tenantId);
}
