package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.EventCompetitionResult;
import com.nextjstemplate.domain.EventMedia;
import com.nextjstemplate.errors.BadRequestAlertException;
import com.nextjstemplate.repository.EventCompetitionResultRepository;
import com.nextjstemplate.repository.EventMediaRepository;
import com.nextjstemplate.service.EventCompetitionResultService;
import com.nextjstemplate.service.dto.EventCompetitionResultDTO;
import com.nextjstemplate.service.mapper.EventCompetitionResultMapper;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EventCompetitionResultServiceImpl implements EventCompetitionResultService {

    private static final String ENTITY_NAME = "eventCompetitionResult";

    private final Logger log = LoggerFactory.getLogger(EventCompetitionResultServiceImpl.class);

    private final EventCompetitionResultRepository eventCompetitionResultRepository;

    private final EventCompetitionResultMapper eventCompetitionResultMapper;

    private final EventMediaRepository eventMediaRepository;

    public EventCompetitionResultServiceImpl(
        EventCompetitionResultRepository eventCompetitionResultRepository,
        EventCompetitionResultMapper eventCompetitionResultMapper,
        EventMediaRepository eventMediaRepository
    ) {
        this.eventCompetitionResultRepository = eventCompetitionResultRepository;
        this.eventCompetitionResultMapper = eventCompetitionResultMapper;
        this.eventMediaRepository = eventMediaRepository;
    }

    @Override
    public EventCompetitionResultDTO save(EventCompetitionResultDTO dto) {
        log.debug("Request to save EventCompetitionResult : {}", dto);
        EventCompetitionResult entity = eventCompetitionResultMapper.toEntity(dto);
        if (entity.getId() != null) {
            log.warn("EventCompetitionResult has ID {} set during create. Clearing ID.", entity.getId());
            entity.setId(null);
        }
        validateResult(entity);
        entity = eventCompetitionResultRepository.save(entity);
        return eventCompetitionResultMapper.toDto(entity);
    }

    @Override
    public EventCompetitionResultDTO update(EventCompetitionResultDTO dto) {
        log.debug("Request to update EventCompetitionResult : {}", dto);
        EventCompetitionResult entity = eventCompetitionResultMapper.toEntity(dto);
        validateResult(entity);
        entity = eventCompetitionResultRepository.save(entity);
        return eventCompetitionResultMapper.toDto(entity);
    }

    @Override
    public Optional<EventCompetitionResultDTO> partialUpdate(EventCompetitionResultDTO dto) {
        log.debug("Request to partially update EventCompetitionResult : {}", dto);
        return eventCompetitionResultRepository
            .findById(dto.getId())
            .map(existing -> {
                eventCompetitionResultMapper.partialUpdate(existing, dto);
                validateResult(existing);
                return existing;
            })
            .map(eventCompetitionResultRepository::save)
            .map(eventCompetitionResultMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventCompetitionResultDTO> findAll(Pageable pageable) {
        return eventCompetitionResultRepository.findAll(pageable).map(eventCompetitionResultMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventCompetitionResultDTO> findOne(Long id) {
        return eventCompetitionResultRepository.findById(id).map(eventCompetitionResultMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        eventCompetitionResultRepository.deleteById(id);
    }

    private void validateResult(EventCompetitionResult result) {
        if (StringUtils.isBlank(result.getDisplayName())) {
            throw new BadRequestAlertException("displayName is required", ENTITY_NAME, "displaynamerequired");
        }
        if (result.getWinnerMedia() != null && result.getWinnerMedia().getId() != null) {
            EventMedia media = eventMediaRepository
                .findById(result.getWinnerMedia().getId())
                .orElseThrow(() -> new BadRequestAlertException("Winner media not found", ENTITY_NAME, "winnermedianotfound"));
            if (result.getEvent() == null || result.getEvent().getId() == null || media.getEventId() == null) {
                throw new BadRequestAlertException("Event is required for winner media", ENTITY_NAME, "eventrequired");
            }
            if (!result.getEvent().getId().equals(media.getEventId())) {
                throw new BadRequestAlertException("Winner media must belong to the same event", ENTITY_NAME, "winnermediaeventmismatch");
            }
        }
    }
}
