package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.ProfileFamilyMemberDTO;
import java.util.Optional;

public interface ProfileFamilyMemberService {
    ProfileFamilyMemberDTO save(ProfileFamilyMemberDTO profileFamilyMemberDTO);
    ProfileFamilyMemberDTO update(ProfileFamilyMemberDTO profileFamilyMemberDTO);
    Optional<ProfileFamilyMemberDTO> partialUpdate(ProfileFamilyMemberDTO profileFamilyMemberDTO);
    Optional<ProfileFamilyMemberDTO> findOne(Long id);
    void delete(Long id);
}
