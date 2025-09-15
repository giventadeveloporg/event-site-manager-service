package com.nextjstemplate.web.rest;

import com.nextjstemplate.repository.EventProgramDirectorsRepository;
import com.nextjstemplate.service.EventProgramDirectorsQueryService;
import com.nextjstemplate.service.EventProgramDirectorsService;
import com.nextjstemplate.service.criteria.EventProgramDirectorsCriteria;
import com.nextjstemplate.service.dto.EventProgramDirectorsDTO;
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
 * {@link com.nextjstemplate.domain.EventProgramDirectors}.
 */
@RestController
@RequestMapping("/api/event-program-directors")
public class EventProgramDirectorsResource {

  private final Logger log = LoggerFactory.getLogger(EventProgramDirectorsResource.class);

  private static final String ENTITY_NAME = "eventProgramDirectors";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final EventProgramDirectorsService eventProgramDirectorsService;

  private final EventProgramDirectorsRepository eventProgramDirectorsRepository;

  private final EventProgramDirectorsQueryService eventProgramDirectorsQueryService;

  public EventProgramDirectorsResource(
      EventProgramDirectorsService eventProgramDirectorsService,
      EventProgramDirectorsRepository eventProgramDirectorsRepository,
      EventProgramDirectorsQueryService eventProgramDirectorsQueryService) {
    this.eventProgramDirectorsService = eventProgramDirectorsService;
    this.eventProgramDirectorsRepository = eventProgramDirectorsRepository;
    this.eventProgramDirectorsQueryService = eventProgramDirectorsQueryService;
  }

  /**
   * {@code POST  /event-program-directors} : Create a new eventProgramDirectors.
   *
   * @param eventProgramDirectorsDTO the eventProgramDirectorsDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
   *         body the new eventProgramDirectorsDTO, or with status
   *         {@code 400 (Bad Request)} if the eventProgramDirectors has already an
   *         ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("")
  public ResponseEntity<EventProgramDirectorsDTO> createEventProgramDirectors(
      @Valid @RequestBody EventProgramDirectorsDTO eventProgramDirectorsDTO)
      throws URISyntaxException {
    log.debug("REST request to save EventProgramDirectors : {}", eventProgramDirectorsDTO);
    if (eventProgramDirectorsDTO.getId() != null) {
      throw new BadRequestAlertException("A new eventProgramDirectors cannot already have an ID", ENTITY_NAME,
          "idexists");
    }
    eventProgramDirectorsDTO = eventProgramDirectorsService.save(eventProgramDirectorsDTO);
    return ResponseEntity
        .created(new URI("/api/event-program-directors/" + eventProgramDirectorsDTO.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME,
            eventProgramDirectorsDTO.getId().toString()))
        .body(eventProgramDirectorsDTO);
  }

  /**
   * {@code PUT  /event-program-directors/:id} : Updates an existing
   * eventProgramDirectors.
   *
   * @param id                       the id of the eventProgramDirectorsDTO to
   *                                 save.
   * @param eventProgramDirectorsDTO the eventProgramDirectorsDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
   *         the updated eventProgramDirectorsDTO,
   *         or with status {@code 400 (Bad Request)} if the
   *         eventProgramDirectorsDTO is not valid,
   *         or with status {@code 500 (Internal Server Error)} if the
   *         eventProgramDirectorsDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/{id}")
  public ResponseEntity<EventProgramDirectorsDTO> updateEventProgramDirectors(
      @PathVariable(value = "id", required = false) final Long id,
      @Valid @RequestBody EventProgramDirectorsDTO eventProgramDirectorsDTO) throws URISyntaxException {
    log.debug("REST request to update EventProgramDirectors : {}, {}", id, eventProgramDirectorsDTO);
    if (eventProgramDirectorsDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, eventProgramDirectorsDTO.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!eventProgramDirectorsRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    eventProgramDirectorsDTO = eventProgramDirectorsService.update(eventProgramDirectorsDTO);
    return ResponseEntity
        .ok()
        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
            eventProgramDirectorsDTO.getId().toString()))
        .body(eventProgramDirectorsDTO);
  }

  /**
   * {@code PATCH  /event-program-directors/:id} : Partial updates given fields of
   * an existing eventProgramDirectors, field will ignore if it is null
   *
   * @param id                       the id of the eventProgramDirectorsDTO to
   *                                 save.
   * @param eventProgramDirectorsDTO the eventProgramDirectorsDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
   *         the updated eventProgramDirectorsDTO,
   *         or with status {@code 400 (Bad Request)} if the
   *         eventProgramDirectorsDTO is not valid,
   *         or with status {@code 404 (Not Found)} if the
   *         eventProgramDirectorsDTO is not found,
   *         or with status {@code 500 (Internal Server Error)} if the
   *         eventProgramDirectorsDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
  public ResponseEntity<EventProgramDirectorsDTO> partialUpdateEventProgramDirectors(
      @PathVariable(value = "id", required = false) final Long id,
      @NotNull @RequestBody EventProgramDirectorsDTO eventProgramDirectorsDTO) throws URISyntaxException {
    log.debug("REST request to partial update EventProgramDirectors partially : {}, {}", id, eventProgramDirectorsDTO);
    if (eventProgramDirectorsDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, eventProgramDirectorsDTO.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!eventProgramDirectorsRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    Optional<EventProgramDirectorsDTO> result = eventProgramDirectorsService.partialUpdate(eventProgramDirectorsDTO);

    return ResponseUtil.wrapOrNotFound(
        result,
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
            eventProgramDirectorsDTO.getId().toString()));
  }

  /**
   * {@code GET  /event-program-directors} : get all the eventProgramDirectors.
   *
   * @param pageable the pagination information.
   * @param criteria the criteria which the requested entities should match.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
   *         of eventProgramDirectors in body.
   */
  @GetMapping("")
  public ResponseEntity<List<EventProgramDirectorsDTO>> getAllEventProgramDirectors(
      EventProgramDirectorsCriteria criteria,
      @org.springdoc.core.annotations.ParameterObject Pageable pageable) {
    log.debug("REST request to get EventProgramDirectors by criteria: {}", criteria);

    final Page<EventProgramDirectorsDTO> page = eventProgramDirectorsQueryService.findByCriteria(criteria, pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(),
        page);
    return ResponseEntity.ok().headers(headers).body(page.getContent());
  }

