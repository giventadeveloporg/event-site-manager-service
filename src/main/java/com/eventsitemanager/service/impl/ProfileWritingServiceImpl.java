package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.ProfileWriting;
import com.eventsitemanager.domain.enumeration.ProfileWritingType;
import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.ProfileWritingRepository;
import com.eventsitemanager.service.ProfileWritingService;
import com.eventsitemanager.service.dto.ProfileWritingDTO;
import com.eventsitemanager.service.mapper.ProfileWritingMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProfileWritingServiceImpl implements ProfileWritingService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileWritingServiceImpl.class);
    private static final String ENTITY_NAME = "profileWriting";

    private final ProfileWritingRepository profileWritingRepository;
    private final ProfileWritingMapper profileWritingMapper;

    public ProfileWritingServiceImpl(ProfileWritingRepository profileWritingRepository, ProfileWritingMapper profileWritingMapper) {
        this.profileWritingRepository = profileWritingRepository;
        this.profileWritingMapper = profileWritingMapper;
    }

    @Override
    public ProfileWritingDTO save(ProfileWritingDTO profileWritingDTO) {
        LOG.debug("Request to save ProfileWriting : {}", profileWritingDTO);
        ProfileWriting profileWriting = profileWritingMapper.toEntity(profileWritingDTO);
        if (profileWriting.getId() != null) {
            LOG.warn(
                "ProfileWriting has ID {} set during create operation. Clearing ID to force sequence generation.",
                profileWriting.getId()
            );
            profileWriting.setId(null);
        }
        validateExternalUrlForWritingType(profileWriting);
        profileWriting = profileWritingRepository.save(profileWriting);
        return profileWritingMapper.toDto(profileWriting);
    }

    @Override
    public ProfileWritingDTO update(ProfileWritingDTO profileWritingDTO) {
        LOG.debug("Request to update ProfileWriting : {}", profileWritingDTO);
        ProfileWriting profileWriting = profileWritingMapper.toEntity(profileWritingDTO);
        validateExternalUrlForWritingType(profileWriting);
        profileWriting = profileWritingRepository.save(profileWriting);
        return profileWritingMapper.toDto(profileWriting);
    }

    @Override
    public Optional<ProfileWritingDTO> partialUpdate(ProfileWritingDTO profileWritingDTO) {
        LOG.debug("Request to partially update ProfileWriting : {}", profileWritingDTO);

        return profileWritingRepository
            .findById(profileWritingDTO.getId())
            .map(existing -> {
                profileWritingMapper.partialUpdate(existing, profileWritingDTO);
                validateExternalUrlForWritingType(existing);
                return existing;
            })
            .map(profileWritingRepository::save)
            .map(profileWritingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProfileWritingDTO> findOne(Long id) {
        LOG.debug("Request to get ProfileWriting : {}", id);
        return profileWritingRepository.findById(id).map(profileWritingMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ProfileWriting : {}", id);
        profileWritingRepository.deleteById(id);
    }

    private void validateExternalUrlForWritingType(ProfileWriting profileWriting) {
        if (profileWriting.getWritingType() == ProfileWritingType.EXTERNAL_LINK) {
            String externalUrl = profileWriting.getExternalUrl();
            if (externalUrl == null || externalUrl.isBlank()) {
                throw new BadRequestAlertException(
                    "externalUrl is required when writingType is EXTERNAL_LINK",
                    ENTITY_NAME,
                    "externalurlrequired"
                );
            }
        }
    }
}
