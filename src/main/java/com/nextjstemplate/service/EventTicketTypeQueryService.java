package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.EventTicketType;
import com.nextjstemplate.repository.EventTicketTypeRepository;
import com.nextjstemplate.service.criteria.EventTicketTypeCriteria;
import com.nextjstemplate.service.dto.EventTicketTypeDTO;
import com.nextjstemplate.service.mapper.EventTicketTypeMapper;
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
 * Service for executing complex queries for {@link EventTicketType} entities in the database.
 * The main input is a {@link EventTicketTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EventTicketTypeDTO} or a {@link Page} of {@link EventTicketTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventTicketTypeQueryService extends QueryService<EventTicketType> {

    private final Logger log = LoggerFactory.getLogger(EventTicketTypeQueryService.class);

    private final EventTicketTypeRepository eventTicketTypeRepository;

    private final EventTicketTypeMapper eventTicketTypeMapper;

    public EventTicketTypeQueryService(EventTicketTypeRepository eventTicketTypeRepository, EventTicketTypeMapper eventTicketTypeMapper) {
        this.eventTicketTypeRepository = eventTicketTypeRepository;
        this.eventTicketTypeMapper = eventTicketTypeMapper;
    }

    /**
     * Return a {@link List} of {@link EventTicketTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EventTicketTypeDTO> findByCriteria(EventTicketTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EventTicketType> specification = createSpecification(criteria);
        return eventTicketTypeMapper.toDto(eventTicketTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EventTicketTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EventTicketTypeDTO> findByCriteria(EventTicketTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EventTicketType> specification = createSpecification(criteria);
        return eventTicketTypeRepository.findAll(specification, page).map(eventTicketTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventTicketTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EventTicketType> specification = createSpecification(criteria);
        return eventTicketTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link EventTicketTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EventTicketType> createSpecification(EventTicketTypeCriteria criteria) {
        Specification<EventTicketType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EventTicketType_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), EventTicketType_.tenantId));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), EventTicketType_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), EventTicketType_.description));
            }
            if (criteria.getIsServiceFeeIncluded() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getIsServiceFeeIncluded(), EventTicketType_.isServiceFeeIncluded));
            }
            if (criteria.getServiceFee() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getServiceFee(), EventTicketType_.serviceFee));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), EventTicketType_.price));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), EventTicketType_.code));
            }
            if (criteria.getAvailableQuantity() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getAvailableQuantity(), EventTicketType_.availableQuantity));
            }
            if (criteria.getSoldQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSoldQuantity(), EventTicketType_.soldQuantity));
            }
            if (criteria.getRemainingQuantity() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getRemainingQuantity(), EventTicketType_.remainingQuantity));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), EventTicketType_.isActive));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), EventTicketType_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), EventTicketType_.updatedAt));
            }
            if (criteria.getEventId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEventId(),
                            root -> root.join(EventTicketType_.event, JoinType.LEFT).get(EventDetails_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
