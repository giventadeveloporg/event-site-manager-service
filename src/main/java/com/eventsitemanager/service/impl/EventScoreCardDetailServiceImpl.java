package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.EventScoreCardDetail;
import com.eventsitemanager.repository.EventScoreCardDetailRepository;
import com.eventsitemanager.service.EventScoreCardDetailService;
import com.eventsitemanager.service.dto.EventScoreCardDetailDTO;
import com.eventsitemanager.service.mapper.EventScoreCardDetailMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.eventsitemanager.domain.EventScoreCardDetail}.
 */
@Service
@Transactional
public class EventScoreCardDetailServiceImpl implements EventScoreCardDetailService {

    private final Logger log = LoggerFactory.getLogger(EventScoreCardDetailServiceImpl.class);

    private final EventScoreCardDetailRepository eventScoreCardDetailRepository;

    private final EventScoreCardDetailMapper eventScoreCardDetailMapper;

    public EventScoreCardDetailServiceImpl(
        EventScoreCardDetailRepository eventScoreCardDetailRepository,
        EventScoreCardDetailMapper eventScoreCardDetailMapper
    ) {
        this.eventScoreCardDetailRepository = eventScoreCardDetailRepository;
        this.eventScoreCardDetailMapper = eventScoreCardDetailMapper;
    }

    @Override
    public EventScoreCardDetailDTO save(EventScoreCardDetailDTO eventScoreCardDetailDTO) {
        log.debug("Request to save EventScoreCardDetail : {}", eventScoreCardDetailDTO);
        EventScoreCardDetail eventScoreCardDetail = eventScoreCardDetailMapper.toEntity(eventScoreCardDetailDTO);
        eventScoreCardDetail = eventScoreCardDetailRepository.save(eventScoreCardDetail);
        return eventScoreCardDetailMapper.toDto(eventScoreCardDetail);
    }

    @Override
    public EventScoreCardDetailDTO update(EventScoreCardDetailDTO eventScoreCardDetailDTO) {
        log.debug("Request to update EventScoreCardDetail : {}", eventScoreCardDetailDTO);
        EventScoreCardDetail eventScoreCardDetail = eventScoreCardDetailMapper.toEntity(eventScoreCardDetailDTO);
        eventScoreCardDetail = eventScoreCardDetailRepository.save(eventScoreCardDetail);
        return eventScoreCardDetailMapper.toDto(eventScoreCardDetail);
    }

    @Override
    public Optional<EventScoreCardDetailDTO> partialUpdate(EventScoreCardDetailDTO eventScoreCardDetailDTO) {
        log.debug("Request to partially update EventScoreCardDetail : {}", eventScoreCardDetailDTO);

        return eventScoreCardDetailRepository
            .findById(eventScoreCardDetailDTO.getId())
            .map(existingEventScoreCardDetail -> {
                eventScoreCardDetailMapper.partialUpdate(existingEventScoreCardDetail, eventScoreCardDetailDTO);

                return existingEventScoreCardDetail;
            })
            .map(eventScoreCardDetailRepository::save)
            .map(eventScoreCardDetailMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventScoreCardDetailDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EventScoreCardDetails");
        return eventScoreCardDetailRepository.findAll(pageable).map(eventScoreCardDetailMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventScoreCardDetailDTO> findOne(Long id) {
        log.debug("Request to get EventScoreCardDetail : {}", id);
        return eventScoreCardDetailRepository.findById(id).map(eventScoreCardDetailMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EventScoreCardDetail : {}", id);
        eventScoreCardDetailRepository.deleteById(id);
    }
}
