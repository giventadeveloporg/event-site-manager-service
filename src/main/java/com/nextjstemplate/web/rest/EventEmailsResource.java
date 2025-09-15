package com.nextjstemplate.web.rest;

import com.nextjstemplate.repository.EventEmailsRepository;
import com.nextjstemplate.service.EventEmailsQueryService;
import com.nextjstemplate.service.EventEmailsService;
import com.nextjstemplate.service.criteria.EventEmailsCriteria;
import com.nextjstemplate.service.dto.EventEmailsDTO;
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
 * REST controller for managing {@link com.nextjstemplate.domain.EventEmails}.
 */
@RestController
@RequestMapping("/api/event-emails")
public class EventEmailsResource {

  private final Logger log = LoggerFactory.getLogger(EventEmailsResource.class);

  private static final String ENTITY_NAME = "eventEmails";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final EventEmailsService eventEmailsService;

  private final EventEmailsRepository eventEmailsRepository;

  private final EventEmailsQueryService eventEmailsQueryService;

  public EventEmailsResource(
      EventEmailsService eventEmailsService,
      EventEmailsRepository eventEmailsRepository,
      EventEmailsQueryService eventEmailsQueryService) {
    this.eventEmailsService = eventEmailsService;
    this.eventEmailsRepository = eventEmailsRepository;
    this.eventEmailsQueryService = eventEmailsQueryService;
  }

  /**
   * {@code POST  /event-emails} : Create a new eventEmails.
   *
   * @param eventEmailsDTO the eventEmailsDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
   *         body the new eventEmailsDTO, or with status {@code 400 (Bad Request)}
   *         if the eventEmails has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("")
  public ResponseEntity<EventEmailsDTO> createEventEmails(@Valid @RequestBody EventEmailsDTO eventEmailsDTO)
      throws URISyntaxException {
    log.debug("REST request to save EventEmails : {}", eventEmailsDTO);
    if (eventEmailsDTO.getId() != null) {
      throw new BadRequestAlertException("A new eventEmails cannot already have an ID", ENTITY_NAME, "idexists");
    }
    eventEmailsDTO = eventEmailsService.save(eventEmailsDTO);
    return ResponseEntity
        .created(new URI("/api/event-emails/" + eventEmailsDTO.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, eventEmailsDTO.getId().toString()))
        .body(eventEmailsDTO);
  }

  /**
   * {@code PUT  /event-emails/:id} : Updates an existing eventEmails.
   *
   * @param id             the id of the eventEmailsDTO to save.
   * @param eventEmailsDTO the eventEmailsDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
   *         the updated eventEmailsDTO,
   *         or with status {@code 400 (Bad Request)} if the eventEmailsDTO is not
   *         valid,
   *         or with status {@code 500 (Internal Server Error)} if the
   *         eventEmailsDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/{id}")
  public ResponseEntity<EventEmailsDTO> updateEventEmails(
      @PathVariable(value = "id", required = false) final Long id,
      @Valid @RequestBody EventEmailsDTO eventEmailsDTO) throws URISyntaxException {
    log.debug("REST request to update EventEmails : {}, {}", id, eventEmailsDTO);
    if (eventEmailsDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, eventEmailsDTO.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!eventEmailsRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    eventEmailsDTO = eventEmailsService.update(eventEmailsDTO);
    return ResponseEntity
        .ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventEmailsDTO.getId().toString()))
        .body(eventEmailsDTO);
  }

  /**
   * {@code PATCH  /event-emails/:id} : Partial updates given fields of an
   * existing eventEmails, field will ignore if it is null
   *
   * @param id             the id of the eventEmailsDTO to save.
   * @param eventEmailsDTO the eventEmailsDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
   *         the updated eventEmailsDTO,
   *         or with status {@code 400 (Bad Request)} if the eventEmailsDTO is not
   *         valid,
   *         or with status {@code 404 (Not Found)} if the eventEmailsDTO is not
   *         found,
   *         or with status {@code 500 (Internal Server Error)} if the
   *         eventEmailsDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
  public ResponseEntity<EventEmailsDTO> partialUpdateEventEmails(
      @PathVariable(value = "id", required = false) final Long id,
      @NotNull @RequestBody EventEmailsDTO eventEmailsDTO) throws URISyntaxException {
    log.debug("REST request to partial update EventEmails partially : {}, {}", id, eventEmailsDTO);
    if (eventEmailsDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, eventEmailsDTO.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!eventEmailsRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    Optional<EventEmailsDTO> result = eventEmailsService.partialUpdate(eventEmailsDTO);

    return ResponseUtil.wrapOrNotFound(
        result,
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventEmailsDTO.getId().toString()));
  }

  /**
   * {@code GET  /event-emails} : get all the eventEmails.
   *
   * @param pageable the pagination information.
   * @param criteria the criteria which the requested entities should match.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
   *         of eventEmails in body.
   */
  @GetMapping("")
  public ResponseEntity<List<EventEmailsDTO>> getAllEventEmails(
      EventEmailsCriteria criteria,
      @org.springdoc.core.annotations.ParameterObject Pageable pageable) {
    log.debug("REST request to get EventEmails by criteria: {}", criteria);

    final Page<EventEmailsDTO> page = eventEmailsQueryService.findByCriteria(criteria, pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(),
        page);
    return ResponseEntity.ok().headers(headers).body(page.getContent());
  }

