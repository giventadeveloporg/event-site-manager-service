package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.EventLiveUpdate;
import com.nextjstemplate.repository.EventLiveUpdateRepository;
import com.nextjstemplate.service.criteria.EventLiveUpdateCriteria;
import com.nextjstemplate.service.dto.EventLiveUpdateDTO;
import com.nextjstemplate.service.mapper.EventLiveUpdateMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link EventLiveUpdate} entities in the database.
 * The main input is a {@link EventLiveUpdateCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EventLiveUpdateDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventLiveUpdateQueryService extends QueryService<EventLiveUpdate> {

    private static final Logger LOG = LoggerFactory.getLogger(EventLiveUpdateQueryService.class);

    private final EventLiveUpdateRepository eventLiveUpdateRepository;

    private final EventLiveUpdateMapper eventLiveUpdateMapper;

    public EventLiveUpdateQueryService(EventLiveUpdateRepository eventLiveUpdateRepository, EventLiveUpdateMapper eventLiveUpdateMapper) {
        this.eventLiveUpdateRepository = eventLiveUpdateRepository;
        this.eventLiveUpdateMapper = eventLiveUpdateMapper;
    }

    /**
     * Return a {@link Page} of {@link EventLiveUpdateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EventLiveUpdateDTO> findByCriteria(EventLiveUpdateCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EventLiveUpdate> specification = createSpecification(criteria);
        return eventLiveUpdateRepository.findAll(specification, page).map(eventLiveUpdateMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventLiveUpdateCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<EventLiveUpdate> specification = createSpecification(criteria);
        return eventLiveUpdateRepository.count(specification);
    }

    /**
     * Function to convert {@link EventLiveUpdateCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EventLiveUpdate> createSpecification(EventLiveUpdateCriteria criteria) {
        Specification<EventLiveUpdate> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), EventLiveUpdate_.id),
                buildStringSpecification(criteria.getUpdateType(), EventLiveUpdate_.updateType),
                buildStringSpecification(criteria.getContentText(), EventLiveUpdate_.contentText),
                buildStringSpecification(criteria.getContentImageUrl(), EventLiveUpdate_.contentImageUrl),
                buildStringSpecification(criteria.getContentVideoUrl(), EventLiveUpdate_.contentVideoUrl),
                buildStringSpecification(criteria.getContentLinkUrl(), EventLiveUpdate_.contentLinkUrl),
                buildStringSpecification(criteria.getMetadata(), EventLiveUpdate_.metadata),
                buildRangeSpecification(criteria.getDisplayOrder(), EventLiveUpdate_.displayOrder),
                buildSpecification(criteria.getIsDefault(), EventLiveUpdate_.isDefault),
                buildRangeSpecification(criteria.getCreatedAt(), EventLiveUpdate_.createdAt),
                buildRangeSpecification(criteria.getUpdatedAt(), EventLiveUpdate_.updatedAt),
                buildSpecification(criteria.getEventId(), root -> root.join(EventLiveUpdate_.event, JoinType.LEFT).get(EventDetails_.id))
            );
        }
        return specification;
    }
}
