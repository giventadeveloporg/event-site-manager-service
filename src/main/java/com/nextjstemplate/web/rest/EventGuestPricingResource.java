package com.nextjstemplate.web.rest;

import com.nextjstemplate.errors.BadRequestAlertException;
import com.nextjstemplate.repository.EventGuestPricingRepository;
import com.nextjstemplate.service.EventGuestPricingQueryService;
import com.nextjstemplate.service.EventGuestPricingService;
import com.nextjstemplate.service.criteria.EventGuestPricingCriteria;
import com.nextjstemplate.service.dto.EventGuestPricingDTO;
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
 * REST controller for managing {@link com.nextjstemplate.domain.EventGuestPricing}.
 */
@RestController
@RequestMapping("/api/event-guest-pricings")
public class EventGuestPricingResource {

    private final Logger log = LoggerFactory.getLogger(EventGuestPricingResource.class);

    private static final String ENTITY_NAME = "eventGuestPricing";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventGuestPricingService eventGuestPricingService;

    private final EventGuestPricingRepository eventGuestPricingRepository;

    private final EventGuestPricingQueryService eventGuestPricingQueryService;

    public EventGuestPricingResource(
        EventGuestPricingService eventGuestPricingService,
        EventGuestPricingRepository eventGuestPricingRepository,
        EventGuestPricingQueryService eventGuestPricingQueryService
    ) {
        this.eventGuestPricingService = eventGuestPricingService;
        this.eventGuestPricingRepository = eventGuestPricingRepository;
        this.eventGuestPricingQueryService = eventGuestPricingQueryService;
    }

    /**
     * {@code POST  /event-guest-pricings} : Create a new eventGuestPricing.
     *
     * @param eventGuestPricingDTO the eventGuestPricingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventGuestPricingDTO, or with status {@code 400 (Bad Request)} if the eventGuestPricing has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EventGuestPricingDTO> createEventGuestPricing(@Valid @RequestBody EventGuestPricingDTO eventGuestPricingDTO)
        throws URISyntaxException {
        log.debug("REST request to save EventGuestPricing : {}", eventGuestPricingDTO);
        if (eventGuestPricingDTO.getId() != null) {
            throw new BadRequestAlertException("A new eventGuestPricing cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventGuestPricingDTO result = eventGuestPricingService.save(eventGuestPricingDTO);
        return ResponseEntity
            .created(new URI("/api/event-guest-pricings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /event-guest-pricings/:id} : Updates an existing eventGuestPricing.
     *
     * @param id the id of the eventGuestPricingDTO to save.
     * @param eventGuestPricingDTO the eventGuestPricingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventGuestPricingDTO,
     * or with status {@code 400 (Bad Request)} if the eventGuestPricingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventGuestPricingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventGuestPricingDTO> updateEventGuestPricing(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EventGuestPricingDTO eventGuestPricingDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EventGuestPricing : {}, {}", id, eventGuestPricingDTO);
        if (eventGuestPricingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventGuestPricingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventGuestPricingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EventGuestPricingDTO result = eventGuestPricingService.update(eventGuestPricingDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventGuestPricingDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /event-guest-pricings/:id} : Partial updates given fields of an existing eventGuestPricing, field will ignore if it is null
     *
     * @param id the id of the eventGuestPricingDTO to save.
     * @param eventGuestPricingDTO the eventGuestPricingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventGuestPricingDTO,
     * or with status {@code 400 (Bad Request)} if the eventGuestPricingDTO is not valid,
     * or with status {@code 404 (Not Found)} if the eventGuestPricingDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the eventGuestPricingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventGuestPricingDTO> partialUpdateEventGuestPricing(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EventGuestPricingDTO eventGuestPricingDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EventGuestPricing partially : {}, {}", id, eventGuestPricingDTO);
        if (eventGuestPricingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventGuestPricingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventGuestPricingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EventGuestPricingDTO> result = eventGuestPricingService.partialUpdate(eventGuestPricingDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventGuestPricingDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /event-guest-pricings} : get all the eventGuestPricings.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eventGuestPricings in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EventGuestPricingDTO>> getAllEventGuestPricings(
        EventGuestPricingCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get EventGuestPricings by criteria: {}", criteria);

        Page<EventGuestPricingDTO> page = eventGuestPricingQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /event-guest-pricings/count} : count all the eventGuestPricings.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEventGuestPricings(EventGuestPricingCriteria criteria) {
        log.debug("REST request to count EventGuestPricings by criteria: {}", criteria);
        return ResponseEntity.ok().body(eventGuestPricingQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /event-guest-pricings/:id} : get the "id" eventGuestPricing.
     *
     * @param id the id of the eventGuestPricingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventGuestPricingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventGuestPricingDTO> getEventGuestPricing(@PathVariable Long id) {
        log.debug("REST request to get EventGuestPricing : {}", id);
        Optional<EventGuestPricingDTO> eventGuestPricingDTO = eventGuestPricingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventGuestPricingDTO);
    }

    /**
     * {@code DELETE  /event-guest-pricings/:id} : delete the "id" eventGuestPricing.
     *
     * @param id the id of the eventGuestPricingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventGuestPricing(@PathVariable Long id) {
        log.debug("REST request to delete EventGuestPricing : {}", id);
        eventGuestPricingService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
