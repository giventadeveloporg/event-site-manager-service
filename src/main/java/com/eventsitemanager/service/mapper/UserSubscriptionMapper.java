package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.UserProfile;
import com.eventsitemanager.domain.UserSubscription;
import com.eventsitemanager.service.dto.UserProfileDTO;
import com.eventsitemanager.service.dto.UserSubscriptionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserSubscription} and its DTO {@link UserSubscriptionDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserSubscriptionMapper extends EntityMapper<UserSubscriptionDTO, UserSubscription> {
    @Mapping(target = "userProfile", source = "userProfile", qualifiedByName = "userProfileId")
    UserSubscriptionDTO toDto(UserSubscription s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
