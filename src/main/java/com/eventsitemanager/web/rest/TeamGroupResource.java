package com.eventsitemanager.web.rest;

import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.TeamGroupRepository;
import com.eventsitemanager.service.TeamGroupQueryService;
import com.eventsitemanager.service.TeamGroupService;
import com.eventsitemanager.service.criteria.TeamGroupCriteria;
import com.eventsitemanager.service.dto.TeamGroupDTO;
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
 * REST controller for managing {@link com.eventsitemanager.domain.TeamGroup}.
 */
@RestController
@RequestMapping("/api/team-groups")
public class TeamGroupResource {

    private static final Logger LOG = LoggerFactory.getLogger(TeamGroupResource.class);

    private static final String ENTITY_NAME = "teamGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TeamGroupService teamGroupService;

    private final TeamGroupRepository teamGroupRepository;

    private final TeamGroupQueryService teamGroupQueryService;

    public TeamGroupResource(
        TeamGroupService teamGroupService,
        TeamGroupRepository teamGroupRepository,
        TeamGroupQueryService teamGroupQueryService
    ) {
        this.teamGroupService = teamGroupService;
        this.teamGroupRepository = teamGroupRepository;
        this.teamGroupQueryService = teamGroupQueryService;
    }

    /**
     * {@code POST  /team-groups} : Create a new teamGroup.
     */
    @PostMapping("")
    public ResponseEntity<TeamGroupDTO> createTeamGroup(@Valid @RequestBody TeamGroupDTO teamGroupDTO) throws URISyntaxException {
        LOG.debug("REST request to save TeamGroup : {}", teamGroupDTO);
        if (teamGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new teamGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (teamGroupDTO.getTenantId() == null || teamGroupDTO.getTenantId().isBlank()) {
            throw new BadRequestAlertException("tenantId is required when creating a team group", ENTITY_NAME, "tenantidrequired");
        }
        teamGroupDTO = teamGroupService.save(teamGroupDTO);
        return ResponseEntity
            .created(new URI("/api/team-groups/" + teamGroupDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, teamGroupDTO.getId().toString()))
            .body(teamGroupDTO);
    }

    /**
     * {@code PUT  /team-groups/:id} : Updates an existing teamGroup.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TeamGroupDTO> updateTeamGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TeamGroupDTO teamGroupDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TeamGroup : {}, {}", id, teamGroupDTO);
        if (teamGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, teamGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!teamGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        teamGroupDTO = teamGroupService.update(teamGroupDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, teamGroupDTO.getId().toString()))
            .body(teamGroupDTO);
    }

    /**
     * {@code PATCH  /team-groups/:id} : Partial updates given fields of an existing teamGroup.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TeamGroupDTO> partialUpdateTeamGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TeamGroupDTO teamGroupDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TeamGroup partially : {}, {}", id, teamGroupDTO);
        if (teamGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, teamGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!teamGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TeamGroupDTO> result = teamGroupService.partialUpdate(teamGroupDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, teamGroupDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /team-groups} : get all the teamGroups.
     */
    @GetMapping("")
    public ResponseEntity<List<TeamGroupDTO>> getAllTeamGroups(
        TeamGroupCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get TeamGroups by criteria: {}", criteria);

        Page<TeamGroupDTO> page = teamGroupQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /team-groups/count} : count all the teamGroups.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTeamGroups(TeamGroupCriteria criteria) {
        LOG.debug("REST request to count TeamGroups by criteria: {}", criteria);
        return ResponseEntity.ok().body(teamGroupQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /team-groups/:id} : get the "id" teamGroup.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TeamGroupDTO> getTeamGroup(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TeamGroup : {}", id);
        Optional<TeamGroupDTO> teamGroupDTO = teamGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(teamGroupDTO);
    }

    /**
     * {@code DELETE  /team-groups/:id} : delete the "id" teamGroup.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeamGroup(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TeamGroup : {}", id);
        teamGroupService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
