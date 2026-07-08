package com.eventsitemanager.service;

import com.eventsitemanager.domain.*;
import com.eventsitemanager.domain.GasStationLocation;
import com.eventsitemanager.repository.GasStationLocationRepository;
import com.eventsitemanager.service.criteria.GasStationLocationCriteria;
import com.eventsitemanager.service.dto.GasStationLocationDTO;
import com.eventsitemanager.service.mapper.GasStationLocationMapper;
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
public class GasStationLocationQueryService extends QueryService<GasStationLocation> {

    private static final Logger LOG = LoggerFactory.getLogger(GasStationLocationQueryService.class);

    private final GasStationLocationRepository gasStationLocationRepository;
    private final GasStationLocationMapper gasStationLocationMapper;
    private final GasStationAccessService gasStationAccessService;

    public GasStationLocationQueryService(
        GasStationLocationRepository gasStationLocationRepository,
        GasStationLocationMapper gasStationLocationMapper,
        GasStationAccessService gasStationAccessService
    ) {
        this.gasStationLocationRepository = gasStationLocationRepository;
        this.gasStationLocationMapper = gasStationLocationMapper;
        this.gasStationAccessService = gasStationAccessService;
    }

    public Page<GasStationLocationDTO> findByCriteria(GasStationLocationCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        if (criteria == null) {
            criteria = new GasStationLocationCriteria();
        }
        gasStationAccessService.applyLocationCriteriaFilter(criteria);
        final Specification<GasStationLocation> specification = createSpecification(criteria);
        return gasStationLocationRepository.findAll(specification, page).map(gasStationLocationMapper::toDto);
    }

    public long countByCriteria(GasStationLocationCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        if (criteria == null) {
            criteria = new GasStationLocationCriteria();
        }
        gasStationAccessService.applyLocationCriteriaFilter(criteria);
        final Specification<GasStationLocation> specification = createSpecification(criteria);
        return gasStationLocationRepository.count(specification);
    }

    protected Specification<GasStationLocation> createSpecification(GasStationLocationCriteria criteria) {
        Specification<GasStationLocation> specification = Specification.where(null);
        if (criteria != null) {
            specification =
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                    criteria.getId() != null ? buildRangeSpecification(criteria.getId(), GasStationLocation_.id) : null,
                    criteria.getTenantId() != null ? buildStringSpecification(criteria.getTenantId(), GasStationLocation_.tenantId) : null,
                    criteria.getStationName() != null
                        ? buildStringSpecification(criteria.getStationName(), GasStationLocation_.stationName)
                        : null,
                    criteria.getStationCode() != null
                        ? buildStringSpecification(criteria.getStationCode(), GasStationLocation_.stationCode)
                        : null,
                    criteria.getBrand() != null ? buildStringSpecification(criteria.getBrand(), GasStationLocation_.brand) : null,
                    criteria.getRegion() != null ? buildStringSpecification(criteria.getRegion(), GasStationLocation_.region) : null,
                    criteria.getCity() != null ? buildStringSpecification(criteria.getCity(), GasStationLocation_.city) : null,
                    criteria.getIsActive() != null ? buildSpecification(criteria.getIsActive(), GasStationLocation_.isActive) : null,
                    null
                );
        }
        return specification;
    }
}
