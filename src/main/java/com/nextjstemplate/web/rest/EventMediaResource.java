package com.nextjstemplate.web.rest;

import com.nextjstemplate.repository.EventMediaRepository;
import com.nextjstemplate.service.EventMediaQueryService;
import com.nextjstemplate.service.EventMediaService;
import com.nextjstemplate.service.EventProgramDirectorsService;
import com.nextjstemplate.service.EventFeaturedPerformersService;
import com.nextjstemplate.service.EventContactsService;
import com.nextjstemplate.service.EventSponsorsService;
import com.nextjstemplate.service.criteria.EventMediaCriteria;
import com.nextjstemplate.service.dto.AidaDTO;
import com.nextjstemplate.service.dto.EventMediaDTO;
import com.nextjstemplate.service.dto.EventProgramDirectorsDTO;
import com.nextjstemplate.service.dto.EventFeaturedPerformersDTO;
import com.nextjstemplate.service.dto.EventContactsDTO;
import com.nextjstemplate.service.dto.EventSponsorsDTO;
import com.nextjstemplate.service.mapper.EventMediaMapper;
import com.nextjstemplate.web.rest.errors.BadRequestAlertException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST controller for managing {@link com.nextjstemplate.domain.EventMedia}.
 */
@RestController
@RequestMapping("/api/event-medias")
public class EventMediaResource {

    private final Logger log = LoggerFactory.getLogger(EventMediaResource.class);

    private static final String ENTITY_NAME = "eventMedia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventMediaService eventMediaService;

    private final EventMediaRepository eventMediaRepository;

    private final EventMediaQueryService eventMediaQueryService;
    private final EventMediaMapper eventMediaMapper;
    private final EventProgramDirectorsService eventProgramDirectorsService;
    private final EventFeaturedPerformersService eventFeaturedPerformersService;
    private final EventContactsService eventContactsService;
    private final EventSponsorsService eventSponsorsService;

    public EventMediaResource(
            EventMediaService eventMediaService,
            EventMediaRepository eventMediaRepository,
            EventMediaQueryService eventMediaQueryService,
            EventMediaMapper eventMediaMapper,
            EventProgramDirectorsService eventProgramDirectorsService,
            EventFeaturedPerformersService eventFeaturedPerformersService,
            EventContactsService eventContactsService,
            EventSponsorsService eventSponsorsService) {
        this.eventMediaService = eventMediaService;
        this.eventMediaRepository = eventMediaRepository;
        this.eventMediaQueryService = eventMediaQueryService;
        this.eventMediaMapper = eventMediaMapper;
        this.eventProgramDirectorsService = eventProgramDirectorsService;
        this.eventFeaturedPerformersService = eventFeaturedPerformersService;
        this.eventContactsService = eventContactsService;
        this.eventSponsorsService = eventSponsorsService;
    }

