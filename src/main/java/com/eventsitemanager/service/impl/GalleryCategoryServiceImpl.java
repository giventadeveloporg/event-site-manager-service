package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.GalleryCategory;
import com.eventsitemanager.errors.ConflictException;
import com.eventsitemanager.repository.GalleryCategoryRepository;
import com.eventsitemanager.service.GalleryCategoryService;
import com.eventsitemanager.service.dto.GalleryCategoryDTO;
import com.eventsitemanager.service.mapper.GalleryCategoryMapper;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.eventsitemanager.domain.GalleryCategory}.
 */
@Service
@Transactional
public class GalleryCategoryServiceImpl implements GalleryCategoryService {

    private static final Logger log = LoggerFactory.getLogger(GalleryCategoryServiceImpl.class);

    private static final String ENTITY_NAME = "galleryCategory";

    private static final Pattern SLUG_PATTERN = Pattern.compile("^[a-z0-9]+(-[a-z0-9]+)*$");

    private final GalleryCategoryRepository galleryCategoryRepository;

    private final GalleryCategoryMapper galleryCategoryMapper;

    public GalleryCategoryServiceImpl(GalleryCategoryRepository galleryCategoryRepository, GalleryCategoryMapper galleryCategoryMapper) {
        this.galleryCategoryRepository = galleryCategoryRepository;
        this.galleryCategoryMapper = galleryCategoryMapper;
    }

    @Override
    public GalleryCategoryDTO save(GalleryCategoryDTO galleryCategoryDTO) {
        log.debug("Request to save GalleryCategory : {}", galleryCategoryDTO);
        validateSlug(galleryCategoryDTO.getSlug());

        GalleryCategory galleryCategory = galleryCategoryMapper.toEntity(galleryCategoryDTO);

        if (galleryCategory.getId() != null) {
            log.warn(
                "GalleryCategory has ID {} set during create operation. Clearing ID to force sequence generation.",
                galleryCategory.getId()
            );
            galleryCategory.setId(null);
        }

        ZonedDateTime now = ZonedDateTime.now();
        if (galleryCategory.getCreatedAt() == null) {
            galleryCategory.setCreatedAt(now);
        }
        galleryCategory.setUpdatedAt(now);
        if (galleryCategory.getIsActive() == null) {
            galleryCategory.setIsActive(true);
        }
        if (galleryCategory.getSortOrder() == null) {
            galleryCategory.setSortOrder(0);
        }

        assertSlugUniqueForTenant(galleryCategory.getTenantId(), galleryCategory.getSlug(), null);

        try {
            galleryCategory = galleryCategoryRepository.save(galleryCategory);
        } catch (DataIntegrityViolationException e) {
            throw conflictForDuplicateSlug(galleryCategoryDTO.getSlug());
        }

        return galleryCategoryMapper.toDto(galleryCategory);
    }

    @Override
    public GalleryCategoryDTO update(GalleryCategoryDTO galleryCategoryDTO) {
        log.debug("Request to update GalleryCategory : {}", galleryCategoryDTO);
        validateSlug(galleryCategoryDTO.getSlug());

        GalleryCategory galleryCategory = galleryCategoryMapper.toEntity(galleryCategoryDTO);
        galleryCategory.setUpdatedAt(ZonedDateTime.now());

        assertSlugUniqueForTenant(galleryCategory.getTenantId(), galleryCategory.getSlug(), galleryCategory.getId());

        try {
            galleryCategory = galleryCategoryRepository.save(galleryCategory);
        } catch (DataIntegrityViolationException e) {
            throw conflictForDuplicateSlug(galleryCategoryDTO.getSlug());
        }

        return galleryCategoryMapper.toDto(galleryCategory);
    }

    @Override
    public Optional<GalleryCategoryDTO> partialUpdate(GalleryCategoryDTO galleryCategoryDTO) {
        log.debug("Request to partially update GalleryCategory : {}", galleryCategoryDTO);

        if (galleryCategoryDTO.getSlug() != null) {
            validateSlug(galleryCategoryDTO.getSlug());
        }

        return galleryCategoryRepository
            .findById(galleryCategoryDTO.getId())
            .map(existingGalleryCategory -> {
                galleryCategoryMapper.partialUpdate(existingGalleryCategory, galleryCategoryDTO);
                existingGalleryCategory.setUpdatedAt(ZonedDateTime.now());
                if (galleryCategoryDTO.getSlug() != null) {
                    assertSlugUniqueForTenant(
                        existingGalleryCategory.getTenantId(),
                        existingGalleryCategory.getSlug(),
                        existingGalleryCategory.getId()
                    );
                }
                return existingGalleryCategory;
            })
            .map(category -> {
                try {
                    return galleryCategoryRepository.save(category);
                } catch (DataIntegrityViolationException e) {
                    throw conflictForDuplicateSlug(galleryCategoryDTO.getSlug());
                }
            })
            .map(galleryCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GalleryCategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all GalleryCategories");
        return galleryCategoryRepository.findAll(pageable).map(galleryCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GalleryCategoryDTO> findOne(Long id) {
        log.debug("Request to get GalleryCategory : {}", id);
        return galleryCategoryRepository.findById(id).map(galleryCategoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete GalleryCategory : {}", id);
        galleryCategoryRepository.deleteById(id);
    }

    private void validateSlug(String slug) {
        if (slug == null || slug.trim().isEmpty()) {
            throw new IllegalArgumentException("Slug cannot be empty");
        }
        if (!SLUG_PATTERN.matcher(slug).matches()) {
            throw new IllegalArgumentException("Slug must contain only lowercase letters, numbers, and hyphens. Invalid slug: " + slug);
        }
    }

    private void assertSlugUniqueForTenant(String tenantId, String slug, Long excludeId) {
        if (tenantId == null || slug == null) {
            return;
        }
        galleryCategoryRepository
            .findByTenantIdAndSlug(tenantId, slug)
            .filter(existing -> excludeId == null || !existing.getId().equals(excludeId))
            .ifPresent(existing -> {
                throw conflictForDuplicateSlug(slug);
            });
    }

    private ConflictException conflictForDuplicateSlug(String slug) {
        return new ConflictException("Category slug already exists", ENTITY_NAME, "slugexists");
    }
}
