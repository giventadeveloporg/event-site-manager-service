package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.EventCompetitionParticipantDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.eventsitemanager.domain.EventCompetitionParticipant}.
 */
public interface EventCompetitionParticipantService {
    EventCompetitionParticipantDTO save(EventCompetitionParticipantDTO eventCompetitionParticipantDTO);

    EventCompetitionParticipantDTO update(EventCompetitionParticipantDTO eventCompetitionParticipantDTO);

    Optional<EventCompetitionParticipantDTO> partialUpdate(EventCompetitionParticipantDTO eventCompetitionParticipantDTO);

    Page<EventCompetitionParticipantDTO> findAll(Pageable pageable);

    Optional<EventCompetitionParticipantDTO> findOne(Long id);

    void delete(Long id);
}
