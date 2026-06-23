package com.eventsitemanager.web.rest;

import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.EventPollOptionRepository;
import com.eventsitemanager.service.EventPollOptionQueryService;
import com.eventsitemanager.service.EventPollOptionService;
import com.eventsitemanager.service.criteria.EventPollOptionCriteria;
import com.eventsitemanager.service.dto.EventPollOptionDTO;
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
 * REST controller for managing {@link com.eventsitemanager.domain.EventPollOption}.
 */
@RestController
@RequestMapping("/api/event-poll-options")
public class EventPollOptionResource {

    private final Logger log = LoggerFactory.getLogger(EventPollOptionResource.class);

    private static final String ENTITY_NAME = "eventPollOption";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventPollOptionService eventPollOptionService;

    private final EventPollOptionRepository eventPollOptionRepository;

    private final EventPollOptionQueryService eventPollOptionQueryService;

    public EventPollOptionResource(
        EventPollOptionService eventPollOptionService,
        EventPollOptionRepository eventPollOptionRepository,
        EventPollOptionQueryService eventPollOptionQueryService
    ) {
        this.eventPollOptionService = eventPollOptionService;
        this.eventPollOptionRepository = eventPollOptionRepository;
        this.eventPollOptionQueryService = eventPollOptionQueryService;
    }

    /**
     * {@code POST  /event-poll-options} : Create a new eventPollOption.
     *
     * @param eventPollOptionDTO the eventPollOptionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventPollOptionDTO, or with status {@code 400 (Bad Request)} if the eventPollOption has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EventPollOptionDTO> createEventPollOption(@Valid @RequestBody EventPollOptionDTO eventPollOptionDTO)
        throws URISyntaxException {
        log.debug("REST request to save EventPollOption : {}", eventPollOptionDTO);
        if (eventPollOptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new eventPollOption cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventPollOptionDTO result = eventPollOptionService.save(eventPollOptionDTO);
        return ResponseEntity
            .created(new URI("/api/event-poll-options/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /event-poll-options/:id} : Updates an existing eventPollOption.
     *
     * @param id the id of the eventPollOptionDTO to save.
     * @param eventPollOptionDTO the eventPollOptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventPollOptionDTO,
     * or with status {@code 400 (Bad Request)} if the eventPollOptionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventPollOptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventPollOptionDTO> updateEventPollOption(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EventPollOptionDTO eventPollOptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EventPollOption : {}, {}", id, eventPollOptionDTO);
        if (eventPollOptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventPollOptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventPollOptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EventPollOptionDTO result = eventPollOptionService.update(eventPollOptionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventPollOptionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /event-poll-options/:id} : Partial updates given fields of an existing eventPollOption, field will ignore if it is null
     *
     * @param id the id of the eventPollOptionDTO to save.
     * @param eventPollOptionDTO the eventPollOptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventPollOptionDTO,
     * or with status {@code 400 (Bad Request)} if the eventPollOptionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the eventPollOptionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the eventPollOptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventPollOptionDTO> partialUpdateEventPollOption(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EventPollOptionDTO eventPollOptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EventPollOption partially : {}, {}", id, eventPollOptionDTO);
        if (eventPollOptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventPollOptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventPollOptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EventPollOptionDTO> result = eventPollOptionService.partialUpdate(eventPollOptionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventPollOptionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /event-poll-options} : get all the eventPollOptions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eventPollOptions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EventPollOptionDTO>> getAllEventPollOptions(
        EventPollOptionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get EventPollOptions by criteria: {}", criteria);

        Page<EventPollOptionDTO> page = eventPollOptionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /event-poll-options/count} : count all the eventPollOptions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEventPollOptions(EventPollOptionCriteria criteria) {
        log.debug("REST request to count EventPollOptions by criteria: {}", criteria);
        return ResponseEntity.ok().body(eventPollOptionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /event-poll-options/:id} : get the "id" eventPollOption.
     *
     * @param id the id of the eventPollOptionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventPollOptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventPollOptionDTO> getEventPollOption(@PathVariable Long id) {
        log.debug("REST request to get EventPollOption : {}", id);
        Optional<EventPollOptionDTO> eventPollOptionDTO = eventPollOptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventPollOptionDTO);
    }

    /**
     * {@code DELETE  /event-poll-options/:id} : delete the "id" eventPollOption.
     *
     * @param id the id of the eventPollOptionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventPollOption(@PathVariable Long id) {
        log.debug("REST request to delete EventPollOption : {}", id);
        eventPollOptionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
