package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.DonationTransaction;
import com.eventsitemanager.domain.EventDetails;
import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.DonationTransactionRepository;
import com.eventsitemanager.repository.EventDetailsRepository;
import com.eventsitemanager.service.DonationTransactionService;
import com.eventsitemanager.service.EmailSenderService;
import com.eventsitemanager.service.QRCodeService;
import com.eventsitemanager.service.dto.DonationTransactionDTO;
import com.eventsitemanager.service.mapper.DonationTransactionMapper;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.eventsitemanager.domain.DonationTransaction}.
 */
@Service
@Transactional
public class DonationTransactionServiceImpl implements DonationTransactionService {

    private static final String ENTITY_NAME = "donationTransaction";

    private final Logger log = LoggerFactory.getLogger(DonationTransactionServiceImpl.class);

    private final DonationTransactionRepository donationTransactionRepository;

    private final DonationTransactionMapper donationTransactionMapper;

    private final QRCodeService qrCodeService;

    private final EmailSenderService emailSenderService;

    private final EventDetailsRepository eventDetailsRepository;

    @Value("${app.email-host-url-prefix:}")
    private String emailHostUrlPrefix;

    public DonationTransactionServiceImpl(
        DonationTransactionRepository donationTransactionRepository,
        DonationTransactionMapper donationTransactionMapper,
        QRCodeService qrCodeService,
        EmailSenderService emailSenderService,
        EventDetailsRepository eventDetailsRepository
    ) {
        this.donationTransactionRepository = donationTransactionRepository;
        this.donationTransactionMapper = donationTransactionMapper;
        this.qrCodeService = qrCodeService;
        this.emailSenderService = emailSenderService;
        this.eventDetailsRepository = eventDetailsRepository;
    }

