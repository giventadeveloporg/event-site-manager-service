package com.nextjstemplate.web.rest;

import com.nextjstemplate.repository.EventAttendeeAttachmentRepository;
import com.nextjstemplate.repository.EventAttendeeRepository;
import com.nextjstemplate.service.EventAttendeeAttachmentService;
import com.nextjstemplate.service.S3Service;
import com.nextjstemplate.service.dto.EventAttendeeAttachmentDTO;
import com.nextjstemplate.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api/event-attendee-attachments")
public class EventAttendeeAttachmentResource {

    private final Logger log = LoggerFactory.getLogger(EventAttendeeAttachmentResource.class);
    private static final String ENTITY_NAME = "eventAttendeeAttachment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventAttendeeAttachmentService service;
    private final EventAttendeeAttachmentRepository repository;
    private final EventAttendeeRepository attendeeRepository;
    private final S3Service s3Service;

    public EventAttendeeAttachmentResource(
        EventAttendeeAttachmentService service,
        EventAttendeeAttachmentRepository repository,
        EventAttendeeRepository attendeeRepository,
        S3Service s3Service
    ) {
        this.service = service;
        this.repository = repository;
        this.attendeeRepository = attendeeRepository;
        this.s3Service = s3Service;
    }

    @PostMapping("")
    public ResponseEntity<EventAttendeeAttachmentDTO> create(@Valid @RequestBody EventAttendeeAttachmentDTO dto) throws URISyntaxException {
        log.debug("REST request to save EventAttendeeAttachment : {}", dto);
        if (dto.getId() != null) {
            throw new BadRequestAlertException("A new attachment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventAttendeeAttachmentDTO result = service.save(dto);
        return ResponseEntity
            .created(new URI("/api/event-attendee-attachments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventAttendeeAttachmentDTO> update(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EventAttendeeAttachmentDTO dto
    ) {
        if (dto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!repository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        EventAttendeeAttachmentDTO result = service.update(dto);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dto.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventAttendeeAttachmentDTO> partialUpdate(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EventAttendeeAttachmentDTO dto
    ) {
        if (dto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!repository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Optional<EventAttendeeAttachmentDTO> result = service.partialUpdate(dto);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dto.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<EventAttendeeAttachmentDTO>> getAll(
        @RequestParam(value = "tenantId.equals", required = false) String tenantId,
        @RequestParam(value = "attendeeId.equals", required = false) Long attendeeId,
        @RequestParam(value = "eventId.equals", required = false) Long eventId
    ) {
        List<EventAttendeeAttachmentDTO> list = service.findAll(tenantId, attendeeId, eventId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventAttendeeAttachmentDTO> get(@PathVariable Long id) {
        Optional<EventAttendeeAttachmentDTO> dto = service.findOne(id);
        return ResponseUtil.wrapOrNotFound(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EventAttendeeAttachmentDTO> upload(
        @RequestParam("file") MultipartFile file,
        @RequestParam("attendeeId") Long attendeeId,
        @RequestParam("eventId") Long eventId,
        @RequestParam("tenantId") String tenantId,
        @RequestParam(value = "title", required = false) String title,
        @RequestParam(value = "description", required = false) String description,
        @RequestParam(value = "eventMediaType", required = false) String eventMediaType,
        @RequestParam(value = "storageType", required = false) String storageType,
        @RequestParam(value = "isPublic", required = false) Boolean isPublic,
        Authentication authentication
    ) throws URISyntaxException {
        if (file.isEmpty()) {
            throw new BadRequestAlertException("File cannot be empty", ENTITY_NAME, "fileempty");
        }
        if (!attendeeRepository.existsById(attendeeId)) {
            throw new BadRequestAlertException("Attendee does not exist", ENTITY_NAME, "attendeenotfound");
        }

        String s3Path = s3Service.generateEventAttendeeAttachmentPath(tenantId, eventId, attendeeId, file.getOriginalFilename());
        String fileUrl = s3Service.uploadFile(s3Path, file);
        Long userProfileId = getCurrentUserProfileId(authentication);
        ZonedDateTime now = ZonedDateTime.now();

        EventAttendeeAttachmentDTO dto = new EventAttendeeAttachmentDTO();
        dto.setTenantId(tenantId);
        dto.setAttendeeId(attendeeId);
        dto.setEventId(eventId);
        dto.setTitle(title != null && !title.trim().isEmpty() ? title : file.getOriginalFilename());
        dto.setDescription(description);
        dto.setFileUrl(fileUrl);
        dto.setContentType(file.getContentType());
        dto.setFileSize(Math.toIntExact(file.getSize()));
        dto.setOriginalFilename(file.getOriginalFilename());
        dto.setStorageType(storageType != null && !storageType.trim().isEmpty() ? storageType : "S3");
        dto.setIsPublic(isPublic != null ? isPublic : false);
        dto.setEventMediaType(eventMediaType != null && !eventMediaType.trim().isEmpty() ? eventMediaType : "ATTENDEE_ATTACHMENT");
        dto.setUploadedById(userProfileId);
        dto.setCreatedAt(now);
        dto.setUpdatedAt(now);

        EventAttendeeAttachmentDTO result = service.save(dto);
        return ResponseEntity
            .created(new URI("/api/event-attendee-attachments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    private Long getCurrentUserProfileId(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return null;
        }
        return 1L;
    }
}
