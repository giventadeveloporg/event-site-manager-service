package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.EventGuestPricingDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.eventsitemanager.domain.EventGuestPricing}.
 */
public interface EventGuestPricingService {
    /**
     * Save a eventGuestPricing.
     *
     * @param eventGuestPricingDTO the entity to save.
     * @return the persisted entity.
     */
    EventGuestPricingDTO save(EventGuestPricingDTO eventGuestPricingDTO);

    /**
     * Updates a eventGuestPricing.
     *
     * @param eventGuestPricingDTO the entity to update.
     * @return the persisted entity.
     */
    EventGuestPricingDTO update(EventGuestPricingDTO eventGuestPricingDTO);

    /**
     * Partially updates a eventGuestPricing.
     *
     * @param eventGuestPricingDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EventGuestPricingDTO> partialUpdate(EventGuestPricingDTO eventGuestPricingDTO);

    /**
     * Get all the eventGuestPricings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EventGuestPricingDTO> findAll(Pageable pageable);

    /**
     * Get the "id" eventGuestPricing.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EventGuestPricingDTO> findOne(Long id);

    /**
     * Delete the "id" eventGuestPricing.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
