package com.eventsitemanager.service;

import com.eventsitemanager.domain.*; // for static metamodels
import com.eventsitemanager.domain.ManualPaymentRequest;
import com.eventsitemanager.repository.ManualPaymentRequestRepository;
import com.eventsitemanager.service.criteria.ManualPaymentRequestCriteria;
import com.eventsitemanager.service.dto.ManualPaymentRequestDTO;
import com.eventsitemanager.service.mapper.ManualPaymentRequestMapper;
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
 * Service for executing complex queries for {@link ManualPaymentRequest} entities in the database.
 */
@Service
@Transactional(readOnly = true)
public class ManualPaymentRequestQueryService extends QueryService<ManualPaymentRequest> {

    private final Logger log = LoggerFactory.getLogger(ManualPaymentRequestQueryService.class);

    private final ManualPaymentRequestRepository manualPaymentRequestRepository;

    private final ManualPaymentRequestMapper manualPaymentRequestMapper;

    public ManualPaymentRequestQueryService(
        ManualPaymentRequestRepository manualPaymentRequestRepository,
        ManualPaymentRequestMapper manualPaymentRequestMapper
    ) {
        this.manualPaymentRequestRepository = manualPaymentRequestRepository;
        this.manualPaymentRequestMapper = manualPaymentRequestMapper;
    }

    public List<ManualPaymentRequestDTO> findByCriteria(ManualPaymentRequestCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ManualPaymentRequest> specification = createSpecification(criteria);
        return manualPaymentRequestMapper.toDto(manualPaymentRequestRepository.findAll(specification));
    }

    public Page<ManualPaymentRequestDTO> findByCriteria(ManualPaymentRequestCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ManualPaymentRequest> specification = createSpecification(criteria);
        return manualPaymentRequestRepository.findAll(specification, page).map(manualPaymentRequestMapper::toDto);
    }

    public long countByCriteria(ManualPaymentRequestCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ManualPaymentRequest> specification = createSpecification(criteria);
        return manualPaymentRequestRepository.count(specification);
    }

    protected Specification<ManualPaymentRequest> createSpecification(ManualPaymentRequestCriteria criteria) {
        Specification<ManualPaymentRequest> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ManualPaymentRequest_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), ManualPaymentRequest_.tenantId));
            }
            if (criteria.getEventId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEventId(), ManualPaymentRequest_.eventId));
            }
            if (criteria.getTicketTransactionId() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getTicketTransactionId(), ManualPaymentRequest_.ticketTransactionId)
                    );
            }
            if (criteria.getRequesterEmail() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getRequesterEmail(), ManualPaymentRequest_.requesterEmail));
            }
            if (criteria.getAmountDue() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmountDue(), ManualPaymentRequest_.amountDue));
            }
            if (criteria.getPaymentMethodType() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getPaymentMethodType(), ManualPaymentRequest_.paymentMethodType));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), ManualPaymentRequest_.status));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), ManualPaymentRequest_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), ManualPaymentRequest_.updatedAt));
            }
        }
        return specification;
    }
}
