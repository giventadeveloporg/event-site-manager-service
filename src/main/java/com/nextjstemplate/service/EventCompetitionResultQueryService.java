package com.nextjstemplate.service;

import com.nextjstemplate.domain.*;
import com.nextjstemplate.domain.EventCompetitionResult;
import com.nextjstemplate.repository.EventCompetitionResultRepository;
import com.nextjstemplate.service.criteria.EventCompetitionResultCriteria;
import com.nextjstemplate.service.dto.EventCompetitionResultDTO;
import com.nextjstemplate.service.mapper.EventCompetitionResultMapper;
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
public class EventCompetitionResultQueryService extends QueryService<EventCompetitionResult> {

    private final Logger log = LoggerFactory.getLogger(EventCompetitionResultQueryService.class);

    private final EventCompetitionResultRepository eventCompetitionResultRepository;

    private final EventCompetitionResultMapper eventCompetitionResultMapper;

    public EventCompetitionResultQueryService(EventCompetitionResultRepository eventCompetitionResultRepository, EventCompetitionResultMapper eventCompetitionResultMapper) {
        this.eventCompetitionResultRepository = eventCompetitionResultRepository;
        this.eventCompetitionResultMapper = eventCompetitionResultMapper;
    }

    public List<EventCompetitionResultDTO> findByCriteria(EventCompetitionResultCriteria criteria) {
        final Specification<EventCompetitionResult> specification = createSpecification(criteria);
        return eventCompetitionResultMapper.toDto(eventCompetitionResultRepository.findAll(specification));
    }

    public Page<EventCompetitionResultDTO> findByCriteria(EventCompetitionResultCriteria criteria, Pageable page) {
        final Specification<EventCompetitionResult> specification = createSpecification(criteria);
        return eventCompetitionResultRepository.findAll(specification, page).map(eventCompetitionResultMapper::toDto);
    }

    public long countByCriteria(EventCompetitionResultCriteria criteria) {
        return eventCompetitionResultRepository.count(createSpecification(criteria));
    }

    protected Specification<EventCompetitionResult> createSpecification(EventCompetitionResultCriteria criteria) {
        Specification<EventCompetitionResult> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EventCompetitionResult_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), EventCompetitionResult_.tenantId));
            }
            if (criteria.getDisplayName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDisplayName(), EventCompetitionResult_.displayName));
            }
            if (criteria.getPlacement() != null) {
                specification = specification.and(buildSpecification(criteria.getPlacement(), EventCompetitionResult_.placement));
            }
            if (criteria.getPlacementLabel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPlacementLabel(), EventCompetitionResult_.placementLabel));
            }
            if (criteria.getPrizeTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrizeTitle(), EventCompetitionResult_.prizeTitle));
            }
            if (criteria.getPrizeDetails() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrizeDetails(), EventCompetitionResult_.prizeDetails));
            }
            if (criteria.getPointsAwarded() != null) {
                specification = specification.and(buildSpecification(criteria.getPointsAwarded(), EventCompetitionResult_.pointsAwarded));
            }
            if (criteria.getWinnerPhotoUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getWinnerPhotoUrl(), EventCompetitionResult_.winnerPhotoUrl));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), EventCompetitionResult_.notes));
            }
            if (criteria.getIsPublished() != null) {
                specification = specification.and(buildSpecification(criteria.getIsPublished(), EventCompetitionResult_.isPublished));
            }
            if (criteria.getPublishedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPublishedAt(), EventCompetitionResult_.publishedAt));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), EventCompetitionResult_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), EventCompetitionResult_.updatedAt));
            }
            if (criteria.getEventId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEventId(), root -> root.join(EventCompetitionResult_.event, JoinType.LEFT).get(EventDetails_.id))
                    );
            }
            if (criteria.getCompetitionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCompetitionId(), root -> root.join(EventCompetitionResult_.competition, JoinType.LEFT).get(EventCompetition_.id))
                    );
            }
            if (criteria.getParticipantProfileId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getParticipantProfileId(), root -> root.join(EventCompetitionResult_.participantProfile, JoinType.LEFT).get(EventCompetitionParticipant_.id))
                    );
            }
            if (criteria.getRegistrationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getRegistrationId(), root -> root.join(EventCompetitionResult_.registration, JoinType.LEFT).get(EventCompetitionRegistration_.id))
                    );
            }
            if (criteria.getWinnerMediaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getWinnerMediaId(), root -> root.join(EventCompetitionResult_.winnerMedia, JoinType.LEFT).get(EventMedia_.id))
                    );
            }
        }
        return specification;
    }
}
