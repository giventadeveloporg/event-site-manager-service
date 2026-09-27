package com.eventsitemanager.web.rest;

import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.LastMatchRepository;
import com.eventsitemanager.service.LastMatchQueryService;
import com.eventsitemanager.service.LastMatchService;
import com.eventsitemanager.service.criteria.LastMatchCriteria;
import com.eventsitemanager.service.dto.LastMatchDTO;
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
 * REST controller for managing {@link com.eventsitemanager.domain.LastMatch}.
 */
@RestController
@RequestMapping("/api/last-matches")
public class LastMatchResource {

    private static final Logger LOG = LoggerFactory.getLogger(LastMatchResource.class);

    private static final String ENTITY_NAME = "lastMatch";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LastMatchService lastMatchService;

    private final LastMatchRepository lastMatchRepository;

    private final LastMatchQueryService lastMatchQueryService;

    public LastMatchResource(
        LastMatchService lastMatchService,
        LastMatchRepository lastMatchRepository,
        LastMatchQueryService lastMatchQueryService
    ) {
        this.lastMatchService = lastMatchService;
        this.lastMatchRepository = lastMatchRepository;
        this.lastMatchQueryService = lastMatchQueryService;
    }

    /**
     * {@code POST  /last-matches} : Create a new lastMatch.
     */
    @PostMapping("")
    public ResponseEntity<LastMatchDTO> createLastMatch(@Valid @RequestBody LastMatchDTO lastMatchDTO) throws URISyntaxException {
        LOG.debug("REST request to save LastMatch : {}", lastMatchDTO);
        if (lastMatchDTO.getId() != null) {
            throw new BadRequestAlertException("A new lastMatch cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (lastMatchDTO.getTenantId() == null || lastMatchDTO.getTenantId().isBlank()) {
            throw new BadRequestAlertException("tenantId is required when creating a last match", ENTITY_NAME, "tenantidrequired");
        }
        lastMatchDTO = lastMatchService.save(lastMatchDTO);
        return ResponseEntity
            .created(new URI("/api/last-matches/" + lastMatchDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, lastMatchDTO.getId().toString()))
            .body(lastMatchDTO);
    }

    /**
     * {@code PUT  /last-matches/:id} : Updates an existing lastMatch.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LastMatchDTO> updateLastMatch(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LastMatchDTO lastMatchDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update LastMatch : {}, {}", id, lastMatchDTO);
        if (lastMatchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lastMatchDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lastMatchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        lastMatchDTO = lastMatchService.update(lastMatchDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lastMatchDTO.getId().toString()))
            .body(lastMatchDTO);
    }

    /**
     * {@code PATCH  /last-matches/:id} : Partial updates given fields of an existing lastMatch.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LastMatchDTO> partialUpdateLastMatch(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LastMatchDTO lastMatchDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update LastMatch partially : {}, {}", id, lastMatchDTO);
        if (lastMatchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lastMatchDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lastMatchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LastMatchDTO> result = lastMatchService.partialUpdate(lastMatchDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lastMatchDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /last-matches} : get all the lastMatches.
     */
    @GetMapping("")
    public ResponseEntity<List<LastMatchDTO>> getAllLastMatches(
        LastMatchCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get LastMatches by criteria: {}", criteria);

        Page<LastMatchDTO> page = lastMatchQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /last-matches/count} : count all the lastMatches.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countLastMatches(LastMatchCriteria criteria) {
        LOG.debug("REST request to count LastMatches by criteria: {}", criteria);
        return ResponseEntity.ok().body(lastMatchQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /last-matches/:id} : get the "id" lastMatch.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LastMatchDTO> getLastMatch(@PathVariable("id") Long id) {
        LOG.debug("REST request to get LastMatch : {}", id);
        Optional<LastMatchDTO> lastMatchDTO = lastMatchService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lastMatchDTO);
    }

    /**
     * {@code DELETE  /last-matches/:id} : delete the "id" lastMatch.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLastMatch(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete LastMatch : {}", id);
        lastMatchService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
