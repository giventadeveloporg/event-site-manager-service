package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.EventAttendeeAttachment;
import com.nextjstemplate.service.dto.EventAttendeeAttachmentDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventAttendeeAttachmentMapper extends EntityMapper<EventAttendeeAttachmentDTO, EventAttendeeAttachment> {}
