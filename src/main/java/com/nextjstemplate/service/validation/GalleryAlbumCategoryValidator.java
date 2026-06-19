package com.nextjstemplate.service.validation;

import com.nextjstemplate.domain.GalleryCategory;
import com.nextjstemplate.errors.BadRequestAlertException;
import com.nextjstemplate.repository.GalleryCategoryRepository;
import org.springframework.stereotype.Component;

/**
 * Validates gallery album category and year fields.
 */
@Component
public class GalleryAlbumCategoryValidator {

    private static final String ENTITY_NAME = "galleryAlbum";

    public static final int MIN_ALBUM_YEAR = 1900;
    public static final int MAX_ALBUM_YEAR = 2100;

    private final GalleryCategoryRepository galleryCategoryRepository;

    public GalleryAlbumCategoryValidator(GalleryCategoryRepository galleryCategoryRepository) {
        this.galleryCategoryRepository = galleryCategoryRepository;
    }

    public void validateAlbumYear(Integer albumYear) {
        if (albumYear == null) {
            return;
        }
        if (albumYear < MIN_ALBUM_YEAR || albumYear > MAX_ALBUM_YEAR) {
            throw new BadRequestAlertException("Album year must be between 1900 and 2100", ENTITY_NAME, "albumYearOutOfRange");
        }
    }

    /**
     * Resolve and validate category for admin create/update operations.
     *
     * @param tenantId album tenant id
     * @param galleryCategoryId category id (nullable to clear on PATCH when explicitly set)
     * @param requireActive when true, inactive categories are rejected
     * @return resolved category or null when id is null
     */
    public GalleryCategory resolveCategory(String tenantId, Long galleryCategoryId, boolean requireActive) {
        if (galleryCategoryId == null) {
            return null;
        }

        GalleryCategory category = galleryCategoryRepository
            .findById(galleryCategoryId)
            .orElseThrow(() -> new BadRequestAlertException("Category not found for tenant", ENTITY_NAME, "categoryNotFoundForTenant"));

        if (!tenantId.equals(category.getTenantId())) {
            throw new BadRequestAlertException("Category not found for tenant", ENTITY_NAME, "categoryNotFoundForTenant");
        }

        if (requireActive && Boolean.FALSE.equals(category.getIsActive())) {
            throw new BadRequestAlertException("Category is not active", ENTITY_NAME, "categoryInactive");
        }

        return category;
    }
}
