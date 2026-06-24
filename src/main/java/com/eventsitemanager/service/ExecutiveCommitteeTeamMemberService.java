package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.ExecutiveCommitteeTeamMemberDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.eventsitemanager.domain.ExecutiveCommitteeTeamMember}.
 */
public interface ExecutiveCommitteeTeamMemberService {
    /**
     * Save a executiveCommitteeTeamMember.
     *
     * @param executiveCommitteeTeamMemberDTO the entity to save.
     * @return the persisted entity.
     */
    ExecutiveCommitteeTeamMemberDTO save(ExecutiveCommitteeTeamMemberDTO executiveCommitteeTeamMemberDTO);

    /**
     * Updates a executiveCommitteeTeamMember.
     *
     * @param executiveCommitteeTeamMemberDTO the entity to update.
     * @return the persisted entity.
     */
    ExecutiveCommitteeTeamMemberDTO update(ExecutiveCommitteeTeamMemberDTO executiveCommitteeTeamMemberDTO);

    /**
     * Partially updates a executiveCommitteeTeamMember.
     *
     * @param executiveCommitteeTeamMemberDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ExecutiveCommitteeTeamMemberDTO> partialUpdate(ExecutiveCommitteeTeamMemberDTO executiveCommitteeTeamMemberDTO);

    /**
     * Get the "id" executiveCommitteeTeamMember.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExecutiveCommitteeTeamMemberDTO> findOne(Long id);

    /**
     * Delete the "id" executiveCommitteeTeamMember.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
