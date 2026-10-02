package com.eventsitemanager.repository;

import com.eventsitemanager.domain.GalleryYoutubeVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the GalleryYoutubeVideo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GalleryYoutubeVideoRepository
    extends JpaRepository<GalleryYoutubeVideo, Long>, JpaSpecificationExecutor<GalleryYoutubeVideo> {}
