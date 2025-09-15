package com.nextjstemplate.web.rest;

import com.nextjstemplate.repository.EventSponsorsRepository;
import com.nextjstemplate.service.EventSponsorsQueryService;
import com.nextjstemplate.service.EventSponsorsService;
import com.nextjstemplate.service.criteria.EventSponsorsCriteria;
import com.nextjstemplate.service.dto.EventSponsorsDTO;
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
 * REST controller for managing {@link com.nextjstemplate.domain.EventSponsors}.
 */
@RestController
@RequestMapping("/api/event-sponsors")
public class EventSponsorsResource {

  private final Logger log = LoggerFactory.getLogger(EventSponsorsResource.class);

  private static final String ENTITY_NAME = "eventSponsors";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final EventSponsorsService eventSponsorsService;

  private final EventSponsorsRepository eventSponsorsRepository;

  private final EventSponsorsQueryService eventSponsorsQueryService;

  public EventSponsorsResource(
      EventSponsorsService eventSponsorsService,
      EventSponsorsRepository eventSponsorsRepository,
      EventSponsorsQueryService eventSponsorsQueryService) {
    this.eventSponsorsService = eventSponsorsService;
    this.eventSponsorsRepository = eventSponsorsRepository;
    this.eventSponsorsQueryService = eventSponsorsQueryService;
  }

  /**
   * {@code POST  /event-sponsors} : Create a new eventSponsors.
   *
   * @param eventSponsorsDTO the eventSponsorsDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
   *         body the new eventSponsorsDTO, or with status
   *         {@code 400 (Bad Request)} if the eventSponsors has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("")
  public ResponseEntity<EventSponsorsDTO> createEventSponsors(@Valid @RequestBody EventSponsorsDTO eventSponsorsDTO)
      throws URISyntaxException {
    log.debug("REST request to save EventSponsors : {}", eventSponsorsDTO);
    if (eventSponsorsDTO.getId() != null) {
      throw new BadRequestAlertException("A new eventSponsors cannot already have an ID", ENTITY_NAME, "idexists");
    }
    eventSponsorsDTO = eventSponsorsService.save(eventSponsorsDTO);
    return ResponseEntity
        .created(new URI("/api/event-sponsors/" + eventSponsorsDTO.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME,
            eventSponsorsDTO.getId().toString()))
        .body(eventSponsorsDTO);
  }

  /**
   * {@code PUT  /event-sponsors/:id} : Updates an existing eventSponsors.
   *
   * @param id               the id of the eventSponsorsDTO to save.
   * @param eventSponsorsDTO the eventSponsorsDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
   *         the updated eventSponsorsDTO,
   *         or with status {@code 400 (Bad Request)} if the eventSponsorsDTO is
   *         not valid,
   *         or with status {@code 500 (Internal Server Error)} if the
   *         eventSponsorsDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/{id}")
  public ResponseEntity<EventSponsorsDTO> updateEventSponsors(
      @PathVariable(value = "id", required = false) final Long id,
      @Valid @RequestBody EventSponsorsDTO eventSponsorsDTO) throws URISyntaxException {
    log.debug("REST request to update EventSponsors : {}, {}", id, eventSponsorsDTO);
    if (eventSponsorsDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, eventSponsorsDTO.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!eventSponsorsRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    eventSponsorsDTO = eventSponsorsService.update(eventSponsorsDTO);
    return ResponseEntity
        .ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventSponsorsDTO.getId().toString()))
        .body(eventSponsorsDTO);
  }

  /**
   * {@code PATCH  /event-sponsors/:id} : Partial updates given fields of an
   * existing eventSponsors, field will ignore if it is null
   *
   * @param id               the id of the eventSponsorsDTO to save.
   * @param eventSponsorsDTO the eventSponsorsDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
   *         the updated eventSponsorsDTO,
   *         or with status {@code 400 (Bad Request)} if the eventSponsorsDTO is
   *         not valid,
   *         or with status {@code 404 (Not Found)} if the eventSponsorsDTO is not
   *         found,
   *         or with status {@code 500 (Internal Server Error)} if the
   *         eventSponsorsDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
  public ResponseEntity<EventSponsorsDTO> partialUpdateEventSponsors(
      @PathVariable(value = "id", required = false) final Long id,
      @NotNull @RequestBody EventSponsorsDTO eventSponsorsDTO) throws URISyntaxException {
    log.debug("REST request to partial update EventSponsors partially : {}, {}", id, eventSponsorsDTO);
    if (eventSponsorsDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, eventSponsorsDTO.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!eventSponsorsRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    Optional<EventSponsorsDTO> result = eventSponsorsService.partialUpdate(eventSponsorsDTO);

    return ResponseUtil.wrapOrNotFound(
        result,
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventSponsorsDTO.getId().toString()));
  }

  /**
   * {@code GET  /event-sponsors} : get all the eventSponsors.
   *
   * @param pageable the pagination information.
   * @param criteria the criteria which the requested entities should match.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
   *         of eventSponsors in body.
   */
  @GetMapping("")
  public ResponseEntity<List<EventSponsorsDTO>> getAllEventSponsors(
      EventSponsorsCriteria criteria,
      @org.springdoc.core.annotations.ParameterObject Pageable pageable) {
    log.debug("REST request to get EventSponsors by criteria: {}", criteria);

    final Page<EventSponsorsDTO> page = eventSponsorsQueryService.findByCriteria(criteria, pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(),
        page);
    return ResponseEntity.ok().headers(headers).body(page.getContent());
  }

