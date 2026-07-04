package com.eventsitemanager.service;

import com.eventsitemanager.domain.*;
import com.eventsitemanager.domain.PublicProfile;
import com.eventsitemanager.repository.PublicProfileRepository;
import com.eventsitemanager.service.criteria.PublicProfileCriteria;
import com.eventsitemanager.service.dto.PublicProfileDTO;
import com.eventsitemanager.service.mapper.PublicProfileMapper;
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
public class PublicProfileQueryService extends QueryService<PublicProfile> {

    private static final Logger LOG = LoggerFactory.getLogger(PublicProfileQueryService.class);

    private final PublicProfileRepository publicProfileRepository;
    private final PublicProfileMapper publicProfileMapper;

    public PublicProfileQueryService(PublicProfileRepository publicProfileRepository, PublicProfileMapper publicProfileMapper) {
        this.publicProfileRepository = publicProfileRepository;
        this.publicProfileMapper = publicProfileMapper;
    }

    public Page<PublicProfileDTO> findByCriteria(PublicProfileCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PublicProfile> specification = createSpecification(criteria);
        return publicProfileRepository.findAll(specification, page).map(publicProfileMapper::toDto);
    }

    public long countByCriteria(PublicProfileCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PublicProfile> specification = createSpecification(criteria);
        return publicProfileRepository.count(specification);
    }

    protected Specification<PublicProfile> createSpecification(PublicProfileCriteria criteria) {
        Specification<PublicProfile> specification = Specification.where(null);
        if (criteria != null) {
            specification =
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                    criteria.getId() != null ? buildRangeSpecification(criteria.getId(), PublicProfile_.id) : null,
                    criteria.getTenantId() != null ? buildStringSpecification(criteria.getTenantId(), PublicProfile_.tenantId) : null,
                    criteria.getDisplayName() != null
                        ? buildStringSpecification(criteria.getDisplayName(), PublicProfile_.displayName)
                        : null,
                    criteria.getPublicSlug() != null ? buildStringSpecification(criteria.getPublicSlug(), PublicProfile_.publicSlug) : null,
                    criteria.getIsPublished() != null ? buildSpecification(criteria.getIsPublished(), PublicProfile_.isPublished) : null,
                    criteria.getOwnerUserProfileId() != null
                        ? buildRangeSpecification(criteria.getOwnerUserProfileId(), PublicProfile_.ownerUserProfileId)
                        : null,
                    criteria.getCreatedAt() != null ? buildRangeSpecification(criteria.getCreatedAt(), PublicProfile_.createdAt) : null,
                    criteria.getUpdatedAt() != null ? buildRangeSpecification(criteria.getUpdatedAt(), PublicProfile_.updatedAt) : null
                );
        }
        return specification;
    }
}
