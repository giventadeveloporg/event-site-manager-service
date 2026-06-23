package com.eventsitemanager.service;

import com.eventsitemanager.domain.*; // for static metamodels
import com.eventsitemanager.domain.DiscountCode;
import com.eventsitemanager.repository.DiscountCodeRepository;
import com.eventsitemanager.service.criteria.DiscountCodeCriteria;
import com.eventsitemanager.service.dto.DiscountCodeDTO;
import com.eventsitemanager.service.mapper.DiscountCodeMapper;
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
 * Service for executing complex queries for {@link DiscountCode} entities in the database.
 * The main input is a {@link DiscountCodeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DiscountCodeDTO} or a {@link Page} of {@link DiscountCodeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DiscountCodeQueryService extends QueryService<DiscountCode> {

    private final Logger log = LoggerFactory.getLogger(DiscountCodeQueryService.class);

    private final DiscountCodeRepository discountCodeRepository;

    private final DiscountCodeMapper discountCodeMapper;

    public DiscountCodeQueryService(DiscountCodeRepository discountCodeRepository, DiscountCodeMapper discountCodeMapper) {
        this.discountCodeRepository = discountCodeRepository;
        this.discountCodeMapper = discountCodeMapper;
    }

    /**
     * Return a {@link List} of {@link DiscountCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DiscountCodeDTO> findByCriteria(DiscountCodeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DiscountCode> specification = createSpecification(criteria);
        return discountCodeMapper.toDto(discountCodeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DiscountCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DiscountCodeDTO> findByCriteria(DiscountCodeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DiscountCode> specification = createSpecification(criteria);
        return discountCodeRepository.findAll(specification, page).map(discountCodeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DiscountCodeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DiscountCode> specification = createSpecification(criteria);
        return discountCodeRepository.count(specification);
    }

    /**
     * Function to convert {@link DiscountCodeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DiscountCode> createSpecification(DiscountCodeCriteria criteria) {
        Specification<DiscountCode> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DiscountCode_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), DiscountCode_.code));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), DiscountCode_.description));
            }
            if (criteria.getDiscountType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDiscountType(), DiscountCode_.discountType));
            }
            if (criteria.getDiscountValue() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDiscountValue(), DiscountCode_.discountValue));
            }
            if (criteria.getMaxUses() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMaxUses(), DiscountCode_.maxUses));
            }
            if (criteria.getUsesCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUsesCount(), DiscountCode_.usesCount));
            }
            if (criteria.getValidFrom() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValidFrom(), DiscountCode_.validFrom));
            }
            if (criteria.getValidTo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValidTo(), DiscountCode_.validTo));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), DiscountCode_.isActive));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), DiscountCode_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), DiscountCode_.updatedAt));
            }
            if (criteria.getEventId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEventId(), DiscountCode_.eventId));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), DiscountCode_.tenantId));
            }
        }
        return specification;
    }
}
