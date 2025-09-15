package com.nextjstemplate.web.rest;

import com.nextjstemplate.repository.EventFeaturedPerformersRepository;
import com.nextjstemplate.service.EventFeaturedPerformersQueryService;
import com.nextjstemplate.service.EventFeaturedPerformersService;
import com.nextjstemplate.service.criteria.EventFeaturedPerformersCriteria;
import com.nextjstemplate.service.dto.EventFeaturedPerformersDTO;
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
 * REST controller for managing
 * {@link com.nextjstemplate.domain.EventFeaturedPerformers}.
 */
@RestController
@RequestMapping("/api/event-featured-performers")
public class EventFeaturedPerformersResource {

  private final Logger log = LoggerFactory.getLogger(EventFeaturedPerformersResource.class);

  private static final String ENTITY_NAME = "eventFeaturedPerformers";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final EventFeaturedPerformersService eventFeaturedPerformersService;

  private final EventFeaturedPerformersRepository eventFeaturedPerformersRepository;

  private final EventFeaturedPerformersQueryService eventFeaturedPerformersQueryService;

  public EventFeaturedPerformersResource(
      EventFeaturedPerformersService eventFeaturedPerformersService,
      EventFeaturedPerformersRepository eventFeaturedPerformersRepository,
      EventFeaturedPerformersQueryService eventFeaturedPerformersQueryService) {
    this.eventFeaturedPerformersService = eventFeaturedPerformersService;
    this.eventFeaturedPerformersRepository = eventFeaturedPerformersRepository;
    this.eventFeaturedPerformersQueryService = eventFeaturedPerformersQueryService;
  }

