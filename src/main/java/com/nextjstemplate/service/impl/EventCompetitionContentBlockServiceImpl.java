package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.EventCompetitionContentBlock;
import com.nextjstemplate.repository.EventCompetitionContentBlockRepository;
import com.nextjstemplate.service.EventCompetitionContentBlockService;
import com.nextjstemplate.service.dto.EventCompetitionContentBlockDTO;
import com.nextjstemplate.service.mapper.EventCompetitionContentBlockMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EventCompetitionContentBlockServiceImpl implements EventCompetitionContentBlockService {

    private final Logger log = LoggerFactory.getLogger(EventCompetitionContentBlockServiceImpl.class);

    private final EventCompetitionContentBlockRepository eventCompetitionContentBlockRepository;

    private final EventCompetitionContentBlockMapper eventCompetitionContentBlockMapper;

    public EventCompetitionContentBlockServiceImpl(EventCompetitionContentBlockRepository eventCompetitionContentBlockRepository, EventCompetitionContentBlockMapper eventCompetitionContentBlockMapper) {
        this.eventCompetitionContentBlockRepository = eventCompetitionContentBlockRepository;
        this.eventCompetitionContentBlockMapper = eventCompetitionContentBlockMapper;
    }

    @Override
    public EventCompetitionContentBlockDTO save(EventCompetitionContentBlockDTO eventCompetitionContentBlockDTO) {
        log.debug("Request to save EventCompetitionContentBlock : {}", eventCompetitionContentBlockDTO);
        EventCompetitionContentBlock entity = eventCompetitionContentBlockMapper.toEntity(eventCompetitionContentBlockDTO);
        if (entity.getId() != null) {
            log.warn("EventCompetitionContentBlock has ID {} set during create operation. Clearing ID to force sequence generation.", entity.getId());
            entity.setId(null);
        }
        entity = eventCompetitionContentBlockRepository.save(entity);
        return eventCompetitionContentBlockMapper.toDto(entity);
    }

    @Override
    public EventCompetitionContentBlockDTO update(EventCompetitionContentBlockDTO eventCompetitionContentBlockDTO) {
        log.debug("Request to update EventCompetitionContentBlock : {}", eventCompetitionContentBlockDTO);
        EventCompetitionContentBlock entity = eventCompetitionContentBlockMapper.toEntity(eventCompetitionContentBlockDTO);
        entity = eventCompetitionContentBlockRepository.save(entity);
        return eventCompetitionContentBlockMapper.toDto(entity);
    }

    @Override
    public Optional<EventCompetitionContentBlockDTO> partialUpdate(EventCompetitionContentBlockDTO eventCompetitionContentBlockDTO) {
        log.debug("Request to partially update EventCompetitionContentBlock : {}", eventCompetitionContentBlockDTO);
        return eventCompetitionContentBlockRepository
            .findById(eventCompetitionContentBlockDTO.getId())
            .map(existing -> {
                eventCompetitionContentBlockMapper.partialUpdate(existing, eventCompetitionContentBlockDTO);
                return existing;
            })
            .map(eventCompetitionContentBlockRepository::save)
            .map(eventCompetitionContentBlockMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventCompetitionContentBlockDTO> findAll(Pageable pageable) {
        return eventCompetitionContentBlockRepository.findAll(pageable).map(eventCompetitionContentBlockMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventCompetitionContentBlockDTO> findOne(Long id) {
        return eventCompetitionContentBlockRepository.findById(id).map(eventCompetitionContentBlockMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        eventCompetitionContentBlockRepository.deleteById(id);
    }
}
