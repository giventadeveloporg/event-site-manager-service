package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.TenantOrganization;
import com.nextjstemplate.repository.TenantOrganizationRepository;
import com.nextjstemplate.service.criteria.TenantOrganizationCriteria;
import com.nextjstemplate.service.dto.TenantOrganizationDTO;
import com.nextjstemplate.service.mapper.TenantOrganizationMapper;
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
 * Service for executing complex queries for {@link TenantOrganization} entities in the database.
 * The main input is a {@link TenantOrganizationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TenantOrganizationDTO} or a {@link Page} of {@link TenantOrganizationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TenantOrganizationQueryService extends QueryService<TenantOrganization> {

    private final Logger log = LoggerFactory.getLogger(TenantOrganizationQueryService.class);

    private final TenantOrganizationRepository tenantOrganizationRepository;

    private final TenantOrganizationMapper tenantOrganizationMapper;

    public TenantOrganizationQueryService(
        TenantOrganizationRepository tenantOrganizationRepository,
        TenantOrganizationMapper tenantOrganizationMapper
    ) {
        this.tenantOrganizationRepository = tenantOrganizationRepository;
        this.tenantOrganizationMapper = tenantOrganizationMapper;
    }

    /**
     * Return a {@link List} of {@link TenantOrganizationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TenantOrganizationDTO> findByCriteria(TenantOrganizationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TenantOrganization> specification = createSpecification(criteria);
        return tenantOrganizationMapper.toDto(tenantOrganizationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TenantOrganizationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TenantOrganizationDTO> findByCriteria(TenantOrganizationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TenantOrganization> specification = createSpecification(criteria);
        return tenantOrganizationRepository.findAll(specification, page).map(tenantOrganizationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TenantOrganizationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TenantOrganization> specification = createSpecification(criteria);
        return tenantOrganizationRepository.count(specification);
    }

    /**
     * Function to convert {@link TenantOrganizationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TenantOrganization> createSpecification(TenantOrganizationCriteria criteria) {
        Specification<TenantOrganization> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TenantOrganization_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), TenantOrganization_.tenantId));
            }
            if (criteria.getOrganizationName() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getOrganizationName(), TenantOrganization_.organizationName));
            }
            if (criteria.getDomain() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDomain(), TenantOrganization_.domain));
            }
            if (criteria.getPrimaryColor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrimaryColor(), TenantOrganization_.primaryColor));
            }
            if (criteria.getSecondaryColor() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getSecondaryColor(), TenantOrganization_.secondaryColor));
            }
            if (criteria.getLogoUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLogoUrl(), TenantOrganization_.logoUrl));
            }
            if (criteria.getContactEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactEmail(), TenantOrganization_.contactEmail));
            }
            if (criteria.getContactPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactPhone(), TenantOrganization_.contactPhone));
            }
            if (criteria.getSubscriptionPlan() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getSubscriptionPlan(), TenantOrganization_.subscriptionPlan));
            }
            if (criteria.getSubscriptionStatus() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getSubscriptionStatus(), TenantOrganization_.subscriptionStatus));
            }
            if (criteria.getSubscriptionStartDate() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getSubscriptionStartDate(), TenantOrganization_.subscriptionStartDate)
                    );
            }
            if (criteria.getSubscriptionEndDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getSubscriptionEndDate(), TenantOrganization_.subscriptionEndDate));
            }
            if (criteria.getMonthlyFeeUsd() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMonthlyFeeUsd(), TenantOrganization_.monthlyFeeUsd));
            }
            if (criteria.getStripeCustomerId() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getStripeCustomerId(), TenantOrganization_.stripeCustomerId));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), TenantOrganization_.isActive));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), TenantOrganization_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), TenantOrganization_.updatedAt));
            }
            /* if (criteria.getTenantSettingsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTenantSettingsId(),
                            root -> root.join(TenantOrganization_.tenantSettings, JoinType.LEFT).get(TenantSettings_.id)
                        )
                    );
            }*/
        }
        return specification;
    }
}
