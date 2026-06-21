package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.GalleryAlbum;
import com.nextjstemplate.domain.GalleryCategory;
import com.nextjstemplate.repository.GalleryAlbumRepository;
import com.nextjstemplate.service.GalleryAlbumService;
import com.nextjstemplate.service.dto.GalleryAlbumDTO;
import com.nextjstemplate.service.mapper.GalleryAlbumMapper;
import com.nextjstemplate.service.validation.GalleryAlbumCategoryValidator;
import java.time.ZonedDateTime;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nextjstemplate.domain.GalleryAlbum}.
 */
@Service
@Transactional
public class GalleryAlbumServiceImpl implements GalleryAlbumService {

    private final Logger log = LoggerFactory.getLogger(GalleryAlbumServiceImpl.class);

    private final GalleryAlbumRepository galleryAlbumRepository;

    private final GalleryAlbumMapper galleryAlbumMapper;

    private final GalleryAlbumCategoryValidator galleryAlbumCategoryValidator;

    public GalleryAlbumServiceImpl(
        GalleryAlbumRepository galleryAlbumRepository,
        GalleryAlbumMapper galleryAlbumMapper,
        GalleryAlbumCategoryValidator galleryAlbumCategoryValidator
    ) {
        this.galleryAlbumRepository = galleryAlbumRepository;
        this.galleryAlbumMapper = galleryAlbumMapper;
        this.galleryAlbumCategoryValidator = galleryAlbumCategoryValidator;
    }

    @Override
    public GalleryAlbumDTO save(GalleryAlbumDTO galleryAlbumDTO) {
        log.debug("Request to save GalleryAlbum : {}", galleryAlbumDTO);
        galleryAlbumCategoryValidator.validateAlbumYear(galleryAlbumDTO.getAlbumYear());
        galleryAlbumCategoryValidator.validateAndNormalizeEventFields(galleryAlbumDTO);

        GalleryAlbum galleryAlbum = galleryAlbumMapper.toEntity(galleryAlbumDTO);
        applyGalleryCategory(galleryAlbum, galleryAlbumDTO, true);
        applyEventFields(galleryAlbum, galleryAlbumDTO);

        // Set timestamps
        ZonedDateTime now = ZonedDateTime.now();
        if (galleryAlbum.getCreatedAt() == null) {
            galleryAlbum.setCreatedAt(now);
        }
        galleryAlbum.setUpdatedAt(now);

        // Set defaults
        if (galleryAlbum.getIsPublic() == null) {
            galleryAlbum.setIsPublic(true);
        }
        if (galleryAlbum.getDisplayOrder() == null) {
            galleryAlbum.setDisplayOrder(0);
        }

        galleryAlbum = galleryAlbumRepository.save(galleryAlbum);
        return galleryAlbumMapper.toDto(galleryAlbum);
    }

    @Override
    public GalleryAlbumDTO update(GalleryAlbumDTO galleryAlbumDTO) {
        log.debug("Request to update GalleryAlbum : {}", galleryAlbumDTO);
        galleryAlbumCategoryValidator.validateAlbumYear(galleryAlbumDTO.getAlbumYear());
        galleryAlbumCategoryValidator.validateAndNormalizeEventFields(galleryAlbumDTO);

        GalleryAlbum galleryAlbum = galleryAlbumMapper.toEntity(galleryAlbumDTO);
        applyGalleryCategory(galleryAlbum, galleryAlbumDTO, true);
        applyEventFields(galleryAlbum, galleryAlbumDTO);

        galleryAlbum.setUpdatedAt(ZonedDateTime.now());

        galleryAlbum = galleryAlbumRepository.save(galleryAlbum);
        return galleryAlbumMapper.toDto(galleryAlbum);
    }

