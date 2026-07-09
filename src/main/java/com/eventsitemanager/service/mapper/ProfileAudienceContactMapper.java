package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.ProfileAudienceContact;
import com.eventsitemanager.service.dto.ProfileAudienceContactDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileAudienceContactMapper extends EntityMapper<ProfileAudienceContactDTO, ProfileAudienceContact> {}
