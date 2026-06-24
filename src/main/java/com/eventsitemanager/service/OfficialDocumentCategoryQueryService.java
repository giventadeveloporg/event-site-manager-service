package com.eventsitemanager.service;

import com.eventsitemanager.domain.OfficialDocumentCategory;
import com.eventsitemanager.domain.OfficialDocumentCategory_;
import com.eventsitemanager.repository.OfficialDocumentCategoryRepository;
import com.eventsitemanager.service.criteria.OfficialDocumentCategoryCriteria;
import com.eventsitemanager.service.dto.OfficialDocumentCategoryDTO;
import com.eventsitemanager.service.mapper.OfficialDocumentCategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Query service for {@link OfficialDocumentCategory}.
 */
@Service
@Transactional(readOnly = true)
public class OfficialDocumentCategoryQueryService extends QueryService<OfficialDocumentCategory> {

    private static final Logger LOG = LoggerFactory.getLogger(OfficialDocumentCategoryQueryService.class);

    private final OfficialDocumentCategoryRepository officialDocumentCategoryRepository;

    private final OfficialDocumentCategoryMapper officialDocumentCategoryMapper;

    public OfficialDocumentCategoryQueryService(
        OfficialDocumentCategoryRepository officialDocumentCategoryRepository,
        OfficialDocumentCategoryMapper officialDocumentCategoryMapper
    ) {
        this.officialDocumentCategoryRepository = officialDocumentCategoryRepository;
        this.officialDocumentCategoryMapper = officialDocumentCategoryMapper;
    }

    @Transactional(readOnly = true)
    public Page<OfficialDocumentCategoryDTO> findByCriteria(OfficialDocumentCategoryCriteria criteria, Pageable page) {
        LOG.debug("find OfficialDocumentCategory by criteria: {}, page: {}", criteria, page);
        final Specification<OfficialDocumentCategory> specification = createSpecification(criteria);
        return officialDocumentCategoryRepository.findAll(specification, page).map(officialDocumentCategoryMapper::toDto);
    }

    @Transactional(readOnly = true)
    public long countByCriteria(OfficialDocumentCategoryCriteria criteria) {
        LOG.debug("count OfficialDocumentCategory by criteria: {}", criteria);
        final Specification<OfficialDocumentCategory> specification = createSpecification(criteria);
        return officialDocumentCategoryRepository.count(specification);
    }

    protected Specification<OfficialDocumentCategory> createSpecification(OfficialDocumentCategoryCriteria criteria) {
        Specification<OfficialDocumentCategory> specification = Specification.where(null);
        if (criteria == null) {
            return specification;
        }
        if (criteria.getDistinct() != null) {
            specification = specification.and(distinct(criteria.getDistinct()));
        }
        if (criteria.getId() != null) {
            specification = specification.and(buildRangeSpecification(criteria.getId(), OfficialDocumentCategory_.id));
        }
        if (criteria.getTenantId() != null) {
            specification = specification.and(buildStringSpecification(criteria.getTenantId(), OfficialDocumentCategory_.tenantId));
        }
        if (criteria.getSlug() != null) {
            specification = specification.and(buildStringSpecification(criteria.getSlug(), OfficialDocumentCategory_.slug));
        }
        if (criteria.getDisplayName() != null) {
            specification = specification.and(buildStringSpecification(criteria.getDisplayName(), OfficialDocumentCategory_.displayName));
        }
        if (criteria.getIsActive() != null) {
            specification = specification.and(buildSpecification(criteria.getIsActive(), OfficialDocumentCategory_.isActive));
        }
        if (criteria.getSortOrder() != null) {
            specification = specification.and(buildRangeSpecification(criteria.getSortOrder(), OfficialDocumentCategory_.sortOrder));
        }
        if (criteria.getCreatedAt() != null) {
            specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), OfficialDocumentCategory_.createdAt));
        }
        if (criteria.getUpdatedAt() != null) {
            specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), OfficialDocumentCategory_.updatedAt));
        }
        return specification;
    }
}
