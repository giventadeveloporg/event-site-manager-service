package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.EventAttendeeAttachment;
import com.eventsitemanager.service.dto.EventAttendeeAttachmentDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventAttendeeAttachmentMapper extends EntityMapper<EventAttendeeAttachmentDTO, EventAttendeeAttachment> {}
