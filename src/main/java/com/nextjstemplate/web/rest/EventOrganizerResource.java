package com.nextjstemplate.web.rest;

import com.nextjstemplate.errors.BadRequestAlertException;
import com.nextjstemplate.repository.EventOrganizerRepository;
import com.nextjstemplate.service.EventOrganizerQueryService;
import com.nextjstemplate.service.EventOrganizerService;
import com.nextjstemplate.service.criteria.EventOrganizerCriteria;
import com.nextjstemplate.service.dto.EventOrganizerDTO;
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
 * REST controller for managing {@link com.nextjstemplate.domain.EventOrganizer}.
 */
@RestController
@RequestMapping("/api/event-organizers")
public class EventOrganizerResource {

    private final Logger log = LoggerFactory.getLogger(EventOrganizerResource.class);

    private static final String ENTITY_NAME = "eventOrganizer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventOrganizerService eventOrganizerService;

    private final EventOrganizerRepository eventOrganizerRepository;

    private final EventOrganizerQueryService eventOrganizerQueryService;

    public EventOrganizerResource(
        EventOrganizerService eventOrganizerService,
        EventOrganizerRepository eventOrganizerRepository,
        EventOrganizerQueryService eventOrganizerQueryService
    ) {
        this.eventOrganizerService = eventOrganizerService;
        this.eventOrganizerRepository = eventOrganizerRepository;
        this.eventOrganizerQueryService = eventOrganizerQueryService;
    }

    /**
     * {@code POST  /event-organizers} : Create a new eventOrganizer.
     *
     * @param eventOrganizerDTO the eventOrganizerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventOrganizerDTO, or with status {@code 400 (Bad Request)} if the eventOrganizer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EventOrganizerDTO> createEventOrganizer(@Valid @RequestBody EventOrganizerDTO eventOrganizerDTO)
        throws URISyntaxException {
        log.debug("REST request to save EventOrganizer : {}", eventOrganizerDTO);
        if (eventOrganizerDTO.getId() != null) {
            throw new BadRequestAlertException("A new eventOrganizer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventOrganizerDTO result = eventOrganizerService.save(eventOrganizerDTO);
        return ResponseEntity
            .created(new URI("/api/event-organizers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /event-organizers/:id} : Updates an existing eventOrganizer.
     *
     * @param id the id of the eventOrganizerDTO to save.
     * @param eventOrganizerDTO the eventOrganizerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventOrganizerDTO,
     * or with status {@code 400 (Bad Request)} if the eventOrganizerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventOrganizerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventOrganizerDTO> updateEventOrganizer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EventOrganizerDTO eventOrganizerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EventOrganizer : {}, {}", id, eventOrganizerDTO);
        if (eventOrganizerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventOrganizerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventOrganizerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EventOrganizerDTO result = eventOrganizerService.update(eventOrganizerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventOrganizerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /event-organizers/:id} : Partial updates given fields of an existing eventOrganizer, field will ignore if it is null
     *
     * @param id the id of the eventOrganizerDTO to save.
     * @param eventOrganizerDTO the eventOrganizerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventOrganizerDTO,
     * or with status {@code 400 (Bad Request)} if the eventOrganizerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the eventOrganizerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the eventOrganizerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventOrganizerDTO> partialUpdateEventOrganizer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EventOrganizerDTO eventOrganizerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EventOrganizer partially : {}, {}", id, eventOrganizerDTO);
        if (eventOrganizerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventOrganizerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventOrganizerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EventOrganizerDTO> result = eventOrganizerService.partialUpdate(eventOrganizerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventOrganizerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /event-organizers} : get all the eventOrganizers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eventOrganizers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EventOrganizerDTO>> getAllEventOrganizers(
        EventOrganizerCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get EventOrganizers by criteria: {}", criteria);

        Page<EventOrganizerDTO> page = eventOrganizerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /event-organizers/count} : count all the eventOrganizers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEventOrganizers(EventOrganizerCriteria criteria) {
        log.debug("REST request to count EventOrganizers by criteria: {}", criteria);
        return ResponseEntity.ok().body(eventOrganizerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /event-organizers/:id} : get the "id" eventOrganizer.
     *
     * @param id the id of the eventOrganizerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventOrganizerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventOrganizerDTO> getEventOrganizer(@PathVariable Long id) {
        log.debug("REST request to get EventOrganizer : {}", id);
        Optional<EventOrganizerDTO> eventOrganizerDTO = eventOrganizerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventOrganizerDTO);
    }

    /**
     * {@code DELETE  /event-organizers/:id} : delete the "id" eventOrganizer.
     *
     * @param id the id of the eventOrganizerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventOrganizer(@PathVariable Long id) {
        log.debug("REST request to delete EventOrganizer : {}", id);
        eventOrganizerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
