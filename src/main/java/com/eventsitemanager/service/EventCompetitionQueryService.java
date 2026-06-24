package com.eventsitemanager.service;

import com.eventsitemanager.domain.*;
import com.eventsitemanager.domain.EventCompetition;
import com.eventsitemanager.repository.EventCompetitionRepository;
import com.eventsitemanager.service.criteria.EventCompetitionCriteria;
import com.eventsitemanager.service.dto.EventCompetitionDTO;
import com.eventsitemanager.service.mapper.EventCompetitionMapper;
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

@Service
@Transactional(readOnly = true)
public class EventCompetitionQueryService extends QueryService<EventCompetition> {

    private final Logger log = LoggerFactory.getLogger(EventCompetitionQueryService.class);

    private final EventCompetitionRepository eventCompetitionRepository;

    private final EventCompetitionMapper eventCompetitionMapper;

    public EventCompetitionQueryService(
        EventCompetitionRepository eventCompetitionRepository,
        EventCompetitionMapper eventCompetitionMapper
    ) {
        this.eventCompetitionRepository = eventCompetitionRepository;
        this.eventCompetitionMapper = eventCompetitionMapper;
    }

    public List<EventCompetitionDTO> findByCriteria(EventCompetitionCriteria criteria) {
        final Specification<EventCompetition> specification = createSpecification(criteria);
        return eventCompetitionMapper.toDto(eventCompetitionRepository.findAll(specification));
    }

    public Page<EventCompetitionDTO> findByCriteria(EventCompetitionCriteria criteria, Pageable page) {
        final Specification<EventCompetition> specification = createSpecification(criteria);
        return eventCompetitionRepository.findAll(specification, page).map(eventCompetitionMapper::toDto);
    }

    public long countByCriteria(EventCompetitionCriteria criteria) {
        return eventCompetitionRepository.count(createSpecification(criteria));
    }

    protected Specification<EventCompetition> createSpecification(EventCompetitionCriteria criteria) {
        Specification<EventCompetition> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EventCompetition_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), EventCompetition_.tenantId));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), EventCompetition_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), EventCompetition_.description));
            }
            if (criteria.getCompetitionType() != null) {
                specification = specification.and(buildSpecification(criteria.getCompetitionType(), EventCompetition_.competitionType));
            }
            if (criteria.getEligibleAudience() != null) {
                specification = specification.and(buildSpecification(criteria.getEligibleAudience(), EventCompetition_.eligibleAudience));
            }
            if (criteria.getCategoryCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCategoryCode(), EventCompetition_.categoryCode));
            }
            if (criteria.getDivisionLabel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDivisionLabel(), EventCompetition_.divisionLabel));
            }
            if (criteria.getTrack() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTrack(), EventCompetition_.track));
            }
            if (criteria.getFeeAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFeeAmount(), EventCompetition_.feeAmount));
            }
            if (criteria.getMaxParticipants() != null) {
                specification = specification.and(buildSpecification(criteria.getMaxParticipants(), EventCompetition_.maxParticipants));
            }
            if (criteria.getMinGroupSize() != null) {
                specification = specification.and(buildSpecification(criteria.getMinGroupSize(), EventCompetition_.minGroupSize));
            }
            if (criteria.getMaxGroupSize() != null) {
                specification = specification.and(buildSpecification(criteria.getMaxGroupSize(), EventCompetition_.maxGroupSize));
            }
            if (criteria.getTimeLimitMinutes() != null) {
                specification = specification.and(buildSpecification(criteria.getTimeLimitMinutes(), EventCompetition_.timeLimitMinutes));
            }
            if (criteria.getRequiresSoundtrack() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getRequiresSoundtrack(), EventCompetition_.requiresSoundtrack));
            }
            if (criteria.getJudgmentCriteriaJson() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getJudgmentCriteriaJson(), EventCompetition_.judgmentCriteriaJson));
            }
            if (criteria.getDisplayOrder() != null) {
                specification = specification.and(buildSpecification(criteria.getDisplayOrder(), EventCompetition_.displayOrder));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), EventCompetition_.isActive));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), EventCompetition_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), EventCompetition_.updatedAt));
            }
            if (criteria.getEventId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEventId(),
                            root -> root.join(EventCompetition_.event, JoinType.LEFT).get(EventDetails_.id)
                        )
                    );
            }
            if (criteria.getCompetitionDayId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCompetitionDayId(),
                            root -> root.join(EventCompetition_.competitionDay, JoinType.LEFT).get(EventCompetitionDay_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
