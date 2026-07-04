package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.ProfileAffiliation;
import com.eventsitemanager.repository.ProfileAffiliationRepository;
import com.eventsitemanager.service.ProfileAffiliationService;
import com.eventsitemanager.service.dto.ProfileAffiliationDTO;
import com.eventsitemanager.service.mapper.ProfileAffiliationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProfileAffiliationServiceImpl implements ProfileAffiliationService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileAffiliationServiceImpl.class);

    private final ProfileAffiliationRepository profileAffiliationRepository;
    private final ProfileAffiliationMapper profileAffiliationMapper;

    public ProfileAffiliationServiceImpl(
        ProfileAffiliationRepository profileAffiliationRepository,
        ProfileAffiliationMapper profileAffiliationMapper
    ) {
        this.profileAffiliationRepository = profileAffiliationRepository;
        this.profileAffiliationMapper = profileAffiliationMapper;
    }

    @Override
    public ProfileAffiliationDTO save(ProfileAffiliationDTO profileAffiliationDTO) {
        LOG.debug("Request to save ProfileAffiliation : {}", profileAffiliationDTO);
        ProfileAffiliation profileAffiliation = profileAffiliationMapper.toEntity(profileAffiliationDTO);
        if (profileAffiliation.getId() != null) {
            LOG.warn(
                "ProfileAffiliation has ID {} set during create operation. Clearing ID to force sequence generation.",
                profileAffiliation.getId()
            );
            profileAffiliation.setId(null);
        }

        profileAffiliation = profileAffiliationRepository.save(profileAffiliation);
        return profileAffiliationMapper.toDto(profileAffiliation);
    }

    @Override
    public ProfileAffiliationDTO update(ProfileAffiliationDTO profileAffiliationDTO) {
        LOG.debug("Request to update ProfileAffiliation : {}", profileAffiliationDTO);
        ProfileAffiliation profileAffiliation = profileAffiliationMapper.toEntity(profileAffiliationDTO);

        profileAffiliation = profileAffiliationRepository.save(profileAffiliation);
        return profileAffiliationMapper.toDto(profileAffiliation);
    }

    @Override
    public Optional<ProfileAffiliationDTO> partialUpdate(ProfileAffiliationDTO profileAffiliationDTO) {
        LOG.debug("Request to partially update ProfileAffiliation : {}", profileAffiliationDTO);

        return profileAffiliationRepository
            .findById(profileAffiliationDTO.getId())
            .map(existing -> {
                profileAffiliationMapper.partialUpdate(existing, profileAffiliationDTO);

                return existing;
            })
            .map(profileAffiliationRepository::save)
            .map(profileAffiliationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProfileAffiliationDTO> findOne(Long id) {
        LOG.debug("Request to get ProfileAffiliation : {}", id);
        return profileAffiliationRepository.findById(id).map(profileAffiliationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ProfileAffiliation : {}", id);
        profileAffiliationRepository.deleteById(id);
    }
}
