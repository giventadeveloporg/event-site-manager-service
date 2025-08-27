package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.ExecutiveCommitteeTeamMember;
import com.nextjstemplate.repository.ExecutiveCommitteeTeamMemberRepository;
import com.nextjstemplate.service.criteria.ExecutiveCommitteeTeamMemberCriteria;
import com.nextjstemplate.service.dto.ExecutiveCommitteeTeamMemberDTO;
import com.nextjstemplate.service.mapper.ExecutiveCommitteeTeamMemberMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ExecutiveCommitteeTeamMember} entities in the database.
 * The main input is a {@link ExecutiveCommitteeTeamMemberCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ExecutiveCommitteeTeamMemberDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExecutiveCommitteeTeamMemberQueryService extends QueryService<ExecutiveCommitteeTeamMember> {

    private static final Logger LOG = LoggerFactory.getLogger(ExecutiveCommitteeTeamMemberQueryService.class);

    private final ExecutiveCommitteeTeamMemberRepository executiveCommitteeTeamMemberRepository;

    private final ExecutiveCommitteeTeamMemberMapper executiveCommitteeTeamMemberMapper;

    public ExecutiveCommitteeTeamMemberQueryService(
        ExecutiveCommitteeTeamMemberRepository executiveCommitteeTeamMemberRepository,
        ExecutiveCommitteeTeamMemberMapper executiveCommitteeTeamMemberMapper
    ) {
        this.executiveCommitteeTeamMemberRepository = executiveCommitteeTeamMemberRepository;
        this.executiveCommitteeTeamMemberMapper = executiveCommitteeTeamMemberMapper;
    }

    /**
     * Return a {@link Page} of {@link ExecutiveCommitteeTeamMemberDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExecutiveCommitteeTeamMemberDTO> findByCriteria(ExecutiveCommitteeTeamMemberCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ExecutiveCommitteeTeamMember> specification = createSpecification(criteria);
        return executiveCommitteeTeamMemberRepository.findAll(specification, page).map(executiveCommitteeTeamMemberMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExecutiveCommitteeTeamMemberCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ExecutiveCommitteeTeamMember> specification = createSpecification(criteria);
        return executiveCommitteeTeamMemberRepository.count(specification);
    }

    /**
     * Function to convert {@link ExecutiveCommitteeTeamMemberCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ExecutiveCommitteeTeamMember> createSpecification(ExecutiveCommitteeTeamMemberCriteria criteria) {
        Specification<ExecutiveCommitteeTeamMember> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                criteria.getId() != null ? buildRangeSpecification(criteria.getId(), ExecutiveCommitteeTeamMember_.id) : null,
                criteria.getFirstName() != null ? buildStringSpecification(criteria.getFirstName(), ExecutiveCommitteeTeamMember_.firstName) : null,
                criteria.getLastName() != null ? buildStringSpecification(criteria.getLastName(), ExecutiveCommitteeTeamMember_.lastName) : null,
                criteria.getTitle() != null ? buildStringSpecification(criteria.getTitle(), ExecutiveCommitteeTeamMember_.title) : null,
                criteria.getDesignation() != null ? buildStringSpecification(criteria.getDesignation(), ExecutiveCommitteeTeamMember_.designation) : null,
                criteria.getBio() != null ? buildStringSpecification(criteria.getBio(), ExecutiveCommitteeTeamMember_.bio) : null,
                criteria.getEmail() != null ? buildStringSpecification(criteria.getEmail(), ExecutiveCommitteeTeamMember_.email) : null,
                criteria.getProfileImageUrl() != null ? buildStringSpecification(criteria.getProfileImageUrl(), ExecutiveCommitteeTeamMember_.profileImageUrl) : null,
                criteria.getExpertise() != null ? buildStringSpecification(criteria.getExpertise(), ExecutiveCommitteeTeamMember_.expertise) : null,
                criteria.getImageBackground() != null ? buildStringSpecification(criteria.getImageBackground(), ExecutiveCommitteeTeamMember_.imageBackground) : null,
                criteria.getImageStyle() != null ? buildStringSpecification(criteria.getImageStyle(), ExecutiveCommitteeTeamMember_.imageStyle) : null,
                criteria.getDepartment() != null ? buildStringSpecification(criteria.getDepartment(), ExecutiveCommitteeTeamMember_.department) : null,
                criteria.getJoinDate() != null ? buildRangeSpecification(criteria.getJoinDate(), ExecutiveCommitteeTeamMember_.joinDate) : null,
                criteria.getIsActive() != null ? buildSpecification(criteria.getIsActive(), ExecutiveCommitteeTeamMember_.isActive) : null,
                criteria.getLinkedinUrl() != null ? buildStringSpecification(criteria.getLinkedinUrl(), ExecutiveCommitteeTeamMember_.linkedinUrl) : null,
                criteria.getTwitterUrl() != null ? buildStringSpecification(criteria.getTwitterUrl(), ExecutiveCommitteeTeamMember_.twitterUrl) : null,
                criteria.getPriorityOrder() != null ? buildRangeSpecification(criteria.getPriorityOrder(), ExecutiveCommitteeTeamMember_.priorityOrder) : null,
                criteria.getWebsiteUrl() != null ? buildStringSpecification(criteria.getWebsiteUrl(), ExecutiveCommitteeTeamMember_.websiteUrl) : null
            );
        }
        return specification;
    }
}
