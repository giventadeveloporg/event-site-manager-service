package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.EventMediaDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Interface for managing {@link com.eventsitemanager.domain.EventMedia}.
 */
public interface EventMediaService {
    /**
     * Save a eventMedia.
     *
     * @param eventMediaDTO the entity to save.
     * @return the persisted entity.
     */
    EventMediaDTO save(EventMediaDTO eventMediaDTO);

    /**
     * Updates a eventMedia.
     *
     * @param eventMediaDTO the entity to update.
     * @return the persisted entity.
     */
    EventMediaDTO update(EventMediaDTO eventMediaDTO);

    /**
     * Partially updates a eventMedia.
     *
     * @param eventMediaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EventMediaDTO> partialUpdate(EventMediaDTO eventMediaDTO);

    /**
     * Get all the eventMedias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EventMediaDTO> findAll(Pageable pageable);

    /**
     * Get the "id" eventMedia.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EventMediaDTO> findOne(Long id);

    /**
     * Delete the "id" eventMedia.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Upload a single file and create an EventMedia entry.
     */
    EventMediaDTO uploadFile(
        MultipartFile file,
        Long eventId,
        Long userProfileId,
        String title,
        String description,
        String tenantId,
        boolean isPublic,
        Boolean eventFlyer,
        Boolean isEventManagementOfficialDocument,
        Boolean isHeroImage,
        Boolean isActiveHeroImage,
        Boolean isTeamMemberProfileImage,
        Long executiveTeamMemberID,
        boolean isHomePageHeroImage,
        boolean isFeaturedEventImage,
        boolean isLiveEventImage,
        LocalDate startDisplayingFromDate,
        // New entity-specific parameters
        Boolean isFeaturedPerformerPortrait,
        Boolean isFeaturedPerformerPerformance,
        Boolean isFeaturedPerformerGallery,
        Boolean isSponsorLogo,
        Boolean isSponsorHero,
        Boolean isSponsorBanner,
        Boolean isContactPhoto,
        Boolean isProgramDirectorPhoto,
        Long entityId,
        String entityType,
        String imageType,
        Long eventSponsorsJoinId,
        Long sponsorId,
        Long directorId,
        Long performerId,
        String eventMediaType,
        String storageType
    );

    /**
     * Upload multiple files and create EventMedia entries.
     */
    List<EventMediaDTO> uploadMultipleFiles(
        List<MultipartFile> files,
        Long eventId,
        Long userProfileId,
        List<String> titles,
        List<String> descriptions,
        String tenantId,
        boolean isPublic,
        Boolean eventFlyer,
        Boolean isEventManagementOfficialDocument,
        Boolean isHeroImage,
        Boolean isActiveHeroImage,
        Boolean isTeamMemberProfileImage,
        Long executiveTeamMemberID,
        boolean isHomePageHeroImage,
        boolean isFeaturedEventImage,
        boolean isLiveEventImage,
        LocalDate startDisplayingFromDate
    );

    /**
     * Upload a single tenant-level official document (non-event) with tenant-scoped S3 prefix.
     */
    EventMediaDTO uploadTenantOfficialDocument(
        MultipartFile file,
        String tenantId,
        String categorySlug,
        Integer officialDocumentYear,
        String title,
        String description,
        String hierarchyPath,
        String hierarchyCategoryLabel,
        Integer displayPriority,
        boolean isPublic,
        MultipartFile thumbnailFile,
        Long userProfileId
    );

    /**
     * Bulk upload tenant-level official documents (non-event).
     * Each uploaded file creates a separate {@code event_media} row.
     */
    List<EventMediaDTO> uploadBulkTenantOfficialDocuments(
        List<MultipartFile> files,
        String tenantId,
        String categorySlug,
        Integer officialDocumentYear,
        String titlePrefix,
        String description,
        String hierarchyPath,
        String hierarchyCategoryLabel,
        Integer displayPriority,
        boolean isPublic,
        MultipartFile thumbnailFile,
        Long userProfileId
    );

