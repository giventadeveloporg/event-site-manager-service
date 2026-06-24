package com.eventsitemanager.web.rest;

import com.eventsitemanager.domain.TeamGroup;
import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.TeamGroupRepository;
import com.eventsitemanager.repository.TeamMemberRepository;
import com.eventsitemanager.service.TeamMemberQueryService;
import com.eventsitemanager.service.TeamMemberService;
import com.eventsitemanager.service.criteria.TeamMemberCriteria;
import com.eventsitemanager.service.dto.TeamMemberDTO;
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
 * REST controller for managing {@link com.eventsitemanager.domain.TeamMember}.
 */
@RestController
@RequestMapping("/api/team-members")
public class TeamMemberResource {

    private static final Logger LOG = LoggerFactory.getLogger(TeamMemberResource.class);

    private static final String ENTITY_NAME = "teamMember";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TeamMemberService teamMemberService;

    private final TeamMemberRepository teamMemberRepository;

    private final TeamMemberQueryService teamMemberQueryService;

    private final TeamGroupRepository teamGroupRepository;

    public TeamMemberResource(
        TeamMemberService teamMemberService,
        TeamMemberRepository teamMemberRepository,
        TeamMemberQueryService teamMemberQueryService,
        TeamGroupRepository teamGroupRepository
    ) {
        this.teamMemberService = teamMemberService;
        this.teamMemberRepository = teamMemberRepository;
        this.teamMemberQueryService = teamMemberQueryService;
        this.teamGroupRepository = teamGroupRepository;
    }

    private void validateTeamGroupForCreate(TeamMemberDTO teamMemberDTO) {
        if (teamMemberDTO.getTeamGroupId() == null) {
            throw new BadRequestAlertException("teamGroupId is required when creating a team member", ENTITY_NAME, "teamgroupidrequired");
        }
        TeamGroup teamGroup = teamGroupRepository
            .findById(teamMemberDTO.getTeamGroupId())
            .orElseThrow(() -> new BadRequestAlertException("Team group not found", ENTITY_NAME, "teamgroupnotfound"));
        if (
            teamMemberDTO.getTenantId() != null &&
            !teamMemberDTO.getTenantId().isBlank() &&
            !Objects.equals(teamGroup.getTenantId(), teamMemberDTO.getTenantId())
        ) {
            throw new BadRequestAlertException(
                "teamGroupId does not belong to the specified tenant",
                ENTITY_NAME,
                "teamgrouptenantmismatch"
            );
        }
    }

    /**
     * {@code POST  /team-members} : Create a new teamMember.
     */
    @PostMapping("")
    public ResponseEntity<TeamMemberDTO> createTeamMember(@Valid @RequestBody TeamMemberDTO teamMemberDTO) throws URISyntaxException {
        LOG.debug("REST request to save TeamMember : {}", teamMemberDTO);
        if (teamMemberDTO.getId() != null) {
            throw new BadRequestAlertException("A new teamMember cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (teamMemberDTO.getTenantId() == null || teamMemberDTO.getTenantId().isBlank()) {
            throw new BadRequestAlertException("tenantId is required when creating a team member", ENTITY_NAME, "tenantidrequired");
        }
        validateTeamGroupForCreate(teamMemberDTO);
        teamMemberDTO = teamMemberService.save(teamMemberDTO);
        return ResponseEntity
            .created(new URI("/api/team-members/" + teamMemberDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, teamMemberDTO.getId().toString()))
            .body(teamMemberDTO);
    }

    /**
     * {@code PUT  /team-members/:id} : Updates an existing teamMember.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TeamMemberDTO> updateTeamMember(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TeamMemberDTO teamMemberDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TeamMember : {}, {}", id, teamMemberDTO);
        if (teamMemberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, teamMemberDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!teamMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        if (teamMemberDTO.getTeamGroupId() != null) {
            validateTeamGroupForCreate(teamMemberDTO);
        }

        teamMemberDTO = teamMemberService.update(teamMemberDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, teamMemberDTO.getId().toString()))
            .body(teamMemberDTO);
    }

    /**
     * {@code PATCH  /team-members/:id} : Partial updates given fields of an existing teamMember.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TeamMemberDTO> partialUpdateTeamMember(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TeamMemberDTO teamMemberDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TeamMember partially : {}, {}", id, teamMemberDTO);
        if (teamMemberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, teamMemberDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!teamMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        if (teamMemberDTO.getTeamGroupId() != null) {
            validateTeamGroupForCreate(teamMemberDTO);
        }

        Optional<TeamMemberDTO> result = teamMemberService.partialUpdate(teamMemberDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, teamMemberDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /team-members} : get all the teamMembers.
     */
    @GetMapping("")
    public ResponseEntity<List<TeamMemberDTO>> getAllTeamMembers(
        TeamMemberCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get TeamMembers by criteria: {}", criteria);

        Page<TeamMemberDTO> page = teamMemberQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /team-members/count} : count all the teamMembers.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTeamMembers(TeamMemberCriteria criteria) {
        LOG.debug("REST request to count TeamMembers by criteria: {}", criteria);
        return ResponseEntity.ok().body(teamMemberQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /team-members/:id} : get the "id" teamMember.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TeamMemberDTO> getTeamMember(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TeamMember : {}", id);
        Optional<TeamMemberDTO> teamMemberDTO = teamMemberService.findOne(id);
        return ResponseUtil.wrapOrNotFound(teamMemberDTO);
    }

    /**
     * {@code DELETE  /team-members/:id} : delete the "id" teamMember.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeamMember(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TeamMember : {}", id);
        teamMemberService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
