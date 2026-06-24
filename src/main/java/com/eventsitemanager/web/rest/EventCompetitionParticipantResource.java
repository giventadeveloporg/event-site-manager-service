package com.eventsitemanager.web.rest;

import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.EventCompetitionParticipantRepository;
import com.eventsitemanager.service.EventCompetitionParticipantQueryService;
import com.eventsitemanager.service.EventCompetitionParticipantService;
import com.eventsitemanager.service.criteria.EventCompetitionParticipantCriteria;
import com.eventsitemanager.service.dto.EventCompetitionParticipantDTO;
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
@RequestMapping("/api/event-competition-participants")
public class EventCompetitionParticipantResource {

    private final Logger log = LoggerFactory.getLogger(EventCompetitionParticipantResource.class);

    private static final String ENTITY_NAME = "eventCompetitionParticipant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventCompetitionParticipantService eventCompetitionParticipantService;

    private final EventCompetitionParticipantRepository eventCompetitionParticipantRepository;

    private final EventCompetitionParticipantQueryService eventCompetitionParticipantQueryService;

    public EventCompetitionParticipantResource(
        EventCompetitionParticipantService eventCompetitionParticipantService,
        EventCompetitionParticipantRepository eventCompetitionParticipantRepository,
        EventCompetitionParticipantQueryService eventCompetitionParticipantQueryService
    ) {
        this.eventCompetitionParticipantService = eventCompetitionParticipantService;
        this.eventCompetitionParticipantRepository = eventCompetitionParticipantRepository;
        this.eventCompetitionParticipantQueryService = eventCompetitionParticipantQueryService;
    }

    @PostMapping("")
    public ResponseEntity<EventCompetitionParticipantDTO> create(@Valid @RequestBody EventCompetitionParticipantDTO dto)
        throws URISyntaxException {
        if (dto.getId() != null) {
            throw new BadRequestAlertException("A new eventCompetitionParticipant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventCompetitionParticipantDTO result = eventCompetitionParticipantService.save(dto);
        return ResponseEntity
            .created(new URI("/api/event-competition-participants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventCompetitionParticipantDTO> update(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EventCompetitionParticipantDTO dto
    ) throws URISyntaxException {
        if (dto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!eventCompetitionParticipantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dto.getId().toString()))
            .body(eventCompetitionParticipantService.update(dto));
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventCompetitionParticipantDTO> partialUpdate(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EventCompetitionParticipantDTO dto
    ) throws URISyntaxException {
        if (dto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!eventCompetitionParticipantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Optional<EventCompetitionParticipantDTO> result = eventCompetitionParticipantService.partialUpdate(dto);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dto.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<EventCompetitionParticipantDTO>> getAll(
        EventCompetitionParticipantCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        Page<EventCompetitionParticipantDTO> page = eventCompetitionParticipantQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count(EventCompetitionParticipantCriteria criteria) {
        return ResponseEntity.ok().body(eventCompetitionParticipantQueryService.countByCriteria(criteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventCompetitionParticipantDTO> getOne(@PathVariable Long id) {
        Optional<EventCompetitionParticipantDTO> dto = eventCompetitionParticipantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eventCompetitionParticipantService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
