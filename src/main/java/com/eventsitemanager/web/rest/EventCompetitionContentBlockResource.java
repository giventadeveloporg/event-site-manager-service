package com.eventsitemanager.web.rest;

import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.EventCompetitionContentBlockRepository;
import com.eventsitemanager.service.EventCompetitionContentBlockQueryService;
import com.eventsitemanager.service.EventCompetitionContentBlockService;
import com.eventsitemanager.service.criteria.EventCompetitionContentBlockCriteria;
import com.eventsitemanager.service.dto.EventCompetitionContentBlockDTO;
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

@RestController
@RequestMapping("/api/event-competition-content-blocks")
public class EventCompetitionContentBlockResource {

    private final Logger log = LoggerFactory.getLogger(EventCompetitionContentBlockResource.class);

    private static final String ENTITY_NAME = "eventCompetitionContentBlock";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventCompetitionContentBlockService eventCompetitionContentBlockService;

    private final EventCompetitionContentBlockRepository eventCompetitionContentBlockRepository;

    private final EventCompetitionContentBlockQueryService eventCompetitionContentBlockQueryService;

    public EventCompetitionContentBlockResource(
        EventCompetitionContentBlockService eventCompetitionContentBlockService,
        EventCompetitionContentBlockRepository eventCompetitionContentBlockRepository,
        EventCompetitionContentBlockQueryService eventCompetitionContentBlockQueryService
    ) {
        this.eventCompetitionContentBlockService = eventCompetitionContentBlockService;
        this.eventCompetitionContentBlockRepository = eventCompetitionContentBlockRepository;
        this.eventCompetitionContentBlockQueryService = eventCompetitionContentBlockQueryService;
    }

    @PostMapping("")
    public ResponseEntity<EventCompetitionContentBlockDTO> create(@Valid @RequestBody EventCompetitionContentBlockDTO dto)
        throws URISyntaxException {
        if (dto.getId() != null) {
            throw new BadRequestAlertException("A new eventCompetitionContentBlock cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventCompetitionContentBlockDTO result = eventCompetitionContentBlockService.save(dto);
        return ResponseEntity
            .created(new URI("/api/event-competition-content-blocks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventCompetitionContentBlockDTO> update(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EventCompetitionContentBlockDTO dto
    ) throws URISyntaxException {
        if (dto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!eventCompetitionContentBlockRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dto.getId().toString()))
            .body(eventCompetitionContentBlockService.update(dto));
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventCompetitionContentBlockDTO> partialUpdate(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EventCompetitionContentBlockDTO dto
    ) throws URISyntaxException {
        if (dto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!eventCompetitionContentBlockRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Optional<EventCompetitionContentBlockDTO> result = eventCompetitionContentBlockService.partialUpdate(dto);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dto.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<EventCompetitionContentBlockDTO>> getAll(
        EventCompetitionContentBlockCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        Page<EventCompetitionContentBlockDTO> page = eventCompetitionContentBlockQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count(EventCompetitionContentBlockCriteria criteria) {
        return ResponseEntity.ok().body(eventCompetitionContentBlockQueryService.countByCriteria(criteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventCompetitionContentBlockDTO> getOne(@PathVariable Long id) {
        Optional<EventCompetitionContentBlockDTO> dto = eventCompetitionContentBlockService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eventCompetitionContentBlockService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
