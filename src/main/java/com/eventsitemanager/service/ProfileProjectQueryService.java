package com.eventsitemanager.service;

import com.eventsitemanager.domain.*;
import com.eventsitemanager.domain.ProfileProject;
import com.eventsitemanager.repository.ProfileProjectRepository;
import com.eventsitemanager.service.criteria.ProfileProjectCriteria;
import com.eventsitemanager.service.dto.ProfileProjectDTO;
import com.eventsitemanager.service.mapper.ProfileProjectMapper;
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
public class ProfileProjectQueryService extends QueryService<ProfileProject> {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileProjectQueryService.class);

    private final ProfileProjectRepository profileProjectRepository;
    private final ProfileProjectMapper profileProjectMapper;

    public ProfileProjectQueryService(ProfileProjectRepository profileProjectRepository, ProfileProjectMapper profileProjectMapper) {
        this.profileProjectRepository = profileProjectRepository;
        this.profileProjectMapper = profileProjectMapper;
    }

    public Page<ProfileProjectDTO> findByCriteria(ProfileProjectCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProfileProject> specification = createSpecification(criteria);
        return profileProjectRepository.findAll(specification, page).map(profileProjectMapper::toDto);
    }

    public long countByCriteria(ProfileProjectCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProfileProject> specification = createSpecification(criteria);
        return profileProjectRepository.count(specification);
    }

    protected Specification<ProfileProject> createSpecification(ProfileProjectCriteria criteria) {
        Specification<ProfileProject> specification = Specification.where(null);
        if (criteria != null) {
            specification =
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                    criteria.getId() != null ? buildRangeSpecification(criteria.getId(), ProfileProject_.id) : null,
                    criteria.getTenantId() != null ? buildStringSpecification(criteria.getTenantId(), ProfileProject_.tenantId) : null,
                    criteria.getTitle() != null ? buildStringSpecification(criteria.getTitle(), ProfileProject_.title) : null,
                    criteria.getSlug() != null ? buildStringSpecification(criteria.getSlug(), ProfileProject_.slug) : null,
                    criteria.getDisplayOrder() != null
                        ? buildRangeSpecification(criteria.getDisplayOrder(), ProfileProject_.displayOrder)
                        : null,
                    criteria.getIsFeatured() != null ? buildSpecification(criteria.getIsFeatured(), ProfileProject_.isFeatured) : null,
                    criteria.getCreatedAt() != null ? buildRangeSpecification(criteria.getCreatedAt(), ProfileProject_.createdAt) : null,
                    criteria.getUpdatedAt() != null ? buildRangeSpecification(criteria.getUpdatedAt(), ProfileProject_.updatedAt) : null,
                    null
                );
        }
        return specification;
    }
}
