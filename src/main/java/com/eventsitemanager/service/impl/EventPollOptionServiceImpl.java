package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.EventPollOption;
import com.eventsitemanager.repository.EventPollOptionRepository;
import com.eventsitemanager.service.EventPollOptionService;
import com.eventsitemanager.service.dto.EventPollOptionDTO;
import com.eventsitemanager.service.mapper.EventPollOptionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.eventsitemanager.domain.EventPollOption}.
 */
@Service
@Transactional
public class EventPollOptionServiceImpl implements EventPollOptionService {

    private final Logger log = LoggerFactory.getLogger(EventPollOptionServiceImpl.class);

    private final EventPollOptionRepository eventPollOptionRepository;

    private final EventPollOptionMapper eventPollOptionMapper;

    public EventPollOptionServiceImpl(EventPollOptionRepository eventPollOptionRepository, EventPollOptionMapper eventPollOptionMapper) {
        this.eventPollOptionRepository = eventPollOptionRepository;
        this.eventPollOptionMapper = eventPollOptionMapper;
    }

    @Override
    public EventPollOptionDTO save(EventPollOptionDTO eventPollOptionDTO) {
        log.debug("Request to save EventPollOption : {}", eventPollOptionDTO);
        EventPollOption eventPollOption = eventPollOptionMapper.toEntity(eventPollOptionDTO);
        eventPollOption = eventPollOptionRepository.save(eventPollOption);
        return eventPollOptionMapper.toDto(eventPollOption);
    }

    @Override
    public EventPollOptionDTO update(EventPollOptionDTO eventPollOptionDTO) {
        log.debug("Request to update EventPollOption : {}", eventPollOptionDTO);
        EventPollOption eventPollOption = eventPollOptionMapper.toEntity(eventPollOptionDTO);
        eventPollOption = eventPollOptionRepository.save(eventPollOption);
        return eventPollOptionMapper.toDto(eventPollOption);
    }

    @Override
    public Optional<EventPollOptionDTO> partialUpdate(EventPollOptionDTO eventPollOptionDTO) {
        log.debug("Request to partially update EventPollOption : {}", eventPollOptionDTO);

        return eventPollOptionRepository
            .findById(eventPollOptionDTO.getId())
            .map(existingEventPollOption -> {
                eventPollOptionMapper.partialUpdate(existingEventPollOption, eventPollOptionDTO);

                return existingEventPollOption;
            })
            .map(eventPollOptionRepository::save)
            .map(eventPollOptionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventPollOptionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EventPollOptions");
        return eventPollOptionRepository.findAll(pageable).map(eventPollOptionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventPollOptionDTO> findOne(Long id) {
        log.debug("Request to get EventPollOption : {}", id);
        return eventPollOptionRepository.findById(id).map(eventPollOptionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EventPollOption : {}", id);
        eventPollOptionRepository.deleteById(id);
    }
}
