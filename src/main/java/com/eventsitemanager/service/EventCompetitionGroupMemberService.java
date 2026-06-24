package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.EventCompetitionGroupMemberDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.eventsitemanager.domain.EventCompetitionGroupMember}.
 */
public interface EventCompetitionGroupMemberService {
    EventCompetitionGroupMemberDTO save(EventCompetitionGroupMemberDTO eventCompetitionGroupMemberDTO);

    EventCompetitionGroupMemberDTO update(EventCompetitionGroupMemberDTO eventCompetitionGroupMemberDTO);

    Optional<EventCompetitionGroupMemberDTO> partialUpdate(EventCompetitionGroupMemberDTO eventCompetitionGroupMemberDTO);

    Page<EventCompetitionGroupMemberDTO> findAll(Pageable pageable);

    Optional<EventCompetitionGroupMemberDTO> findOne(Long id);

    void delete(Long id);
}
