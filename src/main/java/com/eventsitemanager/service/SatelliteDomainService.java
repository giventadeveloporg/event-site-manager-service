package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.SatelliteDomainDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.eventsitemanager.domain.SatelliteDomain}.
 */
public interface SatelliteDomainService {
    /**
     * Save a satelliteDomain.
     *
     * @param satelliteDomainDTO the entity to save.
     * @return the persisted entity.
     */
    SatelliteDomainDTO save(SatelliteDomainDTO satelliteDomainDTO);

    /**
     * Updates a satelliteDomain.
     *
     * @param satelliteDomainDTO the entity to update.
     * @return the persisted entity.
     */
    SatelliteDomainDTO update(SatelliteDomainDTO satelliteDomainDTO);

    /**
     * Partially updates a satelliteDomain.
     *
     * @param satelliteDomainDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SatelliteDomainDTO> partialUpdate(SatelliteDomainDTO satelliteDomainDTO);

    /**
     * Get the "id" satelliteDomain.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SatelliteDomainDTO> findOne(Long id);

    /**
     * Delete the "id" satelliteDomain.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
