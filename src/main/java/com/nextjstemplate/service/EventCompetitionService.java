package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.CompetitionEligibilityCheckDTO;
import com.nextjstemplate.service.dto.EventCompetitionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.nextjstemplate.domain.EventCompetition}.
 */
public interface EventCompetitionService {
    EventCompetitionDTO save(EventCompetitionDTO eventCompetitionDTO);

    EventCompetitionDTO update(EventCompetitionDTO eventCompetitionDTO);

    Optional<EventCompetitionDTO> partialUpdate(EventCompetitionDTO eventCompetitionDTO);

    Page<EventCompetitionDTO> findAll(Pageable pageable);

    Optional<EventCompetitionDTO> findOne(Long id);

    void delete(Long id);

    CompetitionEligibilityCheckDTO checkEligibility(Long competitionId, Long participantProfileId);
}
