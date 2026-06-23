package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.CheckInAnalyticsDTO;
import com.eventsitemanager.service.dto.EventTicketTransactionDTO;
import com.eventsitemanager.service.dto.EventTicketTransactionStatisticsDTO;
import com.eventsitemanager.service.dto.SalesAnalyticsDTO;
import java.time.LocalDate;
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

    /**
     * Get statistics for an event by eventId with optional date range filtering.
     * Task 11: Add date range support to statistics endpoint.
     *
     * @param eventId the id of the event.
     * @param startDate optional start date for filtering (ISO 8601 format: YYYY-MM-DD).
     * @param endDate optional end date for filtering (ISO 8601 format: YYYY-MM-DD).
     * @return the statistics DTO with date range information if provided.
     */
    EventTicketTransactionStatisticsDTO getEventStatistics(Long eventId, LocalDate startDate, LocalDate endDate);

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

    /**
     * Task 9: Get check-in analytics for an event with optional date range filtering.
     * Returns pre-aggregated check-in statistics for better performance.
     *
     * @param eventId the id of the event.
     * @param startDate optional start date for filtering (ISO 8601 format: YYYY-MM-DD).
     * @param endDate optional end date for filtering (ISO 8601 format: YYYY-MM-DD).
     * @return the check-in analytics DTO with aggregated data.
     */
    CheckInAnalyticsDTO getCheckInAnalytics(Long eventId, LocalDate startDate, LocalDate endDate);

    /**
     * Task 10: Get sales analytics for an event with optional date range filtering.
     * Returns pre-aggregated sales statistics for better performance.
     *
     * @param eventId the id of the event.
     * @param startDate optional start date for filtering (ISO 8601 format: YYYY-MM-DD).
     * @param endDate optional end date for filtering (ISO 8601 format: YYYY-MM-DD).
     * @return the sales analytics DTO with aggregated data.
     */
    SalesAnalyticsDTO getSalesAnalytics(Long eventId, LocalDate startDate, LocalDate endDate);
}
