package com.nextjstemplate.web.rest;

import com.nextjstemplate.errors.BadRequestAlertException;
import com.nextjstemplate.repository.UserRegistrationRequestRepository;
import com.nextjstemplate.service.UserRegistrationRequestQueryService;
import com.nextjstemplate.service.UserRegistrationRequestService;
import com.nextjstemplate.service.criteria.UserRegistrationRequestCriteria;
import com.nextjstemplate.service.dto.UserRegistrationRequestDTO;
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
 * REST controller for managing {@link com.nextjstemplate.domain.UserRegistrationRequest}.
 */
@RestController
@RequestMapping("/api/user-registration-requests")
public class UserRegistrationRequestResource {

    private final Logger log = LoggerFactory.getLogger(UserRegistrationRequestResource.class);

    private static final String ENTITY_NAME = "userRegistrationRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserRegistrationRequestService userRegistrationRequestService;

    private final UserRegistrationRequestRepository userRegistrationRequestRepository;

    private final UserRegistrationRequestQueryService userRegistrationRequestQueryService;

    public UserRegistrationRequestResource(
        UserRegistrationRequestService userRegistrationRequestService,
        UserRegistrationRequestRepository userRegistrationRequestRepository,
        UserRegistrationRequestQueryService userRegistrationRequestQueryService
    ) {
        this.userRegistrationRequestService = userRegistrationRequestService;
        this.userRegistrationRequestRepository = userRegistrationRequestRepository;
        this.userRegistrationRequestQueryService = userRegistrationRequestQueryService;
    }

    /**
     * {@code POST  /user-registration-requests} : Create a new userRegistrationRequest.
     *
     * @param userRegistrationRequestDTO the userRegistrationRequestDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userRegistrationRequestDTO, or with status {@code 400 (Bad Request)} if the userRegistrationRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserRegistrationRequestDTO> createUserRegistrationRequest(
        @Valid @RequestBody UserRegistrationRequestDTO userRegistrationRequestDTO
    ) throws URISyntaxException {
        log.debug("REST request to save UserRegistrationRequest : {}", userRegistrationRequestDTO);
        if (userRegistrationRequestDTO.getId() != null) {
            throw new BadRequestAlertException("A new userRegistrationRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserRegistrationRequestDTO result = userRegistrationRequestService.save(userRegistrationRequestDTO);
        return ResponseEntity
            .created(new URI("/api/user-registration-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-registration-requests/:id} : Updates an existing userRegistrationRequest.
     *
     * @param id the id of the userRegistrationRequestDTO to save.
     * @param userRegistrationRequestDTO the userRegistrationRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userRegistrationRequestDTO,
     * or with status {@code 400 (Bad Request)} if the userRegistrationRequestDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userRegistrationRequestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserRegistrationRequestDTO> updateUserRegistrationRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserRegistrationRequestDTO userRegistrationRequestDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserRegistrationRequest : {}, {}", id, userRegistrationRequestDTO);
        if (userRegistrationRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userRegistrationRequestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userRegistrationRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserRegistrationRequestDTO result = userRegistrationRequestService.update(userRegistrationRequestDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userRegistrationRequestDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-registration-requests/:id} : Partial updates given fields of an existing userRegistrationRequest, field will ignore if it is null
     *
     * @param id the id of the userRegistrationRequestDTO to save.
     * @param userRegistrationRequestDTO the userRegistrationRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userRegistrationRequestDTO,
     * or with status {@code 400 (Bad Request)} if the userRegistrationRequestDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userRegistrationRequestDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userRegistrationRequestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserRegistrationRequestDTO> partialUpdateUserRegistrationRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserRegistrationRequestDTO userRegistrationRequestDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserRegistrationRequest partially : {}, {}", id, userRegistrationRequestDTO);
        if (userRegistrationRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userRegistrationRequestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userRegistrationRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserRegistrationRequestDTO> result = userRegistrationRequestService.partialUpdate(userRegistrationRequestDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userRegistrationRequestDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-registration-requests} : get all the userRegistrationRequests.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userRegistrationRequests in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UserRegistrationRequestDTO>> getAllUserRegistrationRequests(
        UserRegistrationRequestCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get UserRegistrationRequests by criteria: {}", criteria);

        Page<UserRegistrationRequestDTO> page = userRegistrationRequestQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-registration-requests/count} : count all the userRegistrationRequests.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countUserRegistrationRequests(UserRegistrationRequestCriteria criteria) {
        log.debug("REST request to count UserRegistrationRequests by criteria: {}", criteria);
        return ResponseEntity.ok().body(userRegistrationRequestQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /user-registration-requests/:id} : get the "id" userRegistrationRequest.
     *
     * @param id the id of the userRegistrationRequestDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userRegistrationRequestDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserRegistrationRequestDTO> getUserRegistrationRequest(@PathVariable Long id) {
        log.debug("REST request to get UserRegistrationRequest : {}", id);
        Optional<UserRegistrationRequestDTO> userRegistrationRequestDTO = userRegistrationRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userRegistrationRequestDTO);
    }

    /**
     * {@code DELETE  /user-registration-requests/:id} : delete the "id" userRegistrationRequest.
     *
     * @param id the id of the userRegistrationRequestDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserRegistrationRequest(@PathVariable Long id) {
        log.debug("REST request to delete UserRegistrationRequest : {}", id);
        userRegistrationRequestService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
