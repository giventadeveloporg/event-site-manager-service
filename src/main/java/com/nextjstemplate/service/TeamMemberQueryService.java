package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.TeamMember;
import com.nextjstemplate.repository.TeamMemberRepository;
import com.nextjstemplate.service.criteria.TeamMemberCriteria;
import com.nextjstemplate.service.dto.TeamMemberDTO;
import com.nextjstemplate.service.mapper.TeamMemberMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TeamMember} entities in the database.
 */
@Service
@Transactional(readOnly = true)
public class TeamMemberQueryService extends QueryService<TeamMember> {

    private static final Logger LOG = LoggerFactory.getLogger(TeamMemberQueryService.class);

    private final TeamMemberRepository teamMemberRepository;

    private final TeamMemberMapper teamMemberMapper;

    public TeamMemberQueryService(TeamMemberRepository teamMemberRepository, TeamMemberMapper teamMemberMapper) {
        this.teamMemberRepository = teamMemberRepository;
        this.teamMemberMapper = teamMemberMapper;
    }

    @Transactional(readOnly = true)
    public Page<TeamMemberDTO> findByCriteria(TeamMemberCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TeamMember> specification = createSpecification(criteria);
        return teamMemberRepository.findAll(specification, page).map(teamMemberMapper::toDto);
    }

    @Transactional(readOnly = true)
    public long countByCriteria(TeamMemberCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TeamMember> specification = createSpecification(criteria);
        return teamMemberRepository.count(specification);
    }

    protected Specification<TeamMember> createSpecification(TeamMemberCriteria criteria) {
        Specification<TeamMember> specification = Specification.where(null);
        if (criteria != null) {
            specification =
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                    criteria.getId() != null ? buildRangeSpecification(criteria.getId(), TeamMember_.id) : null,
                    criteria.getTenantId() != null ? buildStringSpecification(criteria.getTenantId(), TeamMember_.tenantId) : null,
                    criteria.getTeamGroupId() != null ? buildRangeSpecification(criteria.getTeamGroupId(), TeamMember_.teamGroupId) : null,
                    criteria.getUserProfileId() != null
                        ? buildRangeSpecification(criteria.getUserProfileId(), TeamMember_.userProfileId)
                        : null,
                    criteria.getFirstName() != null ? buildStringSpecification(criteria.getFirstName(), TeamMember_.firstName) : null,
                    criteria.getLastName() != null ? buildStringSpecification(criteria.getLastName(), TeamMember_.lastName) : null,
                    criteria.getTitle() != null ? buildStringSpecification(criteria.getTitle(), TeamMember_.title) : null,
                    criteria.getDesignation() != null ? buildStringSpecification(criteria.getDesignation(), TeamMember_.designation) : null,
                    criteria.getBio() != null ? buildStringSpecification(criteria.getBio(), TeamMember_.bio) : null,
                    criteria.getEmail() != null ? buildStringSpecification(criteria.getEmail(), TeamMember_.email) : null,
                    criteria.getProfileImageUrl() != null
                        ? buildStringSpecification(criteria.getProfileImageUrl(), TeamMember_.profileImageUrl)
                        : null,
                    criteria.getExpertise() != null ? buildStringSpecification(criteria.getExpertise(), TeamMember_.expertise) : null,
                    criteria.getImageBackground() != null
                        ? buildStringSpecification(criteria.getImageBackground(), TeamMember_.imageBackground)
                        : null,
                    criteria.getImageStyle() != null ? buildStringSpecification(criteria.getImageStyle(), TeamMember_.imageStyle) : null,
                    criteria.getDepartment() != null ? buildStringSpecification(criteria.getDepartment(), TeamMember_.department) : null,
                    criteria.getJoinDate() != null ? buildRangeSpecification(criteria.getJoinDate(), TeamMember_.joinDate) : null,
                    criteria.getIsActive() != null ? buildSpecification(criteria.getIsActive(), TeamMember_.isActive) : null,
                    criteria.getLinkedinUrl() != null ? buildStringSpecification(criteria.getLinkedinUrl(), TeamMember_.linkedinUrl) : null,
                    criteria.getTwitterUrl() != null ? buildStringSpecification(criteria.getTwitterUrl(), TeamMember_.twitterUrl) : null,
                    criteria.getPriorityOrder() != null
                        ? buildRangeSpecification(criteria.getPriorityOrder(), TeamMember_.priorityOrder)
                        : null,
                    criteria.getWebsiteUrl() != null ? buildStringSpecification(criteria.getWebsiteUrl(), TeamMember_.websiteUrl) : null,
                    criteria.getJerseyNumber() != null
                        ? buildRangeSpecification(criteria.getJerseyNumber(), TeamMember_.jerseyNumber)
                        : null,
                    criteria.getPosition() != null ? buildStringSpecification(criteria.getPosition(), TeamMember_.position) : null,
                    criteria.getLineupSubtitle() != null
                        ? buildStringSpecification(criteria.getLineupSubtitle(), TeamMember_.lineupSubtitle)
                        : null,
                    criteria.getInstrument() != null ? buildStringSpecification(criteria.getInstrument(), TeamMember_.instrument) : null,
                    criteria.getVocalRole() != null ? buildStringSpecification(criteria.getVocalRole(), TeamMember_.vocalRole) : null
                );
        }
        return specification;
    }
}
