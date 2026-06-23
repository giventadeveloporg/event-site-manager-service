package com.eventsitemanager.service;

import com.eventsitemanager.domain.*; // for static metamodels
import com.eventsitemanager.domain.EventTypeDetails;
import com.eventsitemanager.repository.EventTypeDetailsRepository;
import com.eventsitemanager.service.criteria.EventTypeDetailsCriteria;
import com.eventsitemanager.service.dto.EventTypeDetailsDTO;
import com.eventsitemanager.service.mapper.EventTypeDetailsMapper;
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
 * Service for executing complex queries for {@link EventTypeDetails} entities in the database.
 * The main input is a {@link EventTypeDetailsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EventTypeDetailsDTO} or a {@link Page} of {@link EventTypeDetailsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventTypeDetailsQueryService extends QueryService<EventTypeDetails> {

    private final Logger log = LoggerFactory.getLogger(EventTypeDetailsQueryService.class);

    private final EventTypeDetailsRepository eventTypeDetailsRepository;

    private final EventTypeDetailsMapper eventTypeDetailsMapper;

    public EventTypeDetailsQueryService(
        EventTypeDetailsRepository eventTypeDetailsRepository,
        EventTypeDetailsMapper eventTypeDetailsMapper
    ) {
        this.eventTypeDetailsRepository = eventTypeDetailsRepository;
        this.eventTypeDetailsMapper = eventTypeDetailsMapper;
    }

    /**
     * Return a {@link List} of {@link EventTypeDetailsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EventTypeDetailsDTO> findByCriteria(EventTypeDetailsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EventTypeDetails> specification = createSpecification(criteria);
        return eventTypeDetailsMapper.toDto(eventTypeDetailsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EventTypeDetailsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EventTypeDetailsDTO> findByCriteria(EventTypeDetailsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EventTypeDetails> specification = createSpecification(criteria);
        return eventTypeDetailsRepository.findAll(specification, page).map(eventTypeDetailsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventTypeDetailsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EventTypeDetails> specification = createSpecification(criteria);
        return eventTypeDetailsRepository.count(specification);
    }

    /**
     * Function to convert {@link EventTypeDetailsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EventTypeDetails> createSpecification(EventTypeDetailsCriteria criteria) {
        Specification<EventTypeDetails> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EventTypeDetails_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), EventTypeDetails_.tenantId));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), EventTypeDetails_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), EventTypeDetails_.description));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), EventTypeDetails_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), EventTypeDetails_.updatedAt));
            }
        }
        return specification;
    }
}
