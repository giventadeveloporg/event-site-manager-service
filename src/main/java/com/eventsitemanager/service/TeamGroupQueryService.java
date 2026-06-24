package com.eventsitemanager.service;

import com.eventsitemanager.domain.*; // for static metamodels
import com.eventsitemanager.domain.TeamGroup;
import com.eventsitemanager.repository.TeamGroupRepository;
import com.eventsitemanager.service.criteria.TeamGroupCriteria;
import com.eventsitemanager.service.dto.TeamGroupDTO;
import com.eventsitemanager.service.mapper.TeamGroupMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TeamGroup} entities in the database.
 */
@Service
@Transactional(readOnly = true)
public class TeamGroupQueryService extends QueryService<TeamGroup> {

    private static final Logger LOG = LoggerFactory.getLogger(TeamGroupQueryService.class);

    private final TeamGroupRepository teamGroupRepository;

    private final TeamGroupMapper teamGroupMapper;

    public TeamGroupQueryService(TeamGroupRepository teamGroupRepository, TeamGroupMapper teamGroupMapper) {
        this.teamGroupRepository = teamGroupRepository;
        this.teamGroupMapper = teamGroupMapper;
    }

    @Transactional(readOnly = true)
    public Page<TeamGroupDTO> findByCriteria(TeamGroupCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TeamGroup> specification = createSpecification(criteria);
        return teamGroupRepository.findAll(specification, page).map(teamGroupMapper::toDto);
    }

    @Transactional(readOnly = true)
    public long countByCriteria(TeamGroupCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TeamGroup> specification = createSpecification(criteria);
        return teamGroupRepository.count(specification);
    }

    protected Specification<TeamGroup> createSpecification(TeamGroupCriteria criteria) {
        Specification<TeamGroup> specification = Specification.where(null);
        if (criteria != null) {
            specification =
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                    criteria.getId() != null ? buildRangeSpecification(criteria.getId(), TeamGroup_.id) : null,
                    criteria.getTenantId() != null ? buildStringSpecification(criteria.getTenantId(), TeamGroup_.tenantId) : null,
                    criteria.getTeamType() != null ? buildStringSpecification(criteria.getTeamType(), TeamGroup_.teamType) : null,
                    criteria.getName() != null ? buildStringSpecification(criteria.getName(), TeamGroup_.name) : null,
                    criteria.getSlug() != null ? buildStringSpecification(criteria.getSlug(), TeamGroup_.slug) : null,
                    criteria.getIsActive() != null ? buildSpecification(criteria.getIsActive(), TeamGroup_.isActive) : null,
                    criteria.getDisplayOrder() != null ? buildRangeSpecification(criteria.getDisplayOrder(), TeamGroup_.displayOrder) : null
                );
        }
        return specification;
    }
}
