package com.nextjstemplate.web.rest;

import com.nextjstemplate.errors.BadRequestAlertException;
import com.nextjstemplate.repository.QrCodeUsageRepository;
import com.nextjstemplate.service.QrCodeUsageQueryService;
import com.nextjstemplate.service.QrCodeUsageService;
import com.nextjstemplate.service.criteria.QrCodeUsageCriteria;
import com.nextjstemplate.service.dto.QrCodeUsageDTO;
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
 * REST controller for managing {@link com.nextjstemplate.domain.QrCodeUsage}.
 */
@RestController
@RequestMapping("/api/qr-code-usages")
public class QrCodeUsageResource {

    private final Logger log = LoggerFactory.getLogger(QrCodeUsageResource.class);

    private static final String ENTITY_NAME = "qrCodeUsage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QrCodeUsageService qrCodeUsageService;

    private final QrCodeUsageRepository qrCodeUsageRepository;

    private final QrCodeUsageQueryService qrCodeUsageQueryService;

    public QrCodeUsageResource(
        QrCodeUsageService qrCodeUsageService,
        QrCodeUsageRepository qrCodeUsageRepository,
        QrCodeUsageQueryService qrCodeUsageQueryService
    ) {
        this.qrCodeUsageService = qrCodeUsageService;
        this.qrCodeUsageRepository = qrCodeUsageRepository;
        this.qrCodeUsageQueryService = qrCodeUsageQueryService;
    }

    /**
     * {@code POST  /qr-code-usages} : Create a new qrCodeUsage.
     *
     * @param qrCodeUsageDTO the qrCodeUsageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new qrCodeUsageDTO, or with status {@code 400 (Bad Request)} if the qrCodeUsage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<QrCodeUsageDTO> createQrCodeUsage(@Valid @RequestBody QrCodeUsageDTO qrCodeUsageDTO) throws URISyntaxException {
        log.debug("REST request to save QrCodeUsage : {}", qrCodeUsageDTO);
        if (qrCodeUsageDTO.getId() != null) {
            throw new BadRequestAlertException("A new qrCodeUsage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QrCodeUsageDTO result = qrCodeUsageService.save(qrCodeUsageDTO);
        return ResponseEntity
            .created(new URI("/api/qr-code-usages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /qr-code-usages/:id} : Updates an existing qrCodeUsage.
     *
     * @param id the id of the qrCodeUsageDTO to save.
     * @param qrCodeUsageDTO the qrCodeUsageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated qrCodeUsageDTO,
     * or with status {@code 400 (Bad Request)} if the qrCodeUsageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the qrCodeUsageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<QrCodeUsageDTO> updateQrCodeUsage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody QrCodeUsageDTO qrCodeUsageDTO
    ) throws URISyntaxException {
        log.debug("REST request to update QrCodeUsage : {}, {}", id, qrCodeUsageDTO);
        if (qrCodeUsageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, qrCodeUsageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!qrCodeUsageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        QrCodeUsageDTO result = qrCodeUsageService.update(qrCodeUsageDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, qrCodeUsageDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /qr-code-usages/:id} : Partial updates given fields of an existing qrCodeUsage, field will ignore if it is null
     *
     * @param id the id of the qrCodeUsageDTO to save.
     * @param qrCodeUsageDTO the qrCodeUsageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated qrCodeUsageDTO,
     * or with status {@code 400 (Bad Request)} if the qrCodeUsageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the qrCodeUsageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the qrCodeUsageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QrCodeUsageDTO> partialUpdateQrCodeUsage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody QrCodeUsageDTO qrCodeUsageDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update QrCodeUsage partially : {}, {}", id, qrCodeUsageDTO);
        if (qrCodeUsageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, qrCodeUsageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!qrCodeUsageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QrCodeUsageDTO> result = qrCodeUsageService.partialUpdate(qrCodeUsageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, qrCodeUsageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /qr-code-usages} : get all the qrCodeUsages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of qrCodeUsages in body.
     */
    @GetMapping("")
    public ResponseEntity<List<QrCodeUsageDTO>> getAllQrCodeUsages(
        QrCodeUsageCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get QrCodeUsages by criteria: {}", criteria);

        Page<QrCodeUsageDTO> page = qrCodeUsageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /qr-code-usages/count} : count all the qrCodeUsages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countQrCodeUsages(QrCodeUsageCriteria criteria) {
        log.debug("REST request to count QrCodeUsages by criteria: {}", criteria);
        return ResponseEntity.ok().body(qrCodeUsageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /qr-code-usages/:id} : get the "id" qrCodeUsage.
     *
     * @param id the id of the qrCodeUsageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the qrCodeUsageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<QrCodeUsageDTO> getQrCodeUsage(@PathVariable Long id) {
        log.debug("REST request to get QrCodeUsage : {}", id);
        Optional<QrCodeUsageDTO> qrCodeUsageDTO = qrCodeUsageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(qrCodeUsageDTO);
    }

    /**
     * {@code DELETE  /qr-code-usages/:id} : delete the "id" qrCodeUsage.
     *
     * @param id the id of the qrCodeUsageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQrCodeUsage(@PathVariable Long id) {
        log.debug("REST request to delete QrCodeUsage : {}", id);
        qrCodeUsageService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
