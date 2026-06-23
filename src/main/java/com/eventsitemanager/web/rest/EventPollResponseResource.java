package com.eventsitemanager.web.rest;

import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.EventPollResponseRepository;
import com.eventsitemanager.service.EventPollResponseQueryService;
import com.eventsitemanager.service.EventPollResponseService;
import com.eventsitemanager.service.criteria.EventPollResponseCriteria;
import com.eventsitemanager.service.dto.EventPollResponseDTO;
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
 * REST controller for managing {@link com.eventsitemanager.domain.EventPollResponse}.
 */
@RestController
@RequestMapping("/api/event-poll-responses")
public class EventPollResponseResource {

    private final Logger log = LoggerFactory.getLogger(EventPollResponseResource.class);

    private static final String ENTITY_NAME = "eventPollResponse";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventPollResponseService eventPollResponseService;

    private final EventPollResponseRepository eventPollResponseRepository;

    private final EventPollResponseQueryService eventPollResponseQueryService;

    public EventPollResponseResource(
        EventPollResponseService eventPollResponseService,
        EventPollResponseRepository eventPollResponseRepository,
        EventPollResponseQueryService eventPollResponseQueryService
    ) {
        this.eventPollResponseService = eventPollResponseService;
        this.eventPollResponseRepository = eventPollResponseRepository;
        this.eventPollResponseQueryService = eventPollResponseQueryService;
    }

    /**
     * {@code POST  /event-poll-responses} : Create a new eventPollResponse.
     *
     * @param eventPollResponseDTO the eventPollResponseDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventPollResponseDTO, or with status {@code 400 (Bad Request)} if the eventPollResponse has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EventPollResponseDTO> createEventPollResponse(@Valid @RequestBody EventPollResponseDTO eventPollResponseDTO)
        throws URISyntaxException {
        log.debug("REST request to save EventPollResponse : {}", eventPollResponseDTO);
        if (eventPollResponseDTO.getId() != null) {
            throw new BadRequestAlertException("A new eventPollResponse cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventPollResponseDTO result = eventPollResponseService.save(eventPollResponseDTO);
        return ResponseEntity
            .created(new URI("/api/event-poll-responses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /event-poll-responses/:id} : Updates an existing eventPollResponse.
     *
     * @param id the id of the eventPollResponseDTO to save.
     * @param eventPollResponseDTO the eventPollResponseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventPollResponseDTO,
     * or with status {@code 400 (Bad Request)} if the eventPollResponseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventPollResponseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventPollResponseDTO> updateEventPollResponse(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EventPollResponseDTO eventPollResponseDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EventPollResponse : {}, {}", id, eventPollResponseDTO);
        if (eventPollResponseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventPollResponseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventPollResponseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EventPollResponseDTO result = eventPollResponseService.update(eventPollResponseDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventPollResponseDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /event-poll-responses/:id} : Partial updates given fields of an existing eventPollResponse, field will ignore if it is null
     *
     * @param id the id of the eventPollResponseDTO to save.
     * @param eventPollResponseDTO the eventPollResponseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventPollResponseDTO,
     * or with status {@code 400 (Bad Request)} if the eventPollResponseDTO is not valid,
     * or with status {@code 404 (Not Found)} if the eventPollResponseDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the eventPollResponseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventPollResponseDTO> partialUpdateEventPollResponse(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EventPollResponseDTO eventPollResponseDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EventPollResponse partially : {}, {}", id, eventPollResponseDTO);
        if (eventPollResponseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventPollResponseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventPollResponseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EventPollResponseDTO> result = eventPollResponseService.partialUpdate(eventPollResponseDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventPollResponseDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /event-poll-responses} : get all the eventPollResponses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eventPollResponses in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EventPollResponseDTO>> getAllEventPollResponses(
        EventPollResponseCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get EventPollResponses by criteria: {}", criteria);

        Page<EventPollResponseDTO> page = eventPollResponseQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /event-poll-responses/count} : count all the eventPollResponses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEventPollResponses(EventPollResponseCriteria criteria) {
        log.debug("REST request to count EventPollResponses by criteria: {}", criteria);
        return ResponseEntity.ok().body(eventPollResponseQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /event-poll-responses/:id} : get the "id" eventPollResponse.
     *
     * @param id the id of the eventPollResponseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventPollResponseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventPollResponseDTO> getEventPollResponse(@PathVariable Long id) {
        log.debug("REST request to get EventPollResponse : {}", id);
        Optional<EventPollResponseDTO> eventPollResponseDTO = eventPollResponseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventPollResponseDTO);
    }

    /**
     * {@code DELETE  /event-poll-responses/:id} : delete the "id" eventPollResponse.
     *
     * @param id the id of the eventPollResponseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventPollResponse(@PathVariable Long id) {
        log.debug("REST request to delete EventPollResponse : {}", id);
        eventPollResponseService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
