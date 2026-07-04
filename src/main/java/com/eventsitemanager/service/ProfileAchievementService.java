package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.ProfileAchievementDTO;
import java.util.Optional;

public interface ProfileAchievementService {
    ProfileAchievementDTO save(ProfileAchievementDTO profileAchievementDTO);
    ProfileAchievementDTO update(ProfileAchievementDTO profileAchievementDTO);
    Optional<ProfileAchievementDTO> partialUpdate(ProfileAchievementDTO profileAchievementDTO);
    Optional<ProfileAchievementDTO> findOne(Long id);
    void delete(Long id);
}
