package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.GalleryAlbum;
import com.nextjstemplate.repository.GalleryAlbumRepository;
import com.nextjstemplate.service.GalleryAlbumService;
import com.nextjstemplate.service.dto.GalleryAlbumDTO;
import com.nextjstemplate.service.mapper.GalleryAlbumMapper;
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

    public GalleryAlbumServiceImpl(GalleryAlbumRepository galleryAlbumRepository, GalleryAlbumMapper galleryAlbumMapper) {
        this.galleryAlbumRepository = galleryAlbumRepository;
        this.galleryAlbumMapper = galleryAlbumMapper;
    }

    @Override
    public GalleryAlbumDTO save(GalleryAlbumDTO galleryAlbumDTO) {
        log.debug("Request to save GalleryAlbum : {}", galleryAlbumDTO);
        GalleryAlbum galleryAlbum = galleryAlbumMapper.toEntity(galleryAlbumDTO);

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
        GalleryAlbum galleryAlbum = galleryAlbumMapper.toEntity(galleryAlbumDTO);

        // Always update the updatedAt timestamp
        galleryAlbum.setUpdatedAt(ZonedDateTime.now());

        galleryAlbum = galleryAlbumRepository.save(galleryAlbum);
        return galleryAlbumMapper.toDto(galleryAlbum);
    }

    @Override
    public Optional<GalleryAlbumDTO> partialUpdate(GalleryAlbumDTO galleryAlbumDTO) {
        log.debug("Request to partially update GalleryAlbum : {}", galleryAlbumDTO);

        return galleryAlbumRepository
            .findById(galleryAlbumDTO.getId())
            .map(existingGalleryAlbum -> {
                galleryAlbumMapper.partialUpdate(existingGalleryAlbum, galleryAlbumDTO);

                // Always update the updatedAt timestamp
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
        // When album is deleted, associated media records should be updated (album_id set to NULL),
        // NOT deleted (media files are preserved). This is handled by ON DELETE SET NULL foreign key constraint.
        galleryAlbumRepository.deleteById(id);
    }
}
