package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.TenantSettingsDTO;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Interface for managing {@link com.nextjstemplate.domain.TenantSettings}.
 */
public interface TenantSettingsService {
    /**
     * Save a tenantSettings.
     *
     * @param tenantSettingsDTO the entity to save.
     * @return the persisted entity.
     */
    TenantSettingsDTO save(TenantSettingsDTO tenantSettingsDTO);

    /**
     * Updates a tenantSettings.
     *
     * @param tenantSettingsDTO the entity to update.
     * @return the persisted entity.
     */
    TenantSettingsDTO update(TenantSettingsDTO tenantSettingsDTO);

    /**
     * Partially updates a tenantSettings.
     *
     * @param tenantSettingsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TenantSettingsDTO> partialUpdate(TenantSettingsDTO tenantSettingsDTO);

    /**
     * Get the "id" tenantSettings.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TenantSettingsDTO> findOne(Long id);

    /**
     * Delete the "id" tenantSettings.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Upload email footer HTML file and update tenant settings.
     *
     * @param tenantId the tenant ID
     * @param file the HTML file to upload
     * @return the updated tenant settings DTO
     */
    TenantSettingsDTO uploadEmailFooterHtml(String tenantId, MultipartFile file);

    /**
     * Upload tenant logo image and update tenant settings.
     *
     * @param tenantId the tenant ID
     * @param file the image file to upload
     * @return the updated tenant settings DTO
     */
    TenantSettingsDTO uploadTenantLogo(String tenantId, MultipartFile file);
}