    @Override
    public Optional<GalleryAlbumDTO> partialUpdate(GalleryAlbumDTO galleryAlbumDTO) {
        log.debug("Request to partially update GalleryAlbum : {}", galleryAlbumDTO);

        if (galleryAlbumDTO.getAlbumYear() != null) {
            galleryAlbumCategoryValidator.validateAlbumYear(galleryAlbumDTO.getAlbumYear());
        }

        return galleryAlbumRepository
            .findById(galleryAlbumDTO.getId())
            .map(existingGalleryAlbum -> {
                galleryAlbumMapper.partialUpdate(existingGalleryAlbum, galleryAlbumDTO);

                if (galleryAlbumDTO.isGalleryCategoryIdSet()) {
                    applyGalleryCategory(existingGalleryAlbum, galleryAlbumDTO, true);
                }

                applyPartialEventFields(existingGalleryAlbum, galleryAlbumDTO);
                galleryAlbumCategoryValidator.validateEventDateFields(
                    existingGalleryAlbum.getAlbumYear(),
                    existingGalleryAlbum.getEventDateStart(),
                    existingGalleryAlbum.getEventDateEnd(),
                    existingGalleryAlbum.getEventLocation()
                );

                existingGalleryAlbum.setUpdatedAt(ZonedDateTime.now());

                return existingGalleryAlbum;
            })
            .map(galleryAlbumRepository::save)
            .map(galleryAlbumMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GalleryAlbumDTO> findAll(Pageable pageable) {
        log.debug("Request to get all GalleryAlbums");
        return galleryAlbumRepository.findAll(pageable).map(galleryAlbumMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GalleryAlbumDTO> findOne(Long id) {
        log.debug("Request to get GalleryAlbum : {}", id);
        return galleryAlbumRepository.findById(id).map(galleryAlbumMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete GalleryAlbum : {}", id);
        galleryAlbumRepository.deleteById(id);
    }

    @Override
    public void updateCoverImageUrl(Long albumId, String coverImageUrl) {
        log.debug("Request to update cover image URL for GalleryAlbum : {}", albumId);
        GalleryAlbum album = galleryAlbumRepository
            .findById(albumId)
            .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Gallery album not found: " + albumId));
        album.setCoverImageUrl(coverImageUrl);
        album.setUpdatedAt(ZonedDateTime.now());
        galleryAlbumRepository.save(album);
    }

    private void applyGalleryCategory(GalleryAlbum galleryAlbum, GalleryAlbumDTO galleryAlbumDTO, boolean requireActiveCategory) {
        if (galleryAlbumDTO.getGalleryCategoryId() == null) {
            galleryAlbum.setGalleryCategory(null);
            return;
        }

        GalleryCategory category = galleryAlbumCategoryValidator.resolveCategory(
            galleryAlbum.getTenantId(),
            galleryAlbumDTO.getGalleryCategoryId(),
            requireActiveCategory
        );
        galleryAlbum.setGalleryCategory(category);
    }

    private void applyEventFields(GalleryAlbum galleryAlbum, GalleryAlbumDTO galleryAlbumDTO) {
        galleryAlbum.setEventDateStart(galleryAlbumDTO.getEventDateStart());
        galleryAlbum.setEventDateEnd(galleryAlbumDTO.getEventDateEnd());
        galleryAlbum.setEventLocation(galleryAlbumDTO.getEventLocation());
    }

    private void applyPartialEventFields(GalleryAlbum galleryAlbum, GalleryAlbumDTO galleryAlbumDTO) {
        if (galleryAlbumDTO.isEventDateStartSet()) {
            galleryAlbum.setEventDateStart(galleryAlbumDTO.getEventDateStart());
        }
        if (galleryAlbumDTO.isEventDateEndSet()) {
            galleryAlbum.setEventDateEnd(galleryAlbumDTO.getEventDateEnd());
        }
        if (galleryAlbumDTO.isEventLocationSet()) {
            galleryAlbum.setEventLocation(galleryAlbumCategoryValidator.normalizeEventLocation(galleryAlbumDTO.getEventLocation()));
        }
    }
}
