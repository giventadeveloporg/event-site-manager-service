package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.FocusGroupMemberDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing
 * {@link com.nextjstemplate.domain.FocusGroupMember}.
 */
public interface FocusGroupMemberService {
    /**
     * Save a focusGroupMember.
     *
     * @param focusGroupMemberDTO the entity to save.
     * @return the persisted entity.
     */
    FocusGroupMemberDTO save(FocusGroupMemberDTO focusGroupMemberDTO);

    /**
     * Updates a focusGroupMember.
     *
     * @param focusGroupMemberDTO the entity to update.
     * @return the persisted entity.
     */
    FocusGroupMemberDTO update(FocusGroupMemberDTO focusGroupMemberDTO);

    /**
     * Partially updates a focusGroupMember.
     *
     * @param focusGroupMemberDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FocusGroupMemberDTO> partialUpdate(FocusGroupMemberDTO focusGroupMemberDTO);

    /**
     * Get all the focusGroupMembers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FocusGroupMemberDTO> findAll(Pageable pageable);

    /**
     * Get the "id" focusGroupMember.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FocusGroupMemberDTO> findOne(Long id);

    /**
     * Delete the "id" focusGroupMember.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
