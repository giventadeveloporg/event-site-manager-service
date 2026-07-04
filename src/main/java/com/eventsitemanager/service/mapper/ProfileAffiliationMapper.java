package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.ProfileAffiliation;
import com.eventsitemanager.service.dto.ProfileAffiliationDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileAffiliationMapper extends EntityMapper<ProfileAffiliationDTO, ProfileAffiliation> {}
