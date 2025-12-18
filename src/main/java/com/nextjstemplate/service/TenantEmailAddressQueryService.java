package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.TenantEmailAddress;
import com.nextjstemplate.repository.TenantEmailAddressRepository;
import com.nextjstemplate.service.criteria.TenantEmailAddressCriteria;
import com.nextjstemplate.service.dto.TenantEmailAddressDTO;
import com.nextjstemplate.service.mapper.TenantEmailAddressMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TenantEmailAddress} entities in the database.
 * The main input is a {@link TenantEmailAddressCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TenantEmailAddressDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TenantEmailAddressQueryService extends QueryService<TenantEmailAddress> {

    private static final Logger LOG = LoggerFactory.getLogger(TenantEmailAddressQueryService.class);

    private final TenantEmailAddressRepository tenantEmailAddressRepository;

    private final TenantEmailAddressMapper tenantEmailAddressMapper;

    public TenantEmailAddressQueryService(
        TenantEmailAddressRepository tenantEmailAddressRepository,
        TenantEmailAddressMapper tenantEmailAddressMapper
    ) {
        this.tenantEmailAddressRepository = tenantEmailAddressRepository;
        this.tenantEmailAddressMapper = tenantEmailAddressMapper;
    }

    /**
     * Return a {@link Page} of {@link TenantEmailAddressDTO} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TenantEmailAddressDTO> findByCriteria(TenantEmailAddressCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TenantEmailAddress> specification = createSpecification(criteria);
        return tenantEmailAddressRepository.findAll(specification, page).map(tenantEmailAddressMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TenantEmailAddressCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TenantEmailAddress> specification = createSpecification(criteria);
        return tenantEmailAddressRepository.count(specification);
    }

    /**
     * Function to convert {@link TenantEmailAddressCriteria} to a {@link Specification}
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TenantEmailAddress> createSpecification(TenantEmailAddressCriteria criteria) {
        Specification<TenantEmailAddress> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TenantEmailAddress_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), TenantEmailAddress_.tenantId));
            }
            if (criteria.getEmailAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmailAddress(), TenantEmailAddress_.emailAddress));
            }
            if (criteria.getEmailType() != null) {
                specification =
                    specification.and((root, query, cb) -> {
                        jakarta.persistence.criteria.Expression<String> emailTypeStr = root
                            .get(TenantEmailAddress_.emailType)
                            .as(String.class);
                        tech.jhipster.service.filter.StringFilter emailTypeFilter = criteria.getEmailType();
                        jakarta.persistence.criteria.Predicate predicate = null;

                        if (emailTypeFilter.getEquals() != null) {
                            predicate = cb.equal(emailTypeStr, emailTypeFilter.getEquals());
                        } else if (emailTypeFilter.getNotEquals() != null) {
                            predicate = cb.notEqual(emailTypeStr, emailTypeFilter.getNotEquals());
                        } else if (emailTypeFilter.getSpecified() != null) {
                            if (Boolean.TRUE.equals(emailTypeFilter.getSpecified())) {
                                predicate = cb.isNotNull(emailTypeStr);
                            } else {
                                predicate = cb.isNull(emailTypeStr);
                            }
                        } else if (emailTypeFilter.getIn() != null && !emailTypeFilter.getIn().isEmpty()) {
                            predicate = emailTypeStr.in(emailTypeFilter.getIn());
                        } else if (emailTypeFilter.getNotIn() != null && !emailTypeFilter.getNotIn().isEmpty()) {
                            predicate = cb.not(emailTypeStr.in(emailTypeFilter.getNotIn()));
                        } else if (emailTypeFilter.getContains() != null) {
                            predicate = cb.like(cb.lower(emailTypeStr), "%" + emailTypeFilter.getContains().toLowerCase() + "%");
                        } else if (emailTypeFilter.getDoesNotContain() != null) {
                            predicate = cb.notLike(cb.lower(emailTypeStr), "%" + emailTypeFilter.getDoesNotContain().toLowerCase() + "%");
                        }

                        return predicate;
                    });
            }
            if (criteria.getDisplayName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDisplayName(), TenantEmailAddress_.displayName));
            }
            if (criteria.getCopyToEmailAddress() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getCopyToEmailAddress(), TenantEmailAddress_.copyToEmailAddress));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), TenantEmailAddress_.isActive));
            }
            if (criteria.getIsDefault() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDefault(), TenantEmailAddress_.isDefault));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), TenantEmailAddress_.description));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), TenantEmailAddress_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), TenantEmailAddress_.updatedAt));
            }
        }
        return specification;
    }
}
