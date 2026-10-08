package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.ProfileService;
import com.eventsitemanager.repository.ProfileServiceRepository;
import com.eventsitemanager.service.ProfileServiceService;
import com.eventsitemanager.service.dto.ProfileServiceDTO;
import com.eventsitemanager.service.mapper.ProfileServiceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProfileServiceServiceImpl implements ProfileServiceService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileServiceServiceImpl.class);

    private final ProfileServiceRepository profileServiceRepository;
    private final ProfileServiceMapper profileServiceMapper;

    public ProfileServiceServiceImpl(ProfileServiceRepository profileServiceRepository, ProfileServiceMapper profileServiceMapper) {
        this.profileServiceRepository = profileServiceRepository;
        this.profileServiceMapper = profileServiceMapper;
    }

    @Override
    public ProfileServiceDTO save(ProfileServiceDTO profileServiceDTO) {
        LOG.debug("Request to save ProfileService : {}", profileServiceDTO);
        ProfileService profileService = profileServiceMapper.toEntity(profileServiceDTO);
        if (profileService.getId() != null) {
            LOG.warn(
                "ProfileService has ID {} set during create operation. Clearing ID to force sequence generation.",
                profileService.getId()
            );
            profileService.setId(null);
        }

        profileService = profileServiceRepository.save(profileService);
        return profileServiceMapper.toDto(profileService);
    }

    @Override
    public ProfileServiceDTO update(ProfileServiceDTO profileServiceDTO) {
        LOG.debug("Request to update ProfileService : {}", profileServiceDTO);
        ProfileService profileService = profileServiceMapper.toEntity(profileServiceDTO);

        profileService = profileServiceRepository.save(profileService);
        return profileServiceMapper.toDto(profileService);
    }

    @Override
    public Optional<ProfileServiceDTO> partialUpdate(ProfileServiceDTO profileServiceDTO) {
        LOG.debug("Request to partially update ProfileService : {}", profileServiceDTO);

        return profileServiceRepository
            .findById(profileServiceDTO.getId())
            .map(existing -> {
                profileServiceMapper.partialUpdate(existing, profileServiceDTO);
                return existing;
            })
            .map(profileServiceRepository::save)
            .map(profileServiceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProfileServiceDTO> findOne(Long id) {
        LOG.debug("Request to get ProfileService : {}", id);
        return profileServiceRepository.findById(id).map(profileServiceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ProfileService : {}", id);
        profileServiceRepository.deleteById(id);
    }
}
