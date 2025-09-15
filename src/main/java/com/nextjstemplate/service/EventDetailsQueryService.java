package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.repository.EventDetailsRepository;
import com.nextjstemplate.service.criteria.EventDetailsCriteria;
import com.nextjstemplate.service.dto.EventDetailsDTO;
import com.nextjstemplate.service.mapper.EventDetailsMapper;
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
 * Service for executing complex queries for {@link EventDetails} entities in the database.
 * The main input is a {@link EventDetailsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EventDetailsDTO} or a {@link Page} of {@link EventDetailsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventDetailsQueryService extends QueryService<EventDetails> {

    private final Logger log = LoggerFactory.getLogger(EventDetailsQueryService.class);

    private final EventDetailsRepository eventDetailsRepository;

    private final EventDetailsMapper eventDetailsMapper;

    public EventDetailsQueryService(EventDetailsRepository eventDetailsRepository, EventDetailsMapper eventDetailsMapper) {
        this.eventDetailsRepository = eventDetailsRepository;
        this.eventDetailsMapper = eventDetailsMapper;
    }

    /**
     * Return a {@link List} of {@link EventDetailsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EventDetailsDTO> findByCriteria(EventDetailsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EventDetails> specification = createSpecification(criteria);
        return eventDetailsMapper.toDto(eventDetailsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EventDetailsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EventDetailsDTO> findByCriteria(EventDetailsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EventDetails> specification = createSpecification(criteria);
        return eventDetailsRepository.findAll(specification, page).map(eventDetailsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventDetailsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EventDetails> specification = createSpecification(criteria);
        return eventDetailsRepository.count(specification);
    }

    /**
     * Function to convert {@link EventDetailsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EventDetails> createSpecification(EventDetailsCriteria criteria) {
        Specification<EventDetails> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EventDetails_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), EventDetails_.tenantId));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), EventDetails_.title));
            }
            if (criteria.getCaption() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCaption(), EventDetails_.caption));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), EventDetails_.description));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), EventDetails_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), EventDetails_.endDate));
            }
            if (criteria.getStartTime() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStartTime(), EventDetails_.startTime));
            }
            if (criteria.getEndTime() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEndTime(), EventDetails_.endTime));
            }
            if (criteria.getTimezone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTimezone(), EventDetails_.timezone));
            }
            if (criteria.getLocation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocation(), EventDetails_.location));
            }
            if (criteria.getDirectionsToVenue() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getDirectionsToVenue(), EventDetails_.directionsToVenue));
            }
            if (criteria.getCapacity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCapacity(), EventDetails_.capacity));
            }
            if (criteria.getAdmissionType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAdmissionType(), EventDetails_.admissionType));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), EventDetails_.isActive));
            }
            if (criteria.getMaxGuestsPerAttendee() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getMaxGuestsPerAttendee(), EventDetails_.maxGuestsPerAttendee));
            }
            if (criteria.getAllowGuests() != null) {
                specification = specification.and(buildSpecification(criteria.getAllowGuests(), EventDetails_.allowGuests));
            }
            if (criteria.getRequireGuestApproval() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getRequireGuestApproval(), EventDetails_.requireGuestApproval));
            }
            if (criteria.getEnableGuestPricing() != null) {
                specification = specification.and(buildSpecification(criteria.getEnableGuestPricing(), EventDetails_.enableGuestPricing));
            }
            if (criteria.getEnableQrCode() != null) {
                specification = specification.and(buildSpecification(criteria.getEnableQrCode(), EventDetails_.enableQrCode));
            }
            if (criteria.getIsRegistrationRequired() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getIsRegistrationRequired(), EventDetails_.isRegistrationRequired));
            }
            if (criteria.getIsSportsEvent() != null) {
                specification = specification.and(buildSpecification(criteria.getIsSportsEvent(), EventDetails_.isSportsEvent));
            }
            if (criteria.getIsLive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsLive(), EventDetails_.isLive));
            }
            if (criteria.getIsFeaturedEvent() != null) {
                specification = specification.and(buildSpecification(criteria.getIsFeaturedEvent(), EventDetails_.isFeaturedEvent));
            }
            if (criteria.getFeaturedEventPriorityRanking() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getFeaturedEventPriorityRanking(), EventDetails_.featuredEventPriorityRanking));
            }
            if (criteria.getLiveEventPriorityRanking() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getLiveEventPriorityRanking(), EventDetails_.liveEventPriorityRanking));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), EventDetails_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), EventDetails_.updatedAt));
            }
            if (criteria.getCreatedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCreatedById(),
                            root -> root.join(EventDetails_.createdBy, JoinType.LEFT).get(UserProfile_.id)
                        )
                    );
            }
            if (criteria.getEventTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEventTypeId(),
                            root -> root.join(EventDetails_.eventType, JoinType.LEFT).get(EventTypeDetails_.id)
                        )
                    );
            }
            if (criteria.getDiscountCodesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDiscountCodesId(),
                            root -> root.join(EventDetails_.discountCodes, JoinType.LEFT).get(DiscountCode_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
