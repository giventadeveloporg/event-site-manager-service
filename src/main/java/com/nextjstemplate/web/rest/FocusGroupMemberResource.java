package com.nextjstemplate.web.rest;

import com.nextjstemplate.repository.FocusGroupMemberRepository;
import com.nextjstemplate.service.FocusGroupMemberQueryService;
import com.nextjstemplate.service.FocusGroupMemberService;
import com.nextjstemplate.service.criteria.FocusGroupMemberCriteria;
import com.nextjstemplate.service.dto.FocusGroupMemberDTO;
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
 * REST controller for managing
 * {@link com.nextjstemplate.domain.FocusGroupMember}.
 */
@RestController
@RequestMapping("/api/focus-group-members")
public class FocusGroupMemberResource {

    private final Logger log = LoggerFactory.getLogger(FocusGroupMemberResource.class);

    private static final String ENTITY_NAME = "focusGroupMember";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FocusGroupMemberService focusGroupMemberService;

    private final FocusGroupMemberRepository focusGroupMemberRepository;

    private final FocusGroupMemberQueryService focusGroupMemberQueryService;

    public FocusGroupMemberResource(
        FocusGroupMemberService focusGroupMemberService,
        FocusGroupMemberRepository focusGroupMemberRepository,
        FocusGroupMemberQueryService focusGroupMemberQueryService
    ) {
        this.focusGroupMemberService = focusGroupMemberService;
        this.focusGroupMemberRepository = focusGroupMemberRepository;
        this.focusGroupMemberQueryService = focusGroupMemberQueryService;
    }

    /**
     * {@code POST  /focus-group-members} : Create a new focusGroupMember.
     *
     * @param focusGroupMemberDTO the focusGroupMemberDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new focusGroupMemberDTO,
     *         or with status {@code 400 (Bad Request)} if the focusGroupMember has
     *         already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FocusGroupMemberDTO> createFocusGroupMember(@Valid @RequestBody FocusGroupMemberDTO focusGroupMemberDTO)
        throws URISyntaxException {
        log.debug("REST request to save FocusGroupMember : {}", focusGroupMemberDTO);
        if (focusGroupMemberDTO.getId() != null) {
            throw new BadRequestAlertException("A new focusGroupMember cannot already have an ID", ENTITY_NAME, "idexists");
        }

        try {
            FocusGroupMemberDTO result = focusGroupMemberService.save(focusGroupMemberDTO);
            return ResponseEntity
                .created(new URI("/api/focus-group-members/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
        } catch (IllegalArgumentException e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "constraintviolation");
        }
    }

    /**
     * {@code PUT  /focus-group-members/:id} : Updates an existing focusGroupMember.
     *
     * @param id                  the id of the focusGroupMemberDTO to save.
     * @param focusGroupMemberDTO the focusGroupMemberDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated focusGroupMemberDTO,
     *         or with status {@code 400 (Bad Request)} if the focusGroupMemberDTO
     *         is not valid,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         focusGroupMemberDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FocusGroupMemberDTO> updateFocusGroupMember(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FocusGroupMemberDTO focusGroupMemberDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FocusGroupMember : {}, {}", id, focusGroupMemberDTO);
        if (focusGroupMemberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, focusGroupMemberDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!focusGroupMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        try {
            FocusGroupMemberDTO result = focusGroupMemberService.update(focusGroupMemberDTO);
            return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, focusGroupMemberDTO.getId().toString()))
                .body(result);
        } catch (IllegalArgumentException e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "constraintviolation");
        }
    }

    /**
     * {@code PATCH  /focus-group-members/:id} : Partial updates given fields of an
     * existing focusGroupMember, field will ignore if it is null
     *
     * @param id                  the id of the focusGroupMemberDTO to save.
     * @param focusGroupMemberDTO the focusGroupMemberDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated focusGroupMemberDTO,
     *         or with status {@code 400 (Bad Request)} if the focusGroupMemberDTO
     *         is not valid,
     *         or with status {@code 404 (Not Found)} if the focusGroupMemberDTO is
     *         not found,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         focusGroupMemberDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FocusGroupMemberDTO> partialUpdateFocusGroupMember(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FocusGroupMemberDTO focusGroupMemberDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FocusGroupMember partially : {}, {}", id, focusGroupMemberDTO);
        if (focusGroupMemberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, focusGroupMemberDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!focusGroupMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        try {
            Optional<FocusGroupMemberDTO> result = focusGroupMemberService.partialUpdate(focusGroupMemberDTO);
            return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, focusGroupMemberDTO.getId().toString())
            );
        } catch (IllegalArgumentException e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "constraintviolation");
        }
    }

    /**
     * {@code GET  /focus-group-members} : get all the focusGroupMembers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of focusGroupMembers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FocusGroupMemberDTO>> getAllFocusGroupMembers(
        FocusGroupMemberCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get FocusGroupMembers by criteria: {}", criteria);

        Page<FocusGroupMemberDTO> page = focusGroupMemberQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /focus-group-members/count} : count all the focusGroupMembers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
     *         in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countFocusGroupMembers(FocusGroupMemberCriteria criteria) {
        log.debug("REST request to count FocusGroupMembers by criteria: {}", criteria);
        return ResponseEntity.ok().body(focusGroupMemberQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /focus-group-members/:id} : get the "id" focusGroupMember.
     *
     * @param id the id of the focusGroupMemberDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the focusGroupMemberDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FocusGroupMemberDTO> getFocusGroupMember(@PathVariable Long id) {
        log.debug("REST request to get FocusGroupMember : {}", id);
        Optional<FocusGroupMemberDTO> focusGroupMemberDTO = focusGroupMemberService.findOne(id);
        return ResponseUtil.wrapOrNotFound(focusGroupMemberDTO);
    }

    /**
     * {@code DELETE  /focus-group-members/:id} : delete the "id" focusGroupMember.
     *
     * @param id the id of the focusGroupMemberDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFocusGroupMember(@PathVariable Long id) {
        log.debug("REST request to delete FocusGroupMember : {}", id);
        focusGroupMemberService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
