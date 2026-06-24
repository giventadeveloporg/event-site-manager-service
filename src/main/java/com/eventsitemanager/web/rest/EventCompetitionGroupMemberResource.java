package com.eventsitemanager.web.rest;

import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.EventCompetitionGroupMemberRepository;
import com.eventsitemanager.service.EventCompetitionGroupMemberQueryService;
import com.eventsitemanager.service.EventCompetitionGroupMemberService;
import com.eventsitemanager.service.criteria.EventCompetitionGroupMemberCriteria;
import com.eventsitemanager.service.dto.EventCompetitionGroupMemberDTO;
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
@RequestMapping("/api/event-competition-group-members")
public class EventCompetitionGroupMemberResource {

    private final Logger log = LoggerFactory.getLogger(EventCompetitionGroupMemberResource.class);

    private static final String ENTITY_NAME = "eventCompetitionGroupMember";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventCompetitionGroupMemberService eventCompetitionGroupMemberService;

    private final EventCompetitionGroupMemberRepository eventCompetitionGroupMemberRepository;

    private final EventCompetitionGroupMemberQueryService eventCompetitionGroupMemberQueryService;

    public EventCompetitionGroupMemberResource(
        EventCompetitionGroupMemberService eventCompetitionGroupMemberService,
        EventCompetitionGroupMemberRepository eventCompetitionGroupMemberRepository,
        EventCompetitionGroupMemberQueryService eventCompetitionGroupMemberQueryService
    ) {
        this.eventCompetitionGroupMemberService = eventCompetitionGroupMemberService;
        this.eventCompetitionGroupMemberRepository = eventCompetitionGroupMemberRepository;
        this.eventCompetitionGroupMemberQueryService = eventCompetitionGroupMemberQueryService;
    }

    @PostMapping("")
    public ResponseEntity<EventCompetitionGroupMemberDTO> create(@Valid @RequestBody EventCompetitionGroupMemberDTO dto)
        throws URISyntaxException {
        if (dto.getId() != null) {
            throw new BadRequestAlertException("A new eventCompetitionGroupMember cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventCompetitionGroupMemberDTO result = eventCompetitionGroupMemberService.save(dto);
        return ResponseEntity
            .created(new URI("/api/event-competition-group-members/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventCompetitionGroupMemberDTO> update(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EventCompetitionGroupMemberDTO dto
    ) throws URISyntaxException {
        if (dto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!eventCompetitionGroupMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dto.getId().toString()))
            .body(eventCompetitionGroupMemberService.update(dto));
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventCompetitionGroupMemberDTO> partialUpdate(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EventCompetitionGroupMemberDTO dto
    ) throws URISyntaxException {
        if (dto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!eventCompetitionGroupMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Optional<EventCompetitionGroupMemberDTO> result = eventCompetitionGroupMemberService.partialUpdate(dto);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dto.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<EventCompetitionGroupMemberDTO>> getAll(
        EventCompetitionGroupMemberCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        Page<EventCompetitionGroupMemberDTO> page = eventCompetitionGroupMemberQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count(EventCompetitionGroupMemberCriteria criteria) {
        return ResponseEntity.ok().body(eventCompetitionGroupMemberQueryService.countByCriteria(criteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventCompetitionGroupMemberDTO> getOne(@PathVariable Long id) {
        Optional<EventCompetitionGroupMemberDTO> dto = eventCompetitionGroupMemberService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eventCompetitionGroupMemberService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
