package com.eventsitemanager.service;

import com.eventsitemanager.domain.*; // for static metamodels
import com.eventsitemanager.domain.UserProfile;
import com.eventsitemanager.repository.UserProfileRepository;
import com.eventsitemanager.service.criteria.UserProfileCriteria;
import com.eventsitemanager.service.dto.UserProfileDTO;
import com.eventsitemanager.service.mapper.UserProfileMapper;
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

/**
 * Service for executing complex queries for {@link UserProfile} entities in the database.
 * The main input is a {@link UserProfileCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserProfileDTO} or a {@link Page} of {@link UserProfileDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserProfileQueryService extends QueryService<UserProfile> {

    private final Logger log = LoggerFactory.getLogger(UserProfileQueryService.class);

    private final UserProfileRepository userProfileRepository;

    private final UserProfileMapper userProfileMapper;

    public UserProfileQueryService(UserProfileRepository userProfileRepository, UserProfileMapper userProfileMapper) {
        this.userProfileRepository = userProfileRepository;
        this.userProfileMapper = userProfileMapper;
    }

    /**
     * Return a {@link List} of {@link UserProfileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserProfileDTO> findByCriteria(UserProfileCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UserProfile> specification = createSpecification(criteria);
        return userProfileMapper.toDto(userProfileRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UserProfileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserProfileDTO> findByCriteria(UserProfileCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserProfile> specification = createSpecification(criteria);
        return userProfileRepository.findAll(specification, page).map(userProfileMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserProfileCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UserProfile> specification = createSpecification(criteria);
        return userProfileRepository.count(specification);
    }

    /**
     * Function to convert {@link UserProfileCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserProfile> createSpecification(UserProfileCriteria criteria) {
        Specification<UserProfile> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UserProfile_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), UserProfile_.tenantId));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserId(), UserProfile_.userId));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), UserProfile_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), UserProfile_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), UserProfile_.email));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), UserProfile_.phone));
            }
            if (criteria.getAddressLine1() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddressLine1(), UserProfile_.addressLine1));
            }
            if (criteria.getAddressLine2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddressLine2(), UserProfile_.addressLine2));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), UserProfile_.city));
            }
            if (criteria.getState() != null) {
                specification = specification.and(buildStringSpecification(criteria.getState(), UserProfile_.state));
            }
            if (criteria.getZipCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getZipCode(), UserProfile_.zipCode));
            }
            if (criteria.getCountry() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountry(), UserProfile_.country));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), UserProfile_.notes));
            }
            if (criteria.getFamilyName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFamilyName(), UserProfile_.familyName));
            }
            if (criteria.getCityTown() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCityTown(), UserProfile_.cityTown));
            }
            if (criteria.getDistrict() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDistrict(), UserProfile_.district));
            }
            if (criteria.getEducationalInstitution() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getEducationalInstitution(), UserProfile_.educationalInstitution));
            }
            if (criteria.getProfileImageUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProfileImageUrl(), UserProfile_.profileImageUrl));
            }
            if (criteria.getIsEmailSubscribed() != null) {
                specification = specification.and(buildSpecification(criteria.getIsEmailSubscribed(), UserProfile_.isEmailSubscribed));
            }
            if (criteria.getEmailSubscriptionToken() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getEmailSubscriptionToken(), UserProfile_.emailSubscriptionToken));
            }
            if (criteria.getIsEmailSubscriptionTokenUsed() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getIsEmailSubscriptionTokenUsed(), UserProfile_.isEmailSubscriptionTokenUsed)
                    );
            }
            if (criteria.getUserStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserStatus(), UserProfile_.userStatus));
            }
            if (criteria.getUserRole() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserRole(), UserProfile_.userRole));
            }
            if (criteria.getReviewedByAdminAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReviewedByAdminAt(), UserProfile_.reviewedByAdminAt));
            }
            if (criteria.getRequestId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRequestId(), UserProfile_.requestId));
            }
            if (criteria.getRequestReason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRequestReason(), UserProfile_.requestReason));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), UserProfile_.status));
            }
            if (criteria.getAdminComments() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAdminComments(), UserProfile_.adminComments));
            }
            if (criteria.getSubmittedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSubmittedAt(), UserProfile_.submittedAt));
            }
            if (criteria.getReviewedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReviewedAt(), UserProfile_.reviewedAt));
            }
            if (criteria.getApprovedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getApprovedAt(), UserProfile_.approvedAt));
            }
            if (criteria.getRejectedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRejectedAt(), UserProfile_.rejectedAt));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), UserProfile_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), UserProfile_.updatedAt));
            }
            if (criteria.getReviewedByAdminId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getReviewedByAdminId(),
                            root -> root.join(UserProfile_.reviewedByAdmin, JoinType.LEFT).get(UserProfile_.id)
                        )
                    );
            }
            if (criteria.getUserSubscriptionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUserSubscriptionId(),
                            root -> root.join(UserProfile_.userSubscription, JoinType.LEFT).get(UserSubscription_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
