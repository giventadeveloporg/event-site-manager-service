package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.EventTicketTransaction;
import com.nextjstemplate.domain.ManualPaymentRequest;
import com.nextjstemplate.domain.enumeration.ManualPaymentStatus;
import com.nextjstemplate.repository.EventTicketTransactionRepository;
import com.nextjstemplate.repository.ManualPaymentRequestRepository;
import com.nextjstemplate.service.ManualPaymentRequestService;
import com.nextjstemplate.service.QRCodeService;
import com.nextjstemplate.service.S3Service;
import com.nextjstemplate.service.dto.ManualPaymentRequestDTO;
import com.nextjstemplate.service.mapper.ManualPaymentRequestMapper;
import jakarta.persistence.EntityNotFoundException;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for managing {@link com.nextjstemplate.domain.ManualPaymentRequest}.
 */
@Service
@Transactional
public class ManualPaymentRequestServiceImpl implements ManualPaymentRequestService {

    private final Logger log = LoggerFactory.getLogger(ManualPaymentRequestServiceImpl.class);

    private final ManualPaymentRequestRepository manualPaymentRequestRepository;
    private final ManualPaymentRequestMapper manualPaymentRequestMapper;
    private final S3Service s3Service;
    private final Environment environment;
    private final EventTicketTransactionRepository eventTicketTransactionRepository;
    private final QRCodeService qrCodeService;

    @Value("${app.email-host-url-prefix:}")
    private String emailHostUrlPrefix;

    public ManualPaymentRequestServiceImpl(
        ManualPaymentRequestRepository manualPaymentRequestRepository,
        ManualPaymentRequestMapper manualPaymentRequestMapper,
        S3Service s3Service,
        Environment environment,
        EventTicketTransactionRepository eventTicketTransactionRepository,
        QRCodeService qrCodeService
    ) {
        this.manualPaymentRequestRepository = manualPaymentRequestRepository;
        this.manualPaymentRequestMapper = manualPaymentRequestMapper;
        this.s3Service = s3Service;
        this.environment = environment;
        this.eventTicketTransactionRepository = eventTicketTransactionRepository;
        this.qrCodeService = qrCodeService;
    }

    @Override
    public ManualPaymentRequestDTO save(ManualPaymentRequestDTO dto) {
        log.debug("Request to save ManualPaymentRequest : {}", dto);
        ManualPaymentRequest entity = manualPaymentRequestMapper.toEntity(dto);

        ZonedDateTime now = ZonedDateTime.now();
        if (entity.getCreatedAt() == null) {
            entity.setCreatedAt(now);
        }
        entity.setUpdatedAt(now);

        if (entity.getStatus() == null) {
            entity.setStatus(ManualPaymentStatus.REQUESTED);
        }

        entity = manualPaymentRequestRepository.save(entity);
        return manualPaymentRequestMapper.toDto(entity);
    }

