package com.eventsitemanager.service;

import com.eventsitemanager.domain.*;
import com.eventsitemanager.domain.GasStationRecommendation;
import com.eventsitemanager.repository.GasStationRecommendationRepository;
import com.eventsitemanager.service.criteria.GasStationRecommendationCriteria;
import com.eventsitemanager.service.dto.GasStationRecommendationDTO;
import com.eventsitemanager.service.mapper.GasStationRecommendationMapper;
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
public class GasStationRecommendationQueryService extends QueryService<GasStationRecommendation> {

    private static final Logger LOG = LoggerFactory.getLogger(GasStationRecommendationQueryService.class);

    private final GasStationRecommendationRepository gasStationRecommendationRepository;
    private final GasStationRecommendationMapper gasStationRecommendationMapper;

    public GasStationRecommendationQueryService(
        GasStationRecommendationRepository gasStationRecommendationRepository,
        GasStationRecommendationMapper gasStationRecommendationMapper
    ) {
        this.gasStationRecommendationRepository = gasStationRecommendationRepository;
        this.gasStationRecommendationMapper = gasStationRecommendationMapper;
    }

    public Page<GasStationRecommendationDTO> findByCriteria(GasStationRecommendationCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GasStationRecommendation> specification = createSpecification(criteria);
        return gasStationRecommendationRepository.findAll(specification, page).map(gasStationRecommendationMapper::toDto);
    }

    public long countByCriteria(GasStationRecommendationCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<GasStationRecommendation> specification = createSpecification(criteria);
        return gasStationRecommendationRepository.count(specification);
    }

    protected Specification<GasStationRecommendation> createSpecification(GasStationRecommendationCriteria criteria) {
        Specification<GasStationRecommendation> specification = Specification.where(null);
        if (criteria != null) {
            specification =
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                    criteria.getId() != null ? buildRangeSpecification(criteria.getId(), GasStationRecommendation_.id) : null,
                    criteria.getTenantId() != null
                        ? buildStringSpecification(criteria.getTenantId(), GasStationRecommendation_.tenantId)
                        : null,
                    criteria.getStationId() != null
                        ? buildRangeSpecification(criteria.getStationId(), GasStationRecommendation_.stationId)
                        : null,
                    criteria.getRecommendationDate() != null
                        ? buildRangeSpecification(criteria.getRecommendationDate(), GasStationRecommendation_.recommendationDate)
                        : null,
                    criteria.getCategory() != null ? buildSpecification(criteria.getCategory(), GasStationRecommendation_.category) : null,
                    criteria.getStatus() != null ? buildSpecification(criteria.getStatus(), GasStationRecommendation_.status) : null,
                    criteria.getPriority() != null
                        ? buildRangeSpecification(criteria.getPriority(), GasStationRecommendation_.priority)
                        : null,
                    null
                );
        }
        return specification;
    }
}
