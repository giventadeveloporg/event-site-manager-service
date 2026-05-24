package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.TeamMemberDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.nextjstemplate.domain.TeamMember}.
 */
public interface TeamMemberService {
    /**
     * Save a teamMember.
     *
     * @param teamMemberDTO the entity to save.
     * @return the persisted entity.
     */
    TeamMemberDTO save(TeamMemberDTO teamMemberDTO);

    /**
     * Updates a teamMember.
     *
     * @param teamMemberDTO the entity to update.
     * @return the persisted entity.
     */
    TeamMemberDTO update(TeamMemberDTO teamMemberDTO);

    /**
     * Partially updates a teamMember.
     *
     * @param teamMemberDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TeamMemberDTO> partialUpdate(TeamMemberDTO teamMemberDTO);

    /**
     * Get the "id" teamMember.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TeamMemberDTO> findOne(Long id);

    /**
     * Delete the "id" teamMember.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
