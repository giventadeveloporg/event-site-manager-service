package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.EventDetails;
import com.eventsitemanager.domain.EventFeaturedPerformers;
import com.eventsitemanager.domain.EventFocusGroup;
import com.eventsitemanager.domain.EventMedia;
import com.eventsitemanager.domain.EventProgramDirectors;
import com.eventsitemanager.domain.EventSponsors;
import com.eventsitemanager.domain.EventSponsorsJoin;
import com.eventsitemanager.domain.ExecutiveCommitteeTeamMember;
import com.eventsitemanager.domain.GalleryAlbum;
import com.eventsitemanager.domain.OfficialDocumentCategory;
import com.eventsitemanager.domain.PromotionEmailTemplate;
import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.EventDetailsRepository;
import com.eventsitemanager.repository.EventFeaturedPerformersRepository;
import com.eventsitemanager.repository.EventFocusGroupRepository;
import com.eventsitemanager.repository.EventMediaRepository;
import com.eventsitemanager.repository.EventProgramDirectorsRepository;
import com.eventsitemanager.repository.EventSponsorsJoinRepository;
import com.eventsitemanager.repository.EventSponsorsRepository;
import com.eventsitemanager.repository.ExecutiveCommitteeTeamMemberRepository;
import com.eventsitemanager.repository.GalleryAlbumRepository;
import com.eventsitemanager.repository.OfficialDocumentCategoryRepository;
import com.eventsitemanager.repository.PromotionEmailTemplateRepository;
import com.eventsitemanager.service.EventDetailsService;
import com.eventsitemanager.service.EventMediaService;
import com.eventsitemanager.service.FocusGroupService;
import com.eventsitemanager.service.GalleryAlbumService;
import com.eventsitemanager.service.OfficialDocumentYearBundleService;
import com.eventsitemanager.service.PromotionEmailTemplateService;
import com.eventsitemanager.service.S3Service;
import com.eventsitemanager.service.dto.EventDetailsDTO;
import com.eventsitemanager.service.dto.EventMediaDTO;
import com.eventsitemanager.service.dto.FocusGroupDTO;
import com.eventsitemanager.service.dto.PromotionEmailTemplateDTO;
import com.eventsitemanager.service.mapper.EventMediaMapper;
import com.eventsitemanager.service.mapper.PromotionEmailTemplateMapper;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for managing
 * {@link com.eventsitemanager.domain.EventMedia}.
 */
@Service
@Transactional
public class EventMediaServiceImpl implements EventMediaService {

    private final Logger log = LoggerFactory.getLogger(EventMediaServiceImpl.class);

    private final EventMediaRepository eventMediaRepository;

    private final EventDetailsRepository eventRepository;

    private final EventMediaMapper eventMediaMapper;

    private final S3Service s3Service;

    private final ExecutiveCommitteeTeamMemberRepository executiveCommitteeTeamMemberRepository;

    private final EventSponsorsRepository eventSponsorsRepository;

    private final EventSponsorsJoinRepository eventSponsorsJoinRepository;

    private final EventFeaturedPerformersRepository eventFeaturedPerformersRepository;

    private final EventProgramDirectorsRepository eventProgramDirectorsRepository;

    private final EventDetailsService eventDetailsService;

    private final PromotionEmailTemplateRepository promotionEmailTemplateRepository;

    private final PromotionEmailTemplateService promotionEmailTemplateService;

    private final PromotionEmailTemplateMapper promotionEmailTemplateMapper;

    private final FocusGroupService focusGroupService;

    private final GalleryAlbumService galleryAlbumService;

    private final GalleryAlbumRepository galleryAlbumRepository;

    private final EventFocusGroupRepository eventFocusGroupRepository;

    private final OfficialDocumentCategoryRepository officialDocumentCategoryRepository;

    private final OfficialDocumentYearBundleService officialDocumentYearBundleService;

    @Autowired
    public EventMediaServiceImpl(
        EventMediaRepository eventMediaRepository,
        EventMediaMapper eventMediaMapper,
        S3Service s3Service,
        EventDetailsRepository eventRepository,
        ExecutiveCommitteeTeamMemberRepository executiveCommitteeTeamMemberRepository,
        EventSponsorsRepository eventSponsorsRepository,
        EventSponsorsJoinRepository eventSponsorsJoinRepository,
        EventFeaturedPerformersRepository eventFeaturedPerformersRepository,
        EventProgramDirectorsRepository eventProgramDirectorsRepository,
        EventDetailsService eventDetailsService,
        PromotionEmailTemplateRepository promotionEmailTemplateRepository,
        PromotionEmailTemplateService promotionEmailTemplateService,
        PromotionEmailTemplateMapper promotionEmailTemplateMapper,
        FocusGroupService focusGroupService,
        GalleryAlbumService galleryAlbumService,
        GalleryAlbumRepository galleryAlbumRepository,
        EventFocusGroupRepository eventFocusGroupRepository,
        OfficialDocumentCategoryRepository officialDocumentCategoryRepository,
        OfficialDocumentYearBundleService officialDocumentYearBundleService
    ) {
        this.eventMediaRepository = eventMediaRepository;
        this.eventMediaMapper = eventMediaMapper;
        this.s3Service = s3Service;
        this.eventRepository = eventRepository;
        this.executiveCommitteeTeamMemberRepository = executiveCommitteeTeamMemberRepository;
        this.eventSponsorsRepository = eventSponsorsRepository;
        this.eventSponsorsJoinRepository = eventSponsorsJoinRepository;
        this.eventFeaturedPerformersRepository = eventFeaturedPerformersRepository;
        this.eventProgramDirectorsRepository = eventProgramDirectorsRepository;
        this.eventDetailsService = eventDetailsService;
        this.promotionEmailTemplateRepository = promotionEmailTemplateRepository;
        this.promotionEmailTemplateService = promotionEmailTemplateService;
        this.promotionEmailTemplateMapper = promotionEmailTemplateMapper;
        this.focusGroupService = focusGroupService;
        this.galleryAlbumService = galleryAlbumService;
        this.galleryAlbumRepository = galleryAlbumRepository;
        this.eventFocusGroupRepository = eventFocusGroupRepository;
        this.officialDocumentCategoryRepository = officialDocumentCategoryRepository;
        this.officialDocumentYearBundleService = officialDocumentYearBundleService;
    }

    @Override
    @CacheEvict(value = "eventMedia", allEntries = true)
    public EventMediaDTO save(EventMediaDTO eventMediaDTO) {
        log.debug("Request to save EventMedia : {}", eventMediaDTO);

        // Validate that event_id and album_id are mutually exclusive
        validateEventAlbumMutuallyExclusive(eventMediaDTO.getEventId(), eventMediaDTO.getAlbumId());
        // Validate that event_focus_group belongs to the same event as the media
        validateEventFocusGroupMatchesEvent(eventMediaDTO.getEventFocusGroupId(), eventMediaDTO.getEventId());

        // CRITICAL: For child events, if isHomePageHeroImage is true and one already exists, update it instead of creating new
        if (
            eventMediaDTO.getIsHomePageHeroImage() != null && eventMediaDTO.getIsHomePageHeroImage() && eventMediaDTO.getEventId() != null
        ) {
            try {
                EventDetails eventDetails = eventRepository.findById(eventMediaDTO.getEventId()).orElse(null);
                if (eventDetails != null && eventDetails.getParentEvent() != null) {
                    // This is a child event - check if homepage hero image already exists
                    List<EventMedia> existingHomePageHeroImages = eventMediaRepository.findByEventIdAndIsHomePageHeroImageTrue(
                        eventDetails.getId()
                    );
                    if (!existingHomePageHeroImages.isEmpty()) {
                        // Update the first existing record instead of creating new
                        EventMedia existingMedia = existingHomePageHeroImages.get(0);
                        log.info(
                            "Child event {} already has homepage hero image (ID: {}) - updating with new image",
                            eventDetails.getId(),
                            existingMedia.getId()
                        );

                        // Update all fields from DTO
                        eventMediaMapper.partialUpdate(existingMedia, eventMediaDTO);
                        existingMedia.setUpdatedAt(ZonedDateTime.now());

                        EventMedia updatedMedia = eventMediaRepository.save(existingMedia);
                        return eventMediaMapper.toDto(updatedMedia);
                    }
                }
            } catch (Exception e) {
                log.error("Failed to check/update existing homepage hero image for child event {}", eventMediaDTO.getEventId(), e);
                // Continue with normal save if check fails
            }
        }

        EventMedia eventMedia = eventMediaMapper.toEntity(eventMediaDTO);

        // Set album relationship if albumId is provided
        if (eventMediaDTO.getAlbumId() != null) {
            GalleryAlbum album = galleryAlbumRepository
                .findById(eventMediaDTO.getAlbumId())
                .orElseThrow(() -> new EntityNotFoundException("Album not found: " + eventMediaDTO.getAlbumId()));
            eventMedia.setAlbum(album);
            // Ensure eventId is null when albumId is set
            eventMedia.setEventId(null);
        } else {
            eventMedia.setAlbum(null);
        }

        eventMedia = eventMediaRepository.save(eventMedia);

        // CRITICAL: Replicate homepage hero image to child events if this is a parent event
        if (eventMedia.getIsHomePageHeroImage() != null && eventMedia.getIsHomePageHeroImage() && eventMedia.getEventId() != null) {
            try {
                EventDetails eventDetails = eventRepository.findById(eventMedia.getEventId()).orElse(null);
                if (eventDetails != null && eventDetails.getParentEvent() == null) {
                    // This is a parent event - replicate to children
                    log.info("Homepage hero image saved for parent event {} - replicating to child events", eventMedia.getEventId());
                    replicateHomePageHeroImageToChildren(eventMedia, eventDetails);
                }
            } catch (Exception e) {
                log.error("Failed to replicate homepage hero image to child events for event {}", eventMedia.getEventId(), e);
                // Don't fail the save if replication fails
            }
        }

        return eventMediaMapper.toDto(eventMedia);
    }

