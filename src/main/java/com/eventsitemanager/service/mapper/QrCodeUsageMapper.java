package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.EventAttendee;
import com.eventsitemanager.domain.QrCodeUsage;
import com.eventsitemanager.service.dto.EventAttendeeDTO;
import com.eventsitemanager.service.dto.QrCodeUsageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link QrCodeUsage} and its DTO {@link QrCodeUsageDTO}.
 */
@Mapper(componentModel = "spring")
public interface QrCodeUsageMapper extends EntityMapper<QrCodeUsageDTO, QrCodeUsage> {
    @Mapping(target = "attendee", source = "attendee", qualifiedByName = "eventAttendeeId")
    QrCodeUsageDTO toDto(QrCodeUsage s);

    @Named("eventAttendeeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventAttendeeDTO toDtoEventAttendeeId(EventAttendee eventAttendee);
}
