package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.UserProfile;
import com.eventsitemanager.domain.UserRegistrationRequest;
import com.eventsitemanager.service.dto.UserProfileDTO;
import com.eventsitemanager.service.dto.UserRegistrationRequestDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserRegistrationRequest} and its DTO {@link UserRegistrationRequestDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserRegistrationRequestMapper extends EntityMapper<UserRegistrationRequestDTO, UserRegistrationRequest> {
    @Mapping(target = "reviewedBy", source = "reviewedBy", qualifiedByName = "userProfileId")
    UserRegistrationRequestDTO toDto(UserRegistrationRequest s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
