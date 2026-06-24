package com.eventsitemanager.service;

import com.eventsitemanager.domain.*; // for static metamodels
import com.eventsitemanager.domain.QrCodeUsage;
import com.eventsitemanager.repository.QrCodeUsageRepository;
import com.eventsitemanager.service.criteria.QrCodeUsageCriteria;
import com.eventsitemanager.service.dto.QrCodeUsageDTO;
import com.eventsitemanager.service.mapper.QrCodeUsageMapper;
import jakarta.persistence.criteria.JoinType;
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
 * Service for executing complex queries for {@link QrCodeUsage} entities in the database.
 * The main input is a {@link QrCodeUsageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link QrCodeUsageDTO} or a {@link Page} of {@link QrCodeUsageDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QrCodeUsageQueryService extends QueryService<QrCodeUsage> {

    private final Logger log = LoggerFactory.getLogger(QrCodeUsageQueryService.class);

    private final QrCodeUsageRepository qrCodeUsageRepository;

    private final QrCodeUsageMapper qrCodeUsageMapper;

    public QrCodeUsageQueryService(QrCodeUsageRepository qrCodeUsageRepository, QrCodeUsageMapper qrCodeUsageMapper) {
        this.qrCodeUsageRepository = qrCodeUsageRepository;
        this.qrCodeUsageMapper = qrCodeUsageMapper;
    }

    /**
     * Return a {@link List} of {@link QrCodeUsageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<QrCodeUsageDTO> findByCriteria(QrCodeUsageCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<QrCodeUsage> specification = createSpecification(criteria);
        return qrCodeUsageMapper.toDto(qrCodeUsageRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link QrCodeUsageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<QrCodeUsageDTO> findByCriteria(QrCodeUsageCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<QrCodeUsage> specification = createSpecification(criteria);
        return qrCodeUsageRepository.findAll(specification, page).map(qrCodeUsageMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(QrCodeUsageCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<QrCodeUsage> specification = createSpecification(criteria);
        return qrCodeUsageRepository.count(specification);
    }

    /**
     * Function to convert {@link QrCodeUsageCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<QrCodeUsage> createSpecification(QrCodeUsageCriteria criteria) {
        Specification<QrCodeUsage> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), QrCodeUsage_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), QrCodeUsage_.tenantId));
            }
            if (criteria.getQrCodeData() != null) {
                specification = specification.and(buildStringSpecification(criteria.getQrCodeData(), QrCodeUsage_.qrCodeData));
            }
            if (criteria.getGeneratedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGeneratedAt(), QrCodeUsage_.generatedAt));
            }
            if (criteria.getUsedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUsedAt(), QrCodeUsage_.usedAt));
            }
            if (criteria.getUsageCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUsageCount(), QrCodeUsage_.usageCount));
            }
            if (criteria.getLastScannedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastScannedBy(), QrCodeUsage_.lastScannedBy));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), QrCodeUsage_.createdAt));
            }
            if (criteria.getAttendeeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAttendeeId(),
                            root -> root.join(QrCodeUsage_.attendee, JoinType.LEFT).get(EventAttendee_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
