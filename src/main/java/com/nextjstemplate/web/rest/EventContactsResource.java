package com.nextjstemplate.web.rest;

import com.nextjstemplate.repository.EventContactsRepository;
import com.nextjstemplate.service.EventContactsQueryService;
import com.nextjstemplate.service.EventContactsService;
import com.nextjstemplate.service.criteria.EventContactsCriteria;
import com.nextjstemplate.service.dto.EventContactsDTO;
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
 * REST controller for managing {@link com.nextjstemplate.domain.EventContacts}.
 */
@RestController
@RequestMapping("/api/event-contacts")
public class EventContactsResource {

  private final Logger log = LoggerFactory.getLogger(EventContactsResource.class);

  private static final String ENTITY_NAME = "eventContacts";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final EventContactsService eventContactsService;

  private final EventContactsRepository eventContactsRepository;

  private final EventContactsQueryService eventContactsQueryService;

  public EventContactsResource(
      EventContactsService eventContactsService,
      EventContactsRepository eventContactsRepository,
      EventContactsQueryService eventContactsQueryService) {
    this.eventContactsService = eventContactsService;
    this.eventContactsRepository = eventContactsRepository;
    this.eventContactsQueryService = eventContactsQueryService;
  }

  /**
   * {@code POST  /event-contacts} : Create a new eventContacts.
   *
   * @param eventContactsDTO the eventContactsDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
   *         body the new eventContactsDTO, or with status
   *         {@code 400 (Bad Request)} if the eventContacts has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("")
  public ResponseEntity<EventContactsDTO> createEventContacts(@Valid @RequestBody EventContactsDTO eventContactsDTO)
      throws URISyntaxException {
    log.debug("REST request to save EventContacts : {}", eventContactsDTO);
    if (eventContactsDTO.getId() != null) {
      throw new BadRequestAlertException("A new eventContacts cannot already have an ID", ENTITY_NAME, "idexists");
    }
    eventContactsDTO = eventContactsService.save(eventContactsDTO);
    return ResponseEntity
        .created(new URI("/api/event-contacts/" + eventContactsDTO.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME,
            eventContactsDTO.getId().toString()))
        .body(eventContactsDTO);
  }

  /**
   * {@code PUT  /event-contacts/:id} : Updates an existing eventContacts.
   *
   * @param id               the id of the eventContactsDTO to save.
   * @param eventContactsDTO the eventContactsDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
   *         the updated eventContactsDTO,
   *         or with status {@code 400 (Bad Request)} if the eventContactsDTO is
   *         not valid,
   *         or with status {@code 500 (Internal Server Error)} if the
   *         eventContactsDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/{id}")
  public ResponseEntity<EventContactsDTO> updateEventContacts(
      @PathVariable(value = "id", required = false) final Long id,
      @Valid @RequestBody EventContactsDTO eventContactsDTO) throws URISyntaxException {
    log.debug("REST request to update EventContacts : {}, {}", id, eventContactsDTO);
    if (eventContactsDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, eventContactsDTO.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!eventContactsRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    eventContactsDTO = eventContactsService.update(eventContactsDTO);
    return ResponseEntity
        .ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventContactsDTO.getId().toString()))
        .body(eventContactsDTO);
  }

  /**
   * {@code PATCH  /event-contacts/:id} : Partial updates given fields of an
   * existing eventContacts, field will ignore if it is null
   *
   * @param id               the id of the eventContactsDTO to save.
   * @param eventContactsDTO the eventContactsDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
   *         the updated eventContactsDTO,
   *         or with status {@code 400 (Bad Request)} if the eventContactsDTO is
   *         not valid,
   *         or with status {@code 404 (Not Found)} if the eventContactsDTO is not
   *         found,
   *         or with status {@code 500 (Internal Server Error)} if the
   *         eventContactsDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
  public ResponseEntity<EventContactsDTO> partialUpdateEventContacts(
      @PathVariable(value = "id", required = false) final Long id,
      @NotNull @RequestBody EventContactsDTO eventContactsDTO) throws URISyntaxException {
    log.debug("REST request to partial update EventContacts partially : {}, {}", id, eventContactsDTO);
    if (eventContactsDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, eventContactsDTO.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!eventContactsRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    Optional<EventContactsDTO> result = eventContactsService.partialUpdate(eventContactsDTO);

    return ResponseUtil.wrapOrNotFound(
        result,
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventContactsDTO.getId().toString()));
  }

  /**
   * {@code GET  /event-contacts} : get all the eventContacts.
   *
   * @param pageable the pagination information.
   * @param criteria the criteria which the requested entities should match.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
   *         of eventContacts in body.
   */
  @GetMapping("")
  public ResponseEntity<List<EventContactsDTO>> getAllEventContacts(
      EventContactsCriteria criteria,
      @org.springdoc.core.annotations.ParameterObject Pageable pageable) {
    log.debug("REST request to get EventContacts by criteria: {}", criteria);

    final Page<EventContactsDTO> page = eventContactsQueryService.findByCriteria(criteria, pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(),
        page);
    return ResponseEntity.ok().headers(headers).body(page.getContent());
  }

  /**
   * {@code GET  /event-contacts/count} : count all the eventContacts.
   *
   * @param criteria the criteria which the requested entities should match.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
   *         in body.
   */
  @GetMapping("/count")
  public ResponseEntity<Long> countEventContacts(EventContactsCriteria criteria) {
    log.debug("REST request to count EventContacts by criteria: {}", criteria);
    return ResponseEntity.ok().body(eventContactsQueryService.countByCriteria(criteria));
  }

  /**
   * {@code GET  /event-contacts/:id} : get the "id" eventContacts.
   *
   * @param id the id of the eventContactsDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
   *         the eventContactsDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/{id}")
  public ResponseEntity<EventContactsDTO> getEventContacts(@PathVariable("id") Long id) {
    log.debug("REST request to get EventContacts : {}", id);
    Optional<EventContactsDTO> eventContactsDTO = eventContactsService.findOne(id);
    return ResponseUtil.wrapOrNotFound(eventContactsDTO);
  }

  /**
   * {@code DELETE  /event-contacts/:id} : delete the "id" eventContacts.
   *
   * @param id the id of the eventContactsDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (No Content)}.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteEventContacts(@PathVariable("id") Long id) {
    log.debug("REST request to delete EventContacts : {}", id);
    eventContactsService.delete(id);
    return ResponseEntity
        .noContent()
        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }

  /**
   * {@code GET  /event-contacts/event/:eventId} : get all eventContacts for a
   * specific event.
   *
   * @param eventId the id of the event.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
   *         of eventContacts in body.
   */
  @GetMapping("/event/{eventId}")
  public ResponseEntity<List<EventContactsDTO>> getEventContactsByEventId(@PathVariable("eventId") Long eventId) {
    log.debug("REST request to get EventContacts for event : {}", eventId);
    List<EventContactsDTO> eventContacts = eventContactsService.findByEventId(eventId);
    return ResponseEntity.ok().body(eventContacts);
  }
}
