package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.PublicProfileDTO;
import java.util.Optional;

public interface PublicProfileService {
    PublicProfileDTO save(PublicProfileDTO publicProfileDTO);
    PublicProfileDTO update(PublicProfileDTO publicProfileDTO);
    Optional<PublicProfileDTO> partialUpdate(PublicProfileDTO publicProfileDTO);
    Optional<PublicProfileDTO> findOne(Long id);
    void delete(Long id);
}
