package com.nextjstemplate.service;

import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.domain.EventDetails_;
import com.nextjstemplate.domain.EventEmails;
import com.nextjstemplate.domain.EventEmails_;
import com.nextjstemplate.repository.EventEmailsRepository;
import com.nextjstemplate.service.criteria.EventEmailsCriteria;
import com.nextjstemplate.service.dto.EventEmailsDTO;
import com.nextjstemplate.service.mapper.EventEmailsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link EventEmails} entities in the
 * database.
 */
@Service
@Transactional(readOnly = true)
public class EventEmailsQueryService extends QueryService<EventEmails> {

  private final Logger log = LoggerFactory.getLogger(EventEmailsQueryService.class);

  private final EventEmailsRepository eventEmailsRepository;
  private final EventEmailsMapper eventEmailsMapper;

  public EventEmailsQueryService(EventEmailsRepository eventEmailsRepository, EventEmailsMapper eventEmailsMapper) {
    this.eventEmailsRepository = eventEmailsRepository;
    this.eventEmailsMapper = eventEmailsMapper;
  }

  @Transactional(readOnly = true)
  public Page<EventEmailsDTO> findByCriteria(EventEmailsCriteria criteria, Pageable page) {
    log.debug("find by criteria : {}, page: {}", criteria, page);
    final Specification<EventEmails> specification = createSpecification(criteria);
    return eventEmailsRepository.findAll(specification, page).map(eventEmailsMapper::toDto);
  }

  @Transactional(readOnly = true)
  public long countByCriteria(EventEmailsCriteria criteria) {
    log.debug("count by criteria : {}", criteria);
    final Specification<EventEmails> specification = createSpecification(criteria);
    return eventEmailsRepository.count(specification);
  }

  protected Specification<EventEmails> createSpecification(EventEmailsCriteria criteria) {
    Specification<EventEmails> specification = Specification.where(null);
    if (criteria != null) {
      specification = Specification.allOf(
          Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
          criteria.getId() != null ? buildRangeSpecification(criteria.getId(), EventEmails_.id) : null,
          criteria.getTenantId() != null ? buildStringSpecification(criteria.getTenantId(), EventEmails_.tenantId)
              : null,
          criteria.getEmail() != null ? buildStringSpecification(criteria.getEmail(), EventEmails_.email) : null,
          criteria.getCreatedAt() != null ? buildRangeSpecification(criteria.getCreatedAt(), EventEmails_.createdAt)
              : null,
          criteria.getUpdatedAt() != null ? buildRangeSpecification(criteria.getUpdatedAt(), EventEmails_.updatedAt)
              : null,
          criteria.getEventId() != null
              ? buildReferringEntitySpecification(criteria.getEventId(), EventEmails_.event, EventDetails_.id)
              : null);
    }
    return specification;
  }
}
