package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.EventAgendaItem;
import com.eventsitemanager.repository.EventAgendaItemRepository;
import com.eventsitemanager.service.EventAgendaItemService;
import com.eventsitemanager.service.dto.EventAgendaItemDTO;
import com.eventsitemanager.service.mapper.EventAgendaItemMapper;
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

@Service
@Transactional
public class EventAgendaItemServiceImpl implements EventAgendaItemService {

    private final Logger log = LoggerFactory.getLogger(EventAgendaItemServiceImpl.class);

    private final EventAgendaItemRepository eventAgendaItemRepository;

    private final EventAgendaItemMapper eventAgendaItemMapper;

    public EventAgendaItemServiceImpl(EventAgendaItemRepository eventAgendaItemRepository, EventAgendaItemMapper eventAgendaItemMapper) {
        this.eventAgendaItemRepository = eventAgendaItemRepository;
        this.eventAgendaItemMapper = eventAgendaItemMapper;
    }

    @Override
    public EventAgendaItemDTO save(EventAgendaItemDTO eventAgendaItemDTO) {
        log.debug("Request to save EventAgendaItem : {}", eventAgendaItemDTO);
        EventAgendaItem entity = eventAgendaItemMapper.toEntity(eventAgendaItemDTO);
        if (entity.getId() != null) {
            log.warn("EventAgendaItem has ID {} set during create operation. Clearing ID to force sequence generation.", entity.getId());
            entity.setId(null);
        }
        ZonedDateTime now = ZonedDateTime.now();
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
        if (entity.getSortOrder() == null) {
            entity.setSortOrder(0);
        }
        if (entity.getIsPublished() == null) {
            entity.setIsPublished(Boolean.TRUE);
        }
        entity = eventAgendaItemRepository.save(entity);
        return eventAgendaItemMapper.toDto(entity);
    }

    @Override
    public EventAgendaItemDTO update(EventAgendaItemDTO eventAgendaItemDTO) {
        log.debug("Request to update EventAgendaItem : {}", eventAgendaItemDTO);
        EventAgendaItem entity = eventAgendaItemMapper.toEntity(eventAgendaItemDTO);
        entity.setUpdatedAt(ZonedDateTime.now());
        if (entity.getSortOrder() == null) {
            entity.setSortOrder(0);
        }
        if (entity.getIsPublished() == null) {
            entity.setIsPublished(Boolean.TRUE);
        }
        entity = eventAgendaItemRepository.save(entity);
        return eventAgendaItemMapper.toDto(entity);
    }

    @Override
    public Optional<EventAgendaItemDTO> partialUpdate(EventAgendaItemDTO eventAgendaItemDTO) {
        log.debug("Request to partially update EventAgendaItem : {}", eventAgendaItemDTO);
        return eventAgendaItemRepository
            .findById(eventAgendaItemDTO.getId())
            .map(existing -> {
                eventAgendaItemMapper.partialUpdate(existing, eventAgendaItemDTO);
                existing.setUpdatedAt(ZonedDateTime.now());
                return existing;
            })
            .map(eventAgendaItemRepository::save)
            .map(eventAgendaItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventAgendaItemDTO> findAll(Pageable pageable) {
        return eventAgendaItemRepository.findAll(pageable).map(eventAgendaItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventAgendaItemDTO> findOne(Long id) {
        return eventAgendaItemRepository.findById(id).map(eventAgendaItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        eventAgendaItemRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventAgendaItemDTO> findByEventId(Long eventId) {
        return findByEventId(eventId, false);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventAgendaItemDTO> findByEventId(Long eventId, boolean publishedOnly) {
        List<EventAgendaItem> items = publishedOnly
            ? eventAgendaItemRepository.findByEventIdAndIsPublishedTrueOrderBySortOrderAsc(eventId)
            : eventAgendaItemRepository.findByEventIdOrderBySortOrderAsc(eventId);
        return items.stream().map(eventAgendaItemMapper::toDto).collect(Collectors.toList());
    }
}
