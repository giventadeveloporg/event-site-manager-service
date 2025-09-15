package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.EventLiveUpdateAttachmentDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.nextjstemplate.domain.EventLiveUpdateAttachment}.
 */
public interface EventLiveUpdateAttachmentService {
    /**
     * Save a eventLiveUpdateAttachment.
     *
     * @param eventLiveUpdateAttachmentDTO the entity to save.
     * @return the persisted entity.
     */
    EventLiveUpdateAttachmentDTO save(EventLiveUpdateAttachmentDTO eventLiveUpdateAttachmentDTO);

    /**
     * Updates a eventLiveUpdateAttachment.
     *
     * @param eventLiveUpdateAttachmentDTO the entity to update.
     * @return the persisted entity.
     */
    EventLiveUpdateAttachmentDTO update(EventLiveUpdateAttachmentDTO eventLiveUpdateAttachmentDTO);

    /**
     * Partially updates a eventLiveUpdateAttachment.
     *
     * @param eventLiveUpdateAttachmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EventLiveUpdateAttachmentDTO> partialUpdate(EventLiveUpdateAttachmentDTO eventLiveUpdateAttachmentDTO);

    /**
     * Get the "id" eventLiveUpdateAttachment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EventLiveUpdateAttachmentDTO> findOne(Long id);

    /**
     * Delete the "id" eventLiveUpdateAttachment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
