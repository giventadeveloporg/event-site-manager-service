package com.eventsitemanager.service;

import com.eventsitemanager.domain.HomepageYoutubeOverride;
import com.eventsitemanager.domain.HomepageYoutubeOverride_;
import com.eventsitemanager.repository.HomepageYoutubeOverrideRepository;
import com.eventsitemanager.service.criteria.HomepageYoutubeOverrideCriteria;
import com.eventsitemanager.service.dto.HomepageYoutubeOverrideDTO;
import com.eventsitemanager.service.mapper.HomepageYoutubeOverrideMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link HomepageYoutubeOverride} entities.
 */
@Service
@Transactional(readOnly = true)
public class HomepageYoutubeOverrideQueryService extends QueryService<HomepageYoutubeOverride> {

    private static final Logger LOG = LoggerFactory.getLogger(HomepageYoutubeOverrideQueryService.class);

    private final HomepageYoutubeOverrideRepository homepageYoutubeOverrideRepository;

    private final HomepageYoutubeOverrideMapper homepageYoutubeOverrideMapper;

    public HomepageYoutubeOverrideQueryService(
        HomepageYoutubeOverrideRepository homepageYoutubeOverrideRepository,
        HomepageYoutubeOverrideMapper homepageYoutubeOverrideMapper
    ) {
        this.homepageYoutubeOverrideRepository = homepageYoutubeOverrideRepository;
        this.homepageYoutubeOverrideMapper = homepageYoutubeOverrideMapper;
    }

    @Transactional(readOnly = true)
    public Page<HomepageYoutubeOverrideDTO> findByCriteria(HomepageYoutubeOverrideCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HomepageYoutubeOverride> specification = createSpecification(criteria);
        return homepageYoutubeOverrideRepository.findAll(specification, page).map(homepageYoutubeOverrideMapper::toDto);
    }

    @Transactional(readOnly = true)
    public long countByCriteria(HomepageYoutubeOverrideCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<HomepageYoutubeOverride> specification = createSpecification(criteria);
        return homepageYoutubeOverrideRepository.count(specification);
    }

    protected Specification<HomepageYoutubeOverride> createSpecification(HomepageYoutubeOverrideCriteria criteria) {
        Specification<HomepageYoutubeOverride> specification = Specification.where(null);
        if (criteria != null) {
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                criteria.getId() != null ? buildRangeSpecification(criteria.getId(), HomepageYoutubeOverride_.id) : null,
                criteria.getTenantId() != null ? buildStringSpecification(criteria.getTenantId(), HomepageYoutubeOverride_.tenantId) : null,
                criteria.getTitle() != null ? buildStringSpecification(criteria.getTitle(), HomepageYoutubeOverride_.title) : null,
                criteria.getIsActive() != null ? buildSpecification(criteria.getIsActive(), HomepageYoutubeOverride_.isActive) : null
            );
        }
        return specification;
    }
}
