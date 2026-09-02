package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.EventAgendaItemDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.eventsitemanager.domain.EventAgendaItem}.
 */
public interface EventAgendaItemService {
    EventAgendaItemDTO save(EventAgendaItemDTO eventAgendaItemDTO);

    EventAgendaItemDTO update(EventAgendaItemDTO eventAgendaItemDTO);

    Optional<EventAgendaItemDTO> partialUpdate(EventAgendaItemDTO eventAgendaItemDTO);

    Page<EventAgendaItemDTO> findAll(Pageable pageable);

    Optional<EventAgendaItemDTO> findOne(Long id);

    void delete(Long id);

    List<EventAgendaItemDTO> findByEventId(Long eventId);

    List<EventAgendaItemDTO> findByEventId(Long eventId, boolean publishedOnly);
}
