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
          buildRangeSpecification(criteria.getId(), EventSponsors_.id),
          buildStringSpecification(criteria.getName(), EventSponsors_.name),
          buildStringSpecification(criteria.getType(), EventSponsors_.type),
          buildStringSpecification(criteria.getCompanyName(), EventSponsors_.companyName),
          buildStringSpecification(criteria.getTagline(), EventSponsors_.tagline),
          buildStringSpecification(criteria.getDescription(), EventSponsors_.description),
          buildStringSpecification(criteria.getWebsiteUrl(), EventSponsors_.websiteUrl),
          buildStringSpecification(criteria.getContactEmail(), EventSponsors_.contactEmail),
          buildStringSpecification(criteria.getContactPhone(), EventSponsors_.contactPhone),
          buildStringSpecification(criteria.getLogoUrl(), EventSponsors_.logoUrl),
          buildStringSpecification(criteria.getHeroImageUrl(), EventSponsors_.heroImageUrl),
          buildStringSpecification(criteria.getBannerImageUrl(), EventSponsors_.bannerImageUrl),
          buildSpecification(criteria.getIsActive(), EventSponsors_.isActive),
          buildRangeSpecification(criteria.getPriorityRanking(), EventSponsors_.priorityRanking),
          buildStringSpecification(criteria.getFacebookUrl(), EventSponsors_.facebookUrl),
          buildStringSpecification(criteria.getTwitterUrl(), EventSponsors_.twitterUrl),
          buildStringSpecification(criteria.getLinkedinUrl(), EventSponsors_.linkedinUrl),
          buildStringSpecification(criteria.getInstagramUrl(), EventSponsors_.instagramUrl),
          buildRangeSpecification(criteria.getCreatedAt(), EventSponsors_.createdAt),
          buildRangeSpecification(criteria.getUpdatedAt(), EventSponsors_.updatedAt));
    }
    return specification;
  }
}