    @Override
    @CacheEvict(value = "eventMedia", allEntries = true)
    public EventMediaDTO update(EventMediaDTO eventMediaDTO) {
        log.debug("Request to update EventMedia : {}", eventMediaDTO);

        // Validate that event_id and album_id are mutually exclusive
        validateEventAlbumMutuallyExclusive(eventMediaDTO.getEventId(), eventMediaDTO.getAlbumId());
        // Validate that event_focus_group belongs to the same event as the media
        validateEventFocusGroupMatchesEvent(eventMediaDTO.getEventFocusGroupId(), eventMediaDTO.getEventId());

        // Check if isHomePageHeroImage is being set to true
        boolean isHomePageHeroImageBeingSet = eventMediaDTO.getIsHomePageHeroImage() != null && eventMediaDTO.getIsHomePageHeroImage();

        // CRITICAL: For child events, if isHomePageHeroImage is true and one already exists (different ID), update it instead
        if (isHomePageHeroImageBeingSet && eventMediaDTO.getEventId() != null) {
            try {
                EventDetails eventDetails = eventRepository.findById(eventMediaDTO.getEventId()).orElse(null);
                if (eventDetails != null && eventDetails.getParentEvent() != null) {
                    // This is a child event - check if homepage hero image already exists (with different ID)
                    List<EventMedia> existingHomePageHeroImages = eventMediaRepository.findByEventIdAndIsHomePageHeroImageTrue(
                        eventDetails.getId()
                    );
                    if (!existingHomePageHeroImages.isEmpty()) {
                        EventMedia existingMedia = existingHomePageHeroImages.get(0);
                        // If updating a different record, update the existing one instead
                        if (eventMediaDTO.getId() == null || !existingMedia.getId().equals(eventMediaDTO.getId())) {
                            log.info(
                                "Child event {} already has homepage hero image (ID: {}) - updating with new data",
                                eventDetails.getId(),
                                existingMedia.getId()
                            );

                            // Update all fields from DTO
                            eventMediaMapper.partialUpdate(existingMedia, eventMediaDTO);
                            existingMedia.setUpdatedAt(ZonedDateTime.now());

                            EventMedia updatedMedia = eventMediaRepository.save(existingMedia);
                            return eventMediaMapper.toDto(updatedMedia);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("Failed to check/update existing homepage hero image for child event {}", eventMediaDTO.getEventId(), e);
                // Continue with normal update if check fails
            }
        }

        EventMedia eventMedia = eventMediaMapper.toEntity(eventMediaDTO);

        // Set album relationship if albumId is provided
        if (eventMediaDTO.getAlbumId() != null) {
            GalleryAlbum album = galleryAlbumRepository
                .findById(eventMediaDTO.getAlbumId())
                .orElseThrow(() -> new EntityNotFoundException("Album not found: " + eventMediaDTO.getAlbumId()));
            eventMedia.setAlbum(album);
            // Ensure eventId is null when albumId is set
            eventMedia.setEventId(null);
        } else {
            eventMedia.setAlbum(null);
        }

        eventMedia = eventMediaRepository.save(eventMedia);

        // CRITICAL: Replicate homepage hero image to child events if this is a parent event and isHomePageHeroImage is true
        if (isHomePageHeroImageBeingSet && eventMedia.getEventId() != null) {
            try {
                EventDetails eventDetails = eventRepository.findById(eventMedia.getEventId()).orElse(null);
                if (eventDetails != null && eventDetails.getParentEvent() == null) {
                    // This is a parent event - replicate to children
                    log.info("Homepage hero image updated for parent event {} - replicating to child events", eventMedia.getEventId());
                    replicateHomePageHeroImageToChildren(eventMedia, eventDetails);
                }
            } catch (Exception e) {
                log.error("Failed to replicate homepage hero image to child events for event {}", eventMedia.getEventId(), e);
                // Don't fail the update if replication fails
            }
        }

        return eventMediaMapper.toDto(eventMedia);
    }

    @Override
    public Optional<EventMediaDTO> partialUpdate(EventMediaDTO eventMediaDTO) {
        log.debug("Request to partially update EventMedia : {}", eventMediaDTO);

        // Validate that event_id and album_id are mutually exclusive
        validateEventAlbumMutuallyExclusive(eventMediaDTO.getEventId(), eventMediaDTO.getAlbumId());
        validateEventFocusGroupMatchesEvent(eventMediaDTO.getEventFocusGroupId(), eventMediaDTO.getEventId());

        // Check if isHomePageHeroImage is being set to true
        boolean isHomePageHeroImageBeingSet = eventMediaDTO.getIsHomePageHeroImage() != null && eventMediaDTO.getIsHomePageHeroImage();

        // If updating by ID, use normal flow
        if (eventMediaDTO.getId() != null) {
            return eventMediaRepository
                .findById(eventMediaDTO.getId())
                .map(existingEventMedia -> {
                    eventMediaMapper.partialUpdate(existingEventMedia, eventMediaDTO);

                    // Set album relationship if albumId is provided
                    if (eventMediaDTO.getAlbumId() != null) {
                        GalleryAlbum album = galleryAlbumRepository
                            .findById(eventMediaDTO.getAlbumId())
                            .orElseThrow(() -> new EntityNotFoundException("Album not found: " + eventMediaDTO.getAlbumId()));
                        existingEventMedia.setAlbum(album);
                        // Ensure eventId is null when albumId is set
                        existingEventMedia.setEventId(null);
                    } else if (eventMediaDTO.getEventId() != null) {
                        // If eventId is being set and albumId is not provided, clear the album association
                        existingEventMedia.setAlbum(null);
                    }

                    return existingEventMedia;
                })
                .map(eventMediaRepository::save)
                .map(savedEventMedia -> {
                    // CRITICAL: Replicate homepage hero image to child events if this is a parent event and isHomePageHeroImage is true
                    if (isHomePageHeroImageBeingSet && savedEventMedia.getEventId() != null) {
                        try {
                            EventDetails eventDetails = eventRepository.findById(savedEventMedia.getEventId()).orElse(null);
                            if (eventDetails != null && eventDetails.getParentEvent() == null) {
                                // This is a parent event - replicate to children
                                log.info(
                                    "Homepage hero image partially updated for parent event {} - replicating to child events",
                                    savedEventMedia.getEventId()
                                );
                                replicateHomePageHeroImageToChildren(savedEventMedia, eventDetails);
                            }
                        } catch (Exception e) {
                            log.error(
                                "Failed to replicate homepage hero image to child events for event {}",
                                savedEventMedia.getEventId(),
                                e
                            );
                            // Don't fail the update if replication fails
                        }
                    }
                    return savedEventMedia;
                })
                .map(eventMediaMapper::toDto);
        }

        // If no ID provided but eventId is provided and isHomePageHeroImage is true, check for child event
        if (isHomePageHeroImageBeingSet && eventMediaDTO.getEventId() != null) {
            try {
                EventDetails eventDetails = eventRepository.findById(eventMediaDTO.getEventId()).orElse(null);
                if (eventDetails != null && eventDetails.getParentEvent() != null) {
                    // This is a child event - check if homepage hero image already exists
                    List<EventMedia> existingHomePageHeroImages = eventMediaRepository.findByEventIdAndIsHomePageHeroImageTrue(
                        eventDetails.getId()
                    );
                    if (!existingHomePageHeroImages.isEmpty()) {
                        // Update the first existing record instead of creating new
                        EventMedia existingMedia = existingHomePageHeroImages.get(0);
                        log.info(
                            "Child event {} already has homepage hero image (ID: {}) - updating with new data",
                            eventDetails.getId(),
                            existingMedia.getId()
                        );

                        // Update all fields from DTO
                        eventMediaMapper.partialUpdate(existingMedia, eventMediaDTO);
                        existingMedia.setUpdatedAt(ZonedDateTime.now());

                        EventMedia updatedMedia = eventMediaRepository.save(existingMedia);
                        return Optional.of(eventMediaMapper.toDto(updatedMedia));
                    }
                }
            } catch (Exception e) {
                log.error("Failed to check/update existing homepage hero image for child event {}", eventMediaDTO.getEventId(), e);
                // Continue with normal flow if check fails
            }
        }

        // Fallback: return empty if no ID and can't find existing record
        return Optional.empty();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventMediaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EventMedias");
        return eventMediaRepository.findAll(pageable).map(eventMediaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "eventMedia", key = "#id", unless = "#result == null")
    public Optional<EventMediaDTO> findOne(Long id) {
        log.debug("Request to get EventMedia : {}", id);
        return eventMediaRepository.findById(id).map(eventMediaMapper::toDto);
    }

    @Override
    @CacheEvict(value = "eventMedia", allEntries = true)
    public void delete(Long id) {
        log.debug("Request to delete EventMedia : {}", id);
        eventMediaRepository.deleteById(id);
    }

    public EventMediaDTO uploadFile(
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
    ) {
        // Handle EVENT_SPONSOR_POSTER type uploads (custom poster for event-sponsor combination)
        if (
            eventMediaType != null &&
            eventMediaType.equals("EVENT_SPONSOR_POSTER") &&
            eventSponsorsJoinId != null &&
            eventId != null &&
            sponsorId != null
        ) {
            log.debug(
                "Processing event-sponsor poster upload: eventId={}, sponsorId={}, eventSponsorsJoinId={}",
                eventId,
                sponsorId,
                eventSponsorsJoinId
            );

            // 1. Find event_sponsors_join record
            EventSponsorsJoin joinRecord = eventSponsorsJoinRepository
                .findById(eventSponsorsJoinId)
                .orElseThrow(() -> new RuntimeException("Event-sponsor association not found: eventSponsorsJoinId=" + eventSponsorsJoinId));

            // 2. Generate S3 path using the correct format: dev/events/tenantId/{tenantId}/event-id/{eventId}/sponsors/sponsor_id/{sponsorId}/{filename}
            String s3Path = s3Service.generateEventSponsorJoinImagePath(tenantId, eventId, sponsorId, file.getOriginalFilename());

            // 3. Upload to S3
            String s3Url = s3Service.uploadFile(s3Path, file);

            // 4. Create EventMedia record
            EventMedia eventMedia = new EventMedia();
            eventMedia.setEventId(eventId);
            eventMedia.setSponsorId(sponsorId);
            eventMedia.setEventSponsorsJoinId(eventSponsorsJoinId); // ✅ CRITICAL: Set this field
            eventMedia.setTitle(title);
            eventMedia.setDescription(description != null ? description : "Custom poster for event-sponsor combination");
            eventMedia.setFileUrl(s3Url);
            eventMedia.setPreSignedUrl(s3Service.generatePresignedUrl(s3Url, 1));
            eventMedia.setFileSize((int) file.getSize());
            eventMedia.setEventMediaType(eventMediaType);
            eventMedia.setStorageType(storageType != null ? storageType : "S3");
            eventMedia.setIsPublic(isPublic);
            eventMedia.setTenantId(tenantId);
            eventMedia.setPriorityRanking(0); // Default to highest priority
            eventMedia.setCreatedAt(ZonedDateTime.now());
            eventMedia.setUpdatedAt(ZonedDateTime.now());
            eventMedia.setEventFlyer(eventFlyer != null ? eventFlyer : false);
            eventMedia.setIsEventManagementOfficialDocument(
                isEventManagementOfficialDocument != null ? isEventManagementOfficialDocument : false
            );
            eventMedia.setIsHeroImage(isHeroImage != null ? isHeroImage : false);
            eventMedia.setIsActiveHeroImage(isActiveHeroImage != null ? isActiveHeroImage : false);
            eventMedia.setIsHomePageHeroImage(isHomePageHeroImage);
            eventMedia.setIsFeaturedEventImage(isFeaturedEventImage);
            eventMedia.setIsLiveEventImage(isLiveEventImage);
            eventMedia.setStartDisplayingFromDate(startDisplayingFromDate != null ? startDisplayingFromDate : LocalDate.now());
            eventMedia.setUploadedById(userProfileId);

            // 5. Save EventMedia record
            eventMedia = eventMediaRepository.save(eventMedia);
            log.debug(
                "Successfully created EventMedia record for event-sponsor poster: eventId={}, sponsorId={}, eventSponsorsJoinId={}",
                eventId,
                sponsorId,
                eventSponsorsJoinId
            );

            // 6. Update event_sponsors_join.custom_poster_url
            try {
                joinRecord.setCustomPosterUrl(s3Url);
                eventSponsorsJoinRepository.save(joinRecord);
                log.debug("Updated event_sponsors_join.custom_poster_url for joinId={}", eventSponsorsJoinId);
            } catch (Exception e) {
                log.error("Warning: Could not update event_sponsors_join.custom_poster_url: {}", e.getMessage());
                // Don't fail the upload if we can't update the join record
            }

            return eventMediaMapper.toDto(eventMedia);
        }

        // Handle EVENT_DIRECTOR_POSTER type uploads (custom poster for event-director combination)
        if (eventMediaType != null && eventMediaType.equals("EVENT_DIRECTOR_POSTER") && eventId != null && directorId != null) {
            log.debug("Processing event-director poster upload: eventId={}, directorId={}", eventId, directorId);

            // 1. Find director record
            EventProgramDirectors director = eventProgramDirectorsRepository
                .findById(directorId)
                .orElseThrow(() -> new RuntimeException("Director not found: directorId=" + directorId));

            // 2. Generate S3 path using the correct format: dev/events/tenantId/{tenantId}/event-id/{eventId}/program-directors/director_id/{directorId}/{filename}
            String s3Path = s3Service.generateEventDirectorPosterPath(tenantId, eventId, directorId, file.getOriginalFilename());

            // 3. Upload to S3
            String s3Url = s3Service.uploadFile(s3Path, file);

            // 4. Create EventMedia record
            EventMedia eventMedia = new EventMedia();
            eventMedia.setEventId(eventId);
            eventMedia.setDirectorId(directorId);
            eventMedia.setTitle(title);
            eventMedia.setDescription(description != null ? description : "Custom poster for event-director combination");
            eventMedia.setFileUrl(s3Url);
            eventMedia.setPreSignedUrl(s3Service.generatePresignedUrl(s3Url, 1));
            eventMedia.setFileSize((int) file.getSize());
            eventMedia.setEventMediaType(eventMediaType);
            eventMedia.setStorageType(storageType != null ? storageType : "S3");
            eventMedia.setIsPublic(isPublic);
            eventMedia.setTenantId(tenantId);
            eventMedia.setPriorityRanking(0); // Default to highest priority
            eventMedia.setCreatedAt(ZonedDateTime.now());
            eventMedia.setUpdatedAt(ZonedDateTime.now());
            eventMedia.setEventFlyer(eventFlyer != null ? eventFlyer : false);
            eventMedia.setIsEventManagementOfficialDocument(
                isEventManagementOfficialDocument != null ? isEventManagementOfficialDocument : false
            );
            eventMedia.setIsHeroImage(isHeroImage != null ? isHeroImage : false);
            eventMedia.setIsActiveHeroImage(isActiveHeroImage != null ? isActiveHeroImage : false);
            eventMedia.setIsHomePageHeroImage(isHomePageHeroImage);
            eventMedia.setIsFeaturedEventImage(isFeaturedEventImage);
            eventMedia.setIsLiveEventImage(isLiveEventImage);
            eventMedia.setStartDisplayingFromDate(startDisplayingFromDate != null ? startDisplayingFromDate : LocalDate.now());
            eventMedia.setUploadedById(userProfileId);

            // 5. Save EventMedia record
            eventMedia = eventMediaRepository.save(eventMedia);
            log.debug("Successfully created EventMedia record for event-director poster: eventId={}, directorId={}", eventId, directorId);

            // 6. Optionally update director.photoUrl if this is the first poster (similar to sponsors)
            try {
                // Check if this is the first poster for this director in this event
                // Count existing posters before saving (should be 0 for first poster)
                long existingPosterCount = eventMediaRepository.countByDirectorIdAndEventIdAndEventMediaType(
                    directorId,
                    eventId,
                    "EVENT_DIRECTOR_POSTER"
                );
                // Since we just saved one, if count is 1, this was the first poster
                if (existingPosterCount == 1) {
                    director.setPhotoUrl(s3Url);
                    eventProgramDirectorsRepository.save(director);
                    log.debug("Updated director.photoUrl for directorId={}", directorId);
                }
            } catch (Exception e) {
                log.error("Warning: Could not update director.photoUrl: {}", e.getMessage());
                // Don't fail the upload if we can't update the director record
            }

            return eventMediaMapper.toDto(eventMedia);
        }

        // Handle EVENT_PERFORMER_POSTER type uploads (custom poster for event-performer combination)
        if (eventMediaType != null && eventMediaType.equals("EVENT_PERFORMER_POSTER") && eventId != null && performerId != null) {
            log.debug("Processing event-performer poster upload: eventId={}, performerId={}", eventId, performerId);

            // 1. Find performer record
            EventFeaturedPerformers performer = eventFeaturedPerformersRepository
                .findById(performerId)
                .orElseThrow(() -> new RuntimeException("Performer not found: performerId=" + performerId));

            // 2. Generate S3 path using the correct format: dev/events/tenantId/{tenantId}/event-id/{eventId}/performers/performer_id/{performerId}/{filename}
            String s3Path = s3Service.generateEventPerformerPosterPath(tenantId, eventId, performerId, file.getOriginalFilename());

            // 3. Upload to S3
            String s3Url = s3Service.uploadFile(s3Path, file);

            // 4. Create EventMedia record
            EventMedia eventMedia = new EventMedia();
            eventMedia.setEventId(eventId);
            eventMedia.setPerformerId(performerId);
            eventMedia.setTitle(title);
            eventMedia.setDescription(description != null ? description : "Custom poster for event-performer combination");
            eventMedia.setFileUrl(s3Url);
            eventMedia.setPreSignedUrl(s3Service.generatePresignedUrl(s3Url, 1));
            eventMedia.setFileSize((int) file.getSize());
            eventMedia.setEventMediaType(eventMediaType);
            eventMedia.setStorageType(storageType != null ? storageType : "S3");
            eventMedia.setIsPublic(isPublic);
            eventMedia.setTenantId(tenantId);
            eventMedia.setPriorityRanking(0); // Default to highest priority
            eventMedia.setCreatedAt(ZonedDateTime.now());
            eventMedia.setUpdatedAt(ZonedDateTime.now());
            eventMedia.setEventFlyer(eventFlyer != null ? eventFlyer : false);
            eventMedia.setIsEventManagementOfficialDocument(
                isEventManagementOfficialDocument != null ? isEventManagementOfficialDocument : false
            );
            eventMedia.setIsHeroImage(isHeroImage != null ? isHeroImage : false);
            eventMedia.setIsActiveHeroImage(isActiveHeroImage != null ? isActiveHeroImage : false);
            eventMedia.setIsHomePageHeroImage(isHomePageHeroImage);
            eventMedia.setIsFeaturedEventImage(isFeaturedEventImage);
            eventMedia.setIsLiveEventImage(isLiveEventImage);
            eventMedia.setStartDisplayingFromDate(startDisplayingFromDate != null ? startDisplayingFromDate : LocalDate.now());
            eventMedia.setUploadedById(userProfileId);

            // 5. Save EventMedia record
            eventMedia = eventMediaRepository.save(eventMedia);
            log.debug(
                "Successfully created EventMedia record for event-performer poster: eventId={}, performerId={}",
                eventId,
                performerId
            );

            // 6. Optionally update performer.portraitImageUrl if this is the first poster (similar to directors/sponsors)
            try {
                // Check if this is the first poster for this performer in this event
                long existingPosterCount = eventMediaRepository.countByPerformerIdAndEventIdAndEventMediaType(
                    performerId,
                    eventId,
                    "EVENT_PERFORMER_POSTER"
                );
                // Since we just saved one, if count is 1, this was the first poster
                if (existingPosterCount == 1 && performer.getPortraitImageUrl() == null) {
                    performer.setPortraitImageUrl(s3Url);
                    eventFeaturedPerformersRepository.save(performer);
                    log.debug("Updated performer.portraitImageUrl for performerId={}", performerId);
                }
            } catch (Exception e) {
                log.error("Warning: Could not update performer.portraitImageUrl: {}", e.getMessage());
                // Don't fail the upload if we can't update the performer record
            }

            return eventMediaMapper.toDto(eventMedia);
        }

        // Upload to S3 - use entity-specific path if entity parameters are provided
        String fileUrl;
        if (entityId != null && entityType != null && imageType != null) {
            // For sponsors without event association, use sponsor-specific path generation
            if (entityType.equalsIgnoreCase("SPONSOR") && (eventId == null || eventId == 0)) {
                // Use sponsor-specific S3 path (doesn't require eventId)
                // Pass imageType to generate filename with sponsor_{imageType} format
                String s3Path = s3Service.generateSponsorImagePath(tenantId, entityId, file.getOriginalFilename(), imageType);
                fileUrl = s3Service.uploadFile(s3Path, file);
            } else if (entityType.equalsIgnoreCase("PERFORMER") && (eventId == null || eventId == 0)) {
                // Use performer-specific S3 path (doesn't require eventId)
                String s3Path = s3Service.generatePerformerImagePath(tenantId, entityId, file.getOriginalFilename());
                fileUrl = s3Service.uploadFile(s3Path, file);
            } else if (entityType.equalsIgnoreCase("DIRECTOR") && (eventId == null || eventId == 0)) {
                // Use director-specific S3 path (doesn't require eventId)
                String s3Path = s3Service.generateDirectorImagePath(tenantId, entityId, file.getOriginalFilename());
                fileUrl = s3Service.uploadFile(s3Path, file);
            } else {
                // Use entity-specific upload with dynamic path construction (for events)
                fileUrl = s3Service.uploadFileWithEntityPath(file, eventId, entityId, entityType, imageType, title, tenantId);
            }
        } else {
            // Use existing upload method for backward compatibility
            fileUrl = s3Service.uploadFile(file, eventId, title, tenantId, isTeamMemberProfileImage);
        }

        // Handle sponsor image uploads (logo, hero, banner) without event association
        if (
            entityType != null &&
            entityType.equalsIgnoreCase("SPONSOR") &&
            entityId != null &&
            (isSponsorLogo != null || isSponsorHero != null || isSponsorBanner != null)
        ) {
            log.debug(
                "Processing sponsor image upload: entityId={}, isSponsorLogo={}, isSponsorHero={}, isSponsorBanner={}",
                entityId,
                isSponsorLogo,
                isSponsorHero,
                isSponsorBanner
            );

            // Validate sponsor exists
            EventSponsors sponsor = eventSponsorsRepository
                .findById(entityId)
                .orElseThrow(() -> new RuntimeException("Sponsor not found with ID: " + entityId));

            // Determine image type
            String imageTypeLower = (imageType != null) ? imageType.toLowerCase() : "";
            String determinedEventMediaType = null;
            if (isSponsorLogo != null && isSponsorLogo) {
                imageTypeLower = "logo";
                determinedEventMediaType = "SPONSOR_LOGO";
            } else if (isSponsorHero != null && isSponsorHero) {
                imageTypeLower = "hero";
                determinedEventMediaType = "SPONSOR_HERO";
            } else if (isSponsorBanner != null && isSponsorBanner) {
                imageTypeLower = "banner";
                determinedEventMediaType = "SPONSOR_BANNER";
            }

            // Only update sponsor table URL if this is the first image of this type
            // This allows multiple images per type while maintaining a "primary" image in the sponsor record
            if (determinedEventMediaType != null) {
                // Check if there are existing images of this type
                long existingCount = eventMediaRepository.countBySponsorIdAndEventMediaType(entityId, determinedEventMediaType);

                // Only update sponsor table if this is the first image of this type
                if (existingCount == 0) {
                    if (isSponsorLogo != null && isSponsorLogo) {
                        sponsor.setLogoUrl(fileUrl);
                    } else if (isSponsorHero != null && isSponsorHero) {
                        sponsor.setHeroImageUrl(fileUrl);
                    } else if (isSponsorBanner != null && isSponsorBanner) {
                        sponsor.setBannerImageUrl(fileUrl);
                    }
                    eventSponsorsRepository.save(sponsor);
                    log.debug("Updated sponsor table with first {} image URL", imageTypeLower);
                } else {
                    log.debug(
                        "Skipping sponsor table update - {} images of type {} already exist. Creating new EventMedia record only.",
                        existingCount,
                        determinedEventMediaType
                    );
                }
            }

            // Create EventMedia record for sponsor image
            EventMedia eventMedia = new EventMedia();
            eventMedia.setTitle(title);
            eventMedia.setDescription(description);
            eventMedia.setTenantId(tenantId);
            // Use the determined eventMediaType (already set above) or the parameter if provided
            if (eventMediaType != null && !eventMediaType.isEmpty()) {
                eventMedia.setEventMediaType(eventMediaType);
            } else if (determinedEventMediaType != null) {
                eventMedia.setEventMediaType(determinedEventMediaType);
            } else {
                eventMedia.setEventMediaType("SPONSOR_" + imageTypeLower.toUpperCase());
            }
            eventMedia.setStorageType("S3");
            eventMedia.setFileUrl(fileUrl);
            eventMedia.setPreSignedUrl(s3Service.generatePresignedUrl(fileUrl, 1));
            eventMedia.setFileSize((int) file.getSize());
            eventMedia.setIsPublic(isPublic);
            eventMedia.setCreatedAt(ZonedDateTime.now());
            eventMedia.setUpdatedAt(ZonedDateTime.now());
            eventMedia.setEventFlyer(eventFlyer);
            eventMedia.setIsEventManagementOfficialDocument(isEventManagementOfficialDocument);
            eventMedia.setIsHeroImage(isHeroImage);
            eventMedia.setIsActiveHeroImage(isActiveHeroImage);
            eventMedia.setIsHomePageHeroImage(isHomePageHeroImage);
            eventMedia.setIsFeaturedEventImage(isFeaturedEventImage);
            eventMedia.setIsLiveEventImage(isLiveEventImage);
            eventMedia.setStartDisplayingFromDate(startDisplayingFromDate != null ? startDisplayingFromDate : LocalDate.now());

            // Set event_id to null if eventId is 0 or null (sponsor images without event association)
            if (eventId == null || eventId == 0) {
                eventMedia.setEventId(null);
            } else {
                eventMedia.setEventId(eventId);
            }

            // Set sponsor_id to link this media to the sponsor
            eventMedia.setSponsorId(entityId);

            // Set eventSponsorsJoinId if provided
            if (eventSponsorsJoinId != null) {
                eventMedia.setEventSponsorsJoinId(eventSponsorsJoinId);
            }

            // Set priority ranking (default to 0 for highest priority)
            eventMedia.setPriorityRanking(0);

            eventMedia.setUploadedById(userProfileId);

            eventMedia = eventMediaRepository.save(eventMedia);
            log.debug("Successfully created EventMedia record for sponsor image: sponsorId={}, imageType={}", entityId, imageTypeLower);
            return eventMediaMapper.toDto(eventMedia);
        } else if (
            entityType != null &&
            entityType.equalsIgnoreCase("PERFORMER") &&
            entityId != null &&
            (isFeaturedPerformerPortrait != null || isFeaturedPerformerPerformance != null)
        ) {
            log.debug(
                "Processing performer image upload: entityId={}, isFeaturedPerformerPortrait={}, isFeaturedPerformerPerformance={}",
                entityId,
                isFeaturedPerformerPortrait,
                isFeaturedPerformerPerformance
            );

            // Validate performer exists
            EventFeaturedPerformers performer = eventFeaturedPerformersRepository
                .findById(entityId)
                .orElseThrow(() -> new RuntimeException("Performer not found with ID: " + entityId));

            // Determine image type
            String imageTypeLower = (imageType != null) ? imageType.toLowerCase() : "";
            String determinedEventMediaType = null;
            if (isFeaturedPerformerPortrait != null && isFeaturedPerformerPortrait) {
                imageTypeLower = "portrait";
                determinedEventMediaType = "PERFORMER_PORTRAIT";
            } else if (isFeaturedPerformerPerformance != null && isFeaturedPerformerPerformance) {
                imageTypeLower = "performance";
                determinedEventMediaType = "PERFORMER_PERFORMANCE";
            }

            // Only update performer table URL if this is the first image of this type
            if (determinedEventMediaType != null) {
                long existingCount = eventMediaRepository.countByPerformerIdAndEventMediaType(entityId, determinedEventMediaType);

                // Only update performer table if this is the first image of this type
                if (existingCount == 0) {
                    if (isFeaturedPerformerPortrait != null && isFeaturedPerformerPortrait) {
                        performer.setPortraitImageUrl(fileUrl);
                    } else if (isFeaturedPerformerPerformance != null && isFeaturedPerformerPerformance) {
                        performer.setPerformanceImageUrl(fileUrl);
                    }
                    eventFeaturedPerformersRepository.save(performer);
                    log.debug("Updated performer table with first {} image URL", imageTypeLower);
                } else {
                    log.debug(
                        "Skipping performer table update - {} images of type {} already exist. Creating new EventMedia record only.",
                        existingCount,
                        determinedEventMediaType
                    );
                }
            }

            // Create EventMedia record for performer image
            EventMedia eventMedia = new EventMedia();
            eventMedia.setTitle(title);
            eventMedia.setDescription(description);
            eventMedia.setTenantId(tenantId);
            // Use the parameter if provided, otherwise use the determined type
            if (eventMediaType != null && !eventMediaType.isEmpty()) {
                eventMedia.setEventMediaType(eventMediaType);
            } else if (determinedEventMediaType != null) {
                eventMedia.setEventMediaType(determinedEventMediaType);
            } else {
                eventMedia.setEventMediaType("PERFORMER_" + imageTypeLower.toUpperCase());
            }
            eventMedia.setStorageType("S3");
            eventMedia.setFileUrl(fileUrl);
            eventMedia.setPreSignedUrl(s3Service.generatePresignedUrl(fileUrl, 1));
            eventMedia.setFileSize((int) file.getSize());
            eventMedia.setIsPublic(isPublic);
            eventMedia.setCreatedAt(ZonedDateTime.now());
            eventMedia.setUpdatedAt(ZonedDateTime.now());
            eventMedia.setEventFlyer(eventFlyer);
            eventMedia.setIsEventManagementOfficialDocument(isEventManagementOfficialDocument);
            eventMedia.setIsHeroImage(isHeroImage);
            eventMedia.setIsActiveHeroImage(isActiveHeroImage);
            eventMedia.setIsHomePageHeroImage(isHomePageHeroImage);
            eventMedia.setIsFeaturedEventImage(isFeaturedEventImage);
            eventMedia.setIsLiveEventImage(isLiveEventImage);
            eventMedia.setStartDisplayingFromDate(startDisplayingFromDate != null ? startDisplayingFromDate : LocalDate.now());

            // Set event_id to null if eventId is 0 or null (performer images without event association)
            if (eventId == null || eventId == 0) {
                eventMedia.setEventId(null);
            } else {
                eventMedia.setEventId(eventId);
            }

            // Set performer_id to link this media to the performer
            eventMedia.setPerformerId(entityId);

            // Set priority ranking (default to 0 for highest priority)
            eventMedia.setPriorityRanking(0);

            eventMedia.setUploadedById(userProfileId);

            eventMedia = eventMediaRepository.save(eventMedia);
            log.debug("Successfully created EventMedia record for performer image: performerId={}, imageType={}", entityId, imageTypeLower);
            return eventMediaMapper.toDto(eventMedia);
        } else if (
            entityType != null &&
            entityType.equalsIgnoreCase("DIRECTOR") &&
            entityId != null &&
            isProgramDirectorPhoto != null &&
            isProgramDirectorPhoto
        ) {
            log.debug("Processing director image upload: entityId={}, isProgramDirectorPhoto={}", entityId, isProgramDirectorPhoto);

            // Validate director exists
            EventProgramDirectors director = eventProgramDirectorsRepository
                .findById(entityId)
                .orElseThrow(() -> new RuntimeException("Director not found with ID: " + entityId));

            // Determine image type
            String imageTypeLower = (imageType != null) ? imageType.toLowerCase() : "photo";
            String determinedEventMediaType = "DIRECTOR_PHOTO";

            // Only update director table URL if this is the first image of this type
            long existingCount = eventMediaRepository.countByDirectorIdAndEventMediaType(entityId, determinedEventMediaType);

            // Only update director table if this is the first image of this type
            if (existingCount == 0) {
                director.setPhotoUrl(fileUrl);
                eventProgramDirectorsRepository.save(director);
                log.debug("Updated director table with first photo image URL");
            } else {
                log.debug(
                    "Skipping director table update - {} images of type {} already exist. Creating new EventMedia record only.",
                    existingCount,
                    determinedEventMediaType
                );
            }

            // Create EventMedia record for director image
            EventMedia eventMedia = new EventMedia();
            eventMedia.setTitle(title);
            eventMedia.setDescription(description);
            eventMedia.setTenantId(tenantId);
            // Use the parameter if provided, otherwise use the determined type
            if (eventMediaType != null && !eventMediaType.isEmpty()) {
                eventMedia.setEventMediaType(eventMediaType);
            } else {
                eventMedia.setEventMediaType(determinedEventMediaType);
            }
            eventMedia.setStorageType("S3");
            eventMedia.setFileUrl(fileUrl);
            eventMedia.setPreSignedUrl(s3Service.generatePresignedUrl(fileUrl, 1));
            eventMedia.setFileSize((int) file.getSize());
            eventMedia.setIsPublic(isPublic);
            eventMedia.setCreatedAt(ZonedDateTime.now());
            eventMedia.setUpdatedAt(ZonedDateTime.now());
            eventMedia.setEventFlyer(eventFlyer);
            eventMedia.setIsEventManagementOfficialDocument(isEventManagementOfficialDocument);
            eventMedia.setIsHeroImage(isHeroImage);
            eventMedia.setIsActiveHeroImage(isActiveHeroImage);
            eventMedia.setIsHomePageHeroImage(isHomePageHeroImage);
            eventMedia.setIsFeaturedEventImage(isFeaturedEventImage);
            eventMedia.setIsLiveEventImage(isLiveEventImage);
            eventMedia.setStartDisplayingFromDate(startDisplayingFromDate != null ? startDisplayingFromDate : LocalDate.now());

            // Set event_id to null if eventId is 0 or null (director images without event association)
            if (eventId == null || eventId == 0) {
                eventMedia.setEventId(null);
            } else {
                eventMedia.setEventId(eventId);
            }

            // Set director_id to link this media to the director
            eventMedia.setDirectorId(entityId);

            // Set priority ranking (default to 0 for highest priority)
            eventMedia.setPriorityRanking(0);

            eventMedia.setUploadedById(userProfileId);

            eventMedia = eventMediaRepository.save(eventMedia);
            log.debug("Successfully created EventMedia record for director image: directorId={}, imageType={}", entityId, imageTypeLower);
            return eventMediaMapper.toDto(eventMedia);
        } else if (isTeamMemberProfileImage == null || !isTeamMemberProfileImage) {
            EventMedia eventMedia = new EventMedia();
            // eventMedia.setEvent(event); // Event relationship handled by mapper
            eventMedia.setTitle(title);
            eventMedia.setDescription(description);
            eventMedia.setTenantId(tenantId);
            eventMedia.setEventMediaType(file.getContentType() != null ? file.getContentType() : "unknown");
            eventMedia.setStorageType("S3");
            eventMedia.setFileUrl(fileUrl);
            log.info("preSignedUrl length: " + s3Service.generatePresignedUrl(fileUrl, 1).length());
            log.info("preSignedUrl value: " + s3Service.generatePresignedUrl(fileUrl, 1));
            eventMedia.setPreSignedUrl(s3Service.generatePresignedUrl(fileUrl, 1));
            // eventMedia.setFileDataContentType(file.getContentType());
            eventMedia.setFileSize((int) file.getSize());
            eventMedia.setIsPublic(isPublic);
            eventMedia.setCreatedAt(ZonedDateTime.now());
            eventMedia.setUpdatedAt(ZonedDateTime.now());
            eventMedia.setEventFlyer(eventFlyer);
            eventMedia.setIsEventManagementOfficialDocument(isEventManagementOfficialDocument);
            eventMedia.setIsHeroImage(isHeroImage);
            eventMedia.setIsActiveHeroImage(isActiveHeroImage);
            eventMedia.setIsHomePageHeroImage(isHomePageHeroImage);
            eventMedia.setIsFeaturedEventImage(isFeaturedEventImage);
            eventMedia.setIsLiveEventImage(isLiveEventImage);
            // Set startDisplayingFromDate (default to today if not provided to satisfy NOT NULL constraint)
            eventMedia.setStartDisplayingFromDate(startDisplayingFromDate != null ? startDisplayingFromDate : LocalDate.now());

            // Set event_id to null if eventId is 0 or null (for entities without event association)
            if (eventId == null || eventId == 0) {
                eventMedia.setEventId(null);
            } else {
                eventMedia.setEventId(eventId);
            }

            // Set eventSponsorsJoinId if provided
            if (eventSponsorsJoinId != null) {
                eventMedia.setEventSponsorsJoinId(eventSponsorsJoinId);
            }
            // Set sponsorId if provided
            if (sponsorId != null) {
                eventMedia.setSponsorId(sponsorId);
            }

            eventMedia.setUploadedById(userProfileId);
            // Optionally set event and uploadedBy if needed (requires fetching entities)
            // eventMedia.setEvent(...);
            // eventMedia.setUploadedBy(...);

            // CRITICAL: For child events, if isHomePageHeroImage is true and one already exists, update it instead of creating new
            if (isHomePageHeroImage && eventId != null && eventId > 0) {
                try {
                    EventDetails eventDetails = eventRepository.findById(eventId).orElse(null);
                    if (eventDetails != null && eventDetails.getParentEvent() != null) {
                        // This is a child event - check if homepage hero image already exists
                        List<EventMedia> existingHomePageHeroImages = eventMediaRepository.findByEventIdAndIsHomePageHeroImageTrue(eventId);
                        if (!existingHomePageHeroImages.isEmpty()) {
                            // Update the first existing record instead of creating new
                            EventMedia existingMedia = existingHomePageHeroImages.get(0);
                            log.info(
                                "Child event {} already has homepage hero image (ID: {}) - updating with new image",
                                eventId,
                                existingMedia.getId()
                            );

                            // Update all fields from the new upload
                            existingMedia.setTitle(eventMedia.getTitle());
                            existingMedia.setDescription(eventMedia.getDescription());
                            existingMedia.setFileUrl(eventMedia.getFileUrl());
                            existingMedia.setPreSignedUrl(eventMedia.getPreSignedUrl());
                            existingMedia.setContentType(eventMedia.getContentType());
                            existingMedia.setFileSize(eventMedia.getFileSize());
                            existingMedia.setIsPublic(eventMedia.getIsPublic());
                            existingMedia.setEventFlyer(eventMedia.getEventFlyer());
                            existingMedia.setIsEventManagementOfficialDocument(eventMedia.getIsEventManagementOfficialDocument());
                            existingMedia.setAltText(eventMedia.getAltText());
                            existingMedia.setDisplayOrder(eventMedia.getDisplayOrder());
                            existingMedia.setIsFeaturedVideo(eventMedia.getIsFeaturedVideo());
                            existingMedia.setFeaturedVideoUrl(eventMedia.getFeaturedVideoUrl());
                            existingMedia.setIsHeroImage(eventMedia.getIsHeroImage());
                            existingMedia.setIsActiveHeroImage(eventMedia.getIsActiveHeroImage());
                            existingMedia.setIsFeaturedEventImage(eventMedia.getIsFeaturedEventImage());
                            existingMedia.setIsLiveEventImage(eventMedia.getIsLiveEventImage());
                            existingMedia.setStartDisplayingFromDate(eventMedia.getStartDisplayingFromDate());
                            existingMedia.setPriorityRanking(
                                eventMedia.getPriorityRanking() != null
                                    ? eventMedia.getPriorityRanking()
                                    : existingMedia.getPriorityRanking()
                            );
                            existingMedia.setUpdatedAt(ZonedDateTime.now());

                            eventMedia = eventMediaRepository.save(existingMedia);
                            return eventMediaMapper.toDto(eventMedia);
                        }
                    }
                } catch (Exception e) {
                    log.error("Failed to check/update existing homepage hero image for child event {}", eventId, e);
                    // Continue with normal save if check fails
                }
            }

            eventMedia = eventMediaRepository.save(eventMedia);

            // CRITICAL: Replicate homepage hero image to child events if this is a parent event
            if (isHomePageHeroImage && eventId != null && eventId > 0) {
                try {
                    EventDetails eventDetails = eventRepository.findById(eventId).orElse(null);
                    if (eventDetails != null && eventDetails.getParentEvent() == null) {
                        // This is a parent event - replicate to children
                        log.info("Homepage hero image uploaded for parent event {} - replicating to child events", eventId);
                        replicateHomePageHeroImageToChildren(eventMedia, eventDetails);
                    }
                } catch (Exception e) {
                    log.error("Failed to replicate homepage hero image to child events for event {}", eventId, e);
                    // Don't fail the upload if replication fails
                }
            }

            return eventMediaMapper.toDto(eventMedia);
        } else if (executiveTeamMemberID != null) {
            // Handle ExecutiveCommitteeTeamMember profile image update
            log.debug("Updating ExecutiveCommitteeTeamMember profile image for ID: {}", executiveTeamMemberID);
            ExecutiveCommitteeTeamMember teamMember = executiveCommitteeTeamMemberRepository
                .findById(executiveTeamMemberID)
                .orElseThrow(() -> new RuntimeException("ExecutiveCommitteeTeamMember not found with ID: " + executiveTeamMemberID));
            teamMember.setProfileImageUrl(fileUrl);
            executiveCommitteeTeamMemberRepository.save(teamMember);
            log.debug("Successfully updated profile image URL for ExecutiveCommitteeTeamMember ID: {}", executiveTeamMemberID);

            // Create a minimal EventMediaDTO for response (since no EventMedia entity was
            // created)
            EventMediaDTO responseDTO = new EventMediaDTO();
            responseDTO.setId(-1L); // Use -1 to indicate this is not a real EventMedia record
            responseDTO.setTitle(title);
            responseDTO.setFileUrl(fileUrl);
            responseDTO.setTenantId(tenantId);
            responseDTO.setEventMediaType(file.getContentType() != null ? file.getContentType() : "unknown");
            responseDTO.setFileSize((int) file.getSize());
            return responseDTO;
        }

        // This should not happen in normal flow, but return null as fallback
        return null;
    }

    @Override
    public List<EventMediaDTO> uploadMultipleFiles(
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
    ) {
        List<EventMediaDTO> result = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            String title = (titles != null && i < titles.size()) ? titles.get(i) : file.getOriginalFilename();
            String description = (descriptions != null && i < descriptions.size()) ? descriptions.get(i) : null;
            EventMediaDTO uploadResult = uploadFile(
                file,
                eventId,
                userProfileId,
                title,
                description,
                tenantId,
                isPublic,
                eventFlyer,
                isEventManagementOfficialDocument,
                isHeroImage,
                isActiveHeroImage,
                isTeamMemberProfileImage,
                executiveTeamMemberID,
                isHomePageHeroImage,
                isFeaturedEventImage,
                isLiveEventImage,
                startDisplayingFromDate,
                // New entity-specific parameters - all null for multiple files upload
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null, // directorId
                null, // performerId
                null,
                null
            );
            // Only add non-null results to the list
            if (uploadResult != null) {
                result.add(uploadResult);
            }
        }
        return result;
    }

    @Override
    public EventMediaDTO uploadTenantOfficialDocument(
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
    ) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestAlertException("File cannot be empty", "eventMedia", "fileempty");
        }
        if (tenantId == null || tenantId.isBlank()) {
            throw new BadRequestAlertException("Tenant ID is required", "eventMedia", "tenantrequired");
        }
        if (title == null || title.isBlank()) {
            throw new BadRequestAlertException("Title is required", "eventMedia", "titlerequired");
        }
        if (categorySlug == null || categorySlug.isBlank()) {
            throw new BadRequestAlertException("Category slug is required", "eventMedia", "categorySlugrequired");
        }
        if (officialDocumentYear == null) {
            throw new BadRequestAlertException("Official document year is required", "eventMedia", "yeardownrequired");
        }

        String normalizedSlug = normalizeOfficialDocumentCategorySlug(categorySlug);
        validateOfficialDocumentYear(officialDocumentYear);
        String normalizedHierarchyPath = normalizeHierarchyPath(hierarchyPath);
        validateDisplayPriority(displayPriority);

        OfficialDocumentCategory category = officialDocumentCategoryRepository
            .findByTenantIdAndSlug(tenantId, normalizedSlug)
            .orElseThrow(() -> new BadRequestAlertException("Invalid category", "eventMedia", "invalidcategory"));

        String s3Url = s3Service.uploadTenantOfficialDocumentFile(file, tenantId, normalizedSlug, officialDocumentYear, title);

        EventMedia eventMedia = new EventMedia();
        eventMedia.setTenantId(tenantId);
        eventMedia.setTitle(title);
        eventMedia.setDescription(description);
        eventMedia.setEventMediaType("TENANT_OFFICIAL_DOCUMENT");
        eventMedia.setStorageType("S3");
        eventMedia.setFileUrl(s3Url);
        eventMedia.setPreSignedUrl(s3Service.generatePresignedUrl(s3Url, 1));
        eventMedia.setFileSize((int) file.getSize());
        if (file.getContentType() != null && !file.getContentType().isBlank()) {
            eventMedia.setContentType(file.getContentType());
        }
        eventMedia.setIsPublic(isPublic);

        eventMedia.setEventFlyer(false);
        eventMedia.setIsEventManagementOfficialDocument(true);

        // Non-event document defaults
        eventMedia.setIsHomePageHeroImage(false);
        eventMedia.setIsFeaturedEventImage(false);
        eventMedia.setIsLiveEventImage(false);
        eventMedia.setIsHeroImage(false);
        eventMedia.setIsActiveHeroImage(false);
        eventMedia.setIsFeaturedVideo(false);

        eventMedia.setStartDisplayingFromDate(LocalDate.now());
        eventMedia.setUploadedById(userProfileId);
        eventMedia.setPriorityRanking(0);

        // Official document metadata
        eventMedia.setOfficialDocumentCategoryId(category.getId());
        eventMedia.setOfficialDocumentYear(officialDocumentYear);
        eventMedia.setHierarchyPath(normalizedHierarchyPath);
        eventMedia.setHierarchyCategoryLabel(hierarchyCategoryLabel);
        eventMedia.setDisplayPriority(displayPriority);

        applyOptionalOfficialDocumentThumbnail(thumbnailFile, eventMedia, tenantId, normalizedSlug, officialDocumentYear, title);

        eventMedia.setCreatedAt(ZonedDateTime.now());
        eventMedia.setUpdatedAt(ZonedDateTime.now());

        eventMedia = eventMediaRepository.save(eventMedia);
        officialDocumentYearBundleService.ensureBundleForUpload(tenantId, category.getId(), officialDocumentYear);
        return eventMediaMapper.toDto(eventMedia);
    }

    @Override
    public List<EventMediaDTO> uploadBulkTenantOfficialDocuments(
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
    ) {
        if (files == null || files.isEmpty()) {
            throw new BadRequestAlertException("Files list cannot be empty", "eventMedia", "filesempty");
        }
        if (tenantId == null || tenantId.isBlank()) {
            throw new BadRequestAlertException("Tenant ID is required", "eventMedia", "tenantrequired");
        }
        if (titlePrefix == null || titlePrefix.isBlank()) {
            titlePrefix = "Official Document";
        }
        if (categorySlug == null || categorySlug.isBlank()) {
            throw new BadRequestAlertException("Category slug is required", "eventMedia", "categorySlugrequired");
        }
        if (officialDocumentYear == null) {
            throw new BadRequestAlertException("Official document year is required", "eventMedia", "yeardownrequired");
        }

        String normalizedSlug = normalizeOfficialDocumentCategorySlug(categorySlug);
        validateOfficialDocumentYear(officialDocumentYear);
        String normalizedHierarchyPath = normalizeHierarchyPath(hierarchyPath);
        validateDisplayPriority(displayPriority);

        OfficialDocumentCategory category = officialDocumentCategoryRepository
            .findByTenantIdAndSlug(tenantId, normalizedSlug)
            .orElseThrow(() -> new BadRequestAlertException("Invalid category", "eventMedia", "invalidcategory"));

        List<EventMediaDTO> result = new ArrayList<>();

        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            if (file == null || file.isEmpty()) {
                throw new BadRequestAlertException("One or more files are empty", "eventMedia", "fileempty");
            }

            String originalFilename = file.getOriginalFilename();
            String title = titlePrefix;
            if (originalFilename != null && !originalFilename.isBlank()) {
                // Make title unique per file (helps UI display)
                title = titlePrefix + " - " + originalFilename;
            }

            String s3Url = s3Service.uploadTenantOfficialDocumentFile(file, tenantId, normalizedSlug, officialDocumentYear, title);

            EventMedia eventMedia = new EventMedia();
            eventMedia.setTenantId(tenantId);
            eventMedia.setTitle(title);
            eventMedia.setDescription(description);
            eventMedia.setEventMediaType("TENANT_OFFICIAL_DOCUMENT");
            eventMedia.setStorageType("S3");
            eventMedia.setFileUrl(s3Url);
            eventMedia.setPreSignedUrl(s3Service.generatePresignedUrl(s3Url, 1));
            eventMedia.setFileSize((int) file.getSize());
            if (file.getContentType() != null && !file.getContentType().isBlank()) {
                eventMedia.setContentType(file.getContentType());
            }
            eventMedia.setIsPublic(isPublic);

            eventMedia.setEventFlyer(false);
            eventMedia.setIsEventManagementOfficialDocument(true);

            // Non-event document defaults
            eventMedia.setIsHomePageHeroImage(false);
            eventMedia.setIsFeaturedEventImage(false);
            eventMedia.setIsLiveEventImage(false);
            eventMedia.setIsHeroImage(false);
            eventMedia.setIsActiveHeroImage(false);
            eventMedia.setIsFeaturedVideo(false);

            eventMedia.setStartDisplayingFromDate(LocalDate.now());
            eventMedia.setUploadedById(userProfileId);
            eventMedia.setPriorityRanking(0);

            // Official document metadata
            eventMedia.setOfficialDocumentCategoryId(category.getId());
            eventMedia.setOfficialDocumentYear(officialDocumentYear);
            eventMedia.setHierarchyPath(normalizedHierarchyPath);
            eventMedia.setHierarchyCategoryLabel(hierarchyCategoryLabel);
            eventMedia.setDisplayPriority(displayPriority);

            applyOptionalOfficialDocumentThumbnail(thumbnailFile, eventMedia, tenantId, normalizedSlug, officialDocumentYear, title);

            eventMedia.setCreatedAt(ZonedDateTime.now());
            eventMedia.setUpdatedAt(ZonedDateTime.now());

            eventMedia = eventMediaRepository.save(eventMedia);
            result.add(eventMediaMapper.toDto(eventMedia));
        }

        officialDocumentYearBundleService.ensureBundleForUpload(tenantId, category.getId(), officialDocumentYear);
        return result;
    }

