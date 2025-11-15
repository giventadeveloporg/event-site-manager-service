package com.nextjstemplate.repository;

import com.nextjstemplate.domain.EventTicketTransaction;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventTicketTransaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventTicketTransactionRepository
    extends JpaRepository<EventTicketTransaction, Long>, JpaSpecificationExecutor<EventTicketTransaction> {
    /**
     * Find a ticket transaction by Stripe payment intent ID.
     *
     * @param stripePaymentIntentId the Stripe payment intent ID
     * @return Optional containing the ticket transaction if found
     */
    Optional<EventTicketTransaction> findByStripePaymentIntentId(String stripePaymentIntentId);

    /**
     * Find ticket transactions by transaction reference (payment transaction ID).
     * This is used as a fallback when stripePaymentIntentId lookup fails.
     *
     * @param transactionReference the transaction reference (payment transaction ID as string)
     * @return List of ticket transactions matching the transaction reference
     */
    List<EventTicketTransaction> findByTransactionReference(String transactionReference);
}