    @Override
    public Optional<ManualPaymentRequestDTO> partialUpdate(ManualPaymentRequestDTO dto) {
        log.debug("Request to partially update ManualPaymentRequest : {}", dto);

        return manualPaymentRequestRepository
            .findById(dto.getId())
            .map(existing -> {
                manualPaymentRequestMapper.partialUpdate(existing, dto);
                existing.setUpdatedAt(ZonedDateTime.now());
                return existing;
            })
            .map(manualPaymentRequestRepository::save)
            .map(manualPaymentRequestMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ManualPaymentRequestDTO> findAll(Pageable pageable) {
        return manualPaymentRequestRepository.findAll(pageable).map(manualPaymentRequestMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ManualPaymentRequestDTO> findOne(Long id) {
        return manualPaymentRequestRepository.findById(id).map(manualPaymentRequestMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        manualPaymentRequestRepository.deleteById(id);
    }

    @Override
    public ManualPaymentRequestDTO uploadProofOfPayment(Long id, String tenantId, MultipartFile file) {
        ManualPaymentRequest entity = manualPaymentRequestRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Manual payment request not found: " + id));

        if (tenantId != null && entity.getTenantId() != null && !tenantId.equals(entity.getTenantId())) {
            throw new IllegalArgumentException("Manual payment request does not belong to the specified tenant");
        }

        if (entity.getEventId() == null) {
            throw new IllegalArgumentException("eventId is required on manual payment request to upload proof");
        }

        String key = generateProofOfPaymentS3Key(entity.getTenantId(), entity.getEventId(), entity.getId(), file.getOriginalFilename());
        String url = s3Service.uploadFile(key, file);

        entity.setProofOfPaymentFileKey(key);
        entity.setProofOfPaymentFileUrl(url);
        entity.setProofOfPaymentUploadedAt(ZonedDateTime.now());
        entity.setUpdatedAt(ZonedDateTime.now());

        entity = manualPaymentRequestRepository.save(entity);
        return manualPaymentRequestMapper.toDto(entity);
    }

    /**
     * Called by the REST resource when status is updated (Received/Voided/Refunded).
     */
    @Override
    public ManualPaymentRequestDTO updateStatus(
        Long id,
        String tenantId,
        ManualPaymentStatus newStatus,
        String receivedBy,
        String voidReason
    ) {
        ManualPaymentRequest entity = manualPaymentRequestRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Manual payment request not found: " + id));

        if (tenantId != null && entity.getTenantId() != null && !tenantId.equals(entity.getTenantId())) {
            throw new IllegalArgumentException("Manual payment request does not belong to the specified tenant");
        }

        entity.setStatus(newStatus);
        entity.setUpdatedAt(ZonedDateTime.now());

        if (newStatus == ManualPaymentStatus.RECEIVED) {
            entity.setReceivedAt(ZonedDateTime.now());
            entity.setReceivedBy(receivedBy);
            // Ticketing integration (best-effort)
            confirmTicketTransactionIfPresent(entity);
        } else if (newStatus == ManualPaymentStatus.VOIDED || newStatus == ManualPaymentStatus.REFUNDED) {
            entity.setVoidReason(voidReason);
        }

        entity = manualPaymentRequestRepository.save(entity);
        return manualPaymentRequestMapper.toDto(entity);
    }

    private void confirmTicketTransactionIfPresent(ManualPaymentRequest request) {
        try {
            if (request.getTicketTransactionId() == null) {
                return;
            }
            EventTicketTransaction txn = eventTicketTransactionRepository.findById(request.getTicketTransactionId()).orElse(null);
            if (txn == null) {
                log.warn("No EventTicketTransaction found for ticketTransactionId={}", request.getTicketTransactionId());
                return;
            }
            if (txn.getTenantId() != null && request.getTenantId() != null && !request.getTenantId().equals(txn.getTenantId())) {
                log.warn(
                    "Tenant mismatch for manual payment receipt. manualPaymentTenant={}, ticketTransactionTenant={}",
                    request.getTenantId(),
                    txn.getTenantId()
                );
                return;
            }

            if (!"COMPLETED".equalsIgnoreCase(txn.getStatus())) {
                txn.setStatus("COMPLETED");
                txn.setUpdatedAt(ZonedDateTime.now());
            }

            // Generate QR code if missing (best-effort)
            if (txn.getQrCodeImageUrl() == null || txn.getQrCodeImageUrl().isEmpty()) {
                if (emailHostUrlPrefix == null || emailHostUrlPrefix.isEmpty()) {
                    log.warn("emailHostUrlPrefix not configured; skipping QR generation for transaction {}", txn.getId());
                } else {
                    String qrScanUrlContent =
                        emailHostUrlPrefix + "/qrcode-scan/tickets/events/" + txn.getEventId() + "/transactions/" + txn.getId();
                    String qrUrl = qrCodeService.generateAndUploadQRCode(
                        qrScanUrlContent,
                        txn.getEventId(),
                        String.valueOf(txn.getId()),
                        txn.getTenantId()
                    );
                    txn.setQrCodeImageUrl(qrUrl);
                    txn.setUpdatedAt(ZonedDateTime.now());
                }
            }

            eventTicketTransactionRepository.save(txn);
        } catch (Exception e) {
            log.error("Failed to confirm ticket transaction for manual payment request {}: {}", request.getId(), e.getMessage(), e);
        }
    }

    private String generateProofOfPaymentS3Key(String tenantId, Long eventId, Long paymentId, String originalFilename) {
        String profilePrefix = getActiveProfilePrefix();
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String ext = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String safeBase = "proof_of_payment";
        return String.format(
            "%s/events/tenantId/%s/event-id/%d/manual-payments/proof-of-payment/%d/%s_%s_%s%s",
            profilePrefix,
            tenantId,
            eventId,
            paymentId,
            safeBase,
            timestamp,
            uuid,
            ext
        );
    }

    private String getActiveProfilePrefix() {
        String[] activeProfiles = environment.getActiveProfiles();
        if (activeProfiles.length > 0) {
            String profile = activeProfiles[0];
            if ("prod".equalsIgnoreCase(profile) || "production".equalsIgnoreCase(profile)) {
                return "prod";
            }
            return "dev";
        }
        return "dev";
    }
}
