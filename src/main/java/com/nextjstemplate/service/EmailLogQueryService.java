package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.EmailLog;
import com.nextjstemplate.repository.EmailLogRepository;
import com.nextjstemplate.service.criteria.EmailLogCriteria;
import com.nextjstemplate.service.dto.EmailLogDTO;
import com.nextjstemplate.service.mapper.EmailLogMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link EmailLog} entities in the database.
 * The main input is a {@link EmailLogCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EmailLogDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmailLogQueryService extends QueryService<EmailLog> {

    private static final Logger LOG = LoggerFactory.getLogger(EmailLogQueryService.class);

    private final EmailLogRepository emailLogRepository;

    private final EmailLogMapper emailLogMapper;

    public EmailLogQueryService(EmailLogRepository emailLogRepository, EmailLogMapper emailLogMapper) {
        this.emailLogRepository = emailLogRepository;
        this.emailLogMapper = emailLogMapper;
    }

    /**
     * Return a {@link Page} of {@link EmailLogDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EmailLogDTO> findByCriteria(EmailLogCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EmailLog> specification = createSpecification(criteria);
        return emailLogRepository.findAll(specification, page).map(emailLogMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmailLogCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<EmailLog> specification = createSpecification(criteria);
        return emailLogRepository.count(specification);
    }

    /**
     * Function to convert {@link EmailLogCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EmailLog> createSpecification(EmailLogCriteria criteria) {
        Specification<EmailLog> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification =
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                    buildRangeSpecification(criteria.getId(), EmailLog_.id),
                    buildStringSpecification(criteria.getTenantId(), EmailLog_.tenantId),
                    buildStringSpecification(criteria.getRecipientEmail(), EmailLog_.recipientEmail),
                    buildStringSpecification(criteria.getSubject(), EmailLog_.subject),
                    buildStringSpecification(criteria.getBody(), EmailLog_.body),
                    buildRangeSpecification(criteria.getSentAt(), EmailLog_.sentAt),
                    buildStringSpecification(criteria.getStatus(), EmailLog_.status),
                    buildStringSpecification(criteria.getType(), EmailLog_.type),
                    buildRangeSpecification(criteria.getCampaignId(), EmailLog_.campaignId),
                    buildStringSpecification(criteria.getMetadata(), EmailLog_.metadata),
                    buildSpecification(
                        criteria.getCampaignId(),
                        root -> root.join(EmailLog_.campaign, JoinType.LEFT).get(CommunicationCampaign_.id)
                    )
                );
        }
        return specification;
    }
}
