package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.EventMedia;
import com.nextjstemplate.repository.EventMediaRepository;
import com.nextjstemplate.service.criteria.EventMediaCriteria;
import com.nextjstemplate.service.dto.EventMediaDTO;
import com.nextjstemplate.service.mapper.EventMediaMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link EventMedia} entities in the
 * database.
 * The main input is a {@link EventMediaCriteria} which gets converted to
 * {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EventMediaDTO} or a {@link Page} of
 * {@link EventMediaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventMediaQueryService extends QueryService<EventMedia> {

    private final Logger log = LoggerFactory.getLogger(EventMediaQueryService.class);

    private final EventMediaRepository eventMediaRepository;

    private final EventMediaMapper eventMediaMapper;

    private final EventMediaService eventMediaService;

    public EventMediaQueryService(
        EventMediaRepository eventMediaRepository,
        EventMediaMapper eventMediaMapper,
        EventMediaService eventMediaService
    ) {
        this.eventMediaRepository = eventMediaRepository;
        this.eventMediaMapper = eventMediaMapper;
        this.eventMediaService = eventMediaService;
    }

    /**
     * Return a {@link List} of {@link EventMediaDTO} which matches the criteria
     * from the database.
     *
     * @param criteria The object which holds all the filters, which the entities
     *                 should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EventMediaDTO> findByCriteria(EventMediaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EventMedia> specification = createSpecification(criteria);
        return eventMediaMapper.toDto(eventMediaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EventMediaDTO} which matches the criteria
     * from the database.
     *
     * @param criteria The object which holds all the filters, which the entities
     *                 should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EventMediaDTO> findByCriteria(EventMediaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EventMedia> specification = createSpecification(criteria);

        Page<EventMedia> eventMediaPage = eventMediaRepository.findAll(specification, page);

        if (eventMediaPage.hasContent()) {
            return eventMediaPage.map(eventMediaMapper::toDto);
        } else {
            return Page.empty(page);
        }
    }

    /**
     * Return a {@link Page} of {@link EventMediaDTO} which matches the criteria
     * from the database.
     * Safe version that avoids LOB fields to prevent stream access errors.
     *
     * @param criteria The object which holds all the filters, which the entities
     *                 should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EventMediaDTO> findByCriteriaSafe(EventMediaCriteria criteria, Pageable page) {
        log.debug("find by criteria safely (without LOB fields) : {}, page: {}", criteria, page);
        // Use the service method that handles raw Object[] results
        List<EventMediaDTO> dtos = eventMediaService.findAllWithoutLobFields();

        // Create a simple page implementation
        return new org.springframework.data.domain.PageImpl<>(dtos, page, dtos.size());
    }

    /**
     * Return a {@link List} of {@link EventMediaDTO} which matches the criteria
     * from the database.
     * Safe version that avoids LOB fields to prevent stream access errors.
     *
     * @param criteria The object which holds all the filters, which the entities
     *                 should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EventMediaDTO> findByCriteriaSafe(EventMediaCriteria criteria) {
        log.debug("find by criteria safely (without LOB fields) : {}", criteria);
        // Use the service method that handles raw Object[] results
        return eventMediaService.findAllWithoutLobFields();
    }

    /**
     * Return the number of matching entities in the database.
     *
     * @param criteria The object which holds all the filters, which the entities
     *                 should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventMediaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EventMedia> specification = createSpecification(criteria);
        return eventMediaRepository.count(specification);
    }

    /**
     * Function to convert {@link EventMediaCriteria} to a {@link Specification}
     *
     * @param criteria The object which holds all the filters, which the entities
     *                 should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EventMedia> createSpecification(EventMediaCriteria criteria) {
        Specification<EventMedia> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EventMedia_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), EventMedia_.tenantId));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), EventMedia_.title));
            }
            if (criteria.getEventMediaType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEventMediaType(), EventMedia_.eventMediaType));
            }
            if (criteria.getStorageType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStorageType(), EventMedia_.storageType));
            }
            if (criteria.getFileUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileUrl(), EventMedia_.fileUrl));
            }
            if (criteria.getContentType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContentType(), EventMedia_.contentType));
            }
            if (criteria.getFileSize() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFileSize(), EventMedia_.fileSize));
            }
            if (criteria.getIsPublic() != null) {
                specification = specification.and(buildSpecification(criteria.getIsPublic(), EventMedia_.isPublic));
            }
            if (criteria.getEventFlyer() != null) {
                specification = specification.and(buildSpecification(criteria.getEventFlyer(), EventMedia_.eventFlyer));
            }
            if (criteria.getIsEventManagementOfficialDocument() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getIsEventManagementOfficialDocument(), EventMedia_.isEventManagementOfficialDocument)
                    );
            }
            if (criteria.getPreSignedUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPreSignedUrl(), EventMedia_.preSignedUrl));
            }
            if (criteria.getPreSignedUrlExpiresAt() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getPreSignedUrlExpiresAt(), EventMedia_.preSignedUrlExpiresAt));
            }
            if (criteria.getAltText() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAltText(), EventMedia_.altText));
            }
            if (criteria.getDisplayOrder() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDisplayOrder(), EventMedia_.displayOrder));
            }
            if (criteria.getDownloadCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDownloadCount(), EventMedia_.downloadCount));
            }
            if (criteria.getIsFeaturedVideo() != null) {
                specification = specification.and(buildSpecification(criteria.getIsFeaturedVideo(), EventMedia_.isFeaturedVideo));
            }
            if (criteria.getFeaturedVideoUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFeaturedVideoUrl(), EventMedia_.featuredVideoUrl));
            }
            if (criteria.getIsHeroImage() != null) {
                specification = specification.and(buildSpecification(criteria.getIsHeroImage(), EventMedia_.isHeroImage));
            }
            if (criteria.getIsActiveHeroImage() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActiveHeroImage(), EventMedia_.isActiveHeroImage));
            }
            if (criteria.getIsHomePageHeroImage() != null) {
                specification = specification.and(buildSpecification(criteria.getIsHomePageHeroImage(), EventMedia_.isHomePageHeroImage));
            }
            if (criteria.getIsFeaturedEventImage() != null) {
                specification = specification.and(buildSpecification(criteria.getIsFeaturedEventImage(), EventMedia_.isFeaturedEventImage));
            }
            if (criteria.getIsLiveEventImage() != null) {
                specification = specification.and(buildSpecification(criteria.getIsLiveEventImage(), EventMedia_.isLiveEventImage));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), EventMedia_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), EventMedia_.updatedAt));
            }

            if (criteria.getEventId() != null) {
                specification = specification.and(buildSpecification(criteria.getEventId(), EventMedia_.eventId));
            }
            if (criteria.getUploadedById() != null) {
                specification = specification.and(buildSpecification(criteria.getUploadedById(), EventMedia_.uploadedById));
            }
        }
        return specification;
    }
}
