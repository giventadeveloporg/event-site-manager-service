package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.MembershipPlan;
import com.nextjstemplate.repository.MembershipPlanRepository;
import com.nextjstemplate.service.criteria.MembershipPlanCriteria;
import com.nextjstemplate.service.dto.MembershipPlanDTO;
import com.nextjstemplate.service.mapper.MembershipPlanMapper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link MembershipPlan} entities in the database.
 * The main input is a {@link MembershipPlanCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 */
@Service
@Transactional(readOnly = true)
public class MembershipPlanQueryService extends QueryService<MembershipPlan> {

    private final Logger log = LoggerFactory.getLogger(MembershipPlanQueryService.class);

    private final MembershipPlanRepository membershipPlanRepository;
    private final MembershipPlanMapper membershipPlanMapper;

    public MembershipPlanQueryService(MembershipPlanRepository membershipPlanRepository, MembershipPlanMapper membershipPlanMapper) {
        this.membershipPlanRepository = membershipPlanRepository;
        this.membershipPlanMapper = membershipPlanMapper;
    }

    /**
     * Return a {@link List} of {@link MembershipPlanDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MembershipPlanDTO> findByCriteria(MembershipPlanCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MembershipPlan> specification = createSpecification(criteria);
        return membershipPlanMapper.toDto(membershipPlanRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MembershipPlanDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MembershipPlanDTO> findByCriteria(MembershipPlanCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MembershipPlan> specification = createSpecification(criteria);
        return membershipPlanRepository.findAll(specification, page).map(membershipPlanMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MembershipPlanCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MembershipPlan> specification = createSpecification(criteria);
        return membershipPlanRepository.count(specification);
    }

    /**
     * Function to convert {@link MembershipPlanCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MembershipPlan> createSpecification(MembershipPlanCriteria criteria) {
        Specification<MembershipPlan> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MembershipPlan_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), MembershipPlan_.tenantId));
            }
            if (criteria.getPlanName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPlanName(), MembershipPlan_.planName));
            }
            if (criteria.getPlanCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPlanCode(), MembershipPlan_.planCode));
            }
            if (criteria.getPlanType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPlanType(), MembershipPlan_.planType));
            }
            if (criteria.getBillingInterval() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBillingInterval(), MembershipPlan_.billingInterval));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), MembershipPlan_.price));
            }
            if (criteria.getCurrency() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCurrency(), MembershipPlan_.currency));
            }
            if (criteria.getTrialDays() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTrialDays(), MembershipPlan_.trialDays));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), MembershipPlan_.isActive));
            }
            if (criteria.getMaxEventsPerMonth() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getMaxEventsPerMonth(), MembershipPlan_.maxEventsPerMonth));
            }
            if (criteria.getMaxAttendeesPerEvent() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getMaxAttendeesPerEvent(), MembershipPlan_.maxAttendeesPerEvent));
            }
            if (criteria.getStripePriceId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStripePriceId(), MembershipPlan_.stripePriceId));
            }
            if (criteria.getStripeProductId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStripeProductId(), MembershipPlan_.stripeProductId));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), MembershipPlan_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), MembershipPlan_.updatedAt));
            }
        }
        return specification;
    }
}
