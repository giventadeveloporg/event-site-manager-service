package com.eventsitemanager.service;

import com.eventsitemanager.domain.*;
import com.eventsitemanager.domain.ProfileWriting;
import com.eventsitemanager.repository.ProfileWritingRepository;
import com.eventsitemanager.service.criteria.ProfileWritingCriteria;
import com.eventsitemanager.service.dto.ProfileWritingDTO;
import com.eventsitemanager.service.mapper.ProfileWritingMapper;
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
public class ProfileWritingQueryService extends QueryService<ProfileWriting> {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileWritingQueryService.class);

    private final ProfileWritingRepository profileWritingRepository;
    private final ProfileWritingMapper profileWritingMapper;

    public ProfileWritingQueryService(ProfileWritingRepository profileWritingRepository, ProfileWritingMapper profileWritingMapper) {
        this.profileWritingRepository = profileWritingRepository;
        this.profileWritingMapper = profileWritingMapper;
    }

    public Page<ProfileWritingDTO> findByCriteria(ProfileWritingCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProfileWriting> specification = createSpecification(criteria);
        return profileWritingRepository.findAll(specification, page).map(profileWritingMapper::toDto);
    }

    public long countByCriteria(ProfileWritingCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProfileWriting> specification = createSpecification(criteria);
        return profileWritingRepository.count(specification);
    }

    protected Specification<ProfileWriting> createSpecification(ProfileWritingCriteria criteria) {
        Specification<ProfileWriting> specification = Specification.where(null);
        if (criteria != null) {
            specification =
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                    criteria.getId() != null ? buildRangeSpecification(criteria.getId(), ProfileWriting_.id) : null,
                    criteria.getTenantId() != null ? buildStringSpecification(criteria.getTenantId(), ProfileWriting_.tenantId) : null,
                    criteria.getTitle() != null ? buildStringSpecification(criteria.getTitle(), ProfileWriting_.title) : null,
                    criteria.getSlug() != null ? buildStringSpecification(criteria.getSlug(), ProfileWriting_.slug) : null,
                    criteria.getWritingType() != null ? buildSpecification(criteria.getWritingType(), ProfileWriting_.writingType) : null,
                    criteria.getStatus() != null ? buildSpecification(criteria.getStatus(), ProfileWriting_.status) : null,
                    criteria.getDisplayOrder() != null
                        ? buildRangeSpecification(criteria.getDisplayOrder(), ProfileWriting_.displayOrder)
                        : null,
                    criteria.getPublishedAt() != null
                        ? buildRangeSpecification(criteria.getPublishedAt(), ProfileWriting_.publishedAt)
                        : null,
                    criteria.getCreatedAt() != null ? buildRangeSpecification(criteria.getCreatedAt(), ProfileWriting_.createdAt) : null,
                    criteria.getUpdatedAt() != null ? buildRangeSpecification(criteria.getUpdatedAt(), ProfileWriting_.updatedAt) : null,
                    null
                );
        }
        return specification;
    }
}
