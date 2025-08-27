package com.nextjstemplate.web.rest;

import com.nextjstemplate.repository.EventAdminAuditLogRepository;
import com.nextjstemplate.service.EventAdminAuditLogQueryService;
import com.nextjstemplate.service.EventAdminAuditLogService;
import com.nextjstemplate.service.criteria.EventAdminAuditLogCriteria;
import com.nextjstemplate.service.dto.EventAdminAuditLogDTO;
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
 * REST controller for managing {@link com.nextjstemplate.domain.EventAdminAuditLog}.
 */
@RestController
@RequestMapping("/api/event-admin-audit-logs")
public class EventAdminAuditLogResource {

    private static final Logger LOG = LoggerFactory.getLogger(EventAdminAuditLogResource.class);

    private static final String ENTITY_NAME = "eventAdminAuditLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventAdminAuditLogService eventAdminAuditLogService;

    private final EventAdminAuditLogRepository eventAdminAuditLogRepository;

    private final EventAdminAuditLogQueryService eventAdminAuditLogQueryService;

    public EventAdminAuditLogResource(
        EventAdminAuditLogService eventAdminAuditLogService,
        EventAdminAuditLogRepository eventAdminAuditLogRepository,
        EventAdminAuditLogQueryService eventAdminAuditLogQueryService
    ) {
        this.eventAdminAuditLogService = eventAdminAuditLogService;
        this.eventAdminAuditLogRepository = eventAdminAuditLogRepository;
        this.eventAdminAuditLogQueryService = eventAdminAuditLogQueryService;
    }

    /**
     * {@code POST  /event-admin-audit-logs} : Create a new eventAdminAuditLog.
     *
     * @param eventAdminAuditLogDTO the eventAdminAuditLogDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventAdminAuditLogDTO, or with status {@code 400 (Bad Request)} if the eventAdminAuditLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EventAdminAuditLogDTO> createEventAdminAuditLog(@Valid @RequestBody EventAdminAuditLogDTO eventAdminAuditLogDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save EventAdminAuditLog : {}", eventAdminAuditLogDTO);
        if (eventAdminAuditLogDTO.getId() != null) {
            throw new BadRequestAlertException("A new eventAdminAuditLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        eventAdminAuditLogDTO = eventAdminAuditLogService.save(eventAdminAuditLogDTO);
        return ResponseEntity.created(new URI("/api/event-admin-audit-logs/" + eventAdminAuditLogDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, eventAdminAuditLogDTO.getId().toString()))
            .body(eventAdminAuditLogDTO);
    }

    /**
     * {@code PUT  /event-admin-audit-logs/:id} : Updates an existing eventAdminAuditLog.
     *
     * @param id the id of the eventAdminAuditLogDTO to save.
     * @param eventAdminAuditLogDTO the eventAdminAuditLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventAdminAuditLogDTO,
     * or with status {@code 400 (Bad Request)} if the eventAdminAuditLogDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventAdminAuditLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventAdminAuditLogDTO> updateEventAdminAuditLog(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EventAdminAuditLogDTO eventAdminAuditLogDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update EventAdminAuditLog : {}, {}", id, eventAdminAuditLogDTO);
        if (eventAdminAuditLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventAdminAuditLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventAdminAuditLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        eventAdminAuditLogDTO = eventAdminAuditLogService.update(eventAdminAuditLogDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventAdminAuditLogDTO.getId().toString()))
            .body(eventAdminAuditLogDTO);
    }

    /**
     * {@code PATCH  /event-admin-audit-logs/:id} : Partial updates given fields of an existing eventAdminAuditLog, field will ignore if it is null
     *
     * @param id the id of the eventAdminAuditLogDTO to save.
     * @param eventAdminAuditLogDTO the eventAdminAuditLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventAdminAuditLogDTO,
     * or with status {@code 400 (Bad Request)} if the eventAdminAuditLogDTO is not valid,
     * or with status {@code 404 (Not Found)} if the eventAdminAuditLogDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the eventAdminAuditLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventAdminAuditLogDTO> partialUpdateEventAdminAuditLog(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EventAdminAuditLogDTO eventAdminAuditLogDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EventAdminAuditLog partially : {}, {}", id, eventAdminAuditLogDTO);
        if (eventAdminAuditLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventAdminAuditLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventAdminAuditLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EventAdminAuditLogDTO> result = eventAdminAuditLogService.partialUpdate(eventAdminAuditLogDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventAdminAuditLogDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /event-admin-audit-logs} : get all the eventAdminAuditLogs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eventAdminAuditLogs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EventAdminAuditLogDTO>> getAllEventAdminAuditLogs(
        EventAdminAuditLogCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get EventAdminAuditLogs by criteria: {}", criteria);

        Page<EventAdminAuditLogDTO> page = eventAdminAuditLogQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /event-admin-audit-logs/count} : count all the eventAdminAuditLogs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEventAdminAuditLogs(EventAdminAuditLogCriteria criteria) {
        LOG.debug("REST request to count EventAdminAuditLogs by criteria: {}", criteria);
        return ResponseEntity.ok().body(eventAdminAuditLogQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /event-admin-audit-logs/:id} : get the "id" eventAdminAuditLog.
     *
     * @param id the id of the eventAdminAuditLogDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventAdminAuditLogDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventAdminAuditLogDTO> getEventAdminAuditLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EventAdminAuditLog : {}", id);
        Optional<EventAdminAuditLogDTO> eventAdminAuditLogDTO = eventAdminAuditLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventAdminAuditLogDTO);
    }

    /**
     * {@code DELETE  /event-admin-audit-logs/:id} : delete the "id" eventAdminAuditLog.
     *
     * @param id the id of the eventAdminAuditLogDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventAdminAuditLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EventAdminAuditLog : {}", id);
        eventAdminAuditLogService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
