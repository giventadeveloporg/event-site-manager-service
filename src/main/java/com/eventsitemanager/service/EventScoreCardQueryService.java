package com.eventsitemanager.service;

import com.eventsitemanager.domain.*; // for static metamodels
import com.eventsitemanager.domain.EventScoreCard;
import com.eventsitemanager.repository.EventScoreCardRepository;
import com.eventsitemanager.service.criteria.EventScoreCardCriteria;
import com.eventsitemanager.service.dto.EventScoreCardDTO;
import com.eventsitemanager.service.mapper.EventScoreCardMapper;
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
 * Service for executing complex queries for {@link EventScoreCard} entities in the database.
 * The main input is a {@link EventScoreCardCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EventScoreCardDTO} or a {@link Page} of {@link EventScoreCardDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventScoreCardQueryService extends QueryService<EventScoreCard> {

    private final Logger log = LoggerFactory.getLogger(EventScoreCardQueryService.class);

    private final EventScoreCardRepository eventScoreCardRepository;

    private final EventScoreCardMapper eventScoreCardMapper;

    public EventScoreCardQueryService(EventScoreCardRepository eventScoreCardRepository, EventScoreCardMapper eventScoreCardMapper) {
        this.eventScoreCardRepository = eventScoreCardRepository;
        this.eventScoreCardMapper = eventScoreCardMapper;
    }

    /**
     * Return a {@link List} of {@link EventScoreCardDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EventScoreCardDTO> findByCriteria(EventScoreCardCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EventScoreCard> specification = createSpecification(criteria);
        return eventScoreCardMapper.toDto(eventScoreCardRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EventScoreCardDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EventScoreCardDTO> findByCriteria(EventScoreCardCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EventScoreCard> specification = createSpecification(criteria);
        return eventScoreCardRepository.findAll(specification, page).map(eventScoreCardMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventScoreCardCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EventScoreCard> specification = createSpecification(criteria);
        return eventScoreCardRepository.count(specification);
    }

    /**
     * Function to convert {@link EventScoreCardCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EventScoreCard> createSpecification(EventScoreCardCriteria criteria) {
        Specification<EventScoreCard> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EventScoreCard_.id));
            }
            if (criteria.getTeamAName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTeamAName(), EventScoreCard_.teamAName));
            }
            if (criteria.getTeamBName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTeamBName(), EventScoreCard_.teamBName));
            }
            if (criteria.getTeamAScore() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTeamAScore(), EventScoreCard_.teamAScore));
            }
            if (criteria.getTeamBScore() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTeamBScore(), EventScoreCard_.teamBScore));
            }
            if (criteria.getRemarks() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRemarks(), EventScoreCard_.remarks));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), EventScoreCard_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), EventScoreCard_.updatedAt));
            }
            if (criteria.getEventId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEventId(),
                            root -> root.join(EventScoreCard_.event, JoinType.LEFT).get(EventDetails_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
