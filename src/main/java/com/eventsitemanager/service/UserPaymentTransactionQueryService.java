package com.eventsitemanager.service;

import com.eventsitemanager.domain.*; // for static metamodels
import com.eventsitemanager.domain.UserPaymentTransaction;
import com.eventsitemanager.repository.UserPaymentTransactionRepository;
import com.eventsitemanager.service.criteria.UserPaymentTransactionCriteria;
import com.eventsitemanager.service.dto.UserPaymentTransactionDTO;
import com.eventsitemanager.service.mapper.UserPaymentTransactionMapper;
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
 * Service for executing complex queries for {@link UserPaymentTransaction} entities in the database.
 * The main input is a {@link UserPaymentTransactionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserPaymentTransactionDTO} or a {@link Page} of {@link UserPaymentTransactionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserPaymentTransactionQueryService extends QueryService<UserPaymentTransaction> {

    private final Logger log = LoggerFactory.getLogger(UserPaymentTransactionQueryService.class);

    private final UserPaymentTransactionRepository userPaymentTransactionRepository;

    private final UserPaymentTransactionMapper userPaymentTransactionMapper;

    public UserPaymentTransactionQueryService(
        UserPaymentTransactionRepository userPaymentTransactionRepository,
        UserPaymentTransactionMapper userPaymentTransactionMapper
    ) {
        this.userPaymentTransactionRepository = userPaymentTransactionRepository;
        this.userPaymentTransactionMapper = userPaymentTransactionMapper;
    }

    /**
     * Return a {@link List} of {@link UserPaymentTransactionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserPaymentTransactionDTO> findByCriteria(UserPaymentTransactionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UserPaymentTransaction> specification = createSpecification(criteria);
        return userPaymentTransactionMapper.toDto(userPaymentTransactionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UserPaymentTransactionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserPaymentTransactionDTO> findByCriteria(UserPaymentTransactionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserPaymentTransaction> specification = createSpecification(criteria);
        return userPaymentTransactionRepository.findAll(specification, page).map(userPaymentTransactionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserPaymentTransactionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UserPaymentTransaction> specification = createSpecification(criteria);
        return userPaymentTransactionRepository.count(specification);
    }

    /**
     * Function to convert {@link UserPaymentTransactionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserPaymentTransaction> createSpecification(UserPaymentTransactionCriteria criteria) {
        Specification<UserPaymentTransaction> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UserPaymentTransaction_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), UserPaymentTransaction_.tenantId));
            }
            if (criteria.getTransactionType() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getTransactionType(), UserPaymentTransaction_.transactionType));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), UserPaymentTransaction_.amount));
            }
            if (criteria.getCurrency() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCurrency(), UserPaymentTransaction_.currency));
            }
            if (criteria.getStripePaymentIntentId() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getStripePaymentIntentId(), UserPaymentTransaction_.stripePaymentIntentId)
                    );
            }
            if (criteria.getStripeTransferGroup() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getStripeTransferGroup(), UserPaymentTransaction_.stripeTransferGroup)
                    );
            }
            if (criteria.getPlatformFeeAmount() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getPlatformFeeAmount(), UserPaymentTransaction_.platformFeeAmount));
            }
            if (criteria.getTenantAmount() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTenantAmount(), UserPaymentTransaction_.tenantAmount));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), UserPaymentTransaction_.status));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), UserPaymentTransaction_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), UserPaymentTransaction_.updatedAt));
            }
            if (criteria.getEventId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEventId(),
                            root -> root.join(UserPaymentTransaction_.event, JoinType.LEFT).get(EventDetails_.id)
                        )
                    );
            }
            if (criteria.getTicketTransactionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTicketTransactionId(),
                            root -> root.join(UserPaymentTransaction_.ticketTransaction, JoinType.LEFT).get(EventTicketTransaction_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
