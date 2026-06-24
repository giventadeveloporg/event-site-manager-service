package com.eventsitemanager.service;

import com.eventsitemanager.domain.*; // for static metamodels
import com.eventsitemanager.domain.EventGuestPricing;
import com.eventsitemanager.repository.EventGuestPricingRepository;
import com.eventsitemanager.service.criteria.EventGuestPricingCriteria;
import com.eventsitemanager.service.dto.EventGuestPricingDTO;
import com.eventsitemanager.service.mapper.EventGuestPricingMapper;
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
 * Service for executing complex queries for {@link EventGuestPricing} entities in the database.
 * The main input is a {@link EventGuestPricingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EventGuestPricingDTO} or a {@link Page} of {@link EventGuestPricingDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventGuestPricingQueryService extends QueryService<EventGuestPricing> {

    private final Logger log = LoggerFactory.getLogger(EventGuestPricingQueryService.class);

    private final EventGuestPricingRepository eventGuestPricingRepository;

    private final EventGuestPricingMapper eventGuestPricingMapper;

    public EventGuestPricingQueryService(
        EventGuestPricingRepository eventGuestPricingRepository,
        EventGuestPricingMapper eventGuestPricingMapper
    ) {
        this.eventGuestPricingRepository = eventGuestPricingRepository;
        this.eventGuestPricingMapper = eventGuestPricingMapper;
    }

    /**
     * Return a {@link List} of {@link EventGuestPricingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EventGuestPricingDTO> findByCriteria(EventGuestPricingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EventGuestPricing> specification = createSpecification(criteria);
        return eventGuestPricingMapper.toDto(eventGuestPricingRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EventGuestPricingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EventGuestPricingDTO> findByCriteria(EventGuestPricingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EventGuestPricing> specification = createSpecification(criteria);
        return eventGuestPricingRepository.findAll(specification, page).map(eventGuestPricingMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventGuestPricingCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EventGuestPricing> specification = createSpecification(criteria);
        return eventGuestPricingRepository.count(specification);
    }

    /**
     * Function to convert {@link EventGuestPricingCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EventGuestPricing> createSpecification(EventGuestPricingCriteria criteria) {
        Specification<EventGuestPricing> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EventGuestPricing_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), EventGuestPricing_.tenantId));
            }
            if (criteria.getAgeGroup() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAgeGroup(), EventGuestPricing_.ageGroup));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), EventGuestPricing_.price));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), EventGuestPricing_.isActive));
            }
            if (criteria.getValidFrom() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValidFrom(), EventGuestPricing_.validFrom));
            }
            if (criteria.getValidTo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValidTo(), EventGuestPricing_.validTo));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), EventGuestPricing_.description));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), EventGuestPricing_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), EventGuestPricing_.updatedAt));
            }
            if (criteria.getEventId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEventId(),
                            root -> root.join(EventGuestPricing_.event, JoinType.LEFT).get(EventDetails_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
