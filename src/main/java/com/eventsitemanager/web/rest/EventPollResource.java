package com.eventsitemanager.web.rest;

import com.eventsitemanager.repository.EventPollRepository;
import com.eventsitemanager.service.EventPollQueryService;
import com.eventsitemanager.service.EventPollService;
import com.eventsitemanager.service.criteria.EventPollCriteria;
import com.eventsitemanager.service.dto.EventPollDTO;
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
 * REST controller for managing {@link com.eventsitemanager.domain.EventPoll}.
 */
@RestController
@RequestMapping("/api/event-polls")
public class EventPollResource {

    private final Logger log = LoggerFactory.getLogger(EventPollResource.class);

    private static final String ENTITY_NAME = "eventPoll";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventPollService eventPollService;

    private final EventPollRepository eventPollRepository;

    private final EventPollQueryService eventPollQueryService;

    public EventPollResource(
        EventPollService eventPollService,
        EventPollRepository eventPollRepository,
        EventPollQueryService eventPollQueryService
    ) {
        this.eventPollService = eventPollService;
        this.eventPollRepository = eventPollRepository;
        this.eventPollQueryService = eventPollQueryService;
    }

    /**
     * {@code POST  /event-polls} : Create a new eventPoll.
     *
     * @param eventPollDTO the eventPollDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventPollDTO, or with status {@code 400 (Bad Request)} if the eventPoll has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EventPollDTO> createEventPoll(@Valid @RequestBody EventPollDTO eventPollDTO) throws URISyntaxException {
        log.debug("REST request to save EventPoll : {}", eventPollDTO);
        if (eventPollDTO.getId() != null) {
            throw new BadRequestAlertException("A new eventPoll cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventPollDTO result = eventPollService.save(eventPollDTO);
        return ResponseEntity
            .created(new URI("/api/event-polls/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /event-polls/:id} : Updates an existing eventPoll.
     *
     * @param id the id of the eventPollDTO to save.
     * @param eventPollDTO the eventPollDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventPollDTO,
     * or with status {@code 400 (Bad Request)} if the eventPollDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventPollDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventPollDTO> updateEventPoll(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EventPollDTO eventPollDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EventPoll : {}, {}", id, eventPollDTO);
        if (eventPollDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventPollDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventPollRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EventPollDTO result = eventPollService.update(eventPollDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventPollDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /event-polls/:id} : Partial updates given fields of an existing eventPoll, field will ignore if it is null
     *
     * @param id the id of the eventPollDTO to save.
     * @param eventPollDTO the eventPollDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventPollDTO,
     * or with status {@code 400 (Bad Request)} if the eventPollDTO is not valid,
     * or with status {@code 404 (Not Found)} if the eventPollDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the eventPollDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventPollDTO> partialUpdateEventPoll(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EventPollDTO eventPollDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EventPoll partially : {}, {}", id, eventPollDTO);
        if (eventPollDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventPollDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventPollRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EventPollDTO> result = eventPollService.partialUpdate(eventPollDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventPollDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /event-polls} : get all the eventPolls.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eventPolls in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EventPollDTO>> getAllEventPolls(
        EventPollCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get EventPolls by criteria: {}", criteria);

        Page<EventPollDTO> page = eventPollQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /event-polls/count} : count all the eventPolls.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEventPolls(EventPollCriteria criteria) {
        log.debug("REST request to count EventPolls by criteria: {}", criteria);
        return ResponseEntity.ok().body(eventPollQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /event-polls/:id} : get the "id" eventPoll.
     *
     * @param id the id of the eventPollDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventPollDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventPollDTO> getEventPoll(@PathVariable Long id) {
        log.debug("REST request to get EventPoll : {}", id);
        Optional<EventPollDTO> eventPollDTO = eventPollService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventPollDTO);
    }

    /**
     * {@code DELETE  /event-polls/:id} : delete the "id" eventPoll.
     *
     * @param id the id of the eventPollDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventPoll(@PathVariable Long id) {
        log.debug("REST request to delete EventPoll : {}", id);
        eventPollService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
