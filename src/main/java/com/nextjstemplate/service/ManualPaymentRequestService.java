package com.nextjstemplate.service;

import com.nextjstemplate.domain.enumeration.ManualPaymentStatus;
import com.nextjstemplate.service.dto.ManualPaymentRequestDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Interface for managing {@link com.nextjstemplate.domain.ManualPaymentRequest}.
 */
public interface ManualPaymentRequestService {
    ManualPaymentRequestDTO save(ManualPaymentRequestDTO manualPaymentRequestDTO);

    Optional<ManualPaymentRequestDTO> partialUpdate(ManualPaymentRequestDTO manualPaymentRequestDTO);

    Page<ManualPaymentRequestDTO> findAll(Pageable pageable);

    Optional<ManualPaymentRequestDTO> findOne(Long id);

    void delete(Long id);

    ManualPaymentRequestDTO uploadProofOfPayment(Long id, String tenantId, MultipartFile file);

    ManualPaymentRequestDTO updateStatus(Long id, String tenantId, ManualPaymentStatus newStatus, String receivedBy, String voidReason);
}
