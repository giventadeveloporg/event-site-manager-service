package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.EventFocusGroup;
import com.eventsitemanager.repository.EventFocusGroupRepository;
import com.eventsitemanager.service.EventFocusGroupService;
import com.eventsitemanager.service.dto.EventFocusGroupDTO;
import com.eventsitemanager.service.mapper.EventFocusGroupMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing
 * {@link com.eventsitemanager.domain.EventFocusGroup}.
 */
@Service
@Transactional
public class EventFocusGroupServiceImpl implements EventFocusGroupService {

    private final Logger log = LoggerFactory.getLogger(EventFocusGroupServiceImpl.class);

    private final EventFocusGroupRepository eventFocusGroupRepository;

    private final EventFocusGroupMapper eventFocusGroupMapper;

    public EventFocusGroupServiceImpl(EventFocusGroupRepository eventFocusGroupRepository, EventFocusGroupMapper eventFocusGroupMapper) {
        this.eventFocusGroupRepository = eventFocusGroupRepository;
        this.eventFocusGroupMapper = eventFocusGroupMapper;
    }

    @Override
    public EventFocusGroupDTO save(EventFocusGroupDTO eventFocusGroupDTO) {
        log.debug("Request to save EventFocusGroup : {}", eventFocusGroupDTO);

        EventFocusGroup eventFocusGroup = eventFocusGroupMapper.toEntity(eventFocusGroupDTO);

        try {
            eventFocusGroup = eventFocusGroupRepository.save(eventFocusGroup);
        } catch (DataIntegrityViolationException e) {
            log.error("Failed to save EventFocusGroup due to constraint violation: {}", e.getMessage());
            throw new IllegalArgumentException(
                "This focus group is already linked to this event. EventId: " +
                eventFocusGroupDTO.getEventId() +
                ", FocusGroupId: " +
                eventFocusGroupDTO.getFocusGroupId()
            );
        }

        return eventFocusGroupMapper.toDto(eventFocusGroup);
    }

    @Override
    public EventFocusGroupDTO update(EventFocusGroupDTO eventFocusGroupDTO) {
        log.debug("Request to update EventFocusGroup : {}", eventFocusGroupDTO);

        EventFocusGroup eventFocusGroup = eventFocusGroupMapper.toEntity(eventFocusGroupDTO);

        try {
            eventFocusGroup = eventFocusGroupRepository.save(eventFocusGroup);
        } catch (DataIntegrityViolationException e) {
            log.error("Failed to update EventFocusGroup due to constraint violation: {}", e.getMessage());
            throw new IllegalArgumentException("This focus group is already linked to this event.");
        }

        return eventFocusGroupMapper.toDto(eventFocusGroup);
    }

    @Override
    public Optional<EventFocusGroupDTO> partialUpdate(EventFocusGroupDTO eventFocusGroupDTO) {
        log.debug("Request to partially update EventFocusGroup : {}", eventFocusGroupDTO);

        return eventFocusGroupRepository
            .findById(eventFocusGroupDTO.getId())
            .map(existingEventFocusGroup -> {
                eventFocusGroupMapper.partialUpdate(existingEventFocusGroup, eventFocusGroupDTO);
                return existingEventFocusGroup;
            })
            .map(eventFocusGroup -> {
                try {
                    return eventFocusGroupRepository.save(eventFocusGroup);
                } catch (DataIntegrityViolationException e) {
                    log.error("Failed to update EventFocusGroup due to constraint violation: {}", e.getMessage());
                    throw new IllegalArgumentException("This focus group is already linked to this event.");
                }
            })
            .map(eventFocusGroupMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventFocusGroupDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EventFocusGroups");
        return eventFocusGroupRepository.findAll(pageable).map(eventFocusGroupMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventFocusGroupDTO> findOne(Long id) {
        log.debug("Request to get EventFocusGroup : {}", id);
        return eventFocusGroupRepository.findById(id).map(eventFocusGroupMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EventFocusGroup : {}", id);
        eventFocusGroupRepository.deleteById(id);
    }
}
