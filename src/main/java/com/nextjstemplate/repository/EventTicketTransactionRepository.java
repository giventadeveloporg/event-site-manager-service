package com.nextjstemplate.repository;

import com.nextjstemplate.domain.EventTicketTransaction;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
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
     * Find a ticket transaction by Stripe payment intent ID and tenant ID.
     * This ensures tenant isolation when checking for duplicates.
     *
     * @param stripePaymentIntentId the Stripe payment intent ID
     * @param tenantId the tenant ID
     * @return Optional containing the ticket transaction if found for the specific tenant
     */
    Optional<EventTicketTransaction> findByStripePaymentIntentIdAndTenantId(String stripePaymentIntentId, String tenantId);

    /**
     * Check if a ticket transaction exists for a given Stripe payment intent ID.
     * This is a global check across all tenants to prevent any duplicates.
     *
     * @param stripePaymentIntentId the Stripe payment intent ID
     * @return true if a transaction exists for this payment intent
     */
    boolean existsByStripePaymentIntentId(String stripePaymentIntentId);

    /**
     * Find all ticket transactions by Stripe payment intent ID.
     * Used for debugging and detecting cross-tenant duplicates.
     *
     * @param stripePaymentIntentId the Stripe payment intent ID
     * @return List of all ticket transactions with this payment intent ID
     */
    List<EventTicketTransaction> findAllByStripePaymentIntentId(String stripePaymentIntentId);

    /**
     * Find ticket transactions by transaction reference (payment transaction ID).
     * This is used as a fallback when stripePaymentIntentId lookup fails.
     *
     * @param transactionReference the transaction reference (payment transaction ID as string)
     * @return List of ticket transactions matching the transaction reference
     */
    List<EventTicketTransaction> findByTransactionReference(String transactionReference);
}
