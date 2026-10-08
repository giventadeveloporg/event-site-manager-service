package com.eventsitemanager.web.rest;

import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.ProfileServiceRepository;
import com.eventsitemanager.service.ProfileServiceQueryService;
import com.eventsitemanager.service.ProfileServiceService;
import com.eventsitemanager.service.criteria.ProfileServiceCriteria;
import com.eventsitemanager.service.dto.ProfileServiceDTO;
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
@RequestMapping("/api/profile-services")
public class ProfileServiceResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileServiceResource.class);
    private static final String ENTITY_NAME = "profileService";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfileServiceService profileServiceService;
    private final ProfileServiceRepository profileServiceRepository;
    private final ProfileServiceQueryService profileServiceQueryService;

    public ProfileServiceResource(
        ProfileServiceService profileServiceService,
        ProfileServiceRepository profileServiceRepository,
        ProfileServiceQueryService profileServiceQueryService
    ) {
        this.profileServiceService = profileServiceService;
        this.profileServiceRepository = profileServiceRepository;
        this.profileServiceQueryService = profileServiceQueryService;
    }

    @PostMapping("")
    public ResponseEntity<ProfileServiceDTO> createProfileService(@Valid @RequestBody ProfileServiceDTO profileServiceDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ProfileService : {}", profileServiceDTO);
        if (profileServiceDTO.getId() != null) {
            throw new BadRequestAlertException("A new profileService cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (profileServiceDTO.getTenantId() == null || profileServiceDTO.getTenantId().isBlank()) {
            throw new BadRequestAlertException("tenantId is required when creating profileService", ENTITY_NAME, "tenantidrequired");
        }
        ProfileServiceDTO result = profileServiceService.save(profileServiceDTO);
        return ResponseEntity
            .created(new URI("/api/profile-services/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileServiceDTO> updateProfileService(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProfileServiceDTO profileServiceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProfileService : {}, {}", id, profileServiceDTO);
        if (profileServiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, profileServiceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!profileServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        ProfileServiceDTO result = profileServiceService.update(profileServiceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, profileServiceDTO.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProfileServiceDTO> partialUpdateProfileService(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProfileServiceDTO profileServiceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProfileService partially : {}, {}", id, profileServiceDTO);
        if (profileServiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, profileServiceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!profileServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProfileServiceDTO> result = profileServiceService.partialUpdate(profileServiceDTO);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, profileServiceDTO.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<ProfileServiceDTO>> getAllProfileServices(
        ProfileServiceCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ProfileServices by criteria: {}", criteria);
        Page<ProfileServiceDTO> page = profileServiceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countProfileServices(ProfileServiceCriteria criteria) {
        LOG.debug("REST request to count ProfileServices by criteria: {}", criteria);
        return ResponseEntity.ok().body(profileServiceQueryService.countByCriteria(criteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileServiceDTO> getProfileService(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProfileService : {}", id);
        Optional<ProfileServiceDTO> profileServiceDTO = profileServiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(profileServiceDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfileService(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProfileService : {}", id);
        profileServiceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
