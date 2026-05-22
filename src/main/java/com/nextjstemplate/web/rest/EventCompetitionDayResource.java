package com.nextjstemplate.web.rest;

import com.nextjstemplate.errors.BadRequestAlertException;
import com.nextjstemplate.repository.EventCompetitionDayRepository;
import com.nextjstemplate.service.EventCompetitionDayQueryService;
import com.nextjstemplate.service.EventCompetitionDayService;
import com.nextjstemplate.service.criteria.EventCompetitionDayCriteria;
import com.nextjstemplate.service.dto.EventCompetitionDayDTO;
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
@RequestMapping("/api/event-competition-days")
public class EventCompetitionDayResource {

    private final Logger log = LoggerFactory.getLogger(EventCompetitionDayResource.class);

    private static final String ENTITY_NAME = "eventCompetitionDay";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventCompetitionDayService eventCompetitionDayService;

    private final EventCompetitionDayRepository eventCompetitionDayRepository;

    private final EventCompetitionDayQueryService eventCompetitionDayQueryService;

    public EventCompetitionDayResource(EventCompetitionDayService eventCompetitionDayService, EventCompetitionDayRepository eventCompetitionDayRepository, EventCompetitionDayQueryService eventCompetitionDayQueryService) {
        this.eventCompetitionDayService = eventCompetitionDayService;
        this.eventCompetitionDayRepository = eventCompetitionDayRepository;
        this.eventCompetitionDayQueryService = eventCompetitionDayQueryService;
    }

    @PostMapping("")
    public ResponseEntity<EventCompetitionDayDTO> create(@Valid @RequestBody EventCompetitionDayDTO dto) throws URISyntaxException {
        if (dto.getId() != null) {
            throw new BadRequestAlertException("A new eventCompetitionDay cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventCompetitionDayDTO result = eventCompetitionDayService.save(dto);
        return ResponseEntity
            .created(new URI("/api/event-competition-days/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventCompetitionDayDTO> update(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody EventCompetitionDayDTO dto) throws URISyntaxException {
        if (dto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!eventCompetitionDayRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dto.getId().toString())).body(eventCompetitionDayService.update(dto));
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventCompetitionDayDTO> partialUpdate(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody EventCompetitionDayDTO dto) throws URISyntaxException {
        if (dto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!eventCompetitionDayRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Optional<EventCompetitionDayDTO> result = eventCompetitionDayService.partialUpdate(dto);
        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dto.getId().toString()));
    }

    @GetMapping("")
    public ResponseEntity<List<EventCompetitionDayDTO>> getAll(EventCompetitionDayCriteria criteria, @org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        Page<EventCompetitionDayDTO> page = eventCompetitionDayQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count(EventCompetitionDayCriteria criteria) {
        return ResponseEntity.ok().body(eventCompetitionDayQueryService.countByCriteria(criteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventCompetitionDayDTO> getOne(@PathVariable Long id) {
        Optional<EventCompetitionDayDTO> dto = eventCompetitionDayService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eventCompetitionDayService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
