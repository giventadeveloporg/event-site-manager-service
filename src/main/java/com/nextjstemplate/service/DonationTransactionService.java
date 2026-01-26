package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.DonationTransactionDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.nextjstemplate.domain.DonationTransaction}.
 */
public interface DonationTransactionService {
    /**
     * Save a donationTransaction.
     *
     * @param donationTransactionDTO the entity to save.
     * @return the persisted entity.
     */
    DonationTransactionDTO save(DonationTransactionDTO donationTransactionDTO);

    /**
     * Get the "id" donationTransaction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DonationTransactionDTO> findOne(Long id);

    /**
     * Find a donation transaction by transaction reference.
     *
     * @param transactionReference the transaction reference
     * @return the entity if found
     */
    Optional<DonationTransactionDTO> findByTransactionReference(String transactionReference);

    /**
     * Find a donation transaction by GiveButter donation ID.
     *
     * @param givebutterDonationId the GiveButter donation ID
     * @return the entity if found
     */
    Optional<DonationTransactionDTO> findByGivebutterDonationId(String givebutterDonationId);

    /**
     * Find a donation transaction by GiveButter donation ID and tenant ID.
     *
     * @param givebutterDonationId the GiveButter donation ID
     * @param tenantId the tenant ID
     * @return the entity if found for the specific tenant
     */
    Optional<DonationTransactionDTO> findByGivebutterDonationIdAndTenantId(String givebutterDonationId, String tenantId);

    /**
     * Find all donation transactions by event ID and tenant ID.
     *
     * @param eventId the event ID
     * @param tenantId the tenant ID
     * @return List of donation transactions for the event
     */
    List<DonationTransactionDTO> findByEventIdAndTenantId(Long eventId, String tenantId);

    /**
     * Generate QR code for a donation transaction.
     *
     * @param eventId the event ID
     * @param donationTransactionId the donation transaction ID
     * @param emailHostUrlPrefix the email host URL prefix
     * @return the QR code image URL
     */
    String generateQrCode(Long eventId, Long donationTransactionId, String emailHostUrlPrefix);

    /**
     * Send confirmation email for a donation transaction.
     *
     * @param eventId the event ID
     * @param donationTransactionId the donation transaction ID
     * @param to the recipient email address
     * @return true if email was sent successfully
     */
    boolean sendConfirmationEmail(Long eventId, Long donationTransactionId, String to);
}
