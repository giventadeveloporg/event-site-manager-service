package com.eventsitemanager.web.rest;

import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.ProfileWritingRepository;
import com.eventsitemanager.service.ProfileWritingQueryService;
import com.eventsitemanager.service.ProfileWritingService;
import com.eventsitemanager.service.criteria.ProfileWritingCriteria;
import com.eventsitemanager.service.dto.ProfileWritingDTO;
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
@RequestMapping("/api/profile-writings")
public class ProfileWritingResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileWritingResource.class);
    private static final String ENTITY_NAME = "profileWriting";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfileWritingService profileWritingService;
    private final ProfileWritingRepository profileWritingRepository;
    private final ProfileWritingQueryService profileWritingQueryService;

    public ProfileWritingResource(
        ProfileWritingService profileWritingService,
        ProfileWritingRepository profileWritingRepository,
        ProfileWritingQueryService profileWritingQueryService
    ) {
        this.profileWritingService = profileWritingService;
        this.profileWritingRepository = profileWritingRepository;
        this.profileWritingQueryService = profileWritingQueryService;
    }

    @PostMapping("")
    public ResponseEntity<ProfileWritingDTO> createProfileWriting(@Valid @RequestBody ProfileWritingDTO profileWritingDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ProfileWriting : {}", profileWritingDTO);
        if (profileWritingDTO.getId() != null) {
            throw new BadRequestAlertException("A new profileWriting cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (profileWritingDTO.getTenantId() == null || profileWritingDTO.getTenantId().isBlank()) {
            throw new BadRequestAlertException("tenantId is required when creating profileWriting", ENTITY_NAME, "tenantidrequired");
        }
        ProfileWritingDTO result = profileWritingService.save(profileWritingDTO);
        return ResponseEntity
            .created(new URI("/api/profile-writings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileWritingDTO> updateProfileWriting(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProfileWritingDTO profileWritingDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProfileWriting : {}, {}", id, profileWritingDTO);
        if (profileWritingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, profileWritingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!profileWritingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        ProfileWritingDTO result = profileWritingService.update(profileWritingDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, profileWritingDTO.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProfileWritingDTO> partialUpdateProfileWriting(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProfileWritingDTO profileWritingDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProfileWriting partially : {}, {}", id, profileWritingDTO);
        if (profileWritingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, profileWritingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!profileWritingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProfileWritingDTO> result = profileWritingService.partialUpdate(profileWritingDTO);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, profileWritingDTO.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<ProfileWritingDTO>> getAllProfileWritings(
        ProfileWritingCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ProfileWritings by criteria: {}", criteria);
        Page<ProfileWritingDTO> page = profileWritingQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countProfileWritings(ProfileWritingCriteria criteria) {
        LOG.debug("REST request to count ProfileWritings by criteria: {}", criteria);
        return ResponseEntity.ok().body(profileWritingQueryService.countByCriteria(criteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileWritingDTO> getProfileWriting(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProfileWriting : {}", id);
        Optional<ProfileWritingDTO> profileWritingDTO = profileWritingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(profileWritingDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfileWriting(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProfileWriting : {}", id);
        profileWritingService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
