package com.nextjstemplate.service;

import com.nextjstemplate.domain.*;
import com.nextjstemplate.domain.EventCompetitionDay;
import com.nextjstemplate.repository.EventCompetitionDayRepository;
import com.nextjstemplate.service.criteria.EventCompetitionDayCriteria;
import com.nextjstemplate.service.dto.EventCompetitionDayDTO;
import com.nextjstemplate.service.mapper.EventCompetitionDayMapper;
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
public class EventCompetitionDayQueryService extends QueryService<EventCompetitionDay> {

    private final Logger log = LoggerFactory.getLogger(EventCompetitionDayQueryService.class);

    private final EventCompetitionDayRepository eventCompetitionDayRepository;

    private final EventCompetitionDayMapper eventCompetitionDayMapper;

    public EventCompetitionDayQueryService(EventCompetitionDayRepository eventCompetitionDayRepository, EventCompetitionDayMapper eventCompetitionDayMapper) {
        this.eventCompetitionDayRepository = eventCompetitionDayRepository;
        this.eventCompetitionDayMapper = eventCompetitionDayMapper;
    }

    public List<EventCompetitionDayDTO> findByCriteria(EventCompetitionDayCriteria criteria) {
        final Specification<EventCompetitionDay> specification = createSpecification(criteria);
        return eventCompetitionDayMapper.toDto(eventCompetitionDayRepository.findAll(specification));
    }

    public Page<EventCompetitionDayDTO> findByCriteria(EventCompetitionDayCriteria criteria, Pageable page) {
        final Specification<EventCompetitionDay> specification = createSpecification(criteria);
        return eventCompetitionDayRepository.findAll(specification, page).map(eventCompetitionDayMapper::toDto);
    }

    public long countByCriteria(EventCompetitionDayCriteria criteria) {
        return eventCompetitionDayRepository.count(createSpecification(criteria));
    }

    protected Specification<EventCompetitionDay> createSpecification(EventCompetitionDayCriteria criteria) {
        Specification<EventCompetitionDay> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EventCompetitionDay_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), EventCompetitionDay_.tenantId));
            }
            if (criteria.getDayLabel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDayLabel(), EventCompetitionDay_.dayLabel));
            }
            if (criteria.getEventDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEventDate(), EventCompetitionDay_.eventDate));
            }
            if (criteria.getVenueName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVenueName(), EventCompetitionDay_.venueName));
            }
            if (criteria.getVenueAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVenueAddress(), EventCompetitionDay_.venueAddress));
            }
            if (criteria.getSortOrder() != null) {
                specification = specification.and(buildSpecification(criteria.getSortOrder(), EventCompetitionDay_.sortOrder));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), EventCompetitionDay_.notes));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), EventCompetitionDay_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), EventCompetitionDay_.updatedAt));
            }
            if (criteria.getEventId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEventId(), root -> root.join(EventCompetitionDay_.event, JoinType.LEFT).get(EventDetails_.id))
                    );
            }
        }
        return specification;
    }
}
