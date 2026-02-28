package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.EventAttendee;
import com.nextjstemplate.repository.EventAttendeeRepository;
import com.nextjstemplate.service.EventAttendeeService;
import com.nextjstemplate.service.dto.EventAttendeeDTO;
import com.nextjstemplate.service.mapper.EventAttendeeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nextjstemplate.domain.EventAttendee}.
 */
@Service
@Transactional
public class EventAttendeeServiceImpl implements EventAttendeeService {

    private final Logger log = LoggerFactory.getLogger(EventAttendeeServiceImpl.class);

    private final EventAttendeeRepository eventAttendeeRepository;

    private final EventAttendeeMapper eventAttendeeMapper;

    public EventAttendeeServiceImpl(EventAttendeeRepository eventAttendeeRepository, EventAttendeeMapper eventAttendeeMapper) {
        this.eventAttendeeRepository = eventAttendeeRepository;
        this.eventAttendeeMapper = eventAttendeeMapper;
    }

    @Override
    public EventAttendeeDTO save(EventAttendeeDTO eventAttendeeDTO) {
        log.debug("Request to save EventAttendee : {}", eventAttendeeDTO);
        EventAttendee eventAttendee = eventAttendeeMapper.toEntity(eventAttendeeDTO);

        // Ensure ID is null for new entities to force sequence generation
        // This prevents duplicate key errors when entity has ID set from DTO (see .cursor/rules/duplicate-key-prevention.mdc)
        if (eventAttendee.getId() != null) {
            log.warn(
                "EventAttendee has ID {} set during create operation. Clearing ID to force sequence generation.",
                eventAttendee.getId()
            );
            eventAttendee.setId(null);
        }

        eventAttendee = eventAttendeeRepository.save(eventAttendee);
        return eventAttendeeMapper.toDto(eventAttendee);
    }

    @Override
    public EventAttendeeDTO update(EventAttendeeDTO eventAttendeeDTO) {
        log.debug("Request to update EventAttendee : {}", eventAttendeeDTO);
        EventAttendee eventAttendee = eventAttendeeMapper.toEntity(eventAttendeeDTO);
        eventAttendee = eventAttendeeRepository.save(eventAttendee);
        return eventAttendeeMapper.toDto(eventAttendee);
    }

    @Override
    public Optional<EventAttendeeDTO> partialUpdate(EventAttendeeDTO eventAttendeeDTO) {
        log.debug("Request to partially update EventAttendee : {}", eventAttendeeDTO);

        return eventAttendeeRepository
            .findById(eventAttendeeDTO.getId())
            .map(existingEventAttendee -> {
                eventAttendeeMapper.partialUpdate(existingEventAttendee, eventAttendeeDTO);

                return existingEventAttendee;
            })
            .map(eventAttendeeRepository::save)
            .map(eventAttendeeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventAttendeeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EventAttendees");
        return eventAttendeeRepository.findAll(pageable).map(eventAttendeeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventAttendeeDTO> findOne(Long id) {
        log.debug("Request to get EventAttendee : {}", id);
        return eventAttendeeRepository.findById(id).map(eventAttendeeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EventAttendee : {}", id);
        eventAttendeeRepository.deleteById(id);
    }
}
