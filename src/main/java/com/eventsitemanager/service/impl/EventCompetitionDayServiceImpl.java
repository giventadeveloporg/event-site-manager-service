package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.EventCompetitionDay;
import com.eventsitemanager.repository.EventCompetitionDayRepository;
import com.eventsitemanager.service.EventCompetitionDayService;
import com.eventsitemanager.service.dto.EventCompetitionDayDTO;
import com.eventsitemanager.service.mapper.EventCompetitionDayMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EventCompetitionDayServiceImpl implements EventCompetitionDayService {

    private final Logger log = LoggerFactory.getLogger(EventCompetitionDayServiceImpl.class);

    private final EventCompetitionDayRepository eventCompetitionDayRepository;

    private final EventCompetitionDayMapper eventCompetitionDayMapper;

    public EventCompetitionDayServiceImpl(
        EventCompetitionDayRepository eventCompetitionDayRepository,
        EventCompetitionDayMapper eventCompetitionDayMapper
    ) {
        this.eventCompetitionDayRepository = eventCompetitionDayRepository;
        this.eventCompetitionDayMapper = eventCompetitionDayMapper;
    }

    @Override
    public EventCompetitionDayDTO save(EventCompetitionDayDTO eventCompetitionDayDTO) {
        log.debug("Request to save EventCompetitionDay : {}", eventCompetitionDayDTO);
        EventCompetitionDay entity = eventCompetitionDayMapper.toEntity(eventCompetitionDayDTO);
        if (entity.getId() != null) {
            log.warn(
                "EventCompetitionDay has ID {} set during create operation. Clearing ID to force sequence generation.",
                entity.getId()
            );
            entity.setId(null);
        }
        entity = eventCompetitionDayRepository.save(entity);
        return eventCompetitionDayMapper.toDto(entity);
    }

    @Override
    public EventCompetitionDayDTO update(EventCompetitionDayDTO eventCompetitionDayDTO) {
        log.debug("Request to update EventCompetitionDay : {}", eventCompetitionDayDTO);
        EventCompetitionDay entity = eventCompetitionDayMapper.toEntity(eventCompetitionDayDTO);
        entity = eventCompetitionDayRepository.save(entity);
        return eventCompetitionDayMapper.toDto(entity);
    }

    @Override
    public Optional<EventCompetitionDayDTO> partialUpdate(EventCompetitionDayDTO eventCompetitionDayDTO) {
        log.debug("Request to partially update EventCompetitionDay : {}", eventCompetitionDayDTO);
        return eventCompetitionDayRepository
            .findById(eventCompetitionDayDTO.getId())
            .map(existing -> {
                eventCompetitionDayMapper.partialUpdate(existing, eventCompetitionDayDTO);
                return existing;
            })
            .map(eventCompetitionDayRepository::save)
            .map(eventCompetitionDayMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventCompetitionDayDTO> findAll(Pageable pageable) {
        return eventCompetitionDayRepository.findAll(pageable).map(eventCompetitionDayMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventCompetitionDayDTO> findOne(Long id) {
        return eventCompetitionDayRepository.findById(id).map(eventCompetitionDayMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        eventCompetitionDayRepository.deleteById(id);
    }
}
