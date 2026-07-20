package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.ProfileProject;
import com.eventsitemanager.repository.ProfileProjectRepository;
import com.eventsitemanager.service.ProfileProjectService;
import com.eventsitemanager.service.dto.ProfileProjectDTO;
import com.eventsitemanager.service.mapper.ProfileProjectMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProfileProjectServiceImpl implements ProfileProjectService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileProjectServiceImpl.class);

    private final ProfileProjectRepository profileProjectRepository;
    private final ProfileProjectMapper profileProjectMapper;

    public ProfileProjectServiceImpl(ProfileProjectRepository profileProjectRepository, ProfileProjectMapper profileProjectMapper) {
        this.profileProjectRepository = profileProjectRepository;
        this.profileProjectMapper = profileProjectMapper;
    }

    @Override
    public ProfileProjectDTO save(ProfileProjectDTO profileProjectDTO) {
        LOG.debug("Request to save ProfileProject : {}", profileProjectDTO);
        ProfileProject profileProject = profileProjectMapper.toEntity(profileProjectDTO);
        if (profileProject.getId() != null) {
            LOG.warn(
                "ProfileProject has ID {} set during create operation. Clearing ID to force sequence generation.",
                profileProject.getId()
            );
            profileProject.setId(null);
        }

        profileProject = profileProjectRepository.save(profileProject);
        return profileProjectMapper.toDto(profileProject);
    }

    @Override
    public ProfileProjectDTO update(ProfileProjectDTO profileProjectDTO) {
        LOG.debug("Request to update ProfileProject : {}", profileProjectDTO);
        ProfileProject profileProject = profileProjectMapper.toEntity(profileProjectDTO);

        profileProject = profileProjectRepository.save(profileProject);
        return profileProjectMapper.toDto(profileProject);
    }

    @Override
    public Optional<ProfileProjectDTO> partialUpdate(ProfileProjectDTO profileProjectDTO) {
        LOG.debug("Request to partially update ProfileProject : {}", profileProjectDTO);

        return profileProjectRepository
            .findById(profileProjectDTO.getId())
            .map(existing -> {
                profileProjectMapper.partialUpdate(existing, profileProjectDTO);
                return existing;
            })
            .map(profileProjectRepository::save)
            .map(profileProjectMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProfileProjectDTO> findOne(Long id) {
        LOG.debug("Request to get ProfileProject : {}", id);
        return profileProjectRepository.findById(id).map(profileProjectMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ProfileProject : {}", id);
        profileProjectRepository.deleteById(id);
    }
}
