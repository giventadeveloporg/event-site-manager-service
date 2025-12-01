package com.nextjstemplate.repository;

import com.nextjstemplate.domain.EventTicketTransactionItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventTicketTransactionItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventTicketTransactionItemRepository
    extends JpaRepository<EventTicketTransactionItem, Long>, JpaSpecificationExecutor<EventTicketTransactionItem> {
    /**
     * Find items by transactionId.
     *
     * @param transactionId the transaction ID
     * @return List of transaction items for the transaction
     */
    List<EventTicketTransactionItem> findByTransactionId(Long transactionId);

    /**
     * Find an item by transactionId, ticketTypeId, and tenantId.
     * This is used for idempotency checks to prevent duplicate items when both
     * frontend API calls and backend webhooks try to create items simultaneously.
     *
     * @param transactionId the transaction ID
     * @param ticketTypeId the ticket type ID
     * @param tenantId the tenant ID
     * @return Optional containing the transaction item if found
     */
    Optional<EventTicketTransactionItem> findByTransactionIdAndTicketTypeIdAndTenantId(
        Long transactionId,
        Long ticketTypeId,
        String tenantId
    );

    /**
     * Find items by transactionId and tenantId.
     * This ensures tenant isolation when querying items for a transaction.
     *
     * @param transactionId the transaction ID
     * @param tenantId the tenant ID
     * @return List of transaction items for the transaction and tenant
     */
    List<EventTicketTransactionItem> findByTransactionIdAndTenantId(Long transactionId, String tenantId);
}
