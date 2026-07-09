package com.eventsitemanager.service;

import com.eventsitemanager.domain.enumeration.ProfileAudienceContactSource;
import com.eventsitemanager.service.dto.ProfileAudienceBulkImportResultDTO;
import com.eventsitemanager.service.dto.ProfileAudienceContactDTO;
import com.eventsitemanager.service.dto.ProfileAudienceSubscribeRequestDTO;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProfileAudienceContactService {
    ProfileAudienceContactDTO save(ProfileAudienceContactDTO dto);

    ProfileAudienceContactDTO update(ProfileAudienceContactDTO dto);

    Optional<ProfileAudienceContactDTO> partialUpdate(ProfileAudienceContactDTO dto);

    Optional<ProfileAudienceContactDTO> findOne(Long id);

    void delete(Long id);

    ProfileAudienceBulkImportResultDTO bulkImport(String tenantId, List<ProfileAudienceContactDTO> contacts);

    ProfileAudienceContactDTO publicSubscribe(
        String tenantId,
        ProfileAudienceSubscribeRequestDTO request,
        ProfileAudienceContactSource source
    );

    Map<String, Object> unsubscribe(String tenantId, String email, String token);
}
