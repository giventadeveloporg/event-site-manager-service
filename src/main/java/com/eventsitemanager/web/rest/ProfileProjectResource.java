package com.eventsitemanager.web.rest;

import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.ProfileProjectRepository;
import com.eventsitemanager.service.ProfileProjectQueryService;
import com.eventsitemanager.service.ProfileProjectService;
import com.eventsitemanager.service.criteria.ProfileProjectCriteria;
import com.eventsitemanager.service.dto.ProfileProjectDTO;
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
@RequestMapping("/api/profile-projects")
public class ProfileProjectResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileProjectResource.class);
    private static final String ENTITY_NAME = "profileProject";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfileProjectService profileProjectService;
    private final ProfileProjectRepository profileProjectRepository;
    private final ProfileProjectQueryService profileProjectQueryService;

    public ProfileProjectResource(
        ProfileProjectService profileProjectService,
        ProfileProjectRepository profileProjectRepository,
        ProfileProjectQueryService profileProjectQueryService
    ) {
        this.profileProjectService = profileProjectService;
        this.profileProjectRepository = profileProjectRepository;
        this.profileProjectQueryService = profileProjectQueryService;
    }

    @PostMapping("")
    public ResponseEntity<ProfileProjectDTO> createProfileProject(@Valid @RequestBody ProfileProjectDTO profileProjectDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ProfileProject : {}", profileProjectDTO);
        if (profileProjectDTO.getId() != null) {
            throw new BadRequestAlertException("A new profileProject cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (profileProjectDTO.getTenantId() == null || profileProjectDTO.getTenantId().isBlank()) {
            throw new BadRequestAlertException("tenantId is required when creating profileProject", ENTITY_NAME, "tenantidrequired");
        }
        ProfileProjectDTO result = profileProjectService.save(profileProjectDTO);
        return ResponseEntity
            .created(new URI("/api/profile-projects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileProjectDTO> updateProfileProject(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProfileProjectDTO profileProjectDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProfileProject : {}, {}", id, profileProjectDTO);
        if (profileProjectDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, profileProjectDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!profileProjectRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        ProfileProjectDTO result = profileProjectService.update(profileProjectDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, profileProjectDTO.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProfileProjectDTO> partialUpdateProfileProject(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProfileProjectDTO profileProjectDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProfileProject partially : {}, {}", id, profileProjectDTO);
        if (profileProjectDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, profileProjectDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!profileProjectRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProfileProjectDTO> result = profileProjectService.partialUpdate(profileProjectDTO);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, profileProjectDTO.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<ProfileProjectDTO>> getAllProfileProjects(
        ProfileProjectCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ProfileProjects by criteria: {}", criteria);
        Page<ProfileProjectDTO> page = profileProjectQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countProfileProjects(ProfileProjectCriteria criteria) {
        LOG.debug("REST request to count ProfileProjects by criteria: {}", criteria);
        return ResponseEntity.ok().body(profileProjectQueryService.countByCriteria(criteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileProjectDTO> getProfileProject(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProfileProject : {}", id);
        Optional<ProfileProjectDTO> profileProjectDTO = profileProjectService.findOne(id);
        return ResponseUtil.wrapOrNotFound(profileProjectDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfileProject(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProfileProject : {}", id);
        profileProjectService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
