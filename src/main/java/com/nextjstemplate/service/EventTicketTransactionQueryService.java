package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.EventTicketTransaction;
import com.nextjstemplate.repository.EventTicketTransactionRepository;
import com.nextjstemplate.service.criteria.EventTicketTransactionCriteria;
import com.nextjstemplate.service.dto.EventTicketTransactionDTO;
import com.nextjstemplate.service.mapper.EventTicketTransactionMapper;
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
 * Service for executing complex queries for {@link EventTicketTransaction}
 * entities in the database.
 * The main input is a {@link EventTicketTransactionCriteria} which gets
 * converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EventTicketTransactionDTO} or a
 * {@link Page} of {@link EventTicketTransactionDTO} which fulfills the
 * criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventTicketTransactionQueryService extends QueryService<EventTicketTransaction> {

    private final Logger log = LoggerFactory.getLogger(EventTicketTransactionQueryService.class);

    private final EventTicketTransactionRepository eventTicketTransactionRepository;

    private final EventTicketTransactionMapper eventTicketTransactionMapper;

    public EventTicketTransactionQueryService(
        EventTicketTransactionRepository eventTicketTransactionRepository,
        EventTicketTransactionMapper eventTicketTransactionMapper
    ) {
        this.eventTicketTransactionRepository = eventTicketTransactionRepository;
        this.eventTicketTransactionMapper = eventTicketTransactionMapper;
    }

    /**
     * Return a {@link List} of {@link EventTicketTransactionDTO} which matches the
     * criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities
     *                 should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EventTicketTransactionDTO> findByCriteria(EventTicketTransactionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EventTicketTransaction> specification = createSpecification(criteria);
        return eventTicketTransactionMapper.toDto(eventTicketTransactionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EventTicketTransactionDTO} which matches the
     * criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities
     *                 should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EventTicketTransactionDTO> findByCriteria(EventTicketTransactionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EventTicketTransaction> specification = createSpecification(criteria);
        return eventTicketTransactionRepository.findAll(specification, page).map(eventTicketTransactionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     *
     * @param criteria The object which holds all the filters, which the entities
     *                 should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventTicketTransactionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EventTicketTransaction> specification = createSpecification(criteria);
        return eventTicketTransactionRepository.count(specification);
    }

    /**
     * Function to convert {@link EventTicketTransactionCriteria} to a
     * {@link Specification}
     *
     * @param criteria The object which holds all the filters, which the entities
     *                 should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EventTicketTransaction> createSpecification(EventTicketTransactionCriteria criteria) {
        Specification<EventTicketTransaction> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EventTicketTransaction_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), EventTicketTransaction_.tenantId));
            }
            if (criteria.getTransactionReference() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getTransactionReference(), EventTicketTransaction_.transactionReference)
                    );
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), EventTicketTransaction_.email));
            }
            if (criteria.getFirstName() != null) {
                // Check if this is a full name search (contains space)
                String firstNameValue = criteria.getFirstName().getContains();
                if (firstNameValue != null && firstNameValue.contains(" ")) {
                    // This is a full name search, search across both firstName and lastName
                    specification = specification.and(buildFullNameSpecification(firstNameValue));
                } else {
                    // Regular firstName search
                    specification = specification.and(buildStringSpecification(criteria.getFirstName(), EventTicketTransaction_.firstName));
                }
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), EventTicketTransaction_.lastName));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), EventTicketTransaction_.phone));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), EventTicketTransaction_.quantity));
            }
            if (criteria.getPricePerUnit() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getPricePerUnit(), EventTicketTransaction_.pricePerUnit));
            }
            if (criteria.getTotalAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalAmount(), EventTicketTransaction_.totalAmount));
            }
            if (criteria.getTaxAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTaxAmount(), EventTicketTransaction_.taxAmount));
            }
            if (criteria.getPlatformFeeAmount() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getPlatformFeeAmount(), EventTicketTransaction_.platformFeeAmount));
            }
            if (criteria.getDiscountCodeId() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getDiscountCodeId(), EventTicketTransaction_.discountCodeId));
            }
            if (criteria.getDiscountAmount() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getDiscountAmount(), EventTicketTransaction_.discountAmount));
            }
            if (criteria.getFinalAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFinalAmount(), EventTicketTransaction_.finalAmount));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), EventTicketTransaction_.status));
            }
            if (criteria.getPaymentMethod() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getPaymentMethod(), EventTicketTransaction_.paymentMethod));
            }
            if (criteria.getPaymentReference() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getPaymentReference(), EventTicketTransaction_.paymentReference));
            }
            if (criteria.getPurchaseDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getPurchaseDate(), EventTicketTransaction_.purchaseDate));
            }
            if (criteria.getConfirmationSentAt() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getConfirmationSentAt(), EventTicketTransaction_.confirmationSentAt)
                    );
            }
            if (criteria.getRefundAmount() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getRefundAmount(), EventTicketTransaction_.refundAmount));
            }
            if (criteria.getRefundDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRefundDate(), EventTicketTransaction_.refundDate));
            }
            if (criteria.getStripeCheckoutSessionId() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getStripeCheckoutSessionId(), EventTicketTransaction_.stripeCheckoutSessionId)
                    );
            }
            if (criteria.getStripePaymentIntentId() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getStripePaymentIntentId(), EventTicketTransaction_.stripePaymentIntentId)
                    );
            }
            if (criteria.getStripeCustomerId() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getStripeCustomerId(), EventTicketTransaction_.stripeCustomerId));
            }
            if (criteria.getStripePaymentStatus() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getStripePaymentStatus(), EventTicketTransaction_.stripePaymentStatus)
                    );
            }
            if (criteria.getStripeCustomerEmail() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getStripeCustomerEmail(), EventTicketTransaction_.stripeCustomerEmail)
                    );
            }
            if (criteria.getStripePaymentCurrency() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getStripePaymentCurrency(), EventTicketTransaction_.stripePaymentCurrency)
                    );
            }
            if (criteria.getStripeAmountDiscount() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getStripeAmountDiscount(), EventTicketTransaction_.stripeAmountDiscount)
                    );
            }
            if (criteria.getStripeAmountTax() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getStripeAmountTax(), EventTicketTransaction_.stripeAmountTax));
            }
            if (criteria.getStripeFeeAmount() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getStripeFeeAmount(), EventTicketTransaction_.stripeFeeAmount));
            }
            if (criteria.getQrCodeImageUrl() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getQrCodeImageUrl(), EventTicketTransaction_.qrCodeImageUrl));
            }
            if (criteria.getEventId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEventId(), EventTicketTransaction_.eventId));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserId(), EventTicketTransaction_.userId));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), EventTicketTransaction_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), EventTicketTransaction_.updatedAt));
            }
        }
        return specification;
    }

    /**
     * Builds a specification for full name search across firstName and lastName
     * fields
     *
     * @param fullName the full name to search for
     * @return the specification for full name search
     */
    private Specification<EventTicketTransaction> buildFullNameSpecification(String fullName) {
        return (root, query, criteriaBuilder) -> {
            String[] nameParts = fullName.trim().split("\\s+");

            if (nameParts.length == 1) {
                // Single word search - search in both firstName and lastName
                String searchTerm = "%" + nameParts[0].toUpperCase() + "%";
                return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.upper(root.get(EventTicketTransaction_.firstName)), searchTerm),
                    criteriaBuilder.like(criteriaBuilder.upper(root.get(EventTicketTransaction_.lastName)), searchTerm)
                );
            } else {
                // Multiple words - try different combinations
                String firstName = nameParts[0];
                String lastName = nameParts[nameParts.length - 1];

                // Search for firstName + lastName combination
                String firstNameSearch = "%" + firstName.toUpperCase() + "%";
                String lastNameSearch = "%" + lastName.toUpperCase() + "%";

                return criteriaBuilder.or(
                    // Exact firstName + lastName match
                    criteriaBuilder.and(
                        criteriaBuilder.like(criteriaBuilder.upper(root.get(EventTicketTransaction_.firstName)), firstNameSearch),
                        criteriaBuilder.like(criteriaBuilder.upper(root.get(EventTicketTransaction_.lastName)), lastNameSearch)
                    ),
                    // Reverse order (lastName + firstName)
                    criteriaBuilder.and(
                        criteriaBuilder.like(criteriaBuilder.upper(root.get(EventTicketTransaction_.firstName)), lastNameSearch),
                        criteriaBuilder.like(criteriaBuilder.upper(root.get(EventTicketTransaction_.lastName)), firstNameSearch)
                    ),
                    // Search for full name in firstName field (common case)
                    criteriaBuilder.like(
                        criteriaBuilder.upper(root.get(EventTicketTransaction_.firstName)),
                        "%" + fullName.toUpperCase() + "%"
                    )
                );
            }
        };
    }
}
