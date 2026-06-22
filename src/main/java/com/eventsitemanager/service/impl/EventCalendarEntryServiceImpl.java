package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.EventCalendarEntry;
import com.eventsitemanager.repository.EventCalendarEntryRepository;
import com.eventsitemanager.service.EventCalendarEntryService;
import com.eventsitemanager.service.dto.EventCalendarEntryDTO;
import com.eventsitemanager.service.mapper.EventCalendarEntryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.eventsitemanager.domain.EventCalendarEntry}.
 */
@Service
@Transactional
public class EventCalendarEntryServiceImpl implements EventCalendarEntryService {

    private final Logger log = LoggerFactory.getLogger(EventCalendarEntryServiceImpl.class);

    private final EventCalendarEntryRepository eventCalendarEntryRepository;

    private final EventCalendarEntryMapper eventCalendarEntryMapper;

    public EventCalendarEntryServiceImpl(
        EventCalendarEntryRepository eventCalendarEntryRepository,
        EventCalendarEntryMapper eventCalendarEntryMapper
    ) {
        this.eventCalendarEntryRepository = eventCalendarEntryRepository;
        this.eventCalendarEntryMapper = eventCalendarEntryMapper;
    }

    @Override
    public EventCalendarEntryDTO save(EventCalendarEntryDTO eventCalendarEntryDTO) {
        log.debug("Request to save EventCalendarEntry : {}", eventCalendarEntryDTO);
        EventCalendarEntry eventCalendarEntry = eventCalendarEntryMapper.toEntity(eventCalendarEntryDTO);
        eventCalendarEntry = eventCalendarEntryRepository.save(eventCalendarEntry);
        return eventCalendarEntryMapper.toDto(eventCalendarEntry);
    }

    @Override
    public EventCalendarEntryDTO update(EventCalendarEntryDTO eventCalendarEntryDTO) {
        log.debug("Request to update EventCalendarEntry : {}", eventCalendarEntryDTO);
        EventCalendarEntry eventCalendarEntry = eventCalendarEntryMapper.toEntity(eventCalendarEntryDTO);
        eventCalendarEntry = eventCalendarEntryRepository.save(eventCalendarEntry);
        return eventCalendarEntryMapper.toDto(eventCalendarEntry);
    }

    @Override
    public Optional<EventCalendarEntryDTO> partialUpdate(EventCalendarEntryDTO eventCalendarEntryDTO) {
        log.debug("Request to partially update EventCalendarEntry : {}", eventCalendarEntryDTO);

        return eventCalendarEntryRepository
            .findById(eventCalendarEntryDTO.getId())
            .map(existingEventCalendarEntry -> {
                eventCalendarEntryMapper.partialUpdate(existingEventCalendarEntry, eventCalendarEntryDTO);

                return existingEventCalendarEntry;
            })
            .map(eventCalendarEntryRepository::save)
            .map(eventCalendarEntryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventCalendarEntryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EventCalendarEntries");
        return eventCalendarEntryRepository.findAll(pageable).map(eventCalendarEntryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventCalendarEntryDTO> findOne(Long id) {
        log.debug("Request to get EventCalendarEntry : {}", id);
        return eventCalendarEntryRepository.findById(id).map(eventCalendarEntryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EventCalendarEntry : {}", id);
        eventCalendarEntryRepository.deleteById(id);
    }
}
