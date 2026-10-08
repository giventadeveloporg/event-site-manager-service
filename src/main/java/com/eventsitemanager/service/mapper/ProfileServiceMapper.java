package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.ProfileService;
import com.eventsitemanager.service.dto.ProfileServiceDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileServiceMapper extends EntityMapper<ProfileServiceDTO, ProfileService> {}
