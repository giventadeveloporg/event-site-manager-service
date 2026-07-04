package com.eventsitemanager.service;

import com.eventsitemanager.domain.*;
import com.eventsitemanager.domain.ProfileAchievement;
import com.eventsitemanager.repository.ProfileAchievementRepository;
import com.eventsitemanager.service.criteria.ProfileAchievementCriteria;
import com.eventsitemanager.service.dto.ProfileAchievementDTO;
import com.eventsitemanager.service.mapper.ProfileAchievementMapper;
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
public class ProfileAchievementQueryService extends QueryService<ProfileAchievement> {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileAchievementQueryService.class);

    private final ProfileAchievementRepository profileAchievementRepository;
    private final ProfileAchievementMapper profileAchievementMapper;

    public ProfileAchievementQueryService(
        ProfileAchievementRepository profileAchievementRepository,
        ProfileAchievementMapper profileAchievementMapper
    ) {
        this.profileAchievementRepository = profileAchievementRepository;
        this.profileAchievementMapper = profileAchievementMapper;
    }

    public Page<ProfileAchievementDTO> findByCriteria(ProfileAchievementCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProfileAchievement> specification = createSpecification(criteria);
        return profileAchievementRepository.findAll(specification, page).map(profileAchievementMapper::toDto);
    }

    public long countByCriteria(ProfileAchievementCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProfileAchievement> specification = createSpecification(criteria);
        return profileAchievementRepository.count(specification);
    }

    protected Specification<ProfileAchievement> createSpecification(ProfileAchievementCriteria criteria) {
        Specification<ProfileAchievement> specification = Specification.where(null);
        if (criteria != null) {
            specification =
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                    criteria.getId() != null ? buildRangeSpecification(criteria.getId(), ProfileAchievement_.id) : null,
                    criteria.getTenantId() != null ? buildStringSpecification(criteria.getTenantId(), ProfileAchievement_.tenantId) : null,
                    criteria.getTitle() != null ? buildStringSpecification(criteria.getTitle(), ProfileAchievement_.title) : null,
                    criteria.getCategory() != null ? buildSpecification(criteria.getCategory(), ProfileAchievement_.category) : null,
                    criteria.getDisplayOrder() != null
                        ? buildRangeSpecification(criteria.getDisplayOrder(), ProfileAchievement_.displayOrder)
                        : null,
                    criteria.getIsFeatured() != null ? buildSpecification(criteria.getIsFeatured(), ProfileAchievement_.isFeatured) : null,
                    criteria.getAchievementDate() != null
                        ? buildRangeSpecification(criteria.getAchievementDate(), ProfileAchievement_.achievementDate)
                        : null,
                    criteria.getCreatedAt() != null
                        ? buildRangeSpecification(criteria.getCreatedAt(), ProfileAchievement_.createdAt)
                        : null,
                    criteria.getUpdatedAt() != null
                        ? buildRangeSpecification(criteria.getUpdatedAt(), ProfileAchievement_.updatedAt)
                        : null,
                    null
                );
        }
        return specification;
    }
}
