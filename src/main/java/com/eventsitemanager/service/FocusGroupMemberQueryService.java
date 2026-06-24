package com.eventsitemanager.service;

import com.eventsitemanager.domain.*; // for static metamodels
import com.eventsitemanager.domain.FocusGroupMember;
import com.eventsitemanager.repository.FocusGroupMemberRepository;
import com.eventsitemanager.service.criteria.FocusGroupMemberCriteria;
import com.eventsitemanager.service.dto.FocusGroupMemberDTO;
import com.eventsitemanager.service.mapper.FocusGroupMemberMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link FocusGroupMember} entities
 * in the database.
 * The main input is a {@link FocusGroupMemberCriteria} which gets converted to
 * {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link FocusGroupMemberDTO} which fulfills the
 * criteria.
 */
@Service
@Transactional(readOnly = true)
public class FocusGroupMemberQueryService extends QueryService<FocusGroupMember> {

    private static final Logger LOG = LoggerFactory.getLogger(FocusGroupMemberQueryService.class);

    private final FocusGroupMemberRepository focusGroupMemberRepository;

    private final FocusGroupMemberMapper focusGroupMemberMapper;

    public FocusGroupMemberQueryService(
        FocusGroupMemberRepository focusGroupMemberRepository,
        FocusGroupMemberMapper focusGroupMemberMapper
    ) {
        this.focusGroupMemberRepository = focusGroupMemberRepository;
        this.focusGroupMemberMapper = focusGroupMemberMapper;
    }

    /**
     * Return a {@link Page} of {@link FocusGroupMemberDTO} which matches the
     * criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities
     *                 should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FocusGroupMemberDTO> findByCriteria(FocusGroupMemberCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FocusGroupMember> specification = createSpecification(criteria);
        return focusGroupMemberRepository.findAll(specification, page).map(focusGroupMemberMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     *
     * @param criteria The object which holds all the filters, which the entities
     *                 should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FocusGroupMemberCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<FocusGroupMember> specification = createSpecification(criteria);
        return focusGroupMemberRepository.count(specification);
    }

    /**
     * Function to convert {@link FocusGroupMemberCriteria} to a
     * {@link Specification}
     *
     * @param criteria The object which holds all the filters, which the entities
     *                 should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FocusGroupMember> createSpecification(FocusGroupMemberCriteria criteria) {
        Specification<FocusGroupMember> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FocusGroupMember_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), FocusGroupMember_.tenantId));
            }
            if (criteria.getFocusGroupId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFocusGroupId(),
                            root -> root.join(FocusGroupMember_.focusGroup, JoinType.LEFT).get(FocusGroup_.id)
                        )
                    );
            }
            if (criteria.getUserProfileId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserProfileId(), FocusGroupMember_.userProfileId));
            }
            if (criteria.getRole() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRole(), FocusGroupMember_.role));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), FocusGroupMember_.status));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), FocusGroupMember_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), FocusGroupMember_.updatedAt));
            }
        }
        return specification;
    }
}
