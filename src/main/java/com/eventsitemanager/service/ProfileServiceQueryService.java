package com.eventsitemanager.service;

import com.eventsitemanager.domain.ProfileService;
import com.eventsitemanager.domain.ProfileService_;
import com.eventsitemanager.repository.ProfileServiceRepository;
import com.eventsitemanager.service.criteria.ProfileServiceCriteria;
import com.eventsitemanager.service.dto.ProfileServiceDTO;
import com.eventsitemanager.service.mapper.ProfileServiceMapper;
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
public class ProfileServiceQueryService extends QueryService<ProfileService> {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileServiceQueryService.class);

    private final ProfileServiceRepository profileServiceRepository;
    private final ProfileServiceMapper profileServiceMapper;

    public ProfileServiceQueryService(ProfileServiceRepository profileServiceRepository, ProfileServiceMapper profileServiceMapper) {
        this.profileServiceRepository = profileServiceRepository;
        this.profileServiceMapper = profileServiceMapper;
    }

    public Page<ProfileServiceDTO> findByCriteria(ProfileServiceCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProfileService> specification = createSpecification(criteria);
        return profileServiceRepository.findAll(specification, page).map(profileServiceMapper::toDto);
    }

    public long countByCriteria(ProfileServiceCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProfileService> specification = createSpecification(criteria);
        return profileServiceRepository.count(specification);
    }

    protected Specification<ProfileService> createSpecification(ProfileServiceCriteria criteria) {
        Specification<ProfileService> specification = Specification.where(null);
        if (criteria != null) {
            specification =
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                    criteria.getId() != null ? buildRangeSpecification(criteria.getId(), ProfileService_.id) : null,
                    criteria.getTenantId() != null ? buildStringSpecification(criteria.getTenantId(), ProfileService_.tenantId) : null,
                    criteria.getTitle() != null ? buildStringSpecification(criteria.getTitle(), ProfileService_.title) : null,
                    criteria.getSlug() != null ? buildStringSpecification(criteria.getSlug(), ProfileService_.slug) : null,
                    criteria.getCategory() != null ? buildStringSpecification(criteria.getCategory(), ProfileService_.category) : null,
                    criteria.getDisplayOrder() != null
                        ? buildRangeSpecification(criteria.getDisplayOrder(), ProfileService_.displayOrder)
                        : null,
                    criteria.getIsFeatured() != null ? buildSpecification(criteria.getIsFeatured(), ProfileService_.isFeatured) : null,
                    criteria.getIsActive() != null ? buildSpecification(criteria.getIsActive(), ProfileService_.isActive) : null,
                    criteria.getCreatedAt() != null ? buildRangeSpecification(criteria.getCreatedAt(), ProfileService_.createdAt) : null,
                    criteria.getUpdatedAt() != null ? buildRangeSpecification(criteria.getUpdatedAt(), ProfileService_.updatedAt) : null,
                    null
                );
        }
        return specification;
    }
}
