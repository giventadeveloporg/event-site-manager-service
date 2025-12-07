package com.nextjstemplate.web.rest;

import com.nextjstemplate.repository.TenantSettingsRepository;
import com.nextjstemplate.service.TenantSettingsQueryService;
import com.nextjstemplate.service.TenantSettingsService;
import com.nextjstemplate.service.criteria.TenantSettingsCriteria;
import com.nextjstemplate.service.dto.TenantSettingsDTO;
import com.nextjstemplate.web.rest.errors.BadRequestAlertException;
import jakarta.persistence.EntityNotFoundException;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.nextjstemplate.domain.TenantSettings}.
 */
@RestController
@RequestMapping("/api/tenant-settings")
public class TenantSettingsResource {

    private static final Logger LOG = LoggerFactory.getLogger(TenantSettingsResource.class);

    private static final String ENTITY_NAME = "tenantSettings";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TenantSettingsService tenantSettingsService;

    private final TenantSettingsRepository tenantSettingsRepository;

    private final TenantSettingsQueryService tenantSettingsQueryService;

    public TenantSettingsResource(
        TenantSettingsService tenantSettingsService,
        TenantSettingsRepository tenantSettingsRepository,
        TenantSettingsQueryService tenantSettingsQueryService
    ) {
        this.tenantSettingsService = tenantSettingsService;
        this.tenantSettingsRepository = tenantSettingsRepository;
        this.tenantSettingsQueryService = tenantSettingsQueryService;
    }

