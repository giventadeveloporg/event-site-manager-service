package com.eventsitemanager.web.rest;

import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.GalleryYoutubeVideoRepository;
import com.eventsitemanager.service.GalleryYoutubeVideoQueryService;
import com.eventsitemanager.service.GalleryYoutubeVideoService;
import com.eventsitemanager.service.criteria.GalleryYoutubeVideoCriteria;
import com.eventsitemanager.service.dto.GalleryYoutubeVideoDTO;
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
 * REST controller for managing {@link com.eventsitemanager.domain.GalleryYoutubeVideo}.
 */
@RestController
@RequestMapping("/api/gallery-youtube-videos")
public class GalleryYoutubeVideoResource {

    private static final Logger LOG = LoggerFactory.getLogger(GalleryYoutubeVideoResource.class);

    private static final String ENTITY_NAME = "galleryYoutubeVideo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GalleryYoutubeVideoService galleryYoutubeVideoService;

    private final GalleryYoutubeVideoRepository galleryYoutubeVideoRepository;

    private final GalleryYoutubeVideoQueryService galleryYoutubeVideoQueryService;

    public GalleryYoutubeVideoResource(
        GalleryYoutubeVideoService galleryYoutubeVideoService,
        GalleryYoutubeVideoRepository galleryYoutubeVideoRepository,
        GalleryYoutubeVideoQueryService galleryYoutubeVideoQueryService
    ) {
        this.galleryYoutubeVideoService = galleryYoutubeVideoService;
        this.galleryYoutubeVideoRepository = galleryYoutubeVideoRepository;
        this.galleryYoutubeVideoQueryService = galleryYoutubeVideoQueryService;
    }

    @PostMapping("")
    public ResponseEntity<GalleryYoutubeVideoDTO> createGalleryYoutubeVideo(@Valid @RequestBody GalleryYoutubeVideoDTO galleryYoutubeVideoDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save GalleryYoutubeVideo : {}", galleryYoutubeVideoDTO);
        if (galleryYoutubeVideoDTO.getId() != null) {
            throw new BadRequestAlertException("A new galleryYoutubeVideo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (galleryYoutubeVideoDTO.getTenantId() == null || galleryYoutubeVideoDTO.getTenantId().isBlank()) {
            throw new BadRequestAlertException("tenantId is required", ENTITY_NAME, "tenantidrequired");
        }
        if (galleryYoutubeVideoDTO.getYoutubeUrl() == null || galleryYoutubeVideoDTO.getYoutubeUrl().isBlank()) {
            throw new BadRequestAlertException("youtubeUrl is required", ENTITY_NAME, "youtubeurlrequired");
        }
        galleryYoutubeVideoDTO = galleryYoutubeVideoService.save(galleryYoutubeVideoDTO);
        return ResponseEntity.created(new URI("/api/gallery-youtube-videos/" + galleryYoutubeVideoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, galleryYoutubeVideoDTO.getId().toString()))
            .body(galleryYoutubeVideoDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GalleryYoutubeVideoDTO> updateGalleryYoutubeVideo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GalleryYoutubeVideoDTO galleryYoutubeVideoDTO
    ) {
        LOG.debug("REST request to update GalleryYoutubeVideo : {}, {}", id, galleryYoutubeVideoDTO);
        if (galleryYoutubeVideoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, galleryYoutubeVideoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!galleryYoutubeVideoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        galleryYoutubeVideoDTO = galleryYoutubeVideoService.update(galleryYoutubeVideoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, galleryYoutubeVideoDTO.getId().toString()))
            .body(galleryYoutubeVideoDTO);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GalleryYoutubeVideoDTO> partialUpdateGalleryYoutubeVideo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GalleryYoutubeVideoDTO galleryYoutubeVideoDTO
    ) {
        LOG.debug("REST request to partial update GalleryYoutubeVideo : {}, {}", id, galleryYoutubeVideoDTO);
        if (galleryYoutubeVideoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, galleryYoutubeVideoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!galleryYoutubeVideoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Optional<GalleryYoutubeVideoDTO> result = galleryYoutubeVideoService.partialUpdate(galleryYoutubeVideoDTO);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, galleryYoutubeVideoDTO.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<GalleryYoutubeVideoDTO>> getAllGalleryYoutubeVideos(
        GalleryYoutubeVideoCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get GalleryYoutubeVideos by criteria: {}", criteria);
        Page<GalleryYoutubeVideoDTO> page = galleryYoutubeVideoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countGalleryYoutubeVideos(GalleryYoutubeVideoCriteria criteria) {
        LOG.debug("REST request to count GalleryYoutubeVideos by criteria: {}", criteria);
        return ResponseEntity.ok().body(galleryYoutubeVideoQueryService.countByCriteria(criteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GalleryYoutubeVideoDTO> getGalleryYoutubeVideo(@PathVariable("id") Long id) {
        LOG.debug("REST request to get GalleryYoutubeVideo : {}", id);
        Optional<GalleryYoutubeVideoDTO> galleryYoutubeVideoDTO = galleryYoutubeVideoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(galleryYoutubeVideoDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGalleryYoutubeVideo(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete GalleryYoutubeVideo : {}", id);
        galleryYoutubeVideoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
