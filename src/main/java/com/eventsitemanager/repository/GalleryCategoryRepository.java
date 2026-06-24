package com.eventsitemanager.repository;

import com.eventsitemanager.domain.GalleryCategory;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the GalleryCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GalleryCategoryRepository extends JpaRepository<GalleryCategory, Long>, JpaSpecificationExecutor<GalleryCategory> {
    Optional<GalleryCategory> findByTenantIdAndSlug(String tenantId, String slug);
}
