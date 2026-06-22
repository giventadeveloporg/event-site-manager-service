package com.eventsitemanager.web.rest;

import com.eventsitemanager.repository.EventScoreCardRepository;
import com.eventsitemanager.service.EventScoreCardQueryService;
import com.eventsitemanager.service.EventScoreCardService;
import com.eventsitemanager.service.criteria.EventScoreCardCriteria;
import com.eventsitemanager.service.dto.EventScoreCardDTO;
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
 * REST controller for managing {@link com.eventsitemanager.domain.EventScoreCard}.
 */
@RestController
@RequestMapping("/api/event-score-cards")
public class EventScoreCardResource {

    private final Logger log = LoggerFactory.getLogger(EventScoreCardResource.class);

    private static final String ENTITY_NAME = "eventScoreCard";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventScoreCardService eventScoreCardService;

    private final EventScoreCardRepository eventScoreCardRepository;

    private final EventScoreCardQueryService eventScoreCardQueryService;

    public EventScoreCardResource(
        EventScoreCardService eventScoreCardService,
        EventScoreCardRepository eventScoreCardRepository,
        EventScoreCardQueryService eventScoreCardQueryService
    ) {
        this.eventScoreCardService = eventScoreCardService;
        this.eventScoreCardRepository = eventScoreCardRepository;
        this.eventScoreCardQueryService = eventScoreCardQueryService;
    }

    /**
     * {@code POST  /event-score-cards} : Create a new eventScoreCard.
     *
     * @param eventScoreCardDTO the eventScoreCardDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventScoreCardDTO, or with status {@code 400 (Bad Request)} if the eventScoreCard has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EventScoreCardDTO> createEventScoreCard(@Valid @RequestBody EventScoreCardDTO eventScoreCardDTO)
        throws URISyntaxException {
        log.debug("REST request to save EventScoreCard : {}", eventScoreCardDTO);
        if (eventScoreCardDTO.getId() != null) {
            throw new BadRequestAlertException("A new eventScoreCard cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventScoreCardDTO result = eventScoreCardService.save(eventScoreCardDTO);
        return ResponseEntity
            .created(new URI("/api/event-score-cards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /event-score-cards/:id} : Updates an existing eventScoreCard.
     *
     * @param id the id of the eventScoreCardDTO to save.
     * @param eventScoreCardDTO the eventScoreCardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventScoreCardDTO,
     * or with status {@code 400 (Bad Request)} if the eventScoreCardDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventScoreCardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventScoreCardDTO> updateEventScoreCard(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EventScoreCardDTO eventScoreCardDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EventScoreCard : {}, {}", id, eventScoreCardDTO);
        if (eventScoreCardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventScoreCardDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventScoreCardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EventScoreCardDTO result = eventScoreCardService.update(eventScoreCardDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventScoreCardDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /event-score-cards/:id} : Partial updates given fields of an existing eventScoreCard, field will ignore if it is null
     *
     * @param id the id of the eventScoreCardDTO to save.
     * @param eventScoreCardDTO the eventScoreCardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventScoreCardDTO,
     * or with status {@code 400 (Bad Request)} if the eventScoreCardDTO is not valid,
     * or with status {@code 404 (Not Found)} if the eventScoreCardDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the eventScoreCardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventScoreCardDTO> partialUpdateEventScoreCard(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EventScoreCardDTO eventScoreCardDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EventScoreCard partially : {}, {}", id, eventScoreCardDTO);
        if (eventScoreCardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventScoreCardDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventScoreCardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EventScoreCardDTO> result = eventScoreCardService.partialUpdate(eventScoreCardDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventScoreCardDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /event-score-cards} : get all the eventScoreCards.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eventScoreCards in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EventScoreCardDTO>> getAllEventScoreCards(
        EventScoreCardCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get EventScoreCards by criteria: {}", criteria);

        Page<EventScoreCardDTO> page = eventScoreCardQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /event-score-cards/count} : count all the eventScoreCards.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEventScoreCards(EventScoreCardCriteria criteria) {
        log.debug("REST request to count EventScoreCards by criteria: {}", criteria);
        return ResponseEntity.ok().body(eventScoreCardQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /event-score-cards/:id} : get the "id" eventScoreCard.
     *
     * @param id the id of the eventScoreCardDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventScoreCardDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventScoreCardDTO> getEventScoreCard(@PathVariable Long id) {
        log.debug("REST request to get EventScoreCard : {}", id);
        Optional<EventScoreCardDTO> eventScoreCardDTO = eventScoreCardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventScoreCardDTO);
    }

    /**
     * {@code DELETE  /event-score-cards/:id} : delete the "id" eventScoreCard.
     *
     * @param id the id of the eventScoreCardDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventScoreCard(@PathVariable Long id) {
        log.debug("REST request to delete EventScoreCard : {}", id);
        eventScoreCardService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
