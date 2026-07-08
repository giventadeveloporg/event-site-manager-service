package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.GasStationRecommendation;
import com.eventsitemanager.domain.GasStationRecommendation_;
import com.eventsitemanager.domain.UserProfile;
import com.eventsitemanager.domain.enumeration.UserRoleType;
import com.eventsitemanager.repository.GasStationUserStationAssignmentRepository;
import com.eventsitemanager.repository.UserProfileRepository;
import com.eventsitemanager.security.AuthoritiesConstants;
import com.eventsitemanager.security.SecurityUtils;
import com.eventsitemanager.security.TenantContext;
import com.eventsitemanager.service.GasStationAccessService;
import com.eventsitemanager.service.criteria.GasStationLocationCriteria;
import com.eventsitemanager.service.criteria.GasStationUserStationAssignmentCriteria;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tech.jhipster.service.filter.LongFilter;

@Service
@Transactional(readOnly = true)
public class GasStationAccessServiceImpl implements GasStationAccessService {

    private static final Logger LOG = LoggerFactory.getLogger(GasStationAccessServiceImpl.class);
    private static final String SERVICE_JWT_SUBJECT = "jwtadmin";

    private static final Set<String> ALL_LOCATIONS_ROLES = Set.of(
        UserRoleType.SUPER_ADMIN.name(),
        UserRoleType.ADMIN.name(),
        UserRoleType.GAS_STATION_ADMIN.name()
    );

    private static final Set<String> GAS_MODULE_ROLES = Set.of(
        UserRoleType.SUPER_ADMIN.name(),
        UserRoleType.ADMIN.name(),
        UserRoleType.GAS_STATION_ADMIN.name(),
        UserRoleType.GAS_STATION_MANAGER.name()
    );

    private final UserProfileRepository userProfileRepository;
    private final GasStationUserStationAssignmentRepository assignmentRepository;

    public GasStationAccessServiceImpl(
        UserProfileRepository userProfileRepository,
        GasStationUserStationAssignmentRepository assignmentRepository
    ) {
        this.userProfileRepository = userProfileRepository;
        this.assignmentRepository = assignmentRepository;
    }

