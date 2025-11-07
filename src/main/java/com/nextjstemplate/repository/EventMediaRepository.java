package com.nextjstemplate.repository;

import com.nextjstemplate.domain.EventMedia;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring Data JPA repository for the EventMedia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventMediaRepository extends JpaRepository<EventMedia, Long>, JpaSpecificationExecutor<EventMedia> {
    List<EventMedia> findByEventId(Long eventId);

    /**
     * Find all media files for a specific sponsor, sorted by priority ranking (ascending)
     */
    @Query("SELECT e FROM EventMedia e WHERE e.sponsorId = :sponsorId ORDER BY e.priorityRanking ASC")
    List<EventMedia> findBySponsorId(@Param("sponsorId") Long sponsorId);

    /**
     * Find all media files for a specific sponsor with tenant filter, sorted by priority ranking (ascending)
     */
    @Query("SELECT e FROM EventMedia e WHERE e.sponsorId = :sponsorId AND e.tenantId = :tenantId ORDER BY e.priorityRanking ASC")
    List<EventMedia> findBySponsorIdAndTenantId(@Param("sponsorId") Long sponsorId, @Param("tenantId") String tenantId);

    /**
     * Count media files for a specific sponsor and event media type
     */
    @Query("SELECT COUNT(e) FROM EventMedia e WHERE e.sponsorId = :sponsorId AND e.eventMediaType = :eventMediaType")
    long countBySponsorIdAndEventMediaType(@Param("sponsorId") Long sponsorId, @Param("eventMediaType") String eventMediaType);

    /**
     * Count media files for a specific performer and event media type
     */
    @Query("SELECT COUNT(e) FROM EventMedia e WHERE e.performerId = :performerId AND e.eventMediaType = :eventMediaType")
    long countByPerformerIdAndEventMediaType(@Param("performerId") Long performerId, @Param("eventMediaType") String eventMediaType);

    /**
     * Count media files for a specific director and event media type
     */
    @Query("SELECT COUNT(e) FROM EventMedia e WHERE e.directorId = :directorId AND e.eventMediaType = :eventMediaType")
    long countByDirectorIdAndEventMediaType(@Param("directorId") Long directorId, @Param("eventMediaType") String eventMediaType);

    /**
     * Find all media files for an event-sponsor combination, sorted by priority ranking (ascending)
     */
    @Query("SELECT e FROM EventMedia e WHERE e.eventSponsorsJoinId = :eventSponsorsJoinId ORDER BY e.priorityRanking ASC")
    List<EventMedia> findByEventSponsorsJoinId(@Param("eventSponsorsJoinId") Long eventSponsorsJoinId);

    /**
     * Find all media files for an event-sponsor combination with tenant filter, sorted by priority ranking (ascending)
     */
    @Query(
        "SELECT e FROM EventMedia e WHERE e.eventSponsorsJoinId = :eventSponsorsJoinId AND e.tenantId = :tenantId ORDER BY e.priorityRanking ASC"
    )
    List<EventMedia> findByEventSponsorsJoinIdAndTenantId(
        @Param("eventSponsorsJoinId") Long eventSponsorsJoinId,
        @Param("tenantId") String tenantId
    );

    /**
     * Find custom poster for event-sponsor combination, sorted by priority ranking (ascending)
     */
    @Query(
        "SELECT e FROM EventMedia e WHERE e.eventSponsorsJoinId = :eventSponsorsJoinId AND e.eventMediaType = 'EVENT_SPONSOR_POSTER' ORDER BY e.priorityRanking ASC"
    )
    Optional<EventMedia> findCustomPosterByEventSponsorsJoinId(@Param("eventSponsorsJoinId") Long eventSponsorsJoinId);

    // Custom query to fetch EventMedia without LOB fields to avoid LOB stream
    // issues
    @Query(
        value = "SELECT id, tenant_id, title, event_media_type, storage_type, file_url, file_data_content_type, content_type, file_size, is_public, event_flyer, is_event_management_official_document, pre_signed_url, pre_signed_url_expires_at, alt_text, display_order, download_count, is_featured_video, featured_video_url, is_hero_image, is_active_hero_image, is_home_page_hero_image, is_featured_event_image, is_live_event_image, created_at, updated_at, event_id, uploaded_by_id, start_displaying_from_date, sponsor_id, event_sponsors_join_id, performer_id, director_id, priority_ranking FROM event_media",
        nativeQuery = true
    )
    List<Object[]> findAllWithoutLobFieldsRaw();
    /**
     * Update null LOB fields to prevent stream access errors from orphaned LOB
     * references
     */
    /*@Modifying
  @Transactional
  @Query("UPDATE EventMedia e SET e.description = NULL, e.fileData = NULL WHERE e.description IS NOT NULL OR e.fileData IS NOT NULL")
  int updateNullLobFields();*/
}
