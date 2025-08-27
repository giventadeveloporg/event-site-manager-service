package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.UserTaskDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.nextjstemplate.domain.UserTask}.
 */
public interface UserTaskService {
    /**
     * Save a userTask.
     *
     * @param userTaskDTO the entity to save.
     * @return the persisted entity.
     */
    UserTaskDTO save(UserTaskDTO userTaskDTO);

    /**
     * Updates a userTask.
     *
     * @param userTaskDTO the entity to update.
     * @return the persisted entity.
     */
    UserTaskDTO update(UserTaskDTO userTaskDTO);

    /**
     * Partially updates a userTask.
     *
     * @param userTaskDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserTaskDTO> partialUpdate(UserTaskDTO userTaskDTO);

    /**
     * Get the "id" userTask.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserTaskDTO> findOne(Long id);

    /**
     * Delete the "id" userTask.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
