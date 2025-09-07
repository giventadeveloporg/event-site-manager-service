package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.EventAdminAuditLog;
import com.nextjstemplate.repository.EventAdminAuditLogRepository;
import com.nextjstemplate.service.criteria.EventAdminAuditLogCriteria;
import com.nextjstemplate.service.dto.EventAdminAuditLogDTO;
import com.nextjstemplate.service.mapper.EventAdminAuditLogMapper;
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
 * Service for executing complex queries for {@link EventAdminAuditLog} entities in the database.
 * The main input is a {@link EventAdminAuditLogCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EventAdminAuditLogDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventAdminAuditLogQueryService extends QueryService<EventAdminAuditLog> {

    private static final Logger LOG = LoggerFactory.getLogger(EventAdminAuditLogQueryService.class);

    private final EventAdminAuditLogRepository eventAdminAuditLogRepository;

    private final EventAdminAuditLogMapper eventAdminAuditLogMapper;

    public EventAdminAuditLogQueryService(
        EventAdminAuditLogRepository eventAdminAuditLogRepository,
        EventAdminAuditLogMapper eventAdminAuditLogMapper
    ) {
        this.eventAdminAuditLogRepository = eventAdminAuditLogRepository;
        this.eventAdminAuditLogMapper = eventAdminAuditLogMapper;
    }

    /**
     * Return a {@link Page} of {@link EventAdminAuditLogDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EventAdminAuditLogDTO> findByCriteria(EventAdminAuditLogCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EventAdminAuditLog> specification = createSpecification(criteria);
        return eventAdminAuditLogRepository.findAll(specification, page).map(eventAdminAuditLogMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventAdminAuditLogCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<EventAdminAuditLog> specification = createSpecification(criteria);
        return eventAdminAuditLogRepository.count(specification);
    }

    /**
     * Function to convert {@link EventAdminAuditLogCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EventAdminAuditLog> createSpecification(EventAdminAuditLogCriteria criteria) {
        Specification<EventAdminAuditLog> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification =
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                    buildRangeSpecification(criteria.getId(), EventAdminAuditLog_.id),
                    buildStringSpecification(criteria.getTenantId(), EventAdminAuditLog_.tenantId),
                    buildStringSpecification(criteria.getAction(), EventAdminAuditLog_.action),
                    buildStringSpecification(criteria.getTableName(), EventAdminAuditLog_.tableName),
                    buildStringSpecification(criteria.getRecordId(), EventAdminAuditLog_.recordId),
                    buildStringSpecification(criteria.getChanges(), EventAdminAuditLog_.changes),
                    buildRangeSpecification(criteria.getCreatedAt(), EventAdminAuditLog_.createdAt),
                    buildSpecification(
                        criteria.getAdminId(),
                        root -> root.join(EventAdminAuditLog_.admin, JoinType.LEFT).get(UserProfile_.id)
                    )
                );
        }
        return specification;
    }
}
