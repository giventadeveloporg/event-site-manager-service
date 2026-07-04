package com.eventsitemanager.web.rest;

import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.ProfileMediaAssetRepository;
import com.eventsitemanager.service.ProfileMediaAssetQueryService;
import com.eventsitemanager.service.ProfileMediaAssetService;
import com.eventsitemanager.service.criteria.ProfileMediaAssetCriteria;
import com.eventsitemanager.service.dto.ProfileMediaAssetDTO;
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
@RequestMapping("/api/profile-media-assets")
public class ProfileMediaAssetResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileMediaAssetResource.class);
    private static final String ENTITY_NAME = "profileMediaAsset";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfileMediaAssetService profileMediaAssetService;
    private final ProfileMediaAssetRepository profileMediaAssetRepository;
    private final ProfileMediaAssetQueryService profileMediaAssetQueryService;

    public ProfileMediaAssetResource(
        ProfileMediaAssetService profileMediaAssetService,
        ProfileMediaAssetRepository profileMediaAssetRepository,
        ProfileMediaAssetQueryService profileMediaAssetQueryService
    ) {
        this.profileMediaAssetService = profileMediaAssetService;
        this.profileMediaAssetRepository = profileMediaAssetRepository;
        this.profileMediaAssetQueryService = profileMediaAssetQueryService;
    }

    @PostMapping("")
    public ResponseEntity<ProfileMediaAssetDTO> createProfileMediaAsset(@Valid @RequestBody ProfileMediaAssetDTO profileMediaAssetDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ProfileMediaAsset : {}", profileMediaAssetDTO);
        if (profileMediaAssetDTO.getId() != null) {
            throw new BadRequestAlertException("A new profileMediaAsset cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (profileMediaAssetDTO.getTenantId() == null || profileMediaAssetDTO.getTenantId().isBlank()) {
            throw new BadRequestAlertException("tenantId is required when creating profileMediaAsset", ENTITY_NAME, "tenantidrequired");
        }
        ProfileMediaAssetDTO result = profileMediaAssetService.save(profileMediaAssetDTO);
        return ResponseEntity
            .created(new URI("/api/profile-media-assets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileMediaAssetDTO> updateProfileMediaAsset(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProfileMediaAssetDTO profileMediaAssetDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProfileMediaAsset : {}, {}", id, profileMediaAssetDTO);
        if (profileMediaAssetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, profileMediaAssetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!profileMediaAssetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        ProfileMediaAssetDTO result = profileMediaAssetService.update(profileMediaAssetDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, profileMediaAssetDTO.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProfileMediaAssetDTO> partialUpdateProfileMediaAsset(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProfileMediaAssetDTO profileMediaAssetDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProfileMediaAsset partially : {}, {}", id, profileMediaAssetDTO);
        if (profileMediaAssetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, profileMediaAssetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!profileMediaAssetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProfileMediaAssetDTO> result = profileMediaAssetService.partialUpdate(profileMediaAssetDTO);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, profileMediaAssetDTO.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<ProfileMediaAssetDTO>> getAllProfileMediaAssets(
        ProfileMediaAssetCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ProfileMediaAssets by criteria: {}", criteria);
        Page<ProfileMediaAssetDTO> page = profileMediaAssetQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countProfileMediaAssets(ProfileMediaAssetCriteria criteria) {
        LOG.debug("REST request to count ProfileMediaAssets by criteria: {}", criteria);
        return ResponseEntity.ok().body(profileMediaAssetQueryService.countByCriteria(criteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileMediaAssetDTO> getProfileMediaAsset(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProfileMediaAsset : {}", id);
        Optional<ProfileMediaAssetDTO> profileMediaAssetDTO = profileMediaAssetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(profileMediaAssetDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfileMediaAsset(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProfileMediaAsset : {}", id);
        profileMediaAssetService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