    @Override
    public EventMediaDTO uploadOfficialDocumentThumbnail(Long mediaId, MultipartFile thumbnailFile, Long userProfileId) {
        if (mediaId == null) {
            throw new BadRequestAlertException("Media id is required", "eventMedia", "idrequired");
        }
        validateThumbnailFile(thumbnailFile);

        EventMedia eventMedia = eventMediaRepository
            .findById(mediaId)
            .orElseThrow(() -> new BadRequestAlertException("EventMedia not found", "eventMedia", "idnotfound"));

        if (!Boolean.TRUE.equals(eventMedia.getIsEventManagementOfficialDocument())) {
            throw new BadRequestAlertException("Not an official document", "eventMedia", "notofficialdocument");
        }

        String tenantId = eventMedia.getTenantId();
        Integer year = eventMedia.getOfficialDocumentYear();
        Long categoryId = eventMedia.getOfficialDocumentCategoryId();
        if (tenantId == null || tenantId.isBlank() || year == null || categoryId == null) {
            throw new BadRequestAlertException("Official document metadata incomplete", "eventMedia", "metadatarequired");
        }

        OfficialDocumentCategory category = officialDocumentCategoryRepository
            .findById(categoryId)
            .orElseThrow(() -> new BadRequestAlertException("Invalid category", "eventMedia", "invalidcategory"));

        String categorySlug = normalizeOfficialDocumentCategorySlug(category.getSlug());
        applyOptionalOfficialDocumentThumbnail(thumbnailFile, eventMedia, tenantId, categorySlug, year, eventMedia.getTitle());
        eventMedia.setUploadedById(userProfileId);
        eventMedia.setUpdatedAt(ZonedDateTime.now());
        eventMedia = eventMediaRepository.save(eventMedia);
        return eventMediaMapper.toDto(eventMedia);
    }

