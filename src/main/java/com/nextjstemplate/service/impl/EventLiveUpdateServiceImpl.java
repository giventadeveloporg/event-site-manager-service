package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.EventLiveUpdate;
import com.nextjstemplate.repository.EventLiveUpdateRepository;
import com.nextjstemplate.service.EventLiveUpdateService;
import com.nextjstemplate.service.dto.EventLiveUpdateDTO;
import com.nextjstemplate.service.mapper.EventLiveUpdateMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nextjstemplate.domain.EventLiveUpdate}.
 */
@Service
@Transactional
public class EventLiveUpdateServiceImpl implements EventLiveUpdateService {

    private static final Logger LOG = LoggerFactory.getLogger(EventLiveUpdateServiceImpl.class);

    private final EventLiveUpdateRepository eventLiveUpdateRepository;

    private final EventLiveUpdateMapper eventLiveUpdateMapper;

    public EventLiveUpdateServiceImpl(EventLiveUpdateRepository eventLiveUpdateRepository, EventLiveUpdateMapper eventLiveUpdateMapper) {
        this.eventLiveUpdateRepository = eventLiveUpdateRepository;
        this.eventLiveUpdateMapper = eventLiveUpdateMapper;
    }

    @Override
    public EventLiveUpdateDTO save(EventLiveUpdateDTO eventLiveUpdateDTO) {
        LOG.debug("Request to save EventLiveUpdate : {}", eventLiveUpdateDTO);
        EventLiveUpdate eventLiveUpdate = eventLiveUpdateMapper.toEntity(eventLiveUpdateDTO);
        eventLiveUpdate = eventLiveUpdateRepository.save(eventLiveUpdate);
        return eventLiveUpdateMapper.toDto(eventLiveUpdate);
    }

    @Override
    public EventLiveUpdateDTO update(EventLiveUpdateDTO eventLiveUpdateDTO) {
        LOG.debug("Request to update EventLiveUpdate : {}", eventLiveUpdateDTO);
        EventLiveUpdate eventLiveUpdate = eventLiveUpdateMapper.toEntity(eventLiveUpdateDTO);
        eventLiveUpdate = eventLiveUpdateRepository.save(eventLiveUpdate);
        return eventLiveUpdateMapper.toDto(eventLiveUpdate);
    }

    @Override
    public Optional<EventLiveUpdateDTO> partialUpdate(EventLiveUpdateDTO eventLiveUpdateDTO) {
        LOG.debug("Request to partially update EventLiveUpdate : {}", eventLiveUpdateDTO);

        return eventLiveUpdateRepository
            .findById(eventLiveUpdateDTO.getId())
            .map(existingEventLiveUpdate -> {
                eventLiveUpdateMapper.partialUpdate(existingEventLiveUpdate, eventLiveUpdateDTO);

                return existingEventLiveUpdate;
            })
            .map(eventLiveUpdateRepository::save)
            .map(eventLiveUpdateMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventLiveUpdateDTO> findOne(Long id) {
        LOG.debug("Request to get EventLiveUpdate : {}", id);
        return eventLiveUpdateRepository.findById(id).map(eventLiveUpdateMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete EventLiveUpdate : {}", id);
        eventLiveUpdateRepository.deleteById(id);
    }
}
