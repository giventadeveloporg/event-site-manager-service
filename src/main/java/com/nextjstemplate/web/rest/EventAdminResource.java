package com.nextjstemplate.web.rest;

import com.nextjstemplate.errors.BadRequestAlertException;
import com.nextjstemplate.repository.EventAdminRepository;
import com.nextjstemplate.service.EventAdminQueryService;
import com.nextjstemplate.service.EventAdminService;
import com.nextjstemplate.service.criteria.EventAdminCriteria;
import com.nextjstemplate.service.dto.EventAdminDTO;
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
 * REST controller for managing {@link com.nextjstemplate.domain.EventAdmin}.
 */
@RestController
@RequestMapping("/api/event-admins")
public class EventAdminResource {

    private final Logger log = LoggerFactory.getLogger(EventAdminResource.class);

    private static final String ENTITY_NAME = "eventAdmin";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventAdminService eventAdminService;

    private final EventAdminRepository eventAdminRepository;

    private final EventAdminQueryService eventAdminQueryService;

    public EventAdminResource(
        EventAdminService eventAdminService,
        EventAdminRepository eventAdminRepository,
        EventAdminQueryService eventAdminQueryService
    ) {
        this.eventAdminService = eventAdminService;
        this.eventAdminRepository = eventAdminRepository;
        this.eventAdminQueryService = eventAdminQueryService;
    }

    /**
     * {@code POST  /event-admins} : Create a new eventAdmin.
     *
     * @param eventAdminDTO the eventAdminDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventAdminDTO, or with status {@code 400 (Bad Request)} if the eventAdmin has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EventAdminDTO> createEventAdmin(@Valid @RequestBody EventAdminDTO eventAdminDTO) throws URISyntaxException {
        log.debug("REST request to save EventAdmin : {}", eventAdminDTO);
        if (eventAdminDTO.getId() != null) {
            throw new BadRequestAlertException("A new eventAdmin cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventAdminDTO result = eventAdminService.save(eventAdminDTO);
        return ResponseEntity
            .created(new URI("/api/event-admins/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /event-admins/:id} : Updates an existing eventAdmin.
     *
     * @param id the id of the eventAdminDTO to save.
     * @param eventAdminDTO the eventAdminDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventAdminDTO,
     * or with status {@code 400 (Bad Request)} if the eventAdminDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventAdminDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventAdminDTO> updateEventAdmin(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EventAdminDTO eventAdminDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EventAdmin : {}, {}", id, eventAdminDTO);
        if (eventAdminDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventAdminDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventAdminRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EventAdminDTO result = eventAdminService.update(eventAdminDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventAdminDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /event-admins/:id} : Partial updates given fields of an existing eventAdmin, field will ignore if it is null
     *
     * @param id the id of the eventAdminDTO to save.
     * @param eventAdminDTO the eventAdminDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventAdminDTO,
     * or with status {@code 400 (Bad Request)} if the eventAdminDTO is not valid,
     * or with status {@code 404 (Not Found)} if the eventAdminDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the eventAdminDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventAdminDTO> partialUpdateEventAdmin(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EventAdminDTO eventAdminDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EventAdmin partially : {}, {}", id, eventAdminDTO);
        if (eventAdminDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventAdminDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventAdminRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EventAdminDTO> result = eventAdminService.partialUpdate(eventAdminDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventAdminDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /event-admins} : get all the eventAdmins.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eventAdmins in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EventAdminDTO>> getAllEventAdmins(
        EventAdminCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get EventAdmins by criteria: {}", criteria);

        Page<EventAdminDTO> page = eventAdminQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /event-admins/count} : count all the eventAdmins.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEventAdmins(EventAdminCriteria criteria) {
        log.debug("REST request to count EventAdmins by criteria: {}", criteria);
        return ResponseEntity.ok().body(eventAdminQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /event-admins/:id} : get the "id" eventAdmin.
     *
     * @param id the id of the eventAdminDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventAdminDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventAdminDTO> getEventAdmin(@PathVariable Long id) {
        log.debug("REST request to get EventAdmin : {}", id);
        Optional<EventAdminDTO> eventAdminDTO = eventAdminService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventAdminDTO);
    }

    /**
     * {@code DELETE  /event-admins/:id} : delete the "id" eventAdmin.
     *
     * @param id the id of the eventAdminDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventAdmin(@PathVariable Long id) {
        log.debug("REST request to delete EventAdmin : {}", id);
        eventAdminService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
