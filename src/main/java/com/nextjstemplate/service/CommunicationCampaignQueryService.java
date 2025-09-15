package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.CommunicationCampaign;
import com.nextjstemplate.repository.CommunicationCampaignRepository;
import com.nextjstemplate.service.criteria.CommunicationCampaignCriteria;
import com.nextjstemplate.service.dto.CommunicationCampaignDTO;
import com.nextjstemplate.service.mapper.CommunicationCampaignMapper;
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
 * Service for executing complex queries for {@link CommunicationCampaign} entities in the database.
 * The main input is a {@link CommunicationCampaignCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CommunicationCampaignDTO} or a {@link Page} of {@link CommunicationCampaignDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CommunicationCampaignQueryService extends QueryService<CommunicationCampaign> {

    private final Logger log = LoggerFactory.getLogger(CommunicationCampaignQueryService.class);

    private final CommunicationCampaignRepository communicationCampaignRepository;

    private final CommunicationCampaignMapper communicationCampaignMapper;

    public CommunicationCampaignQueryService(
        CommunicationCampaignRepository communicationCampaignRepository,
        CommunicationCampaignMapper communicationCampaignMapper
    ) {
        this.communicationCampaignRepository = communicationCampaignRepository;
        this.communicationCampaignMapper = communicationCampaignMapper;
    }

    /**
     * Return a {@link List} of {@link CommunicationCampaignDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CommunicationCampaignDTO> findByCriteria(CommunicationCampaignCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CommunicationCampaign> specification = createSpecification(criteria);
        return communicationCampaignMapper.toDto(communicationCampaignRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CommunicationCampaignDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CommunicationCampaignDTO> findByCriteria(CommunicationCampaignCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CommunicationCampaign> specification = createSpecification(criteria);
        return communicationCampaignRepository.findAll(specification, page).map(communicationCampaignMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CommunicationCampaignCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CommunicationCampaign> specification = createSpecification(criteria);
        return communicationCampaignRepository.count(specification);
    }

    /**
     * Function to convert {@link CommunicationCampaignCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CommunicationCampaign> createSpecification(CommunicationCampaignCriteria criteria) {
        Specification<CommunicationCampaign> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CommunicationCampaign_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), CommunicationCampaign_.tenantId));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), CommunicationCampaign_.name));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), CommunicationCampaign_.type));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), CommunicationCampaign_.description));
            }
            if (criteria.getCreatedById() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedById(), CommunicationCampaign_.createdById));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), CommunicationCampaign_.createdAt));
            }
            if (criteria.getScheduledAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getScheduledAt(), CommunicationCampaign_.scheduledAt));
            }
            if (criteria.getSentAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSentAt(), CommunicationCampaign_.sentAt));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), CommunicationCampaign_.status));
            }

            if (criteria.getCreatedById() != null) {
                specification = specification.and(buildSpecification(criteria.getCreatedById(), CommunicationCampaign_.createdById));
            }
            /* if (criteria.getCreatedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCreatedById(),
                            root -> root.join(CommunicationCampaign_.createdBy, JoinType.LEFT).get(UserProfile_.id)
                        )
                    );
            }*/
        }
        return specification;
    }
}
