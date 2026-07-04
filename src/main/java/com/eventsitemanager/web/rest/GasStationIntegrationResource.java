package com.eventsitemanager.web.rest;

import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.GasStationIntegrationRepository;
import com.eventsitemanager.service.GasStationIntegrationQueryService;
import com.eventsitemanager.service.GasStationIntegrationService;
import com.eventsitemanager.service.criteria.GasStationIntegrationCriteria;
import com.eventsitemanager.service.dto.GasStationIntegrationDTO;
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
@RequestMapping("/api/gas-station-integrations")
public class GasStationIntegrationResource {

    private static final Logger LOG = LoggerFactory.getLogger(GasStationIntegrationResource.class);
    private static final String ENTITY_NAME = "gasStationIntegration";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GasStationIntegrationService gasStationIntegrationService;
    private final GasStationIntegrationRepository gasStationIntegrationRepository;
    private final GasStationIntegrationQueryService gasStationIntegrationQueryService;

    public GasStationIntegrationResource(
        GasStationIntegrationService gasStationIntegrationService,
        GasStationIntegrationRepository gasStationIntegrationRepository,
        GasStationIntegrationQueryService gasStationIntegrationQueryService
    ) {
        this.gasStationIntegrationService = gasStationIntegrationService;
        this.gasStationIntegrationRepository = gasStationIntegrationRepository;
        this.gasStationIntegrationQueryService = gasStationIntegrationQueryService;
    }

    @PostMapping("")
    public ResponseEntity<GasStationIntegrationDTO> createGasStationIntegration(
        @Valid @RequestBody GasStationIntegrationDTO gasStationIntegrationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save GasStationIntegration : {}", gasStationIntegrationDTO);
        if (gasStationIntegrationDTO.getId() != null) {
            throw new BadRequestAlertException("A new gasStationIntegration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (gasStationIntegrationDTO.getTenantId() == null || gasStationIntegrationDTO.getTenantId().isBlank()) {
            throw new BadRequestAlertException("tenantId is required when creating gasStationIntegration", ENTITY_NAME, "tenantidrequired");
        }
        GasStationIntegrationDTO result = gasStationIntegrationService.save(gasStationIntegrationDTO);
        return ResponseEntity
            .created(new URI("/api/gas-station-integrations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GasStationIntegrationDTO> updateGasStationIntegration(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GasStationIntegrationDTO gasStationIntegrationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update GasStationIntegration : {}, {}", id, gasStationIntegrationDTO);
        if (gasStationIntegrationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gasStationIntegrationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!gasStationIntegrationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        GasStationIntegrationDTO result = gasStationIntegrationService.update(gasStationIntegrationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gasStationIntegrationDTO.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GasStationIntegrationDTO> partialUpdateGasStationIntegration(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GasStationIntegrationDTO gasStationIntegrationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update GasStationIntegration partially : {}, {}", id, gasStationIntegrationDTO);
        if (gasStationIntegrationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gasStationIntegrationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!gasStationIntegrationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GasStationIntegrationDTO> result = gasStationIntegrationService.partialUpdate(gasStationIntegrationDTO);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gasStationIntegrationDTO.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<GasStationIntegrationDTO>> getAllGasStationIntegrations(
        GasStationIntegrationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get GasStationIntegrations by criteria: {}", criteria);
        Page<GasStationIntegrationDTO> page = gasStationIntegrationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countGasStationIntegrations(GasStationIntegrationCriteria criteria) {
        LOG.debug("REST request to count GasStationIntegrations by criteria: {}", criteria);
        return ResponseEntity.ok().body(gasStationIntegrationQueryService.countByCriteria(criteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GasStationIntegrationDTO> getGasStationIntegration(@PathVariable("id") Long id) {
        LOG.debug("REST request to get GasStationIntegration : {}", id);
        Optional<GasStationIntegrationDTO> gasStationIntegrationDTO = gasStationIntegrationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gasStationIntegrationDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGasStationIntegration(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete GasStationIntegration : {}", id);
        gasStationIntegrationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
