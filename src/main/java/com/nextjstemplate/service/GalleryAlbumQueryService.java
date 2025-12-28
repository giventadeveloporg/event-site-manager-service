package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.GalleryAlbum;
import com.nextjstemplate.repository.GalleryAlbumRepository;
import com.nextjstemplate.service.criteria.GalleryAlbumCriteria;
import com.nextjstemplate.service.dto.GalleryAlbumDTO;
import com.nextjstemplate.service.mapper.GalleryAlbumMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link GalleryAlbum} entities in the database.
 * The main input is a {@link GalleryAlbumCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GalleryAlbumDTO} or a {@link Page} of {@link GalleryAlbumDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GalleryAlbumQueryService extends QueryService<GalleryAlbum> {

    private final Logger log = LoggerFactory.getLogger(GalleryAlbumQueryService.class);

    private final GalleryAlbumRepository galleryAlbumRepository;

    private final GalleryAlbumMapper galleryAlbumMapper;

    public GalleryAlbumQueryService(GalleryAlbumRepository galleryAlbumRepository, GalleryAlbumMapper galleryAlbumMapper) {
        this.galleryAlbumRepository = galleryAlbumRepository;
        this.galleryAlbumMapper = galleryAlbumMapper;
    }

    /**
     * Return a {@link List} of {@link GalleryAlbumDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GalleryAlbumDTO> findByCriteria(GalleryAlbumCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GalleryAlbum> specification = createSpecification(criteria);
        return galleryAlbumMapper.toDto(galleryAlbumRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link GalleryAlbumDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GalleryAlbumDTO> findByCriteria(GalleryAlbumCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GalleryAlbum> specification = createSpecification(criteria);
        return galleryAlbumRepository.findAll(specification, page).map(galleryAlbumMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GalleryAlbumCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GalleryAlbum> specification = createSpecification(criteria);
        return galleryAlbumRepository.count(specification);
    }

    /**
     * Function to convert {@link GalleryAlbumCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GalleryAlbum> createSpecification(GalleryAlbumCriteria criteria) {
        Specification<GalleryAlbum> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), GalleryAlbum_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), GalleryAlbum_.tenantId));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), GalleryAlbum_.title));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), GalleryAlbum_.description));
            }
            if (criteria.getCoverImageUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCoverImageUrl(), GalleryAlbum_.coverImageUrl));
            }
            if (criteria.getIsPublic() != null) {
                specification = specification.and(buildSpecification(criteria.getIsPublic(), GalleryAlbum_.isPublic));
            }
            if (criteria.getDisplayOrder() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDisplayOrder(), GalleryAlbum_.displayOrder));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), GalleryAlbum_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), GalleryAlbum_.updatedAt));
            }
            if (criteria.getCreatedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCreatedById(),
                            root -> root.join(GalleryAlbum_.createdBy, JoinType.LEFT).get(UserProfile_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
