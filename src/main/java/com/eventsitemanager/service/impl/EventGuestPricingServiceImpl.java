package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.EventGuestPricing;
import com.eventsitemanager.repository.EventGuestPricingRepository;
import com.eventsitemanager.service.EventGuestPricingService;
import com.eventsitemanager.service.dto.EventGuestPricingDTO;
import com.eventsitemanager.service.mapper.EventGuestPricingMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.eventsitemanager.domain.EventGuestPricing}.
 */
@Service
@Transactional
public class EventGuestPricingServiceImpl implements EventGuestPricingService {

    private final Logger log = LoggerFactory.getLogger(EventGuestPricingServiceImpl.class);

    private final EventGuestPricingRepository eventGuestPricingRepository;

    private final EventGuestPricingMapper eventGuestPricingMapper;

    public EventGuestPricingServiceImpl(
        EventGuestPricingRepository eventGuestPricingRepository,
        EventGuestPricingMapper eventGuestPricingMapper
    ) {
        this.eventGuestPricingRepository = eventGuestPricingRepository;
        this.eventGuestPricingMapper = eventGuestPricingMapper;
    }

    @Override
    public EventGuestPricingDTO save(EventGuestPricingDTO eventGuestPricingDTO) {
        log.debug("Request to save EventGuestPricing : {}", eventGuestPricingDTO);
        EventGuestPricing eventGuestPricing = eventGuestPricingMapper.toEntity(eventGuestPricingDTO);
        eventGuestPricing = eventGuestPricingRepository.save(eventGuestPricing);
        return eventGuestPricingMapper.toDto(eventGuestPricing);
    }

    @Override
    public EventGuestPricingDTO update(EventGuestPricingDTO eventGuestPricingDTO) {
        log.debug("Request to update EventGuestPricing : {}", eventGuestPricingDTO);
        EventGuestPricing eventGuestPricing = eventGuestPricingMapper.toEntity(eventGuestPricingDTO);
        eventGuestPricing = eventGuestPricingRepository.save(eventGuestPricing);
        return eventGuestPricingMapper.toDto(eventGuestPricing);
    }

    @Override
    public Optional<EventGuestPricingDTO> partialUpdate(EventGuestPricingDTO eventGuestPricingDTO) {
        log.debug("Request to partially update EventGuestPricing : {}", eventGuestPricingDTO);

        return eventGuestPricingRepository
            .findById(eventGuestPricingDTO.getId())
            .map(existingEventGuestPricing -> {
                eventGuestPricingMapper.partialUpdate(existingEventGuestPricing, eventGuestPricingDTO);

                return existingEventGuestPricing;
            })
            .map(eventGuestPricingRepository::save)
            .map(eventGuestPricingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventGuestPricingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EventGuestPricings");
        return eventGuestPricingRepository.findAll(pageable).map(eventGuestPricingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventGuestPricingDTO> findOne(Long id) {
        log.debug("Request to get EventGuestPricing : {}", id);
        return eventGuestPricingRepository.findById(id).map(eventGuestPricingMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EventGuestPricing : {}", id);
        eventGuestPricingRepository.deleteById(id);
    }
}
