package com.nextjstemplate.web.rest;

import com.nextjstemplate.errors.BadRequestAlertException;
import com.nextjstemplate.repository.GalleryAlbumRepository;
import com.nextjstemplate.service.GalleryAlbumQueryService;
import com.nextjstemplate.service.GalleryAlbumService;
import com.nextjstemplate.service.criteria.GalleryAlbumCriteria;
import com.nextjstemplate.service.dto.GalleryAlbumDTO;
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
 * REST controller for managing {@link com.nextjstemplate.domain.GalleryAlbum}.
 */
@RestController
@RequestMapping("/api/gallery-albums")
public class GalleryAlbumResource {

    private final Logger log = LoggerFactory.getLogger(GalleryAlbumResource.class);

    private static final String ENTITY_NAME = "galleryAlbum";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GalleryAlbumService galleryAlbumService;

    private final GalleryAlbumRepository galleryAlbumRepository;

    private final GalleryAlbumQueryService galleryAlbumQueryService;

    public GalleryAlbumResource(
        GalleryAlbumService galleryAlbumService,
        GalleryAlbumRepository galleryAlbumRepository,
        GalleryAlbumQueryService galleryAlbumQueryService
    ) {
        this.galleryAlbumService = galleryAlbumService;
        this.galleryAlbumRepository = galleryAlbumRepository;
        this.galleryAlbumQueryService = galleryAlbumQueryService;
    }

    /**
     * {@code POST  /gallery-albums} : Create a new galleryAlbum.
     *
     * @param galleryAlbumDTO the galleryAlbumDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new galleryAlbumDTO, or with status {@code 400 (Bad Request)} if the galleryAlbum has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<GalleryAlbumDTO> createGalleryAlbum(@Valid @RequestBody GalleryAlbumDTO galleryAlbumDTO)
        throws URISyntaxException {
        log.debug("REST request to save GalleryAlbum : {}", galleryAlbumDTO);
        if (galleryAlbumDTO.getId() != null) {
            throw new BadRequestAlertException("A new galleryAlbum cannot already have an ID", ENTITY_NAME, "idexists");
        }

        String tenantId = getTenantId();
        galleryAlbumDTO.setTenantId(tenantId);

        GalleryAlbumDTO result = galleryAlbumService.save(galleryAlbumDTO);
        return ResponseEntity
            .created(new URI("/api/gallery-albums/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /gallery-albums/:id} : Updates an existing galleryAlbum.
     *
     * @param id the id of the galleryAlbumDTO to save.
     * @param galleryAlbumDTO the galleryAlbumDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated galleryAlbumDTO,
     * or with status {@code 400 (Bad Request)} if the galleryAlbumDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the galleryAlbumDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<GalleryAlbumDTO> updateGalleryAlbum(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GalleryAlbumDTO galleryAlbumDTO
    ) throws URISyntaxException {
        log.debug("REST request to update GalleryAlbum : {}, {}", id, galleryAlbumDTO);
        if (galleryAlbumDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, galleryAlbumDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!galleryAlbumRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        String tenantId = getTenantId();
        galleryAlbumDTO.setTenantId(tenantId);

        GalleryAlbumDTO result = galleryAlbumService.update(galleryAlbumDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, galleryAlbumDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /gallery-albums/:id} : Partial updates given fields of an existing galleryAlbum.
     *
     * @param id the id of the galleryAlbumDTO to save.
     * @param galleryAlbumDTO the galleryAlbumDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated galleryAlbumDTO,
     * or with status {@code 400 (Bad Request)} if the galleryAlbumDTO is not valid,
     * or with status {@code 404 (Not Found)} if the galleryAlbumDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the galleryAlbumDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GalleryAlbumDTO> partialUpdateGalleryAlbum(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GalleryAlbumDTO galleryAlbumDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update GalleryAlbum partially : {}, {}", id, galleryAlbumDTO);
        if (galleryAlbumDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, galleryAlbumDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!galleryAlbumRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GalleryAlbumDTO> result = galleryAlbumService.partialUpdate(galleryAlbumDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, galleryAlbumDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /gallery-albums} : get all the galleryAlbums.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of galleryAlbums in body.
     */
    @GetMapping("")
    public ResponseEntity<List<GalleryAlbumDTO>> getAllGalleryAlbums(
        GalleryAlbumCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get GalleryAlbums by criteria: {}", criteria);

        // Wildcard: when no tenant in context, do not add tenant filter (global search for admin dashboard)
        String tenantId = com.nextjstemplate.security.TenantContext.getCurrentTenant();
        if (criteria == null) {
            criteria = new GalleryAlbumCriteria();
        }
        if (tenantId != null && !tenantId.isEmpty() && criteria.getTenantId() == null) {
            criteria.setTenantId(new tech.jhipster.service.filter.StringFilter());
            criteria.getTenantId().setEquals(tenantId);
        }

        Page<GalleryAlbumDTO> page = galleryAlbumQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /gallery-albums/count} : count all the galleryAlbums.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countGalleryAlbums(GalleryAlbumCriteria criteria) {
        log.debug("REST request to count GalleryAlbums by criteria: {}", criteria);

        // Wildcard: when no tenant in context, do not add tenant filter (global count for admin dashboard)
        String tenantId = com.nextjstemplate.security.TenantContext.getCurrentTenant();
        if (criteria == null) {
            criteria = new GalleryAlbumCriteria();
        }
        if (tenantId != null && !tenantId.isEmpty() && criteria.getTenantId() == null) {
            criteria.setTenantId(new tech.jhipster.service.filter.StringFilter());
            criteria.getTenantId().setEquals(tenantId);
        }

        return ResponseEntity.ok().body(galleryAlbumQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /gallery-albums/:id} : get the "id" galleryAlbum.
     *
     * @param id the id of the galleryAlbumDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the galleryAlbumDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<GalleryAlbumDTO> getGalleryAlbum(@PathVariable Long id) {
        log.debug("REST request to get GalleryAlbum : {}", id);
        Optional<GalleryAlbumDTO> galleryAlbumDTO = galleryAlbumService.findOne(id);
        return ResponseUtil.wrapOrNotFound(galleryAlbumDTO);
    }

    /**
     * {@code DELETE  /gallery-albums/:id} : delete the "id" galleryAlbum.
     *
     * @param id the id of the galleryAlbumDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGalleryAlbum(@PathVariable Long id) {
        log.debug("REST request to delete GalleryAlbum : {}", id);
        galleryAlbumService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * Get tenant ID from context.
     */
    private String getTenantId() {
        try {
            String tenantId = com.nextjstemplate.security.TenantContext.getCurrentTenant();
            if (tenantId == null || tenantId.isEmpty()) {
                throw new BadRequestAlertException("Tenant ID is required", ENTITY_NAME, "tenantIdRequired");
            }
            return tenantId;
        } catch (Exception e) {
            log.warn("Could not get tenant ID from context: {}", e.getMessage());
            throw new BadRequestAlertException("Tenant ID is required", ENTITY_NAME, "tenantIdRequired");
        }
    }
}
