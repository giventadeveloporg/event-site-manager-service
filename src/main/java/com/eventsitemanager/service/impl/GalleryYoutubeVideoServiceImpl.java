package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.GalleryYoutubeVideo;
import com.eventsitemanager.repository.GalleryYoutubeVideoRepository;
import com.eventsitemanager.service.GalleryYoutubeVideoService;
import com.eventsitemanager.service.dto.GalleryYoutubeVideoDTO;
import com.eventsitemanager.service.mapper.GalleryYoutubeVideoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.eventsitemanager.domain.GalleryYoutubeVideo}.
 */
@Service
@Transactional
public class GalleryYoutubeVideoServiceImpl implements GalleryYoutubeVideoService {

    private static final Logger LOG = LoggerFactory.getLogger(GalleryYoutubeVideoServiceImpl.class);

    private final GalleryYoutubeVideoRepository galleryYoutubeVideoRepository;

    private final GalleryYoutubeVideoMapper galleryYoutubeVideoMapper;

    public GalleryYoutubeVideoServiceImpl(
        GalleryYoutubeVideoRepository galleryYoutubeVideoRepository,
        GalleryYoutubeVideoMapper galleryYoutubeVideoMapper
    ) {
        this.galleryYoutubeVideoRepository = galleryYoutubeVideoRepository;
        this.galleryYoutubeVideoMapper = galleryYoutubeVideoMapper;
    }

    @Override
    public GalleryYoutubeVideoDTO save(GalleryYoutubeVideoDTO galleryYoutubeVideoDTO) {
        LOG.debug("Request to save GalleryYoutubeVideo : {}", galleryYoutubeVideoDTO);
        GalleryYoutubeVideo galleryYoutubeVideo = galleryYoutubeVideoMapper.toEntity(galleryYoutubeVideoDTO);
        if (galleryYoutubeVideo.getId() != null) {
            galleryYoutubeVideo.setId(null);
        }
        if (galleryYoutubeVideo.getDisplayOrder() == null) {
            galleryYoutubeVideo.setDisplayOrder(0);
        }
        if (galleryYoutubeVideo.getIsActive() == null) {
            galleryYoutubeVideo.setIsActive(true);
        }
        galleryYoutubeVideo = galleryYoutubeVideoRepository.save(galleryYoutubeVideo);
        return galleryYoutubeVideoMapper.toDto(galleryYoutubeVideo);
    }

    @Override
    public GalleryYoutubeVideoDTO update(GalleryYoutubeVideoDTO galleryYoutubeVideoDTO) {
        LOG.debug("Request to update GalleryYoutubeVideo : {}", galleryYoutubeVideoDTO);
        GalleryYoutubeVideo galleryYoutubeVideo = galleryYoutubeVideoMapper.toEntity(galleryYoutubeVideoDTO);
        galleryYoutubeVideo = galleryYoutubeVideoRepository.save(galleryYoutubeVideo);
        return galleryYoutubeVideoMapper.toDto(galleryYoutubeVideo);
    }

    @Override
    public Optional<GalleryYoutubeVideoDTO> partialUpdate(GalleryYoutubeVideoDTO galleryYoutubeVideoDTO) {
        LOG.debug("Request to partially update GalleryYoutubeVideo : {}", galleryYoutubeVideoDTO);
        return galleryYoutubeVideoRepository
            .findById(galleryYoutubeVideoDTO.getId())
            .map(existing -> {
                galleryYoutubeVideoMapper.partialUpdate(existing, galleryYoutubeVideoDTO);
                existing.setYoutubeUrl(galleryYoutubeVideoDTO.getYoutubeUrl());
                existing.setTitle(galleryYoutubeVideoDTO.getTitle());
                existing.setDescription(galleryYoutubeVideoDTO.getDescription());
                if (galleryYoutubeVideoDTO.getDisplayOrder() != null) {
                    existing.setDisplayOrder(galleryYoutubeVideoDTO.getDisplayOrder());
                }
                if (galleryYoutubeVideoDTO.getIsActive() != null) {
                    existing.setIsActive(galleryYoutubeVideoDTO.getIsActive());
                }
                return existing;
            })
            .map(galleryYoutubeVideoRepository::save)
            .map(galleryYoutubeVideoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GalleryYoutubeVideoDTO> findOne(Long id) {
        LOG.debug("Request to get GalleryYoutubeVideo : {}", id);
        return galleryYoutubeVideoRepository.findById(id).map(galleryYoutubeVideoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete GalleryYoutubeVideo : {}", id);
        galleryYoutubeVideoRepository.deleteById(id);
    }
}
