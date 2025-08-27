package com.nextjstemplate.web.rest;

import com.nextjstemplate.repository.ExecutiveCommitteeTeamMemberRepository;
import com.nextjstemplate.service.ExecutiveCommitteeTeamMemberQueryService;
import com.nextjstemplate.service.ExecutiveCommitteeTeamMemberService;
import com.nextjstemplate.service.criteria.ExecutiveCommitteeTeamMemberCriteria;
import com.nextjstemplate.service.dto.ExecutiveCommitteeTeamMemberDTO;
import com.nextjstemplate.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.nextjstemplate.domain.ExecutiveCommitteeTeamMember}.
 */
@RestController
@RequestMapping("/api/executive-committee-team-members")
public class ExecutiveCommitteeTeamMemberResource {

    private static final Logger LOG = LoggerFactory.getLogger(ExecutiveCommitteeTeamMemberResource.class);

    private static final String ENTITY_NAME = "executiveCommitteeTeamMember";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExecutiveCommitteeTeamMemberService executiveCommitteeTeamMemberService;

    private final ExecutiveCommitteeTeamMemberRepository executiveCommitteeTeamMemberRepository;

    private final ExecutiveCommitteeTeamMemberQueryService executiveCommitteeTeamMemberQueryService;

    public ExecutiveCommitteeTeamMemberResource(
        ExecutiveCommitteeTeamMemberService executiveCommitteeTeamMemberService,
        ExecutiveCommitteeTeamMemberRepository executiveCommitteeTeamMemberRepository,
        ExecutiveCommitteeTeamMemberQueryService executiveCommitteeTeamMemberQueryService
    ) {
        this.executiveCommitteeTeamMemberService = executiveCommitteeTeamMemberService;
        this.executiveCommitteeTeamMemberRepository = executiveCommitteeTeamMemberRepository;
        this.executiveCommitteeTeamMemberQueryService = executiveCommitteeTeamMemberQueryService;
    }

    /**
     * {@code POST  /executive-committee-team-members} : Create a new executiveCommitteeTeamMember.
     *
     * @param executiveCommitteeTeamMemberDTO the executiveCommitteeTeamMemberDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new executiveCommitteeTeamMemberDTO, or with status {@code 400 (Bad Request)} if the executiveCommitteeTeamMember has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ExecutiveCommitteeTeamMemberDTO> createExecutiveCommitteeTeamMember(
        @Valid @RequestBody ExecutiveCommitteeTeamMemberDTO executiveCommitteeTeamMemberDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save ExecutiveCommitteeTeamMember : {}", executiveCommitteeTeamMemberDTO);
        if (executiveCommitteeTeamMemberDTO.getId() != null) {
            throw new BadRequestAlertException("A new executiveCommitteeTeamMember cannot already have an ID", ENTITY_NAME, "idexists");
        }
        executiveCommitteeTeamMemberDTO = executiveCommitteeTeamMemberService.save(executiveCommitteeTeamMemberDTO);
        return ResponseEntity.created(new URI("/api/executive-committee-team-members/" + executiveCommitteeTeamMemberDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, executiveCommitteeTeamMemberDTO.getId().toString())
            )
            .body(executiveCommitteeTeamMemberDTO);
    }

    /**
     * {@code PUT  /executive-committee-team-members/:id} : Updates an existing executiveCommitteeTeamMember.
     *
     * @param id the id of the executiveCommitteeTeamMemberDTO to save.
     * @param executiveCommitteeTeamMemberDTO the executiveCommitteeTeamMemberDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated executiveCommitteeTeamMemberDTO,
     * or with status {@code 400 (Bad Request)} if the executiveCommitteeTeamMemberDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the executiveCommitteeTeamMemberDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ExecutiveCommitteeTeamMemberDTO> updateExecutiveCommitteeTeamMember(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExecutiveCommitteeTeamMemberDTO executiveCommitteeTeamMemberDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ExecutiveCommitteeTeamMember : {}, {}", id, executiveCommitteeTeamMemberDTO);
        if (executiveCommitteeTeamMemberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, executiveCommitteeTeamMemberDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!executiveCommitteeTeamMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        executiveCommitteeTeamMemberDTO = executiveCommitteeTeamMemberService.update(executiveCommitteeTeamMemberDTO);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, executiveCommitteeTeamMemberDTO.getId().toString())
            )
            .body(executiveCommitteeTeamMemberDTO);
    }

    /**
     * {@code PATCH  /executive-committee-team-members/:id} : Partial updates given fields of an existing executiveCommitteeTeamMember, field will ignore if it is null
     *
     * @param id the id of the executiveCommitteeTeamMemberDTO to save.
     * @param executiveCommitteeTeamMemberDTO the executiveCommitteeTeamMemberDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated executiveCommitteeTeamMemberDTO,
     * or with status {@code 400 (Bad Request)} if the executiveCommitteeTeamMemberDTO is not valid,
     * or with status {@code 404 (Not Found)} if the executiveCommitteeTeamMemberDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the executiveCommitteeTeamMemberDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExecutiveCommitteeTeamMemberDTO> partialUpdateExecutiveCommitteeTeamMember(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExecutiveCommitteeTeamMemberDTO executiveCommitteeTeamMemberDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ExecutiveCommitteeTeamMember partially : {}, {}", id, executiveCommitteeTeamMemberDTO);
        if (executiveCommitteeTeamMemberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, executiveCommitteeTeamMemberDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!executiveCommitteeTeamMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExecutiveCommitteeTeamMemberDTO> result = executiveCommitteeTeamMemberService.partialUpdate(
            executiveCommitteeTeamMemberDTO
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, executiveCommitteeTeamMemberDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /executive-committee-team-members} : get all the executiveCommitteeTeamMembers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of executiveCommitteeTeamMembers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ExecutiveCommitteeTeamMemberDTO>> getAllExecutiveCommitteeTeamMembers(
        ExecutiveCommitteeTeamMemberCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ExecutiveCommitteeTeamMembers by criteria: {}", criteria);

        Page<ExecutiveCommitteeTeamMemberDTO> page = executiveCommitteeTeamMemberQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /executive-committee-team-members/count} : count all the executiveCommitteeTeamMembers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countExecutiveCommitteeTeamMembers(ExecutiveCommitteeTeamMemberCriteria criteria) {
        LOG.debug("REST request to count ExecutiveCommitteeTeamMembers by criteria: {}", criteria);
        return ResponseEntity.ok().body(executiveCommitteeTeamMemberQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /executive-committee-team-members/:id} : get the "id" executiveCommitteeTeamMember.
     *
     * @param id the id of the executiveCommitteeTeamMemberDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the executiveCommitteeTeamMemberDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExecutiveCommitteeTeamMemberDTO> getExecutiveCommitteeTeamMember(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ExecutiveCommitteeTeamMember : {}", id);
        Optional<ExecutiveCommitteeTeamMemberDTO> executiveCommitteeTeamMemberDTO = executiveCommitteeTeamMemberService.findOne(id);
        return ResponseUtil.wrapOrNotFound(executiveCommitteeTeamMemberDTO);
    }

    /**
     * {@code DELETE  /executive-committee-team-members/:id} : delete the "id" executiveCommitteeTeamMember.
     *
     * @param id the id of the executiveCommitteeTeamMemberDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExecutiveCommitteeTeamMember(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ExecutiveCommitteeTeamMember : {}", id);
        executiveCommitteeTeamMemberService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
