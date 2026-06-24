package com.eventsitemanager.repository;

import com.eventsitemanager.domain.DonationTransaction;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DonationTransaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DonationTransactionRepository
    extends JpaRepository<DonationTransaction, Long>, JpaSpecificationExecutor<DonationTransaction> {
    /**
     * Find a donation transaction by transaction reference.
     *
     * @param transactionReference the transaction reference
     * @return Optional containing the donation transaction if found
     */
    Optional<DonationTransaction> findByTransactionReference(String transactionReference);

    /**
     * Find a donation transaction by GiveButter donation ID.
     *
     * @param givebutterDonationId the GiveButter donation ID
     * @return Optional containing the donation transaction if found
     */
    Optional<DonationTransaction> findByGivebutterDonationId(String givebutterDonationId);

    /**
     * Find a donation transaction by GiveButter donation ID and tenant ID.
     * This ensures tenant isolation when checking for duplicates.
     *
     * @param givebutterDonationId the GiveButter donation ID
     * @param tenantId the tenant ID
     * @return Optional containing the donation transaction if found for the specific tenant
     */
    Optional<DonationTransaction> findByGivebutterDonationIdAndTenantId(String givebutterDonationId, String tenantId);

    /**
     * Check if a donation transaction exists for a given GiveButter donation ID.
     *
     * @param givebutterDonationId the GiveButter donation ID
     * @return true if a transaction exists for this donation ID
     */
    boolean existsByGivebutterDonationId(String givebutterDonationId);

    /**
     * Find all donation transactions by event ID and tenant ID.
     *
     * @param eventId the event ID
     * @param tenantId the tenant ID
     * @return List of donation transactions for the event
     */
    List<DonationTransaction> findByEventIdAndTenantId(Long eventId, String tenantId);

    /**
     * Find all donation transactions by tenant ID.
     *
     * @param tenantId the tenant ID
     * @return List of donation transactions for the tenant
     */
    List<DonationTransaction> findByTenantId(String tenantId);

    /**
     * Find all donation transactions by payment transaction ID.
     *
     * @param paymentTransactionId the payment transaction ID
     * @return List of donation transactions linked to the payment transaction
     */
    List<DonationTransaction> findByPaymentTransactionId(Long paymentTransactionId);
}