  /**
   * {@code GET  /event-program-directors/count} : count all the
   * eventProgramDirectors.
   *
   * @param criteria the criteria which the requested entities should match.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
   *         in body.
   */
  @GetMapping("/count")
  public ResponseEntity<Long> countEventProgramDirectors(EventProgramDirectorsCriteria criteria) {
    log.debug("REST request to count EventProgramDirectors by criteria: {}", criteria);
    return ResponseEntity.ok().body(eventProgramDirectorsQueryService.countByCriteria(criteria));
  }

  /**
   * {@code GET  /event-program-directors/:id} : get the "id"
   * eventProgramDirectors.
   *
   * @param id the id of the eventProgramDirectorsDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
   *         the eventProgramDirectorsDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/{id}")
  public ResponseEntity<EventProgramDirectorsDTO> getEventProgramDirectors(@PathVariable("id") Long id) {
    log.debug("REST request to get EventProgramDirectors : {}", id);
    Optional<EventProgramDirectorsDTO> eventProgramDirectorsDTO = eventProgramDirectorsService.findOne(id);
    return ResponseUtil.wrapOrNotFound(eventProgramDirectorsDTO);
  }

  /**
   * {@code DELETE  /event-program-directors/:id} : delete the "id"
   * eventProgramDirectors.
   *
   * @param id the id of the eventProgramDirectorsDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (No Content)}.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteEventProgramDirectors(@PathVariable("id") Long id) {
    log.debug("REST request to delete EventProgramDirectors : {}", id);
    eventProgramDirectorsService.delete(id);
    return ResponseEntity
        .noContent()
        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }

  /**
   * {@code GET  /event-program-directors/event/:eventId} : get all
   * eventProgramDirectors for a specific event.
   *
   * @param eventId the id of the event.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
   *         of eventProgramDirectors in body.
   */
  @GetMapping("/event/{eventId}")
  public ResponseEntity<List<EventProgramDirectorsDTO>> getEventProgramDirectorsByEventId(
      @PathVariable("eventId") Long eventId) {
    log.debug("REST request to get EventProgramDirectors for event : {}", eventId);
    List<EventProgramDirectorsDTO> eventProgramDirectors = eventProgramDirectorsService.findByEventId(eventId);
    return ResponseEntity.ok().body(eventProgramDirectors);
  }

  /**
   * {@code GET  /event-program-directors/event/:eventId/with-photo} : get all
   * eventProgramDirectors with photos for a specific event.
   *
   * @param eventId the id of the event.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
   *         of eventProgramDirectors in body.
   */
  @GetMapping("/event/{eventId}/with-photo")
  public ResponseEntity<List<EventProgramDirectorsDTO>> getEventProgramDirectorsWithPhotoByEventId(
      @PathVariable("eventId") Long eventId) {
    log.debug("REST request to get EventProgramDirectors with photos for event : {}", eventId);
    List<EventProgramDirectorsDTO> eventProgramDirectors = eventProgramDirectorsService.findByEventIdWithPhoto(eventId);
    return ResponseEntity.ok().body(eventProgramDirectors);
  }

  /**
   * {@code GET  /event-program-directors/event/:eventId/with-bio} : get all
   * eventProgramDirectors with bio for a specific event.
   *
   * @param eventId the id of the event.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
   *         of eventProgramDirectors in body.
   */
  @GetMapping("/event/{eventId}/with-bio")
  public ResponseEntity<List<EventProgramDirectorsDTO>> getEventProgramDirectorsWithBioByEventId(
      @PathVariable("eventId") Long eventId) {
    log.debug("REST request to get EventProgramDirectors with bio for event : {}", eventId);
    List<EventProgramDirectorsDTO> eventProgramDirectors = eventProgramDirectorsService.findByEventIdWithBio(eventId);
    return ResponseEntity.ok().body(eventProgramDirectors);
  }
}
