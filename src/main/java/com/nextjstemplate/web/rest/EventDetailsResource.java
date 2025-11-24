package com.nextjstemplate.web.rest;

import com.nextjstemplate.repository.EventDetailsRepository;
import com.nextjstemplate.service.EventDetailsQueryService;
import com.nextjstemplate.service.EventDetailsService;
import com.nextjstemplate.service.RecurringEventGenerationService;
import com.nextjstemplate.service.criteria.EventDetailsCriteria;
import com.nextjstemplate.service.dto.EventDetailsDTO;
import com.nextjstemplate.service.dto.RecurrenceConfig;
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
 * REST controller for managing {@link com.nextjstemplate.domain.EventDetails}.
 */
@RestController
@RequestMapping("/api/event-details")
public class EventDetailsResource {

    private final Logger log = LoggerFactory.getLogger(EventDetailsResource.class);

    private static final String ENTITY_NAME = "eventDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventDetailsService eventDetailsService;

    private final EventDetailsRepository eventDetailsRepository;

    private final EventDetailsQueryService eventDetailsQueryService;

    private final RecurringEventGenerationService recurringEventGenerationService;

    public EventDetailsResource(
        EventDetailsService eventDetailsService,
        EventDetailsRepository eventDetailsRepository,
        EventDetailsQueryService eventDetailsQueryService,
        RecurringEventGenerationService recurringEventGenerationService
    ) {
        this.eventDetailsService = eventDetailsService;
        this.eventDetailsRepository = eventDetailsRepository;
        this.eventDetailsQueryService = eventDetailsQueryService;
        this.recurringEventGenerationService = recurringEventGenerationService;
    }

    /**
     * {@code POST  /event-details} : Create a new eventDetails.
     *
     * @param eventDetailsDTO the eventDetailsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventDetailsDTO, or with status {@code 400 (Bad Request)} if the eventDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EventDetailsDTO> createEventDetails(@Valid @RequestBody EventDetailsDTO eventDetailsDTO)
        throws URISyntaxException {
        log.debug("REST request to save EventDetails : {}", eventDetailsDTO);
        if (eventDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new eventDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventDetailsDTO result = eventDetailsService.save(eventDetailsDTO);
        return ResponseEntity
            .created(new URI("/api/event-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /event-details/:id} : Updates an existing eventDetails.
     *
     * @param id the id of the eventDetailsDTO to save.
     * @param eventDetailsDTO the eventDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the eventDetailsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventDetailsDTO> updateEventDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EventDetailsDTO eventDetailsDTO
    ) throws URISyntaxException {
        log.info(
            "REST request to update EventDetails - ID: {}, isActive: {}, isRecurring: {}, parentEventId: {}",
            id,
            eventDetailsDTO.getIsActive(),
            eventDetailsDTO.getIsRecurring(),
            eventDetailsDTO.getParentEventId()
        );
        log.debug("REST request to update EventDetails : {}, {}", id, eventDetailsDTO);
        if (eventDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EventDetailsDTO result = eventDetailsService.update(eventDetailsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventDetailsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /event-details/:id} : Partial updates given fields of an existing eventDetails, field will ignore if it is null
     *
     * @param id the id of the eventDetailsDTO to save.
     * @param eventDetailsDTO the eventDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the eventDetailsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the eventDetailsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the eventDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventDetailsDTO> partialUpdateEventDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EventDetailsDTO eventDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EventDetails partially : {}, {}", id, eventDetailsDTO);
        if (eventDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EventDetailsDTO> result = eventDetailsService.partialUpdate(eventDetailsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventDetailsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /event-details} : get all the eventDetails.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eventDetails in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EventDetailsDTO>> getAllEventDetails(
        EventDetailsCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get EventDetails by criteria: {}", criteria);

        Page<EventDetailsDTO> page = eventDetailsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /event-details/count} : count all the eventDetails.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEventDetails(EventDetailsCriteria criteria) {
        log.debug("REST request to count EventDetails by criteria: {}", criteria);
        return ResponseEntity.ok().body(eventDetailsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /event-details/:id} : get the "id" eventDetails.
     *
     * @param id the id of the eventDetailsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventDetailsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventDetailsDTO> getEventDetails(@PathVariable Long id) {
        log.debug("REST request to get EventDetails : {}", id);
        Optional<EventDetailsDTO> eventDetailsDTO = eventDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventDetailsDTO);
    }

    /**
     * {@code DELETE  /event-details/:id} : delete the "id" eventDetails.
     *
     * @param id the id of the eventDetailsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventDetails(@PathVariable Long id) {
        log.debug("REST request to delete EventDetails : {}", id);
        eventDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code POST  /event-details/:id/generate-recurring-events} : Generate all child events for a recurring event series.
     *
     * @param id the id of the parent event.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body containing generated events.
     */
    @PostMapping("/{id}/generate-recurring-events")
    public ResponseEntity<List<EventDetailsDTO>> generateRecurringEvents(@PathVariable Long id) {
        log.debug("REST request to generate recurring events for parent event: {}", id);
        List<EventDetailsDTO> generatedEvents = recurringEventGenerationService.generateRecurringEvents(id);
        return ResponseEntity.ok().body(generatedEvents);
    }

    /**
     * {@code GET  /event-details/:id/recurring-series} : Get all events in a recurring series.
     *
     * @param id the id of the parent event.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body containing all events in the series.
     */
    @GetMapping("/{id}/recurring-series")
    public ResponseEntity<List<EventDetailsDTO>> getRecurringSeries(@PathVariable Long id) {
        log.debug("REST request to get recurring series for parent event: {}", id);
        List<EventDetailsDTO> seriesEvents = recurringEventGenerationService.getRecurringSeries(id);
        return ResponseEntity.ok().body(seriesEvents);
    }

    /**
     * {@code PUT  /event-details/:id/recurring-series} : Update recurrence configuration and optionally regenerate events.
     *
     * @param id the id of the parent event.
     * @param requestBody the request body containing recurrence config and regenerate flag.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)}.
     */
    @PutMapping("/{id}/recurring-series")
    public ResponseEntity<Void> updateRecurringSeries(@PathVariable Long id, @RequestBody UpdateRecurringSeriesRequest requestBody) {
        log.debug("REST request to update recurring series for parent event: {}", id);
        recurringEventGenerationService.updateRecurringSeries(id, requestBody.getRecurrenceConfig(), requestBody.isRegenerateEvents());
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /event-details/:id/recurring-series} : Delete a recurring event series (parent and all child events).
     *
     * @param id the id of the parent event.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}/recurring-series")
    public ResponseEntity<Void> deleteRecurringSeries(@PathVariable Long id) {
        log.debug("REST request to delete recurring series for parent event: {}", id);
        recurringEventGenerationService.deleteRecurringSeries(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Request DTO for updating recurring series.
     */
    public static class UpdateRecurringSeriesRequest {

        private RecurrenceConfig recurrenceConfig;
        private boolean regenerateEvents = false;

        public RecurrenceConfig getRecurrenceConfig() {
            return recurrenceConfig;
        }

        public void setRecurrenceConfig(RecurrenceConfig recurrenceConfig) {
            this.recurrenceConfig = recurrenceConfig;
        }

        public boolean isRegenerateEvents() {
            return regenerateEvents;
        }

        public void setRegenerateEvents(boolean regenerateEvents) {
            this.regenerateEvents = regenerateEvents;
        }
    }
}
