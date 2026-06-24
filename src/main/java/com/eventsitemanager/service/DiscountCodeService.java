package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.DiscountCodeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.eventsitemanager.domain.DiscountCode}.
 */
public interface DiscountCodeService {
    /**
     * Save a discountCode.
     *
     * @param discountCodeDTO the entity to save.
     * @return the persisted entity.
     */
    DiscountCodeDTO save(DiscountCodeDTO discountCodeDTO);

    /**
     * Updates a discountCode.
     *
     * @param discountCodeDTO the entity to update.
     * @return the persisted entity.
     */
    DiscountCodeDTO update(DiscountCodeDTO discountCodeDTO);

    /**
     * Partially updates a discountCode.
     *
     * @param discountCodeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DiscountCodeDTO> partialUpdate(DiscountCodeDTO discountCodeDTO);

    /**
     * Get all the discountCodes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DiscountCodeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" discountCode.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DiscountCodeDTO> findOne(Long id);

    /**
     * Delete the "id" discountCode.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
