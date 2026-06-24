package com.eventsitemanager.service;

import com.eventsitemanager.domain.*; // for static metamodels
import com.eventsitemanager.domain.EventOrganizer;
import com.eventsitemanager.repository.EventOrganizerRepository;
import com.eventsitemanager.service.criteria.EventOrganizerCriteria;
import com.eventsitemanager.service.dto.EventOrganizerDTO;
import com.eventsitemanager.service.mapper.EventOrganizerMapper;
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
 * Service for executing complex queries for {@link EventOrganizer} entities in the database.
 * The main input is a {@link EventOrganizerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EventOrganizerDTO} or a {@link Page} of {@link EventOrganizerDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventOrganizerQueryService extends QueryService<EventOrganizer> {

    private final Logger log = LoggerFactory.getLogger(EventOrganizerQueryService.class);

    private final EventOrganizerRepository eventOrganizerRepository;

    private final EventOrganizerMapper eventOrganizerMapper;

    public EventOrganizerQueryService(EventOrganizerRepository eventOrganizerRepository, EventOrganizerMapper eventOrganizerMapper) {
        this.eventOrganizerRepository = eventOrganizerRepository;
        this.eventOrganizerMapper = eventOrganizerMapper;
    }

    /**
     * Return a {@link List} of {@link EventOrganizerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EventOrganizerDTO> findByCriteria(EventOrganizerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EventOrganizer> specification = createSpecification(criteria);
        return eventOrganizerMapper.toDto(eventOrganizerRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EventOrganizerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EventOrganizerDTO> findByCriteria(EventOrganizerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EventOrganizer> specification = createSpecification(criteria);
        return eventOrganizerRepository.findAll(specification, page).map(eventOrganizerMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventOrganizerCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EventOrganizer> specification = createSpecification(criteria);
        return eventOrganizerRepository.count(specification);
    }

    /**
     * Function to convert {@link EventOrganizerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EventOrganizer> createSpecification(EventOrganizerCriteria criteria) {
        Specification<EventOrganizer> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EventOrganizer_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), EventOrganizer_.tenantId));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), EventOrganizer_.title));
            }
            if (criteria.getDesignation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDesignation(), EventOrganizer_.designation));
            }
            if (criteria.getContactEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactEmail(), EventOrganizer_.contactEmail));
            }
            if (criteria.getContactPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactPhone(), EventOrganizer_.contactPhone));
            }
            if (criteria.getIsPrimary() != null) {
                specification = specification.and(buildSpecification(criteria.getIsPrimary(), EventOrganizer_.isPrimary));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), EventOrganizer_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), EventOrganizer_.updatedAt));
            }
            if (criteria.getEventId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEventId(),
                            root -> root.join(EventOrganizer_.event, JoinType.LEFT).get(EventDetails_.id)
                        )
                    );
            }
            if (criteria.getOrganizerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOrganizerId(),
                            root -> root.join(EventOrganizer_.organizer, JoinType.LEFT).get(UserProfile_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
