package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.EventAdmin;
import com.eventsitemanager.repository.EventAdminRepository;
import com.eventsitemanager.service.EventAdminService;
import com.eventsitemanager.service.dto.EventAdminDTO;
import com.eventsitemanager.service.mapper.EventAdminMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.eventsitemanager.domain.EventAdmin}.
 */
@Service
@Transactional
public class EventAdminServiceImpl implements EventAdminService {

    private final Logger log = LoggerFactory.getLogger(EventAdminServiceImpl.class);

    private final EventAdminRepository eventAdminRepository;

    private final EventAdminMapper eventAdminMapper;

    public EventAdminServiceImpl(EventAdminRepository eventAdminRepository, EventAdminMapper eventAdminMapper) {
        this.eventAdminRepository = eventAdminRepository;
        this.eventAdminMapper = eventAdminMapper;
    }

    @Override
    public EventAdminDTO save(EventAdminDTO eventAdminDTO) {
        log.debug("Request to save EventAdmin : {}", eventAdminDTO);
        EventAdmin eventAdmin = eventAdminMapper.toEntity(eventAdminDTO);
        eventAdmin = eventAdminRepository.save(eventAdmin);
        return eventAdminMapper.toDto(eventAdmin);
    }

    @Override
    public EventAdminDTO update(EventAdminDTO eventAdminDTO) {
        log.debug("Request to update EventAdmin : {}", eventAdminDTO);
        EventAdmin eventAdmin = eventAdminMapper.toEntity(eventAdminDTO);
        eventAdmin = eventAdminRepository.save(eventAdmin);
        return eventAdminMapper.toDto(eventAdmin);
    }

    @Override
    public Optional<EventAdminDTO> partialUpdate(EventAdminDTO eventAdminDTO) {
        log.debug("Request to partially update EventAdmin : {}", eventAdminDTO);

        return eventAdminRepository
            .findById(eventAdminDTO.getId())
            .map(existingEventAdmin -> {
                eventAdminMapper.partialUpdate(existingEventAdmin, eventAdminDTO);

                return existingEventAdmin;
            })
            .map(eventAdminRepository::save)
            .map(eventAdminMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventAdminDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EventAdmins");
        return eventAdminRepository.findAll(pageable).map(eventAdminMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventAdminDTO> findOne(Long id) {
        log.debug("Request to get EventAdmin : {}", id);
        return eventAdminRepository.findById(id).map(eventAdminMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EventAdmin : {}", id);
        eventAdminRepository.deleteById(id);
    }
}
