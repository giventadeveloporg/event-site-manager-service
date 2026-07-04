package com.eventsitemanager.service;

import com.eventsitemanager.domain.*;
import com.eventsitemanager.domain.GasStationIntegration;
import com.eventsitemanager.repository.GasStationIntegrationRepository;
import com.eventsitemanager.service.criteria.GasStationIntegrationCriteria;
import com.eventsitemanager.service.dto.GasStationIntegrationDTO;
import com.eventsitemanager.service.mapper.GasStationIntegrationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

@Service
@Transactional(readOnly = true)
public class GasStationIntegrationQueryService extends QueryService<GasStationIntegration> {

    private static final Logger LOG = LoggerFactory.getLogger(GasStationIntegrationQueryService.class);

    private final GasStationIntegrationRepository gasStationIntegrationRepository;
    private final GasStationIntegrationMapper gasStationIntegrationMapper;

    public GasStationIntegrationQueryService(
        GasStationIntegrationRepository gasStationIntegrationRepository,
        GasStationIntegrationMapper gasStationIntegrationMapper
    ) {
        this.gasStationIntegrationRepository = gasStationIntegrationRepository;
        this.gasStationIntegrationMapper = gasStationIntegrationMapper;
    }

    public Page<GasStationIntegrationDTO> findByCriteria(GasStationIntegrationCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GasStationIntegration> specification = createSpecification(criteria);
        return gasStationIntegrationRepository.findAll(specification, page).map(gasStationIntegrationMapper::toDto);
    }

    public long countByCriteria(GasStationIntegrationCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<GasStationIntegration> specification = createSpecification(criteria);
        return gasStationIntegrationRepository.count(specification);
    }

    protected Specification<GasStationIntegration> createSpecification(GasStationIntegrationCriteria criteria) {
        Specification<GasStationIntegration> specification = Specification.where(null);
        if (criteria != null) {
            specification =
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                    criteria.getId() != null ? buildRangeSpecification(criteria.getId(), GasStationIntegration_.id) : null,
                    criteria.getTenantId() != null
                        ? buildStringSpecification(criteria.getTenantId(), GasStationIntegration_.tenantId)
                        : null,
                    criteria.getStationId() != null
                        ? buildRangeSpecification(criteria.getStationId(), GasStationIntegration_.stationId)
                        : null,
                    criteria.getSystemType() != null
                        ? buildSpecification(criteria.getSystemType(), GasStationIntegration_.systemType)
                        : null,
                    criteria.getConnectionMode() != null
                        ? buildSpecification(criteria.getConnectionMode(), GasStationIntegration_.connectionMode)
                        : null,
                    criteria.getIsEnabled() != null ? buildSpecification(criteria.getIsEnabled(), GasStationIntegration_.isEnabled) : null,
                    null
                );
        }
        return specification;
    }
}
