package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.GalleryCategoryDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.nextjstemplate.domain.GalleryCategory}.
 */
public interface GalleryCategoryService {
    GalleryCategoryDTO save(GalleryCategoryDTO galleryCategoryDTO);

    GalleryCategoryDTO update(GalleryCategoryDTO galleryCategoryDTO);

    Optional<GalleryCategoryDTO> partialUpdate(GalleryCategoryDTO galleryCategoryDTO);

    Page<GalleryCategoryDTO> findAll(Pageable pageable);

    Optional<GalleryCategoryDTO> findOne(Long id);

    void delete(Long id);
}
