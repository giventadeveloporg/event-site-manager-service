package com.nextjstemplate.service;

import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.domain.EventDetails_;
import com.nextjstemplate.domain.EventContacts;
import com.nextjstemplate.domain.EventContacts_;
import com.nextjstemplate.repository.EventContactsRepository;
import com.nextjstemplate.service.criteria.EventContactsCriteria;
import com.nextjstemplate.service.dto.EventContactsDTO;
import com.nextjstemplate.service.mapper.EventContactsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link EventContacts} entities in
 * the database.
 */
@Service
@Transactional(readOnly = true)
public class EventContactsQueryService extends QueryService<EventContacts> {

  private final Logger log = LoggerFactory.getLogger(EventContactsQueryService.class);

  private final EventContactsRepository eventContactsRepository;
  private final EventContactsMapper eventContactsMapper;

  public EventContactsQueryService(EventContactsRepository eventContactsRepository,
      EventContactsMapper eventContactsMapper) {
    this.eventContactsRepository = eventContactsRepository;
    this.eventContactsMapper = eventContactsMapper;
  }

  @Transactional(readOnly = true)
  public Page<EventContactsDTO> findByCriteria(EventContactsCriteria criteria, Pageable page) {
    log.debug("find by criteria : {}, page: {}", criteria, page);
    final Specification<EventContacts> specification = createSpecification(criteria);
    return eventContactsRepository.findAll(specification, page).map(eventContactsMapper::toDto);
  }

  @Transactional(readOnly = true)
  public long countByCriteria(EventContactsCriteria criteria) {
    log.debug("count by criteria : {}", criteria);
    final Specification<EventContacts> specification = createSpecification(criteria);
    return eventContactsRepository.count(specification);
  }

  protected Specification<EventContacts> createSpecification(EventContactsCriteria criteria) {
    Specification<EventContacts> specification = Specification.where(null);
    if (criteria != null) {
      specification = Specification.allOf(
          Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
          criteria.getId() != null ? buildRangeSpecification(criteria.getId(), EventContacts_.id) : null,
          criteria.getName() != null ? buildStringSpecification(criteria.getName(), EventContacts_.name) : null,
          criteria.getPhone() != null ? buildStringSpecification(criteria.getPhone(), EventContacts_.phone) : null,
          criteria.getEmail() != null ? buildStringSpecification(criteria.getEmail(), EventContacts_.email) : null,
          criteria.getCreatedAt() != null ? buildRangeSpecification(criteria.getCreatedAt(), EventContacts_.createdAt)
              : null,
          criteria.getUpdatedAt() != null ? buildRangeSpecification(criteria.getUpdatedAt(), EventContacts_.updatedAt)
              : null,
          criteria.getEventId() != null
              ? buildReferringEntitySpecification(criteria.getEventId(), EventContacts_.event, EventDetails_.id)
              : null);
    }
    return specification;
  }
}
