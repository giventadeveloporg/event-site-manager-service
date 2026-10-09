package com.eventsitemanager.service;

import com.eventsitemanager.domain.ProfileFamilyMember;
import com.eventsitemanager.domain.ProfileFamilyMember_;
import com.eventsitemanager.repository.ProfileFamilyMemberRepository;
import com.eventsitemanager.service.criteria.ProfileFamilyMemberCriteria;
import com.eventsitemanager.service.dto.ProfileFamilyMemberDTO;
import com.eventsitemanager.service.mapper.ProfileFamilyMemberMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

@Service
@Transactional(readOnly = true)
public class ProfileFamilyMemberQueryService extends QueryService<ProfileFamilyMember> {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileFamilyMemberQueryService.class);

    private final ProfileFamilyMemberRepository profileFamilyMemberRepository;
    private final ProfileFamilyMemberMapper profileFamilyMemberMapper;

    public ProfileFamilyMemberQueryService(
        ProfileFamilyMemberRepository profileFamilyMemberRepository,
        ProfileFamilyMemberMapper profileFamilyMemberMapper
    ) {
        this.profileFamilyMemberRepository = profileFamilyMemberRepository;
        this.profileFamilyMemberMapper = profileFamilyMemberMapper;
    }

    public Page<ProfileFamilyMemberDTO> findByCriteria(ProfileFamilyMemberCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProfileFamilyMember> specification = createSpecification(criteria);
        return profileFamilyMemberRepository.findAll(specification, page).map(profileFamilyMemberMapper::toDto);
    }

    public long countByCriteria(ProfileFamilyMemberCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProfileFamilyMember> specification = createSpecification(criteria);
        return profileFamilyMemberRepository.count(specification);
    }

    protected Specification<ProfileFamilyMember> createSpecification(ProfileFamilyMemberCriteria criteria) {
        Specification<ProfileFamilyMember> specification = Specification.where(null);
        if (criteria != null) {
            specification =
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                    criteria.getId() != null ? buildRangeSpecification(criteria.getId(), ProfileFamilyMember_.id) : null,
                    criteria.getTenantId() != null ? buildStringSpecification(criteria.getTenantId(), ProfileFamilyMember_.tenantId) : null,
                    criteria.getDisplayName() != null
                        ? buildStringSpecification(criteria.getDisplayName(), ProfileFamilyMember_.displayName)
                        : null,
                    criteria.getRelationship() != null
                        ? buildStringSpecification(criteria.getRelationship(), ProfileFamilyMember_.relationship)
                        : null,
                    criteria.getRoleTitle() != null
                        ? buildStringSpecification(criteria.getRoleTitle(), ProfileFamilyMember_.roleTitle)
                        : null,
                    criteria.getDisplayOrder() != null
                        ? buildRangeSpecification(criteria.getDisplayOrder(), ProfileFamilyMember_.displayOrder)
                        : null,
                    criteria.getCreatedAt() != null
                        ? buildRangeSpecification(criteria.getCreatedAt(), ProfileFamilyMember_.createdAt)
                        : null,
                    criteria.getUpdatedAt() != null
                        ? buildRangeSpecification(criteria.getUpdatedAt(), ProfileFamilyMember_.updatedAt)
                        : null,
                    null
                );
        }
        return specification;
    }
}