    /**
     * {@code POST  /tenant-settings} : Create a new tenantSettings.
     *
     * @param tenantSettingsDTO the tenantSettingsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tenantSettingsDTO, or with status {@code 400 (Bad Request)} if the tenantSettings has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TenantSettingsDTO> createTenantSettings(@Valid @RequestBody TenantSettingsDTO tenantSettingsDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save TenantSettings : {}", tenantSettingsDTO);
        if (tenantSettingsDTO.getId() != null) {
            throw new BadRequestAlertException("A new tenantSettings cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tenantSettingsDTO = tenantSettingsService.save(tenantSettingsDTO);
        return ResponseEntity
            .created(new URI("/api/tenant-settings/" + tenantSettingsDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, tenantSettingsDTO.getId().toString()))
            .body(tenantSettingsDTO);
    }

    /**
     * {@code PUT  /tenant-settings/:id} : Updates an existing tenantSettings.
     *
     * @param id the id of the tenantSettingsDTO to save.
     * @param tenantSettingsDTO the tenantSettingsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tenantSettingsDTO,
     * or with status {@code 400 (Bad Request)} if the tenantSettingsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tenantSettingsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TenantSettingsDTO> updateTenantSettings(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TenantSettingsDTO tenantSettingsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TenantSettings : {}, {}", id, tenantSettingsDTO);
        if (tenantSettingsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tenantSettingsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tenantSettingsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        tenantSettingsDTO = tenantSettingsService.update(tenantSettingsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tenantSettingsDTO.getId().toString()))
            .body(tenantSettingsDTO);
    }

    /**
     * {@code PATCH  /tenant-settings/:id} : Partial updates given fields of an existing tenantSettings, field will ignore if it is null
     *
     * @param id the id of the tenantSettingsDTO to save.
     * @param tenantSettingsDTO the tenantSettingsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tenantSettingsDTO,
     * or with status {@code 400 (Bad Request)} if the tenantSettingsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tenantSettingsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tenantSettingsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TenantSettingsDTO> partialUpdateTenantSettings(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TenantSettingsDTO tenantSettingsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TenantSettings partially : {}, {}", id, tenantSettingsDTO);
        if (tenantSettingsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tenantSettingsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tenantSettingsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TenantSettingsDTO> result = tenantSettingsService.partialUpdate(tenantSettingsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tenantSettingsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tenant-settings} : get all the tenantSettings.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tenantSettings in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TenantSettingsDTO>> getAllTenantSettings(
        TenantSettingsCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get TenantSettings by criteria: {}", criteria);

        Page<TenantSettingsDTO> page = tenantSettingsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tenant-settings/count} : count all the tenantSettings.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTenantSettings(TenantSettingsCriteria criteria) {
        LOG.debug("REST request to count TenantSettings by criteria: {}", criteria);
        return ResponseEntity.ok().body(tenantSettingsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tenant-settings/:id} : get the "id" tenantSettings.
     *
     * @param id the id of the tenantSettingsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tenantSettingsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TenantSettingsDTO> getTenantSettings(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TenantSettings : {}", id);
        Optional<TenantSettingsDTO> tenantSettingsDTO = tenantSettingsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tenantSettingsDTO);
    }

    /**
     * {@code DELETE  /tenant-settings/:id} : delete the "id" tenantSettings.
     *
     * @param id the id of the tenantSettingsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTenantSettings(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TenantSettings : {}", id);
        tenantSettingsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code POST  /tenant-settings/upload/email-footer-html} : Upload email footer HTML file.
     *
     * @param file the HTML file to upload.
     * @param tenantId the tenant ID (optional, will be retrieved from context if not provided).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tenantSettingsDTO.
     */
    @PostMapping(value = "/upload/email-footer-html", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TenantSettingsDTO> uploadEmailFooterHtml(
        @RequestParam("file") MultipartFile file,
        @RequestParam(value = "tenantId", required = false) String tenantId,
        Authentication authentication
    ) throws URISyntaxException {
        LOG.debug("REST request to upload email footer HTML");

        // Get tenantId from context if not provided
        String finalTenantId = tenantId;
        if (finalTenantId == null || finalTenantId.isEmpty()) {
            try {
                finalTenantId = com.nextjstemplate.security.TenantContext.getCurrentTenant();
                LOG.debug("Tenant ID from context: {}", finalTenantId);
            } catch (Exception e) {
                LOG.warn("Could not get tenant ID from context: {}", e.getMessage());
            }
        }

        if (finalTenantId == null || finalTenantId.isEmpty()) {
            throw new BadRequestAlertException("Tenant ID is required", ENTITY_NAME, "tenantIdRequired");
        }

        try {
            TenantSettingsDTO result = tenantSettingsService.uploadEmailFooterHtml(finalTenantId, file);
            return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
        } catch (EntityNotFoundException e) {
            LOG.error("Tenant settings not found: {}", e.getMessage());
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "tenantSettingsNotFound");
        } catch (IllegalArgumentException e) {
            LOG.error("Invalid request: {}", e.getMessage());
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "invalidRequest");
        } catch (Exception e) {
            LOG.error("Failed to upload email footer HTML", e);
            throw new BadRequestAlertException("Failed to upload email footer HTML: " + e.getMessage(), ENTITY_NAME, "uploadFailed");
        }
    }

    /**
     * {@code POST  /tenant-settings/upload/tenant-logo} : Upload tenant logo image.
     *
     * @param file the image file to upload.
     * @param tenantId the tenant ID (optional, will be retrieved from context if not provided).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tenantSettingsDTO.
     */
    @PostMapping(value = "/upload/tenant-logo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TenantSettingsDTO> uploadTenantLogo(
        @RequestParam("file") MultipartFile file,
        @RequestParam(value = "tenantId", required = false) String tenantId,
        Authentication authentication
    ) throws URISyntaxException {
        LOG.debug("REST request to upload tenant logo");

        // Get tenantId from context if not provided
        String finalTenantId = tenantId;
        if (finalTenantId == null || finalTenantId.isEmpty()) {
            try {
                finalTenantId = com.nextjstemplate.security.TenantContext.getCurrentTenant();
                LOG.debug("Tenant ID from context: {}", finalTenantId);
            } catch (Exception e) {
                LOG.warn("Could not get tenant ID from context: {}", e.getMessage());
            }
        }

        if (finalTenantId == null || finalTenantId.isEmpty()) {
            throw new BadRequestAlertException("Tenant ID is required", ENTITY_NAME, "tenantIdRequired");
        }

        try {
            TenantSettingsDTO result = tenantSettingsService.uploadTenantLogo(finalTenantId, file);
            return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
        } catch (EntityNotFoundException e) {
            LOG.error("Tenant settings not found: {}", e.getMessage());
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "tenantSettingsNotFound");
        } catch (IllegalArgumentException e) {
            LOG.error("Invalid request: {}", e.getMessage());
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "invalidRequest");
        } catch (Exception e) {
            LOG.error("Failed to upload tenant logo", e);
            throw new BadRequestAlertException("Failed to upload tenant logo: " + e.getMessage(), ENTITY_NAME, "uploadFailed");
        }
    }

    /**
     * {@code POST  /tenant-settings/upload/email-header-image} : Upload email header image.
     *
     * @param file the image file to upload.
     * @param tenantId the tenant ID (optional, will be retrieved from context if not provided).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tenantSettingsDTO.
     */
    @PostMapping(value = "/upload/email-header-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TenantSettingsDTO> uploadEmailHeaderImage(
        @RequestParam("file") MultipartFile file,
        @RequestParam(value = "tenantId", required = false) String tenantId,
        Authentication authentication
    ) throws URISyntaxException {
        LOG.debug("REST request to upload email header image");

        // Get tenantId from context if not provided
        String finalTenantId = tenantId;
        if (finalTenantId == null || finalTenantId.isEmpty()) {
            try {
                finalTenantId = com.nextjstemplate.security.TenantContext.getCurrentTenant();
                LOG.debug("Tenant ID from context: {}", finalTenantId);
            } catch (Exception e) {
                LOG.warn("Could not get tenant ID from context: {}", e.getMessage());
            }
        }

        if (finalTenantId == null || finalTenantId.isEmpty()) {
            throw new BadRequestAlertException("Tenant ID is required", ENTITY_NAME, "tenantIdRequired");
        }

        try {
            TenantSettingsDTO result = tenantSettingsService.uploadEmailHeaderImage(finalTenantId, file);
            return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
        } catch (EntityNotFoundException e) {
            LOG.error("Tenant settings not found: {}", e.getMessage());
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "tenantSettingsNotFound");
        } catch (IllegalArgumentException e) {
            LOG.error("Invalid request: {}", e.getMessage());
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "invalidRequest");
        } catch (Exception e) {
            LOG.error("Failed to upload email header image", e);
            throw new BadRequestAlertException("Failed to upload email header image: " + e.getMessage(), ENTITY_NAME, "uploadFailed");
        }
    }
}
