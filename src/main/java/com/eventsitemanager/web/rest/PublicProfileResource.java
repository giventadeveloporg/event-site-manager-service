package com.eventsitemanager.web.rest;

import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.PublicProfileRepository;
import com.eventsitemanager.service.PublicProfileQueryService;
import com.eventsitemanager.service.PublicProfileService;
import com.eventsitemanager.service.criteria.PublicProfileCriteria;
import com.eventsitemanager.service.dto.PublicProfileDTO;
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
@RequestMapping("/api/public-profiles")
public class PublicProfileResource {

    private static final Logger LOG = LoggerFactory.getLogger(PublicProfileResource.class);
    private static final String ENTITY_NAME = "publicProfile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PublicProfileService publicProfileService;
    private final PublicProfileRepository publicProfileRepository;
    private final PublicProfileQueryService publicProfileQueryService;

    public PublicProfileResource(
        PublicProfileService publicProfileService,
        PublicProfileRepository publicProfileRepository,
        PublicProfileQueryService publicProfileQueryService
    ) {
        this.publicProfileService = publicProfileService;
        this.publicProfileRepository = publicProfileRepository;
        this.publicProfileQueryService = publicProfileQueryService;
    }

    @PostMapping("")
    public ResponseEntity<PublicProfileDTO> createPublicProfile(@Valid @RequestBody PublicProfileDTO publicProfileDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save PublicProfile : {}", publicProfileDTO);
        if (publicProfileDTO.getId() != null) {
            throw new BadRequestAlertException("A new publicProfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (publicProfileDTO.getTenantId() == null || publicProfileDTO.getTenantId().isBlank()) {
            throw new BadRequestAlertException("tenantId is required when creating a public profile", ENTITY_NAME, "tenantidrequired");
        }
        PublicProfileDTO result = publicProfileService.save(publicProfileDTO);
        return ResponseEntity
            .created(new URI("/api/public-profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PublicProfileDTO> updatePublicProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PublicProfileDTO publicProfileDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PublicProfile : {}, {}", id, publicProfileDTO);
        if (publicProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, publicProfileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!publicProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        PublicProfileDTO result = publicProfileService.update(publicProfileDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, publicProfileDTO.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PublicProfileDTO> partialUpdatePublicProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PublicProfileDTO publicProfileDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PublicProfile partially : {}, {}", id, publicProfileDTO);
        if (publicProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, publicProfileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!publicProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Optional<PublicProfileDTO> result = publicProfileService.partialUpdate(publicProfileDTO);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, publicProfileDTO.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<PublicProfileDTO>> getAllPublicProfiles(
        PublicProfileCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PublicProfiles by criteria: {}", criteria);
        Page<PublicProfileDTO> page = publicProfileQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countPublicProfiles(PublicProfileCriteria criteria) {
        LOG.debug("REST request to count PublicProfiles by criteria: {}", criteria);
        return ResponseEntity.ok().body(publicProfileQueryService.countByCriteria(criteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublicProfileDTO> getPublicProfile(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PublicProfile : {}", id);
        Optional<PublicProfileDTO> publicProfileDTO = publicProfileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(publicProfileDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublicProfile(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PublicProfile : {}", id);
        publicProfileService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
