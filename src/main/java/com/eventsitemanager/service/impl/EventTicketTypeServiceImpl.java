package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.EventTicketType;
import com.eventsitemanager.repository.EventTicketTypeRepository;
import com.eventsitemanager.service.EventTicketTypeService;
import com.eventsitemanager.service.dto.EventTicketTypeDTO;
import com.eventsitemanager.service.mapper.EventTicketTypeMapper;
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
 * Service Implementation for managing {@link com.eventsitemanager.domain.EventTicketType}.
 */
@Service
@Transactional
public class EventTicketTypeServiceImpl implements EventTicketTypeService {

    private final Logger log = LoggerFactory.getLogger(EventTicketTypeServiceImpl.class);

    private final EventTicketTypeRepository eventTicketTypeRepository;

    private final EventTicketTypeMapper eventTicketTypeMapper;

    public EventTicketTypeServiceImpl(EventTicketTypeRepository eventTicketTypeRepository, EventTicketTypeMapper eventTicketTypeMapper) {
        this.eventTicketTypeRepository = eventTicketTypeRepository;
        this.eventTicketTypeMapper = eventTicketTypeMapper;
    }

    @Override
    @CacheEvict(value = "eventTicketTypes", allEntries = true)
    public EventTicketTypeDTO save(EventTicketTypeDTO eventTicketTypeDTO) {
        log.debug("Request to save EventTicketType : {}", eventTicketTypeDTO);
        EventTicketType eventTicketType = eventTicketTypeMapper.toEntity(eventTicketTypeDTO);

        // Ensure ID is null for new entities to force sequence generation
        // This prevents duplicate key errors when entity has ID set from DTO
        if (eventTicketType.getId() != null) {
            log.warn(
                "EventTicketType entity has ID {} set during create operation. Clearing ID to force sequence generation.",
                eventTicketType.getId()
            );
            eventTicketType.setId(null);
        }

        eventTicketType = eventTicketTypeRepository.save(eventTicketType);
        return eventTicketTypeMapper.toDto(eventTicketType);
    }

    @Override
    @CacheEvict(value = "eventTicketTypes", allEntries = true)
    public EventTicketTypeDTO update(EventTicketTypeDTO eventTicketTypeDTO) {
        log.debug("Request to update EventTicketType : {}", eventTicketTypeDTO);
        EventTicketType eventTicketType = eventTicketTypeMapper.toEntity(eventTicketTypeDTO);
        eventTicketType = eventTicketTypeRepository.save(eventTicketType);
        return eventTicketTypeMapper.toDto(eventTicketType);
    }

    @Override
    public Optional<EventTicketTypeDTO> partialUpdate(EventTicketTypeDTO eventTicketTypeDTO) {
        log.debug("Request to partially update EventTicketType : {}", eventTicketTypeDTO);

        return eventTicketTypeRepository
            .findById(eventTicketTypeDTO.getId())
            .map(existingEventTicketType -> {
                eventTicketTypeMapper.partialUpdate(existingEventTicketType, eventTicketTypeDTO);

                return existingEventTicketType;
            })
            .map(eventTicketTypeRepository::save)
            .map(eventTicketTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventTicketTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EventTicketTypes");
        return eventTicketTypeRepository.findAll(pageable).map(eventTicketTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "eventTicketTypes", key = "#id", unless = "#result == null")
    public Optional<EventTicketTypeDTO> findOne(Long id) {
        log.debug("Request to get EventTicketType : {}", id);
        return eventTicketTypeRepository.findById(id).map(eventTicketTypeMapper::toDto);
    }

    @Override
    @CacheEvict(value = "eventTicketTypes", allEntries = true)
    public void delete(Long id) {
        log.debug("Request to delete EventTicketType : {}", id);
        eventTicketTypeRepository.deleteById(id);
    }
}
