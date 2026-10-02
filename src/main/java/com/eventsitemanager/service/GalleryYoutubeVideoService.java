package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.GalleryYoutubeVideoDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.eventsitemanager.domain.GalleryYoutubeVideo}.
 */
public interface GalleryYoutubeVideoService {
    GalleryYoutubeVideoDTO save(GalleryYoutubeVideoDTO galleryYoutubeVideoDTO);

    GalleryYoutubeVideoDTO update(GalleryYoutubeVideoDTO galleryYoutubeVideoDTO);

    Optional<GalleryYoutubeVideoDTO> partialUpdate(GalleryYoutubeVideoDTO galleryYoutubeVideoDTO);

    Optional<GalleryYoutubeVideoDTO> findOne(Long id);

    void delete(Long id);
}
