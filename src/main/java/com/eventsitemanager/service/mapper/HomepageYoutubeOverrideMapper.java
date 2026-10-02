package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.HomepageYoutubeOverride;
import com.eventsitemanager.service.dto.HomepageYoutubeOverrideDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link HomepageYoutubeOverride} and its DTO {@link HomepageYoutubeOverrideDTO}.
 */
@Mapper(componentModel = "spring")
public interface HomepageYoutubeOverrideMapper extends EntityMapper<HomepageYoutubeOverrideDTO, HomepageYoutubeOverride> {}
