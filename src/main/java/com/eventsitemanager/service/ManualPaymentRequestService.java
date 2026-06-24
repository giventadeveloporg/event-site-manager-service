package com.eventsitemanager.service;

import com.eventsitemanager.domain.enumeration.ManualPaymentStatus;
import com.eventsitemanager.service.dto.ManualPaymentRequestDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Interface for managing {@link com.eventsitemanager.domain.ManualPaymentRequest}.
 */
public interface ManualPaymentRequestService {
    ManualPaymentRequestDTO save(ManualPaymentRequestDTO manualPaymentRequestDTO);

    Optional<ManualPaymentRequestDTO> partialUpdate(ManualPaymentRequestDTO manualPaymentRequestDTO);

    Page<ManualPaymentRequestDTO> findAll(Pageable pageable);

    Optional<ManualPaymentRequestDTO> findOne(Long id);

    void delete(Long id);

    ManualPaymentRequestDTO uploadProofOfPayment(Long id, String tenantId, MultipartFile file);

    ManualPaymentRequestDTO updateStatus(Long id, String tenantId, ManualPaymentStatus newStatus, String receivedBy, String voidReason);

    /**
     * Find manual payment request with nested ticket transaction, items, and event details.
     */
    Optional<ManualPaymentRequestDTO> findOneWithDetails(Long id);

    /**
     * Trigger confirmation email batch job for manual payment request.
     */
    void triggerConfirmationEmail(Long paymentRequestId);
}