    private void applyOptionalOfficialDocumentThumbnail(
        MultipartFile thumbnailFile,
        EventMedia eventMedia,
        String tenantId,
        String categorySlug,
        Integer officialDocumentYear,
        String label
    ) {
        if (thumbnailFile == null || thumbnailFile.isEmpty()) {
            return;
        }
        validateThumbnailFile(thumbnailFile);
        String thumbUrl = s3Service.uploadTenantOfficialDocumentThumbnailFile(
            thumbnailFile,
            tenantId,
            categorySlug,
            officialDocumentYear,
            label
        );
        eventMedia.setThumbnailUrl(thumbUrl);
        eventMedia.setThumbnailPreSignedUrl(s3Service.generatePresignedUrl(thumbUrl, 1));
        eventMedia.setThumbnailPreSignedUrlExpiresAt(ZonedDateTime.now().plusHours(1));
    }

    private void validateThumbnailFile(MultipartFile thumbnailFile) {
        if (thumbnailFile == null || thumbnailFile.isEmpty()) {
            throw new BadRequestAlertException("Thumbnail file cannot be empty", "eventMedia", "thumbnailempty");
        }
        String contentType = thumbnailFile.getContentType();
        if (contentType == null || !contentType.toLowerCase().startsWith("image/")) {
            throw new BadRequestAlertException("Thumbnail must be an image", "eventMedia", "thumbnailnotimage");
        }
    }