  /**
   * {@code POST  /event-featured-performers} : Create a new
   * eventFeaturedPerformers.
   *
   * @param eventFeaturedPerformersDTO the eventFeaturedPerformersDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
   *         body the new eventFeaturedPerformersDTO, or with status
   *         {@code 400 (Bad Request)} if the eventFeaturedPerformers has already
   *         an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("")
  public ResponseEntity<EventFeaturedPerformersDTO> createEventFeaturedPerformers(
      @Valid @RequestBody EventFeaturedPerformersDTO eventFeaturedPerformersDTO) throws URISyntaxException {
    log.debug("REST request to save EventFeaturedPerformers : {}", eventFeaturedPerformersDTO);
    if (eventFeaturedPerformersDTO.getId() != null) {
      throw new BadRequestAlertException("A new eventFeaturedPerformers cannot already have an ID", ENTITY_NAME,
          "idexists");
    }
    eventFeaturedPerformersDTO = eventFeaturedPerformersService.save(eventFeaturedPerformersDTO);
    return ResponseEntity
        .created(new URI("/api/event-featured-performers/" + eventFeaturedPerformersDTO.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME,
            eventFeaturedPerformersDTO.getId().toString()))
        .body(eventFeaturedPerformersDTO);
  }

  /**
   * {@code PUT  /event-featured-performers/:id} : Updates an existing
   * eventFeaturedPerformers.
   *
   * @param id                         the id of the eventFeaturedPerformersDTO to
   *                                   save.
   * @param eventFeaturedPerformersDTO the eventFeaturedPerformersDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
   *         the updated eventFeaturedPerformersDTO,
   *         or with status {@code 400 (Bad Request)} if the
   *         eventFeaturedPerformersDTO is not valid,
   *         or with status {@code 500 (Internal Server Error)} if the
   *         eventFeaturedPerformersDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/{id}")
  public ResponseEntity<EventFeaturedPerformersDTO> updateEventFeaturedPerformers(
      @PathVariable(value = "id", required = false) final Long id,
      @Valid @RequestBody EventFeaturedPerformersDTO eventFeaturedPerformersDTO) throws URISyntaxException {
    log.debug("REST request to update EventFeaturedPerformers : {}, {}", id, eventFeaturedPerformersDTO);
    if (eventFeaturedPerformersDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, eventFeaturedPerformersDTO.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!eventFeaturedPerformersRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    eventFeaturedPerformersDTO = eventFeaturedPerformersService.update(eventFeaturedPerformersDTO);
    return ResponseEntity
        .ok()
        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
            eventFeaturedPerformersDTO.getId().toString()))
        .body(eventFeaturedPerformersDTO);
  }

  /**
   * {@code PATCH  /event-featured-performers/:id} : Partial updates given fields
   * of an existing eventFeaturedPerformers, field will ignore if it is null
   *
   * @param id                         the id of the eventFeaturedPerformersDTO to
   *                                   save.
   * @param eventFeaturedPerformersDTO the eventFeaturedPerformersDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
   *         the updated eventFeaturedPerformersDTO,
   *         or with status {@code 400 (Bad Request)} if the
   *         eventFeaturedPerformersDTO is not valid,
   *         or with status {@code 404 (Not Found)} if the
   *         eventFeaturedPerformersDTO is not found,
   *         or with status {@code 500 (Internal Server Error)} if the
   *         eventFeaturedPerformersDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
  public ResponseEntity<EventFeaturedPerformersDTO> partialUpdateEventFeaturedPerformers(
      @PathVariable(value = "id", required = false) final Long id,
      @NotNull @RequestBody EventFeaturedPerformersDTO eventFeaturedPerformersDTO) throws URISyntaxException {
    log.debug("REST request to partial update EventFeaturedPerformers partially : {}, {}", id,
        eventFeaturedPerformersDTO);
    if (eventFeaturedPerformersDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, eventFeaturedPerformersDTO.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!eventFeaturedPerformersRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    Optional<EventFeaturedPerformersDTO> result = eventFeaturedPerformersService
        .partialUpdate(eventFeaturedPerformersDTO);

    return ResponseUtil.wrapOrNotFound(
        result,
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
            eventFeaturedPerformersDTO.getId().toString()));
  }

  /**
   * {@code GET  /event-featured-performers} : get all the
   * eventFeaturedPerformers.
   *
   * @param pageable the pagination information.
   * @param criteria the criteria which the requested entities should match.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
   *         of eventFeaturedPerformers in body.
   */
  @GetMapping("")
  public ResponseEntity<List<EventFeaturedPerformersDTO>> getAllEventFeaturedPerformers(
      EventFeaturedPerformersCriteria criteria,
      @org.springdoc.core.annotations.ParameterObject Pageable pageable) {
    log.debug("REST request to get EventFeaturedPerformers by criteria: {}", criteria);

    final Page<EventFeaturedPerformersDTO> page = eventFeaturedPerformersQueryService.findByCriteria(criteria,
        pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(),
        page);
    return ResponseEntity.ok().headers(headers).body(page.getContent());
  }

  /**
   * {@code GET  /event-featured-performers/count} : count all the
   * eventFeaturedPerformers.
   *
   * @param criteria the criteria which the requested entities should match.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
   *         in body.
   */
  @GetMapping("/count")
  public ResponseEntity<Long> countEventFeaturedPerformers(EventFeaturedPerformersCriteria criteria) {
    log.debug("REST request to count EventFeaturedPerformers by criteria: {}", criteria);
    return ResponseEntity.ok().body(eventFeaturedPerformersQueryService.countByCriteria(criteria));
  }

  /**
   * {@code GET  /event-featured-performers/:id} : get the "id"
   * eventFeaturedPerformers.
   *
   * @param id the id of the eventFeaturedPerformersDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
   *         the eventFeaturedPerformersDTO, or with status
   *         {@code 404 (Not Found)}.
   */
  @GetMapping("/{id}")
  public ResponseEntity<EventFeaturedPerformersDTO> getEventFeaturedPerformers(@PathVariable("id") Long id) {
    log.debug("REST request to get EventFeaturedPerformers : {}", id);
    Optional<EventFeaturedPerformersDTO> eventFeaturedPerformersDTO = eventFeaturedPerformersService.findOne(id);
    return ResponseUtil.wrapOrNotFound(eventFeaturedPerformersDTO);
  }

