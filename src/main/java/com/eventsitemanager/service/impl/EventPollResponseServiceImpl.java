package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.EventPollResponse;
import com.eventsitemanager.repository.EventPollResponseRepository;
import com.eventsitemanager.service.EventPollResponseService;
import com.eventsitemanager.service.dto.EventPollResponseDTO;
import com.eventsitemanager.service.mapper.EventPollResponseMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.eventsitemanager.domain.EventPollResponse}.
 */
@Service
@Transactional
public class EventPollResponseServiceImpl implements EventPollResponseService {

    private final Logger log = LoggerFactory.getLogger(EventPollResponseServiceImpl.class);

    private final EventPollResponseRepository eventPollResponseRepository;

    private final EventPollResponseMapper eventPollResponseMapper;

    public EventPollResponseServiceImpl(
        EventPollResponseRepository eventPollResponseRepository,
        EventPollResponseMapper eventPollResponseMapper
    ) {
        this.eventPollResponseRepository = eventPollResponseRepository;
        this.eventPollResponseMapper = eventPollResponseMapper;
    }

    @Override
    public EventPollResponseDTO save(EventPollResponseDTO eventPollResponseDTO) {
        log.debug("Request to save EventPollResponse : {}", eventPollResponseDTO);
        EventPollResponse eventPollResponse = eventPollResponseMapper.toEntity(eventPollResponseDTO);
        eventPollResponse = eventPollResponseRepository.save(eventPollResponse);
        return eventPollResponseMapper.toDto(eventPollResponse);
    }

    @Override
    public EventPollResponseDTO update(EventPollResponseDTO eventPollResponseDTO) {
        log.debug("Request to update EventPollResponse : {}", eventPollResponseDTO);
        EventPollResponse eventPollResponse = eventPollResponseMapper.toEntity(eventPollResponseDTO);
        eventPollResponse = eventPollResponseRepository.save(eventPollResponse);
        return eventPollResponseMapper.toDto(eventPollResponse);
    }

    @Override
    public Optional<EventPollResponseDTO> partialUpdate(EventPollResponseDTO eventPollResponseDTO) {
        log.debug("Request to partially update EventPollResponse : {}", eventPollResponseDTO);

        return eventPollResponseRepository
            .findById(eventPollResponseDTO.getId())
            .map(existingEventPollResponse -> {
                eventPollResponseMapper.partialUpdate(existingEventPollResponse, eventPollResponseDTO);

                return existingEventPollResponse;
            })
            .map(eventPollResponseRepository::save)
            .map(eventPollResponseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventPollResponseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EventPollResponses");
        return eventPollResponseRepository.findAll(pageable).map(eventPollResponseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventPollResponseDTO> findOne(Long id) {
        log.debug("Request to get EventPollResponse : {}", id);
        return eventPollResponseRepository.findById(id).map(eventPollResponseMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EventPollResponse : {}", id);
        eventPollResponseRepository.deleteById(id);
    }
}
