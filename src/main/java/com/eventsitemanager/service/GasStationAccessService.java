package com.eventsitemanager.service;

import com.eventsitemanager.service.criteria.GasStationLocationCriteria;
import com.eventsitemanager.service.criteria.GasStationUserStationAssignmentCriteria;
import java.util.Set;
import org.springframework.data.jpa.domain.Specification;
import tech.jhipster.service.filter.LongFilter;

/**
 * Location-scoped RBAC for the gas station COO module.
 */
public interface GasStationAccessService {
    /** Service JWT / no user profile — skip RBAC. */
    boolean isUnrestrictedServiceAccess();

    /** Throws if caller cannot access gas station admin APIs at all. */
    void assertGasModuleAccess();

    /** Throws if caller cannot manage all tenant locations (assignments, create stations, etc.). */
    void assertAllLocationsScope();

    /**
     * Allowed station IDs for the current user, or null when unrestricted (all locations).
     * Empty set = manager with no assignments.
     */
    Set<Long> getAllowedStationIdsOrNull();

    Long getCurrentUserProfileIdOrNull();

    void assertLocationIdAllowed(Long locationId);

    void assertStationIdAllowed(Long stationId);

    void assertRecommendationAccess(Long stationId);

    void applyLocationCriteriaFilter(GasStationLocationCriteria criteria);

    void applyStationIdCriteriaFilter(LongFilter stationIdFilter);

    void applyAssignmentCriteriaFilter(GasStationUserStationAssignmentCriteria criteria);

    /** Managers see assigned stations plus tenant-level rows (stationId null). */
    Specification<com.eventsitemanager.domain.GasStationRecommendation> recommendationAccessSpecification();
}