  /**
   * {@code GET  /event-emails/count} : count all the eventEmails.
   *
   * @param criteria the criteria which the requested entities should match.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
   *         in body.
   */
  @GetMapping("/count")
  public ResponseEntity<Long> countEventEmails(EventEmailsCriteria criteria) {
    log.debug("REST request to count EventEmails by criteria: {}", criteria);
    return ResponseEntity.ok().body(eventEmailsQueryService.countByCriteria(criteria));
  }

  /**
   * {@code GET  /event-emails/:id} : get the "id" eventEmails.
   *
   * @param id the id of the eventEmailsDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
   *         the eventEmailsDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/{id}")
  public ResponseEntity<EventEmailsDTO> getEventEmails(@PathVariable("id") Long id) {
    log.debug("REST request to get EventEmails : {}", id);
    Optional<EventEmailsDTO> eventEmailsDTO = eventEmailsService.findOne(id);
    return ResponseUtil.wrapOrNotFound(eventEmailsDTO);
  }

  /**
   * {@code DELETE  /event-emails/:id} : delete the "id" eventEmails.
   *
   * @param id the id of the eventEmailsDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (No Content)}.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteEventEmails(@PathVariable("id") Long id) {
    log.debug("REST request to delete EventEmails : {}", id);
    eventEmailsService.delete(id);
    return ResponseEntity
        .noContent()
        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }

  /**
   * {@code GET  /event-emails/event/:eventId} : get all eventEmails for a
   * specific event.
   *
   * @param eventId the id of the event.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
   *         of eventEmails in body.
   */
  @GetMapping("/event/{eventId}")
  public ResponseEntity<List<EventEmailsDTO>> getEventEmailsByEventId(@PathVariable("eventId") Long eventId) {
    log.debug("REST request to get EventEmails for event : {}", eventId);
    List<EventEmailsDTO> eventEmails = eventEmailsService.findByEventId(eventId);
    return ResponseEntity.ok().body(eventEmails);
  }

  /**
   * {@code GET  /event-emails/event/:eventId/distinct} : get distinct email
   * addresses for a specific event.
   *
   * @param eventId the id of the event.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
   *         of distinct email addresses in body.
   */
  @GetMapping("/event/{eventId}/distinct")
  public ResponseEntity<List<String>> getDistinctEmailsByEventId(@PathVariable("eventId") Long eventId) {
    log.debug("REST request to get distinct emails for event : {}", eventId);
    List<String> distinctEmails = eventEmailsService.findDistinctEmailsByEventId(eventId);
    return ResponseEntity.ok().body(distinctEmails);
  }
}
