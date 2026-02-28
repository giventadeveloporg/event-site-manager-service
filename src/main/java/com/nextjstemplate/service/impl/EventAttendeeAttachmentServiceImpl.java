package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.EventAttendeeAttachment;
import com.nextjstemplate.repository.EventAttendeeAttachmentRepository;
import com.nextjstemplate.service.EventAttendeeAttachmentService;
import com.nextjstemplate.service.dto.EventAttendeeAttachmentDTO;
import com.nextjstemplate.service.mapper.EventAttendeeAttachmentMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EventAttendeeAttachmentServiceImpl implements EventAttendeeAttachmentService {

    private final Logger log = LoggerFactory.getLogger(EventAttendeeAttachmentServiceImpl.class);

    private final EventAttendeeAttachmentRepository repository;
    private final EventAttendeeAttachmentMapper mapper;

    public EventAttendeeAttachmentServiceImpl(EventAttendeeAttachmentRepository repository, EventAttendeeAttachmentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public EventAttendeeAttachmentDTO save(EventAttendeeAttachmentDTO dto) {
        log.debug("Request to save EventAttendeeAttachment : {}", dto);
        EventAttendeeAttachment entity = mapper.toEntity(dto);

        // Ensure ID is null for new entities to force sequence generation
        // This prevents duplicate key errors when entity has ID set from DTO (see .cursor/rules/duplicate-key-prevention.mdc)
        if (entity.getId() != null) {
            log.warn(
                "EventAttendeeAttachment has ID {} set during create operation. Clearing ID to force sequence generation.",
                entity.getId()
            );
            entity.setId(null);
        }

        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    @Override
    public EventAttendeeAttachmentDTO update(EventAttendeeAttachmentDTO dto) {
        log.debug("Request to update EventAttendeeAttachment : {}", dto);
        EventAttendeeAttachment entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    @Override
    public Optional<EventAttendeeAttachmentDTO> partialUpdate(EventAttendeeAttachmentDTO dto) {
        log.debug("Request to partially update EventAttendeeAttachment : {}", dto);
        return repository
            .findById(dto.getId())
            .map(existing -> {
                mapper.partialUpdate(existing, dto);
                return existing;
            })
            .map(repository::save)
            .map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventAttendeeAttachmentDTO> findAll(String tenantId, Long attendeeId, Long eventId) {
        if (tenantId == null || tenantId.trim().isEmpty()) {
            return List.of();
        }

        List<EventAttendeeAttachment> rows;
        if (attendeeId != null && eventId != null) {
            rows = repository.findByTenantIdAndAttendeeIdAndEventIdOrderByCreatedAtDesc(tenantId, attendeeId, eventId);
        } else if (attendeeId != null) {
            rows = repository.findByTenantIdAndAttendeeIdOrderByCreatedAtDesc(tenantId, attendeeId);
        } else if (eventId != null) {
            rows = repository.findByTenantIdAndEventIdOrderByCreatedAtDesc(tenantId, eventId);
        } else {
            rows = repository.findByTenantIdOrderByCreatedAtDesc(tenantId);
        }

        return mapper.toDto(rows);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventAttendeeAttachmentDTO> findOne(Long id) {
        return repository.findById(id).map(mapper::toDto);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
