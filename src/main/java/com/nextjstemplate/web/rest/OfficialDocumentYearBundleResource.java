package com.nextjstemplate.web.rest;

import com.nextjstemplate.errors.BadRequestAlertException;
import com.nextjstemplate.repository.OfficialDocumentYearBundleRepository;
import com.nextjstemplate.security.AuthoritiesConstants;
import com.nextjstemplate.security.TenantContext;
import com.nextjstemplate.service.OfficialDocumentYearBundleQueryService;
import com.nextjstemplate.service.OfficialDocumentYearBundleService;
import com.nextjstemplate.service.criteria.OfficialDocumentYearBundleCriteria;
import com.nextjstemplate.service.dto.OfficialDocumentYearBundleDTO;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST API for {@code official_document_year_bundle} (cover image per category + year).
 */
@RestController
@RequestMapping("/api/official-document-year-bundles")
public class OfficialDocumentYearBundleResource {

    private static final Logger log = LoggerFactory.getLogger(OfficialDocumentYearBundleResource.class);

    private static final String ENTITY_NAME = "officialDocumentYearBundle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OfficialDocumentYearBundleService officialDocumentYearBundleService;

    private final OfficialDocumentYearBundleRepository officialDocumentYearBundleRepository;

    private final OfficialDocumentYearBundleQueryService officialDocumentYearBundleQueryService;

    public OfficialDocumentYearBundleResource(
        OfficialDocumentYearBundleService officialDocumentYearBundleService,
        OfficialDocumentYearBundleRepository officialDocumentYearBundleRepository,
        OfficialDocumentYearBundleQueryService officialDocumentYearBundleQueryService
    ) {
        this.officialDocumentYearBundleService = officialDocumentYearBundleService;
        this.officialDocumentYearBundleRepository = officialDocumentYearBundleRepository;
        this.officialDocumentYearBundleQueryService = officialDocumentYearBundleQueryService;
    }

