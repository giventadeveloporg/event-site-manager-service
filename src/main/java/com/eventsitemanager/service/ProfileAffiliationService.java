package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.ProfileAffiliationDTO;
import java.util.Optional;

public interface ProfileAffiliationService {
    ProfileAffiliationDTO save(ProfileAffiliationDTO profileAffiliationDTO);
    ProfileAffiliationDTO update(ProfileAffiliationDTO profileAffiliationDTO);
    Optional<ProfileAffiliationDTO> partialUpdate(ProfileAffiliationDTO profileAffiliationDTO);
    Optional<ProfileAffiliationDTO> findOne(Long id);
    void delete(Long id);
}
