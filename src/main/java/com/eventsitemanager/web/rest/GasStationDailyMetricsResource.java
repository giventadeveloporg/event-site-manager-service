package com.eventsitemanager.web.rest;

import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.GasStationDailyMetricsRepository;
import com.eventsitemanager.service.GasStationDailyMetricsQueryService;
import com.eventsitemanager.service.GasStationDailyMetricsService;
import com.eventsitemanager.service.criteria.GasStationDailyMetricsCriteria;
import com.eventsitemanager.service.dto.GasStationDailyMetricsDTO;
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
@RequestMapping("/api/gas-station-daily-metrics")
public class GasStationDailyMetricsResource {

    private static final Logger LOG = LoggerFactory.getLogger(GasStationDailyMetricsResource.class);
    private static final String ENTITY_NAME = "gasStationDailyMetrics";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GasStationDailyMetricsService gasStationDailyMetricsService;
    private final GasStationDailyMetricsRepository gasStationDailyMetricsRepository;
    private final GasStationDailyMetricsQueryService gasStationDailyMetricsQueryService;

    public GasStationDailyMetricsResource(
        GasStationDailyMetricsService gasStationDailyMetricsService,
        GasStationDailyMetricsRepository gasStationDailyMetricsRepository,
        GasStationDailyMetricsQueryService gasStationDailyMetricsQueryService
    ) {
        this.gasStationDailyMetricsService = gasStationDailyMetricsService;
        this.gasStationDailyMetricsRepository = gasStationDailyMetricsRepository;
        this.gasStationDailyMetricsQueryService = gasStationDailyMetricsQueryService;
    }

    @PostMapping("")
    public ResponseEntity<GasStationDailyMetricsDTO> createGasStationDailyMetrics(
        @Valid @RequestBody GasStationDailyMetricsDTO gasStationDailyMetricsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save GasStationDailyMetrics : {}", gasStationDailyMetricsDTO);
        if (gasStationDailyMetricsDTO.getId() != null) {
            throw new BadRequestAlertException("A new gasStationDailyMetrics cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (gasStationDailyMetricsDTO.getTenantId() == null || gasStationDailyMetricsDTO.getTenantId().isBlank()) {
            throw new BadRequestAlertException(
                "tenantId is required when creating gasStationDailyMetrics",
                ENTITY_NAME,
                "tenantidrequired"
            );
        }
        GasStationDailyMetricsDTO result = gasStationDailyMetricsService.save(gasStationDailyMetricsDTO);
        return ResponseEntity
            .created(new URI("/api/gas-station-daily-metrics/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GasStationDailyMetricsDTO> updateGasStationDailyMetrics(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GasStationDailyMetricsDTO gasStationDailyMetricsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update GasStationDailyMetrics : {}, {}", id, gasStationDailyMetricsDTO);
        if (gasStationDailyMetricsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gasStationDailyMetricsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!gasStationDailyMetricsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        GasStationDailyMetricsDTO result = gasStationDailyMetricsService.update(gasStationDailyMetricsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gasStationDailyMetricsDTO.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GasStationDailyMetricsDTO> partialUpdateGasStationDailyMetrics(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GasStationDailyMetricsDTO gasStationDailyMetricsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update GasStationDailyMetrics partially : {}, {}", id, gasStationDailyMetricsDTO);
        if (gasStationDailyMetricsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gasStationDailyMetricsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!gasStationDailyMetricsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GasStationDailyMetricsDTO> result = gasStationDailyMetricsService.partialUpdate(gasStationDailyMetricsDTO);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gasStationDailyMetricsDTO.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<GasStationDailyMetricsDTO>> getAllGasStationDailyMetrics(
        GasStationDailyMetricsCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get GasStationDailyMetrics by criteria: {}", criteria);
        Page<GasStationDailyMetricsDTO> page = gasStationDailyMetricsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countGasStationDailyMetrics(GasStationDailyMetricsCriteria criteria) {
        LOG.debug("REST request to count GasStationDailyMetrics by criteria: {}", criteria);
        return ResponseEntity.ok().body(gasStationDailyMetricsQueryService.countByCriteria(criteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GasStationDailyMetricsDTO> getGasStationDailyMetrics(@PathVariable("id") Long id) {
        LOG.debug("REST request to get GasStationDailyMetrics : {}", id);
        Optional<GasStationDailyMetricsDTO> gasStationDailyMetricsDTO = gasStationDailyMetricsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gasStationDailyMetricsDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGasStationDailyMetrics(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete GasStationDailyMetrics : {}", id);
        gasStationDailyMetricsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
