package com.eventsitemanager.service;

import com.eventsitemanager.domain.*; // for static metamodels
import com.eventsitemanager.domain.TenantOnboardingRequest;
import com.eventsitemanager.repository.TenantOnboardingRequestRepository;
import com.eventsitemanager.service.criteria.TenantOnboardingRequestCriteria;
import com.eventsitemanager.service.dto.TenantOnboardingRequestDTO;
import com.eventsitemanager.service.mapper.TenantOnboardingRequestMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TenantOnboardingRequest} entities in the database.
 */
@Service
@Transactional(readOnly = true)
public class TenantOnboardingRequestQueryService extends QueryService<TenantOnboardingRequest> {

    private static final Logger LOG = LoggerFactory.getLogger(TenantOnboardingRequestQueryService.class);

    private final TenantOnboardingRequestRepository tenantOnboardingRequestRepository;

    private final TenantOnboardingRequestMapper tenantOnboardingRequestMapper;

    public TenantOnboardingRequestQueryService(
        TenantOnboardingRequestRepository tenantOnboardingRequestRepository,
        TenantOnboardingRequestMapper tenantOnboardingRequestMapper
    ) {
        this.tenantOnboardingRequestRepository = tenantOnboardingRequestRepository;
        this.tenantOnboardingRequestMapper = tenantOnboardingRequestMapper;
    }

    @Transactional(readOnly = true)
    public Page<TenantOnboardingRequestDTO> findByCriteria(TenantOnboardingRequestCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TenantOnboardingRequest> specification = createSpecification(criteria);
        return tenantOnboardingRequestRepository.findAll(specification, page).map(tenantOnboardingRequestMapper::toDto);
    }

    @Transactional(readOnly = true)
    public long countByCriteria(TenantOnboardingRequestCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TenantOnboardingRequest> specification = createSpecification(criteria);
        return tenantOnboardingRequestRepository.count(specification);
    }

    protected Specification<TenantOnboardingRequest> createSpecification(TenantOnboardingRequestCriteria criteria) {
        Specification<TenantOnboardingRequest> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TenantOnboardingRequest_.id));
            }
            if (criteria.getRequestCode() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getRequestCode(), TenantOnboardingRequest_.requestCode));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), TenantOnboardingRequest_.status));
            }
            if (criteria.getSource() != null) {
                specification = specification.and(buildSpecification(criteria.getSource(), TenantOnboardingRequest_.source));
            }
            if (criteria.getOrganizationName() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getOrganizationName(), TenantOnboardingRequest_.organizationName));
            }
            if (criteria.getSiteType() != null) {
                specification = specification.and(buildSpecification(criteria.getSiteType(), TenantOnboardingRequest_.siteType));
            }
            if (criteria.getContactEmail() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getContactEmail(), TenantOnboardingRequest_.contactEmail));
            }
            if (criteria.getRequestedHostname() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getRequestedHostname(), TenantOnboardingRequest_.requestedHostname)
                    );
            }
            if (criteria.getAssignedTenantId() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAssignedTenantId(), TenantOnboardingRequest_.assignedTenantId));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), TenantOnboardingRequest_.createdAt));
            }
        }
        return specification;
    }
}
