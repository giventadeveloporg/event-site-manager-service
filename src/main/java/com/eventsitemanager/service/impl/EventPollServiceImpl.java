package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.EventPoll;
import com.eventsitemanager.repository.EventPollRepository;
import com.eventsitemanager.service.EventPollService;
import com.eventsitemanager.service.dto.EventPollDTO;
import com.eventsitemanager.service.mapper.EventPollMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.eventsitemanager.domain.EventPoll}.
 */
@Service
@Transactional
public class EventPollServiceImpl implements EventPollService {

    private final Logger log = LoggerFactory.getLogger(EventPollServiceImpl.class);

    private final EventPollRepository eventPollRepository;

    private final EventPollMapper eventPollMapper;

    public EventPollServiceImpl(EventPollRepository eventPollRepository, EventPollMapper eventPollMapper) {
        this.eventPollRepository = eventPollRepository;
        this.eventPollMapper = eventPollMapper;
    }

    @Override
    public EventPollDTO save(EventPollDTO eventPollDTO) {
        log.debug("Request to save EventPoll : {}", eventPollDTO);
        EventPoll eventPoll = eventPollMapper.toEntity(eventPollDTO);
        eventPoll = eventPollRepository.save(eventPoll);
        return eventPollMapper.toDto(eventPoll);
    }

    @Override
    public EventPollDTO update(EventPollDTO eventPollDTO) {
        log.debug("Request to update EventPoll : {}", eventPollDTO);
        EventPoll eventPoll = eventPollMapper.toEntity(eventPollDTO);
        eventPoll = eventPollRepository.save(eventPoll);
        return eventPollMapper.toDto(eventPoll);
    }

    @Override
    public Optional<EventPollDTO> partialUpdate(EventPollDTO eventPollDTO) {
        log.debug("Request to partially update EventPoll : {}", eventPollDTO);

        return eventPollRepository
            .findById(eventPollDTO.getId())
            .map(existingEventPoll -> {
                eventPollMapper.partialUpdate(existingEventPoll, eventPollDTO);

                return existingEventPoll;
            })
            .map(eventPollRepository::save)
            .map(eventPollMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventPollDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EventPolls");
        return eventPollRepository.findAll(pageable).map(eventPollMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventPollDTO> findOne(Long id) {
        log.debug("Request to get EventPoll : {}", id);
        return eventPollRepository.findById(id).map(eventPollMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EventPoll : {}", id);
        eventPollRepository.deleteById(id);
    }
}