  /**
   * {@code GET  /event-sponsors/count} : count all the eventSponsors.
   *
   * @param criteria the criteria which the requested entities should match.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
   *         in body.
   */
  @GetMapping("/count")
  public ResponseEntity<Long> countEventSponsors(EventSponsorsCriteria criteria) {
    log.debug("REST request to count EventSponsors by criteria: {}", criteria);
    return ResponseEntity.ok().body(eventSponsorsQueryService.countByCriteria(criteria));
  }

  /**
   * {@code GET  /event-sponsors/:id} : get the "id" eventSponsors.
   *
   * @param id the id of the eventSponsorsDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
   *         the eventSponsorsDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/{id}")
  public ResponseEntity<EventSponsorsDTO> getEventSponsors(@PathVariable("id") Long id) {
    log.debug("REST request to get EventSponsors : {}", id);
    Optional<EventSponsorsDTO> eventSponsorsDTO = eventSponsorsService.findOne(id);
    return ResponseUtil.wrapOrNotFound(eventSponsorsDTO);
  }

  /**
   * {@code DELETE  /event-sponsors/:id} : delete the "id" eventSponsors.
   *
   * @param id the id of the eventSponsorsDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (No Content)}.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteEventSponsors(@PathVariable("id") Long id) {
    log.debug("REST request to delete EventSponsors : {}", id);
    eventSponsorsService.delete(id);
    return ResponseEntity
        .noContent()
        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }

  /**
   * {@code GET  /event-sponsors/active} : get all active eventSponsors.
   *
   * @param isActive active status.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
   *         of eventSponsors in body.
   */
  @GetMapping("/active")
  public ResponseEntity<List<EventSponsorsDTO>> getActiveEventSponsors(@RequestParam("isActive") Boolean isActive) {
    log.debug("REST request to get active EventSponsors : {}", isActive);
    List<EventSponsorsDTO> eventSponsors = eventSponsorsService.findByIsActive(isActive);
    return ResponseEntity.ok().body(eventSponsors);
  }

  /**
   * {@code GET  /event-sponsors/type/:type} : get all eventSponsors by type.
   *
   * @param type the sponsor type.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
   *         of eventSponsors in body.
   */
  @GetMapping("/type/{type}")
  public ResponseEntity<List<EventSponsorsDTO>> getEventSponsorsByType(@PathVariable("type") String type) {
    log.debug("REST request to get EventSponsors by type : {}", type);
    List<EventSponsorsDTO> eventSponsors = eventSponsorsService.findByType(type);
    return ResponseEntity.ok().body(eventSponsors);
  }

  /**
   * {@code GET  /event-sponsors/priority} : get all eventSponsors ordered by
   * priority.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
   *         of eventSponsors in body.
   */
  @GetMapping("/priority")
  public ResponseEntity<List<EventSponsorsDTO>> getEventSponsorsOrderByPriority() {
    log.debug("REST request to get EventSponsors ordered by priority");
    List<EventSponsorsDTO> eventSponsors = eventSponsorsService.findAllOrderByPriorityRanking();
    return ResponseEntity.ok().body(eventSponsors);
  }
}
