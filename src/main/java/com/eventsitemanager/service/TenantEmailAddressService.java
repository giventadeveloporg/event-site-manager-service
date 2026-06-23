package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.TenantEmailAddressDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.eventsitemanager.domain.TenantEmailAddress}.
 */
public interface TenantEmailAddressService {
    /**
     * Save a tenantEmailAddress.
     *
     * @param tenantEmailAddressDTO the entity to save.
     * @return the persisted entity.
     */
    TenantEmailAddressDTO save(TenantEmailAddressDTO tenantEmailAddressDTO);

    /**
     * Updates a tenantEmailAddress.
     *
     * @param tenantEmailAddressDTO the entity to update.
     * @return the persisted entity.
     */
    TenantEmailAddressDTO update(TenantEmailAddressDTO tenantEmailAddressDTO);

    /**
     * Partially updates a tenantEmailAddress.
     *
     * @param tenantEmailAddressDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TenantEmailAddressDTO> partialUpdate(TenantEmailAddressDTO tenantEmailAddressDTO);

    /**
     * Get all the tenantEmailAddresses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TenantEmailAddressDTO> findAll(Pageable pageable);

    /**
     * Get the "id" tenantEmailAddress.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TenantEmailAddressDTO> findOne(Long id);

    /**
     * Delete the "id" tenantEmailAddress.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all tenant email addresses for a tenant.
     *
     * @param tenantId the tenant ID.
     * @return the list of entities.
     */
    List<TenantEmailAddressDTO> findByTenantId(String tenantId);

    /**
     * Get active tenant email addresses for a tenant.
     *
     * @param tenantId the tenant ID.
     * @return the list of entities.
     */
    List<TenantEmailAddressDTO> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
