package com.eventsitemanager.service;

import com.eventsitemanager.domain.GalleryYoutubeVideo;
import com.eventsitemanager.domain.GalleryYoutubeVideo_;
import com.eventsitemanager.repository.GalleryYoutubeVideoRepository;
import com.eventsitemanager.service.criteria.GalleryYoutubeVideoCriteria;
import com.eventsitemanager.service.dto.GalleryYoutubeVideoDTO;
import com.eventsitemanager.service.mapper.GalleryYoutubeVideoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link GalleryYoutubeVideo} entities.
 */
@Service
@Transactional(readOnly = true)
public class GalleryYoutubeVideoQueryService extends QueryService<GalleryYoutubeVideo> {

    private static final Logger LOG = LoggerFactory.getLogger(GalleryYoutubeVideoQueryService.class);

    private final GalleryYoutubeVideoRepository galleryYoutubeVideoRepository;

    private final GalleryYoutubeVideoMapper galleryYoutubeVideoMapper;

    public GalleryYoutubeVideoQueryService(
        GalleryYoutubeVideoRepository galleryYoutubeVideoRepository,
        GalleryYoutubeVideoMapper galleryYoutubeVideoMapper
    ) {
        this.galleryYoutubeVideoRepository = galleryYoutubeVideoRepository;
        this.galleryYoutubeVideoMapper = galleryYoutubeVideoMapper;
    }

    @Transactional(readOnly = true)
    public Page<GalleryYoutubeVideoDTO> findByCriteria(GalleryYoutubeVideoCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GalleryYoutubeVideo> specification = createSpecification(criteria);
        return galleryYoutubeVideoRepository.findAll(specification, page).map(galleryYoutubeVideoMapper::toDto);
    }

    @Transactional(readOnly = true)
    public long countByCriteria(GalleryYoutubeVideoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<GalleryYoutubeVideo> specification = createSpecification(criteria);
        return galleryYoutubeVideoRepository.count(specification);
    }

    protected Specification<GalleryYoutubeVideo> createSpecification(GalleryYoutubeVideoCriteria criteria) {
        Specification<GalleryYoutubeVideo> specification = Specification.where(null);
        if (criteria != null) {
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                criteria.getId() != null ? buildRangeSpecification(criteria.getId(), GalleryYoutubeVideo_.id) : null,
                criteria.getTenantId() != null ? buildStringSpecification(criteria.getTenantId(), GalleryYoutubeVideo_.tenantId) : null,
                criteria.getTitle() != null ? buildStringSpecification(criteria.getTitle(), GalleryYoutubeVideo_.title) : null,
                criteria.getDisplayOrder() != null
                    ? buildRangeSpecification(criteria.getDisplayOrder(), GalleryYoutubeVideo_.displayOrder)
                    : null,
                criteria.getIsActive() != null ? buildSpecification(criteria.getIsActive(), GalleryYoutubeVideo_.isActive) : null
            );
        }
        return specification;
    }
}
