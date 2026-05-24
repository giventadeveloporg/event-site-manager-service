package com.nextjstemplate.web.rest;

import com.nextjstemplate.errors.BadRequestAlertException;
import com.nextjstemplate.repository.EventCompetitionResultRepository;
import com.nextjstemplate.service.EventCompetitionResultQueryService;
import com.nextjstemplate.service.EventCompetitionResultService;
import com.nextjstemplate.service.criteria.EventCompetitionResultCriteria;
import com.nextjstemplate.service.dto.EventCompetitionResultDTO;
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
@RequestMapping("/api/event-competition-results")
public class EventCompetitionResultResource {

    private final Logger log = LoggerFactory.getLogger(EventCompetitionResultResource.class);

    private static final String ENTITY_NAME = "eventCompetitionResult";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventCompetitionResultService eventCompetitionResultService;

    private final EventCompetitionResultRepository eventCompetitionResultRepository;

    private final EventCompetitionResultQueryService eventCompetitionResultQueryService;

    public EventCompetitionResultResource(EventCompetitionResultService eventCompetitionResultService, EventCompetitionResultRepository eventCompetitionResultRepository, EventCompetitionResultQueryService eventCompetitionResultQueryService) {
        this.eventCompetitionResultService = eventCompetitionResultService;
        this.eventCompetitionResultRepository = eventCompetitionResultRepository;
        this.eventCompetitionResultQueryService = eventCompetitionResultQueryService;
    }

    @PostMapping("")
    public ResponseEntity<EventCompetitionResultDTO> create(@Valid @RequestBody EventCompetitionResultDTO dto) throws URISyntaxException {
        if (dto.getId() != null) {
            throw new BadRequestAlertException("A new eventCompetitionResult cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventCompetitionResultDTO result = eventCompetitionResultService.save(dto);
        return ResponseEntity
            .created(new URI("/api/event-competition-results/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventCompetitionResultDTO> update(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody EventCompetitionResultDTO dto) throws URISyntaxException {
        if (dto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!eventCompetitionResultRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dto.getId().toString())).body(eventCompetitionResultService.update(dto));
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventCompetitionResultDTO> partialUpdate(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody EventCompetitionResultDTO dto) throws URISyntaxException {
        if (dto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!eventCompetitionResultRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Optional<EventCompetitionResultDTO> result = eventCompetitionResultService.partialUpdate(dto);
        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dto.getId().toString()));
    }

    @GetMapping("")
    public ResponseEntity<List<EventCompetitionResultDTO>> getAll(EventCompetitionResultCriteria criteria, @org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        Page<EventCompetitionResultDTO> page = eventCompetitionResultQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count(EventCompetitionResultCriteria criteria) {
        return ResponseEntity.ok().body(eventCompetitionResultQueryService.countByCriteria(criteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventCompetitionResultDTO> getOne(@PathVariable Long id) {
        Optional<EventCompetitionResultDTO> dto = eventCompetitionResultService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eventCompetitionResultService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
