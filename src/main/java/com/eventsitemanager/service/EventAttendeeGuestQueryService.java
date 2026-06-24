package com.eventsitemanager.service;

import com.eventsitemanager.domain.*; // for static metamodels
import com.eventsitemanager.domain.EventAttendeeGuest;
import com.eventsitemanager.repository.EventAttendeeGuestRepository;
import com.eventsitemanager.service.criteria.EventAttendeeGuestCriteria;
import com.eventsitemanager.service.dto.EventAttendeeGuestDTO;
import com.eventsitemanager.service.mapper.EventAttendeeGuestMapper;
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
 * Service for executing complex queries for {@link EventAttendeeGuest} entities in the database.
 * The main input is a {@link EventAttendeeGuestCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EventAttendeeGuestDTO} or a {@link Page} of {@link EventAttendeeGuestDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventAttendeeGuestQueryService extends QueryService<EventAttendeeGuest> {

    private final Logger log = LoggerFactory.getLogger(EventAttendeeGuestQueryService.class);

    private final EventAttendeeGuestRepository eventAttendeeGuestRepository;

    private final EventAttendeeGuestMapper eventAttendeeGuestMapper;

    public EventAttendeeGuestQueryService(
        EventAttendeeGuestRepository eventAttendeeGuestRepository,
        EventAttendeeGuestMapper eventAttendeeGuestMapper
    ) {
        this.eventAttendeeGuestRepository = eventAttendeeGuestRepository;
        this.eventAttendeeGuestMapper = eventAttendeeGuestMapper;
    }

    /**
     * Return a {@link List} of {@link EventAttendeeGuestDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EventAttendeeGuestDTO> findByCriteria(EventAttendeeGuestCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EventAttendeeGuest> specification = createSpecification(criteria);
        return eventAttendeeGuestMapper.toDto(eventAttendeeGuestRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EventAttendeeGuestDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EventAttendeeGuestDTO> findByCriteria(EventAttendeeGuestCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EventAttendeeGuest> specification = createSpecification(criteria);
        return eventAttendeeGuestRepository.findAll(specification, page).map(eventAttendeeGuestMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventAttendeeGuestCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EventAttendeeGuest> specification = createSpecification(criteria);
        return eventAttendeeGuestRepository.count(specification);
    }

    /**
     * Function to convert {@link EventAttendeeGuestCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EventAttendeeGuest> createSpecification(EventAttendeeGuestCriteria criteria) {
        Specification<EventAttendeeGuest> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EventAttendeeGuest_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), EventAttendeeGuest_.tenantId));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), EventAttendeeGuest_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), EventAttendeeGuest_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), EventAttendeeGuest_.email));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), EventAttendeeGuest_.phone));
            }
            if (criteria.getAgeGroup() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAgeGroup(), EventAttendeeGuest_.ageGroup));
            }
            if (criteria.getRelationship() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRelationship(), EventAttendeeGuest_.relationship));
            }
            if (criteria.getSpecialRequirements() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getSpecialRequirements(), EventAttendeeGuest_.specialRequirements));
            }
            if (criteria.getRegistrationStatus() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getRegistrationStatus(), EventAttendeeGuest_.registrationStatus));
            }
            if (criteria.getCheckInStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCheckInStatus(), EventAttendeeGuest_.checkInStatus));
            }
            if (criteria.getCheckInTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCheckInTime(), EventAttendeeGuest_.checkInTime));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), EventAttendeeGuest_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), EventAttendeeGuest_.updatedAt));
            }
            if (criteria.getPrimaryAttendeeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPrimaryAttendeeId(),
                            root -> root.join(EventAttendeeGuest_.primaryAttendee, JoinType.LEFT).get(EventAttendee_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
