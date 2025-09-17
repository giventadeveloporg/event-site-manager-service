package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.domain.EventMedia;
import com.nextjstemplate.domain.ExecutiveCommitteeTeamMember;
import com.nextjstemplate.repository.EventDetailsRepository;
import com.nextjstemplate.repository.EventMediaRepository;
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

    @Autowired
    public EventMediaServiceImpl(
            EventMediaRepository eventMediaRepository,
            EventMediaMapper eventMediaMapper,
            S3Service s3Service,
            EventDetailsRepository eventRepository,
            ExecutiveCommitteeTeamMemberRepository executiveCommitteeTeamMemberRepository) {
        this.eventMediaRepository = eventMediaRepository;
        this.eventMediaMapper = eventMediaMapper;
        this.s3Service = s3Service;
        this.eventRepository = eventRepository;
        this.executiveCommitteeTeamMemberRepository = executiveCommitteeTeamMemberRepository;
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
            String imageType) {
        // Upload to S3 - use entity-specific path if entity parameters are provided
        String fileUrl;
        if (entityId != null && entityType != null && imageType != null) {
            // Use entity-specific upload with dynamic path construction
            fileUrl = s3Service.uploadFileWithEntityPath(file, eventId, entityId, entityType, imageType, title,
                    tenantId);
        } else {
            // Use existing upload method for backward compatibility
            fileUrl = s3Service.uploadFile(file, eventId, title, tenantId, isTeamMemberProfileImage);
        }

        if (isTeamMemberProfileImage == null || !isTeamMemberProfileImage) {
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
            eventMedia.setStartDisplayingFromDate(startDisplayingFromDate);
            eventMedia.setEventId(eventId);
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
                    .orElseThrow(() -> new RuntimeException(
                            "ExecutiveCommitteeTeamMember not found with ID: " + executiveTeamMemberID));
            teamMember.setProfileImageUrl(fileUrl);
            executiveCommitteeTeamMemberRepository.save(teamMember);
            log.debug("Successfully updated profile image URL for ExecutiveCommitteeTeamMember ID: {}",
                    executiveTeamMemberID);

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
            LocalDate startDisplayingFromDate) {
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
                    null, null, null, null, null, null, null, null, null, null, null);
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
                dto.setPreSignedUrlExpiresAt(
                        ((java.sql.Timestamp) raw[13]).toInstant().atZone(java.time.ZoneId.systemDefault()));
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

        return dto;
    }
}
