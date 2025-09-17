package com.nextjstemplate.service;

import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.domain.EventDetails_;
import com.nextjstemplate.domain.EventFeaturedPerformers;
import com.nextjstemplate.domain.EventFeaturedPerformers_;
import com.nextjstemplate.repository.EventFeaturedPerformersRepository;
import com.nextjstemplate.service.criteria.EventFeaturedPerformersCriteria;
import com.nextjstemplate.service.dto.EventFeaturedPerformersDTO;
import com.nextjstemplate.service.mapper.EventFeaturedPerformersMapper;
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
 * Service for executing complex queries for {@link EventFeaturedPerformers}
 * entities in the database.
 */
@Service
@Transactional(readOnly = true)
public class EventFeaturedPerformersQueryService extends QueryService<EventFeaturedPerformers> {

    private final Logger log = LoggerFactory.getLogger(EventFeaturedPerformersQueryService.class);

    private final EventFeaturedPerformersRepository eventFeaturedPerformersRepository;
    private final EventFeaturedPerformersMapper eventFeaturedPerformersMapper;

    public EventFeaturedPerformersQueryService(
            EventFeaturedPerformersRepository eventFeaturedPerformersRepository,
            EventFeaturedPerformersMapper eventFeaturedPerformersMapper) {
        this.eventFeaturedPerformersRepository = eventFeaturedPerformersRepository;
        this.eventFeaturedPerformersMapper = eventFeaturedPerformersMapper;
    }

    @Transactional(readOnly = true)
    public Page<EventFeaturedPerformersDTO> findByCriteria(EventFeaturedPerformersCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EventFeaturedPerformers> specification = createSpecification(criteria);
        return eventFeaturedPerformersRepository.findAll(specification, page).map(eventFeaturedPerformersMapper::toDto);
    }

    @Transactional(readOnly = true)
    public long countByCriteria(EventFeaturedPerformersCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EventFeaturedPerformers> specification = createSpecification(criteria);
        return eventFeaturedPerformersRepository.count(specification);
    }

    protected Specification<EventFeaturedPerformers> createSpecification(EventFeaturedPerformersCriteria criteria) {
        Specification<EventFeaturedPerformers> specification = Specification.where(null);
        if (criteria != null) {
            specification = Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                    criteria.getId() != null ? buildRangeSpecification(criteria.getId(), EventFeaturedPerformers_.id)
                            : null,
                    criteria.getTenantId() != null
                            ? buildStringSpecification(criteria.getTenantId(), EventFeaturedPerformers_.tenantId)
                            : null,
                    criteria.getName() != null
                            ? buildStringSpecification(criteria.getName(), EventFeaturedPerformers_.name)
                            : null,
                    criteria.getStageName() != null
                            ? buildStringSpecification(criteria.getStageName(), EventFeaturedPerformers_.stageName)
                            : null,
                    criteria.getRole() != null
                            ? buildStringSpecification(criteria.getRole(), EventFeaturedPerformers_.role)
                            : null,
                    criteria.getBio() != null
                            ? buildStringSpecification(criteria.getBio(), EventFeaturedPerformers_.bio)
                            : null,
                    criteria.getNationality() != null
                            ? buildStringSpecification(criteria.getNationality(), EventFeaturedPerformers_.nationality)
                            : null,
                    criteria.getDateOfBirth() != null
                            ? buildRangeSpecification(criteria.getDateOfBirth(), EventFeaturedPerformers_.dateOfBirth)
                            : null,
                    criteria.getEmail() != null
                            ? buildStringSpecification(criteria.getEmail(), EventFeaturedPerformers_.email)
                            : null,
                    criteria.getPhone() != null
                            ? buildStringSpecification(criteria.getPhone(), EventFeaturedPerformers_.phone)
                            : null,
                    criteria.getWebsiteUrl() != null
                            ? buildStringSpecification(criteria.getWebsiteUrl(), EventFeaturedPerformers_.websiteUrl)
                            : null,
                    criteria.getPortraitImageUrl() != null ? buildStringSpecification(criteria.getPortraitImageUrl(),
                            EventFeaturedPerformers_.portraitImageUrl) : null,
                    criteria.getPerformanceImageUrl() != null
                            ? buildStringSpecification(criteria.getPerformanceImageUrl(),
                                    EventFeaturedPerformers_.performanceImageUrl)
                            : null,
                    criteria.getGalleryImageUrls() != null ? buildStringSpecification(criteria.getGalleryImageUrls(),
                            EventFeaturedPerformers_.galleryImageUrls) : null,
                    criteria.getPerformanceDurationMinutes() != null
                            ? buildRangeSpecification(criteria.getPerformanceDurationMinutes(),
                                    EventFeaturedPerformers_.performanceDurationMinutes)
                            : null,
                    criteria.getPerformanceOrder() != null
                            ? buildRangeSpecification(criteria.getPerformanceOrder(),
                                    EventFeaturedPerformers_.performanceOrder)
                            : null,
                    criteria.getIsHeadliner() != null
                            ? buildSpecification(criteria.getIsHeadliner(), EventFeaturedPerformers_.isHeadliner)
                            : null,
                    criteria.getFacebookUrl() != null
                            ? buildStringSpecification(criteria.getFacebookUrl(), EventFeaturedPerformers_.facebookUrl)
                            : null,
                    criteria.getTwitterUrl() != null
                            ? buildStringSpecification(criteria.getTwitterUrl(), EventFeaturedPerformers_.twitterUrl)
                            : null,
                    criteria.getInstagramUrl() != null
                            ? buildStringSpecification(criteria.getInstagramUrl(),
                                    EventFeaturedPerformers_.instagramUrl)
                            : null,
                    criteria.getYoutubeUrl() != null
                            ? buildStringSpecification(criteria.getYoutubeUrl(), EventFeaturedPerformers_.youtubeUrl)
                            : null,
                    criteria.getLinkedinUrl() != null
                            ? buildStringSpecification(criteria.getLinkedinUrl(), EventFeaturedPerformers_.linkedinUrl)
                            : null,
                    criteria.getTiktokUrl() != null
                            ? buildStringSpecification(criteria.getTiktokUrl(), EventFeaturedPerformers_.tiktokUrl)
                            : null,
                    criteria.getIsActive() != null
                            ? buildSpecification(criteria.getIsActive(), EventFeaturedPerformers_.isActive)
                            : null,
                    criteria.getPriorityRanking() != null
                            ? buildRangeSpecification(criteria.getPriorityRanking(),
                                    EventFeaturedPerformers_.priorityRanking)
                            : null,
                    criteria.getCreatedAt() != null
                            ? buildRangeSpecification(criteria.getCreatedAt(), EventFeaturedPerformers_.createdAt)
                            : null,
                    criteria.getUpdatedAt() != null
                            ? buildRangeSpecification(criteria.getUpdatedAt(), EventFeaturedPerformers_.updatedAt)
                            : null,
                    criteria.getEventId() != null
                            ? buildReferringEntitySpecification(criteria.getEventId(), EventFeaturedPerformers_.event,
                                    EventDetails_.id)
                            : null);
        }
        return specification;
    }
}
