package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.CommunicationCampaign;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.service.dto.CommunicationCampaignDTO;
import com.nextjstemplate.service.dto.UserProfileDTO;
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
