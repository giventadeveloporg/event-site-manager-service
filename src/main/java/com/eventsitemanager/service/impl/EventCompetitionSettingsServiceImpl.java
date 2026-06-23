package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.EventCompetitionSettings;
import com.eventsitemanager.repository.EventCompetitionSettingsRepository;
import com.eventsitemanager.service.EventCompetitionSettingsService;
import com.eventsitemanager.service.dto.EventCompetitionSettingsDTO;
import com.eventsitemanager.service.mapper.EventCompetitionSettingsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EventCompetitionSettingsServiceImpl implements EventCompetitionSettingsService {

    private final Logger log = LoggerFactory.getLogger(EventCompetitionSettingsServiceImpl.class);

    private final EventCompetitionSettingsRepository eventCompetitionSettingsRepository;

    private final EventCompetitionSettingsMapper eventCompetitionSettingsMapper;

    public EventCompetitionSettingsServiceImpl(
        EventCompetitionSettingsRepository eventCompetitionSettingsRepository,
        EventCompetitionSettingsMapper eventCompetitionSettingsMapper
    ) {
        this.eventCompetitionSettingsRepository = eventCompetitionSettingsRepository;
        this.eventCompetitionSettingsMapper = eventCompetitionSettingsMapper;
    }

    @Override
    public EventCompetitionSettingsDTO save(EventCompetitionSettingsDTO eventCompetitionSettingsDTO) {
        log.debug("Request to save EventCompetitionSettings : {}", eventCompetitionSettingsDTO);
        EventCompetitionSettings entity = eventCompetitionSettingsMapper.toEntity(eventCompetitionSettingsDTO);
        if (entity.getId() != null) {
            log.warn(
                "EventCompetitionSettings has ID {} set during create operation. Clearing ID to force sequence generation.",
                entity.getId()
            );
            entity.setId(null);
        }
        entity = eventCompetitionSettingsRepository.save(entity);
        return eventCompetitionSettingsMapper.toDto(entity);
    }

    @Override
    public EventCompetitionSettingsDTO update(EventCompetitionSettingsDTO eventCompetitionSettingsDTO) {
        log.debug("Request to update EventCompetitionSettings : {}", eventCompetitionSettingsDTO);
        EventCompetitionSettings entity = eventCompetitionSettingsMapper.toEntity(eventCompetitionSettingsDTO);
        entity = eventCompetitionSettingsRepository.save(entity);
        return eventCompetitionSettingsMapper.toDto(entity);
    }

    @Override
    public Optional<EventCompetitionSettingsDTO> partialUpdate(EventCompetitionSettingsDTO eventCompetitionSettingsDTO) {
        log.debug("Request to partially update EventCompetitionSettings : {}", eventCompetitionSettingsDTO);
        return eventCompetitionSettingsRepository
            .findById(eventCompetitionSettingsDTO.getId())
            .map(existing -> {
                eventCompetitionSettingsMapper.partialUpdate(existing, eventCompetitionSettingsDTO);
                return existing;
            })
            .map(eventCompetitionSettingsRepository::save)
            .map(eventCompetitionSettingsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventCompetitionSettingsDTO> findAll(Pageable pageable) {
        return eventCompetitionSettingsRepository.findAll(pageable).map(eventCompetitionSettingsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventCompetitionSettingsDTO> findOne(Long id) {
        return eventCompetitionSettingsRepository.findById(id).map(eventCompetitionSettingsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        eventCompetitionSettingsRepository.deleteById(id);
    }
}
