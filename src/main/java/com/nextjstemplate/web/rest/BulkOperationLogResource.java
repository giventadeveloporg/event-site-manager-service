package com.nextjstemplate.web.rest;

import com.nextjstemplate.repository.BulkOperationLogRepository;
import com.nextjstemplate.service.BulkOperationLogQueryService;
import com.nextjstemplate.service.BulkOperationLogService;
import com.nextjstemplate.service.criteria.BulkOperationLogCriteria;
import com.nextjstemplate.service.dto.BulkOperationLogDTO;
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
 * REST controller for managing {@link com.nextjstemplate.domain.BulkOperationLog}.
 */
@RestController
@RequestMapping("/api/bulk-operation-logs")
public class BulkOperationLogResource {

    private static final Logger LOG = LoggerFactory.getLogger(BulkOperationLogResource.class);

    private static final String ENTITY_NAME = "bulkOperationLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BulkOperationLogService bulkOperationLogService;

    private final BulkOperationLogRepository bulkOperationLogRepository;

    private final BulkOperationLogQueryService bulkOperationLogQueryService;

    public BulkOperationLogResource(
        BulkOperationLogService bulkOperationLogService,
        BulkOperationLogRepository bulkOperationLogRepository,
        BulkOperationLogQueryService bulkOperationLogQueryService
    ) {
        this.bulkOperationLogService = bulkOperationLogService;
        this.bulkOperationLogRepository = bulkOperationLogRepository;
        this.bulkOperationLogQueryService = bulkOperationLogQueryService;
    }

    /**
     * {@code POST  /bulk-operation-logs} : Create a new bulkOperationLog.
     *
     * @param bulkOperationLogDTO the bulkOperationLogDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bulkOperationLogDTO, or with status {@code 400 (Bad Request)} if the bulkOperationLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BulkOperationLogDTO> createBulkOperationLog(@Valid @RequestBody BulkOperationLogDTO bulkOperationLogDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save BulkOperationLog : {}", bulkOperationLogDTO);
        if (bulkOperationLogDTO.getId() != null) {
            throw new BadRequestAlertException("A new bulkOperationLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        bulkOperationLogDTO = bulkOperationLogService.save(bulkOperationLogDTO);
        return ResponseEntity
            .created(new URI("/api/bulk-operation-logs/" + bulkOperationLogDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, bulkOperationLogDTO.getId().toString()))
            .body(bulkOperationLogDTO);
    }

    /**
     * {@code PUT  /bulk-operation-logs/:id} : Updates an existing bulkOperationLog.
     *
     * @param id the id of the bulkOperationLogDTO to save.
     * @param bulkOperationLogDTO the bulkOperationLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bulkOperationLogDTO,
     * or with status {@code 400 (Bad Request)} if the bulkOperationLogDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bulkOperationLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BulkOperationLogDTO> updateBulkOperationLog(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BulkOperationLogDTO bulkOperationLogDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update BulkOperationLog : {}, {}", id, bulkOperationLogDTO);
        if (bulkOperationLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bulkOperationLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bulkOperationLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        bulkOperationLogDTO = bulkOperationLogService.update(bulkOperationLogDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bulkOperationLogDTO.getId().toString()))
            .body(bulkOperationLogDTO);
    }

    /**
     * {@code PATCH  /bulk-operation-logs/:id} : Partial updates given fields of an existing bulkOperationLog, field will ignore if it is null
     *
     * @param id the id of the bulkOperationLogDTO to save.
     * @param bulkOperationLogDTO the bulkOperationLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bulkOperationLogDTO,
     * or with status {@code 400 (Bad Request)} if the bulkOperationLogDTO is not valid,
     * or with status {@code 404 (Not Found)} if the bulkOperationLogDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the bulkOperationLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BulkOperationLogDTO> partialUpdateBulkOperationLog(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BulkOperationLogDTO bulkOperationLogDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BulkOperationLog partially : {}, {}", id, bulkOperationLogDTO);
        if (bulkOperationLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bulkOperationLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bulkOperationLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BulkOperationLogDTO> result = bulkOperationLogService.partialUpdate(bulkOperationLogDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bulkOperationLogDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /bulk-operation-logs} : get all the bulkOperationLogs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bulkOperationLogs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BulkOperationLogDTO>> getAllBulkOperationLogs(
        BulkOperationLogCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get BulkOperationLogs by criteria: {}", criteria);

        Page<BulkOperationLogDTO> page = bulkOperationLogQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bulk-operation-logs/count} : count all the bulkOperationLogs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countBulkOperationLogs(BulkOperationLogCriteria criteria) {
        LOG.debug("REST request to count BulkOperationLogs by criteria: {}", criteria);
        return ResponseEntity.ok().body(bulkOperationLogQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /bulk-operation-logs/:id} : get the "id" bulkOperationLog.
     *
     * @param id the id of the bulkOperationLogDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bulkOperationLogDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BulkOperationLogDTO> getBulkOperationLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to get BulkOperationLog : {}", id);
        Optional<BulkOperationLogDTO> bulkOperationLogDTO = bulkOperationLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bulkOperationLogDTO);
    }

    /**
     * {@code DELETE  /bulk-operation-logs/:id} : delete the "id" bulkOperationLog.
     *
     * @param id the id of the bulkOperationLogDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBulkOperationLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete BulkOperationLog : {}", id);
        bulkOperationLogService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
