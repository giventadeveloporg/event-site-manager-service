package com.nextjstemplate.web.rest;

import com.nextjstemplate.service.OfficialDocumentCategoryQueryService;
import com.nextjstemplate.service.criteria.OfficialDocumentCategoryCriteria;
import com.nextjstemplate.service.dto.OfficialDocumentCategoryDTO;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

/**
 * REST API for tenant official document categories (lookup table).
 */
@RestController
@RequestMapping("/api/official-document-categories")
public class OfficialDocumentCategoryResource {

    private static final Logger log = LoggerFactory.getLogger(OfficialDocumentCategoryResource.class);

    private final OfficialDocumentCategoryQueryService officialDocumentCategoryQueryService;

    public OfficialDocumentCategoryResource(OfficialDocumentCategoryQueryService officialDocumentCategoryQueryService) {
        this.officialDocumentCategoryQueryService = officialDocumentCategoryQueryService;
    }

    /**
     * {@code GET /official-document-categories} : list categories with JHipster criteria and pagination.
     * <p>
     * Example:
     * {@code ?tenantId.equals=demo&isActive.equals=true&sort=sortOrder,asc&size=200}
     */
    @GetMapping("")
    public ResponseEntity<List<OfficialDocumentCategoryDTO>> getAllOfficialDocumentCategories(
        OfficialDocumentCategoryCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get OfficialDocumentCategories by criteria: {}", criteria);
        Page<OfficialDocumentCategoryDTO> page = officialDocumentCategoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET /official-document-categories/count} : count matching categories.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOfficialDocumentCategories(OfficialDocumentCategoryCriteria criteria) {
        log.debug("REST request to count OfficialDocumentCategories by criteria: {}", criteria);
        return ResponseEntity.ok().body(officialDocumentCategoryQueryService.countByCriteria(criteria));
    }
}
