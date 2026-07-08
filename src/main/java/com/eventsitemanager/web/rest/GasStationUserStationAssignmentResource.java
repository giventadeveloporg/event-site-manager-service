package com.eventsitemanager.web.rest;

import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.GasStationUserStationAssignmentRepository;
import com.eventsitemanager.service.GasStationUserStationAssignmentQueryService;
import com.eventsitemanager.service.GasStationUserStationAssignmentService;
import com.eventsitemanager.service.criteria.GasStationUserStationAssignmentCriteria;
import com.eventsitemanager.service.dto.GasStationUserStationAssignmentDTO;
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
@RequestMapping("/api/gas-station-user-station-assignments")
public class GasStationUserStationAssignmentResource {

    private static final Logger LOG = LoggerFactory.getLogger(GasStationUserStationAssignmentResource.class);
    private static final String ENTITY_NAME = "gasStationUserStationAssignment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GasStationUserStationAssignmentService assignmentService;
    private final GasStationUserStationAssignmentRepository assignmentRepository;
    private final GasStationUserStationAssignmentQueryService assignmentQueryService;

    public GasStationUserStationAssignmentResource(
        GasStationUserStationAssignmentService assignmentService,
        GasStationUserStationAssignmentRepository assignmentRepository,
        GasStationUserStationAssignmentQueryService assignmentQueryService
    ) {
        this.assignmentService = assignmentService;
        this.assignmentRepository = assignmentRepository;
        this.assignmentQueryService = assignmentQueryService;
    }

    @PostMapping("")
    public ResponseEntity<GasStationUserStationAssignmentDTO> createGasStationUserStationAssignment(
        @Valid @RequestBody GasStationUserStationAssignmentDTO dto
    ) throws URISyntaxException {
        LOG.debug("REST request to save GasStationUserStationAssignment : {}", dto);
        if (dto.getId() != null) {
            throw new BadRequestAlertException("A new gasStationUserStationAssignment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (dto.getTenantId() == null || dto.getTenantId().isBlank()) {
            throw new BadRequestAlertException("tenantId is required when creating assignment", ENTITY_NAME, "tenantidrequired");
        }
        GasStationUserStationAssignmentDTO result = assignmentService.save(dto);
        return ResponseEntity
            .created(new URI("/api/gas-station-user-station-assignments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GasStationUserStationAssignmentDTO> updateGasStationUserStationAssignment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GasStationUserStationAssignmentDTO dto
    ) throws URISyntaxException {
        LOG.debug("REST request to update GasStationUserStationAssignment : {}, {}", id, dto);
        if (dto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!assignmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        GasStationUserStationAssignmentDTO result = assignmentService.update(dto);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dto.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GasStationUserStationAssignmentDTO> partialUpdateGasStationUserStationAssignment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GasStationUserStationAssignmentDTO dto
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update GasStationUserStationAssignment partially : {}, {}", id, dto);
        if (dto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!assignmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GasStationUserStationAssignmentDTO> result = assignmentService.partialUpdate(dto);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dto.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<GasStationUserStationAssignmentDTO>> getAllGasStationUserStationAssignments(
        GasStationUserStationAssignmentCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get GasStationUserStationAssignments by criteria: {}", criteria);
        Page<GasStationUserStationAssignmentDTO> page = assignmentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countGasStationUserStationAssignments(GasStationUserStationAssignmentCriteria criteria) {
        LOG.debug("REST request to count GasStationUserStationAssignments by criteria: {}", criteria);
        return ResponseEntity.ok().body(assignmentQueryService.countByCriteria(criteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GasStationUserStationAssignmentDTO> getGasStationUserStationAssignment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get GasStationUserStationAssignment : {}", id);
        Optional<GasStationUserStationAssignmentDTO> dto = assignmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGasStationUserStationAssignment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete GasStationUserStationAssignment : {}", id);
        assignmentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
