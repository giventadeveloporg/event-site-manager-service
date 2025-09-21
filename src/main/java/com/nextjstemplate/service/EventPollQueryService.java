package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.EventPoll;
import com.nextjstemplate.repository.EventPollRepository;
import com.nextjstemplate.service.criteria.EventPollCriteria;
import com.nextjstemplate.service.dto.EventPollDTO;
import com.nextjstemplate.service.mapper.EventPollMapper;
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
 * Service for executing complex queries for {@link EventPoll} entities in the database.
 * The main input is a {@link EventPollCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EventPollDTO} or a {@link Page} of {@link EventPollDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventPollQueryService extends QueryService<EventPoll> {

    private final Logger log = LoggerFactory.getLogger(EventPollQueryService.class);

    private final EventPollRepository eventPollRepository;

    private final EventPollMapper eventPollMapper;

    public EventPollQueryService(EventPollRepository eventPollRepository, EventPollMapper eventPollMapper) {
        this.eventPollRepository = eventPollRepository;
        this.eventPollMapper = eventPollMapper;
    }

    /**
     * Return a {@link List} of {@link EventPollDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EventPollDTO> findByCriteria(EventPollCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EventPoll> specification = createSpecification(criteria);
        return eventPollMapper.toDto(eventPollRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EventPollDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EventPollDTO> findByCriteria(EventPollCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EventPoll> specification = createSpecification(criteria);
        return eventPollRepository.findAll(specification, page).map(eventPollMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventPollCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EventPoll> specification = createSpecification(criteria);
        return eventPollRepository.count(specification);
    }

    /**
     * Function to convert {@link EventPollCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EventPoll> createSpecification(EventPollCriteria criteria) {
        Specification<EventPoll> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EventPoll_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), EventPoll_.tenantId));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), EventPoll_.title));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), EventPoll_.description));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), EventPoll_.isActive));
            }
            if (criteria.getIsAnonymous() != null) {
                specification = specification.and(buildSpecification(criteria.getIsAnonymous(), EventPoll_.isAnonymous));
            }
            if (criteria.getAllowMultipleChoices() != null) {
                specification = specification.and(buildSpecification(criteria.getAllowMultipleChoices(), EventPoll_.allowMultipleChoices));
            }
            if (criteria.getMaxResponsesPerUser() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMaxResponsesPerUser(), EventPoll_.maxResponsesPerUser));
            }
            if (criteria.getResultsVisibleTo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getResultsVisibleTo(), EventPoll_.resultsVisibleTo));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), EventPoll_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), EventPoll_.endDate));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), EventPoll_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), EventPoll_.updatedAt));
            }
            if (criteria.getEventId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEventId(), root -> root.join(EventPoll_.event, JoinType.LEFT).get(EventDetails_.id))
                    );
            }
            if (criteria.getCreatedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCreatedById(),
                            root -> root.join(EventPoll_.createdBy, JoinType.LEFT).get(UserProfile_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
