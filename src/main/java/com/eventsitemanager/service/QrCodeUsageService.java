package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.QrCodeUsageDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.eventsitemanager.domain.QrCodeUsage}.
 */
public interface QrCodeUsageService {
    /**
     * Save a qrCodeUsage.
     *
     * @param qrCodeUsageDTO the entity to save.
     * @return the persisted entity.
     */
    QrCodeUsageDTO save(QrCodeUsageDTO qrCodeUsageDTO);

    /**
     * Updates a qrCodeUsage.
     *
     * @param qrCodeUsageDTO the entity to update.
     * @return the persisted entity.
     */
    QrCodeUsageDTO update(QrCodeUsageDTO qrCodeUsageDTO);

    /**
     * Partially updates a qrCodeUsage.
     *
     * @param qrCodeUsageDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<QrCodeUsageDTO> partialUpdate(QrCodeUsageDTO qrCodeUsageDTO);

    /**
     * Get all the qrCodeUsages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<QrCodeUsageDTO> findAll(Pageable pageable);

    /**
     * Get the "id" qrCodeUsage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<QrCodeUsageDTO> findOne(Long id);

    /**
     * Delete the "id" qrCodeUsage.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
