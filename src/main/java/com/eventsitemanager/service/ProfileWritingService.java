package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.ProfileWritingDTO;
import java.util.Optional;

public interface ProfileWritingService {
    ProfileWritingDTO save(ProfileWritingDTO profileWritingDTO);
    ProfileWritingDTO update(ProfileWritingDTO profileWritingDTO);
    Optional<ProfileWritingDTO> partialUpdate(ProfileWritingDTO profileWritingDTO);
    Optional<ProfileWritingDTO> findOne(Long id);
    void delete(Long id);
}
