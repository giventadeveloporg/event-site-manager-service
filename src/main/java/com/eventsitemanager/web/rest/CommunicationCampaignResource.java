package com.eventsitemanager.web.rest;

import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.CommunicationCampaignRepository;
import com.eventsitemanager.service.CommunicationCampaignQueryService;
import com.eventsitemanager.service.CommunicationCampaignService;
import com.eventsitemanager.service.criteria.CommunicationCampaignCriteria;
import com.eventsitemanager.service.dto.CommunicationCampaignDTO;
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
 * REST controller for managing {@link com.eventsitemanager.domain.CommunicationCampaign}.
 */
@RestController
@RequestMapping("/api/communication-campaigns")
public class CommunicationCampaignResource {

    private final Logger log = LoggerFactory.getLogger(CommunicationCampaignResource.class);

    private static final String ENTITY_NAME = "communicationCampaign";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommunicationCampaignService communicationCampaignService;

    private final CommunicationCampaignRepository communicationCampaignRepository;

    private final CommunicationCampaignQueryService communicationCampaignQueryService;

    public CommunicationCampaignResource(
        CommunicationCampaignService communicationCampaignService,
        CommunicationCampaignRepository communicationCampaignRepository,
        CommunicationCampaignQueryService communicationCampaignQueryService
    ) {
        this.communicationCampaignService = communicationCampaignService;
        this.communicationCampaignRepository = communicationCampaignRepository;
        this.communicationCampaignQueryService = communicationCampaignQueryService;
    }

    /**
     * {@code POST  /communication-campaigns} : Create a new communicationCampaign.
     *
     * @param communicationCampaignDTO the communicationCampaignDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new communicationCampaignDTO, or with status {@code 400 (Bad Request)} if the communicationCampaign has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CommunicationCampaignDTO> createCommunicationCampaign(
        @Valid @RequestBody CommunicationCampaignDTO communicationCampaignDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CommunicationCampaign : {}", communicationCampaignDTO);
        if (communicationCampaignDTO.getId() != null) {
            throw new BadRequestAlertException("A new communicationCampaign cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CommunicationCampaignDTO result = communicationCampaignService.save(communicationCampaignDTO);
        return ResponseEntity
            .created(new URI("/api/communication-campaigns/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /communication-campaigns/:id} : Updates an existing communicationCampaign.
     *
     * @param id the id of the communicationCampaignDTO to save.
     * @param communicationCampaignDTO the communicationCampaignDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated communicationCampaignDTO,
     * or with status {@code 400 (Bad Request)} if the communicationCampaignDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the communicationCampaignDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CommunicationCampaignDTO> updateCommunicationCampaign(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CommunicationCampaignDTO communicationCampaignDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CommunicationCampaign : {}, {}", id, communicationCampaignDTO);
        if (communicationCampaignDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, communicationCampaignDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!communicationCampaignRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CommunicationCampaignDTO result = communicationCampaignService.update(communicationCampaignDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, communicationCampaignDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /communication-campaigns/:id} : Partial updates given fields of an existing communicationCampaign, field will ignore if it is null
     *
     * @param id the id of the communicationCampaignDTO to save.
     * @param communicationCampaignDTO the communicationCampaignDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated communicationCampaignDTO,
     * or with status {@code 400 (Bad Request)} if the communicationCampaignDTO is not valid,
     * or with status {@code 404 (Not Found)} if the communicationCampaignDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the communicationCampaignDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CommunicationCampaignDTO> partialUpdateCommunicationCampaign(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CommunicationCampaignDTO communicationCampaignDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CommunicationCampaign partially : {}, {}", id, communicationCampaignDTO);
        if (communicationCampaignDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, communicationCampaignDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!communicationCampaignRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CommunicationCampaignDTO> result = communicationCampaignService.partialUpdate(communicationCampaignDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, communicationCampaignDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /communication-campaigns} : get all the communicationCampaigns.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of communicationCampaigns in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CommunicationCampaignDTO>> getAllCommunicationCampaigns(
        CommunicationCampaignCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get CommunicationCampaigns by criteria: {}", criteria);

        Page<CommunicationCampaignDTO> page = communicationCampaignQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /communication-campaigns/count} : count all the communicationCampaigns.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCommunicationCampaigns(CommunicationCampaignCriteria criteria) {
        log.debug("REST request to count CommunicationCampaigns by criteria: {}", criteria);
        return ResponseEntity.ok().body(communicationCampaignQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /communication-campaigns/:id} : get the "id" communicationCampaign.
     *
     * @param id the id of the communicationCampaignDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the communicationCampaignDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CommunicationCampaignDTO> getCommunicationCampaign(@PathVariable Long id) {
        log.debug("REST request to get CommunicationCampaign : {}", id);
        Optional<CommunicationCampaignDTO> communicationCampaignDTO = communicationCampaignService.findOne(id);
        return ResponseUtil.wrapOrNotFound(communicationCampaignDTO);
    }

    /**
     * {@code DELETE  /communication-campaigns/:id} : delete the "id" communicationCampaign.
     *
     * @param id the id of the communicationCampaignDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommunicationCampaign(@PathVariable Long id) {
        log.debug("REST request to delete CommunicationCampaign : {}", id);
        communicationCampaignService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