    @Override
    public boolean isUnrestrictedServiceAccess() {
        if (!SecurityUtils.isAuthenticated()) {
            return false;
        }
        Optional<String> login = SecurityUtils.getCurrentUserLogin();
        if (login.filter(SERVICE_JWT_SUBJECT::equals).isPresent()) {
            return true;
        }
        Optional<UserProfile> profile = resolveCurrentUserProfile();
        return profile.isEmpty() && SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN);
    }

    @Override
    public void assertGasModuleAccess() {
        if (isUnrestrictedServiceAccess()) {
            return;
        }
        UserProfile profile = resolveCurrentUserProfile()
            .orElseThrow(() -> gasAccessDenied("Gas station access requires an authenticated user profile."));
        String role = normalizeRole(profile.getUserRole());
        if (!GAS_MODULE_ROLES.contains(role)) {
            throw gasAccessDenied("User does not have gas station module access.");
        }
    }

    @Override
    public void assertAllLocationsScope() {
        assertGasModuleAccess();
        if (isUnrestrictedServiceAccess()) {
            return;
        }
        UserProfile profile = resolveCurrentUserProfile().orElseThrow(() -> gasAccessDenied("Gas station admin role required."));
        String role = normalizeRole(profile.getUserRole());
        if (!ALL_LOCATIONS_ROLES.contains(role)) {
            throw gasAccessDenied("Gas station tenant admin role required for this action.");
        }
    }

    @Override
    public Set<Long> getAllowedStationIdsOrNull() {
        if (isUnrestrictedServiceAccess()) {
            return null;
        }
        UserProfile profile = resolveCurrentUserProfile().orElse(null);
        if (profile == null) {
            return Collections.emptySet();
        }
        String role = normalizeRole(profile.getUserRole());
        if (ALL_LOCATIONS_ROLES.contains(role)) {
            return null;
        }
        if (!UserRoleType.GAS_STATION_MANAGER.name().equals(role)) {
            return Collections.emptySet();
        }
        String tenantId = requireTenantId();
        return assignmentRepository
            .findByTenantIdAndUserProfileId(tenantId, profile.getId())
            .stream()
            .map(a -> a.getStationId())
            .collect(Collectors.toCollection(HashSet::new));
    }

    @Override
    public Long getCurrentUserProfileIdOrNull() {
        return resolveCurrentUserProfile().map(UserProfile::getId).orElse(null);
    }

    @Override
    public void assertLocationIdAllowed(Long locationId) {
        assertGasModuleAccess();
        if (locationId == null) {
            throw gasAccessDenied("Location id is required.");
        }
        Set<Long> allowed = getAllowedStationIdsOrNull();
        if (allowed != null && !allowed.contains(locationId)) {
            throw gasAccessDenied("Access denied for gas station location " + locationId);
        }
    }

    @Override
    public void assertStationIdAllowed(Long stationId) {
        assertLocationIdAllowed(stationId);
    }

    @Override
    public void assertRecommendationAccess(Long stationId) {
        assertGasModuleAccess();
        if (isUnrestrictedServiceAccess()) {
            return;
        }
        Set<Long> allowed = getAllowedStationIdsOrNull();
        if (allowed == null) {
            return;
        }
        if (stationId == null) {
            if (allowed.isEmpty()) {
                throw gasAccessDenied("Access denied for tenant-level gas station recommendation.");
            }
            return;
        }
        if (!allowed.contains(stationId)) {
            throw gasAccessDenied("Access denied for gas station recommendation at station " + stationId);
        }
    }

    @Override
    public void applyLocationCriteriaFilter(GasStationLocationCriteria criteria) {
        assertGasModuleAccess();
        if (isUnrestrictedServiceAccess()) {
            return;
        }
        Set<Long> allowed = getAllowedStationIdsOrNull();
        if (allowed == null) {
            return;
        }
        LongFilter idFilter = criteria.getId();
        if (idFilter == null) {
            idFilter = new LongFilter();
            criteria.setId(idFilter);
        }
        mergeLongFilterIn(idFilter, allowed);
    }

    @Override
    public void applyStationIdCriteriaFilter(LongFilter stationIdFilter) {
        assertGasModuleAccess();
        if (isUnrestrictedServiceAccess()) {
            return;
        }
        Set<Long> allowed = getAllowedStationIdsOrNull();
        if (allowed == null) {
            return;
        }
        if (stationIdFilter == null) {
            return;
        }
        mergeLongFilterIn(stationIdFilter, allowed);
    }

    @Override
    public void applyAssignmentCriteriaFilter(GasStationUserStationAssignmentCriteria criteria) {
        assertGasModuleAccess();
        if (isUnrestrictedServiceAccess()) {
            return;
        }
        UserProfile profile = resolveCurrentUserProfile().orElse(null);
        if (profile == null) {
            throw gasAccessDenied("Gas station access requires an authenticated user profile.");
        }
        String role = normalizeRole(profile.getUserRole());
        if (ALL_LOCATIONS_ROLES.contains(role)) {
            return;
        }
        if (UserRoleType.GAS_STATION_MANAGER.name().equals(role)) {
            LongFilter userFilter = criteria.getUserProfileId();
            if (userFilter == null) {
                userFilter = new LongFilter();
                criteria.setUserProfileId(userFilter);
            }
            userFilter.setEquals(profile.getId());
            Set<Long> allowed = getAllowedStationIdsOrNull();
            if (allowed != null) {
                LongFilter stationFilter = criteria.getStationId();
                if (stationFilter == null) {
                    stationFilter = new LongFilter();
                    criteria.setStationId(stationFilter);
                }
                mergeLongFilterIn(stationFilter, allowed);
            }
            return;
        }
        throw gasAccessDenied("User does not have gas station assignment access.");
    }

    @Override
    public Specification<GasStationRecommendation> recommendationAccessSpecification() {
        if (isUnrestrictedServiceAccess()) {
            return Specification.where(null);
        }
        Set<Long> allowed = getAllowedStationIdsOrNull();
        if (allowed == null) {
            return Specification.where(null);
        }
        if (allowed.isEmpty()) {
            return (root, query, cb) -> cb.disjunction();
        }
        return (root, query, cb) ->
            cb.or(cb.isNull(root.get(GasStationRecommendation_.stationId)), root.get(GasStationRecommendation_.stationId).in(allowed));
    }

    private void mergeLongFilterIn(LongFilter filter, Set<Long> allowed) {
        if (allowed.isEmpty()) {
            filter.setIn(List.of(-1L));
            return;
        }
        if (filter.getEquals() != null) {
            if (!allowed.contains(filter.getEquals())) {
                throw gasAccessDenied("Access denied for requested station filter.");
            }
            return;
        }
        if (filter.getIn() != null && !filter.getIn().isEmpty()) {
            List<Long> intersected = filter.getIn().stream().filter(allowed::contains).toList();
            filter.setIn(intersected.isEmpty() ? List.of(-1L) : intersected);
            return;
        }
        filter.setIn(allowed.stream().sorted().toList());
    }

    private Optional<UserProfile> resolveCurrentUserProfile() {
        Optional<String> login = SecurityUtils.getCurrentUserLogin();
        if (login.isEmpty()) {
            return Optional.empty();
        }
        String tenantId = TenantContext.getCurrentTenant();
        if (!StringUtils.hasText(tenantId)) {
            return Optional.empty();
        }
        return userProfileRepository.findByUserIdAndTenantId(login.orElseThrow(), tenantId);
    }

    private String requireTenantId() {
        String tenantId = TenantContext.getCurrentTenant();
        if (!StringUtils.hasText(tenantId)) {
            throw gasAccessDenied("Tenant context is required for gas station location access.");
        }
        return tenantId;
    }

    private static String normalizeRole(String role) {
        return role == null ? "" : role.trim().toUpperCase();
    }

    private static AccessDeniedException gasAccessDenied(String message) {
        return new AccessDeniedException(message);
    }
}
