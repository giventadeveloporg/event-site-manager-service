package com.nextjstemplate.web.rest;

import com.nextjstemplate.repository.EventLiveUpdateAttachmentRepository;
import com.nextjstemplate.service.EventLiveUpdateAttachmentQueryService;
import com.nextjstemplate.service.EventLiveUpdateAttachmentService;
import com.nextjstemplate.service.criteria.EventLiveUpdateAttachmentCriteria;
import com.nextjstemplate.service.dto.EventLiveUpdateAttachmentDTO;
import com.nextjstemplate.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.nextjstemplate.domain.EventLiveUpdateAttachment}.
 */
@RestController
@RequestMapping("/api/event-live-update-attachments")
public class EventLiveUpdateAttachmentResource {

    private static final Logger LOG = LoggerFactory.getLogger(EventLiveUpdateAttachmentResource.class);

    private static final String ENTITY_NAME = "eventLiveUpdateAttachment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventLiveUpdateAttachmentService eventLiveUpdateAttachmentService;

    private final EventLiveUpdateAttachmentRepository eventLiveUpdateAttachmentRepository;

    private final EventLiveUpdateAttachmentQueryService eventLiveUpdateAttachmentQueryService;

    public EventLiveUpdateAttachmentResource(
        EventLiveUpdateAttachmentService eventLiveUpdateAttachmentService,
        EventLiveUpdateAttachmentRepository eventLiveUpdateAttachmentRepository,
        EventLiveUpdateAttachmentQueryService eventLiveUpdateAttachmentQueryService
    ) {
        this.eventLiveUpdateAttachmentService = eventLiveUpdateAttachmentService;
        this.eventLiveUpdateAttachmentRepository = eventLiveUpdateAttachmentRepository;
        this.eventLiveUpdateAttachmentQueryService = eventLiveUpdateAttachmentQueryService;
    }

    /**
     * {@code POST  /event-live-update-attachments} : Create a new eventLiveUpdateAttachment.
     *
     * @param eventLiveUpdateAttachmentDTO the eventLiveUpdateAttachmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventLiveUpdateAttachmentDTO, or with status {@code 400 (Bad Request)} if the eventLiveUpdateAttachment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EventLiveUpdateAttachmentDTO> createEventLiveUpdateAttachment(
        @Valid @RequestBody EventLiveUpdateAttachmentDTO eventLiveUpdateAttachmentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save EventLiveUpdateAttachment : {}", eventLiveUpdateAttachmentDTO);
        if (eventLiveUpdateAttachmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new eventLiveUpdateAttachment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        eventLiveUpdateAttachmentDTO = eventLiveUpdateAttachmentService.save(eventLiveUpdateAttachmentDTO);
        return ResponseEntity.created(new URI("/api/event-live-update-attachments/" + eventLiveUpdateAttachmentDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, eventLiveUpdateAttachmentDTO.getId().toString())
            )
            .body(eventLiveUpdateAttachmentDTO);
    }

    /**
     * {@code PUT  /event-live-update-attachments/:id} : Updates an existing eventLiveUpdateAttachment.
     *
     * @param id the id of the eventLiveUpdateAttachmentDTO to save.
     * @param eventLiveUpdateAttachmentDTO the eventLiveUpdateAttachmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventLiveUpdateAttachmentDTO,
     * or with status {@code 400 (Bad Request)} if the eventLiveUpdateAttachmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventLiveUpdateAttachmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventLiveUpdateAttachmentDTO> updateEventLiveUpdateAttachment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EventLiveUpdateAttachmentDTO eventLiveUpdateAttachmentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update EventLiveUpdateAttachment : {}, {}", id, eventLiveUpdateAttachmentDTO);
        if (eventLiveUpdateAttachmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventLiveUpdateAttachmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventLiveUpdateAttachmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        eventLiveUpdateAttachmentDTO = eventLiveUpdateAttachmentService.update(eventLiveUpdateAttachmentDTO);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventLiveUpdateAttachmentDTO.getId().toString())
            )
            .body(eventLiveUpdateAttachmentDTO);
    }

    /**
     * {@code PATCH  /event-live-update-attachments/:id} : Partial updates given fields of an existing eventLiveUpdateAttachment, field will ignore if it is null
     *
     * @param id the id of the eventLiveUpdateAttachmentDTO to save.
     * @param eventLiveUpdateAttachmentDTO the eventLiveUpdateAttachmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventLiveUpdateAttachmentDTO,
     * or with status {@code 400 (Bad Request)} if the eventLiveUpdateAttachmentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the eventLiveUpdateAttachmentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the eventLiveUpdateAttachmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventLiveUpdateAttachmentDTO> partialUpdateEventLiveUpdateAttachment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EventLiveUpdateAttachmentDTO eventLiveUpdateAttachmentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EventLiveUpdateAttachment partially : {}, {}", id, eventLiveUpdateAttachmentDTO);
        if (eventLiveUpdateAttachmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventLiveUpdateAttachmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventLiveUpdateAttachmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EventLiveUpdateAttachmentDTO> result = eventLiveUpdateAttachmentService.partialUpdate(eventLiveUpdateAttachmentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventLiveUpdateAttachmentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /event-live-update-attachments} : get all the eventLiveUpdateAttachments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eventLiveUpdateAttachments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EventLiveUpdateAttachmentDTO>> getAllEventLiveUpdateAttachments(
        EventLiveUpdateAttachmentCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get EventLiveUpdateAttachments by criteria: {}", criteria);

        Page<EventLiveUpdateAttachmentDTO> page = eventLiveUpdateAttachmentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /event-live-update-attachments/count} : count all the eventLiveUpdateAttachments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEventLiveUpdateAttachments(EventLiveUpdateAttachmentCriteria criteria) {
        LOG.debug("REST request to count EventLiveUpdateAttachments by criteria: {}", criteria);
        return ResponseEntity.ok().body(eventLiveUpdateAttachmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /event-live-update-attachments/:id} : get the "id" eventLiveUpdateAttachment.
     *
     * @param id the id of the eventLiveUpdateAttachmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventLiveUpdateAttachmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventLiveUpdateAttachmentDTO> getEventLiveUpdateAttachment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EventLiveUpdateAttachment : {}", id);
        Optional<EventLiveUpdateAttachmentDTO> eventLiveUpdateAttachmentDTO = eventLiveUpdateAttachmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventLiveUpdateAttachmentDTO);
    }

    /**
     * {@code DELETE  /event-live-update-attachments/:id} : delete the "id" eventLiveUpdateAttachment.
     *
     * @param id the id of the eventLiveUpdateAttachmentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventLiveUpdateAttachment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EventLiveUpdateAttachment : {}", id);
        eventLiveUpdateAttachmentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
