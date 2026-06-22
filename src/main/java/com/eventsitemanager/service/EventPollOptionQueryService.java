package com.eventsitemanager.service;

import com.eventsitemanager.domain.*; // for static metamodels
import com.eventsitemanager.domain.EventPollOption;
import com.eventsitemanager.repository.EventPollOptionRepository;
import com.eventsitemanager.service.criteria.EventPollOptionCriteria;
import com.eventsitemanager.service.dto.EventPollOptionDTO;
import com.eventsitemanager.service.mapper.EventPollOptionMapper;
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
 * Service for executing complex queries for {@link EventPollOption} entities in the database.
 * The main input is a {@link EventPollOptionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EventPollOptionDTO} or a {@link Page} of {@link EventPollOptionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventPollOptionQueryService extends QueryService<EventPollOption> {

    private final Logger log = LoggerFactory.getLogger(EventPollOptionQueryService.class);

    private final EventPollOptionRepository eventPollOptionRepository;

    private final EventPollOptionMapper eventPollOptionMapper;

    public EventPollOptionQueryService(EventPollOptionRepository eventPollOptionRepository, EventPollOptionMapper eventPollOptionMapper) {
        this.eventPollOptionRepository = eventPollOptionRepository;
        this.eventPollOptionMapper = eventPollOptionMapper;
    }

    /**
     * Return a {@link List} of {@link EventPollOptionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EventPollOptionDTO> findByCriteria(EventPollOptionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EventPollOption> specification = createSpecification(criteria);
        return eventPollOptionMapper.toDto(eventPollOptionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EventPollOptionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EventPollOptionDTO> findByCriteria(EventPollOptionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EventPollOption> specification = createSpecification(criteria);
        return eventPollOptionRepository.findAll(specification, page).map(eventPollOptionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventPollOptionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EventPollOption> specification = createSpecification(criteria);
        return eventPollOptionRepository.count(specification);
    }

    /**
     * Function to convert {@link EventPollOptionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EventPollOption> createSpecification(EventPollOptionCriteria criteria) {
        Specification<EventPollOption> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EventPollOption_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), EventPollOption_.tenantId));
            }
            if (criteria.getOptionText() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOptionText(), EventPollOption_.optionText));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), EventPollOption_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), EventPollOption_.updatedAt));
            }
            if (criteria.getPollId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPollId(), root -> root.join(EventPollOption_.poll, JoinType.LEFT).get(EventPoll_.id))
                    );
            }
        }
        return specification;
    }
}