  /**
   * {@code DELETE  /event-featured-performers/:id} : delete the "id"
   * eventFeaturedPerformers.
   *
   * @param id the id of the eventFeaturedPerformersDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (No Content)}.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteEventFeaturedPerformers(@PathVariable("id") Long id) {
    log.debug("REST request to delete EventFeaturedPerformers : {}", id);
    eventFeaturedPerformersService.delete(id);
    return ResponseEntity
        .noContent()
        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }

  /**
   * {@code GET  /event-featured-performers/event/:eventId} : get all
   * eventFeaturedPerformers for a specific event.
   *
   * @param eventId the id of the event.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
   *         of eventFeaturedPerformers in body.
   */
  @GetMapping("/event/{eventId}")
  public ResponseEntity<List<EventFeaturedPerformersDTO>> getEventFeaturedPerformersByEventId(
      @PathVariable("eventId") Long eventId) {
    log.debug("REST request to get EventFeaturedPerformers for event : {}", eventId);
    List<EventFeaturedPerformersDTO> eventFeaturedPerformers = eventFeaturedPerformersService.findByEventId(eventId);
    return ResponseEntity.ok().body(eventFeaturedPerformers);
  }

  /**
   * {@code GET  /event-featured-performers/event/:eventId/active} : get all
   * active eventFeaturedPerformers for a specific event.
   *
   * @param eventId  the id of the event.
   * @param isActive active status.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
   *         of eventFeaturedPerformers in body.
   */
  @GetMapping("/event/{eventId}/active")
  public ResponseEntity<List<EventFeaturedPerformersDTO>> getEventFeaturedPerformersByEventIdAndActive(
      @PathVariable("eventId") Long eventId,
      @RequestParam("isActive") Boolean isActive) {
    log.debug("REST request to get EventFeaturedPerformers for event : {} and active status : {}", eventId, isActive);
    List<EventFeaturedPerformersDTO> eventFeaturedPerformers = eventFeaturedPerformersService
        .findByEventIdAndIsActive(eventId, isActive);
    return ResponseEntity.ok().body(eventFeaturedPerformers);
  }

  /**
   * {@code GET  /event-featured-performers/event/:eventId/priority} : get all
   * eventFeaturedPerformers for a specific event ordered by priority.
   *
   * @param eventId the id of the event.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
   *         of eventFeaturedPerformers in body.
   */
  @GetMapping("/event/{eventId}/priority")
  public ResponseEntity<List<EventFeaturedPerformersDTO>> getEventFeaturedPerformersByEventIdOrderByPriority(
      @PathVariable("eventId") Long eventId) {
    log.debug("REST request to get EventFeaturedPerformers for event : {} ordered by priority", eventId);
    List<EventFeaturedPerformersDTO> eventFeaturedPerformers = eventFeaturedPerformersService
        .findByEventIdOrderByPriorityRanking(eventId);
    return ResponseEntity.ok().body(eventFeaturedPerformers);
  }

  /**
   * {@code GET  /event-featured-performers/event/:eventId/headliners} : get all
   * headliner eventFeaturedPerformers for a specific event.
   *
   * @param eventId     the id of the event.
   * @param isHeadliner headliner status.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
   *         of eventFeaturedPerformers in body.
   */
  @GetMapping("/event/{eventId}/headliners")
  public ResponseEntity<List<EventFeaturedPerformersDTO>> getEventFeaturedPerformersByEventIdAndHeadliner(
      @PathVariable("eventId") Long eventId,
      @RequestParam("isHeadliner") Boolean isHeadliner) {
    log.debug("REST request to get headliner EventFeaturedPerformers for event : {} and headliner status : {}", eventId,
        isHeadliner);
    List<EventFeaturedPerformersDTO> eventFeaturedPerformers = eventFeaturedPerformersService
        .findByEventIdAndIsHeadliner(eventId, isHeadliner);
    return ResponseEntity.ok().body(eventFeaturedPerformers);
  }
}
