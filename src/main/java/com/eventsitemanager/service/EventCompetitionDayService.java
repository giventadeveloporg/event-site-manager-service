package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.EventCompetitionDayDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.eventsitemanager.domain.EventCompetitionDay}.
 */
public interface EventCompetitionDayService {
    EventCompetitionDayDTO save(EventCompetitionDayDTO eventCompetitionDayDTO);

    EventCompetitionDayDTO update(EventCompetitionDayDTO eventCompetitionDayDTO);

    Optional<EventCompetitionDayDTO> partialUpdate(EventCompetitionDayDTO eventCompetitionDayDTO);

    Page<EventCompetitionDayDTO> findAll(Pageable pageable);

    Optional<EventCompetitionDayDTO> findOne(Long id);

    void delete(Long id);
}
