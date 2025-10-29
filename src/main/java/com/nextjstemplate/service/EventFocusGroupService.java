package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.EventFocusGroupDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing
 * {@link com.nextjstemplate.domain.EventFocusGroup}.
 */
public interface EventFocusGroupService {
    /**
     * Save an eventFocusGroup.
     *
     * @param eventFocusGroupDTO the entity to save.
     * @return the persisted entity.
     */
    EventFocusGroupDTO save(EventFocusGroupDTO eventFocusGroupDTO);

    /**
     * Updates an eventFocusGroup.
     *
     * @param eventFocusGroupDTO the entity to update.
     * @return the persisted entity.
     */
    EventFocusGroupDTO update(EventFocusGroupDTO eventFocusGroupDTO);

    /**
     * Partially updates an eventFocusGroup.
     *
     * @param eventFocusGroupDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EventFocusGroupDTO> partialUpdate(EventFocusGroupDTO eventFocusGroupDTO);

    /**
     * Get all the eventFocusGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EventFocusGroupDTO> findAll(Pageable pageable);

    /**
     * Get the "id" eventFocusGroup.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EventFocusGroupDTO> findOne(Long id);

    /**
     * Delete the "id" eventFocusGroup.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
