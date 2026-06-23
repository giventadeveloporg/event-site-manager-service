package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.EventCompetitionSettingsDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.eventsitemanager.domain.EventCompetitionSettings}.
 */
public interface EventCompetitionSettingsService {
    EventCompetitionSettingsDTO save(EventCompetitionSettingsDTO eventCompetitionSettingsDTO);

    EventCompetitionSettingsDTO update(EventCompetitionSettingsDTO eventCompetitionSettingsDTO);

    Optional<EventCompetitionSettingsDTO> partialUpdate(EventCompetitionSettingsDTO eventCompetitionSettingsDTO);

    Page<EventCompetitionSettingsDTO> findAll(Pageable pageable);

    Optional<EventCompetitionSettingsDTO> findOne(Long id);

    void delete(Long id);
}
