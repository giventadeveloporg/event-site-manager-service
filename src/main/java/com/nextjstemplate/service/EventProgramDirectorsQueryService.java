package com.nextjstemplate.service;

import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.domain.EventDetails_;
import com.nextjstemplate.domain.EventProgramDirectors;
import com.nextjstemplate.domain.EventProgramDirectors_;
import com.nextjstemplate.repository.EventProgramDirectorsRepository;
import com.nextjstemplate.service.criteria.EventProgramDirectorsCriteria;
import com.nextjstemplate.service.dto.EventProgramDirectorsDTO;
import com.nextjstemplate.service.mapper.EventProgramDirectorsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link EventProgramDirectors}
 * entities in the database.
 */
@Service
@Transactional(readOnly = true)
public class EventProgramDirectorsQueryService extends QueryService<EventProgramDirectors> {

  private final Logger log = LoggerFactory.getLogger(EventProgramDirectorsQueryService.class);

  private final EventProgramDirectorsRepository eventProgramDirectorsRepository;
  private final EventProgramDirectorsMapper eventProgramDirectorsMapper;

  public EventProgramDirectorsQueryService(
      EventProgramDirectorsRepository eventProgramDirectorsRepository,
      EventProgramDirectorsMapper eventProgramDirectorsMapper) {
    this.eventProgramDirectorsRepository = eventProgramDirectorsRepository;
    this.eventProgramDirectorsMapper = eventProgramDirectorsMapper;
  }

  @Transactional(readOnly = true)
  public Page<EventProgramDirectorsDTO> findByCriteria(EventProgramDirectorsCriteria criteria, Pageable page) {
    log.debug("find by criteria : {}, page: {}", criteria, page);
    final Specification<EventProgramDirectors> specification = createSpecification(criteria);
    return eventProgramDirectorsRepository.findAll(specification, page).map(eventProgramDirectorsMapper::toDto);
  }

  @Transactional(readOnly = true)
  public long countByCriteria(EventProgramDirectorsCriteria criteria) {
    log.debug("count by criteria : {}", criteria);
    final Specification<EventProgramDirectors> specification = createSpecification(criteria);
    return eventProgramDirectorsRepository.count(specification);
  }

  protected Specification<EventProgramDirectors> createSpecification(EventProgramDirectorsCriteria criteria) {
    Specification<EventProgramDirectors> specification = Specification.where(null);
    if (criteria != null) {
      specification = Specification.allOf(
          Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
          criteria.getId() != null ? buildRangeSpecification(criteria.getId(), EventProgramDirectors_.id) : null,
          criteria.getTenantId() != null
              ? buildStringSpecification(criteria.getTenantId(), EventProgramDirectors_.tenantId)
              : null,
          criteria.getName() != null ? buildStringSpecification(criteria.getName(), EventProgramDirectors_.name) : null,
          criteria.getPhotoUrl() != null
              ? buildStringSpecification(criteria.getPhotoUrl(), EventProgramDirectors_.photoUrl)
              : null,
          criteria.getBio() != null ? buildStringSpecification(criteria.getBio(), EventProgramDirectors_.bio) : null,
          criteria.getCreatedAt() != null
              ? buildRangeSpecification(criteria.getCreatedAt(), EventProgramDirectors_.createdAt)
              : null,
          criteria.getUpdatedAt() != null
              ? buildRangeSpecification(criteria.getUpdatedAt(), EventProgramDirectors_.updatedAt)
              : null,
          criteria.getEventId() != null
              ? buildReferringEntitySpecification(criteria.getEventId(), EventProgramDirectors_.event, EventDetails_.id)
              : null);
    }
    return specification;
  }
}
