package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.CommunicationCampaign;
import com.eventsitemanager.domain.UserProfile;
import com.eventsitemanager.service.dto.CommunicationCampaignDTO;
import com.eventsitemanager.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CommunicationCampaign} and its DTO {@link CommunicationCampaignDTO}.
 */
@Mapper(componentModel = "spring")
public interface CommunicationCampaignMapper extends EntityMapper<CommunicationCampaignDTO, CommunicationCampaign> {
    //    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "userProfileId")
    CommunicationCampaignDTO toDto(CommunicationCampaign s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
