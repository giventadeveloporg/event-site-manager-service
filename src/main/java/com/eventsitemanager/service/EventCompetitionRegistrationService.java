package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.EventCompetitionRegistrationDTO;
import com.eventsitemanager.service.dto.TeamRegistrationRequestDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.eventsitemanager.domain.EventCompetitionRegistration}.
 */
public interface EventCompetitionRegistrationService {
    EventCompetitionRegistrationDTO save(EventCompetitionRegistrationDTO eventCompetitionRegistrationDTO);

    EventCompetitionRegistrationDTO update(EventCompetitionRegistrationDTO eventCompetitionRegistrationDTO);

    Optional<EventCompetitionRegistrationDTO> partialUpdate(EventCompetitionRegistrationDTO eventCompetitionRegistrationDTO);

    Page<EventCompetitionRegistrationDTO> findAll(Pageable pageable);

    Optional<EventCompetitionRegistrationDTO> findOne(Long id);

    void delete(Long id);

    EventCompetitionRegistrationDTO saveTeamRegistration(TeamRegistrationRequestDTO request);

    List<EventCompetitionRegistrationDTO> saveBulkRegistrations(List<EventCompetitionRegistrationDTO> registrations);
}
