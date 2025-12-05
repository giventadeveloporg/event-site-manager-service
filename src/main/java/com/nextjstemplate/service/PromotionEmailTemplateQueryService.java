package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.PromotionEmailTemplate;
import com.nextjstemplate.repository.PromotionEmailTemplateRepository;
import com.nextjstemplate.service.criteria.PromotionEmailTemplateCriteria;
import com.nextjstemplate.service.dto.PromotionEmailTemplateDTO;
import com.nextjstemplate.service.mapper.PromotionEmailTemplateMapper;
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
 * Service for executing complex queries for {@link PromotionEmailTemplate} entities in the database.
 * The main input is a {@link PromotionEmailTemplateCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PromotionEmailTemplateDTO} or a {@link Page} of {@link PromotionEmailTemplateDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PromotionEmailTemplateQueryService extends QueryService<PromotionEmailTemplate> {

    private final Logger log = LoggerFactory.getLogger(PromotionEmailTemplateQueryService.class);

    private final PromotionEmailTemplateRepository promotionEmailTemplateRepository;

    private final PromotionEmailTemplateMapper promotionEmailTemplateMapper;

    public PromotionEmailTemplateQueryService(
        PromotionEmailTemplateRepository promotionEmailTemplateRepository,
        PromotionEmailTemplateMapper promotionEmailTemplateMapper
    ) {
        this.promotionEmailTemplateRepository = promotionEmailTemplateRepository;
        this.promotionEmailTemplateMapper = promotionEmailTemplateMapper;
    }

    /**
     * Return a {@link List} of {@link PromotionEmailTemplateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PromotionEmailTemplateDTO> findByCriteria(PromotionEmailTemplateCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PromotionEmailTemplate> specification = createSpecification(criteria);
        return promotionEmailTemplateMapper.toDto(promotionEmailTemplateRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PromotionEmailTemplateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PromotionEmailTemplateDTO> findByCriteria(PromotionEmailTemplateCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PromotionEmailTemplate> specification = createSpecification(criteria);
        return promotionEmailTemplateRepository.findAll(specification, page).map(promotionEmailTemplateMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PromotionEmailTemplateCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PromotionEmailTemplate> specification = createSpecification(criteria);
        return promotionEmailTemplateRepository.count(specification);
    }

    /**
     * Function to convert {@link PromotionEmailTemplateCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PromotionEmailTemplate> createSpecification(PromotionEmailTemplateCriteria criteria) {
        Specification<PromotionEmailTemplate> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PromotionEmailTemplate_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), PromotionEmailTemplate_.tenantId));
            }
            if (criteria.getEventId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEventId(), PromotionEmailTemplate_.eventId));
            }
            if (criteria.getTemplateName() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getTemplateName(), PromotionEmailTemplate_.templateName));
            }
            if (criteria.getSubject() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSubject(), PromotionEmailTemplate_.subject));
            }
            if (criteria.getPromotionCode() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getPromotionCode(), PromotionEmailTemplate_.promotionCode));
            }
            if (criteria.getDiscountCodeId() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getDiscountCodeId(), PromotionEmailTemplate_.discountCodeId));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), PromotionEmailTemplate_.isActive));
            }
            if (criteria.getCreatedById() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedById(), PromotionEmailTemplate_.createdById));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), PromotionEmailTemplate_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), PromotionEmailTemplate_.updatedAt));
            }
        }
        return specification;
    }
}
