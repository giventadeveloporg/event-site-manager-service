package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.ProfileProjectDTO;
import java.util.Optional;

public interface ProfileProjectService {
    ProfileProjectDTO save(ProfileProjectDTO profileProjectDTO);
    ProfileProjectDTO update(ProfileProjectDTO profileProjectDTO);
    Optional<ProfileProjectDTO> partialUpdate(ProfileProjectDTO profileProjectDTO);
    Optional<ProfileProjectDTO> findOne(Long id);
    void delete(Long id);
}