    private String normalizeOfficialDocumentCategorySlug(String categorySlug) {
        // Basic protection against path traversal and unexpected characters.
        String normalized = categorySlug.trim().toLowerCase();
        if (normalized.contains("..") || normalized.contains("/") || normalized.contains("\\") || normalized.isBlank()) {
            throw new BadRequestAlertException("Invalid category slug", "eventMedia", "invalidcategory");
        }

        // Convert any non [a-z0-9-] chars to '-'
        normalized = normalized.replaceAll("[^a-z0-9-]", "-");
        normalized = normalized.replaceAll("-{2,}", "-");
        normalized = normalized.replaceAll("^-+", "").replaceAll("-+$", "");

        if (normalized.isBlank() || normalized.length() > 80 || !normalized.matches("^[a-z0-9-]+$")) {
            throw new BadRequestAlertException("Invalid category slug", "eventMedia", "invalidcategory");
        }

        return normalized;
    }

    private String normalizeHierarchyPath(String hierarchyPath) {
        if (hierarchyPath == null || hierarchyPath.isBlank()) {
            return null;
        }
        String normalized = hierarchyPath.trim().replace("/", "\\");
        if (normalized.contains("..")) {
            throw new BadRequestAlertException("Invalid hierarchy path", "eventMedia", "invalidhierarchypath");
        }
        return normalized;
    }

    private void validateDisplayPriority(Integer displayPriority) {
        if (displayPriority != null && displayPriority < 0) {
            throw new BadRequestAlertException("Display priority must be non-negative", "eventMedia", "invaliddisplaypriority");
        }
    }

    private void validateOfficialDocumentYear(Integer officialDocumentYear) {
        if (officialDocumentYear == null) {
            throw new BadRequestAlertException("Official document year is required", "eventMedia", "yeardownrequired");
        }
        if (officialDocumentYear < 1000 || officialDocumentYear > 9999) {
            throw new BadRequestAlertException("Invalid official document year", "eventMedia", "invalidyear");
        }
    }

