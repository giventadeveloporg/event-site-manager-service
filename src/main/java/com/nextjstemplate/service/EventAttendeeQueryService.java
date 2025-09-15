package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.EventAttendee;
import com.nextjstemplate.repository.EventAttendeeRepository;
import com.nextjstemplate.service.criteria.EventAttendeeCriteria;
import com.nextjstemplate.service.dto.EventAttendeeDTO;
import com.nextjstemplate.service.mapper.EventAttendeeMapper;
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
 * Service for executing complex queries for {@link EventAttendee} entities in the database.
 * The main input is a {@link EventAttendeeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EventAttendeeDTO} or a {@link Page} of {@link EventAttendeeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventAttendeeQueryService extends QueryService<EventAttendee> {

    private final Logger log = LoggerFactory.getLogger(EventAttendeeQueryService.class);

    private final EventAttendeeRepository eventAttendeeRepository;

    private final EventAttendeeMapper eventAttendeeMapper;

    public EventAttendeeQueryService(EventAttendeeRepository eventAttendeeRepository, EventAttendeeMapper eventAttendeeMapper) {
        this.eventAttendeeRepository = eventAttendeeRepository;
        this.eventAttendeeMapper = eventAttendeeMapper;
    }

    /**
     * Return a {@link List} of {@link EventAttendeeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EventAttendeeDTO> findByCriteria(EventAttendeeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EventAttendee> specification = createSpecification(criteria);
        return eventAttendeeMapper.toDto(eventAttendeeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EventAttendeeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EventAttendeeDTO> findByCriteria(EventAttendeeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EventAttendee> specification = createSpecification(criteria);
        return eventAttendeeRepository.findAll(specification, page).map(eventAttendeeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventAttendeeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EventAttendee> specification = createSpecification(criteria);
        return eventAttendeeRepository.count(specification);
    }

    /**
     * Function to convert {@link EventAttendeeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EventAttendee> createSpecification(EventAttendeeCriteria criteria) {
        Specification<EventAttendee> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EventAttendee_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), EventAttendee_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), EventAttendee_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), EventAttendee_.email));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), EventAttendee_.phone));
            }
            if (criteria.getIsMember() != null) {
                specification = specification.and(buildSpecification(criteria.getIsMember(), EventAttendee_.isMember));
            }
            if (criteria.getEventId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEventId(), EventAttendee_.eventId));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserId(), EventAttendee_.userId));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), EventAttendee_.tenantId));
            }
            if (criteria.getRegistrationStatus() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getRegistrationStatus(), EventAttendee_.registrationStatus));
            }
            if (criteria.getRegistrationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRegistrationDate(), EventAttendee_.registrationDate));
            }
            if (criteria.getConfirmationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getConfirmationDate(), EventAttendee_.confirmationDate));
            }
            if (criteria.getCancellationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCancellationDate(), EventAttendee_.cancellationDate));
            }
            if (criteria.getCancellationReason() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getCancellationReason(), EventAttendee_.cancellationReason));
            }
            if (criteria.getAttendeeType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAttendeeType(), EventAttendee_.attendeeType));
            }
            if (criteria.getSpecialRequirements() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getSpecialRequirements(), EventAttendee_.specialRequirements));
            }
            if (criteria.getEmergencyContactName() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getEmergencyContactName(), EventAttendee_.emergencyContactName));
            }
            if (criteria.getEmergencyContactPhone() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getEmergencyContactPhone(), EventAttendee_.emergencyContactPhone));
            }
            if (criteria.getCheckInStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCheckInStatus(), EventAttendee_.checkInStatus));
            }
            if (criteria.getCheckInTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCheckInTime(), EventAttendee_.checkInTime));
            }
            if (criteria.getTotalNumberOfGuests() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTotalNumberOfGuests(), EventAttendee_.totalNumberOfGuests));
            }
            if (criteria.getNumberOfGuestsCheckedIn() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getNumberOfGuestsCheckedIn(), EventAttendee_.numberOfGuestsCheckedIn)
                    );
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), EventAttendee_.notes));
            }
            if (criteria.getQrCodeData() != null) {
                specification = specification.and(buildStringSpecification(criteria.getQrCodeData(), EventAttendee_.qrCodeData));
            }
            if (criteria.getQrCodeGenerated() != null) {
                specification = specification.and(buildSpecification(criteria.getQrCodeGenerated(), EventAttendee_.qrCodeGenerated));
            }
            if (criteria.getQrCodeGeneratedAt() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getQrCodeGeneratedAt(), EventAttendee_.qrCodeGeneratedAt));
            }
            if (criteria.getDietaryRestrictions() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getDietaryRestrictions(), EventAttendee_.dietaryRestrictions));
            }
            if (criteria.getAccessibilityNeeds() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAccessibilityNeeds(), EventAttendee_.accessibilityNeeds));
            }
            if (criteria.getEmergencyContactRelationship() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getEmergencyContactRelationship(), EventAttendee_.emergencyContactRelationship)
                    );
            }
            if (criteria.getCheckOutTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCheckOutTime(), EventAttendee_.checkOutTime));
            }
            if (criteria.getAttendanceRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAttendanceRating(), EventAttendee_.attendanceRating));
            }
            if (criteria.getFeedback() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFeedback(), EventAttendee_.feedback));
            }
            if (criteria.getRegistrationSource() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getRegistrationSource(), EventAttendee_.registrationSource));
            }
            if (criteria.getWaitListPosition() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWaitListPosition(), EventAttendee_.waitListPosition));
            }
            if (criteria.getPriorityScore() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPriorityScore(), EventAttendee_.priorityScore));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), EventAttendee_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), EventAttendee_.updatedAt));
            }
        }
        return specification;
    }
}
