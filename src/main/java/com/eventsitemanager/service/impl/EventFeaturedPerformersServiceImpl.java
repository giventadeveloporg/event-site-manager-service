package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.EventDetails;
import com.eventsitemanager.domain.EventFeaturedPerformers;
import com.eventsitemanager.repository.EventDetailsRepository;
import com.eventsitemanager.repository.EventFeaturedPerformersRepository;
import com.eventsitemanager.service.EventFeaturedPerformersService;
import com.eventsitemanager.service.dto.EventFeaturedPerformersDTO;
import com.eventsitemanager.service.mapper.EventFeaturedPerformersMapper;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EventFeaturedPerformers}.
 */
@Service
@Transactional
public class EventFeaturedPerformersServiceImpl implements EventFeaturedPerformersService {

    private final Logger log = LoggerFactory.getLogger(EventFeaturedPerformersServiceImpl.class);

    private final EventFeaturedPerformersRepository eventFeaturedPerformersRepository;

    private final EventFeaturedPerformersMapper eventFeaturedPerformersMapper;

    private final EventDetailsRepository eventDetailsRepository;

    public EventFeaturedPerformersServiceImpl(
        EventFeaturedPerformersRepository eventFeaturedPerformersRepository,
        EventFeaturedPerformersMapper eventFeaturedPerformersMapper,
        EventDetailsRepository eventDetailsRepository
    ) {
        this.eventFeaturedPerformersRepository = eventFeaturedPerformersRepository;
        this.eventFeaturedPerformersMapper = eventFeaturedPerformersMapper;
        this.eventDetailsRepository = eventDetailsRepository;
    }

    @Override
    public EventFeaturedPerformersDTO save(EventFeaturedPerformersDTO eventFeaturedPerformersDTO) {
        log.debug("Request to save EventFeaturedPerformers : {}", eventFeaturedPerformersDTO);
        EventFeaturedPerformers eventFeaturedPerformers = eventFeaturedPerformersMapper.toEntity(eventFeaturedPerformersDTO);

        // Set timestamps for new entities
        ZonedDateTime now = ZonedDateTime.now();
        if (eventFeaturedPerformers.getId() == null) {
            eventFeaturedPerformers.setCreatedAt(now);
        }
        eventFeaturedPerformers.setUpdatedAt(now);

        eventFeaturedPerformers = eventFeaturedPerformersRepository.save(eventFeaturedPerformers);
        return eventFeaturedPerformersMapper.toDto(eventFeaturedPerformers);
    }

    @Override
    public EventFeaturedPerformersDTO update(EventFeaturedPerformersDTO eventFeaturedPerformersDTO) {
        log.debug("Request to update EventFeaturedPerformers : {}", eventFeaturedPerformersDTO);
        EventFeaturedPerformers eventFeaturedPerformers = eventFeaturedPerformersMapper.toEntity(eventFeaturedPerformersDTO);
        eventFeaturedPerformers = eventFeaturedPerformersRepository.save(eventFeaturedPerformers);
        return eventFeaturedPerformersMapper.toDto(eventFeaturedPerformers);
    }

