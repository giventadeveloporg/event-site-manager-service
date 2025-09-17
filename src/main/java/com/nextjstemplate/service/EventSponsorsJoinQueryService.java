package com.nextjstemplate.service;

import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.domain.EventDetails_;
import com.nextjstemplate.domain.EventSponsors;
import com.nextjstemplate.domain.EventSponsors_;
import com.nextjstemplate.domain.EventSponsorsJoin;
import com.nextjstemplate.domain.EventSponsorsJoin_;
import com.nextjstemplate.repository.EventSponsorsJoinRepository;
import com.nextjstemplate.service.criteria.EventSponsorsJoinCriteria;
import com.nextjstemplate.service.dto.EventSponsorsJoinDTO;
import com.nextjstemplate.service.mapper.EventSponsorsJoinMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link EventSponsorsJoin} entities
 * in the database.
 */
@Service
@Transactional(readOnly = true)
public class EventSponsorsJoinQueryService extends QueryService<EventSponsorsJoin> {

  private final Logger log = LoggerFactory.getLogger(EventSponsorsJoinQueryService.class);

  private final EventSponsorsJoinRepository eventSponsorsJoinRepository;
  private final EventSponsorsJoinMapper eventSponsorsJoinMapper;

  public EventSponsorsJoinQueryService(
      EventSponsorsJoinRepository eventSponsorsJoinRepository,
      EventSponsorsJoinMapper eventSponsorsJoinMapper) {
    this.eventSponsorsJoinRepository = eventSponsorsJoinRepository;
    this.eventSponsorsJoinMapper = eventSponsorsJoinMapper;
  }

  @Transactional(readOnly = true)
  public Page<EventSponsorsJoinDTO> findByCriteria(EventSponsorsJoinCriteria criteria, Pageable page) {
    log.debug("find by criteria : {}, page: {}", criteria, page);
    final Specification<EventSponsorsJoin> specification = createSpecification(criteria);
    return eventSponsorsJoinRepository.findAll(specification, page).map(eventSponsorsJoinMapper::toDto);
  }

  @Transactional(readOnly = true)
  public long countByCriteria(EventSponsorsJoinCriteria criteria) {
    log.debug("count by criteria : {}", criteria);
    final Specification<EventSponsorsJoin> specification = createSpecification(criteria);
    return eventSponsorsJoinRepository.count(specification);
  }

  protected Specification<EventSponsorsJoin> createSpecification(EventSponsorsJoinCriteria criteria) {
    Specification<EventSponsorsJoin> specification = Specification.where(null);
    if (criteria != null) {
      specification = Specification.allOf(
          Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
          criteria.getId() != null ? buildRangeSpecification(criteria.getId(), EventSponsorsJoin_.id) : null,
          criteria.getTenantId() != null ? buildStringSpecification(criteria.getTenantId(), EventSponsorsJoin_.tenantId)
              : null,
          criteria.getCreatedAt() != null
              ? buildRangeSpecification(criteria.getCreatedAt(), EventSponsorsJoin_.createdAt)
              : null,
          criteria.getEventId() != null
              ? buildReferringEntitySpecification(criteria.getEventId(), EventSponsorsJoin_.event, EventDetails_.id)
              : null,
          criteria.getSponsorId() != null
              ? buildReferringEntitySpecification(criteria.getSponsorId(), EventSponsorsJoin_.sponsor,
                  EventSponsors_.id)
              : null);
    }
    return specification;
  }
}
