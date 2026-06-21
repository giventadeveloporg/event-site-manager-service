package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.GalleryCategory;
import com.nextjstemplate.repository.GalleryCategoryRepository;
import com.nextjstemplate.service.criteria.GalleryCategoryCriteria;
import com.nextjstemplate.service.dto.GalleryCategoryDTO;
import com.nextjstemplate.service.mapper.GalleryCategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link GalleryCategory} entities in the database.
 */
@Service
@Transactional(readOnly = true)
public class GalleryCategoryQueryService extends QueryService<GalleryCategory> {

    private final Logger log = LoggerFactory.getLogger(GalleryCategoryQueryService.class);

    private final GalleryCategoryRepository galleryCategoryRepository;

    private final GalleryCategoryMapper galleryCategoryMapper;

    public GalleryCategoryQueryService(GalleryCategoryRepository galleryCategoryRepository, GalleryCategoryMapper galleryCategoryMapper) {
        this.galleryCategoryRepository = galleryCategoryRepository;
        this.galleryCategoryMapper = galleryCategoryMapper;
    }

    @Transactional(readOnly = true)
    public Page<GalleryCategoryDTO> findByCriteria(GalleryCategoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GalleryCategory> specification = createSpecification(criteria);
        return galleryCategoryRepository.findAll(specification, page).map(galleryCategoryMapper::toDto);
    }

    @Transactional(readOnly = true)
    public long countByCriteria(GalleryCategoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GalleryCategory> specification = createSpecification(criteria);
        return galleryCategoryRepository.count(specification);
    }

    protected Specification<GalleryCategory> createSpecification(GalleryCategoryCriteria criteria) {
        Specification<GalleryCategory> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), GalleryCategory_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), GalleryCategory_.tenantId));
            }
            if (criteria.getSlug() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSlug(), GalleryCategory_.slug));
            }
            if (criteria.getDisplayName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDisplayName(), GalleryCategory_.displayName));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), GalleryCategory_.isActive));
            }
            if (criteria.getSortOrder() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSortOrder(), GalleryCategory_.sortOrder));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), GalleryCategory_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), GalleryCategory_.updatedAt));
            }
        }
        return specification;
    }
}
