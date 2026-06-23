package com.eventsitemanager.repository;

import com.eventsitemanager.domain.UserPaymentTransaction;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserPaymentTransaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserPaymentTransactionRepository
    extends JpaRepository<UserPaymentTransaction, Long>, JpaSpecificationExecutor<UserPaymentTransaction> {
    /**
     * Find a transaction by Stripe payment intent ID.
     *
     * @param stripePaymentIntentId the Stripe payment intent ID
     * @return Optional containing the transaction if found
     */
    Optional<UserPaymentTransaction> findByStripePaymentIntentId(String stripePaymentIntentId);

    /**
     * Find transactions by ID using native query to handle potential duplicate primary keys.
     * This is a workaround for database integrity issues where duplicate IDs exist.
     *
     * @param id the transaction ID
     * @return List of transactions with the given ID (should normally be 0 or 1)
     */
    @Query(value = "SELECT * FROM user_payment_transaction WHERE id = :id ORDER BY created_at ASC LIMIT 1", nativeQuery = true)
    Optional<UserPaymentTransaction> findByIdWithDuplicateHandling(@Param("id") Long id);
}
