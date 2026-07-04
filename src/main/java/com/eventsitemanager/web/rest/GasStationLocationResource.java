package com.eventsitemanager.web.rest;

import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.GasStationLocationRepository;
import com.eventsitemanager.service.GasStationLocationQueryService;
import com.eventsitemanager.service.GasStationLocationService;
import com.eventsitemanager.service.criteria.GasStationLocationCriteria;
import com.eventsitemanager.service.dto.GasStationLocationDTO;
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
@RequestMapping("/api/gas-station-locations")
public class GasStationLocationResource {

    private static final Logger LOG = LoggerFactory.getLogger(GasStationLocationResource.class);
    private static final String ENTITY_NAME = "gasStationLocation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GasStationLocationService gasStationLocationService;
    private final GasStationLocationRepository gasStationLocationRepository;
    private final GasStationLocationQueryService gasStationLocationQueryService;

    public GasStationLocationResource(
        GasStationLocationService gasStationLocationService,
        GasStationLocationRepository gasStationLocationRepository,
        GasStationLocationQueryService gasStationLocationQueryService
    ) {
        this.gasStationLocationService = gasStationLocationService;
        this.gasStationLocationRepository = gasStationLocationRepository;
        this.gasStationLocationQueryService = gasStationLocationQueryService;
    }

    @PostMapping("")
    public ResponseEntity<GasStationLocationDTO> createGasStationLocation(@Valid @RequestBody GasStationLocationDTO gasStationLocationDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save GasStationLocation : {}", gasStationLocationDTO);
        if (gasStationLocationDTO.getId() != null) {
            throw new BadRequestAlertException("A new gasStationLocation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (gasStationLocationDTO.getTenantId() == null || gasStationLocationDTO.getTenantId().isBlank()) {
            throw new BadRequestAlertException("tenantId is required when creating gasStationLocation", ENTITY_NAME, "tenantidrequired");
        }
        GasStationLocationDTO result = gasStationLocationService.save(gasStationLocationDTO);
        return ResponseEntity
            .created(new URI("/api/gas-station-locations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GasStationLocationDTO> updateGasStationLocation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GasStationLocationDTO gasStationLocationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update GasStationLocation : {}, {}", id, gasStationLocationDTO);
        if (gasStationLocationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gasStationLocationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!gasStationLocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        GasStationLocationDTO result = gasStationLocationService.update(gasStationLocationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gasStationLocationDTO.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GasStationLocationDTO> partialUpdateGasStationLocation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GasStationLocationDTO gasStationLocationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update GasStationLocation partially : {}, {}", id, gasStationLocationDTO);
        if (gasStationLocationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gasStationLocationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!gasStationLocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GasStationLocationDTO> result = gasStationLocationService.partialUpdate(gasStationLocationDTO);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gasStationLocationDTO.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<GasStationLocationDTO>> getAllGasStationLocations(
        GasStationLocationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get GasStationLocations by criteria: {}", criteria);
        Page<GasStationLocationDTO> page = gasStationLocationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countGasStationLocations(GasStationLocationCriteria criteria) {
        LOG.debug("REST request to count GasStationLocations by criteria: {}", criteria);
        return ResponseEntity.ok().body(gasStationLocationQueryService.countByCriteria(criteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GasStationLocationDTO> getGasStationLocation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get GasStationLocation : {}", id);
        Optional<GasStationLocationDTO> gasStationLocationDTO = gasStationLocationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gasStationLocationDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGasStationLocation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete GasStationLocation : {}", id);
        gasStationLocationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
