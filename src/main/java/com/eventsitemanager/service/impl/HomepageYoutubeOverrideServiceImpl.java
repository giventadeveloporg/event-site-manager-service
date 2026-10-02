package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.HomepageYoutubeOverride;
import com.eventsitemanager.repository.HomepageYoutubeOverrideRepository;
import com.eventsitemanager.service.HomepageYoutubeOverrideService;
import com.eventsitemanager.service.dto.HomepageYoutubeOverrideDTO;
import com.eventsitemanager.service.mapper.HomepageYoutubeOverrideMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.eventsitemanager.domain.HomepageYoutubeOverride}.
 */
@Service
@Transactional
public class HomepageYoutubeOverrideServiceImpl implements HomepageYoutubeOverrideService {

    private static final Logger LOG = LoggerFactory.getLogger(HomepageYoutubeOverrideServiceImpl.class);

    private final HomepageYoutubeOverrideRepository homepageYoutubeOverrideRepository;

    private final HomepageYoutubeOverrideMapper homepageYoutubeOverrideMapper;

    public HomepageYoutubeOverrideServiceImpl(
        HomepageYoutubeOverrideRepository homepageYoutubeOverrideRepository,
        HomepageYoutubeOverrideMapper homepageYoutubeOverrideMapper
    ) {
        this.homepageYoutubeOverrideRepository = homepageYoutubeOverrideRepository;
        this.homepageYoutubeOverrideMapper = homepageYoutubeOverrideMapper;
    }

    @Override
    public HomepageYoutubeOverrideDTO save(HomepageYoutubeOverrideDTO homepageYoutubeOverrideDTO) {
        LOG.debug("Request to save HomepageYoutubeOverride : {}", homepageYoutubeOverrideDTO);
        HomepageYoutubeOverride homepageYoutubeOverride = homepageYoutubeOverrideMapper.toEntity(homepageYoutubeOverrideDTO);
        if (homepageYoutubeOverride.getId() != null) {
            homepageYoutubeOverride.setId(null);
        }
        homepageYoutubeOverride = homepageYoutubeOverrideRepository.save(homepageYoutubeOverride);
        return homepageYoutubeOverrideMapper.toDto(homepageYoutubeOverride);
    }

    @Override
    public HomepageYoutubeOverrideDTO update(HomepageYoutubeOverrideDTO homepageYoutubeOverrideDTO) {
        LOG.debug("Request to update HomepageYoutubeOverride : {}", homepageYoutubeOverrideDTO);
        HomepageYoutubeOverride homepageYoutubeOverride = homepageYoutubeOverrideMapper.toEntity(homepageYoutubeOverrideDTO);
        homepageYoutubeOverride = homepageYoutubeOverrideRepository.save(homepageYoutubeOverride);
        return homepageYoutubeOverrideMapper.toDto(homepageYoutubeOverride);
    }

    @Override
    public Optional<HomepageYoutubeOverrideDTO> partialUpdate(HomepageYoutubeOverrideDTO homepageYoutubeOverrideDTO) {
        LOG.debug("Request to partially update HomepageYoutubeOverride : {}", homepageYoutubeOverrideDTO);
        return homepageYoutubeOverrideRepository
            .findById(homepageYoutubeOverrideDTO.getId())
            .map(existing -> {
                homepageYoutubeOverrideMapper.partialUpdate(existing, homepageYoutubeOverrideDTO);
                // Full-form saves send null to clear optional fields. MapStruct ignores nulls.
                existing.setYoutubeUrl(homepageYoutubeOverrideDTO.getYoutubeUrl());
                existing.setTitle(homepageYoutubeOverrideDTO.getTitle());
                existing.setDescription(homepageYoutubeOverrideDTO.getDescription());
                existing.setStartsAt(homepageYoutubeOverrideDTO.getStartsAt());
                existing.setEndsAt(homepageYoutubeOverrideDTO.getEndsAt());
                if (homepageYoutubeOverrideDTO.getIsActive() != null) {
                    existing.setIsActive(homepageYoutubeOverrideDTO.getIsActive());
                }
                return existing;
            })
            .map(homepageYoutubeOverrideRepository::save)
            .map(homepageYoutubeOverrideMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HomepageYoutubeOverrideDTO> findOne(Long id) {
        LOG.debug("Request to get HomepageYoutubeOverride : {}", id);
        return homepageYoutubeOverrideRepository.findById(id).map(homepageYoutubeOverrideMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete HomepageYoutubeOverride : {}", id);
        homepageYoutubeOverrideRepository.deleteById(id);
    }
}
