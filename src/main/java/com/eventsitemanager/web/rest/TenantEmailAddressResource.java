package com.eventsitemanager.web.rest;

import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.TenantEmailAddressRepository;
import com.eventsitemanager.service.TenantEmailAddressQueryService;
import com.eventsitemanager.service.TenantEmailAddressService;
import com.eventsitemanager.service.criteria.TenantEmailAddressCriteria;
import com.eventsitemanager.service.dto.TenantEmailAddressDTO;
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
 * REST controller for managing {@link com.eventsitemanager.domain.TenantEmailAddress}.
 */
@RestController
@RequestMapping("/api/tenant-email-addresses")
public class TenantEmailAddressResource {

    private final Logger log = LoggerFactory.getLogger(TenantEmailAddressResource.class);

    private static final String ENTITY_NAME = "tenantEmailAddress";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TenantEmailAddressService tenantEmailAddressService;

    private final TenantEmailAddressRepository tenantEmailAddressRepository;

    private final TenantEmailAddressQueryService tenantEmailAddressQueryService;

    public TenantEmailAddressResource(
        TenantEmailAddressService tenantEmailAddressService,
        TenantEmailAddressRepository tenantEmailAddressRepository,
        TenantEmailAddressQueryService tenantEmailAddressQueryService
    ) {
        this.tenantEmailAddressService = tenantEmailAddressService;
        this.tenantEmailAddressRepository = tenantEmailAddressRepository;
        this.tenantEmailAddressQueryService = tenantEmailAddressQueryService;
    }

    /**
     * {@code POST  /tenant-email-addresses} : Create a new tenantEmailAddress.
     *
     * @param tenantEmailAddressDTO the tenantEmailAddressDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tenantEmailAddressDTO,
     *         or with status {@code 400 (Bad Request)} if the tenantEmailAddress has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TenantEmailAddressDTO> createTenantEmailAddress(@Valid @RequestBody TenantEmailAddressDTO tenantEmailAddressDTO)
        throws URISyntaxException {
        log.debug("REST request to save TenantEmailAddress : {}", tenantEmailAddressDTO);
        if (tenantEmailAddressDTO.getId() != null) {
            throw new BadRequestAlertException("A new tenantEmailAddress cannot already have an ID", ENTITY_NAME, "idexists");
        }

        try {
            TenantEmailAddressDTO result = tenantEmailAddressService.save(tenantEmailAddressDTO);
            return ResponseEntity
                .created(new URI("/api/tenant-email-addresses/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
        } catch (IllegalArgumentException e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "validationfailed");
        }
    }

    /**
     * {@code PUT  /tenant-email-addresses/:id} : Updates an existing tenantEmailAddress.
     *
     * @param id                    the id of the tenantEmailAddressDTO to save.
     * @param tenantEmailAddressDTO the tenantEmailAddressDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tenantEmailAddressDTO,
     *         or with status {@code 400 (Bad Request)} if the tenantEmailAddressDTO is not valid,
     *         or with status {@code 500 (Internal Server Error)} if the tenantEmailAddressDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TenantEmailAddressDTO> updateTenantEmailAddress(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TenantEmailAddressDTO tenantEmailAddressDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TenantEmailAddress : {}, {}", id, tenantEmailAddressDTO);
        if (tenantEmailAddressDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tenantEmailAddressDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tenantEmailAddressRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        try {
            TenantEmailAddressDTO result = tenantEmailAddressService.update(tenantEmailAddressDTO);
            return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tenantEmailAddressDTO.getId().toString()))
                .body(result);
        } catch (IllegalArgumentException e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "validationfailed");
        }
    }

    /**
     * {@code PATCH  /tenant-email-addresses/:id} : Partial updates given fields of an existing tenantEmailAddress, field will ignore if it is null
     *
     * @param id                    the id of the tenantEmailAddressDTO to save.
     * @param tenantEmailAddressDTO the tenantEmailAddressDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tenantEmailAddressDTO,
     *         or with status {@code 400 (Bad Request)} if the tenantEmailAddressDTO is not valid,
     *         or with status {@code 404 (Not Found)} if the tenantEmailAddressDTO is not found,
     *         or with status {@code 500 (Internal Server Error)} if the tenantEmailAddressDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TenantEmailAddressDTO> partialUpdateTenantEmailAddress(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TenantEmailAddressDTO tenantEmailAddressDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TenantEmailAddress partially : {}, {}", id, tenantEmailAddressDTO);
        if (tenantEmailAddressDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tenantEmailAddressDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tenantEmailAddressRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        try {
            Optional<TenantEmailAddressDTO> result = tenantEmailAddressService.partialUpdate(tenantEmailAddressDTO);
            return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tenantEmailAddressDTO.getId().toString())
            );
        } catch (IllegalArgumentException e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "validationfailed");
        }
    }

    /**
     * {@code GET  /tenant-email-addresses} : get all the tenantEmailAddresses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tenantEmailAddresses in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TenantEmailAddressDTO>> getAllTenantEmailAddresses(
        TenantEmailAddressCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TenantEmailAddresses by criteria: {}", criteria);

        Page<TenantEmailAddressDTO> page = tenantEmailAddressQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tenant-email-addresses/count} : count all the tenantEmailAddresses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTenantEmailAddresses(TenantEmailAddressCriteria criteria) {
        log.debug("REST request to count TenantEmailAddresses by criteria: {}", criteria);
        return ResponseEntity.ok().body(tenantEmailAddressQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tenant-email-addresses/:id} : get the "id" tenantEmailAddress.
     *
     * @param id the id of the tenantEmailAddressDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tenantEmailAddressDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TenantEmailAddressDTO> getTenantEmailAddress(@PathVariable Long id) {
        log.debug("REST request to get TenantEmailAddress : {}", id);
        Optional<TenantEmailAddressDTO> tenantEmailAddressDTO = tenantEmailAddressService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tenantEmailAddressDTO);
    }

    /**
     * {@code DELETE  /tenant-email-addresses/:id} : delete the "id" tenantEmailAddress.
     *
     * @param id the id of the tenantEmailAddressDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTenantEmailAddress(@PathVariable Long id) {
        log.debug("REST request to delete TenantEmailAddress : {}", id);
        tenantEmailAddressService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
