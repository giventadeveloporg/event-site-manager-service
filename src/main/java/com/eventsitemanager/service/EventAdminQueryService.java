package com.eventsitemanager.service;

import com.eventsitemanager.domain.*; // for static metamodels
import com.eventsitemanager.domain.EventAdmin;
import com.eventsitemanager.repository.EventAdminRepository;
import com.eventsitemanager.service.criteria.EventAdminCriteria;
import com.eventsitemanager.service.dto.EventAdminDTO;
import com.eventsitemanager.service.mapper.EventAdminMapper;
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
 * Service for executing complex queries for {@link EventAdmin} entities in the database.
 * The main input is a {@link EventAdminCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EventAdminDTO} or a {@link Page} of {@link EventAdminDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventAdminQueryService extends QueryService<EventAdmin> {

    private final Logger log = LoggerFactory.getLogger(EventAdminQueryService.class);

    private final EventAdminRepository eventAdminRepository;

    private final EventAdminMapper eventAdminMapper;

    public EventAdminQueryService(EventAdminRepository eventAdminRepository, EventAdminMapper eventAdminMapper) {
        this.eventAdminRepository = eventAdminRepository;
        this.eventAdminMapper = eventAdminMapper;
    }

    /**
     * Return a {@link List} of {@link EventAdminDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EventAdminDTO> findByCriteria(EventAdminCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EventAdmin> specification = createSpecification(criteria);
        return eventAdminMapper.toDto(eventAdminRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EventAdminDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EventAdminDTO> findByCriteria(EventAdminCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EventAdmin> specification = createSpecification(criteria);
        return eventAdminRepository.findAll(specification, page).map(eventAdminMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventAdminCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EventAdmin> specification = createSpecification(criteria);
        return eventAdminRepository.count(specification);
    }

    /**
     * Function to convert {@link EventAdminCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EventAdmin> createSpecification(EventAdminCriteria criteria) {
        Specification<EventAdmin> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EventAdmin_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), EventAdmin_.tenantId));
            }
            if (criteria.getRole() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRole(), EventAdmin_.role));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), EventAdmin_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), EventAdmin_.updatedAt));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(EventAdmin_.user, JoinType.LEFT).get(UserProfile_.id))
                    );
            }
            if (criteria.getCreatedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCreatedById(),
                            root -> root.join(EventAdmin_.createdBy, JoinType.LEFT).get(UserProfile_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