    @PostMapping("test")
    public ResponseEntity<AidaDTO> test(@Valid @RequestBody AidaDTO eventMediaDTO) throws URISyntaxException {
        log.debug("REST request to save EventMedia : {}", eventMediaDTO);
        if (eventMediaDTO.getId() != null) {
            throw new BadRequestAlertException("A new eventMedia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return ResponseEntity.ok().body(eventMediaDTO);
    }

    /**
     * {@code POST  /event-medias} : Create a new eventMedia.
     *
     * @param eventMediaDTO the eventMediaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new eventMediaDTO, or with status {@code 400 (Bad Request)}
     *         if the eventMedia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EventMediaDTO> createEventMedia(@Valid @RequestBody EventMediaDTO eventMediaDTO)
            throws URISyntaxException {
        log.debug("REST request to save EventMedia : {}", eventMediaDTO);
        if (eventMediaDTO.getId() != null) {
            throw new BadRequestAlertException("A new eventMedia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventMediaDTO result = eventMediaService.save(eventMediaDTO);
        return ResponseEntity
                .created(new URI("/api/event-medias/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME,
                        result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /event-medias/:id} : Updates an existing eventMedia.
     *
     * @param id            the id of the eventMediaDTO to save.
     * @param eventMediaDTO the eventMediaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated eventMediaDTO,
     *         or with status {@code 400 (Bad Request)} if the eventMediaDTO is not
     *         valid,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         eventMediaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventMediaDTO> updateEventMedia(
            @PathVariable(value = "id", required = false) final Long id,
            @Valid @RequestBody EventMediaDTO eventMediaDTO) throws URISyntaxException {
        log.debug("REST request to update EventMedia : {}, {}", id, eventMediaDTO);
        if (eventMediaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventMediaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventMediaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EventMediaDTO result = eventMediaService.update(eventMediaDTO);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
                        eventMediaDTO.getId().toString()))
                .body(result);
    }

    /**
     * {@code PATCH  /event-medias/:id} : Partial updates given fields of an
     * existing eventMedia, field will ignore if it is null
     *
     * @param id            the id of the eventMediaDTO to save.
     * @param eventMediaDTO the eventMediaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated eventMediaDTO,
     *         or with status {@code 400 (Bad Request)} if the eventMediaDTO is not
     *         valid,
     *         or with status {@code 404 (Not Found)} if the eventMediaDTO is not
     *         found,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         eventMediaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventMediaDTO> partialUpdateEventMedia(
            @PathVariable(value = "id", required = false) final Long id,
            @Valid @RequestBody EventMediaDTO eventMediaDTO,
            HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to partial update EventMedia partially : {}, {}", id, eventMediaDTO);
        log.debug("Request content type: {}", request.getContentType());
        log.debug("Request body: {}", eventMediaDTO);

        if (eventMediaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventMediaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventMediaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EventMediaDTO> result = eventMediaService.partialUpdate(eventMediaDTO);

        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
                        eventMediaDTO.getId().toString()));
    }

    /**
     * {@code GET  /event-medias} : get all the eventMedias.
     *
     * @param criteria the criteria which the requested entities should match.
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of eventMedias in body.
     */
    @GetMapping("")
    @Transactional(readOnly = true)
    public ResponseEntity<List<EventMediaDTO>> getAllEventMedias(
            EventMediaCriteria criteria,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get EventMedias by criteria: {}", criteria);

        try {
            // Use the safe version that avoids LOB fields to prevent stream access errors
            // Page<EventMediaDTO> page =
            // eventMediaQueryService.findByCriteriaSafe(criteria, pageable);
            Page<EventMediaDTO> page = eventMediaQueryService.findByCriteria(criteria, pageable);
            HttpHeaders headers = PaginationUtil
                    .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (Exception e) {
            log.error("Error fetching EventMedia with criteria, falling back to safe method", e);
            // Fallback to safe method without LOB fields
            List<EventMediaDTO> safeResults = eventMediaService.findAllWithoutLobFields();
            return ResponseEntity.ok().body(safeResults);
        }
    }

    /**
     * {@code GET  /event-medias/safe} : Get all eventMedias without LOB fields.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of eventMedias in body.
     */
    @GetMapping("/safe")
    @Transactional(readOnly = true)
    public ResponseEntity<List<EventMediaDTO>> getAllEventMediasSafe() {
        log.debug("REST request to get EventMedias safely without LOB fields");
        List<EventMediaDTO> results = eventMediaService.findAllWithoutLobFields();
        return ResponseEntity.ok().body(results);
    }

    /**
     * {@code POST  /event-medias/cleanup-lob} : Clean up orphaned LOB references.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and cleanup
     *         result.
     */
    @PostMapping("/cleanup-lob")
    @Transactional
    public ResponseEntity<Map<String, Object>> cleanupOrphanedLobReferences() {
        log.debug("REST request to cleanup orphaned LOB references");

        Map<String, Object> result = new HashMap<>();
        try {
            // Update records with null LOB fields to prevent stream access errors
            /*
             * int updatedCount = eventMediaRepository.updateNullLobFields();
             * result.put("success", true);
             * result.put("updatedRecords", updatedCount);
             * result.put("message", "Successfully cleaned up " + updatedCount +
             * " records with orphaned LOB references");
             */
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error during LOB cleanup", e);
            result.put("success", false);
            result.put("error", e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * {@code GET  /event-medias/count} : count all the eventMedias.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
     *         in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEventMedias(EventMediaCriteria criteria) {
        log.debug("REST request to count EventMedias by criteria: {}", criteria);
        return ResponseEntity.ok().body(eventMediaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /event-medias/:id} : get the "id" eventMedia.
     *
     * @param id the id of the eventMediaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the eventMediaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventMediaDTO> getEventMedia(@PathVariable Long id) {
        log.debug("REST request to get EventMedia : {}", id);
        Optional<EventMediaDTO> eventMediaDTO = eventMediaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventMediaDTO);
    }

    /**
     * {@code DELETE  /event-medias/:id} : delete the "id" eventMedia.
     *
     * @param id the id of the eventMediaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventMedia(@PathVariable Long id) {
        log.debug("REST request to delete EventMedia : {}", id);
        eventMediaService.delete(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build();
    }

    // --- Begin: S3/Upload endpoints migrated from EventMediaUploadResource ---

    /**
     * POST /event-medias/upload : Upload a single media file to S3.
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EventMediaDTO> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "eventId", required = false) Long eventId,
            @RequestParam(value = "executiveTeamMemberID", required = false) Long executiveTeamMemberID,
            @RequestParam("title") @NotNull String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("tenantId") String tenantId,
            @RequestParam(value = "isPublic", required = false) Boolean isPublic,
            @RequestParam(value = "eventFlyer", required = false) Boolean eventFlyer,
            @RequestParam(value = "isEventManagementOfficialDocument", required = false) Boolean isEventManagementOfficialDocument,
            @RequestParam(value = "isHeroImage", required = false) Boolean isHeroImage,
            @RequestParam(value = "isActiveHeroImage", required = false) Boolean isActiveHeroImage,
            @RequestParam(value = "isTeamMemberProfileImage", required = false) Boolean isTeamMemberProfileImage,
            @RequestParam(value = "isHomePageHeroImage", required = false) Boolean isHomePageHeroImage,
            @RequestParam(value = "isFeaturedEventImage", required = false) Boolean isFeaturedEventImage,
            @RequestParam(value = "isLiveEventImage", required = false) Boolean isLiveEventImage,
            @RequestParam(value = "startDisplayingFromDate", required = false) String startDisplayingFromDate,
            // New entity-specific upload parameters
            @RequestParam(value = "isFeaturedPerformerPortrait", required = false) Boolean isFeaturedPerformerPortrait,
            @RequestParam(value = "isFeaturedPerformerPerformance", required = false) Boolean isFeaturedPerformerPerformance,
            @RequestParam(value = "isFeaturedPerformerGallery", required = false) Boolean isFeaturedPerformerGallery,
            @RequestParam(value = "isSponsorLogo", required = false) Boolean isSponsorLogo,
            @RequestParam(value = "isSponsorHero", required = false) Boolean isSponsorHero,
            @RequestParam(value = "isSponsorBanner", required = false) Boolean isSponsorBanner,
            @RequestParam(value = "isContactPhoto", required = false) Boolean isContactPhoto,
            @RequestParam(value = "isProgramDirectorPhoto", required = false) Boolean isProgramDirectorPhoto,
            @RequestParam(value = "entityId", required = false) Long entityId,
            @RequestParam(value = "entityType", required = false) String entityType,
            @RequestParam(value = "imageType", required = false) String imageType,
            Authentication authentication) throws URISyntaxException {
        log.debug("REST request to upload EventMedia file: {} for event: {}", file.getOriginalFilename(), eventId);
        if (file.isEmpty()) {
            throw new BadRequestAlertException("File cannot be empty", ENTITY_NAME, "fileempty");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new BadRequestAlertException("Title cannot be empty", ENTITY_NAME, "titleempty");
        }
        Long userProfileId = getCurrentUserProfileId(authentication);
        boolean isPublicValue = isPublic != null ? isPublic : false;
        boolean isHomePageHeroImageValue = isHomePageHeroImage != null ? isHomePageHeroImage : false;
        boolean isFeaturedEventImageValue = isFeaturedEventImage != null ? isFeaturedEventImage : false;
        boolean isLiveEventImageValue = isLiveEventImage != null ? isLiveEventImage : false;

        // Convert date string from ISO format (YYYY-MM-DD) to LocalDate
        LocalDate startDisplayingFromDateIn = null;
        if (startDisplayingFromDate != null && !startDisplayingFromDate.trim().isEmpty()) {
            try {
                startDisplayingFromDateIn = LocalDate.parse(startDisplayingFromDate);
            } catch (Exception e) {
                throw new BadRequestAlertException(
                        "Invalid date format for startDisplayingFromDate. Expected format: YYYY-MM-DD", ENTITY_NAME,
                        "invalidDateFormat");
            }
        }

        EventMediaDTO result = eventMediaService.uploadFile(
                file,
                eventId,
                userProfileId,
                title,
                description,
                tenantId,
                isPublicValue,
                eventFlyer,
                isEventManagementOfficialDocument,
                isHeroImage,
                isActiveHeroImage,
                isTeamMemberProfileImage,
                executiveTeamMemberID,
                isHomePageHeroImageValue,
                isFeaturedEventImageValue,
                isLiveEventImageValue,
                startDisplayingFromDateIn,
                // New entity-specific parameters
                isFeaturedPerformerPortrait,
                isFeaturedPerformerPerformance,
                isFeaturedPerformerGallery,
                isSponsorLogo,
                isSponsorHero,
                isSponsorBanner,
                isContactPhoto,
                isProgramDirectorPhoto,
                entityId,
                entityType,
                imageType);

        // For team member profile images, return OK status instead of CREATED since no
        // EventMedia record is created
        /*
         * if (result != null && result.getId() != null && result.getId() == -1L) {
         * return ResponseEntity
         * .ok()
         * .headers(HeaderUtil.createAlert(applicationName,
         * "Profile image uploaded successfully", file.getOriginalFilename()))
         * .body(result);
         * }
         */
        /*
         * It's OK even if the result is null since this method is used by
         * multiple front end urls This method is used for file upload of any type from
         * the front end so we're trying to
         * make this method general so it is OK for the result variable to be null
         * which may not have the event media DTO object So even if it is null we are
         * planning
         * to return a success and in the front it will check the HTTP status code
         * anything success is
         */
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createAlert(applicationName, "File uploaded successfully",
                        file.getOriginalFilename()))
                .body(result);
    }

    /**
     * POST /event-medias/upload/featured-performer : Upload image for featured
     * performer.
     */
    @PostMapping(value = "/upload/featured-performer", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EventMediaDTO> uploadFeaturedPerformerImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("eventId") Long eventId,
            @RequestParam("entityId") Long entityId,
            @RequestParam("imageType") String imageType,
            @RequestParam("title") @NotNull String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("tenantId") String tenantId,
            @RequestParam(value = "isPublic", required = false) Boolean isPublic,
            Authentication authentication) throws URISyntaxException {
        log.debug("REST request to upload FeaturedPerformer {} image for entity: {}", imageType, entityId);

        // Validate image type
        if (!isValidImageType(imageType, "featured-performer")) {
            throw new BadRequestAlertException("Invalid image type for featured performer: " + imageType, ENTITY_NAME,
                    "invalidImageType");
        }

        Long userProfileId = getCurrentUserProfileId(authentication);
        boolean isPublicValue = isPublic != null ? isPublic : false;

        EventMediaDTO result = eventMediaService.uploadFile(
                file, eventId, userProfileId, title, description, tenantId, isPublicValue,
                null, null, null, null, false, null, false, false, false, LocalDate.now(),
                imageType.equals("portrait"), imageType.equals("performance"), imageType.equals("gallery"),
                null, null, null, null, null, entityId, "featured-performer", imageType);

        // Update the EventFeaturedPerformers entity with the appropriate image URL
        if (result != null && result.getFileUrl() != null) {
            try {
                EventFeaturedPerformersDTO featuredPerformer = eventFeaturedPerformersService.findOne(entityId)
                        .orElse(null);
                if (featuredPerformer != null) {
                    switch (imageType) {
                        case "portrait":
                            featuredPerformer.setPortraitImageUrl(result.getFileUrl());
                            break;
                        case "performance":
                            featuredPerformer.setPerformanceImageUrl(result.getFileUrl());
                            break;
                        case "gallery":
                            // For gallery, append to existing URLs or set if empty
                            String existingGalleryUrls = featuredPerformer.getGalleryImageUrls();
                            if (existingGalleryUrls == null || existingGalleryUrls.trim().isEmpty()) {
                                featuredPerformer.setGalleryImageUrls(result.getFileUrl());
                            } else {
                                featuredPerformer.setGalleryImageUrls(existingGalleryUrls + "," + result.getFileUrl());
                            }
                            break;
                    }
                    eventFeaturedPerformersService.update(featuredPerformer);
                    log.debug("Updated EventFeaturedPerformers ID {} with {} image URL: {}", entityId, imageType,
                            result.getFileUrl());
                } else {
                    log.warn("EventFeaturedPerformers with ID {} not found, cannot update {} image URL", entityId,
                            imageType);
                }
            } catch (Exception e) {
                log.error("Failed to update EventFeaturedPerformers {} image URL for ID {}: {}", imageType, entityId,
                        e.getMessage());
                // Don't fail the upload if we can't update the featured performer record
            }
        }

        return ResponseEntity.created(new URI("/api/event-medias/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME,
                        result.getId().toString()))
                .body(result);
    }

    /**
     * POST /event-medias/upload/sponsor : Upload image for sponsor.
     */
    @PostMapping(value = "/upload/sponsor", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EventMediaDTO> uploadSponsorImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("eventId") Long eventId,
            @RequestParam("entityId") Long entityId,
            @RequestParam("imageType") String imageType,
            @RequestParam("title") @NotNull String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("tenantId") String tenantId,
            @RequestParam(value = "isPublic", required = false) Boolean isPublic,
            Authentication authentication) throws URISyntaxException {
        log.debug("REST request to upload Sponsor {} image for entity: {}", imageType, entityId);

        // Validate image type
        if (!isValidImageType(imageType, "sponsor")) {
            throw new BadRequestAlertException("Invalid image type for sponsor: " + imageType, ENTITY_NAME,
                    "invalidImageType");
        }

        Long userProfileId = getCurrentUserProfileId(authentication);
        boolean isPublicValue = isPublic != null ? isPublic : false;

        EventMediaDTO result = eventMediaService.uploadFile(
                file, eventId, userProfileId, title, description, tenantId, isPublicValue,
                null, null, null, null, false, null, false, false, false, LocalDate.now(),
                null, null, null, imageType.equals("logo"), imageType.equals("hero"), imageType.equals("banner"),
                null, null, entityId, "sponsor", imageType);

        // Update the EventSponsors entity with the appropriate image URL
        if (result != null && result.getFileUrl() != null) {
            try {
                EventSponsorsDTO sponsor = eventSponsorsService.findOne(entityId).orElse(null);
                if (sponsor != null) {
                    switch (imageType) {
                        case "logo":
                            sponsor.setLogoUrl(result.getFileUrl());
                            break;
                        case "hero":
                            sponsor.setHeroImageUrl(result.getFileUrl());
                            break;
                        case "banner":
                            sponsor.setBannerImageUrl(result.getFileUrl());
                            break;
                    }
                    eventSponsorsService.update(sponsor);
                    log.debug("Updated EventSponsors ID {} with {} image URL: {}", entityId, imageType,
                            result.getFileUrl());
                } else {
                    log.warn("EventSponsors with ID {} not found, cannot update {} image URL", entityId, imageType);
                }
            } catch (Exception e) {
                log.error("Failed to update EventSponsors {} image URL for ID {}: {}", imageType, entityId,
                        e.getMessage());
                // Don't fail the upload if we can't update the sponsor record
            }
        }

        return ResponseEntity.created(new URI("/api/event-medias/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME,
                        result.getId().toString()))
                .body(result);
    }

    /**
     * POST /event-medias/upload/contact : Upload photo for contact.
     */
    @PostMapping(value = "/upload/contact", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EventMediaDTO> uploadContactPhoto(
            @RequestParam("file") MultipartFile file,
            @RequestParam("eventId") Long eventId,
            @RequestParam("entityId") Long entityId,
            @RequestParam("title") @NotNull String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("tenantId") String tenantId,
            @RequestParam(value = "isPublic", required = false) Boolean isPublic,
            Authentication authentication) throws URISyntaxException {
        log.debug("REST request to upload Contact photo for entity: {}", entityId);

        Long userProfileId = getCurrentUserProfileId(authentication);
        boolean isPublicValue = isPublic != null ? isPublic : false;

        EventMediaDTO result = eventMediaService.uploadFile(
                file, eventId, userProfileId, title, description, tenantId, isPublicValue,
                null, null, null, null, false, null, false, false, false, LocalDate.now(),
                null, null, null, null, null, null, true, null, entityId, "contact", "photo");

        // Update the EventContacts entity with the photo URL
        if (result != null && result.getFileUrl() != null) {
            try {
                EventContactsDTO contact = eventContactsService.findOne(entityId).orElse(null);
                if (contact != null) {
                    contact.setPhotoUrl(result.getFileUrl());
                    eventContactsService.update(contact);
                    log.debug("Updated EventContacts ID {} with photo URL: {}", entityId, result.getFileUrl());
                } else {
                    log.warn("EventContacts with ID {} not found, cannot update photo URL", entityId);
                }
            } catch (Exception e) {
                log.error("Failed to update EventContacts photo URL for ID {}: {}", entityId, e.getMessage());
                // Don't fail the upload if we can't update the contact record
            }
        }

        return ResponseEntity.created(new URI("/api/event-medias/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME,
                        result.getId().toString()))
                .body(result);
    }

    /**
     * POST /event-medias/upload/program-director : Upload photo for program
     * director.
     */
    @PostMapping(value = "/upload/program-director", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EventMediaDTO> uploadProgramDirectorPhoto(
            @RequestParam("file") MultipartFile file,
            @RequestParam("eventId") Long eventId,
            @RequestParam("entityId") Long entityId,
            @RequestParam("title") @NotNull String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("tenantId") String tenantId,
            @RequestParam(value = "isPublic", required = false) Boolean isPublic,
            Authentication authentication) throws URISyntaxException {
        log.debug("REST request to upload ProgramDirector photo for entity: {}", entityId);

        Long userProfileId = getCurrentUserProfileId(authentication);
        boolean isPublicValue = isPublic != null ? isPublic : false;

        EventMediaDTO result = eventMediaService.uploadFile(
                file, eventId, userProfileId, title, description, tenantId, isPublicValue,
                null, null, null, null, false, null, false, false, false, LocalDate.now(),
                null, null, null, null, null, null, null, true, entityId, "program-director", "photo");

        // Update the EventProgramDirectors entity with the photo URL
        if (result != null && result.getFileUrl() != null) {
            try {
                EventProgramDirectorsDTO programDirector = eventProgramDirectorsService.findOne(entityId).orElse(null);
                if (programDirector != null) {
                    programDirector.setPhotoUrl(result.getFileUrl());
                    eventProgramDirectorsService.update(programDirector);
                    log.debug("Updated EventProgramDirectors ID {} with photo URL: {}", entityId, result.getFileUrl());
                } else {
                    log.warn("EventProgramDirectors with ID {} not found, cannot update photo URL", entityId);
                }
            } catch (Exception e) {
                log.error("Failed to update EventProgramDirectors photo URL for ID {}: {}", entityId, e.getMessage());
                // Don't fail the upload if we can't update the program director record
            }
        }

        return ResponseEntity.created(new URI("/api/event-medias/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME,
                        result.getId().toString()))
                .body(result);
    }

    /**
     * Helper method to validate image type for specific entity type.
     */
    private boolean isValidImageType(String imageType, String entityType) {
        return switch (entityType) {
            case "featured-performer" -> Set.of("portrait", "performance", "gallery").contains(imageType);
            case "sponsor" -> Set.of("logo", "hero", "banner").contains(imageType);
            case "contact", "program-director" -> "photo".equals(imageType);
            default -> false;
        };
    }

    /**
     * POST /event-medias/upload-multiple : Upload multiple media files to S3.
     */
    @PostMapping(value = "/upload-multiple", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<EventMediaDTO>> uploadMultipleFiles(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam(value = "eventId", required = false) Long eventId,
            @RequestParam(value = "upLoadedById", required = false) Long upLoadedById,
            @RequestParam(value = "executiveTeamMemberID", required = false) Long executiveTeamMemberID,
            @RequestParam("titles") List<String> titles,
            @RequestParam(value = "descriptions", required = false) List<String> descriptions,
            @RequestParam("tenantId") String tenantId,
            @RequestParam(value = "isPublic", required = false) Boolean isPublic,
            @RequestParam(value = "eventFlyer", required = false) Boolean eventFlyer,
            @RequestParam(value = "isEventManagementOfficialDocument", required = false) Boolean isEventManagementOfficialDocument,
            @RequestParam(value = "isHeroImage", required = false) Boolean isHeroImage,
            @RequestParam(value = "isActiveHeroImage", required = false) Boolean isActiveHeroImage,
            @RequestParam(value = "isTeamMemberProfileImage", required = false) Boolean isTeamMemberProfileImage,
            @RequestParam(value = "isHomePageHeroImage", required = false) Boolean isHomePageHeroImage,
            @RequestParam(value = "isFeaturedEventImage", required = false) Boolean isFeaturedEventImage,
            @RequestParam(value = "isLiveEventImage", required = false) Boolean isLiveEventImage,
            @RequestParam(value = "startDisplayingFromDate", required = false) String startDisplayingFromDate,
            Authentication authentication) {
        log.debug("REST request to upload {} EventMedia files for event: {}", files.size(), eventId);
        if (files.isEmpty()) {
            throw new BadRequestAlertException("Files list cannot be empty", ENTITY_NAME, "filesempty");
        }
        boolean hasEmptyFile = files.stream().anyMatch(MultipartFile::isEmpty);
        if (hasEmptyFile) {
            throw new BadRequestAlertException("One or more files are empty", ENTITY_NAME, "fileempty");
        }

        // Validate that titles are provided and match the number of files
        if (titles == null || titles.isEmpty()) {
            throw new BadRequestAlertException("Titles are required for all files", ENTITY_NAME, "titlesmissing");
        }
        if (titles.size() != files.size()) {
            throw new BadRequestAlertException("Number of titles must match number of files", ENTITY_NAME,
                    "titlessizemismatch");
        }

        // Validate that titles are not empty or blank
        for (int i = 0; i < titles.size(); i++) {
            String title = titles.get(i);
            if (title == null || title.trim().isEmpty()) {
                throw new BadRequestAlertException("Title for file " + (i + 1) + " cannot be empty", ENTITY_NAME,
                        "titleempty");
            }
        }
        Long userProfileId = null;
        if (upLoadedById == null) {
            userProfileId = getCurrentUserProfileId(authentication);
        } else {
            userProfileId = upLoadedById;
        }
        boolean isPublicValue = isPublic != null ? isPublic : false;
        boolean isHomePageHeroImageValue = isHomePageHeroImage != null ? isHomePageHeroImage : false;
        boolean isFeaturedEventImageValue = isFeaturedEventImage != null ? isFeaturedEventImage : false;
        boolean isLiveEventImageValue = isLiveEventImage != null ? isLiveEventImage : false;

        // Convert date string from ISO format (YYYY-MM-DD) to LocalDate
        LocalDate startDisplayingFromDateIn = null;
        if (startDisplayingFromDate != null && !startDisplayingFromDate.trim().isEmpty()) {
            try {
                startDisplayingFromDateIn = LocalDate.parse(startDisplayingFromDate);
            } catch (Exception e) {
                throw new BadRequestAlertException(
                        "Invalid date format for startDisplayingFromDate. Expected format: YYYY-MM-DD", ENTITY_NAME,
                        "invalidDateFormat");
            }
        }
        List<EventMediaDTO> results = eventMediaService.uploadMultipleFiles(
                files,
                eventId,
                userProfileId,
                titles,
                descriptions,
                tenantId,
                isPublicValue,
                eventFlyer,
                isEventManagementOfficialDocument,
                isHeroImage,
                isActiveHeroImage,
                isTeamMemberProfileImage,
                executiveTeamMemberID,
                isHomePageHeroImageValue,
                isFeaturedEventImageValue,
                isLiveEventImageValue,
                startDisplayingFromDateIn);

        /*
         * It's OK even if the result is null since this method is used by
         * multiple front end urls This method is used for file upload of any type from
         * the front end so we're trying to
         * make this method general so it is OK for the result variable to be null
         * which may not have the event media DTO object So even if it is null we are
         * planning
         * to return a success and in the front it will check the HTTP status code
         * anything success is
         */

        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createAlert(applicationName, "eventMedia.uploaded", String.valueOf(results.size())))
                .body(results);
    }

    /**
     * GET /event-medias/event/{eventId}/view : Get EventMedias for an event with
     * viewing URLs.
     */
    @GetMapping("/event/{eventId}/view")
    public ResponseEntity<List<EventMediaDTO>> getEventMediaForViewing(
            @PathVariable Long eventId,
            @RequestParam(value = "includePrivate", defaultValue = "false") boolean includePrivate,
            Authentication authentication) {
        log.debug("REST request to get EventMedias with viewing URLs for event: {}", eventId);
        Long userProfileId = getCurrentUserProfileId(authentication);
        List<EventMediaDTO> eventMedia = eventMediaService.getEventMediaWithUrls(eventId, userProfileId,
                includePrivate);
        return ResponseEntity.ok(eventMedia);
    }

    /**
     * GET /event-medias/{id}/download-url : Get download URL (presigned) for a
     * specific EventMedia.
     */
    @GetMapping("/{id}/download-url")
    public ResponseEntity<Map<String, Object>> getDownloadUrl(
            @PathVariable Long id,
            @RequestParam(value = "expirationHours", defaultValue = "2") int expirationHours,
            Authentication authentication) {
        log.debug("REST request to get download URL for EventMedia: {}", id);
        Long userProfileId = getCurrentUserProfileId(authentication);
        String downloadUrl = eventMediaService.getViewingUrl(id, userProfileId);
        Map<String, Object> response = new HashMap<>();
        response.put("downloadUrl", downloadUrl);
        response.put("mediaId", id);
        response.put("expiresAt", Instant.now().plusSeconds(expirationHours * 3600));
        return ResponseEntity.ok(response);
    }

    /**
     * POST /event-medias/refresh-urls : Refresh presigned URLs for multiple
     * EventMedias.
     */
    @PostMapping("/refresh-urls")
    public ResponseEntity<List<Map<String, Object>>> refreshUrls(@RequestBody UrlRefreshRequest request,
            Authentication authentication) {
        log.debug("REST request to refresh URLs for {} EventMedias", request.getMediaIds().size());
        Long userProfileId = getCurrentUserProfileId(authentication);
        List<Map<String, Object>> refreshedMedia = request
                .getMediaIds()
                .stream()
                .map(mediaId -> {
                    try {
                        String newUrl = eventMediaService.getViewingUrl(mediaId, userProfileId);
                        Map<String, Object> dto = new HashMap<>();
                        dto.put("id", mediaId);
                        dto.put("viewingUrl", newUrl);
                        dto.put("expiresAt", Instant.now().plusSeconds(request.getExpirationHours() * 3600));
                        return dto;
                    } catch (Exception e) {
                        log.error("Error refreshing URL for EventMedia: {}", mediaId, e);
                        return null;
                    }
                })
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toList());
        return ResponseEntity.ok(refreshedMedia);
    }

    // Helper method for authentication
    private Long getCurrentUserProfileId(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new SecurityException("Authentication required");
        }
        // Implement based on your authentication mechanism
        // Example implementations:
        // For custom UserPrincipal:
        // if (authentication.getPrincipal() instanceof UserPrincipal) {
        // UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        // return principal.getUserProfileId();
        // }
        // For JWT with custom claims:
        // if (authentication instanceof JwtAuthenticationToken) {
        // JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
        // String userProfileId =
        // jwtToken.getTokenAttributes().get("userProfileId").toString();
        // return Long.parseLong(userProfileId);
        // }
        // Placeholder implementation - replace with your actual logic
        return 1L;
    }

    // Inner class for refresh request
    public static class UrlRefreshRequest {

        private List<Long> mediaIds;
        private int expirationHours = 2;

        public List<Long> getMediaIds() {
            return mediaIds;
        }

        public void setMediaIds(List<Long> mediaIds) {
            this.mediaIds = mediaIds;
        }

        public int getExpirationHours() {
            return expirationHours;
        }

        public void setExpirationHours(int expirationHours) {
            this.expirationHours = expirationHours;
        }
    }
    // --- End: S3/Upload endpoints ---
}
