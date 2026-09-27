package com.eventsitemanager.service;

import com.eventsitemanager.domain.*; // for static metamodels
import com.eventsitemanager.domain.LastMatch;
import com.eventsitemanager.repository.LastMatchRepository;
import com.eventsitemanager.service.criteria.LastMatchCriteria;
import com.eventsitemanager.service.dto.LastMatchDTO;
import com.eventsitemanager.service.mapper.LastMatchMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link LastMatch} entities in the database.
 */
@Service
@Transactional(readOnly = true)
public class LastMatchQueryService extends QueryService<LastMatch> {

    private static final Logger LOG = LoggerFactory.getLogger(LastMatchQueryService.class);

    private final LastMatchRepository lastMatchRepository;

    private final LastMatchMapper lastMatchMapper;

    public LastMatchQueryService(LastMatchRepository lastMatchRepository, LastMatchMapper lastMatchMapper) {
        this.lastMatchRepository = lastMatchRepository;
        this.lastMatchMapper = lastMatchMapper;
    }

    @Transactional(readOnly = true)
    public Page<LastMatchDTO> findByCriteria(LastMatchCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LastMatch> specification = createSpecification(criteria);
        return lastMatchRepository.findAll(specification, page).map(lastMatchMapper::toDto);
    }

    @Transactional(readOnly = true)
    public long countByCriteria(LastMatchCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<LastMatch> specification = createSpecification(criteria);
        return lastMatchRepository.count(specification);
    }

    protected Specification<LastMatch> createSpecification(LastMatchCriteria criteria) {
        Specification<LastMatch> specification = Specification.where(null);
        if (criteria != null) {
            specification =
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                    criteria.getId() != null ? buildRangeSpecification(criteria.getId(), LastMatch_.id) : null,
                    criteria.getTenantId() != null ? buildStringSpecification(criteria.getTenantId(), LastMatch_.tenantId) : null,
                    criteria.getTitle() != null ? buildStringSpecification(criteria.getTitle(), LastMatch_.title) : null,
                    criteria.getLeagueName() != null ? buildStringSpecification(criteria.getLeagueName(), LastMatch_.leagueName) : null,
                    criteria.getMatchKind() != null ? buildStringSpecification(criteria.getMatchKind(), LastMatch_.matchKind) : null,
                    criteria.getIsActive() != null ? buildSpecification(criteria.getIsActive(), LastMatch_.isActive) : null,
                    criteria.getPriorityOrder() != null ? buildRangeSpecification(criteria.getPriorityOrder(), LastMatch_.priorityOrder) : null
                );
        }
        return specification;
    }
}
