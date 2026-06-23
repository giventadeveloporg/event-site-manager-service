package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.EventOrganizer;
import com.eventsitemanager.repository.EventOrganizerRepository;
import com.eventsitemanager.service.EventOrganizerService;
import com.eventsitemanager.service.dto.EventOrganizerDTO;
import com.eventsitemanager.service.mapper.EventOrganizerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.eventsitemanager.domain.EventOrganizer}.
 */
@Service
@Transactional
public class EventOrganizerServiceImpl implements EventOrganizerService {

    private final Logger log = LoggerFactory.getLogger(EventOrganizerServiceImpl.class);

    private final EventOrganizerRepository eventOrganizerRepository;

    private final EventOrganizerMapper eventOrganizerMapper;

    public EventOrganizerServiceImpl(EventOrganizerRepository eventOrganizerRepository, EventOrganizerMapper eventOrganizerMapper) {
        this.eventOrganizerRepository = eventOrganizerRepository;
        this.eventOrganizerMapper = eventOrganizerMapper;
    }

    @Override
    public EventOrganizerDTO save(EventOrganizerDTO eventOrganizerDTO) {
        log.debug("Request to save EventOrganizer : {}", eventOrganizerDTO);
        EventOrganizer eventOrganizer = eventOrganizerMapper.toEntity(eventOrganizerDTO);
        eventOrganizer = eventOrganizerRepository.save(eventOrganizer);
        return eventOrganizerMapper.toDto(eventOrganizer);
    }

    @Override
    public EventOrganizerDTO update(EventOrganizerDTO eventOrganizerDTO) {
        log.debug("Request to update EventOrganizer : {}", eventOrganizerDTO);
        EventOrganizer eventOrganizer = eventOrganizerMapper.toEntity(eventOrganizerDTO);
        eventOrganizer = eventOrganizerRepository.save(eventOrganizer);
        return eventOrganizerMapper.toDto(eventOrganizer);
    }

    @Override
    public Optional<EventOrganizerDTO> partialUpdate(EventOrganizerDTO eventOrganizerDTO) {
        log.debug("Request to partially update EventOrganizer : {}", eventOrganizerDTO);

        return eventOrganizerRepository
            .findById(eventOrganizerDTO.getId())
            .map(existingEventOrganizer -> {
                eventOrganizerMapper.partialUpdate(existingEventOrganizer, eventOrganizerDTO);

                return existingEventOrganizer;
            })
            .map(eventOrganizerRepository::save)
            .map(eventOrganizerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventOrganizerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EventOrganizers");
        return eventOrganizerRepository.findAll(pageable).map(eventOrganizerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventOrganizerDTO> findOne(Long id) {
        log.debug("Request to get EventOrganizer : {}", id);
        return eventOrganizerRepository.findById(id).map(eventOrganizerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EventOrganizer : {}", id);
        eventOrganizerRepository.deleteById(id);
    }
}
