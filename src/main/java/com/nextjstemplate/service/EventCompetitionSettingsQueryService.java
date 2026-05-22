package com.nextjstemplate.service;

import com.nextjstemplate.domain.*;
import com.nextjstemplate.domain.EventCompetitionSettings;
import com.nextjstemplate.repository.EventCompetitionSettingsRepository;
import com.nextjstemplate.service.criteria.EventCompetitionSettingsCriteria;
import com.nextjstemplate.service.dto.EventCompetitionSettingsDTO;
import com.nextjstemplate.service.mapper.EventCompetitionSettingsMapper;
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
public class EventCompetitionSettingsQueryService extends QueryService<EventCompetitionSettings> {

    private final Logger log = LoggerFactory.getLogger(EventCompetitionSettingsQueryService.class);

    private final EventCompetitionSettingsRepository eventCompetitionSettingsRepository;

    private final EventCompetitionSettingsMapper eventCompetitionSettingsMapper;

    public EventCompetitionSettingsQueryService(EventCompetitionSettingsRepository eventCompetitionSettingsRepository, EventCompetitionSettingsMapper eventCompetitionSettingsMapper) {
        this.eventCompetitionSettingsRepository = eventCompetitionSettingsRepository;
        this.eventCompetitionSettingsMapper = eventCompetitionSettingsMapper;
    }

    public List<EventCompetitionSettingsDTO> findByCriteria(EventCompetitionSettingsCriteria criteria) {
        final Specification<EventCompetitionSettings> specification = createSpecification(criteria);
        return eventCompetitionSettingsMapper.toDto(eventCompetitionSettingsRepository.findAll(specification));
    }

    public Page<EventCompetitionSettingsDTO> findByCriteria(EventCompetitionSettingsCriteria criteria, Pageable page) {
        final Specification<EventCompetitionSettings> specification = createSpecification(criteria);
        return eventCompetitionSettingsRepository.findAll(specification, page).map(eventCompetitionSettingsMapper::toDto);
    }

    public long countByCriteria(EventCompetitionSettingsCriteria criteria) {
        return eventCompetitionSettingsRepository.count(createSpecification(criteria));
    }

    protected Specification<EventCompetitionSettings> createSpecification(EventCompetitionSettingsCriteria criteria) {
        Specification<EventCompetitionSettings> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EventCompetitionSettings_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), EventCompetitionSettings_.tenantId));
            }
            if (criteria.getAudienceMode() != null) {
                specification = specification.and(buildSpecification(criteria.getAudienceMode(), EventCompetitionSettings_.audienceMode));
            }
            if (criteria.getRegistrationMode() != null) {
                specification = specification.and(buildSpecification(criteria.getRegistrationMode(), EventCompetitionSettings_.registrationMode));
            }
            if (criteria.getRegistrationDeadline() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRegistrationDeadline(), EventCompetitionSettings_.registrationDeadline));
            }
            if (criteria.getRegistrationOpen() != null) {
                specification = specification.and(buildSpecification(criteria.getRegistrationOpen(), EventCompetitionSettings_.registrationOpen));
            }
            if (criteria.getAllowTicketSales() != null) {
                specification = specification.and(buildSpecification(criteria.getAllowTicketSales(), EventCompetitionSettings_.allowTicketSales));
            }
            if (criteria.getPointsFirst() != null) {
                specification = specification.and(buildSpecification(criteria.getPointsFirst(), EventCompetitionSettings_.pointsFirst));
            }
            if (criteria.getPointsSecond() != null) {
                specification = specification.and(buildSpecification(criteria.getPointsSecond(), EventCompetitionSettings_.pointsSecond));
            }
            if (criteria.getPointsThird() != null) {
                specification = specification.and(buildSpecification(criteria.getPointsThird(), EventCompetitionSettings_.pointsThird));
            }
            if (criteria.getChampionEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getChampionEnabled(), EventCompetitionSettings_.championEnabled));
            }
            if (criteria.getChampionExcludeGroupPoints() != null) {
                specification = specification.and(buildSpecification(criteria.getChampionExcludeGroupPoints(), EventCompetitionSettings_.championExcludeGroupPoints));
            }
            if (criteria.getChampionMaxCategory() != null) {
                specification = specification.and(buildSpecification(criteria.getChampionMaxCategory(), EventCompetitionSettings_.championMaxCategory));
            }
            if (criteria.getResultsDisplayMode() != null) {
                specification = specification.and(buildSpecification(criteria.getResultsDisplayMode(), EventCompetitionSettings_.resultsDisplayMode));
            }
            if (criteria.getEligibilityText() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEligibilityText(), EventCompetitionSettings_.eligibilityText));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), EventCompetitionSettings_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), EventCompetitionSettings_.updatedAt));
            }
            if (criteria.getEventId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEventId(), root -> root.join(EventCompetitionSettings_.event, JoinType.LEFT).get(EventDetails_.id))
                    );
            }
        }
        return specification;
    }
}
