package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.domain.EventFeaturedPerformers;
import com.nextjstemplate.domain.EventMedia;
import com.nextjstemplate.domain.EventProgramDirectors;
import com.nextjstemplate.domain.EventSponsors;
import com.nextjstemplate.domain.EventSponsorsJoin;
import com.nextjstemplate.domain.ExecutiveCommitteeTeamMember;
import com.nextjstemplate.repository.EventDetailsRepository;
import com.nextjstemplate.repository.EventFeaturedPerformersRepository;
import com.nextjstemplate.repository.EventMediaRepository;
import com.nextjstemplate.repository.EventProgramDirectorsRepository;
import com.nextjstemplate.repository.EventSponsorsJoinRepository;
import com.nextjstemplate.repository.EventSponsorsRepository;
import com.nextjstemplate.repository.ExecutiveCommitteeTeamMemberRepository;
import com.nextjstemplate.service.EventMediaService;
import com.nextjstemplate.service.S3Service;
import com.nextjstemplate.service.dto.EventMediaDTO;
import com.nextjstemplate.service.mapper.EventMediaMapper;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for managing
 * {@link com.nextjstemplate.domain.EventMedia}.
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
        EventProgramDirectorsRepository eventProgramDirectorsRepository
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
    }

    @Override
    public EventMediaDTO save(EventMediaDTO eventMediaDTO) {
        log.debug("Request to save EventMedia : {}", eventMediaDTO);
        EventMedia eventMedia = eventMediaMapper.toEntity(eventMediaDTO);
        eventMedia = eventMediaRepository.save(eventMedia);
        return eventMediaMapper.toDto(eventMedia);
    }

    @Override
    public EventMediaDTO update(EventMediaDTO eventMediaDTO) {
        log.debug("Request to update EventMedia : {}", eventMediaDTO);
        EventMedia eventMedia = eventMediaMapper.toEntity(eventMediaDTO);
        eventMedia = eventMediaRepository.save(eventMedia);
        return eventMediaMapper.toDto(eventMedia);
    }

    @Override
    public Optional<EventMediaDTO> partialUpdate(EventMediaDTO eventMediaDTO) {
        log.debug("Request to partially update EventMedia : {}", eventMediaDTO);

        return eventMediaRepository
            .findById(eventMediaDTO.getId())
            .map(existingEventMedia -> {
                eventMediaMapper.partialUpdate(existingEventMedia, eventMediaDTO);

                return existingEventMedia;
            })
            .map(eventMediaRepository::save)
            .map(eventMediaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventMediaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EventMedias");
        return eventMediaRepository.findAll(pageable).map(eventMediaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventMediaDTO> findOne(Long id) {
        log.debug("Request to get EventMedia : {}", id);
        return eventMediaRepository.findById(id).map(eventMediaMapper::toDto);
    }

    @Override
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

            eventMedia = eventMediaRepository.save(eventMedia);
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
}
