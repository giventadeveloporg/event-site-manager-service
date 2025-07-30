package com.nextjstemplate.web.rest;

import com.nextjstemplate.repository.EventTicketTransactionRepository;
import com.nextjstemplate.service.EventTicketTransactionQueryService;
import com.nextjstemplate.service.EventTicketTransactionService;
import com.nextjstemplate.service.EmailSenderService;
import com.nextjstemplate.service.criteria.EventTicketTransactionCriteria;
import com.nextjstemplate.service.dto.EventTicketTransactionDTO;
import com.nextjstemplate.service.dto.EventTicketTransactionStatisticsDTO;
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
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing
 * {@link com.nextjstemplate.domain.EventTicketTransaction}.
 */
@RestController
@RequestMapping("/api/event-ticket-transactions")
public class EventTicketTransactionResource {

    private final Logger log = LoggerFactory.getLogger(EventTicketTransactionResource.class);

    private static final String ENTITY_NAME = "eventTicketTransaction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventTicketTransactionService eventTicketTransactionService;

    private final EventTicketTransactionRepository eventTicketTransactionRepository;

    private final EventTicketTransactionQueryService eventTicketTransactionQueryService;

    private final EmailSenderService emailSenderService;

    public EventTicketTransactionResource(
            EventTicketTransactionService eventTicketTransactionService,
            EventTicketTransactionRepository eventTicketTransactionRepository,
            EventTicketTransactionQueryService eventTicketTransactionQueryService,
            EmailSenderService emailSenderService) {
        this.eventTicketTransactionService = eventTicketTransactionService;
        this.eventTicketTransactionRepository = eventTicketTransactionRepository;
        this.eventTicketTransactionQueryService = eventTicketTransactionQueryService;
        this.emailSenderService = emailSenderService;
    }

    /**
     * {@code POST  /event-ticket-transactions} : Create a new
     * eventTicketTransaction.
     *
     * @param eventTicketTransactionDTO the eventTicketTransactionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new eventTicketTransactionDTO, or with status
     *         {@code 400 (Bad Request)} if the eventTicketTransaction has already
     *         an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EventTicketTransactionDTO> createEventTicketTransaction(
            @Valid @RequestBody EventTicketTransactionDTO eventTicketTransactionDTO) throws URISyntaxException {
        log.debug("REST request to save EventTicketTransaction : {}", eventTicketTransactionDTO);
        if (eventTicketTransactionDTO.getId() != null) {
            throw new BadRequestAlertException("A new eventTicketTransaction cannot already have an ID", ENTITY_NAME,
                    "idexists");
        }
        EventTicketTransactionDTO result = eventTicketTransactionService.save(eventTicketTransactionDTO);
        return ResponseEntity
                .created(new URI("/api/event-ticket-transactions/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME,
                        result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /event-ticket-transactions/:id} : Updates an existing
     * eventTicketTransaction.
     *
     * @param id                        the id of the eventTicketTransactionDTO to
     *                                  save.
     * @param eventTicketTransactionDTO the eventTicketTransactionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated eventTicketTransactionDTO,
     *         or with status {@code 400 (Bad Request)} if the
     *         eventTicketTransactionDTO is not valid,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         eventTicketTransactionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventTicketTransactionDTO> updateEventTicketTransaction(
            @PathVariable(value = "id", required = false) final Long id,
            @Valid @RequestBody EventTicketTransactionDTO eventTicketTransactionDTO) throws URISyntaxException {
        log.debug("REST request to update EventTicketTransaction : {}, {}", id, eventTicketTransactionDTO);
        if (eventTicketTransactionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventTicketTransactionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventTicketTransactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EventTicketTransactionDTO result = eventTicketTransactionService.update(eventTicketTransactionDTO);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
                        eventTicketTransactionDTO.getId().toString()))
                .body(result);
    }

    /**
     * {@code PATCH  /event-ticket-transactions/:id} : Partial updates given fields
     * of an existing eventTicketTransaction, field will ignore if it is null
     *
     * @param id                        the id of the eventTicketTransactionDTO to
     *                                  save.
     * @param eventTicketTransactionDTO the eventTicketTransactionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated eventTicketTransactionDTO,
     *         or with status {@code 400 (Bad Request)} if the
     *         eventTicketTransactionDTO is not valid,
     *         or with status {@code 404 (Not Found)} if the
     *         eventTicketTransactionDTO is not found,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         eventTicketTransactionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventTicketTransactionDTO> partialUpdateEventTicketTransaction(
            @PathVariable(value = "id", required = false) final Long id,
            @NotNull @RequestBody EventTicketTransactionDTO eventTicketTransactionDTO) throws URISyntaxException {
        log.debug("REST request to partial update EventTicketTransaction partially : {}, {}", id,
                eventTicketTransactionDTO);
        if (eventTicketTransactionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventTicketTransactionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventTicketTransactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EventTicketTransactionDTO> result = eventTicketTransactionService
                .partialUpdate(eventTicketTransactionDTO);

        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
                        eventTicketTransactionDTO.getId().toString()));
    }

    /**
     * {@code GET  /event-ticket-transactions} : get all the
     * eventTicketTransactions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of eventTicketTransactions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EventTicketTransactionDTO>> getAllEventTicketTransactions(
            EventTicketTransactionCriteria criteria,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get EventTicketTransactions by criteria: {}", criteria);

        // Special handling for ID-based searches to avoid pagination issues
        if (criteria != null && criteria.getId() != null && criteria.getId().getEquals() != null) {
            // For ID searches, get the specific record and return it regardless of page
            Long id = criteria.getId().getEquals();
            Optional<EventTicketTransactionDTO> result = eventTicketTransactionService.findOne(id);
            if (result.isPresent()) {
                return ResponseEntity.ok().body(List.of(result.orElseThrow()));
            } else {
                return ResponseEntity.ok().body(List.of());
            }
        }

        Page<EventTicketTransactionDTO> page = eventTicketTransactionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil
                .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /event-ticket-transactions/count} : count all the
     * eventTicketTransactions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
     *         in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEventTicketTransactions(EventTicketTransactionCriteria criteria) {
        log.debug("REST request to count EventTicketTransactions by criteria: {}", criteria);
        return ResponseEntity.ok().body(eventTicketTransactionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /event-ticket-transactions/:id} : get the "id"
     * eventTicketTransaction.
     *
     * @param id the id of the eventTicketTransactionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the eventTicketTransactionDTO, or with status
     *         {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventTicketTransactionDTO> getEventTicketTransaction(@PathVariable Long id) {
        log.debug("REST request to get EventTicketTransaction : {}", id);
        Optional<EventTicketTransactionDTO> eventTicketTransactionDTO = eventTicketTransactionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventTicketTransactionDTO);
    }

    /**
     * {@code GET  /event-ticket-transactions/search/by-transaction-id} : search for
     * eventTicketTransaction by transaction ID.
     *
     * @param transactionId the transaction ID to search for.
     * @param tenantId      the tenant ID for filtering.
     * @param eventId       the event ID for filtering.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the list of matching eventTicketTransactionDTOs.
     */
    @GetMapping("/search/by-transaction-id")
    public ResponseEntity<List<EventTicketTransactionDTO>> searchByTransactionId(
            @RequestParam(required = false) Long transactionId,
            @RequestParam(required = false) String tenantId,
            @RequestParam(required = false) Long eventId) {
        log.debug("REST request to search EventTicketTransaction by transaction ID: {}", transactionId);

        if (transactionId == null) {
            return ResponseEntity.badRequest().build();
        }

        EventTicketTransactionCriteria criteria = new EventTicketTransactionCriteria();
        criteria.setId(new LongFilter());
        criteria.getId().setEquals(transactionId);

        if (tenantId != null) {
            criteria.setTenantId(new StringFilter());
            criteria.getTenantId().setEquals(tenantId);
        }

        if (eventId != null) {
            criteria.setEventId(new LongFilter());
            criteria.getEventId().setEquals(eventId);
        }

        List<EventTicketTransactionDTO> results = eventTicketTransactionQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(results);
    }

