package com.eventsitemanager.web.rest;

import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.GasStationRecommendationRepository;
import com.eventsitemanager.service.GasStationRecommendationQueryService;
import com.eventsitemanager.service.GasStationRecommendationService;
import com.eventsitemanager.service.criteria.GasStationRecommendationCriteria;
import com.eventsitemanager.service.dto.GasStationRecommendationDTO;
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

@RestController
@RequestMapping("/api/gas-station-recommendations")
public class GasStationRecommendationResource {

    private static final Logger LOG = LoggerFactory.getLogger(GasStationRecommendationResource.class);
    private static final String ENTITY_NAME = "gasStationRecommendation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GasStationRecommendationService gasStationRecommendationService;
    private final GasStationRecommendationRepository gasStationRecommendationRepository;
    private final GasStationRecommendationQueryService gasStationRecommendationQueryService;

    public GasStationRecommendationResource(
        GasStationRecommendationService gasStationRecommendationService,
        GasStationRecommendationRepository gasStationRecommendationRepository,
        GasStationRecommendationQueryService gasStationRecommendationQueryService
    ) {
        this.gasStationRecommendationService = gasStationRecommendationService;
        this.gasStationRecommendationRepository = gasStationRecommendationRepository;
        this.gasStationRecommendationQueryService = gasStationRecommendationQueryService;
    }

    @PostMapping("")
    public ResponseEntity<GasStationRecommendationDTO> createGasStationRecommendation(
        @Valid @RequestBody GasStationRecommendationDTO gasStationRecommendationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save GasStationRecommendation : {}", gasStationRecommendationDTO);
        if (gasStationRecommendationDTO.getId() != null) {
            throw new BadRequestAlertException("A new gasStationRecommendation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (gasStationRecommendationDTO.getTenantId() == null || gasStationRecommendationDTO.getTenantId().isBlank()) {
            throw new BadRequestAlertException(
                "tenantId is required when creating gasStationRecommendation",
                ENTITY_NAME,
                "tenantidrequired"
            );
        }
        GasStationRecommendationDTO result = gasStationRecommendationService.save(gasStationRecommendationDTO);
        return ResponseEntity
            .created(new URI("/api/gas-station-recommendations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GasStationRecommendationDTO> updateGasStationRecommendation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GasStationRecommendationDTO gasStationRecommendationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update GasStationRecommendation : {}, {}", id, gasStationRecommendationDTO);
        if (gasStationRecommendationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gasStationRecommendationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!gasStationRecommendationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        GasStationRecommendationDTO result = gasStationRecommendationService.update(gasStationRecommendationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gasStationRecommendationDTO.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GasStationRecommendationDTO> partialUpdateGasStationRecommendation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GasStationRecommendationDTO gasStationRecommendationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update GasStationRecommendation partially : {}, {}", id, gasStationRecommendationDTO);
        if (gasStationRecommendationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gasStationRecommendationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!gasStationRecommendationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GasStationRecommendationDTO> result = gasStationRecommendationService.partialUpdate(gasStationRecommendationDTO);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gasStationRecommendationDTO.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<GasStationRecommendationDTO>> getAllGasStationRecommendations(
        GasStationRecommendationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get GasStationRecommendations by criteria: {}", criteria);
        Page<GasStationRecommendationDTO> page = gasStationRecommendationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countGasStationRecommendations(GasStationRecommendationCriteria criteria) {
        LOG.debug("REST request to count GasStationRecommendations by criteria: {}", criteria);
        return ResponseEntity.ok().body(gasStationRecommendationQueryService.countByCriteria(criteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GasStationRecommendationDTO> getGasStationRecommendation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get GasStationRecommendation : {}", id);
        Optional<GasStationRecommendationDTO> gasStationRecommendationDTO = gasStationRecommendationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gasStationRecommendationDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGasStationRecommendation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete GasStationRecommendation : {}", id);
        gasStationRecommendationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
