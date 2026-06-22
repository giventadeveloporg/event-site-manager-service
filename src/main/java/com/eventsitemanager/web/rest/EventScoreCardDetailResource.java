package com.eventsitemanager.web.rest;

import com.eventsitemanager.repository.EventScoreCardDetailRepository;
import com.eventsitemanager.service.EventScoreCardDetailQueryService;
import com.eventsitemanager.service.EventScoreCardDetailService;
import com.eventsitemanager.service.criteria.EventScoreCardDetailCriteria;
import com.eventsitemanager.service.dto.EventScoreCardDetailDTO;
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
 * REST controller for managing {@link com.eventsitemanager.domain.EventScoreCardDetail}.
 */
@RestController
@RequestMapping("/api/event-score-card-details")
public class EventScoreCardDetailResource {

    private final Logger log = LoggerFactory.getLogger(EventScoreCardDetailResource.class);

    private static final String ENTITY_NAME = "eventScoreCardDetail";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventScoreCardDetailService eventScoreCardDetailService;

    private final EventScoreCardDetailRepository eventScoreCardDetailRepository;

    private final EventScoreCardDetailQueryService eventScoreCardDetailQueryService;

    public EventScoreCardDetailResource(
        EventScoreCardDetailService eventScoreCardDetailService,
        EventScoreCardDetailRepository eventScoreCardDetailRepository,
        EventScoreCardDetailQueryService eventScoreCardDetailQueryService
    ) {
        this.eventScoreCardDetailService = eventScoreCardDetailService;
        this.eventScoreCardDetailRepository = eventScoreCardDetailRepository;
        this.eventScoreCardDetailQueryService = eventScoreCardDetailQueryService;
    }

    /**
     * {@code POST  /event-score-card-details} : Create a new eventScoreCardDetail.
     *
     * @param eventScoreCardDetailDTO the eventScoreCardDetailDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventScoreCardDetailDTO, or with status {@code 400 (Bad Request)} if the eventScoreCardDetail has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EventScoreCardDetailDTO> createEventScoreCardDetail(
        @Valid @RequestBody EventScoreCardDetailDTO eventScoreCardDetailDTO
    ) throws URISyntaxException {
        log.debug("REST request to save EventScoreCardDetail : {}", eventScoreCardDetailDTO);
        if (eventScoreCardDetailDTO.getId() != null) {
            throw new BadRequestAlertException("A new eventScoreCardDetail cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventScoreCardDetailDTO result = eventScoreCardDetailService.save(eventScoreCardDetailDTO);
        return ResponseEntity
            .created(new URI("/api/event-score-card-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /event-score-card-details/:id} : Updates an existing eventScoreCardDetail.
     *
     * @param id the id of the eventScoreCardDetailDTO to save.
     * @param eventScoreCardDetailDTO the eventScoreCardDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventScoreCardDetailDTO,
     * or with status {@code 400 (Bad Request)} if the eventScoreCardDetailDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventScoreCardDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventScoreCardDetailDTO> updateEventScoreCardDetail(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EventScoreCardDetailDTO eventScoreCardDetailDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EventScoreCardDetail : {}, {}", id, eventScoreCardDetailDTO);
        if (eventScoreCardDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventScoreCardDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventScoreCardDetailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EventScoreCardDetailDTO result = eventScoreCardDetailService.update(eventScoreCardDetailDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventScoreCardDetailDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /event-score-card-details/:id} : Partial updates given fields of an existing eventScoreCardDetail, field will ignore if it is null
     *
     * @param id the id of the eventScoreCardDetailDTO to save.
     * @param eventScoreCardDetailDTO the eventScoreCardDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventScoreCardDetailDTO,
     * or with status {@code 400 (Bad Request)} if the eventScoreCardDetailDTO is not valid,
     * or with status {@code 404 (Not Found)} if the eventScoreCardDetailDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the eventScoreCardDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventScoreCardDetailDTO> partialUpdateEventScoreCardDetail(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EventScoreCardDetailDTO eventScoreCardDetailDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EventScoreCardDetail partially : {}, {}", id, eventScoreCardDetailDTO);
        if (eventScoreCardDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventScoreCardDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventScoreCardDetailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EventScoreCardDetailDTO> result = eventScoreCardDetailService.partialUpdate(eventScoreCardDetailDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventScoreCardDetailDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /event-score-card-details} : get all the eventScoreCardDetails.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eventScoreCardDetails in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EventScoreCardDetailDTO>> getAllEventScoreCardDetails(
        EventScoreCardDetailCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get EventScoreCardDetails by criteria: {}", criteria);

        Page<EventScoreCardDetailDTO> page = eventScoreCardDetailQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /event-score-card-details/count} : count all the eventScoreCardDetails.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEventScoreCardDetails(EventScoreCardDetailCriteria criteria) {
        log.debug("REST request to count EventScoreCardDetails by criteria: {}", criteria);
        return ResponseEntity.ok().body(eventScoreCardDetailQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /event-score-card-details/:id} : get the "id" eventScoreCardDetail.
     *
     * @param id the id of the eventScoreCardDetailDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventScoreCardDetailDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventScoreCardDetailDTO> getEventScoreCardDetail(@PathVariable Long id) {
        log.debug("REST request to get EventScoreCardDetail : {}", id);
        Optional<EventScoreCardDetailDTO> eventScoreCardDetailDTO = eventScoreCardDetailService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventScoreCardDetailDTO);
    }

    /**
     * {@code DELETE  /event-score-card-details/:id} : delete the "id" eventScoreCardDetail.
     *
     * @param id the id of the eventScoreCardDetailDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventScoreCardDetail(@PathVariable Long id) {
        log.debug("REST request to delete EventScoreCardDetail : {}", id);
        eventScoreCardDetailService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
