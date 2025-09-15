package com.nextjstemplate.web.rest;

import com.nextjstemplate.repository.EventSponsorsJoinRepository;
import com.nextjstemplate.service.EventSponsorsJoinQueryService;
import com.nextjstemplate.service.EventSponsorsJoinService;
import com.nextjstemplate.service.criteria.EventSponsorsJoinCriteria;
import com.nextjstemplate.service.dto.EventSponsorsJoinDTO;
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
 * {@link com.nextjstemplate.domain.EventSponsorsJoin}.
 */
@RestController
@RequestMapping("/api/event-sponsors-join")
public class EventSponsorsJoinResource {

  private final Logger log = LoggerFactory.getLogger(EventSponsorsJoinResource.class);

  private static final String ENTITY_NAME = "eventSponsorsJoin";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final EventSponsorsJoinService eventSponsorsJoinService;

  private final EventSponsorsJoinRepository eventSponsorsJoinRepository;

  private final EventSponsorsJoinQueryService eventSponsorsJoinQueryService;

  public EventSponsorsJoinResource(
      EventSponsorsJoinService eventSponsorsJoinService,
      EventSponsorsJoinRepository eventSponsorsJoinRepository,
      EventSponsorsJoinQueryService eventSponsorsJoinQueryService) {
    this.eventSponsorsJoinService = eventSponsorsJoinService;
    this.eventSponsorsJoinRepository = eventSponsorsJoinRepository;
    this.eventSponsorsJoinQueryService = eventSponsorsJoinQueryService;
  }

  /**
   * {@code POST  /event-sponsors-join} : Create a new eventSponsorsJoin.
   *
   * @param eventSponsorsJoinDTO the eventSponsorsJoinDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
   *         body the new eventSponsorsJoinDTO, or with status
   *         {@code 400 (Bad Request)} if the eventSponsorsJoin has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("")
  public ResponseEntity<EventSponsorsJoinDTO> createEventSponsorsJoin(
      @Valid @RequestBody EventSponsorsJoinDTO eventSponsorsJoinDTO)
      throws URISyntaxException {
    log.debug("REST request to save EventSponsorsJoin : {}", eventSponsorsJoinDTO);
    if (eventSponsorsJoinDTO.getId() != null) {
      throw new BadRequestAlertException("A new eventSponsorsJoin cannot already have an ID", ENTITY_NAME, "idexists");
    }
    eventSponsorsJoinDTO = eventSponsorsJoinService.save(eventSponsorsJoinDTO);
    return ResponseEntity
        .created(new URI("/api/event-sponsors-join/" + eventSponsorsJoinDTO.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME,
            eventSponsorsJoinDTO.getId().toString()))
        .body(eventSponsorsJoinDTO);
  }

  /**
   * {@code PUT  /event-sponsors-join/:id} : Updates an existing
   * eventSponsorsJoin.
   *
   * @param id                   the id of the eventSponsorsJoinDTO to save.
   * @param eventSponsorsJoinDTO the eventSponsorsJoinDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
   *         the updated eventSponsorsJoinDTO,
   *         or with status {@code 400 (Bad Request)} if the eventSponsorsJoinDTO
   *         is not valid,
   *         or with status {@code 500 (Internal Server Error)} if the
   *         eventSponsorsJoinDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/{id}")
  public ResponseEntity<EventSponsorsJoinDTO> updateEventSponsorsJoin(
      @PathVariable(value = "id", required = false) final Long id,
      @Valid @RequestBody EventSponsorsJoinDTO eventSponsorsJoinDTO) throws URISyntaxException {
    log.debug("REST request to update EventSponsorsJoin : {}, {}", id, eventSponsorsJoinDTO);
    if (eventSponsorsJoinDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, eventSponsorsJoinDTO.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!eventSponsorsJoinRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    eventSponsorsJoinDTO = eventSponsorsJoinService.update(eventSponsorsJoinDTO);
    return ResponseEntity
        .ok()
        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
            eventSponsorsJoinDTO.getId().toString()))
        .body(eventSponsorsJoinDTO);
  }

  /**
   * {@code PATCH  /event-sponsors-join/:id} : Partial updates given fields of an
   * existing eventSponsorsJoin, field will ignore if it is null
   *
   * @param id                   the id of the eventSponsorsJoinDTO to save.
   * @param eventSponsorsJoinDTO the eventSponsorsJoinDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
   *         the updated eventSponsorsJoinDTO,
   *         or with status {@code 400 (Bad Request)} if the eventSponsorsJoinDTO
   *         is not valid,
   *         or with status {@code 404 (Not Found)} if the eventSponsorsJoinDTO is
   *         not found,
   *         or with status {@code 500 (Internal Server Error)} if the
   *         eventSponsorsJoinDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
  public ResponseEntity<EventSponsorsJoinDTO> partialUpdateEventSponsorsJoin(
      @PathVariable(value = "id", required = false) final Long id,
      @NotNull @RequestBody EventSponsorsJoinDTO eventSponsorsJoinDTO) throws URISyntaxException {
    log.debug("REST request to partial update EventSponsorsJoin partially : {}, {}", id, eventSponsorsJoinDTO);
    if (eventSponsorsJoinDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, eventSponsorsJoinDTO.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!eventSponsorsJoinRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    Optional<EventSponsorsJoinDTO> result = eventSponsorsJoinService.partialUpdate(eventSponsorsJoinDTO);

    return ResponseUtil.wrapOrNotFound(
        result,
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
            eventSponsorsJoinDTO.getId().toString()));
  }

  /**
   * {@code GET  /event-sponsors-join} : get all the eventSponsorsJoin.
   *
   * @param pageable the pagination information.
   * @param criteria the criteria which the requested entities should match.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
   *         of eventSponsorsJoin in body.
   */
  @GetMapping("")
  public ResponseEntity<List<EventSponsorsJoinDTO>> getAllEventSponsorsJoin(
      EventSponsorsJoinCriteria criteria,
      @org.springdoc.core.annotations.ParameterObject Pageable pageable) {
    log.debug("REST request to get EventSponsorsJoin by criteria: {}", criteria);

    final Page<EventSponsorsJoinDTO> page = eventSponsorsJoinQueryService.findByCriteria(criteria, pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(),
        page);
    return ResponseEntity.ok().headers(headers).body(page.getContent());
  }

