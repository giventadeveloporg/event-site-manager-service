package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.TenantSettings;
import com.nextjstemplate.repository.TenantSettingsRepository;
import com.nextjstemplate.service.criteria.TenantSettingsCriteria;
import com.nextjstemplate.service.dto.TenantSettingsDTO;
import com.nextjstemplate.service.mapper.TenantSettingsMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TenantSettings} entities in
 * the database.
 * The main input is a {@link TenantSettingsCriteria} which gets converted to
 * {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TenantSettingsDTO} which fulfills the
 * criteria.
 */
@Service
@Transactional(readOnly = true)
public class TenantSettingsQueryService extends QueryService<TenantSettings> {

    private static final Logger LOG = LoggerFactory.getLogger(TenantSettingsQueryService.class);

    private final TenantSettingsRepository tenantSettingsRepository;

    private final TenantSettingsMapper tenantSettingsMapper;

    public TenantSettingsQueryService(TenantSettingsRepository tenantSettingsRepository,
            TenantSettingsMapper tenantSettingsMapper) {
        this.tenantSettingsRepository = tenantSettingsRepository;
        this.tenantSettingsMapper = tenantSettingsMapper;
    }

    /**
     * Return a {@link Page} of {@link TenantSettingsDTO} which matches the criteria
     * from the database.
     * 
     * @param criteria The object which holds all the filters, which the entities
     *                 should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TenantSettingsDTO> findByCriteria(TenantSettingsCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TenantSettings> specification = createSpecification(criteria);
        return tenantSettingsRepository.findAll(specification, page).map(tenantSettingsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * 
     * @param criteria The object which holds all the filters, which the entities
     *                 should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TenantSettingsCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TenantSettings> specification = createSpecification(criteria);
        return tenantSettingsRepository.count(specification);
    }

    /**
     * Function to convert {@link TenantSettingsCriteria} to a {@link Specification}
     * 
     * @param criteria The object which holds all the filters, which the entities
     *                 should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TenantSettings> createSpecification(TenantSettingsCriteria criteria) {
        Specification<TenantSettings> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TenantSettings_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification
                        .and(buildStringSpecification(criteria.getTenantId(), TenantSettings_.tenantId));
            }
            if (criteria.getAllowUserRegistration() != null) {
                specification = specification.and(
                        buildSpecification(criteria.getAllowUserRegistration(), TenantSettings_.allowUserRegistration));
            }
            if (criteria.getRequireAdminApproval() != null) {
                specification = specification.and(
                        buildSpecification(criteria.getRequireAdminApproval(), TenantSettings_.requireAdminApproval));
            }
            if (criteria.getEnableWhatsappIntegration() != null) {
                specification = specification.and(buildSpecification(criteria.getEnableWhatsappIntegration(),
                        TenantSettings_.enableWhatsappIntegration));
            }
            if (criteria.getEnableEmailMarketing() != null) {
                specification = specification.and(
                        buildSpecification(criteria.getEnableEmailMarketing(), TenantSettings_.enableEmailMarketing));
            }
            if (criteria.getWhatsappApiKey() != null) {
                specification = specification
                        .and(buildStringSpecification(criteria.getWhatsappApiKey(), TenantSettings_.whatsappApiKey));
            }
            if (criteria.getEmailProviderConfig() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmailProviderConfig(),
                        TenantSettings_.emailProviderConfig));
            }
            if (criteria.getMaxEventsPerMonth() != null) {
                specification = specification.and(
                        buildRangeSpecification(criteria.getMaxEventsPerMonth(), TenantSettings_.maxEventsPerMonth));
            }
            if (criteria.getMaxAttendeesPerEvent() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMaxAttendeesPerEvent(),
                        TenantSettings_.maxAttendeesPerEvent));
            }
            if (criteria.getEnableGuestRegistration() != null) {
                specification = specification.and(buildSpecification(criteria.getEnableGuestRegistration(),
                        TenantSettings_.enableGuestRegistration));
            }
            if (criteria.getMaxGuestsPerAttendee() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMaxGuestsPerAttendee(),
                        TenantSettings_.maxGuestsPerAttendee));
            }
            if (criteria.getDefaultEventCapacity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDefaultEventCapacity(),
                        TenantSettings_.defaultEventCapacity));
            }
            if (criteria.getPlatformFeePercentage() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPlatformFeePercentage(),
                        TenantSettings_.platformFeePercentage));
            }
            if (criteria.getCustomCss() != null) {
                specification = specification
                        .and(buildStringSpecification(criteria.getCustomCss(), TenantSettings_.customCss));
            }
            if (criteria.getCustomJs() != null) {
                specification = specification
                        .and(buildStringSpecification(criteria.getCustomJs(), TenantSettings_.customJs));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification
                        .and(buildRangeSpecification(criteria.getCreatedAt(), TenantSettings_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification
                        .and(buildRangeSpecification(criteria.getUpdatedAt(), TenantSettings_.updatedAt));
            }
            if (criteria.getTenantOrganizationId() != null) {
                specification = specification.and(
                        buildSpecification(
                                criteria.getTenantOrganizationId(),
                                root -> root.join(TenantSettings_.tenantOrganization, JoinType.LEFT)
                                        .get(TenantOrganization_.id)));
            }
        }
        return specification;
    }
}
