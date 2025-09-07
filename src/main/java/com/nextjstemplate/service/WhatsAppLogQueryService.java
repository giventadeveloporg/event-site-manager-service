package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.WhatsAppLog;
import com.nextjstemplate.repository.WhatsAppLogRepository;
import com.nextjstemplate.service.criteria.WhatsAppLogCriteria;
import com.nextjstemplate.service.dto.WhatsAppLogDTO;
import com.nextjstemplate.service.mapper.WhatsAppLogMapper;
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
 * Service for executing complex queries for {@link WhatsAppLog} entities in the database.
 * The main input is a {@link WhatsAppLogCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link WhatsAppLogDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WhatsAppLogQueryService extends QueryService<WhatsAppLog> {

    private static final Logger LOG = LoggerFactory.getLogger(WhatsAppLogQueryService.class);

    private final WhatsAppLogRepository whatsAppLogRepository;

    private final WhatsAppLogMapper whatsAppLogMapper;

    public WhatsAppLogQueryService(WhatsAppLogRepository whatsAppLogRepository, WhatsAppLogMapper whatsAppLogMapper) {
        this.whatsAppLogRepository = whatsAppLogRepository;
        this.whatsAppLogMapper = whatsAppLogMapper;
    }

    /**
     * Return a {@link Page} of {@link WhatsAppLogDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WhatsAppLogDTO> findByCriteria(WhatsAppLogCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WhatsAppLog> specification = createSpecification(criteria);
        return whatsAppLogRepository.findAll(specification, page).map(whatsAppLogMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WhatsAppLogCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<WhatsAppLog> specification = createSpecification(criteria);
        return whatsAppLogRepository.count(specification);
    }

    /**
     * Function to convert {@link WhatsAppLogCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<WhatsAppLog> createSpecification(WhatsAppLogCriteria criteria) {
        Specification<WhatsAppLog> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification =
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                    buildRangeSpecification(criteria.getId(), WhatsAppLog_.id),
                    buildStringSpecification(criteria.getTenantId(), WhatsAppLog_.tenantId),
                    buildStringSpecification(criteria.getRecipientPhone(), WhatsAppLog_.recipientPhone),
                    buildStringSpecification(criteria.getMessageBody(), WhatsAppLog_.messageBody),
                    buildRangeSpecification(criteria.getSentAt(), WhatsAppLog_.sentAt),
                    buildStringSpecification(criteria.getStatus(), WhatsAppLog_.status),
                    buildStringSpecification(criteria.getType(), WhatsAppLog_.type),
                    buildRangeSpecification(criteria.getCampaignId(), WhatsAppLog_.campaignId),
                    buildStringSpecification(criteria.getMetadata(), WhatsAppLog_.metadata),
                    buildSpecification(
                        criteria.getCampaignId(),
                        root -> root.join(WhatsAppLog_.campaign, JoinType.LEFT).get(CommunicationCampaign_.id)
                    )
                );
        }
        return specification;
    }
}