    /**
     * Attach or replace thumbnail on an existing official document row.
     */
    EventMediaDTO uploadOfficialDocumentThumbnail(Long mediaId, MultipartFile thumbnailFile, Long userProfileId);

    List<EventMediaDTO> getEventMediaWithUrls(Long eventId, Long userProfileId, boolean includePrivate);

    Page<EventMediaDTO> findPublicOfficialDocumentsForDownloads(
        String tenantId,
        Long officialDocumentCategoryId,
        Integer officialDocumentYear,
        Pageable pageable
    );

    String getViewingUrl(Long mediaId, Long userProfileId);

    List<EventMediaDTO> findAllWithoutLobFields();

    /**
     * Upload sponsor image (logo, hero, or banner).
     * Updates the corresponding field in event_sponsors table.
     * Also creates an EventMedia record with priority_ranking = 0.
     *
     * @param sponsorId the sponsor ID.
     * @param eventId the event ID (can be null or 0 for main sponsors page).
     * @param imageType the image type ("logo", "hero", or "banner").
     * @param file the file to upload.
     * @param tenantId the tenant ID.
     * @return the created EventMedia DTO.
     */
    EventMediaDTO uploadSponsorImage(Long sponsorId, Long eventId, String imageType, MultipartFile file, String tenantId);

    /**
     * Upload custom poster for event-sponsor combination.
     * Updates event_sponsors_join.custom_poster_url field.
     * Also creates an EventMedia record with priority_ranking = 0.
     *
     * @param eventId the event ID.
     * @param sponsorId the sponsor ID.
     * @param file the file to upload.
     * @param tenantId the tenant ID.
     * @return the created EventMedia DTO.
     */
    EventMediaDTO uploadEventSponsorJoinPoster(Long eventId, Long sponsorId, MultipartFile file, String tenantId);

    /**
     * Upload multiple media files for a sponsor.
     * Creates EventMedia record with priority_ranking = 0 (can be updated later).
     *
     * @param sponsorId the sponsor ID.
     * @param file the file to upload.
     * @param title the title (optional).
     * @param description the description (optional).
     * @param tenantId the tenant ID.
     * @param priorityRanking the priority ranking (optional, defaults to 0).
     * @return the created EventMedia DTO.
     */
    EventMediaDTO uploadSponsorMedia(
        Long sponsorId,
        MultipartFile file,
        String title,
        String description,
        String tenantId,
        Integer priorityRanking
    );

    /**
     * Upload multiple media files for an event-sponsor combination.
     * Creates EventMedia record with priority_ranking = 0 (can be updated later).
     *
     * @param eventId the event ID.
     * @param sponsorId the sponsor ID.
     * @param file the file to upload.
     * @param title the title (optional).
     * @param description the description (optional).
     * @param tenantId the tenant ID.
     * @param priorityRanking the priority ranking (optional, defaults to 0).
     * @return the created EventMedia DTO.
     */
    EventMediaDTO uploadEventSponsorMedia(
        Long eventId,
        Long sponsorId,
        MultipartFile file,
        String title,
        String description,
        String tenantId,
        Integer priorityRanking
    );

    /**
     * Find all media files for a sponsor, sorted by priority ranking (ascending).
     *
     * @param sponsorId the sponsor ID.
     * @param tenantId the tenant ID (optional).
     * @return list of EventMedia DTOs sorted by priority ranking.
     */
    List<EventMediaDTO> findBySponsorId(Long sponsorId, String tenantId);

    /**
     * Find all media files for an event-sponsor combination, sorted by priority ranking (ascending).
     *
     * @param eventSponsorsJoinId the event-sponsors join ID.
     * @param tenantId the tenant ID (optional).
     * @return list of EventMedia DTOs sorted by priority ranking.
     */
    List<EventMediaDTO> findByEventSponsorsJoinId(Long eventSponsorsJoinId, String tenantId);

