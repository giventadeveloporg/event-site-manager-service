package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.EventDetails;
import com.eventsitemanager.repository.EventDetailsRepository;
import com.eventsitemanager.service.EventDetailsService;
import com.eventsitemanager.service.dto.EventDetailsDTO;
import com.eventsitemanager.service.mapper.EventDetailsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.eventsitemanager.domain.EventDetails}.
 */
@Service
@Transactional
public class EventDetailsServiceImpl implements EventDetailsService {

    private final Logger log = LoggerFactory.getLogger(EventDetailsServiceImpl.class);

    private final EventDetailsRepository eventDetailsRepository;

    private final EventDetailsMapper eventDetailsMapper;

    public EventDetailsServiceImpl(EventDetailsRepository eventDetailsRepository, EventDetailsMapper eventDetailsMapper) {
        this.eventDetailsRepository = eventDetailsRepository;
        this.eventDetailsMapper = eventDetailsMapper;
    }

    @Override
    public EventDetailsDTO save(EventDetailsDTO eventDetailsDTO) {
        log.debug("Request to save EventDetails : {}", eventDetailsDTO);
        EventDetails eventDetails = eventDetailsMapper.toEntity(eventDetailsDTO);
        eventDetails = eventDetailsRepository.save(eventDetails);
        return eventDetailsMapper.toDto(eventDetails);
    }

    @Override
    public EventDetailsDTO update(EventDetailsDTO eventDetailsDTO) {
        log.debug("Request to update EventDetails : {}", eventDetailsDTO);
        EventDetails eventDetails = eventDetailsMapper.toEntity(eventDetailsDTO);
        eventDetails = eventDetailsRepository.save(eventDetails);
        return eventDetailsMapper.toDto(eventDetails);
    }

    @Override
    public Optional<EventDetailsDTO> partialUpdate(EventDetailsDTO eventDetailsDTO) {
        log.debug("Request to partially update EventDetails : {}", eventDetailsDTO);

        return eventDetailsRepository
            .findById(eventDetailsDTO.getId())
            .map(existingEventDetails -> {
                eventDetailsMapper.partialUpdate(existingEventDetails, eventDetailsDTO);

                return existingEventDetails;
            })
            .map(eventDetailsRepository::save)
            .map(eventDetailsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventDetailsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EventDetails");
        return eventDetailsRepository.findAll(pageable).map(eventDetailsMapper::toDto);
    }

    public Page<EventDetailsDTO> findAllWithEagerRelationships(Pageable pageable) {
        return eventDetailsRepository.findAllWithEagerRelationships(pageable).map(eventDetailsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventDetailsDTO> findOne(Long id) {
        log.debug("Request to get EventDetails : {}", id);
        return eventDetailsRepository.findOneWithEagerRelationships(id).map(eventDetailsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EventDetails : {}", id);
        eventDetailsRepository.deleteById(id);
    }
}
