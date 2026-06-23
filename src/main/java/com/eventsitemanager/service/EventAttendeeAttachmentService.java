package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.EventAttendeeAttachmentDTO;
import java.util.List;
import java.util.Optional;

public interface EventAttendeeAttachmentService {
    EventAttendeeAttachmentDTO save(EventAttendeeAttachmentDTO dto);

    EventAttendeeAttachmentDTO update(EventAttendeeAttachmentDTO dto);

    Optional<EventAttendeeAttachmentDTO> partialUpdate(EventAttendeeAttachmentDTO dto);

    List<EventAttendeeAttachmentDTO> findAll(String tenantId, Long attendeeId, Long eventId);

    Optional<EventAttendeeAttachmentDTO> findOne(Long id);

    void delete(Long id);
}
