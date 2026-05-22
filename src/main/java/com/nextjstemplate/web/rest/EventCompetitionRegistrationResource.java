package com.nextjstemplate.web.rest;

import com.nextjstemplate.errors.BadRequestAlertException;
import com.nextjstemplate.repository.EventCompetitionRegistrationRepository;
import com.nextjstemplate.service.EventCompetitionRegistrationQueryService;
import com.nextjstemplate.service.EventCompetitionRegistrationService;
import com.nextjstemplate.service.criteria.EventCompetitionRegistrationCriteria;
import com.nextjstemplate.service.dto.EventCompetitionRegistrationDTO;
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
@RequestMapping("/api/event-competition-registrations")
public class EventCompetitionRegistrationResource {

    private final Logger log = LoggerFactory.getLogger(EventCompetitionRegistrationResource.class);

    private static final String ENTITY_NAME = "eventCompetitionRegistration";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventCompetitionRegistrationService eventCompetitionRegistrationService;

    private final EventCompetitionRegistrationRepository eventCompetitionRegistrationRepository;

    private final EventCompetitionRegistrationQueryService eventCompetitionRegistrationQueryService;

    public EventCompetitionRegistrationResource(EventCompetitionRegistrationService eventCompetitionRegistrationService, EventCompetitionRegistrationRepository eventCompetitionRegistrationRepository, EventCompetitionRegistrationQueryService eventCompetitionRegistrationQueryService) {
        this.eventCompetitionRegistrationService = eventCompetitionRegistrationService;
        this.eventCompetitionRegistrationRepository = eventCompetitionRegistrationRepository;
        this.eventCompetitionRegistrationQueryService = eventCompetitionRegistrationQueryService;
    }

    @PostMapping("")
    public ResponseEntity<EventCompetitionRegistrationDTO> create(@Valid @RequestBody EventCompetitionRegistrationDTO dto) throws URISyntaxException {
        if (dto.getId() != null) {
            throw new BadRequestAlertException("A new eventCompetitionRegistration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventCompetitionRegistrationDTO result = eventCompetitionRegistrationService.save(dto);
        return ResponseEntity
            .created(new URI("/api/event-competition-registrations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventCompetitionRegistrationDTO> update(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody EventCompetitionRegistrationDTO dto) throws URISyntaxException {
        if (dto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!eventCompetitionRegistrationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dto.getId().toString())).body(eventCompetitionRegistrationService.update(dto));
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventCompetitionRegistrationDTO> partialUpdate(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody EventCompetitionRegistrationDTO dto) throws URISyntaxException {
        if (dto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!eventCompetitionRegistrationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Optional<EventCompetitionRegistrationDTO> result = eventCompetitionRegistrationService.partialUpdate(dto);
        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dto.getId().toString()));
    }

    @GetMapping("")
    public ResponseEntity<List<EventCompetitionRegistrationDTO>> getAll(EventCompetitionRegistrationCriteria criteria, @org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        Page<EventCompetitionRegistrationDTO> page = eventCompetitionRegistrationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count(EventCompetitionRegistrationCriteria criteria) {
        return ResponseEntity.ok().body(eventCompetitionRegistrationQueryService.countByCriteria(criteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventCompetitionRegistrationDTO> getOne(@PathVariable Long id) {
        Optional<EventCompetitionRegistrationDTO> dto = eventCompetitionRegistrationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eventCompetitionRegistrationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
