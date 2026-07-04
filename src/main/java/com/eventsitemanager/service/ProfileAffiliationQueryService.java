package com.eventsitemanager.service;

import com.eventsitemanager.domain.*;
import com.eventsitemanager.domain.ProfileAffiliation;
import com.eventsitemanager.repository.ProfileAffiliationRepository;
import com.eventsitemanager.service.criteria.ProfileAffiliationCriteria;
import com.eventsitemanager.service.dto.ProfileAffiliationDTO;
import com.eventsitemanager.service.mapper.ProfileAffiliationMapper;
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
public class ProfileAffiliationQueryService extends QueryService<ProfileAffiliation> {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileAffiliationQueryService.class);

    private final ProfileAffiliationRepository profileAffiliationRepository;
    private final ProfileAffiliationMapper profileAffiliationMapper;

    public ProfileAffiliationQueryService(
        ProfileAffiliationRepository profileAffiliationRepository,
        ProfileAffiliationMapper profileAffiliationMapper
    ) {
        this.profileAffiliationRepository = profileAffiliationRepository;
        this.profileAffiliationMapper = profileAffiliationMapper;
    }

    public Page<ProfileAffiliationDTO> findByCriteria(ProfileAffiliationCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProfileAffiliation> specification = createSpecification(criteria);
        return profileAffiliationRepository.findAll(specification, page).map(profileAffiliationMapper::toDto);
    }

    public long countByCriteria(ProfileAffiliationCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProfileAffiliation> specification = createSpecification(criteria);
        return profileAffiliationRepository.count(specification);
    }

    protected Specification<ProfileAffiliation> createSpecification(ProfileAffiliationCriteria criteria) {
        Specification<ProfileAffiliation> specification = Specification.where(null);
        if (criteria != null) {
            specification =
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                    criteria.getId() != null ? buildRangeSpecification(criteria.getId(), ProfileAffiliation_.id) : null,
                    criteria.getTenantId() != null ? buildStringSpecification(criteria.getTenantId(), ProfileAffiliation_.tenantId) : null,
                    criteria.getOrganizationName() != null
                        ? buildStringSpecification(criteria.getOrganizationName(), ProfileAffiliation_.organizationName)
                        : null,
                    criteria.getRole() != null ? buildStringSpecification(criteria.getRole(), ProfileAffiliation_.role) : null,
                    criteria.getDisplayOrder() != null
                        ? buildRangeSpecification(criteria.getDisplayOrder(), ProfileAffiliation_.displayOrder)
                        : null,
                    criteria.getStartDate() != null
                        ? buildRangeSpecification(criteria.getStartDate(), ProfileAffiliation_.startDate)
                        : null,
                    criteria.getEndDate() != null ? buildRangeSpecification(criteria.getEndDate(), ProfileAffiliation_.endDate) : null,
                    criteria.getCreatedAt() != null
                        ? buildRangeSpecification(criteria.getCreatedAt(), ProfileAffiliation_.createdAt)
                        : null,
                    criteria.getUpdatedAt() != null
                        ? buildRangeSpecification(criteria.getUpdatedAt(), ProfileAffiliation_.updatedAt)
                        : null,
                    null
                );
        }
        return specification;
    }
}
