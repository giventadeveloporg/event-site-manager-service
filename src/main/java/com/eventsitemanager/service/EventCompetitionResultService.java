package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.EventCompetitionResultDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.eventsitemanager.domain.EventCompetitionResult}.
 */
public interface EventCompetitionResultService {
    EventCompetitionResultDTO save(EventCompetitionResultDTO eventCompetitionResultDTO);

    EventCompetitionResultDTO update(EventCompetitionResultDTO eventCompetitionResultDTO);

    Optional<EventCompetitionResultDTO> partialUpdate(EventCompetitionResultDTO eventCompetitionResultDTO);

    Page<EventCompetitionResultDTO> findAll(Pageable pageable);

    Optional<EventCompetitionResultDTO> findOne(Long id);

    void delete(Long id);
}
