package com.eventsitemanager.service;

import com.eventsitemanager.domain.*; // for static metamodels
import com.eventsitemanager.domain.EventScoreCardDetail;
import com.eventsitemanager.repository.EventScoreCardDetailRepository;
import com.eventsitemanager.service.criteria.EventScoreCardDetailCriteria;
import com.eventsitemanager.service.dto.EventScoreCardDetailDTO;
import com.eventsitemanager.service.mapper.EventScoreCardDetailMapper;
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
 * Service for executing complex queries for {@link EventScoreCardDetail} entities in the database.
 * The main input is a {@link EventScoreCardDetailCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EventScoreCardDetailDTO} or a {@link Page} of {@link EventScoreCardDetailDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventScoreCardDetailQueryService extends QueryService<EventScoreCardDetail> {

    private final Logger log = LoggerFactory.getLogger(EventScoreCardDetailQueryService.class);

    private final EventScoreCardDetailRepository eventScoreCardDetailRepository;

    private final EventScoreCardDetailMapper eventScoreCardDetailMapper;

    public EventScoreCardDetailQueryService(
        EventScoreCardDetailRepository eventScoreCardDetailRepository,
        EventScoreCardDetailMapper eventScoreCardDetailMapper
    ) {
        this.eventScoreCardDetailRepository = eventScoreCardDetailRepository;
        this.eventScoreCardDetailMapper = eventScoreCardDetailMapper;
    }

    /**
     * Return a {@link List} of {@link EventScoreCardDetailDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EventScoreCardDetailDTO> findByCriteria(EventScoreCardDetailCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EventScoreCardDetail> specification = createSpecification(criteria);
        return eventScoreCardDetailMapper.toDto(eventScoreCardDetailRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EventScoreCardDetailDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EventScoreCardDetailDTO> findByCriteria(EventScoreCardDetailCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EventScoreCardDetail> specification = createSpecification(criteria);
        return eventScoreCardDetailRepository.findAll(specification, page).map(eventScoreCardDetailMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventScoreCardDetailCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EventScoreCardDetail> specification = createSpecification(criteria);
        return eventScoreCardDetailRepository.count(specification);
    }

    /**
     * Function to convert {@link EventScoreCardDetailCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EventScoreCardDetail> createSpecification(EventScoreCardDetailCriteria criteria) {
        Specification<EventScoreCardDetail> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EventScoreCardDetail_.id));
            }
            if (criteria.getTeamName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTeamName(), EventScoreCardDetail_.teamName));
            }
            if (criteria.getPlayerName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPlayerName(), EventScoreCardDetail_.playerName));
            }
            if (criteria.getPoints() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPoints(), EventScoreCardDetail_.points));
            }
            if (criteria.getRemarks() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRemarks(), EventScoreCardDetail_.remarks));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), EventScoreCardDetail_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), EventScoreCardDetail_.updatedAt));
            }
            if (criteria.getScoreCardId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getScoreCardId(),
                            root -> root.join(EventScoreCardDetail_.scoreCard, JoinType.LEFT).get(EventScoreCard_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
