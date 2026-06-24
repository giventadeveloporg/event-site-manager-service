package com.eventsitemanager.service;

import com.eventsitemanager.domain.*;
import com.eventsitemanager.domain.EventCompetitionContentBlock;
import com.eventsitemanager.repository.EventCompetitionContentBlockRepository;
import com.eventsitemanager.service.criteria.EventCompetitionContentBlockCriteria;
import com.eventsitemanager.service.dto.EventCompetitionContentBlockDTO;
import com.eventsitemanager.service.mapper.EventCompetitionContentBlockMapper;
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
public class EventCompetitionContentBlockQueryService extends QueryService<EventCompetitionContentBlock> {

    private final Logger log = LoggerFactory.getLogger(EventCompetitionContentBlockQueryService.class);

    private final EventCompetitionContentBlockRepository eventCompetitionContentBlockRepository;

    private final EventCompetitionContentBlockMapper eventCompetitionContentBlockMapper;

    public EventCompetitionContentBlockQueryService(
        EventCompetitionContentBlockRepository eventCompetitionContentBlockRepository,
        EventCompetitionContentBlockMapper eventCompetitionContentBlockMapper
    ) {
        this.eventCompetitionContentBlockRepository = eventCompetitionContentBlockRepository;
        this.eventCompetitionContentBlockMapper = eventCompetitionContentBlockMapper;
    }

    public List<EventCompetitionContentBlockDTO> findByCriteria(EventCompetitionContentBlockCriteria criteria) {
        final Specification<EventCompetitionContentBlock> specification = createSpecification(criteria);
        return eventCompetitionContentBlockMapper.toDto(eventCompetitionContentBlockRepository.findAll(specification));
    }

    public Page<EventCompetitionContentBlockDTO> findByCriteria(EventCompetitionContentBlockCriteria criteria, Pageable page) {
        final Specification<EventCompetitionContentBlock> specification = createSpecification(criteria);
        return eventCompetitionContentBlockRepository.findAll(specification, page).map(eventCompetitionContentBlockMapper::toDto);
    }

    public long countByCriteria(EventCompetitionContentBlockCriteria criteria) {
        return eventCompetitionContentBlockRepository.count(createSpecification(criteria));
    }

    protected Specification<EventCompetitionContentBlock> createSpecification(EventCompetitionContentBlockCriteria criteria) {
        Specification<EventCompetitionContentBlock> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EventCompetitionContentBlock_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), EventCompetitionContentBlock_.tenantId));
            }
            if (criteria.getBlockType() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getBlockType(), EventCompetitionContentBlock_.blockType));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), EventCompetitionContentBlock_.title));
            }
            if (criteria.getBodyMarkdown() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getBodyMarkdown(), EventCompetitionContentBlock_.bodyMarkdown));
            }
            if (criteria.getSortOrder() != null) {
                specification = specification.and(buildSpecification(criteria.getSortOrder(), EventCompetitionContentBlock_.sortOrder));
            }
            if (criteria.getCreatedAt() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getCreatedAt(), EventCompetitionContentBlock_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getUpdatedAt(), EventCompetitionContentBlock_.updatedAt));
            }
            if (criteria.getEventId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEventId(),
                            root -> root.join(EventCompetitionContentBlock_.event, JoinType.LEFT).get(EventDetails_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
