package com.eventsitemanager.service;

import com.eventsitemanager.domain.*; // for static metamodels
import com.eventsitemanager.domain.MembershipSubscription;
import com.eventsitemanager.repository.MembershipSubscriptionRepository;
import com.eventsitemanager.service.criteria.MembershipSubscriptionCriteria;
import com.eventsitemanager.service.dto.MembershipSubscriptionDTO;
import com.eventsitemanager.service.mapper.MembershipSubscriptionMapper;
import jakarta.persistence.criteria.JoinType;
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
 * Service for executing complex queries for {@link MembershipSubscription} entities in the database.
 * The main input is a {@link MembershipSubscriptionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 */
@Service
@Transactional(readOnly = true)
public class MembershipSubscriptionQueryService extends QueryService<MembershipSubscription> {

    private final Logger log = LoggerFactory.getLogger(MembershipSubscriptionQueryService.class);

    private final MembershipSubscriptionRepository membershipSubscriptionRepository;
    private final MembershipSubscriptionMapper membershipSubscriptionMapper;

    public MembershipSubscriptionQueryService(
        MembershipSubscriptionRepository membershipSubscriptionRepository,
        MembershipSubscriptionMapper membershipSubscriptionMapper
    ) {
        this.membershipSubscriptionRepository = membershipSubscriptionRepository;
        this.membershipSubscriptionMapper = membershipSubscriptionMapper;
    }

    /**
     * Return a {@link List} of {@link MembershipSubscriptionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MembershipSubscriptionDTO> findByCriteria(MembershipSubscriptionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MembershipSubscription> specification = createSpecification(criteria);
        return membershipSubscriptionMapper.toDto(membershipSubscriptionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MembershipSubscriptionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MembershipSubscriptionDTO> findByCriteria(MembershipSubscriptionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MembershipSubscription> specification = createSpecification(criteria);
        return membershipSubscriptionRepository.findAll(specification, page).map(membershipSubscriptionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MembershipSubscriptionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MembershipSubscription> specification = createSpecification(criteria);
        return membershipSubscriptionRepository.count(specification);
    }

    /**
     * Function to convert {@link MembershipSubscriptionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MembershipSubscription> createSpecification(MembershipSubscriptionCriteria criteria) {
        Specification<MembershipSubscription> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MembershipSubscription_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), MembershipSubscription_.tenantId));
            }
            if (criteria.getSubscriptionStatus() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getSubscriptionStatus(), MembershipSubscription_.subscriptionStatus)
                    );
            }
            if (criteria.getCurrentPeriodStart() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getCurrentPeriodStart(), MembershipSubscription_.currentPeriodStart)
                    );
            }
            if (criteria.getCurrentPeriodEnd() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getCurrentPeriodEnd(), MembershipSubscription_.currentPeriodEnd));
            }
            if (criteria.getTrialStart() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTrialStart(), MembershipSubscription_.trialStart));
            }
            if (criteria.getTrialEnd() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTrialEnd(), MembershipSubscription_.trialEnd));
            }
            if (criteria.getCancelAtPeriodEnd() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getCancelAtPeriodEnd(), MembershipSubscription_.cancelAtPeriodEnd));
            }
            if (criteria.getCancelledAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCancelledAt(), MembershipSubscription_.cancelledAt));
            }
            if (criteria.getStripeSubscriptionId() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getStripeSubscriptionId(), MembershipSubscription_.stripeSubscriptionId)
                    );
            }
            if (criteria.getStripeCustomerId() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getStripeCustomerId(), MembershipSubscription_.stripeCustomerId));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), MembershipSubscription_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), MembershipSubscription_.updatedAt));
            }
            if (criteria.getUserProfileId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUserProfileId(),
                            root -> root.join(MembershipSubscription_.userProfile, JoinType.LEFT).get(UserProfile_.id)
                        )
                    );
            }
            if (criteria.getMembershipPlanId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMembershipPlanId(),
                            root -> root.join(MembershipSubscription_.membershipPlan, JoinType.LEFT).get(MembershipPlan_.id)
                        )
                    );
            }
            if (criteria.getPaymentProviderConfigId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPaymentProviderConfigId(),
                            root -> root.join(MembershipSubscription_.paymentProviderConfig, JoinType.LEFT).get(PaymentProviderConfig_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
