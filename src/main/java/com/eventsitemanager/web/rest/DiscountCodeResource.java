package com.eventsitemanager.web.rest;

import com.eventsitemanager.repository.DiscountCodeRepository;
import com.eventsitemanager.service.DiscountCodeQueryService;
import com.eventsitemanager.service.DiscountCodeService;
import com.eventsitemanager.service.criteria.DiscountCodeCriteria;
import com.eventsitemanager.service.dto.DiscountCodeDTO;
import com.eventsitemanager.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.eventsitemanager.domain.DiscountCode}.
 */
@RestController
@RequestMapping("/api/discount-codes")
public class DiscountCodeResource {

    private final Logger log = LoggerFactory.getLogger(DiscountCodeResource.class);

    private static final String ENTITY_NAME = "discountCode";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DiscountCodeService discountCodeService;

    private final DiscountCodeRepository discountCodeRepository;

    private final DiscountCodeQueryService discountCodeQueryService;

    public DiscountCodeResource(
        DiscountCodeService discountCodeService,
        DiscountCodeRepository discountCodeRepository,
        DiscountCodeQueryService discountCodeQueryService
    ) {
        this.discountCodeService = discountCodeService;
        this.discountCodeRepository = discountCodeRepository;
        this.discountCodeQueryService = discountCodeQueryService;
    }

    /**
     * {@code POST  /discount-codes} : Create a new discountCode.
     *
     * @param discountCodeDTO the discountCodeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new discountCodeDTO, or with status {@code 400 (Bad Request)} if the discountCode has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DiscountCodeDTO> createDiscountCode(@Valid @RequestBody DiscountCodeDTO discountCodeDTO)
        throws URISyntaxException {
        log.debug("REST request to save DiscountCode : {}", discountCodeDTO);
        if (discountCodeDTO.getId() != null) {
            throw new BadRequestAlertException("A new discountCode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DiscountCodeDTO result = discountCodeService.save(discountCodeDTO);
        return ResponseEntity
            .created(new URI("/api/discount-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /discount-codes/:id} : Updates an existing discountCode.
     *
     * @param id the id of the discountCodeDTO to save.
     * @param discountCodeDTO the discountCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated discountCodeDTO,
     * or with status {@code 400 (Bad Request)} if the discountCodeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the discountCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DiscountCodeDTO> updateDiscountCode(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DiscountCodeDTO discountCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DiscountCode : {}, {}", id, discountCodeDTO);
        if (discountCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, discountCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!discountCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DiscountCodeDTO result = discountCodeService.update(discountCodeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, discountCodeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /discount-codes/:id} : Partial updates given fields of an existing discountCode, field will ignore if it is null
     *
     * @param id the id of the discountCodeDTO to save.
     * @param discountCodeDTO the discountCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated discountCodeDTO,
     * or with status {@code 400 (Bad Request)} if the discountCodeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the discountCodeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the discountCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DiscountCodeDTO> partialUpdateDiscountCode(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DiscountCodeDTO discountCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DiscountCode partially : {}, {}", id, discountCodeDTO);
        if (discountCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, discountCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!discountCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DiscountCodeDTO> result = discountCodeService.partialUpdate(discountCodeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, discountCodeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /discount-codes} : get all the discountCodes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of discountCodes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DiscountCodeDTO>> getAllDiscountCodes(
        DiscountCodeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get DiscountCodes by criteria: {}", criteria);

        Page<DiscountCodeDTO> page = discountCodeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /discount-codes/count} : count all the discountCodes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDiscountCodes(DiscountCodeCriteria criteria) {
        log.debug("REST request to count DiscountCodes by criteria: {}", criteria);
        return ResponseEntity.ok().body(discountCodeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /discount-codes/:id} : get the "id" discountCode.
     *
     * @param id the id of the discountCodeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the discountCodeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DiscountCodeDTO> getDiscountCode(@PathVariable Long id) {
        log.debug("REST request to get DiscountCode : {}", id);
        Optional<DiscountCodeDTO> discountCodeDTO = discountCodeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(discountCodeDTO);
    }

    /**
     * {@code DELETE  /discount-codes/:id} : delete the "id" discountCode.
     *
     * @param id the id of the discountCodeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiscountCode(@PathVariable Long id) {
        log.debug("REST request to delete DiscountCode : {}", id);
        discountCodeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