    @Override
    public List<EventMediaDTO> getEventMediaWithUrls(Long eventId, Long userProfileId, boolean includePrivate) {
        List<EventMedia> mediaList = eventMediaRepository.findByEventId(eventId);
        List<EventMediaDTO> result = new ArrayList<>();
        for (EventMedia media : mediaList) {
            if (Boolean.TRUE.equals(media.getIsPublic()) || includePrivate) {
                EventMediaDTO dto = eventMediaMapper.toDto(media);
                // Set presigned viewing URL
                if (media.getFileUrl() != null && !media.getFileUrl().isEmpty()) {
                    String presignedUrl = s3Service.generatePresignedUrl(media.getFileUrl(), 2); // 2 hours default
                    dto.setFileUrl(presignedUrl);
                }
                result.add(dto);
            }
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventMediaDTO> findPublicOfficialDocumentsForDownloads(
        String tenantId,
        Long officialDocumentCategoryId,
        Integer officialDocumentYear,
        Pageable pageable
    ) {
        return eventMediaRepository
            .findPublicOfficialDocumentsForDownloads(tenantId, officialDocumentCategoryId, officialDocumentYear, pageable)
            .map(eventMediaMapper::toDto);
    }

    @Override
    public String getViewingUrl(Long mediaId, Long userProfileId) {
        Optional<EventMedia> mediaOpt = eventMediaRepository.findById(mediaId);
        if (mediaOpt.isPresent()) {
            EventMedia media = mediaOpt.orElseThrow(() -> new EntityNotFoundException("EventMedia not found"));
            if (media.getFileUrl() != null && !media.getFileUrl().isEmpty()) {
                return s3Service.generatePresignedUrl(media.getFileUrl(), 2); // 2 hours default
            }
        }
        return "";
    }

    /**
     * Get EventMedia without LOB fields to avoid LOB stream access issues
     */
    @Override
    @Transactional(readOnly = true)
    public List<EventMediaDTO> findAllWithoutLobFields() {
        log.debug("Request to get all EventMedia without LOB fields");
        List<Object[]> rawResults = eventMediaRepository.findAllWithoutLobFieldsRaw();

        return rawResults.stream().map(this::convertRawToEventMediaDTO).collect(Collectors.toList());
    }

    /**
     * Convert raw Object[] result to EventMediaDTO
     */
    private EventMediaDTO convertRawToEventMediaDTO(Object[] raw) {
        EventMediaDTO dto = new EventMediaDTO();

        // Map the raw results to DTO fields with proper type handling
        // Order: id, tenant_id, title, event_media_type, storage_type, file_url,
        // file_data_content_type,
        // content_type, file_size, is_public, event_flyer,
        // is_event_management_official_document,
        // pre_signed_url, pre_signed_url_expires_at, alt_text, display_order,
        // download_count,
        // is_featured_video, featured_video_url, is_hero_image,
        // is_active_hero_image, is_home_page_hero_image, is_featured_event_image,
        // is_live_event_image,
        // created_at, updated_at, event_id, uploaded_by_id, start_displaying_from

        dto.setId((Long) raw[0]);
        dto.setTenantId((String) raw[1]);
        dto.setTitle((String) raw[2]);
        dto.setEventMediaType((String) raw[3]);
        dto.setStorageType((String) raw[4]);
        dto.setFileUrl((String) raw[5]);
        // dto.setFileDataContentType((String) raw[6]);
        dto.setContentType((String) raw[7]);

        // Handle Integer fields that might come as Long from database
        if (raw[8] != null) {
            if (raw[8] instanceof Long) {
                dto.setFileSize(((Long) raw[8]).intValue());
            } else {
                dto.setFileSize((Integer) raw[8]);
            }
        }

        dto.setIsPublic((Boolean) raw[9]);
        dto.setEventFlyer((Boolean) raw[10]);
        dto.setIsEventManagementOfficialDocument((Boolean) raw[11]);
        dto.setPreSignedUrl((String) raw[12]);

        // Handle date/time fields that come as java.sql.Timestamp from database
        if (raw[13] != null) {
            if (raw[13] instanceof java.sql.Timestamp) {
                dto.setPreSignedUrlExpiresAt(((java.sql.Timestamp) raw[13]).toInstant().atZone(java.time.ZoneId.systemDefault()));
            } else {
                dto.setPreSignedUrlExpiresAt((ZonedDateTime) raw[13]);
            }
        }

        dto.setAltText((String) raw[14]);

        // Handle Integer fields that might come as Long from database
        if (raw[15] != null) {
            if (raw[15] instanceof Long) {
                dto.setDisplayOrder(((Long) raw[15]).intValue());
            } else {
                dto.setDisplayOrder((Integer) raw[15]);
            }
        }

        if (raw[16] != null) {
            if (raw[16] instanceof Long) {
                dto.setDownloadCount(((Long) raw[16]).intValue());
            } else {
                dto.setDownloadCount((Integer) raw[16]);
            }
        }

        dto.setIsFeaturedVideo((Boolean) raw[17]);
        dto.setFeaturedVideoUrl((String) raw[18]);
        dto.setIsHeroImage((Boolean) raw[19]);
        dto.setIsActiveHeroImage((Boolean) raw[20]);
        dto.setIsHomePageHeroImage((Boolean) raw[21]);
        dto.setIsFeaturedEventImage((Boolean) raw[22]);
        dto.setIsLiveEventImage((Boolean) raw[23]);

        // Handle date/time fields that come as java.sql.Timestamp from database
        if (raw[24] != null) {
            if (raw[24] instanceof java.sql.Timestamp) {
                dto.setCreatedAt(((java.sql.Timestamp) raw[24]).toInstant().atZone(java.time.ZoneId.systemDefault()));
            } else {
                dto.setCreatedAt((ZonedDateTime) raw[24]);
            }
        }

        if (raw[25] != null) {
            if (raw[25] instanceof java.sql.Timestamp) {
                dto.setUpdatedAt(((java.sql.Timestamp) raw[25]).toInstant().atZone(java.time.ZoneId.systemDefault()));
            } else {
                dto.setUpdatedAt((ZonedDateTime) raw[25]);
            }
        }

        dto.setEventId((Long) raw[26]);
        dto.setUploadedById((Long) raw[27]);

        // Handle LocalDate field that might come as java.sql.Date from database
        if (raw[28] != null) {
            if (raw[28] instanceof java.sql.Date) {
                dto.setStartDisplayingFromDate(((java.sql.Date) raw[28]).toLocalDate());
            } else {
                dto.setStartDisplayingFromDate((java.time.LocalDate) raw[28]);
            }
        }

        // Handle new fields (sponsorId, eventSponsorsJoinId, priorityRanking)
        if (raw.length > 29 && raw[29] != null) {
            dto.setSponsorId((Long) raw[29]);
        }
        if (raw.length > 30 && raw[30] != null) {
            dto.setEventSponsorsJoinId((Long) raw[30]);
        }
        if (raw.length > 31 && raw[31] != null) {
            dto.setPriorityRanking((Integer) raw[31]);
        }

        // Official document metadata (added for tenant official documents)
        if (raw.length > 32 && raw[32] != null) {
            if (raw[32] instanceof Long) {
                dto.setOfficialDocumentCategoryId((Long) raw[32]);
            } else if (raw[32] instanceof Number) {
                dto.setOfficialDocumentCategoryId(((Number) raw[32]).longValue());
            }
        }
        if (raw.length > 33 && raw[33] != null) {
            if (raw[33] instanceof Integer) {
                dto.setOfficialDocumentYear((Integer) raw[33]);
            } else if (raw[33] instanceof Number) {
                dto.setOfficialDocumentYear(((Number) raw[33]).intValue());
            }
        }
        if (raw.length > 34 && raw[34] != null) {
            dto.setHierarchyPath((String) raw[34]);
        }
        if (raw.length > 35 && raw[35] != null) {
            dto.setHierarchyCategoryLabel((String) raw[35]);
        }
        if (raw.length > 36 && raw[36] != null) {
            if (raw[36] instanceof Integer) {
                dto.setDisplayPriority((Integer) raw[36]);
            } else if (raw[36] instanceof Number) {
                dto.setDisplayPriority(((Number) raw[36]).intValue());
            }
        }

        return dto;
    }

    @Override
    public EventMediaDTO uploadSponsorImage(Long sponsorId, Long eventId, String imageType, MultipartFile file, String tenantId) {
        log.debug("Request to upload sponsor image: sponsorId={}, eventId={}, imageType={}", sponsorId, eventId, imageType);

        // Normalize imageType: convert LOGO_IMAGE -> logo, HERO_IMAGE -> hero, BANNER_IMAGE -> banner
        String normalizedImageType = normalizeSponsorImageType(imageType);

        // 1. Generate S3 path (pass imageType to use it in filename: logo_xxx.jpg instead of email_header_image_xxx.jpg)
        String s3Path = s3Service.generateSponsorImagePath(tenantId, sponsorId, file.getOriginalFilename(), imageType);

        // 2. Upload to S3
        String s3Url = s3Service.uploadFile(s3Path, file);

        // 3. Create EventMedia record
        EventMediaDTO mediaDTO = new EventMediaDTO();
        mediaDTO.setSponsorId(sponsorId);
        mediaDTO.setEventId(eventId != null && eventId > 0 ? eventId : null);
        mediaDTO.setTitle(normalizedImageType + " - " + sponsorId);
        mediaDTO.setDescription("Sponsor " + normalizedImageType + " image");
        mediaDTO.setFileUrl(s3Url);
        mediaDTO.setEventMediaType("SPONSOR_" + normalizedImageType.toUpperCase());
        mediaDTO.setStorageType("S3");
        mediaDTO.setIsPublic(true);
        mediaDTO.setTenantId(tenantId);
        mediaDTO.setPriorityRanking(0); // Default to highest priority
        mediaDTO.setIsHomePageHeroImage(false);
        mediaDTO.setIsFeaturedEventImage(false);
        mediaDTO.setIsLiveEventImage(false);
        mediaDTO.setCreatedAt(ZonedDateTime.now());
        mediaDTO.setUpdatedAt(ZonedDateTime.now());

        EventMediaDTO savedMedia = save(mediaDTO);

        // 4. Update event_sponsors table
        EventSponsors sponsor = eventSponsorsRepository
            .findById(sponsorId)
            .orElseThrow(() -> new RuntimeException("Sponsor not found: " + sponsorId));

        switch (normalizedImageType) {
            case "logo":
                sponsor.setLogoUrl(s3Url);
                break;
            case "hero":
                sponsor.setHeroImageUrl(s3Url);
                break;
            case "banner":
                sponsor.setBannerImageUrl(s3Url);
                break;
            default:
                throw new IllegalArgumentException("Invalid image type: " + imageType);
        }
        eventSponsorsRepository.save(sponsor);

        return savedMedia;
    }

    /**
     * Helper method to normalize sponsor image type.
     * Converts LOGO_IMAGE -> logo, HERO_IMAGE -> hero, BANNER_IMAGE -> banner
     * Also handles lowercase versions for backward compatibility.
     */
    private String normalizeSponsorImageType(String imageType) {
        if (imageType == null) {
            return imageType;
        }
        String upper = imageType.toUpperCase();
        return switch (upper) {
            case "LOGO_IMAGE" -> "logo";
            case "HERO_IMAGE" -> "hero";
            case "BANNER_IMAGE" -> "banner";
            default -> imageType.toLowerCase(); // Return lowercase for backward compatibility
        };
    }

    @Override
    public EventMediaDTO uploadEventSponsorJoinPoster(Long eventId, Long sponsorId, MultipartFile file, String tenantId) {
        log.debug("Request to upload event-sponsor join poster: eventId={}, sponsorId={}", eventId, sponsorId);

        // 1. Find or create event_sponsors_join record
        EventSponsorsJoin joinRecord = eventSponsorsJoinRepository
            .findByEventIdAndSponsorId(eventId, sponsorId)
            .orElseThrow(() -> new RuntimeException("Event-sponsor association not found: eventId=" + eventId + ", sponsorId=" + sponsorId)
            );

        // 2. Generate S3 path
        String s3Path = s3Service.generateEventSponsorJoinImagePath(tenantId, eventId, sponsorId, file.getOriginalFilename());

        // 3. Upload to S3
        String s3Url = s3Service.uploadFile(s3Path, file);

        // 4. Create EventMedia record
        EventMediaDTO mediaDTO = new EventMediaDTO();
        mediaDTO.setEventSponsorsJoinId(joinRecord.getId());
        mediaDTO.setEventId(eventId);
        mediaDTO.setSponsorId(sponsorId);
        mediaDTO.setTitle("Custom Poster - Event " + eventId + " - Sponsor " + sponsorId);
        mediaDTO.setDescription("Custom poster for event-sponsor combination");
        mediaDTO.setFileUrl(s3Url);
        mediaDTO.setEventMediaType("EVENT_SPONSOR_POSTER");
        mediaDTO.setStorageType("S3");
        mediaDTO.setIsPublic(true);
        mediaDTO.setTenantId(tenantId);
        mediaDTO.setPriorityRanking(0); // Default to highest priority
        mediaDTO.setIsHomePageHeroImage(false);
        mediaDTO.setIsFeaturedEventImage(false);
        mediaDTO.setIsLiveEventImage(false);
        mediaDTO.setCreatedAt(ZonedDateTime.now());
        mediaDTO.setUpdatedAt(ZonedDateTime.now());

        EventMediaDTO savedMedia = save(mediaDTO);

        // 5. Update event_sponsors_join table
        joinRecord.setCustomPosterUrl(s3Url);
        eventSponsorsJoinRepository.save(joinRecord);

        return savedMedia;
    }

    @Override
    public EventMediaDTO uploadSponsorMedia(
        Long sponsorId,
        MultipartFile file,
        String title,
        String description,
        String tenantId,
        Integer priorityRanking
    ) {
        log.debug("Request to upload sponsor media: sponsorId={}", sponsorId);

        // 1. Generate S3 path
        String s3Path = s3Service.generateSponsorImagePath(tenantId, sponsorId, file.getOriginalFilename());

        // 2. Upload to S3
        String s3Url = s3Service.uploadFile(s3Path, file);

        // 3. Create EventMedia record
        EventMediaDTO mediaDTO = new EventMediaDTO();
        mediaDTO.setSponsorId(sponsorId);
        mediaDTO.setTitle(title != null ? title : "Sponsor Media");
        mediaDTO.setDescription(description != null ? description : "Sponsor media file");
        mediaDTO.setFileUrl(s3Url);
        mediaDTO.setEventMediaType("SPONSOR_MEDIA");
        mediaDTO.setStorageType("S3");
        mediaDTO.setIsPublic(true);
        mediaDTO.setTenantId(tenantId);
        mediaDTO.setPriorityRanking(priorityRanking != null ? priorityRanking : 0); // Default to highest priority
        mediaDTO.setIsHomePageHeroImage(false);
        mediaDTO.setIsFeaturedEventImage(false);
        mediaDTO.setIsLiveEventImage(false);
        mediaDTO.setCreatedAt(ZonedDateTime.now());
        mediaDTO.setUpdatedAt(ZonedDateTime.now());

        return save(mediaDTO);
    }

    @Override
    public EventMediaDTO uploadEventSponsorMedia(
        Long eventId,
        Long sponsorId,
        MultipartFile file,
        String title,
        String description,
        String tenantId,
        Integer priorityRanking
    ) {
        log.debug("Request to upload event-sponsor media: eventId={}, sponsorId={}", eventId, sponsorId);

        // 1. Find event_sponsors_join record
        EventSponsorsJoin joinRecord = eventSponsorsJoinRepository
            .findByEventIdAndSponsorId(eventId, sponsorId)
            .orElseThrow(() -> new RuntimeException("Event-sponsor association not found: eventId=" + eventId + ", sponsorId=" + sponsorId)
            );

        // 2. Generate S3 path
        String s3Path = s3Service.generateEventSponsorJoinImagePath(tenantId, eventId, sponsorId, file.getOriginalFilename());

        // 3. Upload to S3
        String s3Url = s3Service.uploadFile(s3Path, file);

        // 4. Create EventMedia record
        EventMediaDTO mediaDTO = new EventMediaDTO();
        mediaDTO.setEventSponsorsJoinId(joinRecord.getId());
        mediaDTO.setEventId(eventId);
        mediaDTO.setSponsorId(sponsorId);
        mediaDTO.setTitle(title != null ? title : "Event-Sponsor Media");
        mediaDTO.setDescription(description != null ? description : "Event-sponsor media file");
        mediaDTO.setFileUrl(s3Url);
        mediaDTO.setEventMediaType("EVENT_SPONSOR_MEDIA");
        mediaDTO.setStorageType("S3");
        mediaDTO.setIsPublic(true);
        mediaDTO.setTenantId(tenantId);
        mediaDTO.setPriorityRanking(priorityRanking != null ? priorityRanking : 0); // Default to highest priority
        mediaDTO.setIsHomePageHeroImage(false);
        mediaDTO.setIsFeaturedEventImage(false);
        mediaDTO.setIsLiveEventImage(false);
        mediaDTO.setCreatedAt(ZonedDateTime.now());
        mediaDTO.setUpdatedAt(ZonedDateTime.now());

        return save(mediaDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventMediaDTO> findBySponsorId(Long sponsorId, String tenantId) {
        log.debug("Request to get sponsor media: sponsorId={}, tenantId={}", sponsorId, tenantId);
        List<EventMedia> media;
        if (tenantId != null && !tenantId.isEmpty()) {
            media = eventMediaRepository.findBySponsorIdAndTenantId(sponsorId, tenantId);
        } else {
            media = eventMediaRepository.findBySponsorId(sponsorId);
        }
        return media.stream().map(eventMediaMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventMediaDTO> findByEventSponsorsJoinId(Long eventSponsorsJoinId, String tenantId) {
        log.debug("Request to get event-sponsor media: eventSponsorsJoinId={}, tenantId={}", eventSponsorsJoinId, tenantId);
        List<EventMedia> media;
        if (tenantId != null && !tenantId.isEmpty()) {
            media = eventMediaRepository.findByEventSponsorsJoinIdAndTenantId(eventSponsorsJoinId, tenantId);
        } else {
            media = eventMediaRepository.findByEventSponsorsJoinId(eventSponsorsJoinId);
        }
        return media.stream().map(eventMediaMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public EventMediaDTO updateMediaPriorityRanking(Long mediaId, Integer priorityRanking) {
        log.debug("Request to update media priority ranking: mediaId={}, priorityRanking={}", mediaId, priorityRanking);
        EventMedia media = eventMediaRepository.findById(mediaId).orElseThrow(() -> new RuntimeException("Media not found: " + mediaId));

        media.setPriorityRanking(priorityRanking);
        EventMedia updated = eventMediaRepository.save(media);
        return eventMediaMapper.toDto(updated);
    }

    @Override
    public EventMediaDTO uploadEmailHeaderImage(
        Long eventId,
        MultipartFile file,
        String tenantId,
        String title,
        String description,
        Boolean isPublic
    ) {
        log.debug("Request to upload email header image: eventId={}, tenantId={}", eventId, tenantId);

        // 1. Validate event exists
        EventDetails event = eventRepository
            .findById(eventId)
            .orElseThrow(() -> new EntityNotFoundException("Event not found: " + eventId));

        // 2. Validate file
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }

        // Validate file type (images only)
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("File must be an image");
        }

        // Validate file size (max 10MB)
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("File size must be less than 10MB");
        }

        // 3. Generate S3 path
        String s3Path = s3Service.generateEmailHeaderImagePath(tenantId, eventId, file.getOriginalFilename());

        // 4. Upload to S3
        String s3Url = s3Service.uploadFile(s3Path, file);
        log.debug("Uploaded email header image to S3: {}", s3Url);

        // 5. Create EventMedia record for audit/tracking
        EventMediaDTO mediaDTO = new EventMediaDTO();
        mediaDTO.setEventId(eventId);
        mediaDTO.setTenantId(tenantId);
        mediaDTO.setTitle(title != null && !title.isEmpty() ? title : "Email Header Image");
        mediaDTO.setDescription(
            description != null && !description.isEmpty() ? description : "Email header image for ticket confirmation emails"
        );
        mediaDTO.setFileUrl(s3Url);
        mediaDTO.setEventMediaType("EMAIL_HEADER_IMAGE");
        mediaDTO.setStorageType("S3");
        mediaDTO.setIsPublic(isPublic != null ? isPublic : true);
        mediaDTO.setPriorityRanking(0);
        mediaDTO.setIsHomePageHeroImage(false);
        mediaDTO.setIsFeaturedEventImage(false);
        mediaDTO.setIsLiveEventImage(false);
        mediaDTO.setIsEmailHeaderImage(true);
        mediaDTO.setStartDisplayingFromDate(LocalDate.now());
        mediaDTO.setCreatedAt(ZonedDateTime.now());
        mediaDTO.setUpdatedAt(ZonedDateTime.now());

        EventMediaDTO savedMedia = save(mediaDTO);
        log.debug("Created EventMedia record for email header image: {}", savedMedia.getId());

        // 6. Update event_details table with email header image URL using EventDetailsService
        try {
            EventDetailsDTO eventDetailsDTO = eventDetailsService
                .findOne(eventId)
                .orElseThrow(() -> new EntityNotFoundException("EventDetails not found: " + eventId));

            eventDetailsDTO.setEmailHeaderImageUrl(s3Url);
            eventDetailsService.update(eventDetailsDTO);
            log.info("Updated event {} with email header image URL via EventDetailsService: {}", eventId, s3Url);
        } catch (Exception e) {
            log.error("Failed to update EventDetailsDTO with email header image URL for event {}: {}", eventId, e.getMessage(), e);
            // Fallback to direct entity update if service update fails
            event.setEmailHeaderImageUrl(s3Url);
            eventRepository.save(event);
            log.warn("Updated event {} with email header image URL via direct entity update (fallback)", eventId);
        }

        return savedMedia;
    }

    @Override
    public EventMediaDTO uploadPromotionalEmailHeaderImage(
        Long eventId,
        Long promotionId,
        MultipartFile file,
        String tenantId,
        String title,
        String description,
        Boolean isPublic
    ) {
        log.debug(
            "Request to upload promotional email header image: eventId={}, promotionId={}, tenantId={}",
            eventId,
            promotionId,
            tenantId
        );

        // 1. Validate event exists
        EventDetails event = eventRepository
            .findById(eventId)
            .orElseThrow(() -> new EntityNotFoundException("Event not found: " + eventId));

        // 2. Validate promotion template exists
        PromotionEmailTemplate promotionTemplate = promotionEmailTemplateRepository
            .findById(promotionId)
            .orElseThrow(() -> new EntityNotFoundException("Promotion email template not found: " + promotionId));

        // Validate template belongs to event and tenant
        if (!promotionTemplate.getEventId().equals(eventId)) {
            throw new IllegalArgumentException("Promotion template does not belong to the specified event");
        }
        if (!promotionTemplate.getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException("Promotion template does not belong to the specified tenant");
        }

        // 3. Validate file
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }

        // Validate file type (images only)
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("File must be an image");
        }

        // Validate file size (max 10MB)
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("File size must be less than 10MB");
        }

        // 4. Generate S3 path
        String s3Path = s3Service.generatePromotionalEmailHeaderImagePath(tenantId, eventId, promotionId, file.getOriginalFilename());

        // 5. Upload to S3
        String s3Url = s3Service.uploadFile(s3Path, file);
        log.debug("Uploaded promotional email header image to S3: {}", s3Url);

        // 6. Create EventMedia record for audit/tracking
        EventMediaDTO mediaDTO = new EventMediaDTO();
        mediaDTO.setEventId(eventId);
        mediaDTO.setTenantId(tenantId);
        mediaDTO.setTitle(title != null && !title.isEmpty() ? title : "Promotional Email Header Image");
        mediaDTO.setDescription(
            description != null && !description.isEmpty()
                ? description
                : "Promotional email header image for template: " + promotionTemplate.getTemplateName()
        );
        mediaDTO.setFileUrl(s3Url);
        mediaDTO.setEventMediaType("PROMOTIONAL_EMAIL_HEADER_IMAGE");
        mediaDTO.setStorageType("S3");
        mediaDTO.setIsPublic(isPublic != null ? isPublic : true);
        mediaDTO.setPriorityRanking(0);
        mediaDTO.setIsHomePageHeroImage(false);
        mediaDTO.setIsFeaturedEventImage(false);
        mediaDTO.setIsLiveEventImage(false);
        mediaDTO.setIsEmailHeaderImage(false);
        mediaDTO.setStartDisplayingFromDate(LocalDate.now());
        mediaDTO.setCreatedAt(ZonedDateTime.now());
        mediaDTO.setUpdatedAt(ZonedDateTime.now());

        EventMediaDTO savedMedia = save(mediaDTO);
        log.debug("Created EventMedia record for promotional email header image: {}", savedMedia.getId());

        // 7. Update promotion_email_template table with header image URL using PromotionEmailTemplateService
        try {
            PromotionEmailTemplateDTO templateDTO = promotionEmailTemplateMapper.toDto(promotionTemplate);
            templateDTO.setHeaderImageUrl(s3Url);
            promotionEmailTemplateService.partialUpdate(promotionId, templateDTO);
            log.info("Updated promotion template {} with header image URL via PromotionEmailTemplateService: {}", promotionId, s3Url);
        } catch (Exception e) {
            log.error(
                "Failed to update PromotionEmailTemplateDTO with header image URL for template {}: {}",
                promotionId,
                e.getMessage(),
                e
            );
            // Fallback to direct entity update if service update fails
            try {
                promotionTemplate.setHeaderImageUrl(s3Url);
                promotionTemplate.setUpdatedAt(ZonedDateTime.now());
                promotionEmailTemplateRepository.save(promotionTemplate);
                log.warn("Updated promotion template {} with header image URL via direct entity update (fallback)", promotionId);
            } catch (Exception fallbackException) {
                log.error(
                    "Failed to update PromotionEmailTemplate with header image URL via fallback for template {}: {}",
                    promotionId,
                    fallbackException.getMessage(),
                    fallbackException
                );
                throw new RuntimeException("Failed to update promotion template with header image URL", fallbackException);
            }
        }

        return savedMedia;
    }

    @Override
    public EventMediaDTO uploadPromotionalEmailFooterImage(
        Long eventId,
        Long promotionId,
        MultipartFile file,
        String tenantId,
        String title,
        String description,
        Boolean isPublic
    ) {
        log.debug(
            "Request to upload promotional email footer image: eventId={}, promotionId={}, tenantId={}",
            eventId,
            promotionId,
            tenantId
        );

        // 1. Validate event exists
        EventDetails event = eventRepository
            .findById(eventId)
            .orElseThrow(() -> new EntityNotFoundException("Event not found: " + eventId));

        // 2. Validate promotion template exists
        PromotionEmailTemplate promotionTemplate = promotionEmailTemplateRepository
            .findById(promotionId)
            .orElseThrow(() -> new EntityNotFoundException("Promotion email template not found: " + promotionId));

        // Validate template belongs to event and tenant
        if (!promotionTemplate.getEventId().equals(eventId)) {
            throw new IllegalArgumentException("Promotion template does not belong to the specified event");
        }
        if (!promotionTemplate.getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException("Promotion template does not belong to the specified tenant");
        }

        // 3. Validate file
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }

        // Validate file type (images only)
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("File must be an image");
        }

        // Validate file size (max 10MB)
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("File size must be less than 10MB");
        }

