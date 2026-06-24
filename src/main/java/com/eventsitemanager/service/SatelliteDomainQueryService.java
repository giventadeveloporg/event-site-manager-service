package com.eventsitemanager.service;

import com.eventsitemanager.domain.*; // for static metamodels
import com.eventsitemanager.domain.SatelliteDomain;
import com.eventsitemanager.repository.SatelliteDomainRepository;
import com.eventsitemanager.service.criteria.SatelliteDomainCriteria;
import com.eventsitemanager.service.dto.SatelliteDomainDTO;
import com.eventsitemanager.service.mapper.SatelliteDomainMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link SatelliteDomain} entities in
 * the database.
 * The main input is a {@link SatelliteDomainCriteria} which gets converted to
 * {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SatelliteDomainDTO} which fulfills the
 * criteria.
 */
@Service
@Transactional(readOnly = true)
public class SatelliteDomainQueryService extends QueryService<SatelliteDomain> {

    private static final Logger LOG = LoggerFactory.getLogger(SatelliteDomainQueryService.class);

    private final SatelliteDomainRepository satelliteDomainRepository;

    private final SatelliteDomainMapper satelliteDomainMapper;

    public SatelliteDomainQueryService(SatelliteDomainRepository satelliteDomainRepository, SatelliteDomainMapper satelliteDomainMapper) {
        this.satelliteDomainRepository = satelliteDomainRepository;
        this.satelliteDomainMapper = satelliteDomainMapper;
    }

    /**
     * Return a {@link Page} of {@link SatelliteDomainDTO} which matches the criteria
     * from the database.
     *
     * @param criteria The object which holds all the filters, which the entities
     *                 should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SatelliteDomainDTO> findByCriteria(SatelliteDomainCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SatelliteDomain> specification = createSpecification(criteria);
        return satelliteDomainRepository.findAll(specification, page).map(satelliteDomainMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     *
     * @param criteria The object which holds all the filters, which the entities
     *                 should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SatelliteDomainCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SatelliteDomain> specification = createSpecification(criteria);
        return satelliteDomainRepository.count(specification);
    }

    /**
     * Function to convert {@link SatelliteDomainCriteria} to a {@link Specification}
     *
     * @param criteria The object which holds all the filters, which the entities
     *                 should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SatelliteDomain> createSpecification(SatelliteDomainCriteria criteria) {
        Specification<SatelliteDomain> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SatelliteDomain_.id));
            }
            if (criteria.getSatelliteKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSatelliteKey(), SatelliteDomain_.satelliteKey));
            }
            if (criteria.getDomain() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDomain(), SatelliteDomain_.domain));
            }
            if (criteria.getHostname() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHostname(), SatelliteDomain_.hostname));
            }
            if (criteria.getDisplayName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDisplayName(), SatelliteDomain_.displayName));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), SatelliteDomain_.tenantId));
            }
            if (criteria.getEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getEnabled(), SatelliteDomain_.enabled));
            }
            if (criteria.getOrgName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrgName(), SatelliteDomain_.orgName));
            }
            if (criteria.getFullName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFullName(), SatelliteDomain_.fullName));
            }
            if (criteria.getContactEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactEmail(), SatelliteDomain_.contactEmail));
            }
            if (criteria.getShowOnAuthHeader() != null) {
                specification = specification.and(buildSpecification(criteria.getShowOnAuthHeader(), SatelliteDomain_.showOnAuthHeader));
            }
            if (criteria.getShowOnAuthFooter() != null) {
                specification = specification.and(buildSpecification(criteria.getShowOnAuthFooter(), SatelliteDomain_.showOnAuthFooter));
            }
        }
        return specification;
    }
}