    @Override
    public Optional<EventFeaturedPerformersDTO> partialUpdate(EventFeaturedPerformersDTO eventFeaturedPerformersDTO) {
        log.debug("Request to partially update EventFeaturedPerformers : {}", eventFeaturedPerformersDTO);

        return eventFeaturedPerformersRepository
            .findById(eventFeaturedPerformersDTO.getId())
            .map(existingEventFeaturedPerformers -> {
                eventFeaturedPerformersMapper.partialUpdate(existingEventFeaturedPerformers, eventFeaturedPerformersDTO);

                return existingEventFeaturedPerformers;
            })
            .map(eventFeaturedPerformersRepository::save)
            .map(eventFeaturedPerformersMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventFeaturedPerformersDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EventFeaturedPerformers");
        return eventFeaturedPerformersRepository.findAll(pageable).map(eventFeaturedPerformersMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventFeaturedPerformersDTO> findOne(Long id) {
        log.debug("Request to get EventFeaturedPerformers : {}", id);
        return eventFeaturedPerformersRepository.findById(id).map(eventFeaturedPerformersMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EventFeaturedPerformers : {}", id);
        eventFeaturedPerformersRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFeaturedPerformersDTO> findByEventId(Long eventId) {
        log.debug("Request to get all EventFeaturedPerformers for event : {}", eventId);
        return eventFeaturedPerformersRepository
            .findByEventId(eventId)
            .stream()
            .map(eventFeaturedPerformersMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventFeaturedPerformersDTO> findByEventId(Long eventId, Pageable pageable) {
        log.debug("Request to get all EventFeaturedPerformers for event : {} with pagination", eventId);
        return eventFeaturedPerformersRepository.findByEventId(eventId, pageable).map(eventFeaturedPerformersMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFeaturedPerformersDTO> findByEventIdAndIsActive(Long eventId, Boolean isActive) {
        log.debug("Request to get all active EventFeaturedPerformers for event : {} with active status : {}", eventId, isActive);
        return eventFeaturedPerformersRepository
            .findByEventIdAndIsActive(eventId, isActive)
            .stream()
            .map(eventFeaturedPerformersMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFeaturedPerformersDTO> findByNameContaining(String name) {
        log.debug("Request to get all EventFeaturedPerformers by name containing : {}", name);
        return eventFeaturedPerformersRepository
            .findByNameContainingIgnoreCase(name)
            .stream()
            .map(eventFeaturedPerformersMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFeaturedPerformersDTO> findByRole(String role) {
        log.debug("Request to get all EventFeaturedPerformers by role : {}", role);
        return eventFeaturedPerformersRepository
            .findByRole(role)
            .stream()
            .map(eventFeaturedPerformersMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFeaturedPerformersDTO> findByNationality(String nationality) {
        log.debug("Request to get all EventFeaturedPerformers by nationality : {}", nationality);
        return eventFeaturedPerformersRepository
            .findByNationality(nationality)
            .stream()
            .map(eventFeaturedPerformersMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFeaturedPerformersDTO> findByEventIdAndIsHeadliner(Long eventId, Boolean isHeadliner) {
        log.debug("Request to get all headliner EventFeaturedPerformers for event : {} with headliner status : {}", eventId, isHeadliner);
        return eventFeaturedPerformersRepository
            .findByEventIdAndIsHeadliner(eventId, isHeadliner)
            .stream()
            .map(eventFeaturedPerformersMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFeaturedPerformersDTO> findByEventIdOrderByPriorityRanking(Long eventId) {
        log.debug("Request to get all EventFeaturedPerformers for event : {} ordered by priority ranking", eventId);
        return eventFeaturedPerformersRepository
            .findByEventIdOrderByPriorityRanking(eventId)
            .stream()
            .map(eventFeaturedPerformersMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long countByEventId(Long eventId) {
        log.debug("Request to count EventFeaturedPerformers for event : {}", eventId);
        return eventFeaturedPerformersRepository.countByEventId(eventId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByEventIdAndIsActive(Long eventId, Boolean isActive) {
        log.debug("Request to count active EventFeaturedPerformers for event : {} with active status : {}", eventId, isActive);
        return eventFeaturedPerformersRepository.countByEventIdAndIsActive(eventId, isActive);
    }

    // ===================================================================
    // Multi-tenant and nullable event_id support methods
    // ===================================================================

    @Override
    @Transactional(readOnly = true)
    public List<EventFeaturedPerformersDTO> findAllTenantLevelFeaturedPerformers(String tenantId) {
        log.debug("Request to get all tenant-level EventFeaturedPerformers for tenant : {}", tenantId);
        return eventFeaturedPerformersRepository
            .findByTenantIdAndEventIsNull(tenantId)
            .stream()
            .map(eventFeaturedPerformersMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFeaturedPerformersDTO> findAvailableFeaturedPerformersForEvent(String tenantId, Long eventId) {
        log.debug("Request to get available EventFeaturedPerformers for tenant : {} and event : {}", tenantId, eventId);
        return eventFeaturedPerformersRepository
            .findByTenantIdAndEventIsNullOrNotEqualToEventId(tenantId, eventId)
            .stream()
            .map(eventFeaturedPerformersMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public EventFeaturedPerformersDTO disassociateFromEvent(Long id) {
        log.debug("Request to disassociate EventFeaturedPerformers : {} from its event", id);
        EventFeaturedPerformers eventFeaturedPerformers = eventFeaturedPerformersRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("EventFeaturedPerformers not found with id: " + id));

        eventFeaturedPerformers.setEvent(null);
        eventFeaturedPerformers.setUpdatedAt(ZonedDateTime.now());

        eventFeaturedPerformers = eventFeaturedPerformersRepository.save(eventFeaturedPerformers);
        return eventFeaturedPerformersMapper.toDto(eventFeaturedPerformers);
    }

    @Override
    public EventFeaturedPerformersDTO associateWithEvent(Long id, Long eventId) {
        log.debug("Request to associate EventFeaturedPerformers : {} with event : {}", id, eventId);
        EventFeaturedPerformers eventFeaturedPerformers = eventFeaturedPerformersRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("EventFeaturedPerformers not found with id: " + id));

        EventDetails event = eventDetailsRepository
            .findById(eventId)
            .orElseThrow(() -> new RuntimeException("EventDetails not found with id: " + eventId));

        eventFeaturedPerformers.setEvent(event);
        eventFeaturedPerformers.setUpdatedAt(ZonedDateTime.now());

        eventFeaturedPerformers = eventFeaturedPerformersRepository.save(eventFeaturedPerformers);
        return eventFeaturedPerformersMapper.toDto(eventFeaturedPerformers);
    }
}