        // 4. Generate S3 path
        String s3Path = s3Service.generatePromotionalEmailFooterImagePath(tenantId, eventId, promotionId, file.getOriginalFilename());

        // 5. Upload to S3
        String s3Url = s3Service.uploadFile(s3Path, file);
        log.debug("Uploaded promotional email footer image to S3: {}", s3Url);

        // 6. Create EventMedia record for audit/tracking
        EventMediaDTO mediaDTO = new EventMediaDTO();
        mediaDTO.setEventId(eventId);
        mediaDTO.setTenantId(tenantId);
        mediaDTO.setTitle(title != null && !title.isEmpty() ? title : "Promotional Email Footer Image");
        mediaDTO.setDescription(
            description != null && !description.isEmpty()
                ? description
                : "Promotional email footer image for template: " + promotionTemplate.getTemplateName()
        );
        mediaDTO.setFileUrl(s3Url);
        mediaDTO.setEventMediaType("PROMOTIONAL_EMAIL_FOOTER_IMAGE");
        mediaDTO.setStorageType("S3");
        mediaDTO.setIsPublic(isPublic != null ? isPublic : true);
        mediaDTO.setPriorityRanking(0);
        mediaDTO.setIsHomePageHeroImage(false);
        mediaDTO.setIsFeaturedEventImage(false);
        mediaDTO.setIsLiveEventImage(false);
        mediaDTO.setIsEmailHeaderImage(false);
        mediaDTO.setStartDisplayingFromDate(LocalDate.now());
        mediaDTO.setCreatedAt(ZonedDateTime.now());
        mediaDTO.setUpdatedAt(ZonedDateTime.now());

        EventMediaDTO savedMedia = save(mediaDTO);
        log.debug("Created EventMedia record for promotional email footer image: {}", savedMedia.getId());

