package com.eventsitemanager.web.rest;

import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.EventFocusGroupRepository;
import com.eventsitemanager.service.EventFocusGroupQueryService;
import com.eventsitemanager.service.EventFocusGroupService;
import com.eventsitemanager.service.criteria.EventFocusGroupCriteria;
import com.eventsitemanager.service.dto.EventFocusGroupDTO;
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
 * REST controller for managing
 * {@link com.eventsitemanager.domain.EventFocusGroup}.
 */
@RestController
@RequestMapping("/api/event-focus-groups")
public class EventFocusGroupResource {

    private final Logger log = LoggerFactory.getLogger(EventFocusGroupResource.class);

    private static final String ENTITY_NAME = "eventFocusGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventFocusGroupService eventFocusGroupService;

    private final EventFocusGroupRepository eventFocusGroupRepository;

    private final EventFocusGroupQueryService eventFocusGroupQueryService;

    public EventFocusGroupResource(
        EventFocusGroupService eventFocusGroupService,
        EventFocusGroupRepository eventFocusGroupRepository,
        EventFocusGroupQueryService eventFocusGroupQueryService
    ) {
        this.eventFocusGroupService = eventFocusGroupService;
        this.eventFocusGroupRepository = eventFocusGroupRepository;
        this.eventFocusGroupQueryService = eventFocusGroupQueryService;
    }

    /**
     * {@code POST  /event-focus-groups} : Create a new eventFocusGroup.
     *
     * @param eventFocusGroupDTO the eventFocusGroupDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new eventFocusGroupDTO,
     *         or with status {@code 400 (Bad Request)} if the eventFocusGroup has
     *         already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EventFocusGroupDTO> createEventFocusGroup(@Valid @RequestBody EventFocusGroupDTO eventFocusGroupDTO)
        throws URISyntaxException {
        log.debug("REST request to save EventFocusGroup : {}", eventFocusGroupDTO);
        if (eventFocusGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new eventFocusGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }

        try {
            EventFocusGroupDTO result = eventFocusGroupService.save(eventFocusGroupDTO);
            return ResponseEntity
                .created(new URI("/api/event-focus-groups/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
        } catch (IllegalArgumentException e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "constraintviolation");
        }
    }

    /**
     * {@code PUT  /event-focus-groups/:id} : Updates an existing eventFocusGroup.
     *
     * @param id                 the id of the eventFocusGroupDTO to save.
     * @param eventFocusGroupDTO the eventFocusGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated eventFocusGroupDTO,
     *         or with status {@code 400 (Bad Request)} if the eventFocusGroupDTO is
     *         not valid,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         eventFocusGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventFocusGroupDTO> updateEventFocusGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EventFocusGroupDTO eventFocusGroupDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EventFocusGroup : {}, {}", id, eventFocusGroupDTO);
        if (eventFocusGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventFocusGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventFocusGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        try {
            EventFocusGroupDTO result = eventFocusGroupService.update(eventFocusGroupDTO);
            return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventFocusGroupDTO.getId().toString()))
                .body(result);
        } catch (IllegalArgumentException e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "constraintviolation");
        }
    }

    /**
     * {@code PATCH  /event-focus-groups/:id} : Partial updates given fields of an
     * existing eventFocusGroup, field will ignore if it is null
     *
     * @param id                 the id of the eventFocusGroupDTO to save.
     * @param eventFocusGroupDTO the eventFocusGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated eventFocusGroupDTO,
     *         or with status {@code 400 (Bad Request)} if the eventFocusGroupDTO is
     *         not valid,
     *         or with status {@code 404 (Not Found)} if the eventFocusGroupDTO is
     *         not found,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         eventFocusGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventFocusGroupDTO> partialUpdateEventFocusGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EventFocusGroupDTO eventFocusGroupDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EventFocusGroup partially : {}, {}", id, eventFocusGroupDTO);
        if (eventFocusGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventFocusGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventFocusGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        try {
            Optional<EventFocusGroupDTO> result = eventFocusGroupService.partialUpdate(eventFocusGroupDTO);
            return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventFocusGroupDTO.getId().toString())
            );
        } catch (IllegalArgumentException e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "constraintviolation");
        }
    }

    /**
     * {@code GET  /event-focus-groups} : get all the eventFocusGroups.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of eventFocusGroups in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EventFocusGroupDTO>> getAllEventFocusGroups(
        EventFocusGroupCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get EventFocusGroups by criteria: {}", criteria);

        Page<EventFocusGroupDTO> page = eventFocusGroupQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /event-focus-groups/count} : count all the eventFocusGroups.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
     *         in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEventFocusGroups(EventFocusGroupCriteria criteria) {
        log.debug("REST request to count EventFocusGroups by criteria: {}", criteria);
        return ResponseEntity.ok().body(eventFocusGroupQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /event-focus-groups/:id} : get the "id" eventFocusGroup.
     *
     * @param id the id of the eventFocusGroupDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the eventFocusGroupDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventFocusGroupDTO> getEventFocusGroup(@PathVariable Long id) {
        log.debug("REST request to get EventFocusGroup : {}", id);
        Optional<EventFocusGroupDTO> eventFocusGroupDTO = eventFocusGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventFocusGroupDTO);
    }

    /**
     * {@code DELETE  /event-focus-groups/:id} : delete the "id" eventFocusGroup.
     *
     * @param id the id of the eventFocusGroupDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventFocusGroup(@PathVariable Long id) {
        log.debug("REST request to delete EventFocusGroup : {}", id);
        eventFocusGroupService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
