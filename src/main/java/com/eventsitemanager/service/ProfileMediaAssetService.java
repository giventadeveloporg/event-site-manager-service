package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.ProfileMediaAssetDTO;
import java.util.Optional;

public interface ProfileMediaAssetService {
    ProfileMediaAssetDTO save(ProfileMediaAssetDTO profileMediaAssetDTO);
    ProfileMediaAssetDTO update(ProfileMediaAssetDTO profileMediaAssetDTO);
    Optional<ProfileMediaAssetDTO> partialUpdate(ProfileMediaAssetDTO profileMediaAssetDTO);
    Optional<ProfileMediaAssetDTO> findOne(Long id);
    void delete(Long id);
}