        // 7. Update promotion_email_template table with footer image URL using PromotionEmailTemplateService
        try {
            PromotionEmailTemplateDTO templateDTO = promotionEmailTemplateMapper.toDto(promotionTemplate);
            templateDTO.setFooterImageUrl(s3Url);
            promotionEmailTemplateService.partialUpdate(promotionId, templateDTO);
            log.info("Updated promotion template {} with footer image URL via PromotionEmailTemplateService: {}", promotionId, s3Url);
        } catch (Exception e) {
            log.error(
                "Failed to update PromotionEmailTemplateDTO with footer image URL for template {}: {}",
                promotionId,
                e.getMessage(),
                e
            );
            // Fallback to direct entity update if service update fails
            try {
                promotionTemplate.setFooterImageUrl(s3Url);
                promotionTemplate.setUpdatedAt(ZonedDateTime.now());
                promotionEmailTemplateRepository.save(promotionTemplate);
                log.warn("Updated promotion template {} with footer image URL via direct entity update (fallback)", promotionId);
            } catch (Exception fallbackException) {
                log.error(
                    "Failed to update PromotionEmailTemplate with footer image URL via fallback for template {}: {}",
                    promotionId,
                    fallbackException.getMessage(),
                    fallbackException
                );
                throw new RuntimeException("Failed to update promotion template with footer image URL", fallbackException);
            }
        }

        return savedMedia;
    }

    @Override
    public EventMediaDTO uploadFocusGroupCoverImage(
        Long focusGroupId,
        MultipartFile file,
        String tenantId,
        String title,
        String description,
        Boolean isPublic
    ) {
        log.debug("Request to upload focus group cover image: focusGroupId={}, tenantId={}", focusGroupId, tenantId);

        // 1. Validate focus group exists
        FocusGroupDTO focusGroup = focusGroupService
            .findOne(focusGroupId)
            .orElseThrow(() -> new EntityNotFoundException("Focus group not found: " + focusGroupId));

        // Validate focus group belongs to tenant
        if (!focusGroup.getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException("Focus group does not belong to the specified tenant");
        }

        // 2. Validate file
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }

        // Validate file type (images only)
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("File must be an image");
        }

        // Validate file size (max 10MB)
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("File size must be less than 10MB");
        }

        // 3. Generate S3 path
        String s3Path = s3Service.generateFocusGroupCoverImagePath(tenantId, focusGroupId, file.getOriginalFilename());

        // 4. Upload to S3
        String s3Url = s3Service.uploadFile(s3Path, file);
        log.debug("Uploaded focus group cover image to S3: {}", s3Url);

        // 5. Create EventMedia record for audit/tracking
        EventMediaDTO mediaDTO = new EventMediaDTO();
        mediaDTO.setEventId(null); // Focus group cover image is not event-specific
        mediaDTO.setTenantId(tenantId);
        mediaDTO.setTitle(title != null && !title.isEmpty() ? title : "Focus Group Cover Image");
        mediaDTO.setDescription(
            description != null && !description.isEmpty() ? description : "Cover image for focus group: " + focusGroup.getName()
        );
        mediaDTO.setFileUrl(s3Url);
        mediaDTO.setEventMediaType("FOCUS_GROUP_COVER_IMAGE");
        mediaDTO.setStorageType("S3");
        mediaDTO.setIsPublic(isPublic != null ? isPublic : true);
        mediaDTO.setPriorityRanking(0);
        mediaDTO.setIsHomePageHeroImage(false);
        mediaDTO.setIsFeaturedEventImage(false);
        mediaDTO.setIsLiveEventImage(false);
        mediaDTO.setIsEmailHeaderImage(false);
        mediaDTO.setStartDisplayingFromDate(LocalDate.now());
        mediaDTO.setCreatedAt(ZonedDateTime.now());
        mediaDTO.setUpdatedAt(ZonedDateTime.now());

        EventMediaDTO savedMedia = save(mediaDTO);
        log.debug("Created EventMedia record for focus group cover image: {}", savedMedia.getId());

        // 6. Update focus_group table with cover image URL using FocusGroupService
        try {
            FocusGroupDTO focusGroupDTO = focusGroupService
                .findOne(focusGroupId)
                .orElseThrow(() -> new EntityNotFoundException("FocusGroup not found: " + focusGroupId));

            focusGroupDTO.setCoverImageUrl(s3Url);
            focusGroupService.update(focusGroupDTO);
            log.info("Updated focus group {} with cover image URL via FocusGroupService: {}", focusGroupId, s3Url);
        } catch (Exception e) {
            log.error("Failed to update FocusGroupDTO with cover image URL for focus group {}: {}", focusGroupId, e.getMessage(), e);
            throw new RuntimeException("Failed to update focus group with cover image URL", e);
        }

        return savedMedia;
    }

    @Override
    public EventMediaDTO uploadGalleryAlbumCoverImage(
        Long albumId,
        MultipartFile file,
        String tenantId,
        String title,
        String description,
        Boolean isPublic
    ) {
        log.debug("Request to upload gallery album cover image: albumId={}, tenantId={}", albumId, tenantId);

        if (albumId == null) {
            throw new IllegalArgumentException("Missing required parameter: albumId");
        }
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("Missing required parameter: tenantId");
        }

        GalleryAlbum album = galleryAlbumRepository
            .findById(albumId)
            .orElseThrow(() -> new EntityNotFoundException("Gallery album not found: " + albumId));

        if (!tenantId.equals(album.getTenantId())) {
            throw new AccessDeniedException("Tenant does not match album");
        }

        validateGalleryCoverImageFile(file);

        String s3Path = s3Service.generateGalleryAlbumCoverPath(tenantId, albumId, file.getOriginalFilename());
        String s3Url = s3Service.uploadFile(s3Path, file);
        log.debug("Uploaded gallery album cover image to S3: {}", s3Url);

        EventMediaDTO mediaDTO = new EventMediaDTO();
        mediaDTO.setEventId(null);
        mediaDTO.setAlbumId(albumId);
        mediaDTO.setTenantId(tenantId);
        mediaDTO.setTitle(title != null && !title.isEmpty() ? title : "Gallery Album Cover Image");
        mediaDTO.setDescription(description != null && !description.isEmpty() ? description : "Cover image for gallery album");
        mediaDTO.setFileUrl(s3Url);
        mediaDTO.setEventMediaType("GALLERY_COVER");
        mediaDTO.setStorageType("S3");
        mediaDTO.setIsPublic(isPublic != null ? isPublic : true);
        mediaDTO.setPriorityRanking(0);
        mediaDTO.setIsHomePageHeroImage(false);
        mediaDTO.setIsFeaturedEventImage(false);
        mediaDTO.setIsLiveEventImage(false);
        mediaDTO.setIsEmailHeaderImage(false);
        mediaDTO.setStartDisplayingFromDate(LocalDate.now());
        mediaDTO.setCreatedAt(ZonedDateTime.now());
        mediaDTO.setUpdatedAt(ZonedDateTime.now());

        EventMediaDTO savedMedia = save(mediaDTO);
        log.debug("Created EventMedia record for gallery album cover image: {}", savedMedia.getId());

        galleryAlbumService.updateCoverImageUrl(albumId, s3Url);
        log.info("Updated gallery album {} with cover image URL: {}", albumId, s3Url);

        return savedMedia;
    }

    private void validateGalleryCoverImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }

        String contentType = file.getContentType();
        if (
            contentType == null ||
            (!contentType.equals("image/jpeg") &&
                !contentType.equals("image/png") &&
                !contentType.equals("image/webp") &&
                !contentType.equals("image/gif"))
        ) {
            throw new IllegalArgumentException("Invalid file type");
        }

        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("File too large");
        }
    }

    /**
     * Replicate homepage hero image from parent event to all child events in the recurrence series.
     * Only creates media records for child events that don't already have a homepage hero image.
     *
     * @param parentMedia the parent event's media record
     * @param parentEvent the parent event details
     */
    private void replicateHomePageHeroImageToChildren(EventMedia parentMedia, EventDetails parentEvent) {
        try {
            // Fetch all child events in the recurrence series
            // Use parentEventId to find direct children
            List<EventDetails> childEvents = eventRepository.findByParentEventId(parentEvent.getId());

            if (childEvents.isEmpty()) {
                log.debug("No child events found for parent event: {} (this is expected if event has no children)", parentEvent.getId());
                return;
            }

            log.info("Replicating homepage hero image from parent event {} to {} child events", parentEvent.getId(), childEvents.size());

            int createdCount = 0;
            int skippedCount = 0;
            int errorCount = 0;

            for (EventDetails childEvent : childEvents) {
                try {
                    Long childId = childEvent.getId();

                    // Check if child event already has a homepage hero image
                    List<EventMedia> existingHomePageHeroImages = eventMediaRepository.findByEventIdAndIsHomePageHeroImageTrue(childId);

                    if (!existingHomePageHeroImages.isEmpty()) {
                        log.debug(
                            "Child event {} already has {} homepage hero image(s), skipping replication",
                            childId,
                            existingHomePageHeroImages.size()
                        );
                        skippedCount++;
                        continue; // Skip this child - preserve existing homepage hero image
                    }

                    // Child doesn't have homepage hero image - create one
                    EventMedia childMedia = new EventMedia();

                    // Copy all fields from parent media
                    childMedia.setTitle(parentMedia.getTitle());
                    childMedia.setDescription(parentMedia.getDescription());
                    childMedia.setEventMediaType(parentMedia.getEventMediaType());
                    childMedia.setStorageType(parentMedia.getStorageType());
                    childMedia.setFileUrl(parentMedia.getFileUrl()); // Same URL
                    childMedia.setContentType(parentMedia.getContentType());
                    childMedia.setFileSize(parentMedia.getFileSize());
                    childMedia.setIsPublic(parentMedia.getIsPublic());
                    childMedia.setEventFlyer(parentMedia.getEventFlyer());
                    childMedia.setIsEventManagementOfficialDocument(parentMedia.getIsEventManagementOfficialDocument());
                    childMedia.setAltText(parentMedia.getAltText());
                    childMedia.setDisplayOrder(parentMedia.getDisplayOrder());
                    childMedia.setIsFeaturedVideo(parentMedia.getIsFeaturedVideo());
                    childMedia.setFeaturedVideoUrl(parentMedia.getFeaturedVideoUrl());
                    childMedia.setIsHeroImage(parentMedia.getIsHeroImage());
                    childMedia.setIsActiveHeroImage(parentMedia.getIsActiveHeroImage());
                    childMedia.setIsHomePageHeroImage(true); // Explicitly set to true
                    childMedia.setIsFeaturedEventImage(parentMedia.getIsFeaturedEventImage());
                    childMedia.setIsLiveEventImage(parentMedia.getIsLiveEventImage());
                    childMedia.setStartDisplayingFromDate(parentMedia.getStartDisplayingFromDate());
                    childMedia.setPreSignedUrl(parentMedia.getPreSignedUrl());
                    childMedia.setPreSignedUrlExpiresAt(parentMedia.getPreSignedUrlExpiresAt());
                    childMedia.setPriorityRanking(parentMedia.getPriorityRanking() != null ? parentMedia.getPriorityRanking() : 0);

                    // Set child-specific fields
                    childMedia.setEventId(childId); // Child event ID
                    childMedia.setUploadedById(parentMedia.getUploadedById()); // Same uploader
                    childMedia.setTenantId(parentMedia.getTenantId()); // Same tenant

                    // Set timestamps
                    childMedia.setCreatedAt(ZonedDateTime.now());
                    childMedia.setUpdatedAt(ZonedDateTime.now());

                    // Save child media record
                    eventMediaRepository.save(childMedia);
                    createdCount++;

                    log.debug("Created homepage hero image media record for child event {}", childId);
                } catch (Exception e) {
                    errorCount++;
                    log.error("Failed to replicate homepage hero image to child event {}", childEvent.getId(), e);
                    // Continue with next child - don't fail entire replication
                }
            }

            log.info(
                "Completed homepage hero image replication for parent event {}: {} created, {} skipped (already had image), {} errors",
                parentEvent.getId(),
                createdCount,
                skippedCount,
                errorCount
            );
        } catch (Exception e) {
            log.error("Failed to replicate homepage hero image to child events for parent event {}", parentEvent.getId(), e);
            // Don't throw - parent media upload should still succeed even if replication fails
        }
    }

    /**
     * Validates that when event_focus_group_id is set, the referenced focus group belongs to the same event as the media.
     *
     * @param eventFocusGroupId the event focus group ID (can be null; validation skipped if null)
     * @param mediaEventId the event ID of the media (must match the focus group's event_id when eventFocusGroupId is set)
     * @throws EntityNotFoundException if the focus group is not found
     * @throws IllegalArgumentException if mediaEventId is null or does not match the focus group's event_id
     */
    private void validateEventFocusGroupMatchesEvent(Long eventFocusGroupId, Long mediaEventId) {
        if (eventFocusGroupId == null) {
            return;
        }
        EventFocusGroup focusGroup = eventFocusGroupRepository
            .findById(eventFocusGroupId)
            .orElseThrow(() -> new EntityNotFoundException("EventFocusGroup not found: " + eventFocusGroupId));
        if (mediaEventId == null || !mediaEventId.equals(focusGroup.getEventId())) {
            throw new IllegalArgumentException(
                "event_focus_group must belong to the same event as the media. Focus group event_id: " +
                focusGroup.getEventId() +
                ", media event_id: " +
                mediaEventId
            );
        }
    }

    /**
     * Validates that event_id and album_id are mutually exclusive.
     * Media can belong to either an event OR an album, not both.
     *
     * @param eventId the event ID (can be null)
     * @param albumId the album ID (can be null)
     * @throws IllegalArgumentException if both eventId and albumId are non-null
     */
    private void validateEventAlbumMutuallyExclusive(Long eventId, Long albumId) {
        if (eventId != null && albumId != null) {
            throw new IllegalArgumentException(
                "Media cannot belong to both an event and an album. Event ID: " + eventId + ", Album ID: " + albumId
            );
        }
    }
}
