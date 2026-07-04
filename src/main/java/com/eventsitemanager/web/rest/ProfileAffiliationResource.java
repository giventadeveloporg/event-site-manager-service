package com.eventsitemanager.web.rest;

import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.ProfileAffiliationRepository;
import com.eventsitemanager.service.ProfileAffiliationQueryService;
import com.eventsitemanager.service.ProfileAffiliationService;
import com.eventsitemanager.service.criteria.ProfileAffiliationCriteria;
import com.eventsitemanager.service.dto.ProfileAffiliationDTO;
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
@RequestMapping("/api/profile-affiliations")
public class ProfileAffiliationResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileAffiliationResource.class);
    private static final String ENTITY_NAME = "profileAffiliation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfileAffiliationService profileAffiliationService;
    private final ProfileAffiliationRepository profileAffiliationRepository;
    private final ProfileAffiliationQueryService profileAffiliationQueryService;

    public ProfileAffiliationResource(
        ProfileAffiliationService profileAffiliationService,
        ProfileAffiliationRepository profileAffiliationRepository,
        ProfileAffiliationQueryService profileAffiliationQueryService
    ) {
        this.profileAffiliationService = profileAffiliationService;
        this.profileAffiliationRepository = profileAffiliationRepository;
        this.profileAffiliationQueryService = profileAffiliationQueryService;
    }

    @PostMapping("")
    public ResponseEntity<ProfileAffiliationDTO> createProfileAffiliation(@Valid @RequestBody ProfileAffiliationDTO profileAffiliationDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ProfileAffiliation : {}", profileAffiliationDTO);
        if (profileAffiliationDTO.getId() != null) {
            throw new BadRequestAlertException("A new profileAffiliation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (profileAffiliationDTO.getTenantId() == null || profileAffiliationDTO.getTenantId().isBlank()) {
            throw new BadRequestAlertException("tenantId is required when creating profileAffiliation", ENTITY_NAME, "tenantidrequired");
        }
        ProfileAffiliationDTO result = profileAffiliationService.save(profileAffiliationDTO);
        return ResponseEntity
            .created(new URI("/api/profile-affiliations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileAffiliationDTO> updateProfileAffiliation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProfileAffiliationDTO profileAffiliationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProfileAffiliation : {}, {}", id, profileAffiliationDTO);
        if (profileAffiliationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, profileAffiliationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!profileAffiliationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        ProfileAffiliationDTO result = profileAffiliationService.update(profileAffiliationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, profileAffiliationDTO.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProfileAffiliationDTO> partialUpdateProfileAffiliation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProfileAffiliationDTO profileAffiliationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProfileAffiliation partially : {}, {}", id, profileAffiliationDTO);
        if (profileAffiliationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, profileAffiliationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!profileAffiliationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProfileAffiliationDTO> result = profileAffiliationService.partialUpdate(profileAffiliationDTO);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, profileAffiliationDTO.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<ProfileAffiliationDTO>> getAllProfileAffiliations(
        ProfileAffiliationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ProfileAffiliations by criteria: {}", criteria);
        Page<ProfileAffiliationDTO> page = profileAffiliationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countProfileAffiliations(ProfileAffiliationCriteria criteria) {
        LOG.debug("REST request to count ProfileAffiliations by criteria: {}", criteria);
        return ResponseEntity.ok().body(profileAffiliationQueryService.countByCriteria(criteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileAffiliationDTO> getProfileAffiliation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProfileAffiliation : {}", id);
        Optional<ProfileAffiliationDTO> profileAffiliationDTO = profileAffiliationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(profileAffiliationDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfileAffiliation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProfileAffiliation : {}", id);
        profileAffiliationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
