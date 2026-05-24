package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.EventCompetitionRegistrationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.nextjstemplate.domain.EventCompetitionRegistration}.
 */
public interface EventCompetitionRegistrationService {
    EventCompetitionRegistrationDTO save(EventCompetitionRegistrationDTO eventCompetitionRegistrationDTO);

    EventCompetitionRegistrationDTO update(EventCompetitionRegistrationDTO eventCompetitionRegistrationDTO);

    Optional<EventCompetitionRegistrationDTO> partialUpdate(EventCompetitionRegistrationDTO eventCompetitionRegistrationDTO);

    Page<EventCompetitionRegistrationDTO> findAll(Pageable pageable);

    Optional<EventCompetitionRegistrationDTO> findOne(Long id);

    void delete(Long id);
}
