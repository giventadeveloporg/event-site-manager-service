package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.FocusGroupDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.nextjstemplate.domain.FocusGroup}.
 */
public interface FocusGroupService {
    /**
     * Save a focusGroup.
     *
     * @param focusGroupDTO the entity to save.
     * @return the persisted entity.
     */
    FocusGroupDTO save(FocusGroupDTO focusGroupDTO);

    /**
     * Updates a focusGroup.
     *
     * @param focusGroupDTO the entity to update.
     * @return the persisted entity.
     */
    FocusGroupDTO update(FocusGroupDTO focusGroupDTO);

    /**
     * Partially updates a focusGroup.
     *
     * @param focusGroupDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FocusGroupDTO> partialUpdate(FocusGroupDTO focusGroupDTO);

    /**
     * Get all the focusGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FocusGroupDTO> findAll(Pageable pageable);

    /**
     * Get the "id" focusGroup.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FocusGroupDTO> findOne(Long id);

    /**
     * Get the focusGroup by slug and tenant ID.
     *
     * @param tenantId the tenant ID
     * @param slug     the slug to search for
     * @return the entity.
     */
    Optional<FocusGroupDTO> findByTenantIdAndSlug(String tenantId, String slug);

    /**
     * Delete the "id" focusGroup.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
