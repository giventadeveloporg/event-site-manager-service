package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.ProfileMediaAsset;
import com.eventsitemanager.domain.enumeration.ProfileMediaKind;
import com.eventsitemanager.repository.ProfileMediaAssetRepository;
import com.eventsitemanager.service.ProfileMediaAssetService;
import com.eventsitemanager.service.dto.ProfileMediaAssetDTO;
import com.eventsitemanager.service.mapper.ProfileMediaAssetMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProfileMediaAssetServiceImpl implements ProfileMediaAssetService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileMediaAssetServiceImpl.class);

    private final ProfileMediaAssetRepository profileMediaAssetRepository;
    private final ProfileMediaAssetMapper profileMediaAssetMapper;

    public ProfileMediaAssetServiceImpl(
        ProfileMediaAssetRepository profileMediaAssetRepository,
        ProfileMediaAssetMapper profileMediaAssetMapper
    ) {
        this.profileMediaAssetRepository = profileMediaAssetRepository;
        this.profileMediaAssetMapper = profileMediaAssetMapper;
    }

    @Override
    public ProfileMediaAssetDTO save(ProfileMediaAssetDTO profileMediaAssetDTO) {
        LOG.debug("Request to save ProfileMediaAsset : {}", profileMediaAssetDTO);
        ProfileMediaAsset profileMediaAsset = profileMediaAssetMapper.toEntity(profileMediaAssetDTO);
        if (profileMediaAsset.getId() != null) {
            LOG.warn(
                "ProfileMediaAsset has ID {} set during create operation. Clearing ID to force sequence generation.",
                profileMediaAsset.getId()
            );
            profileMediaAsset.setId(null);
        }
        if (profileMediaAsset.getMediaKind() == null) {
            profileMediaAsset.setMediaKind(ProfileMediaKind.DOCUMENT);
        }

        profileMediaAsset = profileMediaAssetRepository.save(profileMediaAsset);
        return profileMediaAssetMapper.toDto(profileMediaAsset);
    }

    @Override
    public ProfileMediaAssetDTO update(ProfileMediaAssetDTO profileMediaAssetDTO) {
        LOG.debug("Request to update ProfileMediaAsset : {}", profileMediaAssetDTO);
        ProfileMediaAsset profileMediaAsset = profileMediaAssetMapper.toEntity(profileMediaAssetDTO);

        profileMediaAsset = profileMediaAssetRepository.save(profileMediaAsset);
        return profileMediaAssetMapper.toDto(profileMediaAsset);
    }

    @Override
    public Optional<ProfileMediaAssetDTO> partialUpdate(ProfileMediaAssetDTO profileMediaAssetDTO) {
        LOG.debug("Request to partially update ProfileMediaAsset : {}", profileMediaAssetDTO);

        return profileMediaAssetRepository
            .findById(profileMediaAssetDTO.getId())
            .map(existing -> {
                profileMediaAssetMapper.partialUpdate(existing, profileMediaAssetDTO);

                return existing;
            })
            .map(profileMediaAssetRepository::save)
            .map(profileMediaAssetMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProfileMediaAssetDTO> findOne(Long id) {
        LOG.debug("Request to get ProfileMediaAsset : {}", id);
        return profileMediaAssetRepository.findById(id).map(profileMediaAssetMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ProfileMediaAsset : {}", id);
        profileMediaAssetRepository.deleteById(id);
    }
}