    /**
     * Update priority ranking for a media file.
     *
     * @param mediaId the media ID.
     * @param priorityRanking the new priority ranking.
     * @return the updated EventMedia DTO.
     */
    EventMediaDTO updateMediaPriorityRanking(Long mediaId, Integer priorityRanking);

    /**
     * Upload email header image for an event.
     * Uploads an image to S3 and updates the event_details.email_header_image_url field.
     * Also creates an EventMedia record for audit/tracking.
     *
     * @param eventId Event ID
     * @param file Multipart file to upload
     * @param tenantId Tenant ID
     * @param title Optional title (defaults to "Email Header Image")
     * @param description Optional description (defaults to "Email header image for ticket confirmation emails")
     * @param isPublic Optional public flag (defaults to true)
     * @return EventMediaDTO with uploaded image details
     */
    EventMediaDTO uploadEmailHeaderImage(
        Long eventId,
        MultipartFile file,
        String tenantId,
        String title,
        String description,
        Boolean isPublic
    );

    /**
     * Upload promotional email header image for a promotion template.
     * Uploads an image to S3 and updates the promotion_email_template.header_image_url field.
     * Also creates an EventMedia record for audit/tracking.
     *
     * @param eventId Event ID
     * @param promotionId Promotion email template ID
     * @param file Multipart file to upload
     * @param tenantId Tenant ID
     * @param title Optional title (defaults to "Promotional Email Header Image")
     * @param description Optional description (defaults to "Promotional email header image")
     * @param isPublic Optional public flag (defaults to true)
     * @return EventMediaDTO with uploaded image details
     */
    EventMediaDTO uploadPromotionalEmailHeaderImage(
        Long eventId,
        Long promotionId,
        MultipartFile file,
        String tenantId,
        String title,
        String description,
        Boolean isPublic
    );

    /**
     * Upload promotional email footer image for a promotion template.
     * Uploads an image to S3 and updates the promotion_email_template.footer_image_url field.
     * Also creates an EventMedia record for audit/tracking.
     *
     * @param eventId Event ID
     * @param promotionId Promotion email template ID
     * @param file Multipart file to upload
     * @param tenantId Tenant ID
     * @param title Optional title (defaults to "Promotional Email Footer Image")
     * @param description Optional description (defaults to "Promotional email footer image")
     * @param isPublic Optional public flag (defaults to true)
     * @return EventMediaDTO with uploaded image details
     */
    EventMediaDTO uploadPromotionalEmailFooterImage(
        Long eventId,
        Long promotionId,
        MultipartFile file,
        String tenantId,
        String title,
        String description,
        Boolean isPublic
    );

    /**
     * Upload focus group cover image.
     * Uploads an image to S3 and updates the focus_group.cover_image_url field.
     * Also creates an EventMedia record for audit/tracking.
     *
     * @param focusGroupId Focus Group ID
     * @param file Multipart file to upload
     * @param tenantId Tenant ID
     * @param title Optional title (defaults to "Focus Group Cover Image")
     * @param description Optional description (defaults to "Cover image for focus group")
     * @param isPublic Optional public flag (defaults to true)
     * @return EventMediaDTO with uploaded image details
     */
    EventMediaDTO uploadFocusGroupCoverImage(
        Long focusGroupId,
        MultipartFile file,
        String tenantId,
        String title,
        String description,
        Boolean isPublic
    );

    /**
     * Upload gallery album cover image.
     * Uploads an image to S3, creates an EventMedia record, and updates gallery_album.cover_image_url.
     *
     * @param albumId Gallery album ID
     * @param file Multipart file to upload
     * @param tenantId Tenant ID
     * @param title Optional title (defaults to "Gallery Album Cover Image")
     * @param description Optional description (defaults to "Cover image for gallery album")
     * @param isPublic Optional public flag (defaults to true)
     * @return EventMediaDTO with uploaded image details
     */
    EventMediaDTO uploadGalleryAlbumCoverImage(
        Long albumId,
        MultipartFile file,
        String tenantId,
        String title,
        String description,
        Boolean isPublic
    );
}
