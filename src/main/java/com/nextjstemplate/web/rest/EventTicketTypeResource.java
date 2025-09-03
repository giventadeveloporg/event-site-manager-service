package com.nextjstemplate.web.rest;

import com.nextjstemplate.repository.EventTicketTypeRepository;
import com.nextjstemplate.service.EventTicketTypeQueryService;
import com.nextjstemplate.service.EventTicketTypeService;
import com.nextjstemplate.service.criteria.EventTicketTypeCriteria;
import com.nextjstemplate.service.dto.EventTicketTypeDTO;
import com.nextjstemplate.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.nextjstemplate.domain.EventTicketType}.
 */
@RestController
@RequestMapping("/api/event-ticket-types")
public class EventTicketTypeResource {

    private final Logger log = LoggerFactory.getLogger(EventTicketTypeResource.class);

    private static final String ENTITY_NAME = "eventTicketType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventTicketTypeService eventTicketTypeService;

    private final EventTicketTypeRepository eventTicketTypeRepository;

    private final EventTicketTypeQueryService eventTicketTypeQueryService;

    public EventTicketTypeResource(
        EventTicketTypeService eventTicketTypeService,
        EventTicketTypeRepository eventTicketTypeRepository,
        EventTicketTypeQueryService eventTicketTypeQueryService
    ) {
        this.eventTicketTypeService = eventTicketTypeService;
        this.eventTicketTypeRepository = eventTicketTypeRepository;
        this.eventTicketTypeQueryService = eventTicketTypeQueryService;
    }

    /**
     * {@code POST  /event-ticket-types} : Create a new eventTicketType.
     *
     * @param eventTicketTypeDTO the eventTicketTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventTicketTypeDTO, or with status {@code 400 (Bad Request)} if the eventTicketType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EventTicketTypeDTO> createEventTicketType(@Valid @RequestBody EventTicketTypeDTO eventTicketTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save EventTicketType : {}", eventTicketTypeDTO);
        if (eventTicketTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new eventTicketType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        eventTicketTypeDTO.setSoldQuantity(0);
        eventTicketTypeDTO.setRemainingQuantity(0);
        EventTicketTypeDTO result = eventTicketTypeService.save(eventTicketTypeDTO);
        return ResponseEntity
            .created(new URI("/api/event-ticket-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /event-ticket-types/:id} : Updates an existing eventTicketType.
     *
     * @param id the id of the eventTicketTypeDTO to save.
     * @param eventTicketTypeDTO the eventTicketTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventTicketTypeDTO,
     * or with status {@code 400 (Bad Request)} if the eventTicketTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventTicketTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventTicketTypeDTO> updateEventTicketType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EventTicketTypeDTO eventTicketTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EventTicketType : {}, {}", id, eventTicketTypeDTO);
        if (eventTicketTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventTicketTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventTicketTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EventTicketTypeDTO result = eventTicketTypeService.update(eventTicketTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventTicketTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /event-ticket-types/:id} : Partial updates given fields of an existing eventTicketType, field will ignore if it is null
     *
     * @param id the id of the eventTicketTypeDTO to save.
     * @param eventTicketTypeDTO the eventTicketTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventTicketTypeDTO,
     * or with status {@code 400 (Bad Request)} if the eventTicketTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the eventTicketTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the eventTicketTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventTicketTypeDTO> partialUpdateEventTicketType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EventTicketTypeDTO eventTicketTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EventTicketType partially : {}, {}", id, eventTicketTypeDTO);
        if (eventTicketTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventTicketTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventTicketTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EventTicketTypeDTO> result = eventTicketTypeService.partialUpdate(eventTicketTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventTicketTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /event-ticket-types} : get all the eventTicketTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eventTicketTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EventTicketTypeDTO>> getAllEventTicketTypes(
        EventTicketTypeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get EventTicketTypes by criteria: {}", criteria);

        Page<EventTicketTypeDTO> page = eventTicketTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /event-ticket-types/count} : count all the eventTicketTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEventTicketTypes(EventTicketTypeCriteria criteria) {
        log.debug("REST request to count EventTicketTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(eventTicketTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /event-ticket-types/:id} : get the "id" eventTicketType.
     *
     * @param id the id of the eventTicketTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventTicketTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventTicketTypeDTO> getEventTicketType(@PathVariable Long id) {
        log.debug("REST request to get EventTicketType : {}", id);
        Optional<EventTicketTypeDTO> eventTicketTypeDTO = eventTicketTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventTicketTypeDTO);
    }

    /**
     * {@code DELETE  /event-ticket-types/:id} : delete the "id" eventTicketType.
     *
     * @param id the id of the eventTicketTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventTicketType(@PathVariable Long id) {
        log.debug("REST request to delete EventTicketType : {}", id);
        eventTicketTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
