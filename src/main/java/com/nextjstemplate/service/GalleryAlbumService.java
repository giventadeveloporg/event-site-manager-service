package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.GalleryAlbumDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.nextjstemplate.domain.GalleryAlbum}.
 */
public interface GalleryAlbumService {
    /**
     * Save a galleryAlbum.
     *
     * @param galleryAlbumDTO the entity to save.
     * @return the persisted entity.
     */
    GalleryAlbumDTO save(GalleryAlbumDTO galleryAlbumDTO);

    /**
     * Updates a galleryAlbum.
     *
     * @param galleryAlbumDTO the entity to update.
     * @return the persisted entity.
     */
    GalleryAlbumDTO update(GalleryAlbumDTO galleryAlbumDTO);

    /**
     * Partially updates a galleryAlbum.
     *
     * @param galleryAlbumDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GalleryAlbumDTO> partialUpdate(GalleryAlbumDTO galleryAlbumDTO);

    /**
     * Get all the galleryAlbums.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<GalleryAlbumDTO> findAll(Pageable pageable);

    /**
     * Get the "id" galleryAlbum.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GalleryAlbumDTO> findOne(Long id);

    /**
     * Delete the "id" galleryAlbum.
     * When album is deleted, associated media records should be updated (album_id set to NULL),
     * NOT deleted (media files are preserved).
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