    /**
     * {@code POST /official-document-year-bundles} : create a bundle.
     */
    @PostMapping("")
    public ResponseEntity<OfficialDocumentYearBundleDTO> createOfficialDocumentYearBundle(
        @Valid @RequestBody OfficialDocumentYearBundleDTO dto,
        Authentication authentication
    ) throws URISyntaxException {
        log.debug("REST request to save OfficialDocumentYearBundle : {}", dto);
        requireAdmin(authentication);
        requireTenantMatch(dto.getTenantId());
        if (dto.getId() != null) {
            throw new BadRequestAlertException("A new officialDocumentYearBundle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OfficialDocumentYearBundleDTO result = officialDocumentYearBundleService.save(dto);
        return ResponseEntity
            .created(new URI("/api/official-document-year-bundles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT /official-document-year-bundles/:id} : full update (including clearing {@code coverEventMediaId}).
     */
    @PutMapping("/{id}")
    public ResponseEntity<OfficialDocumentYearBundleDTO> updateOfficialDocumentYearBundle(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OfficialDocumentYearBundleDTO dto,
        Authentication authentication
    ) throws URISyntaxException {
        log.debug("REST request to update OfficialDocumentYearBundle : {}, {}", id, dto);
        requireAdmin(authentication);
        if (dto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!officialDocumentYearBundleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Optional<OfficialDocumentYearBundleDTO> existingOpt = officialDocumentYearBundleService.findOne(id);
        if (existingOpt.isPresent()) {
            requireTenantMatch(existingOpt.orElseThrow().getTenantId());
        }
        requireTenantMatch(dto.getTenantId());

        OfficialDocumentYearBundleDTO result = officialDocumentYearBundleService.update(dto);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dto.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH /official-document-year-bundles/:id} : partial update (null fields ignored).
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OfficialDocumentYearBundleDTO> partialUpdateOfficialDocumentYearBundle(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OfficialDocumentYearBundleDTO dto,
        Authentication authentication
    ) throws URISyntaxException {
        log.debug("REST request to partial update OfficialDocumentYearBundle : {}, {}", id, dto);
        requireAdmin(authentication);
        if (dto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!officialDocumentYearBundleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Optional<OfficialDocumentYearBundleDTO> existingOpt = officialDocumentYearBundleService.findOne(id);
        if (existingOpt.isPresent()) {
            requireTenantMatch(existingOpt.orElseThrow().getTenantId());
        }

        Optional<OfficialDocumentYearBundleDTO> result = officialDocumentYearBundleService.partialUpdate(dto);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dto.getId().toString())
        );
    }

    /**
     * {@code GET /official-document-year-bundles} : list with criteria (tenant from context when omitted).
     */
    @GetMapping("")
    public ResponseEntity<List<OfficialDocumentYearBundleDTO>> getAllOfficialDocumentYearBundles(
        OfficialDocumentYearBundleCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get OfficialDocumentYearBundles by criteria: {}", criteria);
        if (criteria == null) {
            criteria = new OfficialDocumentYearBundleCriteria();
        }
        if (criteria.getTenantId() == null || criteria.getTenantId().getEquals() == null) {
            String tenantId = TenantContext.getCurrentTenant();
            if (tenantId != null && !tenantId.isEmpty()) {
                criteria.setTenantId(new StringFilter());
                criteria.getTenantId().setEquals(tenantId);
                log.debug("Auto-injected tenantId from TenantContext: {}", tenantId);
            }
        }

        // ID-based single fetch with tenant isolation
        if (criteria.getId() != null && criteria.getId().getEquals() != null) {
            Long id = criteria.getId().getEquals();
            Optional<OfficialDocumentYearBundleDTO> result = officialDocumentYearBundleService.findOne(id);
            if (result.isPresent()) {
                OfficialDocumentYearBundleDTO bundle = result.orElseThrow();
                String tenantId = criteria.getTenantId() != null ? criteria.getTenantId().getEquals() : TenantContext.getCurrentTenant();
                if (tenantId != null && !tenantId.equals(bundle.getTenantId())) {
                    log.warn("Tenant mismatch: Requested tenantId={}, bundle tenantId={}", tenantId, bundle.getTenantId());
                    return ResponseEntity.status(403).build();
                }
                return ResponseEntity.ok().body(List.of(bundle));
            }
            return ResponseEntity.ok().body(List.of());
        }

        Page<OfficialDocumentYearBundleDTO> page = officialDocumentYearBundleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET /official-document-year-bundles/count} : count matching bundles.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOfficialDocumentYearBundles(OfficialDocumentYearBundleCriteria criteria) {
        log.debug("REST request to count OfficialDocumentYearBundles by criteria: {}", criteria);
        if (criteria == null) {
            criteria = new OfficialDocumentYearBundleCriteria();
        }
        if (criteria.getTenantId() == null || criteria.getTenantId().getEquals() == null) {
            String tenantId = TenantContext.getCurrentTenant();
            if (tenantId != null && !tenantId.isEmpty()) {
                criteria.setTenantId(new StringFilter());
                criteria.getTenantId().setEquals(tenantId);
            }
        }
        return ResponseEntity.ok().body(officialDocumentYearBundleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET /official-document-year-bundles/:id} : get one bundle.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OfficialDocumentYearBundleDTO> getOfficialDocumentYearBundle(@PathVariable Long id) {
        log.debug("REST request to get OfficialDocumentYearBundle : {}", id);
        Optional<OfficialDocumentYearBundleDTO> dto = officialDocumentYearBundleService.findOne(id);
        if (dto.isEmpty()) {
            return ResponseUtil.wrapOrNotFound(dto);
        }
        OfficialDocumentYearBundleDTO bundle = dto.orElseThrow();
        String tenantId = TenantContext.getCurrentTenant();
        if (tenantId != null && !tenantId.isEmpty() && !tenantId.equals(bundle.getTenantId())) {
            log.warn("Tenant mismatch: Requested tenantId={}, bundle tenantId={}", tenantId, bundle.getTenantId());
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(bundle);
    }

    /**
     * {@code DELETE /official-document-year-bundles/:id} : delete the bundle.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOfficialDocumentYearBundle(@PathVariable Long id, Authentication authentication) {
        log.debug("REST request to delete OfficialDocumentYearBundle : {}", id);
        requireAdmin(authentication);
        Optional<OfficialDocumentYearBundleDTO> existing = officialDocumentYearBundleService.findOne(id);
        if (existing.isPresent()) {
            requireTenantMatch(existing.orElseThrow().getTenantId());
        }
        officialDocumentYearBundleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    private void requireAdmin(Authentication authentication) {
        if (authentication == null || authentication.getAuthorities() == null) {
            throw new BadRequestAlertException("Authentication required", ENTITY_NAME, "authrequired");
        }
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> AuthoritiesConstants.ADMIN.equals(a.getAuthority()));
        if (!isAdmin) {
            throw new BadRequestAlertException("Admin access required", ENTITY_NAME, "adminrequired");
        }
    }

    private void requireTenantMatch(String tenantId) {
        String currentTenant = TenantContext.getCurrentTenant();
        if (currentTenant == null || currentTenant.isBlank()) {
            throw new BadRequestAlertException("Tenant context is required", ENTITY_NAME, "tenantrequired");
        }
        if (tenantId == null || tenantId.isBlank()) {
            throw new BadRequestAlertException("TenantId is required", ENTITY_NAME, "tenantIdRequired");
        }
        if (!currentTenant.equals(tenantId)) {
            throw new BadRequestAlertException("Tenant mismatch", ENTITY_NAME, "tenantmismatch");
        }
    }
}
