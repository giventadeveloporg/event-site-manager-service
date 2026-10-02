package com.eventsitemanager.web.rest;

import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.HomepageYoutubeOverrideRepository;
import com.eventsitemanager.service.HomepageYoutubeOverrideQueryService;
import com.eventsitemanager.service.HomepageYoutubeOverrideService;
import com.eventsitemanager.service.criteria.HomepageYoutubeOverrideCriteria;
import com.eventsitemanager.service.dto.HomepageYoutubeOverrideDTO;
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
 * REST controller for managing {@link com.eventsitemanager.domain.HomepageYoutubeOverride}.
 */
@RestController
@RequestMapping("/api/homepage-youtube-overrides")
public class HomepageYoutubeOverrideResource {

    private static final Logger LOG = LoggerFactory.getLogger(HomepageYoutubeOverrideResource.class);

    private static final String ENTITY_NAME = "homepageYoutubeOverride";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HomepageYoutubeOverrideService homepageYoutubeOverrideService;

    private final HomepageYoutubeOverrideRepository homepageYoutubeOverrideRepository;

    private final HomepageYoutubeOverrideQueryService homepageYoutubeOverrideQueryService;

    public HomepageYoutubeOverrideResource(
        HomepageYoutubeOverrideService homepageYoutubeOverrideService,
        HomepageYoutubeOverrideRepository homepageYoutubeOverrideRepository,
        HomepageYoutubeOverrideQueryService homepageYoutubeOverrideQueryService
    ) {
        this.homepageYoutubeOverrideService = homepageYoutubeOverrideService;
        this.homepageYoutubeOverrideRepository = homepageYoutubeOverrideRepository;
        this.homepageYoutubeOverrideQueryService = homepageYoutubeOverrideQueryService;
    }

    @PostMapping("")
    public ResponseEntity<HomepageYoutubeOverrideDTO> createHomepageYoutubeOverride(
        @Valid @RequestBody HomepageYoutubeOverrideDTO homepageYoutubeOverrideDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save HomepageYoutubeOverride : {}", homepageYoutubeOverrideDTO);
        if (homepageYoutubeOverrideDTO.getId() != null) {
            throw new BadRequestAlertException("A new homepageYoutubeOverride cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (homepageYoutubeOverrideDTO.getTenantId() == null || homepageYoutubeOverrideDTO.getTenantId().isBlank()) {
            throw new BadRequestAlertException("tenantId is required", ENTITY_NAME, "tenantidrequired");
        }
        homepageYoutubeOverrideDTO = homepageYoutubeOverrideService.save(homepageYoutubeOverrideDTO);
        return ResponseEntity.created(new URI("/api/homepage-youtube-overrides/" + homepageYoutubeOverrideDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, homepageYoutubeOverrideDTO.getId().toString()))
            .body(homepageYoutubeOverrideDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HomepageYoutubeOverrideDTO> updateHomepageYoutubeOverride(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HomepageYoutubeOverrideDTO homepageYoutubeOverrideDTO
    ) {
        LOG.debug("REST request to update HomepageYoutubeOverride : {}, {}", id, homepageYoutubeOverrideDTO);
        if (homepageYoutubeOverrideDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, homepageYoutubeOverrideDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!homepageYoutubeOverrideRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        homepageYoutubeOverrideDTO = homepageYoutubeOverrideService.update(homepageYoutubeOverrideDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, homepageYoutubeOverrideDTO.getId().toString()))
            .body(homepageYoutubeOverrideDTO);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HomepageYoutubeOverrideDTO> partialUpdateHomepageYoutubeOverride(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HomepageYoutubeOverrideDTO homepageYoutubeOverrideDTO
    ) {
        LOG.debug("REST request to partial update HomepageYoutubeOverride : {}, {}", id, homepageYoutubeOverrideDTO);
        if (homepageYoutubeOverrideDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, homepageYoutubeOverrideDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!homepageYoutubeOverrideRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Optional<HomepageYoutubeOverrideDTO> result = homepageYoutubeOverrideService.partialUpdate(homepageYoutubeOverrideDTO);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, homepageYoutubeOverrideDTO.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<HomepageYoutubeOverrideDTO>> getAllHomepageYoutubeOverrides(
        HomepageYoutubeOverrideCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get HomepageYoutubeOverrides by criteria: {}", criteria);
        Page<HomepageYoutubeOverrideDTO> page = homepageYoutubeOverrideQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countHomepageYoutubeOverrides(HomepageYoutubeOverrideCriteria criteria) {
        LOG.debug("REST request to count HomepageYoutubeOverrides by criteria: {}", criteria);
        return ResponseEntity.ok().body(homepageYoutubeOverrideQueryService.countByCriteria(criteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HomepageYoutubeOverrideDTO> getHomepageYoutubeOverride(@PathVariable("id") Long id) {
        LOG.debug("REST request to get HomepageYoutubeOverride : {}", id);
        Optional<HomepageYoutubeOverrideDTO> homepageYoutubeOverrideDTO = homepageYoutubeOverrideService.findOne(id);
        return ResponseUtil.wrapOrNotFound(homepageYoutubeOverrideDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHomepageYoutubeOverride(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete HomepageYoutubeOverride : {}", id);
        homepageYoutubeOverrideService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
