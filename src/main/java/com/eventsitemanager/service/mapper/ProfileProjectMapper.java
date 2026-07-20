package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.ProfileProject;
import com.eventsitemanager.service.dto.ProfileProjectDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileProjectMapper extends EntityMapper<ProfileProjectDTO, ProfileProject> {}