    @Override
    @CacheEvict(value = "donationTransactions", allEntries = true)
    public DonationTransactionDTO save(DonationTransactionDTO donationTransactionDTO) {
        log.debug("Request to save DonationTransaction : {}", donationTransactionDTO);

        DonationTransaction donationTransaction = donationTransactionMapper.toEntity(donationTransactionDTO);

        // Ensure ID is null for new entities to force sequence generation
        // This prevents duplicate key errors when entity has ID set from DTO
        if (donationTransaction.getId() != null) {
            log.warn(
                "DonationTransaction {} has ID {} set during create operation. Clearing ID to force sequence generation.",
                donationTransaction.getClass().getSimpleName(),
                donationTransaction.getId()
            );
            donationTransaction.setId(null);
        }

        // Set timestamps if not already set
        if (donationTransaction.getCreatedAt() == null) {
            donationTransaction.setCreatedAt(ZonedDateTime.now());
        }
        if (donationTransaction.getUpdatedAt() == null) {
            donationTransaction.setUpdatedAt(ZonedDateTime.now());
        }

        donationTransaction = donationTransactionRepository.save(donationTransaction);
        return donationTransactionMapper.toDto(donationTransaction);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DonationTransactionDTO> findOne(Long id) {
        log.debug("Request to get DonationTransaction : {}", id);
        return donationTransactionRepository.findById(id).map(donationTransactionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DonationTransactionDTO> findByTransactionReference(String transactionReference) {
        log.debug("Request to find DonationTransaction by transactionReference: {}", transactionReference);
        return donationTransactionRepository.findByTransactionReference(transactionReference).map(donationTransactionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DonationTransactionDTO> findByGivebutterDonationId(String givebutterDonationId) {
        log.debug("Request to find DonationTransaction by givebutterDonationId: {}", givebutterDonationId);
        return donationTransactionRepository.findByGivebutterDonationId(givebutterDonationId).map(donationTransactionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DonationTransactionDTO> findByGivebutterDonationIdAndTenantId(String givebutterDonationId, String tenantId) {
        log.debug("Request to find DonationTransaction by givebutterDonationId: {} and tenantId: {}", givebutterDonationId, tenantId);
        return donationTransactionRepository
            .findByGivebutterDonationIdAndTenantId(givebutterDonationId, tenantId)
            .map(donationTransactionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DonationTransactionDTO> findByEventIdAndTenantId(Long eventId, String tenantId) {
        log.debug("Request to find DonationTransactions by eventId: {} and tenantId: {}", eventId, tenantId);
        return donationTransactionRepository
            .findByEventIdAndTenantId(eventId, tenantId)
            .stream()
            .map(donationTransactionMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public String generateQrCode(Long eventId, Long donationTransactionId, String emailHostUrlPrefix) {
        log.debug("Request to generate QR code for donation transaction: {} for event: {}", donationTransactionId, eventId);

        DonationTransaction donation = donationTransactionRepository
            .findById(donationTransactionId)
            .orElseThrow(() -> new BadRequestAlertException("Donation transaction not found", ENTITY_NAME, "idnotfound"));

        if (donation.getEventId() != null && !donation.getEventId().equals(eventId)) {
            throw new BadRequestAlertException("Event ID mismatch", ENTITY_NAME, "eventidmismatch");
        }

        // Generate QR code URL content
        String qrScanUrlContent =
            emailHostUrlPrefix + "/qrcode-scan/donations/events/" + eventId + "/transactions/" + donationTransactionId;

        log.info("QR scan URL content: {}", qrScanUrlContent);

        // Generate and upload QR code
        String qrCodeImageUrl;
        try {
            qrCodeImageUrl =
                qrCodeService.generateAndUploadQRCode(
                    qrScanUrlContent,
                    eventId,
                    String.valueOf(donationTransactionId),
                    donation.getTenantId()
                );
        } catch (java.io.IOException e) {
            log.error("Failed to generate QR code for donation transaction {}: {}", donationTransactionId, e.getMessage(), e);
            throw new BadRequestAlertException("QR code generation failed: " + e.getMessage(), ENTITY_NAME, "qrcodegenerationfailed");
        }

        if (qrCodeImageUrl == null || qrCodeImageUrl.isEmpty()) {
            log.error("QR code generation returned NULL or EMPTY URL for donationTransactionId={}", donationTransactionId);
            throw new BadRequestAlertException("QR code generation failed", ENTITY_NAME, "qrcodegenerationfailed");
        }

        // Update donation transaction with QR code URLs
        donation.setQrCodeUrl(qrScanUrlContent);
        donation.setQrCodeImageUrl(qrCodeImageUrl);
        donation.setUpdatedAt(ZonedDateTime.now());
        donationTransactionRepository.save(donation);

        log.info("QR code generated and saved for donation transaction {}: {}", donationTransactionId, qrCodeImageUrl);
        return qrCodeImageUrl;
    }

    @Override
    public boolean sendConfirmationEmail(Long eventId, Long donationTransactionId, String to) {
        log.debug(
            "Request to send confirmation email for donation transaction: {} for event: {} to: {}",
            donationTransactionId,
            eventId,
            to
        );

        DonationTransaction donation = donationTransactionRepository
            .findById(donationTransactionId)
            .orElseThrow(() -> new BadRequestAlertException("Donation transaction not found", ENTITY_NAME, "idnotfound"));

        if (donation.getEventId() != null && !donation.getEventId().equals(eventId)) {
            throw new BadRequestAlertException("Event ID mismatch", ENTITY_NAME, "eventidmismatch");
        }

        try {
            // Fetch event details if available
            EventDetails event = null;
            if (eventId != null) {
                event = eventDetailsRepository.findById(eventId).orElse(null);
            }

            // Build email subject
            String eventName = event != null ? event.getTitle() : "Event";
            String subject = "Donation Confirmation - " + eventName;

            // Build email body (HTML)
            StringBuilder emailBody = new StringBuilder();
            emailBody.append("<html><body>");
            emailBody.append("<h2>Thank you for your donation!</h2>");
            emailBody.append("<p>Dear ").append(donation.getFirstName() != null ? donation.getFirstName() : "Donor").append(",</p>");
            emailBody.append("<p>We have received your donation of $").append(donation.getAmount()).append(".</p>");

            if (event != null) {
                emailBody.append("<p><strong>Event:</strong> ").append(event.getTitle()).append("</p>");
            }

            if (donation.getPrayerIntention() != null && !donation.getPrayerIntention().isEmpty()) {
                emailBody.append("<p><strong>Prayer Intention:</strong> ").append(donation.getPrayerIntention()).append("</p>");
            }

            if (donation.getQrCodeImageUrl() != null && !donation.getQrCodeImageUrl().isEmpty()) {
                emailBody.append("<p><img src=\"").append(donation.getQrCodeImageUrl()).append("\" alt=\"Donation QR Code\" /></p>");
            }

            emailBody.append("<p>Transaction Reference: ").append(donation.getTransactionReference()).append("</p>");
            emailBody.append("<p>Thank you for your generous support!</p>");
            emailBody.append("</body></html>");

            // Send email
            emailSenderService.sendEmail(to, subject, emailBody.toString(), true);

            // Update email sent status
            donation.setEmailSent(true);
            donation.setUpdatedAt(ZonedDateTime.now());
            donationTransactionRepository.save(donation);

            log.info("Donation confirmation email sent successfully for transaction {} to {}", donationTransactionId, to);
            return true;
        } catch (Exception e) {
            log.error("Error sending donation confirmation email for transaction {}: {}", donationTransactionId, e.getMessage(), e);
            return false;
        }
    }
}
