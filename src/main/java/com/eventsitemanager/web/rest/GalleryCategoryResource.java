package com.eventsitemanager.web.rest;

import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.errors.ConflictException;
import com.eventsitemanager.repository.GalleryCategoryRepository;
import com.eventsitemanager.service.GalleryCategoryQueryService;
import com.eventsitemanager.service.GalleryCategoryService;
import com.eventsitemanager.service.criteria.GalleryCategoryCriteria;
import com.eventsitemanager.service.dto.GalleryCategoryDTO;
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
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.eventsitemanager.domain.GalleryCategory}.
 */
@RestController
@RequestMapping("/api/gallery-categories")
public class GalleryCategoryResource {

    private final Logger log = LoggerFactory.getLogger(GalleryCategoryResource.class);

    private static final String ENTITY_NAME = "galleryCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GalleryCategoryService galleryCategoryService;

    private final GalleryCategoryRepository galleryCategoryRepository;

    private final GalleryCategoryQueryService galleryCategoryQueryService;

    public GalleryCategoryResource(
        GalleryCategoryService galleryCategoryService,
        GalleryCategoryRepository galleryCategoryRepository,
        GalleryCategoryQueryService galleryCategoryQueryService
    ) {
        this.galleryCategoryService = galleryCategoryService;
        this.galleryCategoryRepository = galleryCategoryRepository;
        this.galleryCategoryQueryService = galleryCategoryQueryService;
    }

    @PostMapping("")
    public ResponseEntity<GalleryCategoryDTO> createGalleryCategory(@Valid @RequestBody GalleryCategoryDTO galleryCategoryDTO)
        throws URISyntaxException {
        log.debug("REST request to save GalleryCategory : {}", galleryCategoryDTO);
        if (galleryCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new galleryCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }

        String tenantId = getTenantId();
        galleryCategoryDTO.setTenantId(tenantId);

        try {
            GalleryCategoryDTO result = galleryCategoryService.save(galleryCategoryDTO);
            return ResponseEntity
                .created(new URI("/api/gallery-categories/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
        } catch (ConflictException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "validationfailed");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<GalleryCategoryDTO> updateGalleryCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GalleryCategoryDTO galleryCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update GalleryCategory : {}, {}", id, galleryCategoryDTO);
        if (galleryCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, galleryCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!galleryCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        String tenantId = getTenantId();
        galleryCategoryDTO.setTenantId(tenantId);

        try {
            GalleryCategoryDTO result = galleryCategoryService.update(galleryCategoryDTO);
            return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, galleryCategoryDTO.getId().toString()))
                .body(result);
        } catch (ConflictException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "validationfailed");
        }
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GalleryCategoryDTO> partialUpdateGalleryCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GalleryCategoryDTO galleryCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update GalleryCategory partially : {}, {}", id, galleryCategoryDTO);
        if (galleryCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, galleryCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!galleryCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        try {
            Optional<GalleryCategoryDTO> result = galleryCategoryService.partialUpdate(galleryCategoryDTO);
            return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, galleryCategoryDTO.getId().toString())
            );
        } catch (ConflictException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "validationfailed");
        }
    }

    @GetMapping("")
    public ResponseEntity<List<GalleryCategoryDTO>> getAllGalleryCategories(
        GalleryCategoryCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get GalleryCategories by criteria: {}", criteria);

        String tenantId = com.eventsitemanager.security.TenantContext.getCurrentTenant();
        if (criteria == null) {
            criteria = new GalleryCategoryCriteria();
        }
        if (tenantId != null && !tenantId.isEmpty() && criteria.getTenantId() == null) {
            criteria.setTenantId(new StringFilter());
            criteria.getTenantId().setEquals(tenantId);
        }

        Page<GalleryCategoryDTO> page = galleryCategoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countGalleryCategories(GalleryCategoryCriteria criteria) {
        log.debug("REST request to count GalleryCategories by criteria: {}", criteria);

        String tenantId = com.eventsitemanager.security.TenantContext.getCurrentTenant();
        if (criteria == null) {
            criteria = new GalleryCategoryCriteria();
        }
        if (tenantId != null && !tenantId.isEmpty() && criteria.getTenantId() == null) {
            criteria.setTenantId(new StringFilter());
            criteria.getTenantId().setEquals(tenantId);
        }

        return ResponseEntity.ok().body(galleryCategoryQueryService.countByCriteria(criteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GalleryCategoryDTO> getGalleryCategory(@PathVariable Long id) {
        log.debug("REST request to get GalleryCategory : {}", id);
        Optional<GalleryCategoryDTO> galleryCategoryDTO = galleryCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(galleryCategoryDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGalleryCategory(@PathVariable Long id) {
        log.debug("REST request to delete GalleryCategory : {}", id);
        galleryCategoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    private String getTenantId() {
        try {
            String tenantId = com.eventsitemanager.security.TenantContext.getCurrentTenant();
            if (tenantId == null || tenantId.isEmpty()) {
                throw new BadRequestAlertException("Tenant ID is required", ENTITY_NAME, "tenantIdRequired");
            }
            return tenantId;
        } catch (BadRequestAlertException e) {
            throw e;
        } catch (Exception e) {
            log.warn("Could not get tenant ID from context: {}", e.getMessage());
            throw new BadRequestAlertException("Tenant ID is required", ENTITY_NAME, "tenantIdRequired");
        }
    }
}
