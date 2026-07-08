package com.eventsitemanager.service;

import com.eventsitemanager.domain.*;
import com.eventsitemanager.domain.GasStationDailyMetrics;
import com.eventsitemanager.repository.GasStationDailyMetricsRepository;
import com.eventsitemanager.service.criteria.GasStationDailyMetricsCriteria;
import com.eventsitemanager.service.dto.GasStationDailyMetricsDTO;
import com.eventsitemanager.service.mapper.GasStationDailyMetricsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import tech.jhipster.service.filter.LongFilter;

@Service
@Transactional(readOnly = true)
public class GasStationDailyMetricsQueryService extends QueryService<GasStationDailyMetrics> {

    private static final Logger LOG = LoggerFactory.getLogger(GasStationDailyMetricsQueryService.class);

    private final GasStationDailyMetricsRepository gasStationDailyMetricsRepository;
    private final GasStationDailyMetricsMapper gasStationDailyMetricsMapper;
    private final GasStationAccessService gasStationAccessService;

    public GasStationDailyMetricsQueryService(
        GasStationDailyMetricsRepository gasStationDailyMetricsRepository,
        GasStationDailyMetricsMapper gasStationDailyMetricsMapper,
        GasStationAccessService gasStationAccessService
    ) {
        this.gasStationDailyMetricsRepository = gasStationDailyMetricsRepository;
        this.gasStationDailyMetricsMapper = gasStationDailyMetricsMapper;
        this.gasStationAccessService = gasStationAccessService;
    }

    private void applyAccessFilter(GasStationDailyMetricsCriteria criteria) {
        if (criteria == null) {
            return;
        }
        gasStationAccessService.assertGasModuleAccess();
        if (gasStationAccessService.isUnrestrictedServiceAccess()) {
            return;
        }
        if (gasStationAccessService.getAllowedStationIdsOrNull() == null) {
            return;
        }
        LongFilter stationFilter = criteria.getStationId();
        if (stationFilter == null) {
            stationFilter = new LongFilter();
            criteria.setStationId(stationFilter);
        }
        gasStationAccessService.applyStationIdCriteriaFilter(stationFilter);
    }

    public Page<GasStationDailyMetricsDTO> findByCriteria(GasStationDailyMetricsCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        if (criteria == null) {
            criteria = new GasStationDailyMetricsCriteria();
        }
        applyAccessFilter(criteria);
        final Specification<GasStationDailyMetrics> specification = createSpecification(criteria);
        return gasStationDailyMetricsRepository.findAll(specification, page).map(gasStationDailyMetricsMapper::toDto);
    }

    public long countByCriteria(GasStationDailyMetricsCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        if (criteria == null) {
            criteria = new GasStationDailyMetricsCriteria();
        }
        applyAccessFilter(criteria);
        final Specification<GasStationDailyMetrics> specification = createSpecification(criteria);
        return gasStationDailyMetricsRepository.count(specification);
    }

    protected Specification<GasStationDailyMetrics> createSpecification(GasStationDailyMetricsCriteria criteria) {
        Specification<GasStationDailyMetrics> specification = Specification.where(null);
        if (criteria != null) {
            specification =
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                    criteria.getId() != null ? buildRangeSpecification(criteria.getId(), GasStationDailyMetrics_.id) : null,
                    criteria.getTenantId() != null
                        ? buildStringSpecification(criteria.getTenantId(), GasStationDailyMetrics_.tenantId)
                        : null,
                    criteria.getStationId() != null
                        ? buildRangeSpecification(criteria.getStationId(), GasStationDailyMetrics_.stationId)
                        : null,
                    criteria.getMetricDate() != null
                        ? buildRangeSpecification(criteria.getMetricDate(), GasStationDailyMetrics_.metricDate)
                        : null,
                    null
                );
        }
        return specification;
    }
}
