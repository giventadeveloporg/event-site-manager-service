package com.nextjstemplate.web.rest;

import com.nextjstemplate.errors.BadRequestAlertException;
import com.nextjstemplate.repository.EventTypeDetailsRepository;
import com.nextjstemplate.service.EventTypeDetailsQueryService;
import com.nextjstemplate.service.EventTypeDetailsService;
import com.nextjstemplate.service.criteria.EventTypeDetailsCriteria;
import com.nextjstemplate.service.dto.EventTypeDetailsDTO;
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
 * REST controller for managing {@link com.nextjstemplate.domain.EventTypeDetails}.
 */
@RestController
@RequestMapping("/api/event-type-details")
public class EventTypeDetailsResource {

    private final Logger log = LoggerFactory.getLogger(EventTypeDetailsResource.class);

    private static final String ENTITY_NAME = "eventTypeDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventTypeDetailsService eventTypeDetailsService;

    private final EventTypeDetailsRepository eventTypeDetailsRepository;

    private final EventTypeDetailsQueryService eventTypeDetailsQueryService;

    public EventTypeDetailsResource(
        EventTypeDetailsService eventTypeDetailsService,
        EventTypeDetailsRepository eventTypeDetailsRepository,
        EventTypeDetailsQueryService eventTypeDetailsQueryService
    ) {
        this.eventTypeDetailsService = eventTypeDetailsService;
        this.eventTypeDetailsRepository = eventTypeDetailsRepository;
        this.eventTypeDetailsQueryService = eventTypeDetailsQueryService;
    }

    /**
     * {@code POST  /event-type-details} : Create a new eventTypeDetails.
     *
     * @param eventTypeDetailsDTO the eventTypeDetailsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventTypeDetailsDTO, or with status {@code 400 (Bad Request)} if the eventTypeDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EventTypeDetailsDTO> createEventTypeDetails(@Valid @RequestBody EventTypeDetailsDTO eventTypeDetailsDTO)
        throws URISyntaxException {
        log.debug("REST request to save EventTypeDetails : {}", eventTypeDetailsDTO);
        if (eventTypeDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new eventTypeDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventTypeDetailsDTO result = eventTypeDetailsService.save(eventTypeDetailsDTO);
        return ResponseEntity
            .created(new URI("/api/event-type-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /event-type-details/:id} : Updates an existing eventTypeDetails.
     *
     * @param id the id of the eventTypeDetailsDTO to save.
     * @param eventTypeDetailsDTO the eventTypeDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventTypeDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the eventTypeDetailsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventTypeDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventTypeDetailsDTO> updateEventTypeDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EventTypeDetailsDTO eventTypeDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EventTypeDetails : {}, {}", id, eventTypeDetailsDTO);
        if (eventTypeDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventTypeDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventTypeDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EventTypeDetailsDTO result = eventTypeDetailsService.update(eventTypeDetailsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventTypeDetailsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /event-type-details/:id} : Partial updates given fields of an existing eventTypeDetails, field will ignore if it is null
     *
     * @param id the id of the eventTypeDetailsDTO to save.
     * @param eventTypeDetailsDTO the eventTypeDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventTypeDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the eventTypeDetailsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the eventTypeDetailsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the eventTypeDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventTypeDetailsDTO> partialUpdateEventTypeDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EventTypeDetailsDTO eventTypeDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EventTypeDetails partially : {}, {}", id, eventTypeDetailsDTO);
        if (eventTypeDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventTypeDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventTypeDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EventTypeDetailsDTO> result = eventTypeDetailsService.partialUpdate(eventTypeDetailsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventTypeDetailsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /event-type-details} : get all the eventTypeDetails.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eventTypeDetails in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EventTypeDetailsDTO>> getAllEventTypeDetails(
        EventTypeDetailsCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get EventTypeDetails by criteria: {}", criteria);
        // just to handle all the all retrieve all rows for event types..
        criteria.setTenantId(null);
        Page<EventTypeDetailsDTO> page = eventTypeDetailsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /event-type-details/count} : count all the eventTypeDetails.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEventTypeDetails(EventTypeDetailsCriteria criteria) {
        log.debug("REST request to count EventTypeDetails by criteria: {}", criteria);
        return ResponseEntity.ok().body(eventTypeDetailsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /event-type-details/:id} : get the "id" eventTypeDetails.
     *
     * @param id the id of the eventTypeDetailsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventTypeDetailsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventTypeDetailsDTO> getEventTypeDetails(@PathVariable Long id) {
        log.debug("REST request to get EventTypeDetails : {}", id);
        Optional<EventTypeDetailsDTO> eventTypeDetailsDTO = eventTypeDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventTypeDetailsDTO);
    }

    /**
     * {@code DELETE  /event-type-details/:id} : delete the "id" eventTypeDetails.
     *
     * @param id the id of the eventTypeDetailsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventTypeDetails(@PathVariable Long id) {
        log.debug("REST request to delete EventTypeDetails : {}", id);
        eventTypeDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
