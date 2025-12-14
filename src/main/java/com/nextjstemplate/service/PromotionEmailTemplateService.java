package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.PromotionEmailTemplateDTO;
import com.nextjstemplate.service.dto.PromotionEmailTemplateFormDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.nextjstemplate.domain.PromotionEmailTemplate}.
 */
public interface PromotionEmailTemplateService {
    /**
     * Create a new template.
     *
     * @param formDTO the form DTO containing template data
     * @param tenantId the tenant ID
     * @param userId the user ID creating the template
     * @return the persisted entity
     */
    PromotionEmailTemplateDTO createTemplate(PromotionEmailTemplateFormDTO formDTO, String tenantId, Long userId);

    /**
     * Update an existing template.
     *
     * @param id the id of the template
     * @param formDTO the form DTO containing updated template data
     * @param tenantId the tenant ID
     * @return the persisted entity
     */
    PromotionEmailTemplateDTO updateTemplate(Long id, PromotionEmailTemplateFormDTO formDTO, String tenantId);

    /**
     * Partially update a template.
     *
     * @param id the id of the template
     * @param templateDTO the DTO containing partial updates
     * @return the persisted entity
     */
    Optional<PromotionEmailTemplateDTO> partialUpdate(Long id, PromotionEmailTemplateDTO templateDTO);

    /**
     * Get all templates with pagination and filtering.
     *
     * @param pageable the pagination information
     * @param tenantId the tenant ID
     * @return the list of entities
     */
    Page<PromotionEmailTemplateDTO> findAll(Pageable pageable, String tenantId);

    /**
     * Get the "id" template.
     *
     * @param id the id of the entity
     * @param tenantId the tenant ID
     * @return the entity
     */
    Optional<PromotionEmailTemplateDTO> findOne(Long id, String tenantId);

    /**
     * Delete (soft delete) the "id" template by setting isActive=false.
     *
     * @param id the id of the entity
     * @param tenantId the tenant ID
     */
    void delete(Long id, String tenantId);

    /**
     * Validate template name uniqueness per event per tenant.
     *
     * @param templateName the template name to validate
     * @param eventId the event ID
     * @param tenantId the tenant ID
     * @param excludeId optional template ID to exclude from uniqueness check (for updates)
     * @return true if name is unique, false otherwise
     */
    boolean validateTemplateName(String templateName, Long eventId, String tenantId, Long excludeId);
}

