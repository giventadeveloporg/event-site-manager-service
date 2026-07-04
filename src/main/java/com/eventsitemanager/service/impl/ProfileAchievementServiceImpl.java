package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.ProfileAchievement;
import com.eventsitemanager.repository.ProfileAchievementRepository;
import com.eventsitemanager.service.ProfileAchievementService;
import com.eventsitemanager.service.dto.ProfileAchievementDTO;
import com.eventsitemanager.service.mapper.ProfileAchievementMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProfileAchievementServiceImpl implements ProfileAchievementService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileAchievementServiceImpl.class);

    private final ProfileAchievementRepository profileAchievementRepository;
    private final ProfileAchievementMapper profileAchievementMapper;

    public ProfileAchievementServiceImpl(
        ProfileAchievementRepository profileAchievementRepository,
        ProfileAchievementMapper profileAchievementMapper
    ) {
        this.profileAchievementRepository = profileAchievementRepository;
        this.profileAchievementMapper = profileAchievementMapper;
    }

    @Override
    public ProfileAchievementDTO save(ProfileAchievementDTO profileAchievementDTO) {
        LOG.debug("Request to save ProfileAchievement : {}", profileAchievementDTO);
        ProfileAchievement profileAchievement = profileAchievementMapper.toEntity(profileAchievementDTO);
        if (profileAchievement.getId() != null) {
            LOG.warn(
                "ProfileAchievement has ID {} set during create operation. Clearing ID to force sequence generation.",
                profileAchievement.getId()
            );
            profileAchievement.setId(null);
        }

        profileAchievement = profileAchievementRepository.save(profileAchievement);
        return profileAchievementMapper.toDto(profileAchievement);
    }

    @Override
    public ProfileAchievementDTO update(ProfileAchievementDTO profileAchievementDTO) {
        LOG.debug("Request to update ProfileAchievement : {}", profileAchievementDTO);
        ProfileAchievement profileAchievement = profileAchievementMapper.toEntity(profileAchievementDTO);

        profileAchievement = profileAchievementRepository.save(profileAchievement);
        return profileAchievementMapper.toDto(profileAchievement);
    }

    @Override
    public Optional<ProfileAchievementDTO> partialUpdate(ProfileAchievementDTO profileAchievementDTO) {
        LOG.debug("Request to partially update ProfileAchievement : {}", profileAchievementDTO);

        return profileAchievementRepository
            .findById(profileAchievementDTO.getId())
            .map(existing -> {
                profileAchievementMapper.partialUpdate(existing, profileAchievementDTO);

                return existing;
            })
            .map(profileAchievementRepository::save)
            .map(profileAchievementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProfileAchievementDTO> findOne(Long id) {
        LOG.debug("Request to get ProfileAchievement : {}", id);
        return profileAchievementRepository.findById(id).map(profileAchievementMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ProfileAchievement : {}", id);
        profileAchievementRepository.deleteById(id);
    }
}
