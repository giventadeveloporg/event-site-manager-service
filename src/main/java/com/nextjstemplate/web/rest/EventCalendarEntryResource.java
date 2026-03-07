package com.nextjstemplate.web.rest;

import com.nextjstemplate.errors.BadRequestAlertException;
import com.nextjstemplate.repository.EventCalendarEntryRepository;
import com.nextjstemplate.service.EventCalendarEntryQueryService;
import com.nextjstemplate.service.EventCalendarEntryService;
import com.nextjstemplate.service.criteria.EventCalendarEntryCriteria;
import com.nextjstemplate.service.dto.EventCalendarEntryDTO;
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
 * REST controller for managing {@link com.nextjstemplate.domain.EventCalendarEntry}.
 */
@RestController
@RequestMapping("/api/event-calendar-entries")
public class EventCalendarEntryResource {

    private final Logger log = LoggerFactory.getLogger(EventCalendarEntryResource.class);

    private static final String ENTITY_NAME = "eventCalendarEntry";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventCalendarEntryService eventCalendarEntryService;

    private final EventCalendarEntryRepository eventCalendarEntryRepository;

    private final EventCalendarEntryQueryService eventCalendarEntryQueryService;

    public EventCalendarEntryResource(
        EventCalendarEntryService eventCalendarEntryService,
        EventCalendarEntryRepository eventCalendarEntryRepository,
        EventCalendarEntryQueryService eventCalendarEntryQueryService
    ) {
        this.eventCalendarEntryService = eventCalendarEntryService;
        this.eventCalendarEntryRepository = eventCalendarEntryRepository;
        this.eventCalendarEntryQueryService = eventCalendarEntryQueryService;
    }

    /**
     * {@code POST  /event-calendar-entries} : Create a new eventCalendarEntry.
     *
     * @param eventCalendarEntryDTO the eventCalendarEntryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventCalendarEntryDTO, or with status {@code 400 (Bad Request)} if the eventCalendarEntry has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EventCalendarEntryDTO> createEventCalendarEntry(@Valid @RequestBody EventCalendarEntryDTO eventCalendarEntryDTO)
        throws URISyntaxException {
        log.debug("REST request to save EventCalendarEntry : {}", eventCalendarEntryDTO);
        if (eventCalendarEntryDTO.getId() != null) {
            throw new BadRequestAlertException("A new eventCalendarEntry cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventCalendarEntryDTO result = eventCalendarEntryService.save(eventCalendarEntryDTO);
        return ResponseEntity
            .created(new URI("/api/event-calendar-entries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /event-calendar-entries/:id} : Updates an existing eventCalendarEntry.
     *
     * @param id the id of the eventCalendarEntryDTO to save.
     * @param eventCalendarEntryDTO the eventCalendarEntryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventCalendarEntryDTO,
     * or with status {@code 400 (Bad Request)} if the eventCalendarEntryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventCalendarEntryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventCalendarEntryDTO> updateEventCalendarEntry(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EventCalendarEntryDTO eventCalendarEntryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EventCalendarEntry : {}, {}", id, eventCalendarEntryDTO);
        if (eventCalendarEntryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventCalendarEntryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventCalendarEntryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EventCalendarEntryDTO result = eventCalendarEntryService.update(eventCalendarEntryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventCalendarEntryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /event-calendar-entries/:id} : Partial updates given fields of an existing eventCalendarEntry, field will ignore if it is null
     *
     * @param id the id of the eventCalendarEntryDTO to save.
     * @param eventCalendarEntryDTO the eventCalendarEntryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventCalendarEntryDTO,
     * or with status {@code 400 (Bad Request)} if the eventCalendarEntryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the eventCalendarEntryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the eventCalendarEntryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventCalendarEntryDTO> partialUpdateEventCalendarEntry(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EventCalendarEntryDTO eventCalendarEntryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EventCalendarEntry partially : {}, {}", id, eventCalendarEntryDTO);
        if (eventCalendarEntryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventCalendarEntryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventCalendarEntryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EventCalendarEntryDTO> result = eventCalendarEntryService.partialUpdate(eventCalendarEntryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventCalendarEntryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /event-calendar-entries} : get all the eventCalendarEntries.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eventCalendarEntries in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EventCalendarEntryDTO>> getAllEventCalendarEntries(
        EventCalendarEntryCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get EventCalendarEntries by criteria: {}", criteria);

        Page<EventCalendarEntryDTO> page = eventCalendarEntryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /event-calendar-entries/count} : count all the eventCalendarEntries.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEventCalendarEntries(EventCalendarEntryCriteria criteria) {
        log.debug("REST request to count EventCalendarEntries by criteria: {}", criteria);
        return ResponseEntity.ok().body(eventCalendarEntryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /event-calendar-entries/:id} : get the "id" eventCalendarEntry.
     *
     * @param id the id of the eventCalendarEntryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventCalendarEntryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventCalendarEntryDTO> getEventCalendarEntry(@PathVariable Long id) {
        log.debug("REST request to get EventCalendarEntry : {}", id);
        Optional<EventCalendarEntryDTO> eventCalendarEntryDTO = eventCalendarEntryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventCalendarEntryDTO);
    }

    /**
     * {@code DELETE  /event-calendar-entries/:id} : delete the "id" eventCalendarEntry.
     *
     * @param id the id of the eventCalendarEntryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventCalendarEntry(@PathVariable Long id) {
        log.debug("REST request to delete EventCalendarEntry : {}", id);
        eventCalendarEntryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