    /**
     * {@code GET  /event-ticket-transactions/search/by-name} : search for
     * eventTicketTransaction by name.
     *
     * @param name     the name to search for (can be first name, last name, or full
     *                 name).
     * @param tenantId the tenant ID for filtering.
     * @param eventId  the event ID for filtering.
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the list of matching eventTicketTransactionDTOs.
     */
    @GetMapping("/search/by-name")
    public ResponseEntity<List<EventTicketTransactionDTO>> searchByName(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String tenantId,
            @RequestParam(required = false) Long eventId,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to search EventTicketTransaction by name: {}", name);

        if (name == null || name.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        EventTicketTransactionCriteria criteria = new EventTicketTransactionCriteria();
        criteria.setFirstName(new StringFilter());
        criteria.getFirstName().setContains(name.trim());

        if (tenantId != null) {
            criteria.setTenantId(new StringFilter());
            criteria.getTenantId().setEquals(tenantId);
        }

        if (eventId != null) {
            criteria.setEventId(new LongFilter());
            criteria.getEventId().setEquals(eventId);
        }

        Page<EventTicketTransactionDTO> page = eventTicketTransactionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil
                .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code DELETE  /event-ticket-transactions/:id} : delete the "id"
     * eventTicketTransaction.
     *
     * @param id the id of the eventTicketTransactionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventTicketTransaction(@PathVariable Long id) {
        log.debug("REST request to delete EventTicketTransaction : {}", id);
        eventTicketTransactionService.delete(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build();
    }

    /**
     * {@code GET /event-ticket-transactions/statistics/{eventId}} : get statistics
     * for an event.
     *
     * @param eventId the id of the event.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the
     *         statistics in body.
     */
    @GetMapping("/statistics/{eventId}")
    public ResponseEntity<EventTicketTransactionStatisticsDTO> getEventStatistics(@PathVariable Long eventId) {
        log.debug("REST request to get statistics for event : {}", eventId);
        EventTicketTransactionStatisticsDTO stats = eventTicketTransactionService.getEventStatistics(eventId);
        return ResponseEntity.ok(stats);
    }
}
