package com.eventsitemanager.service;

import com.eventsitemanager.domain.*; // for static metamodels
import com.eventsitemanager.domain.EventFocusGroup;
import com.eventsitemanager.repository.EventFocusGroupRepository;
import com.eventsitemanager.service.criteria.EventFocusGroupCriteria;
import com.eventsitemanager.service.dto.EventFocusGroupDTO;
import com.eventsitemanager.service.mapper.EventFocusGroupMapper;
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
 * Service for executing complex queries for {@link EventFocusGroup} entities in
 * the database.
 * The main input is a {@link EventFocusGroupCriteria} which gets converted to
 * {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EventFocusGroupDTO} which fulfills the
 * criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventFocusGroupQueryService extends QueryService<EventFocusGroup> {

    private static final Logger LOG = LoggerFactory.getLogger(EventFocusGroupQueryService.class);

    private final EventFocusGroupRepository eventFocusGroupRepository;

    private final EventFocusGroupMapper eventFocusGroupMapper;

    public EventFocusGroupQueryService(EventFocusGroupRepository eventFocusGroupRepository, EventFocusGroupMapper eventFocusGroupMapper) {
        this.eventFocusGroupRepository = eventFocusGroupRepository;
        this.eventFocusGroupMapper = eventFocusGroupMapper;
    }

    /**
     * Return a {@link Page} of {@link EventFocusGroupDTO} which matches the
     * criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities
     *                 should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EventFocusGroupDTO> findByCriteria(EventFocusGroupCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EventFocusGroup> specification = createSpecification(criteria);
        return eventFocusGroupRepository.findAll(specification, page).map(eventFocusGroupMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     *
     * @param criteria The object which holds all the filters, which the entities
     *                 should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventFocusGroupCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<EventFocusGroup> specification = createSpecification(criteria);
        return eventFocusGroupRepository.count(specification);
    }

    /**
     * Function to convert {@link EventFocusGroupCriteria} to a
     * {@link Specification}
     *
     * @param criteria The object which holds all the filters, which the entities
     *                 should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EventFocusGroup> createSpecification(EventFocusGroupCriteria criteria) {
        Specification<EventFocusGroup> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EventFocusGroup_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), EventFocusGroup_.tenantId));
            }
            if (criteria.getEventId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEventId(), EventFocusGroup_.eventId));
            }
            if (criteria.getFocusGroupId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFocusGroupId(),
                            root -> root.join(EventFocusGroup_.focusGroup, JoinType.LEFT).get(FocusGroup_.id)
                        )
                    );
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), EventFocusGroup_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), EventFocusGroup_.updatedAt));
            }
        }
        return specification;
    }
}
