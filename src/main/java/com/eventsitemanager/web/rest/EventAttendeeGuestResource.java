package com.eventsitemanager.web.rest;

import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.EventAttendeeGuestRepository;
import com.eventsitemanager.service.EventAttendeeGuestQueryService;
import com.eventsitemanager.service.EventAttendeeGuestService;
import com.eventsitemanager.service.criteria.EventAttendeeGuestCriteria;
import com.eventsitemanager.service.dto.EventAttendeeGuestDTO;
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
 * REST controller for managing {@link com.eventsitemanager.domain.EventAttendeeGuest}.
 */
@RestController
@RequestMapping("/api/event-attendee-guests")
public class EventAttendeeGuestResource {

    private final Logger log = LoggerFactory.getLogger(EventAttendeeGuestResource.class);

    private static final String ENTITY_NAME = "eventAttendeeGuest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventAttendeeGuestService eventAttendeeGuestService;

    private final EventAttendeeGuestRepository eventAttendeeGuestRepository;

    private final EventAttendeeGuestQueryService eventAttendeeGuestQueryService;

    public EventAttendeeGuestResource(
        EventAttendeeGuestService eventAttendeeGuestService,
        EventAttendeeGuestRepository eventAttendeeGuestRepository,
        EventAttendeeGuestQueryService eventAttendeeGuestQueryService
    ) {
        this.eventAttendeeGuestService = eventAttendeeGuestService;
        this.eventAttendeeGuestRepository = eventAttendeeGuestRepository;
        this.eventAttendeeGuestQueryService = eventAttendeeGuestQueryService;
    }

    /**
     * {@code POST  /event-attendee-guests} : Create a new eventAttendeeGuest.
     *
     * @param eventAttendeeGuestDTO the eventAttendeeGuestDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventAttendeeGuestDTO, or with status {@code 400 (Bad Request)} if the eventAttendeeGuest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EventAttendeeGuestDTO> createEventAttendeeGuest(@Valid @RequestBody EventAttendeeGuestDTO eventAttendeeGuestDTO)
        throws URISyntaxException {
        log.debug("REST request to save EventAttendeeGuest : {}", eventAttendeeGuestDTO);
        if (eventAttendeeGuestDTO.getId() != null) {
            throw new BadRequestAlertException("A new eventAttendeeGuest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventAttendeeGuestDTO result = eventAttendeeGuestService.save(eventAttendeeGuestDTO);
        return ResponseEntity
            .created(new URI("/api/event-attendee-guests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /event-attendee-guests/:id} : Updates an existing eventAttendeeGuest.
     *
     * @param id the id of the eventAttendeeGuestDTO to save.
     * @param eventAttendeeGuestDTO the eventAttendeeGuestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventAttendeeGuestDTO,
     * or with status {@code 400 (Bad Request)} if the eventAttendeeGuestDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventAttendeeGuestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventAttendeeGuestDTO> updateEventAttendeeGuest(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EventAttendeeGuestDTO eventAttendeeGuestDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EventAttendeeGuest : {}, {}", id, eventAttendeeGuestDTO);
        if (eventAttendeeGuestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventAttendeeGuestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventAttendeeGuestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EventAttendeeGuestDTO result = eventAttendeeGuestService.update(eventAttendeeGuestDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventAttendeeGuestDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /event-attendee-guests/:id} : Partial updates given fields of an existing eventAttendeeGuest, field will ignore if it is null
     *
     * @param id the id of the eventAttendeeGuestDTO to save.
     * @param eventAttendeeGuestDTO the eventAttendeeGuestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventAttendeeGuestDTO,
     * or with status {@code 400 (Bad Request)} if the eventAttendeeGuestDTO is not valid,
     * or with status {@code 404 (Not Found)} if the eventAttendeeGuestDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the eventAttendeeGuestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventAttendeeGuestDTO> partialUpdateEventAttendeeGuest(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EventAttendeeGuestDTO eventAttendeeGuestDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EventAttendeeGuest partially : {}, {}", id, eventAttendeeGuestDTO);
        if (eventAttendeeGuestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventAttendeeGuestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventAttendeeGuestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EventAttendeeGuestDTO> result = eventAttendeeGuestService.partialUpdate(eventAttendeeGuestDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventAttendeeGuestDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /event-attendee-guests} : get all the eventAttendeeGuests.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eventAttendeeGuests in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EventAttendeeGuestDTO>> getAllEventAttendeeGuests(
        EventAttendeeGuestCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get EventAttendeeGuests by criteria: {}", criteria);

        Page<EventAttendeeGuestDTO> page = eventAttendeeGuestQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /event-attendee-guests/count} : count all the eventAttendeeGuests.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEventAttendeeGuests(EventAttendeeGuestCriteria criteria) {
        log.debug("REST request to count EventAttendeeGuests by criteria: {}", criteria);
        return ResponseEntity.ok().body(eventAttendeeGuestQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /event-attendee-guests/:id} : get the "id" eventAttendeeGuest.
     *
     * @param id the id of the eventAttendeeGuestDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventAttendeeGuestDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventAttendeeGuestDTO> getEventAttendeeGuest(@PathVariable Long id) {
        log.debug("REST request to get EventAttendeeGuest : {}", id);
        Optional<EventAttendeeGuestDTO> eventAttendeeGuestDTO = eventAttendeeGuestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventAttendeeGuestDTO);
    }

    /**
     * {@code DELETE  /event-attendee-guests/:id} : delete the "id" eventAttendeeGuest.
     *
     * @param id the id of the eventAttendeeGuestDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventAttendeeGuest(@PathVariable Long id) {
        log.debug("REST request to delete EventAttendeeGuest : {}", id);
        eventAttendeeGuestService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
