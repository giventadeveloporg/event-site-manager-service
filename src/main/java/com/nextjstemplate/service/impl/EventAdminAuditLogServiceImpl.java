package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.EventAdminAuditLog;
import com.nextjstemplate.repository.EventAdminAuditLogRepository;
import com.nextjstemplate.service.EventAdminAuditLogService;
import com.nextjstemplate.service.dto.EventAdminAuditLogDTO;
import com.nextjstemplate.service.mapper.EventAdminAuditLogMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nextjstemplate.domain.EventAdminAuditLog}.
 */
@Service
@Transactional
public class EventAdminAuditLogServiceImpl implements EventAdminAuditLogService {

    private static final Logger LOG = LoggerFactory.getLogger(EventAdminAuditLogServiceImpl.class);

    private final EventAdminAuditLogRepository eventAdminAuditLogRepository;

    private final EventAdminAuditLogMapper eventAdminAuditLogMapper;

    public EventAdminAuditLogServiceImpl(
        EventAdminAuditLogRepository eventAdminAuditLogRepository,
        EventAdminAuditLogMapper eventAdminAuditLogMapper
    ) {
        this.eventAdminAuditLogRepository = eventAdminAuditLogRepository;
        this.eventAdminAuditLogMapper = eventAdminAuditLogMapper;
    }

    @Override
    public EventAdminAuditLogDTO save(EventAdminAuditLogDTO eventAdminAuditLogDTO) {
        LOG.debug("Request to save EventAdminAuditLog : {}", eventAdminAuditLogDTO);
        EventAdminAuditLog eventAdminAuditLog = eventAdminAuditLogMapper.toEntity(eventAdminAuditLogDTO);
        eventAdminAuditLog = eventAdminAuditLogRepository.save(eventAdminAuditLog);
        return eventAdminAuditLogMapper.toDto(eventAdminAuditLog);
    }

    @Override
    public EventAdminAuditLogDTO update(EventAdminAuditLogDTO eventAdminAuditLogDTO) {
        LOG.debug("Request to update EventAdminAuditLog : {}", eventAdminAuditLogDTO);
        EventAdminAuditLog eventAdminAuditLog = eventAdminAuditLogMapper.toEntity(eventAdminAuditLogDTO);
        eventAdminAuditLog = eventAdminAuditLogRepository.save(eventAdminAuditLog);
        return eventAdminAuditLogMapper.toDto(eventAdminAuditLog);
    }

    @Override
    public Optional<EventAdminAuditLogDTO> partialUpdate(EventAdminAuditLogDTO eventAdminAuditLogDTO) {
        LOG.debug("Request to partially update EventAdminAuditLog : {}", eventAdminAuditLogDTO);

        return eventAdminAuditLogRepository
            .findById(eventAdminAuditLogDTO.getId())
            .map(existingEventAdminAuditLog -> {
                eventAdminAuditLogMapper.partialUpdate(existingEventAdminAuditLog, eventAdminAuditLogDTO);

                return existingEventAdminAuditLog;
            })
            .map(eventAdminAuditLogRepository::save)
            .map(eventAdminAuditLogMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventAdminAuditLogDTO> findOne(Long id) {
        LOG.debug("Request to get EventAdminAuditLog : {}", id);
        return eventAdminAuditLogRepository.findById(id).map(eventAdminAuditLogMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete EventAdminAuditLog : {}", id);
        eventAdminAuditLogRepository.deleteById(id);
    }
}
