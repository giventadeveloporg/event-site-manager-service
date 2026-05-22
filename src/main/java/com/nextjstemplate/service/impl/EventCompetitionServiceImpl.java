package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.EventCompetition;
import com.nextjstemplate.repository.EventCompetitionRepository;
import com.nextjstemplate.service.EventCompetitionService;
import com.nextjstemplate.service.dto.EventCompetitionDTO;
import com.nextjstemplate.service.mapper.EventCompetitionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EventCompetitionServiceImpl implements EventCompetitionService {

    private final Logger log = LoggerFactory.getLogger(EventCompetitionServiceImpl.class);

    private final EventCompetitionRepository eventCompetitionRepository;

    private final EventCompetitionMapper eventCompetitionMapper;

    public EventCompetitionServiceImpl(EventCompetitionRepository eventCompetitionRepository, EventCompetitionMapper eventCompetitionMapper) {
        this.eventCompetitionRepository = eventCompetitionRepository;
        this.eventCompetitionMapper = eventCompetitionMapper;
    }

    @Override
    public EventCompetitionDTO save(EventCompetitionDTO eventCompetitionDTO) {
        log.debug("Request to save EventCompetition : {}", eventCompetitionDTO);
        EventCompetition entity = eventCompetitionMapper.toEntity(eventCompetitionDTO);
        if (entity.getId() != null) {
            log.warn("EventCompetition has ID {} set during create operation. Clearing ID to force sequence generation.", entity.getId());
            entity.setId(null);
        }
        entity = eventCompetitionRepository.save(entity);
        return eventCompetitionMapper.toDto(entity);
    }

    @Override
    public EventCompetitionDTO update(EventCompetitionDTO eventCompetitionDTO) {
        log.debug("Request to update EventCompetition : {}", eventCompetitionDTO);
        EventCompetition entity = eventCompetitionMapper.toEntity(eventCompetitionDTO);
        entity = eventCompetitionRepository.save(entity);
        return eventCompetitionMapper.toDto(entity);
    }

    @Override
    public Optional<EventCompetitionDTO> partialUpdate(EventCompetitionDTO eventCompetitionDTO) {
        log.debug("Request to partially update EventCompetition : {}", eventCompetitionDTO);
        return eventCompetitionRepository
            .findById(eventCompetitionDTO.getId())
            .map(existing -> {
                eventCompetitionMapper.partialUpdate(existing, eventCompetitionDTO);
                return existing;
            })
            .map(eventCompetitionRepository::save)
            .map(eventCompetitionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventCompetitionDTO> findAll(Pageable pageable) {
        return eventCompetitionRepository.findAll(pageable).map(eventCompetitionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventCompetitionDTO> findOne(Long id) {
        return eventCompetitionRepository.findById(id).map(eventCompetitionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        eventCompetitionRepository.deleteById(id);
    }
}
