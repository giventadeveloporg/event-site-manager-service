package com.eventsitemanager.service;

import com.eventsitemanager.domain.*; // for static metamodels
import com.eventsitemanager.domain.EventCalendarEntry;
import com.eventsitemanager.repository.EventCalendarEntryRepository;
import com.eventsitemanager.service.criteria.EventCalendarEntryCriteria;
import com.eventsitemanager.service.dto.EventCalendarEntryDTO;
import com.eventsitemanager.service.mapper.EventCalendarEntryMapper;
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
 * Service for executing complex queries for {@link EventCalendarEntry} entities in the database.
 * The main input is a {@link EventCalendarEntryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EventCalendarEntryDTO} or a {@link Page} of {@link EventCalendarEntryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventCalendarEntryQueryService extends QueryService<EventCalendarEntry> {

    private final Logger log = LoggerFactory.getLogger(EventCalendarEntryQueryService.class);

    private final EventCalendarEntryRepository eventCalendarEntryRepository;

    private final EventCalendarEntryMapper eventCalendarEntryMapper;

    public EventCalendarEntryQueryService(
        EventCalendarEntryRepository eventCalendarEntryRepository,
        EventCalendarEntryMapper eventCalendarEntryMapper
    ) {
        this.eventCalendarEntryRepository = eventCalendarEntryRepository;
        this.eventCalendarEntryMapper = eventCalendarEntryMapper;
    }

    /**
     * Return a {@link List} of {@link EventCalendarEntryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EventCalendarEntryDTO> findByCriteria(EventCalendarEntryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EventCalendarEntry> specification = createSpecification(criteria);
        return eventCalendarEntryMapper.toDto(eventCalendarEntryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EventCalendarEntryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EventCalendarEntryDTO> findByCriteria(EventCalendarEntryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EventCalendarEntry> specification = createSpecification(criteria);
        return eventCalendarEntryRepository.findAll(specification, page).map(eventCalendarEntryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventCalendarEntryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EventCalendarEntry> specification = createSpecification(criteria);
        return eventCalendarEntryRepository.count(specification);
    }

    /**
     * Function to convert {@link EventCalendarEntryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EventCalendarEntry> createSpecification(EventCalendarEntryCriteria criteria) {
        Specification<EventCalendarEntry> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EventCalendarEntry_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), EventCalendarEntry_.tenantId));
            }
            if (criteria.getCalendarProvider() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getCalendarProvider(), EventCalendarEntry_.calendarProvider));
            }
            if (criteria.getExternalEventId() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getExternalEventId(), EventCalendarEntry_.externalEventId));
            }
            if (criteria.getCalendarLink() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCalendarLink(), EventCalendarEntry_.calendarLink));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), EventCalendarEntry_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), EventCalendarEntry_.updatedAt));
            }
            if (criteria.getEventId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEventId(),
                            root -> root.join(EventCalendarEntry_.event, JoinType.LEFT).get(EventDetails_.id)
                        )
                    );
            }
            if (criteria.getCreatedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCreatedById(),
                            root -> root.join(EventCalendarEntry_.createdBy, JoinType.LEFT).get(UserProfile_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
