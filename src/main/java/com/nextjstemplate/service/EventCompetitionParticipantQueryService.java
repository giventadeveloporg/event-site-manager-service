package com.nextjstemplate.service;

import com.nextjstemplate.domain.*;
import com.nextjstemplate.domain.EventCompetitionParticipant;
import com.nextjstemplate.repository.EventCompetitionParticipantRepository;
import com.nextjstemplate.service.criteria.EventCompetitionParticipantCriteria;
import com.nextjstemplate.service.dto.EventCompetitionParticipantDTO;
import com.nextjstemplate.service.mapper.EventCompetitionParticipantMapper;
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
public class EventCompetitionParticipantQueryService extends QueryService<EventCompetitionParticipant> {

    private final Logger log = LoggerFactory.getLogger(EventCompetitionParticipantQueryService.class);

    private final EventCompetitionParticipantRepository eventCompetitionParticipantRepository;

    private final EventCompetitionParticipantMapper eventCompetitionParticipantMapper;

    public EventCompetitionParticipantQueryService(EventCompetitionParticipantRepository eventCompetitionParticipantRepository, EventCompetitionParticipantMapper eventCompetitionParticipantMapper) {
        this.eventCompetitionParticipantRepository = eventCompetitionParticipantRepository;
        this.eventCompetitionParticipantMapper = eventCompetitionParticipantMapper;
    }

    public List<EventCompetitionParticipantDTO> findByCriteria(EventCompetitionParticipantCriteria criteria) {
        final Specification<EventCompetitionParticipant> specification = createSpecification(criteria);
        return eventCompetitionParticipantMapper.toDto(eventCompetitionParticipantRepository.findAll(specification));
    }

    public Page<EventCompetitionParticipantDTO> findByCriteria(EventCompetitionParticipantCriteria criteria, Pageable page) {
        final Specification<EventCompetitionParticipant> specification = createSpecification(criteria);
        return eventCompetitionParticipantRepository.findAll(specification, page).map(eventCompetitionParticipantMapper::toDto);
    }

    public long countByCriteria(EventCompetitionParticipantCriteria criteria) {
        return eventCompetitionParticipantRepository.count(createSpecification(criteria));
    }

    protected Specification<EventCompetitionParticipant> createSpecification(EventCompetitionParticipantCriteria criteria) {
        Specification<EventCompetitionParticipant> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EventCompetitionParticipant_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), EventCompetitionParticipant_.tenantId));
            }
            if (criteria.getParticipantType() != null) {
                specification = specification.and(buildSpecification(criteria.getParticipantType(), EventCompetitionParticipant_.participantType));
            }
            if (criteria.getClerkUserId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getClerkUserId(), EventCompetitionParticipant_.clerkUserId));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), EventCompetitionParticipant_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), EventCompetitionParticipant_.lastName));
            }
            if (criteria.getDisplayName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDisplayName(), EventCompetitionParticipant_.displayName));
            }
            if (criteria.getDateOfBirth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateOfBirth(), EventCompetitionParticipant_.dateOfBirth));
            }
            if (criteria.getCurrentGrade() != null) {
                specification = specification.and(buildSpecification(criteria.getCurrentGrade(), EventCompetitionParticipant_.currentGrade));
            }
            if (criteria.getSchoolName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSchoolName(), EventCompetitionParticipant_.schoolName));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), EventCompetitionParticipant_.phone));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), EventCompetitionParticipant_.email));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), EventCompetitionParticipant_.isActive));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), EventCompetitionParticipant_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), EventCompetitionParticipant_.updatedAt));
            }
            if (criteria.getUserProfileId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserProfileId(), root -> root.join(EventCompetitionParticipant_.userProfile, JoinType.LEFT).get(UserProfile_.id))
                    );
            }
            if (criteria.getGuardianUserProfileId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getGuardianUserProfileId(), root -> root.join(EventCompetitionParticipant_.guardianUserProfile, JoinType.LEFT).get(UserProfile_.id))
                    );
            }
        }
        return specification;
    }
}
