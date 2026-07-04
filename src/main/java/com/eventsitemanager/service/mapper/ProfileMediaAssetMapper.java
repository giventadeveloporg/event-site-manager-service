package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.ProfileMediaAsset;
import com.eventsitemanager.service.dto.ProfileMediaAssetDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMediaAssetMapper extends EntityMapper<ProfileMediaAssetDTO, ProfileMediaAsset> {}
