package com.nextjstemplate.web.rest;

import com.nextjstemplate.repository.EventTicketTransactionItemRepository;
import com.nextjstemplate.service.EventTicketTransactionItemQueryService;
import com.nextjstemplate.service.EventTicketTransactionItemService;
import com.nextjstemplate.service.criteria.EventTicketTransactionItemCriteria;
import com.nextjstemplate.service.dto.EventTicketTransactionItemDTO;
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
 * {@link com.nextjstemplate.domain.EventTicketTransactionItem}.
 */
@RestController
@RequestMapping("/api/event-ticket-transaction-items")
public class EventTicketTransactionItemResource {

    private final Logger log = LoggerFactory.getLogger(EventTicketTransactionItemResource.class);

    private static final String ENTITY_NAME = "eventTicketTransactionItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventTicketTransactionItemService eventTicketTransactionItemService;

    private final EventTicketTransactionItemRepository eventTicketTransactionItemRepository;

    private final EventTicketTransactionItemQueryService eventTicketTransactionItemQueryService;

    public EventTicketTransactionItemResource(
        EventTicketTransactionItemService eventTicketTransactionItemService,
        EventTicketTransactionItemRepository eventTicketTransactionItemRepository,
        EventTicketTransactionItemQueryService eventTicketTransactionItemQueryService
    ) {
        this.eventTicketTransactionItemService = eventTicketTransactionItemService;
        this.eventTicketTransactionItemRepository = eventTicketTransactionItemRepository;
        this.eventTicketTransactionItemQueryService = eventTicketTransactionItemQueryService;
    }

    /**
     * {@code POST  /event-ticket-transaction-items} : Create a new
     * eventTicketTransactionItem.
     *
     * @param eventTicketTransactionItemDTO the eventTicketTransactionItemDTO to
     *                                      create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new eventTicketTransactionItemDTO, or with status
     *         {@code 400 (Bad Request)} if the eventTicketTransactionItem has
     *         already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EventTicketTransactionItemDTO> createEventTicketTransactionItem(
        @Valid @RequestBody EventTicketTransactionItemDTO eventTicketTransactionItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to save EventTicketTransactionItem : {}", eventTicketTransactionItemDTO);
        if (eventTicketTransactionItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new eventTicketTransactionItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventTicketTransactionItemDTO result = eventTicketTransactionItemService.save(eventTicketTransactionItemDTO);
        return ResponseEntity
            .created(new URI("/api/event-ticket-transaction-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /event-ticket-transaction-items/:id} : Updates an existing
     * eventTicketTransactionItem.
     *
     * @param id                            the id of the
     *                                      eventTicketTransactionItemDTO to save.
     * @param eventTicketTransactionItemDTO the eventTicketTransactionItemDTO to
     *                                      update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated eventTicketTransactionItemDTO,
     *         or with status {@code 400 (Bad Request)} if the
     *         eventTicketTransactionItemDTO is not valid,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         eventTicketTransactionItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventTicketTransactionItemDTO> updateEventTicketTransactionItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EventTicketTransactionItemDTO eventTicketTransactionItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EventTicketTransactionItem : {}, {}", id, eventTicketTransactionItemDTO);
        if (eventTicketTransactionItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventTicketTransactionItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventTicketTransactionItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EventTicketTransactionItemDTO result = eventTicketTransactionItemService.update(eventTicketTransactionItemDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventTicketTransactionItemDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /event-ticket-transaction-items/:id} : Partial updates given
     * fields of an existing eventTicketTransactionItem, field will ignore if it is
     * null
     *
     * @param id                            the id of the
     *                                      eventTicketTransactionItemDTO to save.
     * @param eventTicketTransactionItemDTO the eventTicketTransactionItemDTO to
     *                                      update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated eventTicketTransactionItemDTO,
     *         or with status {@code 400 (Bad Request)} if the
     *         eventTicketTransactionItemDTO is not valid,
     *         or with status {@code 404 (Not Found)} if the
     *         eventTicketTransactionItemDTO is not found,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         eventTicketTransactionItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventTicketTransactionItemDTO> partialUpdateEventTicketTransactionItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EventTicketTransactionItemDTO eventTicketTransactionItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EventTicketTransactionItem partially : {}, {}", id, eventTicketTransactionItemDTO);
        if (eventTicketTransactionItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventTicketTransactionItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventTicketTransactionItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EventTicketTransactionItemDTO> result = eventTicketTransactionItemService.partialUpdate(eventTicketTransactionItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventTicketTransactionItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /event-ticket-transaction-items} : get all the
     * eventTicketTransactionItems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of eventTicketTransactionItems in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EventTicketTransactionItemDTO>> getAllEventTicketTransactionItems(
        EventTicketTransactionItemCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get EventTicketTransactionItems by criteria: {}", criteria);

        Page<EventTicketTransactionItemDTO> page = eventTicketTransactionItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /event-ticket-transaction-items/count} : count all the
     * eventTicketTransactionItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
     *         in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEventTicketTransactionItems(EventTicketTransactionItemCriteria criteria) {
        log.debug("REST request to count EventTicketTransactionItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(eventTicketTransactionItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /event-ticket-transaction-items/:id} : get the "id"
     * eventTicketTransactionItem.
     *
     * @param id the id of the eventTicketTransactionItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the eventTicketTransactionItemDTO, or with status
     *         {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventTicketTransactionItemDTO> getEventTicketTransactionItem(@PathVariable Long id) {
        log.debug("REST request to get EventTicketTransactionItem : {}", id);
        Optional<EventTicketTransactionItemDTO> eventTicketTransactionItemDTO = eventTicketTransactionItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventTicketTransactionItemDTO);
    }

    /**
     * {@code DELETE  /event-ticket-transaction-items/:id} : delete the "id"
     * eventTicketTransactionItem.
     *
     * @param id the id of the eventTicketTransactionItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventTicketTransactionItem(@PathVariable Long id) {
        log.debug("REST request to delete EventTicketTransactionItem : {}", id);
        eventTicketTransactionItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code POST  /event-ticket-transaction-items/bulk} : Create multiple
     * eventTicketTransactionItems.
     *
     * @param eventTicketTransactionItemDTOs the list of DTOs to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the list of created DTOs.
     */
    @PostMapping("/bulk")
    public ResponseEntity<List<EventTicketTransactionItemDTO>> createEventTicketTransactionItemsBulk(
        @Valid @RequestBody List<EventTicketTransactionItemDTO> eventTicketTransactionItemDTOs
    ) {
        log.debug("REST request to save bulk EventTicketTransactionItems : {}", eventTicketTransactionItemDTOs);
        List<EventTicketTransactionItemDTO> result = eventTicketTransactionItemService.saveAll(eventTicketTransactionItemDTOs);
        return ResponseEntity.ok().body(result);
    }
}
