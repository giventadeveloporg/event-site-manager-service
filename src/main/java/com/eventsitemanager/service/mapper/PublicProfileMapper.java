package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.PublicProfile;
import com.eventsitemanager.service.dto.PublicProfileDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PublicProfileMapper extends EntityMapper<PublicProfileDTO, PublicProfile> {}
