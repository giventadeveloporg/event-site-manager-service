package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.GalleryYoutubeVideo;
import com.eventsitemanager.service.dto.GalleryYoutubeVideoDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link GalleryYoutubeVideo} and its DTO {@link GalleryYoutubeVideoDTO}.
 */
@Mapper(componentModel = "spring")
public interface GalleryYoutubeVideoMapper extends EntityMapper<GalleryYoutubeVideoDTO, GalleryYoutubeVideo> {}
