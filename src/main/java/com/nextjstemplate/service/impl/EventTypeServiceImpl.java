package com.nextjstemplate.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nextjstemplate.domain.EventType}.
 */
@Service
@Transactional
public class EventTypeServiceImpl /* implements EventTypeService*/{/*

    private final Logger log = LoggerFactory.getLogger(EventTypeServiceImpl.class);

    private final EventTypeRepository eventTypeRepository;

    private final EventTypeMapper eventTypeMapper;

    public EventTypeServiceImpl(EventTypeRepository eventTypeRepository, EventTypeMapper eventTypeMapper) {
        this.eventTypeRepository = eventTypeRepository;
        this.eventTypeMapper = eventTypeMapper;
    }

    @Override
    public EventTypeDTO save(EventTypeDTO eventTypeDTO) {
        log.debug("Request to save EventType : {}", eventTypeDTO);
        EventType eventType = eventTypeMapper.toEntity(eventTypeDTO);
        eventType = eventTypeRepository.save(eventType);
        return eventTypeMapper.toDto(eventType);
    }

    @Override
    public EventTypeDTO update(EventTypeDTO eventTypeDTO) {
        log.debug("Request to update EventType : {}", eventTypeDTO);
        EventType eventType = eventTypeMapper.toEntity(eventTypeDTO);
        eventType = eventTypeRepository.save(eventType);
        return eventTypeMapper.toDto(eventType);
    }

    @Override
    public Optional<EventTypeDTO> partialUpdate(EventTypeDTO eventTypeDTO) {
        log.debug("Request to partially update EventType : {}", eventTypeDTO);

        return eventTypeRepository
            .findById(eventTypeDTO.getId())
            .map(existingEventType -> {
                eventTypeMapper.partialUpdate(existingEventType, eventTypeDTO);

                return existingEventType;
            })
            .map(eventTypeRepository::save)
            .map(eventTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EventTypes");
        return eventTypeRepository.findAll(pageable).map(eventTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventTypeDTO> findOne(Long id) {
        log.debug("Request to get EventType : {}", id);
        return eventTypeRepository.findById(id).map(eventTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EventType : {}", id);
        eventTypeRepository.deleteById(id);
    }*/}
