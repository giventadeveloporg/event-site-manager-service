package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.PublicProfile;
import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.PublicProfileRepository;
import com.eventsitemanager.service.PublicProfileService;
import com.eventsitemanager.service.dto.PublicProfileDTO;
import com.eventsitemanager.service.mapper.PublicProfileMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class PublicProfileServiceImpl implements PublicProfileService {

    private static final Logger LOG = LoggerFactory.getLogger(PublicProfileServiceImpl.class);
    private static final String ENTITY_NAME = "publicProfile";

    private final PublicProfileRepository publicProfileRepository;
    private final PublicProfileMapper publicProfileMapper;

    public PublicProfileServiceImpl(PublicProfileRepository publicProfileRepository, PublicProfileMapper publicProfileMapper) {
        this.publicProfileRepository = publicProfileRepository;
        this.publicProfileMapper = publicProfileMapper;
    }

    @Override
    public PublicProfileDTO save(PublicProfileDTO publicProfileDTO) {
        LOG.debug("Request to save PublicProfile : {}", publicProfileDTO);
        if (publicProfileDTO.getTenantId() != null && publicProfileRepository.existsByTenantId(publicProfileDTO.getTenantId())) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "A public profile already exists for tenantId " + publicProfileDTO.getTenantId()
            );
        }

        PublicProfile publicProfile = publicProfileMapper.toEntity(publicProfileDTO);
        if (publicProfile.getId() != null) {
            LOG.warn(
                "PublicProfile has ID {} set during create operation. Clearing ID to force sequence generation.",
                publicProfile.getId()
            );
            publicProfile.setId(null);
        }

        try {
            publicProfile = publicProfileRepository.save(publicProfile);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A public profile already exists for this tenant", ex);
        }

        return publicProfileMapper.toDto(publicProfile);
    }

    @Override
    public PublicProfileDTO update(PublicProfileDTO publicProfileDTO) {
        LOG.debug("Request to update PublicProfile : {}", publicProfileDTO);
        PublicProfile publicProfile = publicProfileMapper.toEntity(publicProfileDTO);
        publicProfile = publicProfileRepository.save(publicProfile);
        return publicProfileMapper.toDto(publicProfile);
    }

    @Override
    public Optional<PublicProfileDTO> partialUpdate(PublicProfileDTO publicProfileDTO) {
        LOG.debug("Request to partially update PublicProfile : {}", publicProfileDTO);

        return publicProfileRepository
            .findById(publicProfileDTO.getId())
            .map(existing -> {
                publicProfileMapper.partialUpdate(existing, publicProfileDTO);
                return existing;
            })
            .map(publicProfileRepository::save)
            .map(publicProfileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PublicProfileDTO> findOne(Long id) {
        LOG.debug("Request to get PublicProfile : {}", id);
        return publicProfileRepository.findById(id).map(publicProfileMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete PublicProfile : {}", id);
        publicProfileRepository.deleteById(id);
    }
}
