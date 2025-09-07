package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.EventTicketTransactionItem;
import com.nextjstemplate.repository.EventTicketTransactionItemRepository;
import com.nextjstemplate.service.criteria.EventTicketTransactionItemCriteria;
import com.nextjstemplate.service.dto.EventTicketTransactionItemDTO;
import com.nextjstemplate.service.mapper.EventTicketTransactionItemMapper;
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
 * Service for executing complex queries for {@link EventTicketTransactionItem} entities in the database.
 * The main input is a {@link EventTicketTransactionItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EventTicketTransactionItemDTO} or a {@link Page} of {@link EventTicketTransactionItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventTicketTransactionItemQueryService extends QueryService<EventTicketTransactionItem> {

    private final Logger log = LoggerFactory.getLogger(EventTicketTransactionItemQueryService.class);

    private final EventTicketTransactionItemRepository eventTicketTransactionItemRepository;

    private final EventTicketTransactionItemMapper eventTicketTransactionItemMapper;

    public EventTicketTransactionItemQueryService(
        EventTicketTransactionItemRepository eventTicketTransactionItemRepository,
        EventTicketTransactionItemMapper eventTicketTransactionItemMapper
    ) {
        this.eventTicketTransactionItemRepository = eventTicketTransactionItemRepository;
        this.eventTicketTransactionItemMapper = eventTicketTransactionItemMapper;
    }

    /**
     * Return a {@link List} of {@link EventTicketTransactionItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EventTicketTransactionItemDTO> findByCriteria(EventTicketTransactionItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EventTicketTransactionItem> specification = createSpecification(criteria);
        return eventTicketTransactionItemMapper.toDto(eventTicketTransactionItemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EventTicketTransactionItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EventTicketTransactionItemDTO> findByCriteria(EventTicketTransactionItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EventTicketTransactionItem> specification = createSpecification(criteria);
        return eventTicketTransactionItemRepository.findAll(specification, page).map(eventTicketTransactionItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventTicketTransactionItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EventTicketTransactionItem> specification = createSpecification(criteria);
        return eventTicketTransactionItemRepository.count(specification);
    }

    /**
     * Function to convert {@link EventTicketTransactionItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EventTicketTransactionItem> createSpecification(EventTicketTransactionItemCriteria criteria) {
        Specification<EventTicketTransactionItem> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EventTicketTransactionItem_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), EventTicketTransactionItem_.tenantId));
            }
            if (criteria.getTransactionId() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTransactionId(), EventTicketTransactionItem_.transactionId));
            }
            if (criteria.getTicketTypeId() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTicketTypeId(), EventTicketTransactionItem_.ticketTypeId));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), EventTicketTransactionItem_.quantity));
            }
            if (criteria.getPricePerUnit() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getPricePerUnit(), EventTicketTransactionItem_.pricePerUnit));
            }
            if (criteria.getTotalAmount() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTotalAmount(), EventTicketTransactionItem_.totalAmount));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), EventTicketTransactionItem_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), EventTicketTransactionItem_.updatedAt));
            }
            /* if (criteria.getTransactionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTransactionId(),
                            root -> root.join(EventTicketTransactionItem_.transaction, JoinType.LEFT).get(EventTicketTransaction_.id)
                        )
                    );
            }
            if (criteria.getTicketTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTicketTypeId(),
                            root -> root.join(EventTicketTransactionItem_.ticketType, JoinType.LEFT).get(EventTicketType_.id)
                        )
                    );
            }*/
        }
        return specification;
    }
}
