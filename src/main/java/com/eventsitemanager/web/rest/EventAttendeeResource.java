package com.eventsitemanager.web.rest;

import com.eventsitemanager.repository.EventAttendeeRepository;
import com.eventsitemanager.service.EventAttendeeQueryService;
import com.eventsitemanager.service.EventAttendeeService;
import com.eventsitemanager.service.criteria.EventAttendeeCriteria;
import com.eventsitemanager.service.dto.EventAttendeeDTO;
import com.eventsitemanager.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.eventsitemanager.domain.EventAttendee}.
 */
@RestController
@RequestMapping("/api/event-attendees")
public class EventAttendeeResource {

    private final Logger log = LoggerFactory.getLogger(EventAttendeeResource.class);

    private static final String ENTITY_NAME = "eventAttendee";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventAttendeeService eventAttendeeService;

    private final EventAttendeeRepository eventAttendeeRepository;

    private final EventAttendeeQueryService eventAttendeeQueryService;

    public EventAttendeeResource(
        EventAttendeeService eventAttendeeService,
        EventAttendeeRepository eventAttendeeRepository,
        EventAttendeeQueryService eventAttendeeQueryService
    ) {
        this.eventAttendeeService = eventAttendeeService;
        this.eventAttendeeRepository = eventAttendeeRepository;
        this.eventAttendeeQueryService = eventAttendeeQueryService;
    }

    /**
     * {@code POST  /event-attendees} : Create a new eventAttendee.
     *
     * @param eventAttendeeDTO the eventAttendeeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventAttendeeDTO, or with status {@code 400 (Bad Request)} if the eventAttendee has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EventAttendeeDTO> createEventAttendee(@Valid @RequestBody EventAttendeeDTO eventAttendeeDTO)
        throws URISyntaxException {
        log.debug("REST request to save EventAttendee : {}", eventAttendeeDTO);
        if (eventAttendeeDTO.getId() != null) {
            throw new BadRequestAlertException("A new eventAttendee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventAttendeeDTO result = eventAttendeeService.save(eventAttendeeDTO);
        return ResponseEntity
            .created(new URI("/api/event-attendees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /event-attendees/:id} : Updates an existing eventAttendee.
     *
     * @param id the id of the eventAttendeeDTO to save.
     * @param eventAttendeeDTO the eventAttendeeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventAttendeeDTO,
     * or with status {@code 400 (Bad Request)} if the eventAttendeeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventAttendeeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventAttendeeDTO> updateEventAttendee(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EventAttendeeDTO eventAttendeeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EventAttendee : {}, {}", id, eventAttendeeDTO);
        if (eventAttendeeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventAttendeeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventAttendeeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EventAttendeeDTO result = eventAttendeeService.update(eventAttendeeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventAttendeeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /event-attendees/:id} : Partial updates given fields of an existing eventAttendee, field will ignore if it is null
     *
     * @param id the id of the eventAttendeeDTO to save.
     * @param eventAttendeeDTO the eventAttendeeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventAttendeeDTO,
     * or with status {@code 400 (Bad Request)} if the eventAttendeeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the eventAttendeeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the eventAttendeeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventAttendeeDTO> partialUpdateEventAttendee(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EventAttendeeDTO eventAttendeeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EventAttendee partially : {}, {}", id, eventAttendeeDTO);
        if (eventAttendeeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventAttendeeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventAttendeeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EventAttendeeDTO> result = eventAttendeeService.partialUpdate(eventAttendeeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventAttendeeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /event-attendees} : get all the eventAttendees.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eventAttendees in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EventAttendeeDTO>> getAllEventAttendees(
        EventAttendeeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get EventAttendees by criteria: {}", criteria);

        Page<EventAttendeeDTO> page = eventAttendeeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /event-attendees/count} : count all the eventAttendees.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEventAttendees(EventAttendeeCriteria criteria) {
        log.debug("REST request to count EventAttendees by criteria: {}", criteria);
        return ResponseEntity.ok().body(eventAttendeeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /event-attendees/:id} : get the "id" eventAttendee.
     *
     * @param id the id of the eventAttendeeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventAttendeeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventAttendeeDTO> getEventAttendee(@PathVariable Long id) {
        log.debug("REST request to get EventAttendee : {}", id);
        Optional<EventAttendeeDTO> eventAttendeeDTO = eventAttendeeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventAttendeeDTO);
    }

    /**
     * {@code DELETE  /event-attendees/:id} : delete the "id" eventAttendee.
     *
     * @param id the id of the eventAttendeeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventAttendee(@PathVariable Long id) {
        log.debug("REST request to delete EventAttendee : {}", id);
        eventAttendeeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
