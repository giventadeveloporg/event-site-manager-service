package com.nextjstemplate.web.rest;

import com.nextjstemplate.errors.BadRequestAlertException;
import com.nextjstemplate.repository.SatelliteDomainRepository;
import com.nextjstemplate.service.SatelliteDomainQueryService;
import com.nextjstemplate.service.SatelliteDomainService;
import com.nextjstemplate.service.criteria.SatelliteDomainCriteria;
import com.nextjstemplate.service.dto.SatelliteDomainDTO;
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
 * REST controller for managing {@link com.nextjstemplate.domain.SatelliteDomain}.
 */
@RestController
@RequestMapping("/api/satellite-domains")
public class SatelliteDomainResource {

    private static final Logger LOG = LoggerFactory.getLogger(SatelliteDomainResource.class);

    private static final String ENTITY_NAME = "satelliteDomain";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SatelliteDomainService satelliteDomainService;

    private final SatelliteDomainRepository satelliteDomainRepository;

    private final SatelliteDomainQueryService satelliteDomainQueryService;

    public SatelliteDomainResource(
        SatelliteDomainService satelliteDomainService,
        SatelliteDomainRepository satelliteDomainRepository,
        SatelliteDomainQueryService satelliteDomainQueryService
    ) {
        this.satelliteDomainService = satelliteDomainService;
        this.satelliteDomainRepository = satelliteDomainRepository;
        this.satelliteDomainQueryService = satelliteDomainQueryService;
    }

    /**
     * {@code POST  /satellite-domains} : Create a new satelliteDomain.
     *
     * @param satelliteDomainDTO the satelliteDomainDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new satelliteDomainDTO, or with status {@code 400 (Bad Request)} if the satelliteDomain has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SatelliteDomainDTO> createSatelliteDomain(@Valid @RequestBody SatelliteDomainDTO satelliteDomainDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save SatelliteDomain : {}", satelliteDomainDTO);
        if (satelliteDomainDTO.getId() != null) {
            throw new BadRequestAlertException("A new satelliteDomain cannot already have an ID", ENTITY_NAME, "idexists");
        }
        satelliteDomainDTO = satelliteDomainService.save(satelliteDomainDTO);
        return ResponseEntity
            .created(new URI("/api/satellite-domains/" + satelliteDomainDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, satelliteDomainDTO.getId().toString()))
            .body(satelliteDomainDTO);
    }

    /**
     * {@code PUT  /satellite-domains/:id} : Updates an existing satelliteDomain.
     *
     * @param id the id of the satelliteDomainDTO to save.
     * @param satelliteDomainDTO the satelliteDomainDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated satelliteDomainDTO,
     * or with status {@code 400 (Bad Request)} if the satelliteDomainDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the satelliteDomainDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SatelliteDomainDTO> updateSatelliteDomain(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SatelliteDomainDTO satelliteDomainDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SatelliteDomain : {}, {}", id, satelliteDomainDTO);
        if (satelliteDomainDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, satelliteDomainDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!satelliteDomainRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        satelliteDomainDTO = satelliteDomainService.update(satelliteDomainDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, satelliteDomainDTO.getId().toString()))
            .body(satelliteDomainDTO);
    }

    /**
     * {@code PATCH  /satellite-domains/:id} : Partial updates given fields of an existing satelliteDomain, field will ignore if it is null
     *
     * @param id the id of the satelliteDomainDTO to save.
     * @param satelliteDomainDTO the satelliteDomainDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated satelliteDomainDTO,
     * or with status {@code 400 (Bad Request)} if the satelliteDomainDTO is not valid,
     * or with status {@code 404 (Not Found)} if the satelliteDomainDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the satelliteDomainDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SatelliteDomainDTO> partialUpdateSatelliteDomain(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SatelliteDomainDTO satelliteDomainDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SatelliteDomain partially : {}, {}", id, satelliteDomainDTO);
        if (satelliteDomainDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, satelliteDomainDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!satelliteDomainRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SatelliteDomainDTO> result = satelliteDomainService.partialUpdate(satelliteDomainDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, satelliteDomainDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /satellite-domains} : get all the satelliteDomains.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of satelliteDomains in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SatelliteDomainDTO>> getAllSatelliteDomains(
        SatelliteDomainCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get SatelliteDomains by criteria: {}", criteria);

        Page<SatelliteDomainDTO> page = satelliteDomainQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /satellite-domains/count} : count all the satelliteDomains.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSatelliteDomains(SatelliteDomainCriteria criteria) {
        LOG.debug("REST request to count SatelliteDomains by criteria: {}", criteria);
        return ResponseEntity.ok().body(satelliteDomainQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /satellite-domains/:id} : get the "id" satelliteDomain.
     *
     * @param id the id of the satelliteDomainDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the satelliteDomainDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SatelliteDomainDTO> getSatelliteDomain(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SatelliteDomain : {}", id);
        Optional<SatelliteDomainDTO> satelliteDomainDTO = satelliteDomainService.findOne(id);
        return ResponseUtil.wrapOrNotFound(satelliteDomainDTO);
    }

    /**
     * {@code DELETE  /satellite-domains/:id} : delete the "id" satelliteDomain.
     *
     * @param id the id of the satelliteDomainDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSatelliteDomain(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SatelliteDomain : {}", id);
        satelliteDomainService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
