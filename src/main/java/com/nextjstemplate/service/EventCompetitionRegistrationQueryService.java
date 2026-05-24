package com.nextjstemplate.service;

import com.nextjstemplate.domain.*;
import com.nextjstemplate.domain.EventCompetitionRegistration;
import com.nextjstemplate.repository.EventCompetitionRegistrationRepository;
import com.nextjstemplate.service.criteria.EventCompetitionRegistrationCriteria;
import com.nextjstemplate.service.dto.EventCompetitionRegistrationDTO;
import com.nextjstemplate.service.mapper.EventCompetitionRegistrationMapper;
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
public class EventCompetitionRegistrationQueryService extends QueryService<EventCompetitionRegistration> {

    private final Logger log = LoggerFactory.getLogger(EventCompetitionRegistrationQueryService.class);

    private final EventCompetitionRegistrationRepository eventCompetitionRegistrationRepository;

    private final EventCompetitionRegistrationMapper eventCompetitionRegistrationMapper;

    public EventCompetitionRegistrationQueryService(EventCompetitionRegistrationRepository eventCompetitionRegistrationRepository, EventCompetitionRegistrationMapper eventCompetitionRegistrationMapper) {
        this.eventCompetitionRegistrationRepository = eventCompetitionRegistrationRepository;
        this.eventCompetitionRegistrationMapper = eventCompetitionRegistrationMapper;
    }

    public List<EventCompetitionRegistrationDTO> findByCriteria(EventCompetitionRegistrationCriteria criteria) {
        final Specification<EventCompetitionRegistration> specification = createSpecification(criteria);
        return eventCompetitionRegistrationMapper.toDto(eventCompetitionRegistrationRepository.findAll(specification));
    }

    public Page<EventCompetitionRegistrationDTO> findByCriteria(EventCompetitionRegistrationCriteria criteria, Pageable page) {
        final Specification<EventCompetitionRegistration> specification = createSpecification(criteria);
        return eventCompetitionRegistrationRepository.findAll(specification, page).map(eventCompetitionRegistrationMapper::toDto);
    }

    public long countByCriteria(EventCompetitionRegistrationCriteria criteria) {
        return eventCompetitionRegistrationRepository.count(createSpecification(criteria));
    }

    protected Specification<EventCompetitionRegistration> createSpecification(EventCompetitionRegistrationCriteria criteria) {
        Specification<EventCompetitionRegistration> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EventCompetitionRegistration_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), EventCompetitionRegistration_.tenantId));
            }
            if (criteria.getRegistrationStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getRegistrationStatus(), EventCompetitionRegistration_.registrationStatus));
            }
            if (criteria.getFeeAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFeeAmount(), EventCompetitionRegistration_.feeAmount));
            }
            if (criteria.getEffectiveCategory() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEffectiveCategory(), EventCompetitionRegistration_.effectiveCategory));
            }
            if (criteria.getStripePaymentIntentId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStripePaymentIntentId(), EventCompetitionRegistration_.stripePaymentIntentId));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), EventCompetitionRegistration_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), EventCompetitionRegistration_.updatedAt));
            }
            if (criteria.getEventId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEventId(), root -> root.join(EventCompetitionRegistration_.event, JoinType.LEFT).get(EventDetails_.id))
                    );
            }
            if (criteria.getCompetitionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCompetitionId(), root -> root.join(EventCompetitionRegistration_.competition, JoinType.LEFT).get(EventCompetition_.id))
                    );
            }
            if (criteria.getParticipantProfileId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getParticipantProfileId(), root -> root.join(EventCompetitionRegistration_.participantProfile, JoinType.LEFT).get(EventCompetitionParticipant_.id))
                    );
            }
            if (criteria.getRegisteredByUserProfileId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getRegisteredByUserProfileId(), root -> root.join(EventCompetitionRegistration_.registeredByUserProfile, JoinType.LEFT).get(UserProfile_.id))
                    );
            }
            if (criteria.getGroupLeaderRegistrationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getGroupLeaderRegistrationId(), root -> root.join(EventCompetitionRegistration_.groupLeaderRegistration, JoinType.LEFT).get(EventCompetitionRegistration_.id))
                    );
            }
        }
        return specification;
    }
}
