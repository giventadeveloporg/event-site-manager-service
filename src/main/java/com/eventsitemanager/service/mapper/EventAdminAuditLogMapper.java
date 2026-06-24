package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.EventAdminAuditLog;
import com.eventsitemanager.domain.UserProfile;
import com.eventsitemanager.service.dto.EventAdminAuditLogDTO;
import com.eventsitemanager.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventAdminAuditLog} and its DTO {@link EventAdminAuditLogDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventAdminAuditLogMapper extends EntityMapper<EventAdminAuditLogDTO, EventAdminAuditLog> {
    @Mapping(target = "admin", source = "admin", qualifiedByName = "userProfileId")
    EventAdminAuditLogDTO toDto(EventAdminAuditLog s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
