package com.eventsitemanager.service;

import com.eventsitemanager.domain.*;
import com.eventsitemanager.repository.EventAgendaItemRepository;
import com.eventsitemanager.service.criteria.EventAgendaItemCriteria;
import com.eventsitemanager.service.dto.EventAgendaItemDTO;
import com.eventsitemanager.service.mapper.EventAgendaItemMapper;
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
public class EventAgendaItemQueryService extends QueryService<EventAgendaItem> {

    private final Logger log = LoggerFactory.getLogger(EventAgendaItemQueryService.class);

    private final EventAgendaItemRepository eventAgendaItemRepository;

    private final EventAgendaItemMapper eventAgendaItemMapper;

    public EventAgendaItemQueryService(EventAgendaItemRepository eventAgendaItemRepository, EventAgendaItemMapper eventAgendaItemMapper) {
        this.eventAgendaItemRepository = eventAgendaItemRepository;
        this.eventAgendaItemMapper = eventAgendaItemMapper;
    }

    public List<EventAgendaItemDTO> findByCriteria(EventAgendaItemCriteria criteria) {
        final Specification<EventAgendaItem> specification = createSpecification(criteria);
        return eventAgendaItemMapper.toDto(eventAgendaItemRepository.findAll(specification));
    }

    public Page<EventAgendaItemDTO> findByCriteria(EventAgendaItemCriteria criteria, Pageable page) {
        final Specification<EventAgendaItem> specification = createSpecification(criteria);
        return eventAgendaItemRepository.findAll(specification, page).map(eventAgendaItemMapper::toDto);
    }

    public long countByCriteria(EventAgendaItemCriteria criteria) {
        return eventAgendaItemRepository.count(createSpecification(criteria));
    }

    protected Specification<EventAgendaItem> createSpecification(EventAgendaItemCriteria criteria) {
        Specification<EventAgendaItem> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EventAgendaItem_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), EventAgendaItem_.tenantId));
            }
            if (criteria.getScheduleDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getScheduleDate(), EventAgendaItem_.scheduleDate));
            }
            if (criteria.getStartTime() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStartTime(), EventAgendaItem_.startTime));
            }
            if (criteria.getEndTime() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEndTime(), EventAgendaItem_.endTime));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), EventAgendaItem_.title));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), EventAgendaItem_.description));
            }
            if (criteria.getImageUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImageUrl(), EventAgendaItem_.imageUrl));
            }
            if (criteria.getSortOrder() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSortOrder(), EventAgendaItem_.sortOrder));
            }
            if (criteria.getIsPublished() != null) {
                specification = specification.and(buildSpecification(criteria.getIsPublished(), EventAgendaItem_.isPublished));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), EventAgendaItem_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), EventAgendaItem_.updatedAt));
            }
            if (criteria.getEventId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEventId(),
                            root -> root.join(EventAgendaItem_.event, JoinType.LEFT).get(EventDetails_.id)
                        )
                    );
            }
            if (criteria.getEventMediaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEventMediaId(),
                            root -> root.join(EventAgendaItem_.eventMedia, JoinType.LEFT).get(EventMedia_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
