package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.EventAdmin;
import com.eventsitemanager.domain.UserProfile;
import com.eventsitemanager.service.dto.EventAdminDTO;
import com.eventsitemanager.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventAdmin} and its DTO {@link EventAdminDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventAdminMapper extends EntityMapper<EventAdminDTO, EventAdmin> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userProfileId")
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "userProfileId")
    EventAdminDTO toDto(EventAdmin s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
