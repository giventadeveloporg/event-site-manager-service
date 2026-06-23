package com.eventsitemanager.web.rest;

import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.EventCompetitionRepository;
import com.eventsitemanager.service.EventCompetitionQueryService;
import com.eventsitemanager.service.EventCompetitionService;
import com.eventsitemanager.service.criteria.EventCompetitionCriteria;
import com.eventsitemanager.service.dto.CompetitionEligibilityCheckDTO;
import com.eventsitemanager.service.dto.EventCompetitionDTO;
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
@RequestMapping("/api/event-competitions")
public class EventCompetitionResource {

    private final Logger log = LoggerFactory.getLogger(EventCompetitionResource.class);

    private static final String ENTITY_NAME = "eventCompetition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventCompetitionService eventCompetitionService;

    private final EventCompetitionRepository eventCompetitionRepository;

    private final EventCompetitionQueryService eventCompetitionQueryService;

    public EventCompetitionResource(
        EventCompetitionService eventCompetitionService,
        EventCompetitionRepository eventCompetitionRepository,
        EventCompetitionQueryService eventCompetitionQueryService
    ) {
        this.eventCompetitionService = eventCompetitionService;
        this.eventCompetitionRepository = eventCompetitionRepository;
        this.eventCompetitionQueryService = eventCompetitionQueryService;
    }

    @PostMapping("")
    public ResponseEntity<EventCompetitionDTO> create(@Valid @RequestBody EventCompetitionDTO dto) throws URISyntaxException {
        if (dto.getId() != null) {
            throw new BadRequestAlertException("A new eventCompetition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventCompetitionDTO result = eventCompetitionService.save(dto);
        return ResponseEntity
            .created(new URI("/api/event-competitions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventCompetitionDTO> update(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EventCompetitionDTO dto
    ) throws URISyntaxException {
        if (dto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!eventCompetitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dto.getId().toString()))
            .body(eventCompetitionService.update(dto));
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventCompetitionDTO> partialUpdate(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EventCompetitionDTO dto
    ) throws URISyntaxException {
        if (dto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!eventCompetitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Optional<EventCompetitionDTO> result = eventCompetitionService.partialUpdate(dto);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dto.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<EventCompetitionDTO>> getAll(
        EventCompetitionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        Page<EventCompetitionDTO> page = eventCompetitionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count(EventCompetitionCriteria criteria) {
        return ResponseEntity.ok().body(eventCompetitionQueryService.countByCriteria(criteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventCompetitionDTO> getOne(@PathVariable Long id) {
        Optional<EventCompetitionDTO> dto = eventCompetitionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dto);
    }

    @GetMapping("/{id}/eligibility-check")
    public ResponseEntity<CompetitionEligibilityCheckDTO> checkEligibility(
        @PathVariable Long id,
        @RequestParam(name = "participantProfileId.equals") Long participantProfileId
    ) {
        return ResponseEntity.ok(eventCompetitionService.checkEligibility(id, participantProfileId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eventCompetitionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
