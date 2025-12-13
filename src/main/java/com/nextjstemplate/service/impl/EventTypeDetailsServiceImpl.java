package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.EventTypeDetails;
import com.nextjstemplate.repository.EventTypeDetailsRepository;
import com.nextjstemplate.service.EventTypeDetailsService;
import com.nextjstemplate.service.dto.EventTypeDetailsDTO;
import com.nextjstemplate.service.mapper.EventTypeDetailsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nextjstemplate.domain.EventTypeDetails}.
 */
@Service
@Transactional
public class EventTypeDetailsServiceImpl implements EventTypeDetailsService {

    private final Logger log = LoggerFactory.getLogger(EventTypeDetailsServiceImpl.class);

    private final EventTypeDetailsRepository eventTypeDetailsRepository;

    private final EventTypeDetailsMapper eventTypeDetailsMapper;

    public EventTypeDetailsServiceImpl(
        EventTypeDetailsRepository eventTypeDetailsRepository,
        EventTypeDetailsMapper eventTypeDetailsMapper
    ) {
        this.eventTypeDetailsRepository = eventTypeDetailsRepository;
        this.eventTypeDetailsMapper = eventTypeDetailsMapper;
    }

    @Override
    @CacheEvict(value = "eventTypeDetails", allEntries = true)
    public EventTypeDetailsDTO save(EventTypeDetailsDTO eventTypeDetailsDTO) {
        log.debug("Request to save EventTypeDetails : {}", eventTypeDetailsDTO);
        EventTypeDetails eventTypeDetails = eventTypeDetailsMapper.toEntity(eventTypeDetailsDTO);
        eventTypeDetails = eventTypeDetailsRepository.save(eventTypeDetails);
        return eventTypeDetailsMapper.toDto(eventTypeDetails);
    }

    @Override
    @CacheEvict(value = "eventTypeDetails", allEntries = true)
    public EventTypeDetailsDTO update(EventTypeDetailsDTO eventTypeDetailsDTO) {
        log.debug("Request to update EventTypeDetails : {}", eventTypeDetailsDTO);
        EventTypeDetails eventTypeDetails = eventTypeDetailsMapper.toEntity(eventTypeDetailsDTO);
        eventTypeDetails = eventTypeDetailsRepository.save(eventTypeDetails);
        return eventTypeDetailsMapper.toDto(eventTypeDetails);
    }

    @Override
    public Optional<EventTypeDetailsDTO> partialUpdate(EventTypeDetailsDTO eventTypeDetailsDTO) {
        log.debug("Request to partially update EventTypeDetails : {}", eventTypeDetailsDTO);

        return eventTypeDetailsRepository
            .findById(eventTypeDetailsDTO.getId())
            .map(existingEventTypeDetails -> {
                eventTypeDetailsMapper.partialUpdate(existingEventTypeDetails, eventTypeDetailsDTO);

                return existingEventTypeDetails;
            })
            .map(eventTypeDetailsRepository::save)
            .map(eventTypeDetailsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventTypeDetailsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EventTypeDetails");
        return eventTypeDetailsRepository.findAll(pageable).map(eventTypeDetailsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "eventTypeDetails", key = "#id", unless = "#result == null")
    public Optional<EventTypeDetailsDTO> findOne(Long id) {
        log.debug("Request to get EventTypeDetails : {}", id);
        return eventTypeDetailsRepository.findById(id).map(eventTypeDetailsMapper::toDto);
    }

    @Override
    @CacheEvict(value = "eventTypeDetails", allEntries = true)
    public void delete(Long id) {
        log.debug("Request to delete EventTypeDetails : {}", id);
        eventTypeDetailsRepository.deleteById(id);
    }
}
