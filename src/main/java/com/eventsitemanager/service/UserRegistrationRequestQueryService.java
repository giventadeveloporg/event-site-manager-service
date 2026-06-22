package com.eventsitemanager.service;

import com.eventsitemanager.domain.*; // for static metamodels
import com.eventsitemanager.domain.UserRegistrationRequest;
import com.eventsitemanager.repository.UserRegistrationRequestRepository;
import com.eventsitemanager.service.criteria.UserRegistrationRequestCriteria;
import com.eventsitemanager.service.dto.UserRegistrationRequestDTO;
import com.eventsitemanager.service.mapper.UserRegistrationRequestMapper;
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
 * Service for executing complex queries for {@link UserRegistrationRequest} entities in the database.
 * The main input is a {@link UserRegistrationRequestCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserRegistrationRequestDTO} or a {@link Page} of {@link UserRegistrationRequestDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserRegistrationRequestQueryService extends QueryService<UserRegistrationRequest> {

    private final Logger log = LoggerFactory.getLogger(UserRegistrationRequestQueryService.class);

    private final UserRegistrationRequestRepository userRegistrationRequestRepository;

    private final UserRegistrationRequestMapper userRegistrationRequestMapper;

    public UserRegistrationRequestQueryService(
        UserRegistrationRequestRepository userRegistrationRequestRepository,
        UserRegistrationRequestMapper userRegistrationRequestMapper
    ) {
        this.userRegistrationRequestRepository = userRegistrationRequestRepository;
        this.userRegistrationRequestMapper = userRegistrationRequestMapper;
    }

    /**
     * Return a {@link List} of {@link UserRegistrationRequestDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserRegistrationRequestDTO> findByCriteria(UserRegistrationRequestCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UserRegistrationRequest> specification = createSpecification(criteria);
        return userRegistrationRequestMapper.toDto(userRegistrationRequestRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UserRegistrationRequestDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserRegistrationRequestDTO> findByCriteria(UserRegistrationRequestCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserRegistrationRequest> specification = createSpecification(criteria);
        return userRegistrationRequestRepository.findAll(specification, page).map(userRegistrationRequestMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserRegistrationRequestCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UserRegistrationRequest> specification = createSpecification(criteria);
        return userRegistrationRequestRepository.count(specification);
    }

    /**
     * Function to convert {@link UserRegistrationRequestCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserRegistrationRequest> createSpecification(UserRegistrationRequestCriteria criteria) {
        Specification<UserRegistrationRequest> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UserRegistrationRequest_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), UserRegistrationRequest_.tenantId));
            }
            if (criteria.getRequestId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRequestId(), UserRegistrationRequest_.requestId));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserId(), UserRegistrationRequest_.userId));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), UserRegistrationRequest_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), UserRegistrationRequest_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), UserRegistrationRequest_.email));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), UserRegistrationRequest_.phone));
            }
            if (criteria.getAddressLine1() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAddressLine1(), UserRegistrationRequest_.addressLine1));
            }
            if (criteria.getAddressLine2() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAddressLine2(), UserRegistrationRequest_.addressLine2));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), UserRegistrationRequest_.city));
            }
            if (criteria.getState() != null) {
                specification = specification.and(buildStringSpecification(criteria.getState(), UserRegistrationRequest_.state));
            }
            if (criteria.getZipCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getZipCode(), UserRegistrationRequest_.zipCode));
            }
            if (criteria.getCountry() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountry(), UserRegistrationRequest_.country));
            }
            if (criteria.getFamilyName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFamilyName(), UserRegistrationRequest_.familyName));
            }
            if (criteria.getCityTown() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCityTown(), UserRegistrationRequest_.cityTown));
            }
            if (criteria.getDistrict() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDistrict(), UserRegistrationRequest_.district));
            }
            if (criteria.getEducationalInstitution() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getEducationalInstitution(), UserRegistrationRequest_.educationalInstitution)
                    );
            }
            if (criteria.getProfileImageUrl() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getProfileImageUrl(), UserRegistrationRequest_.profileImageUrl));
            }
            if (criteria.getRequestReason() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getRequestReason(), UserRegistrationRequest_.requestReason));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), UserRegistrationRequest_.status));
            }
            if (criteria.getAdminComments() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAdminComments(), UserRegistrationRequest_.adminComments));
            }
            if (criteria.getSubmittedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSubmittedAt(), UserRegistrationRequest_.submittedAt));
            }
            if (criteria.getReviewedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReviewedAt(), UserRegistrationRequest_.reviewedAt));
            }
            if (criteria.getApprovedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getApprovedAt(), UserRegistrationRequest_.approvedAt));
            }
            if (criteria.getRejectedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRejectedAt(), UserRegistrationRequest_.rejectedAt));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), UserRegistrationRequest_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), UserRegistrationRequest_.updatedAt));
            }
            if (criteria.getReviewedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getReviewedById(),
                            root -> root.join(UserRegistrationRequest_.reviewedBy, JoinType.LEFT).get(UserProfile_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
