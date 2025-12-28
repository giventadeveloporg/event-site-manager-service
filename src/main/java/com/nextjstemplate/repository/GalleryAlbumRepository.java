package com.nextjstemplate.repository;

import com.nextjstemplate.domain.GalleryAlbum;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the GalleryAlbum entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GalleryAlbumRepository extends JpaRepository<GalleryAlbum, Long>, JpaSpecificationExecutor<GalleryAlbum> {
    /**
     * Find all public albums for a specific tenant
     */
    @Query("SELECT ga FROM GalleryAlbum ga WHERE ga.tenantId = :tenantId AND ga.isPublic = true")
    Page<GalleryAlbum> findPublicAlbumsByTenant(@Param("tenantId") String tenantId, Pageable pageable);

    /**
     * Count public media items for a specific album
     */
    @Query("SELECT COUNT(em) FROM EventMedia em WHERE em.album.id = :albumId AND em.isPublic = true")
    Long countPublicMediaByAlbum(@Param("albumId") Long albumId);
}