  /**
   * {@code GET  /event-sponsors-join/count} : count all the eventSponsorsJoin.
   *
   * @param criteria the criteria which the requested entities should match.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
   *         in body.
   */
  @GetMapping("/count")
  public ResponseEntity<Long> countEventSponsorsJoin(EventSponsorsJoinCriteria criteria) {
    log.debug("REST request to count EventSponsorsJoin by criteria: {}", criteria);
    return ResponseEntity.ok().body(eventSponsorsJoinQueryService.countByCriteria(criteria));
  }

  /**
   * {@code GET  /event-sponsors-join/:id} : get the "id" eventSponsorsJoin.
   *
   * @param id the id of the eventSponsorsJoinDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
   *         the eventSponsorsJoinDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/{id}")
  public ResponseEntity<EventSponsorsJoinDTO> getEventSponsorsJoin(@PathVariable("id") Long id) {
    log.debug("REST request to get EventSponsorsJoin : {}", id);
    Optional<EventSponsorsJoinDTO> eventSponsorsJoinDTO = eventSponsorsJoinService.findOne(id);
    return ResponseUtil.wrapOrNotFound(eventSponsorsJoinDTO);
  }

  /**
   * {@code DELETE  /event-sponsors-join/:id} : delete the "id" eventSponsorsJoin.
   *
   * @param id the id of the eventSponsorsJoinDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (No Content)}.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteEventSponsorsJoin(@PathVariable("id") Long id) {
    log.debug("REST request to delete EventSponsorsJoin : {}", id);
    eventSponsorsJoinService.delete(id);
    return ResponseEntity
        .noContent()
        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }

  /**
   * {@code GET  /event-sponsors-join/event/:eventId} : get all eventSponsorsJoin
   * for a specific event.
   *
   * @param eventId the id of the event.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
   *         of eventSponsorsJoin in body.
   */
  @GetMapping("/event/{eventId}")
  public ResponseEntity<List<EventSponsorsJoinDTO>> getEventSponsorsJoinByEventId(
      @PathVariable("eventId") Long eventId) {
    log.debug("REST request to get EventSponsorsJoin for event : {}", eventId);
    List<EventSponsorsJoinDTO> eventSponsorsJoin = eventSponsorsJoinService.findByEventId(eventId);
    return ResponseEntity.ok().body(eventSponsorsJoin);
  }

  /**
   * {@code GET  /event-sponsors-join/sponsor/:sponsorId} : get all
   * eventSponsorsJoin for a specific sponsor.
   *
   * @param sponsorId the id of the sponsor.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
   *         of eventSponsorsJoin in body.
   */
  @GetMapping("/sponsor/{sponsorId}")
  public ResponseEntity<List<EventSponsorsJoinDTO>> getEventSponsorsJoinBySponsorId(
      @PathVariable("sponsorId") Long sponsorId) {
    log.debug("REST request to get EventSponsorsJoin for sponsor : {}", sponsorId);
    List<EventSponsorsJoinDTO> eventSponsorsJoin = eventSponsorsJoinService.findBySponsorId(sponsorId);
    return ResponseEntity.ok().body(eventSponsorsJoin);
  }

  /**
   * {@code GET  /event-sponsors-join/event/:eventId/sponsor/:sponsorId} : get
   * specific event-sponsor association.
   *
   * @param eventId   the id of the event.
   * @param sponsorId the id of the sponsor.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
   *         the eventSponsorsJoinDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/event/{eventId}/sponsor/{sponsorId}")
  public ResponseEntity<EventSponsorsJoinDTO> getEventSponsorsJoinByEventIdAndSponsorId(
      @PathVariable("eventId") Long eventId,
      @PathVariable("sponsorId") Long sponsorId) {
    log.debug("REST request to get EventSponsorsJoin for event : {} and sponsor : {}", eventId, sponsorId);
    Optional<EventSponsorsJoinDTO> eventSponsorsJoinDTO = eventSponsorsJoinService.findByEventIdAndSponsorId(eventId,
        sponsorId);
    return ResponseUtil.wrapOrNotFound(eventSponsorsJoinDTO);
  }

  /**
   * {@code DELETE  /event-sponsors-join/event/:eventId/sponsor/:sponsorId} :
   * delete specific event-sponsor association.
   *
   * @param eventId   the id of the event.
   * @param sponsorId the id of the sponsor.
   * @return the {@link ResponseEntity} with status {@code 204 (No Content)}.
   */
  @DeleteMapping("/event/{eventId}/sponsor/{sponsorId}")
  public ResponseEntity<Void> deleteEventSponsorsJoinByEventIdAndSponsorId(
      @PathVariable("eventId") Long eventId,
      @PathVariable("sponsorId") Long sponsorId) {
    log.debug("REST request to delete EventSponsorsJoin for event : {} and sponsor : {}", eventId, sponsorId);
    eventSponsorsJoinService.deleteByEventIdAndSponsorId(eventId, sponsorId);
    return ResponseEntity
        .noContent()
        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, eventId + "-" + sponsorId))
        .build();
  }
}
