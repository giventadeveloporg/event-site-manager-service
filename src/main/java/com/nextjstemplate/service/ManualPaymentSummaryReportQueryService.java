package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.ManualPaymentSummaryReport;
import com.nextjstemplate.repository.ManualPaymentSummaryReportRepository;
import com.nextjstemplate.service.criteria.ManualPaymentSummaryReportCriteria;
import com.nextjstemplate.service.dto.ManualPaymentSummaryReportDTO;
import com.nextjstemplate.service.mapper.ManualPaymentSummaryReportMapper;
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
 * Service for executing complex queries for {@link ManualPaymentSummaryReport} entities in the database.
 */
@Service
@Transactional(readOnly = true)
public class ManualPaymentSummaryReportQueryService extends QueryService<ManualPaymentSummaryReport> {

    private final Logger log = LoggerFactory.getLogger(ManualPaymentSummaryReportQueryService.class);

    private final ManualPaymentSummaryReportRepository manualPaymentSummaryReportRepository;

    private final ManualPaymentSummaryReportMapper manualPaymentSummaryReportMapper;

    public ManualPaymentSummaryReportQueryService(
        ManualPaymentSummaryReportRepository manualPaymentSummaryReportRepository,
        ManualPaymentSummaryReportMapper manualPaymentSummaryReportMapper
    ) {
        this.manualPaymentSummaryReportRepository = manualPaymentSummaryReportRepository;
        this.manualPaymentSummaryReportMapper = manualPaymentSummaryReportMapper;
    }

    public List<ManualPaymentSummaryReportDTO> findByCriteria(ManualPaymentSummaryReportCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ManualPaymentSummaryReport> specification = createSpecification(criteria);
        return manualPaymentSummaryReportMapper.toDto(manualPaymentSummaryReportRepository.findAll(specification));
    }

    public Page<ManualPaymentSummaryReportDTO> findByCriteria(ManualPaymentSummaryReportCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ManualPaymentSummaryReport> specification = createSpecification(criteria);
        return manualPaymentSummaryReportRepository.findAll(specification, page).map(manualPaymentSummaryReportMapper::toDto);
    }

    public long countByCriteria(ManualPaymentSummaryReportCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ManualPaymentSummaryReport> specification = createSpecification(criteria);
        return manualPaymentSummaryReportRepository.count(specification);
    }

    protected Specification<ManualPaymentSummaryReport> createSpecification(ManualPaymentSummaryReportCriteria criteria) {
        Specification<ManualPaymentSummaryReport> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ManualPaymentSummaryReport_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), ManualPaymentSummaryReport_.tenantId));
            }
            if (criteria.getEventId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEventId(), ManualPaymentSummaryReport_.eventId));
            }
            if (criteria.getPaymentMethodType() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getPaymentMethodType(), ManualPaymentSummaryReport_.paymentMethodType));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), ManualPaymentSummaryReport_.status));
            }
            if (criteria.getSnapshotDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getSnapshotDate(), ManualPaymentSummaryReport_.snapshotDate));
            }
        }
        return specification;
    }
}
