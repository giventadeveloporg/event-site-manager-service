package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.FocusGroup;
import com.nextjstemplate.repository.FocusGroupRepository;
import com.nextjstemplate.service.criteria.FocusGroupCriteria;
import com.nextjstemplate.service.dto.FocusGroupDTO;
import com.nextjstemplate.service.mapper.FocusGroupMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link FocusGroup} entities in the
 * database.
 * The main input is a {@link FocusGroupCriteria} which gets converted to
 * {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link FocusGroupDTO} which fulfills the
 * criteria.
 */
@Service
@Transactional(readOnly = true)
public class FocusGroupQueryService extends QueryService<FocusGroup> {

    private static final Logger LOG = LoggerFactory.getLogger(FocusGroupQueryService.class);

    private final FocusGroupRepository focusGroupRepository;

    private final FocusGroupMapper focusGroupMapper;

    public FocusGroupQueryService(FocusGroupRepository focusGroupRepository, FocusGroupMapper focusGroupMapper) {
        this.focusGroupRepository = focusGroupRepository;
        this.focusGroupMapper = focusGroupMapper;
    }

    /**
     * Return a {@link Page} of {@link FocusGroupDTO} which matches the criteria
     * from the database.
     *
     * @param criteria The object which holds all the filters, which the entities
     *                 should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FocusGroupDTO> findByCriteria(FocusGroupCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FocusGroup> specification = createSpecification(criteria);
        return focusGroupRepository.findAll(specification, page).map(focusGroupMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     *
     * @param criteria The object which holds all the filters, which the entities
     *                 should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FocusGroupCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<FocusGroup> specification = createSpecification(criteria);
        return focusGroupRepository.count(specification);
    }

    /**
     * Function to convert {@link FocusGroupCriteria} to a {@link Specification}
     *
     * @param criteria The object which holds all the filters, which the entities
     *                 should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FocusGroup> createSpecification(FocusGroupCriteria criteria) {
        Specification<FocusGroup> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FocusGroup_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), FocusGroup_.tenantId));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), FocusGroup_.name));
            }
            if (criteria.getSlug() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSlug(), FocusGroup_.slug));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), FocusGroup_.isActive));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), FocusGroup_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), FocusGroup_.updatedAt));
            }
        }
        return specification;
    }
}
