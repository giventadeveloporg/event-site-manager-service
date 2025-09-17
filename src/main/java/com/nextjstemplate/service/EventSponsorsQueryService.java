package com.nextjstemplate.service;

import com.nextjstemplate.domain.EventSponsors;
import com.nextjstemplate.domain.EventSponsors_;
import com.nextjstemplate.repository.EventSponsorsRepository;
import com.nextjstemplate.service.criteria.EventSponsorsCriteria;
import com.nextjstemplate.service.dto.EventSponsorsDTO;
import com.nextjstemplate.service.mapper.EventSponsorsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link EventSponsors} entities in
 * the database.
 */
@Service
@Transactional(readOnly = true)
public class EventSponsorsQueryService extends QueryService<EventSponsors> {

  private final Logger log = LoggerFactory.getLogger(EventSponsorsQueryService.class);

  private final EventSponsorsRepository eventSponsorsRepository;
  private final EventSponsorsMapper eventSponsorsMapper;

  public EventSponsorsQueryService(EventSponsorsRepository eventSponsorsRepository,
      EventSponsorsMapper eventSponsorsMapper) {
    this.eventSponsorsRepository = eventSponsorsRepository;
    this.eventSponsorsMapper = eventSponsorsMapper;
  }

  @Transactional(readOnly = true)
  public Page<EventSponsorsDTO> findByCriteria(EventSponsorsCriteria criteria, Pageable page) {
    log.debug("find by criteria : {}, page: {}", criteria, page);
    final Specification<EventSponsors> specification = createSpecification(criteria);
    return eventSponsorsRepository.findAll(specification, page).map(eventSponsorsMapper::toDto);
  }

  @Transactional(readOnly = true)
  public long countByCriteria(EventSponsorsCriteria criteria) {
    log.debug("count by criteria : {}", criteria);
    final Specification<EventSponsors> specification = createSpecification(criteria);
    return eventSponsorsRepository.count(specification);
  }

  protected Specification<EventSponsors> createSpecification(EventSponsorsCriteria criteria) {
    Specification<EventSponsors> specification = Specification.where(null);
    if (criteria != null) {
      specification = Specification.allOf(
          Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
          criteria.getId() != null ? buildRangeSpecification(criteria.getId(), EventSponsors_.id) : null,
          criteria.getTenantId() != null ? buildStringSpecification(criteria.getTenantId(), EventSponsors_.tenantId)
              : null,
          criteria.getName() != null ? buildStringSpecification(criteria.getName(), EventSponsors_.name) : null,
          criteria.getType() != null ? buildStringSpecification(criteria.getType(), EventSponsors_.type) : null,
          criteria.getCompanyName() != null
              ? buildStringSpecification(criteria.getCompanyName(), EventSponsors_.companyName)
              : null,
          criteria.getTagline() != null ? buildStringSpecification(criteria.getTagline(), EventSponsors_.tagline)
              : null,
          criteria.getDescription() != null
              ? buildStringSpecification(criteria.getDescription(), EventSponsors_.description)
              : null,
          criteria.getWebsiteUrl() != null
              ? buildStringSpecification(criteria.getWebsiteUrl(), EventSponsors_.websiteUrl)
              : null,
          criteria.getContactEmail() != null
              ? buildStringSpecification(criteria.getContactEmail(), EventSponsors_.contactEmail)
              : null,
          criteria.getContactPhone() != null
              ? buildStringSpecification(criteria.getContactPhone(), EventSponsors_.contactPhone)
              : null,
          criteria.getLogoUrl() != null ? buildStringSpecification(criteria.getLogoUrl(), EventSponsors_.logoUrl)
              : null,
          criteria.getHeroImageUrl() != null
              ? buildStringSpecification(criteria.getHeroImageUrl(), EventSponsors_.heroImageUrl)
              : null,
          criteria.getBannerImageUrl() != null
              ? buildStringSpecification(criteria.getBannerImageUrl(), EventSponsors_.bannerImageUrl)
              : null,
          criteria.getIsActive() != null ? buildSpecification(criteria.getIsActive(), EventSponsors_.isActive) : null,
          criteria.getPriorityRanking() != null
              ? buildRangeSpecification(criteria.getPriorityRanking(), EventSponsors_.priorityRanking)
              : null,
          criteria.getFacebookUrl() != null
              ? buildStringSpecification(criteria.getFacebookUrl(), EventSponsors_.facebookUrl)
              : null,
          criteria.getTwitterUrl() != null
              ? buildStringSpecification(criteria.getTwitterUrl(), EventSponsors_.twitterUrl)
              : null,
          criteria.getLinkedinUrl() != null
              ? buildStringSpecification(criteria.getLinkedinUrl(), EventSponsors_.linkedinUrl)
              : null,
          criteria.getInstagramUrl() != null
              ? buildStringSpecification(criteria.getInstagramUrl(), EventSponsors_.instagramUrl)
              : null,
          criteria.getCreatedAt() != null ? buildRangeSpecification(criteria.getCreatedAt(), EventSponsors_.createdAt)
              : null,
          criteria.getUpdatedAt() != null ? buildRangeSpecification(criteria.getUpdatedAt(), EventSponsors_.updatedAt)
              : null);
    }
    return specification;
  }
}
