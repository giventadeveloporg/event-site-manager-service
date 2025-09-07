package com.nextjstemplate.web.rest;

import com.nextjstemplate.repository.EventLiveUpdateRepository;
import com.nextjstemplate.service.EventLiveUpdateQueryService;
import com.nextjstemplate.service.EventLiveUpdateService;
import com.nextjstemplate.service.criteria.EventLiveUpdateCriteria;
import com.nextjstemplate.service.dto.EventLiveUpdateDTO;
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
 * REST controller for managing {@link com.nextjstemplate.domain.EventLiveUpdate}.
 */
@RestController
@RequestMapping("/api/event-live-updates")
public class EventLiveUpdateResource {

    private static final Logger LOG = LoggerFactory.getLogger(EventLiveUpdateResource.class);

    private static final String ENTITY_NAME = "eventLiveUpdate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventLiveUpdateService eventLiveUpdateService;

    private final EventLiveUpdateRepository eventLiveUpdateRepository;

    private final EventLiveUpdateQueryService eventLiveUpdateQueryService;

    public EventLiveUpdateResource(
        EventLiveUpdateService eventLiveUpdateService,
        EventLiveUpdateRepository eventLiveUpdateRepository,
        EventLiveUpdateQueryService eventLiveUpdateQueryService
    ) {
        this.eventLiveUpdateService = eventLiveUpdateService;
        this.eventLiveUpdateRepository = eventLiveUpdateRepository;
        this.eventLiveUpdateQueryService = eventLiveUpdateQueryService;
    }

    /**
     * {@code POST  /event-live-updates} : Create a new eventLiveUpdate.
     *
     * @param eventLiveUpdateDTO the eventLiveUpdateDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventLiveUpdateDTO, or with status {@code 400 (Bad Request)} if the eventLiveUpdate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EventLiveUpdateDTO> createEventLiveUpdate(@Valid @RequestBody EventLiveUpdateDTO eventLiveUpdateDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save EventLiveUpdate : {}", eventLiveUpdateDTO);
        if (eventLiveUpdateDTO.getId() != null) {
            throw new BadRequestAlertException("A new eventLiveUpdate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        eventLiveUpdateDTO = eventLiveUpdateService.save(eventLiveUpdateDTO);
        return ResponseEntity
            .created(new URI("/api/event-live-updates/" + eventLiveUpdateDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, eventLiveUpdateDTO.getId().toString()))
            .body(eventLiveUpdateDTO);
    }

    /**
     * {@code PUT  /event-live-updates/:id} : Updates an existing eventLiveUpdate.
     *
     * @param id the id of the eventLiveUpdateDTO to save.
     * @param eventLiveUpdateDTO the eventLiveUpdateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventLiveUpdateDTO,
     * or with status {@code 400 (Bad Request)} if the eventLiveUpdateDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventLiveUpdateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventLiveUpdateDTO> updateEventLiveUpdate(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EventLiveUpdateDTO eventLiveUpdateDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update EventLiveUpdate : {}, {}", id, eventLiveUpdateDTO);
        if (eventLiveUpdateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventLiveUpdateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventLiveUpdateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        eventLiveUpdateDTO = eventLiveUpdateService.update(eventLiveUpdateDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventLiveUpdateDTO.getId().toString()))
            .body(eventLiveUpdateDTO);
    }

    /**
     * {@code PATCH  /event-live-updates/:id} : Partial updates given fields of an existing eventLiveUpdate, field will ignore if it is null
     *
     * @param id the id of the eventLiveUpdateDTO to save.
     * @param eventLiveUpdateDTO the eventLiveUpdateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventLiveUpdateDTO,
     * or with status {@code 400 (Bad Request)} if the eventLiveUpdateDTO is not valid,
     * or with status {@code 404 (Not Found)} if the eventLiveUpdateDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the eventLiveUpdateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventLiveUpdateDTO> partialUpdateEventLiveUpdate(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EventLiveUpdateDTO eventLiveUpdateDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EventLiveUpdate partially : {}, {}", id, eventLiveUpdateDTO);
        if (eventLiveUpdateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventLiveUpdateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventLiveUpdateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EventLiveUpdateDTO> result = eventLiveUpdateService.partialUpdate(eventLiveUpdateDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventLiveUpdateDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /event-live-updates} : get all the eventLiveUpdates.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eventLiveUpdates in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EventLiveUpdateDTO>> getAllEventLiveUpdates(
        EventLiveUpdateCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get EventLiveUpdates by criteria: {}", criteria);

        Page<EventLiveUpdateDTO> page = eventLiveUpdateQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /event-live-updates/count} : count all the eventLiveUpdates.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEventLiveUpdates(EventLiveUpdateCriteria criteria) {
        LOG.debug("REST request to count EventLiveUpdates by criteria: {}", criteria);
        return ResponseEntity.ok().body(eventLiveUpdateQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /event-live-updates/:id} : get the "id" eventLiveUpdate.
     *
     * @param id the id of the eventLiveUpdateDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventLiveUpdateDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventLiveUpdateDTO> getEventLiveUpdate(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EventLiveUpdate : {}", id);
        Optional<EventLiveUpdateDTO> eventLiveUpdateDTO = eventLiveUpdateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventLiveUpdateDTO);
    }

    /**
     * {@code DELETE  /event-live-updates/:id} : delete the "id" eventLiveUpdate.
     *
     * @param id the id of the eventLiveUpdateDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventLiveUpdate(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EventLiveUpdate : {}", id);
        eventLiveUpdateService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
