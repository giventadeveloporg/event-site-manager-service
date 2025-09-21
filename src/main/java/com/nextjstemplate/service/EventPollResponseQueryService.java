package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.EventPollResponse;
import com.nextjstemplate.repository.EventPollResponseRepository;
import com.nextjstemplate.service.criteria.EventPollResponseCriteria;
import com.nextjstemplate.service.dto.EventPollResponseDTO;
import com.nextjstemplate.service.mapper.EventPollResponseMapper;
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
 * Service for executing complex queries for {@link EventPollResponse} entities in the database.
 * The main input is a {@link EventPollResponseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EventPollResponseDTO} or a {@link Page} of {@link EventPollResponseDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventPollResponseQueryService extends QueryService<EventPollResponse> {

    private final Logger log = LoggerFactory.getLogger(EventPollResponseQueryService.class);

    private final EventPollResponseRepository eventPollResponseRepository;

    private final EventPollResponseMapper eventPollResponseMapper;

    public EventPollResponseQueryService(
        EventPollResponseRepository eventPollResponseRepository,
        EventPollResponseMapper eventPollResponseMapper
    ) {
        this.eventPollResponseRepository = eventPollResponseRepository;
        this.eventPollResponseMapper = eventPollResponseMapper;
    }

    /**
     * Return a {@link List} of {@link EventPollResponseDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EventPollResponseDTO> findByCriteria(EventPollResponseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EventPollResponse> specification = createSpecification(criteria);
        return eventPollResponseMapper.toDto(eventPollResponseRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EventPollResponseDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EventPollResponseDTO> findByCriteria(EventPollResponseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EventPollResponse> specification = createSpecification(criteria);
        return eventPollResponseRepository.findAll(specification, page).map(eventPollResponseMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventPollResponseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EventPollResponse> specification = createSpecification(criteria);
        return eventPollResponseRepository.count(specification);
    }

    /**
     * Function to convert {@link EventPollResponseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EventPollResponse> createSpecification(EventPollResponseCriteria criteria) {
        Specification<EventPollResponse> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EventPollResponse_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), EventPollResponse_.tenantId));
            }
            if (criteria.getResponseValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getResponseValue(), EventPollResponse_.responseValue));
            }
            if (criteria.getIsAnonymous() != null) {
                specification = specification.and(buildSpecification(criteria.getIsAnonymous(), EventPollResponse_.isAnonymous));
            }
            if (criteria.getComment() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComment(), EventPollResponse_.comment));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), EventPollResponse_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), EventPollResponse_.updatedAt));
            }
            if (criteria.getPollId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPollId(),
                            root -> root.join(EventPollResponse_.poll, JoinType.LEFT).get(EventPoll_.id)
                        )
                    );
            }
            if (criteria.getPollOptionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPollOptionId(),
                            root -> root.join(EventPollResponse_.pollOption, JoinType.LEFT).get(EventPollOption_.id)
                        )
                    );
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUserId(),
                            root -> root.join(EventPollResponse_.user, JoinType.LEFT).get(UserProfile_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
