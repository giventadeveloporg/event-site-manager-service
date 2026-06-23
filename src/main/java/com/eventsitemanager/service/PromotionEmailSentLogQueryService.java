package com.eventsitemanager.service;

import com.eventsitemanager.domain.*; // for static metamodels
import com.eventsitemanager.domain.PromotionEmailSentLog;
import com.eventsitemanager.domain.enumeration.EmailStatus;
import com.eventsitemanager.repository.PromotionEmailSentLogRepository;
import com.eventsitemanager.service.criteria.PromotionEmailSentLogCriteria;
import com.eventsitemanager.service.dto.PromotionEmailSentLogDTO;
import com.eventsitemanager.service.mapper.PromotionEmailSentLogMapper;
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
 * Service for executing complex queries for {@link PromotionEmailSentLog} entities in the database.
 * The main input is a {@link PromotionEmailSentLogCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PromotionEmailSentLogDTO} or a {@link Page} of {@link PromotionEmailSentLogDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PromotionEmailSentLogQueryService extends QueryService<PromotionEmailSentLog> {

    private final Logger log = LoggerFactory.getLogger(PromotionEmailSentLogQueryService.class);

    private final PromotionEmailSentLogRepository promotionEmailSentLogRepository;

    private final PromotionEmailSentLogMapper promotionEmailSentLogMapper;

    public PromotionEmailSentLogQueryService(
        PromotionEmailSentLogRepository promotionEmailSentLogRepository,
        PromotionEmailSentLogMapper promotionEmailSentLogMapper
    ) {
        this.promotionEmailSentLogRepository = promotionEmailSentLogRepository;
        this.promotionEmailSentLogMapper = promotionEmailSentLogMapper;
    }

    /**
     * Return a {@link List} of {@link PromotionEmailSentLogDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PromotionEmailSentLogDTO> findByCriteria(PromotionEmailSentLogCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PromotionEmailSentLog> specification = createSpecification(criteria);
        return promotionEmailSentLogMapper.toDto(promotionEmailSentLogRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PromotionEmailSentLogDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PromotionEmailSentLogDTO> findByCriteria(PromotionEmailSentLogCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PromotionEmailSentLog> specification = createSpecification(criteria);
        return promotionEmailSentLogRepository.findAll(specification, page).map(promotionEmailSentLogMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PromotionEmailSentLogCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PromotionEmailSentLog> specification = createSpecification(criteria);
        return promotionEmailSentLogRepository.count(specification);
    }

    /**
     * Function to convert {@link PromotionEmailSentLogCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PromotionEmailSentLog> createSpecification(PromotionEmailSentLogCriteria criteria) {
        Specification<PromotionEmailSentLog> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PromotionEmailSentLog_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), PromotionEmailSentLog_.tenantId));
            }
            if (criteria.getTemplateId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTemplateId(), PromotionEmailSentLog_.templateId));
            }
            if (criteria.getEventId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEventId(), PromotionEmailSentLog_.eventId));
            }
            if (criteria.getRecipientEmail() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getRecipientEmail(), PromotionEmailSentLog_.recipientEmail));
            }
            if (criteria.getSubject() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSubject(), PromotionEmailSentLog_.subject));
            }
            if (criteria.getPromotionCode() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getPromotionCode(), PromotionEmailSentLog_.promotionCode));
            }
            if (criteria.getDiscountCodeId() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getDiscountCodeId(), PromotionEmailSentLog_.discountCodeId));
            }
            if (criteria.getSentAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSentAt(), PromotionEmailSentLog_.sentAt));
            }
            if (criteria.getIsTestEmail() != null) {
                specification = specification.and(buildSpecification(criteria.getIsTestEmail(), PromotionEmailSentLog_.isTestEmail));
            }
            if (criteria.getEmailStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getEmailStatus(), PromotionEmailSentLog_.emailStatus));
            }
            if (criteria.getErrorMessage() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getErrorMessage(), PromotionEmailSentLog_.errorMessage));
            }
            if (criteria.getSentById() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSentById(), PromotionEmailSentLog_.sentById));
            }
        }
        return specification;
    }
}
