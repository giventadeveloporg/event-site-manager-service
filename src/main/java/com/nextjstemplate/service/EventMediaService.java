package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.EventMediaDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Interface for managing {@link com.nextjstemplate.domain.EventMedia}.
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

    List<EventMediaDTO> getEventMediaWithUrls(Long eventId, Long userProfileId, boolean includePrivate);

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
}
