package com.eventsitemanager.web.rest;

import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.EventAgendaItemRepository;
import com.eventsitemanager.service.EventAgendaItemQueryService;
import com.eventsitemanager.service.EventAgendaItemService;
import com.eventsitemanager.service.criteria.EventAgendaItemCriteria;
import com.eventsitemanager.service.dto.EventAgendaItemDTO;
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
@RequestMapping("/api/event-agenda-items")
public class EventAgendaItemResource {

    private final Logger log = LoggerFactory.getLogger(EventAgendaItemResource.class);

    private static final String ENTITY_NAME = "eventAgendaItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventAgendaItemService eventAgendaItemService;

    private final EventAgendaItemRepository eventAgendaItemRepository;

    private final EventAgendaItemQueryService eventAgendaItemQueryService;

    public EventAgendaItemResource(
        EventAgendaItemService eventAgendaItemService,
        EventAgendaItemRepository eventAgendaItemRepository,
        EventAgendaItemQueryService eventAgendaItemQueryService
    ) {
        this.eventAgendaItemService = eventAgendaItemService;
        this.eventAgendaItemRepository = eventAgendaItemRepository;
        this.eventAgendaItemQueryService = eventAgendaItemQueryService;
    }

    @PostMapping("")
    public ResponseEntity<EventAgendaItemDTO> create(@Valid @RequestBody EventAgendaItemDTO dto) throws URISyntaxException {
        if (dto.getId() != null) {
            throw new BadRequestAlertException("A new eventAgendaItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventAgendaItemDTO result = eventAgendaItemService.save(dto);
        return ResponseEntity
            .created(new URI("/api/event-agenda-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventAgendaItemDTO> update(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EventAgendaItemDTO dto
    ) throws URISyntaxException {
        if (dto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!eventAgendaItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dto.getId().toString()))
            .body(eventAgendaItemService.update(dto));
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventAgendaItemDTO> partialUpdate(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EventAgendaItemDTO dto
    ) throws URISyntaxException {
        if (dto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!eventAgendaItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Optional<EventAgendaItemDTO> result = eventAgendaItemService.partialUpdate(dto);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dto.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<EventAgendaItemDTO>> getAll(
        EventAgendaItemCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        Page<EventAgendaItemDTO> page = eventAgendaItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count(EventAgendaItemCriteria criteria) {
        return ResponseEntity.ok().body(eventAgendaItemQueryService.countByCriteria(criteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventAgendaItemDTO> getOne(@PathVariable Long id) {
        Optional<EventAgendaItemDTO> dto = eventAgendaItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eventAgendaItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /event-agenda-items/event/:eventId} : get all agenda items for an event.
     *
     * @param eventId the event id.
     * @param publishedOnly when true, return only published items.
     */
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<EventAgendaItemDTO>> getByEventId(
        @PathVariable("eventId") Long eventId,
        @RequestParam(value = "publishedOnly", required = false, defaultValue = "false") boolean publishedOnly
    ) {
        log.debug("REST request to get EventAgendaItems for event : {}, publishedOnly={}", eventId, publishedOnly);
        return ResponseEntity.ok().body(eventAgendaItemService.findByEventId(eventId, publishedOnly));
    }
}
