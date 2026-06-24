package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.UserRegistrationRequestDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.eventsitemanager.domain.UserRegistrationRequest}.
 */
public interface UserRegistrationRequestService {
    /**
     * Save a userRegistrationRequest.
     *
     * @param userRegistrationRequestDTO the entity to save.
     * @return the persisted entity.
     */
    UserRegistrationRequestDTO save(UserRegistrationRequestDTO userRegistrationRequestDTO);

    /**
     * Updates a userRegistrationRequest.
     *
     * @param userRegistrationRequestDTO the entity to update.
     * @return the persisted entity.
     */
    UserRegistrationRequestDTO update(UserRegistrationRequestDTO userRegistrationRequestDTO);

    /**
     * Partially updates a userRegistrationRequest.
     *
     * @param userRegistrationRequestDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserRegistrationRequestDTO> partialUpdate(UserRegistrationRequestDTO userRegistrationRequestDTO);

    /**
     * Get all the userRegistrationRequests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserRegistrationRequestDTO> findAll(Pageable pageable);

    /**
     * Get the "id" userRegistrationRequest.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserRegistrationRequestDTO> findOne(Long id);

    /**
     * Delete the "id" userRegistrationRequest.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
