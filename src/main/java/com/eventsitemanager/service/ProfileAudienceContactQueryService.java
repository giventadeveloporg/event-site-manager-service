package com.eventsitemanager.service;

import com.eventsitemanager.domain.ProfileAudienceContact;
import com.eventsitemanager.domain.ProfileAudienceContact_;
import com.eventsitemanager.repository.ProfileAudienceContactRepository;
import com.eventsitemanager.service.criteria.ProfileAudienceContactCriteria;
import com.eventsitemanager.service.dto.ProfileAudienceContactDTO;
import com.eventsitemanager.service.mapper.ProfileAudienceContactMapper;
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
public class ProfileAudienceContactQueryService extends QueryService<ProfileAudienceContact> {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileAudienceContactQueryService.class);

    private final ProfileAudienceContactRepository profileAudienceContactRepository;
    private final ProfileAudienceContactMapper profileAudienceContactMapper;

    public ProfileAudienceContactQueryService(
        ProfileAudienceContactRepository profileAudienceContactRepository,
        ProfileAudienceContactMapper profileAudienceContactMapper
    ) {
        this.profileAudienceContactRepository = profileAudienceContactRepository;
        this.profileAudienceContactMapper = profileAudienceContactMapper;
    }

    public Page<ProfileAudienceContactDTO> findByCriteria(ProfileAudienceContactCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProfileAudienceContact> specification = createSpecification(criteria);
        return profileAudienceContactRepository.findAll(specification, page).map(profileAudienceContactMapper::toDto);
    }

    public long countByCriteria(ProfileAudienceContactCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProfileAudienceContact> specification = createSpecification(criteria);
        return profileAudienceContactRepository.count(specification);
    }

    protected Specification<ProfileAudienceContact> createSpecification(ProfileAudienceContactCriteria criteria) {
        Specification<ProfileAudienceContact> specification = Specification.where(null);
        if (criteria != null) {
            specification =
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                    criteria.getId() != null ? buildRangeSpecification(criteria.getId(), ProfileAudienceContact_.id) : null,
                    criteria.getTenantId() != null
                        ? buildStringSpecification(criteria.getTenantId(), ProfileAudienceContact_.tenantId)
                        : null,
                    criteria.getPublicProfileId() != null
                        ? buildRangeSpecification(criteria.getPublicProfileId(), ProfileAudienceContact_.publicProfileId)
                        : null,
                    criteria.getEmail() != null ? buildStringSpecification(criteria.getEmail(), ProfileAudienceContact_.email) : null,
                    criteria.getSource() != null ? buildSpecification(criteria.getSource(), ProfileAudienceContact_.source) : null,
                    criteria.getOptInStatus() != null
                        ? buildSpecification(criteria.getOptInStatus(), ProfileAudienceContact_.optInStatus)
                        : null,
                    criteria.getCreatedAt() != null
                        ? buildRangeSpecification(criteria.getCreatedAt(), ProfileAudienceContact_.createdAt)
                        : null,
                    criteria.getUpdatedAt() != null
                        ? buildRangeSpecification(criteria.getUpdatedAt(), ProfileAudienceContact_.updatedAt)
                        : null,
                    null
                );
        }
        return specification;
    }
}
