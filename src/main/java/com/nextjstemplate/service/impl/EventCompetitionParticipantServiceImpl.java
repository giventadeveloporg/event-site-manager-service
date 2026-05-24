package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.EventCompetitionParticipant;
import com.nextjstemplate.repository.EventCompetitionParticipantRepository;
import com.nextjstemplate.service.EventCompetitionParticipantService;
import com.nextjstemplate.service.dto.EventCompetitionParticipantDTO;
import com.nextjstemplate.service.mapper.EventCompetitionParticipantMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EventCompetitionParticipantServiceImpl implements EventCompetitionParticipantService {

    private final Logger log = LoggerFactory.getLogger(EventCompetitionParticipantServiceImpl.class);

    private final EventCompetitionParticipantRepository eventCompetitionParticipantRepository;

    private final EventCompetitionParticipantMapper eventCompetitionParticipantMapper;

    public EventCompetitionParticipantServiceImpl(EventCompetitionParticipantRepository eventCompetitionParticipantRepository, EventCompetitionParticipantMapper eventCompetitionParticipantMapper) {
        this.eventCompetitionParticipantRepository = eventCompetitionParticipantRepository;
        this.eventCompetitionParticipantMapper = eventCompetitionParticipantMapper;
    }

    @Override
    public EventCompetitionParticipantDTO save(EventCompetitionParticipantDTO eventCompetitionParticipantDTO) {
        log.debug("Request to save EventCompetitionParticipant : {}", eventCompetitionParticipantDTO);
        EventCompetitionParticipant entity = eventCompetitionParticipantMapper.toEntity(eventCompetitionParticipantDTO);
        if (entity.getId() != null) {
            log.warn("EventCompetitionParticipant has ID {} set during create operation. Clearing ID to force sequence generation.", entity.getId());
            entity.setId(null);
        }
        entity = eventCompetitionParticipantRepository.save(entity);
        return eventCompetitionParticipantMapper.toDto(entity);
    }

    @Override
    public EventCompetitionParticipantDTO update(EventCompetitionParticipantDTO eventCompetitionParticipantDTO) {
        log.debug("Request to update EventCompetitionParticipant : {}", eventCompetitionParticipantDTO);
        EventCompetitionParticipant entity = eventCompetitionParticipantMapper.toEntity(eventCompetitionParticipantDTO);
        entity = eventCompetitionParticipantRepository.save(entity);
        return eventCompetitionParticipantMapper.toDto(entity);
    }

    @Override
    public Optional<EventCompetitionParticipantDTO> partialUpdate(EventCompetitionParticipantDTO eventCompetitionParticipantDTO) {
        log.debug("Request to partially update EventCompetitionParticipant : {}", eventCompetitionParticipantDTO);
        return eventCompetitionParticipantRepository
            .findById(eventCompetitionParticipantDTO.getId())
            .map(existing -> {
                eventCompetitionParticipantMapper.partialUpdate(existing, eventCompetitionParticipantDTO);
                return existing;
            })
            .map(eventCompetitionParticipantRepository::save)
            .map(eventCompetitionParticipantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventCompetitionParticipantDTO> findAll(Pageable pageable) {
        return eventCompetitionParticipantRepository.findAll(pageable).map(eventCompetitionParticipantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventCompetitionParticipantDTO> findOne(Long id) {
        return eventCompetitionParticipantRepository.findById(id).map(eventCompetitionParticipantMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        eventCompetitionParticipantRepository.deleteById(id);
    }
}
