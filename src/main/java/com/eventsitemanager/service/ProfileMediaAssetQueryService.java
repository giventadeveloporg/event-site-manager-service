package com.eventsitemanager.service;

import com.eventsitemanager.domain.*;
import com.eventsitemanager.domain.ProfileMediaAsset;
import com.eventsitemanager.repository.ProfileMediaAssetRepository;
import com.eventsitemanager.service.criteria.ProfileMediaAssetCriteria;
import com.eventsitemanager.service.dto.ProfileMediaAssetDTO;
import com.eventsitemanager.service.mapper.ProfileMediaAssetMapper;
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
public class ProfileMediaAssetQueryService extends QueryService<ProfileMediaAsset> {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileMediaAssetQueryService.class);

    private final ProfileMediaAssetRepository profileMediaAssetRepository;
    private final ProfileMediaAssetMapper profileMediaAssetMapper;

    public ProfileMediaAssetQueryService(
        ProfileMediaAssetRepository profileMediaAssetRepository,
        ProfileMediaAssetMapper profileMediaAssetMapper
    ) {
        this.profileMediaAssetRepository = profileMediaAssetRepository;
        this.profileMediaAssetMapper = profileMediaAssetMapper;
    }

    public Page<ProfileMediaAssetDTO> findByCriteria(ProfileMediaAssetCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProfileMediaAsset> specification = createSpecification(criteria);
        return profileMediaAssetRepository.findAll(specification, page).map(profileMediaAssetMapper::toDto);
    }

    public long countByCriteria(ProfileMediaAssetCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProfileMediaAsset> specification = createSpecification(criteria);
        return profileMediaAssetRepository.count(specification);
    }

    protected Specification<ProfileMediaAsset> createSpecification(ProfileMediaAssetCriteria criteria) {
        Specification<ProfileMediaAsset> specification = Specification.where(null);
        if (criteria != null) {
            specification =
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                    criteria.getId() != null ? buildRangeSpecification(criteria.getId(), ProfileMediaAsset_.id) : null,
                    criteria.getTenantId() != null ? buildStringSpecification(criteria.getTenantId(), ProfileMediaAsset_.tenantId) : null,
                    criteria.getTitle() != null ? buildStringSpecification(criteria.getTitle(), ProfileMediaAsset_.title) : null,
                    criteria.getFileType() != null ? buildStringSpecification(criteria.getFileType(), ProfileMediaAsset_.fileType) : null,
                    criteria.getDisplayOrder() != null
                        ? buildRangeSpecification(criteria.getDisplayOrder(), ProfileMediaAsset_.displayOrder)
                        : null,
                    criteria.getIsDownloadable() != null
                        ? buildSpecification(criteria.getIsDownloadable(), ProfileMediaAsset_.isDownloadable)
                        : null,
                    criteria.getRequiresEmail() != null
                        ? buildSpecification(criteria.getRequiresEmail(), ProfileMediaAsset_.requiresEmail)
                        : null,
                    criteria.getCreatedAt() != null ? buildRangeSpecification(criteria.getCreatedAt(), ProfileMediaAsset_.createdAt) : null,
                    criteria.getUpdatedAt() != null ? buildRangeSpecification(criteria.getUpdatedAt(), ProfileMediaAsset_.updatedAt) : null,
                    null
                );
        }
        return specification;
    }
}
