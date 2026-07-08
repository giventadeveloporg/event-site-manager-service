package com.eventsitemanager.service;

import com.eventsitemanager.domain.GasStationUserStationAssignment;
import com.eventsitemanager.repository.GasStationUserStationAssignmentRepository;
import com.eventsitemanager.service.criteria.GasStationUserStationAssignmentCriteria;
import com.eventsitemanager.service.dto.GasStationUserStationAssignmentDTO;
import com.eventsitemanager.service.mapper.GasStationUserStationAssignmentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

@Service
@Transactional(readOnly = true)
public class GasStationUserStationAssignmentQueryService extends QueryService<GasStationUserStationAssignment> {

    private static final Logger LOG = LoggerFactory.getLogger(GasStationUserStationAssignmentQueryService.class);

    private final GasStationUserStationAssignmentRepository assignmentRepository;
    private final GasStationUserStationAssignmentMapper assignmentMapper;
    private final GasStationAccessService gasStationAccessService;

    public GasStationUserStationAssignmentQueryService(
        GasStationUserStationAssignmentRepository assignmentRepository,
        GasStationUserStationAssignmentMapper assignmentMapper,
        GasStationAccessService gasStationAccessService
    ) {
        this.assignmentRepository = assignmentRepository;
        this.assignmentMapper = assignmentMapper;
        this.gasStationAccessService = gasStationAccessService;
    }

    public Page<GasStationUserStationAssignmentDTO> findByCriteria(GasStationUserStationAssignmentCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        if (criteria == null) {
            criteria = new GasStationUserStationAssignmentCriteria();
        }
        gasStationAccessService.applyAssignmentCriteriaFilter(criteria);
        final Specification<GasStationUserStationAssignment> specification = createSpecification(criteria);
        return assignmentRepository.findAll(specification, page).map(assignmentMapper::toDto);
    }

    public long countByCriteria(GasStationUserStationAssignmentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        if (criteria == null) {
            criteria = new GasStationUserStationAssignmentCriteria();
        }
        gasStationAccessService.applyAssignmentCriteriaFilter(criteria);
        final Specification<GasStationUserStationAssignment> specification = createSpecification(criteria);
        return assignmentRepository.count(specification);
    }

    protected Specification<GasStationUserStationAssignment> createSpecification(GasStationUserStationAssignmentCriteria criteria) {
        Specification<GasStationUserStationAssignment> specification = Specification.where(null);
        if (criteria != null) {
            specification =
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                    criteria.getId() != null ? buildLongSpecification(criteria.getId(), "id") : null,
                    criteria.getTenantId() != null ? buildStringSpecification(criteria.getTenantId(), "tenantId") : null,
                    criteria.getUserProfileId() != null ? buildLongSpecification(criteria.getUserProfileId(), "userProfileId") : null,
                    criteria.getStationId() != null ? buildLongSpecification(criteria.getStationId(), "stationId") : null,
                    null
                );
        }
        return specification;
    }

    private Specification<GasStationUserStationAssignment> buildLongSpecification(LongFilter filter, String field) {
        return (root, query, cb) -> {
            if (filter.getEquals() != null) {
                return cb.equal(root.get(field), filter.getEquals());
            }
            if (filter.getIn() != null && !filter.getIn().isEmpty()) {
                return root.get(field).in(filter.getIn());
            }
            return cb.conjunction();
        };
    }

    private Specification<GasStationUserStationAssignment> buildStringSpecification(StringFilter filter, String field) {
        return (root, query, cb) -> {
            if (filter.getEquals() != null) {
                return cb.equal(root.get(field), filter.getEquals());
            }
            if (filter.getContains() != null) {
                return cb.like(cb.lower(root.get(field)), "%" + filter.getContains().toLowerCase() + "%");
            }
            return cb.conjunction();
        };
    }
}
