package com.eventsitemanager.service;

import com.eventsitemanager.domain.OfficialDocumentYearBundle;
import com.eventsitemanager.domain.OfficialDocumentYearBundle_;
import com.eventsitemanager.repository.OfficialDocumentYearBundleRepository;
import com.eventsitemanager.service.criteria.OfficialDocumentYearBundleCriteria;
import com.eventsitemanager.service.dto.OfficialDocumentYearBundleDTO;
import com.eventsitemanager.service.mapper.OfficialDocumentYearBundleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Query service for {@link OfficialDocumentYearBundle}.
 */
@Service
@Transactional(readOnly = true)
public class OfficialDocumentYearBundleQueryService extends QueryService<OfficialDocumentYearBundle> {

    private static final Logger LOG = LoggerFactory.getLogger(OfficialDocumentYearBundleQueryService.class);

    private final OfficialDocumentYearBundleRepository officialDocumentYearBundleRepository;

    private final OfficialDocumentYearBundleMapper officialDocumentYearBundleMapper;

    public OfficialDocumentYearBundleQueryService(
        OfficialDocumentYearBundleRepository officialDocumentYearBundleRepository,
        OfficialDocumentYearBundleMapper officialDocumentYearBundleMapper
    ) {
        this.officialDocumentYearBundleRepository = officialDocumentYearBundleRepository;
        this.officialDocumentYearBundleMapper = officialDocumentYearBundleMapper;
    }

    @Transactional(readOnly = true)
    public Page<OfficialDocumentYearBundleDTO> findByCriteria(OfficialDocumentYearBundleCriteria criteria, Pageable page) {
        LOG.debug("find OfficialDocumentYearBundle by criteria: {}, page: {}", criteria, page);
        final Specification<OfficialDocumentYearBundle> specification = createSpecification(criteria);
        return officialDocumentYearBundleRepository.findAll(specification, page).map(officialDocumentYearBundleMapper::toDto);
    }

    @Transactional(readOnly = true)
    public long countByCriteria(OfficialDocumentYearBundleCriteria criteria) {
        LOG.debug("count OfficialDocumentYearBundle by criteria: {}", criteria);
        final Specification<OfficialDocumentYearBundle> specification = createSpecification(criteria);
        return officialDocumentYearBundleRepository.count(specification);
    }

    protected Specification<OfficialDocumentYearBundle> createSpecification(OfficialDocumentYearBundleCriteria criteria) {
        Specification<OfficialDocumentYearBundle> specification = Specification.where(null);
        if (criteria == null) {
            return specification;
        }
        if (criteria.getDistinct() != null) {
            specification = specification.and(distinct(criteria.getDistinct()));
        }
        if (criteria.getId() != null) {
            specification = specification.and(buildRangeSpecification(criteria.getId(), OfficialDocumentYearBundle_.id));
        }
        if (criteria.getTenantId() != null) {
            specification = specification.and(buildStringSpecification(criteria.getTenantId(), OfficialDocumentYearBundle_.tenantId));
        }
        if (criteria.getOfficialDocumentCategoryId() != null) {
            specification =
                specification.and(
                    buildRangeSpecification(
                        criteria.getOfficialDocumentCategoryId(),
                        OfficialDocumentYearBundle_.officialDocumentCategoryId
                    )
                );
        }
        if (criteria.getDocumentYear() != null) {
            specification =
                specification.and(buildRangeSpecification(criteria.getDocumentYear(), OfficialDocumentYearBundle_.documentYear));
        }
        if (criteria.getCoverEventMediaId() != null) {
            specification =
                specification.and(buildRangeSpecification(criteria.getCoverEventMediaId(), OfficialDocumentYearBundle_.coverEventMediaId));
        }
        if (criteria.getCreatedAt() != null) {
            specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), OfficialDocumentYearBundle_.createdAt));
        }
        if (criteria.getUpdatedAt() != null) {
            specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), OfficialDocumentYearBundle_.updatedAt));
        }
        return specification;
    }
}
