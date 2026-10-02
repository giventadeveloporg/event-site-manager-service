package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.HomepageYoutubeOverrideDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.eventsitemanager.domain.HomepageYoutubeOverride}.
 */
public interface HomepageYoutubeOverrideService {
    HomepageYoutubeOverrideDTO save(HomepageYoutubeOverrideDTO homepageYoutubeOverrideDTO);

    HomepageYoutubeOverrideDTO update(HomepageYoutubeOverrideDTO homepageYoutubeOverrideDTO);

    Optional<HomepageYoutubeOverrideDTO> partialUpdate(HomepageYoutubeOverrideDTO homepageYoutubeOverrideDTO);

    Optional<HomepageYoutubeOverrideDTO> findOne(Long id);

    void delete(Long id);
}
