package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.ProfileWriting;
import com.eventsitemanager.service.dto.ProfileWritingDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileWritingMapper extends EntityMapper<ProfileWritingDTO, ProfileWriting> {}
