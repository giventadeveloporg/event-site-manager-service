package com.nextjstemplate.repository;

import com.nextjstemplate.domain.UserPaymentTransaction;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
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
}
