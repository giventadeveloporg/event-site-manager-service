package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.repository.UserProfileRepository;
import com.nextjstemplate.service.TenantService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of TenantService for multi-tenant operations.
 */
@Service
@Transactional
public class TenantServiceImpl implements TenantService {

    private static final Logger log = LoggerFactory.getLogger(TenantServiceImpl.class);

    private final UserProfileRepository userProfileRepository;

    @Value("${tenant-isolation-strategy:DATABASE_PER_TENANT}")
    private String tenantIsolationStrategy;

    @Value("${default-tenant:tenant_demo_001}")
    private String defaultTenant;

    @Value("${allowed-domains:localhost,127.0.0.1}")
    private String allowedDomains;

    public TenantServiceImpl(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public boolean isValidTenant(String tenantId) {
        if (tenantId == null || tenantId.trim().isEmpty()) {
            log.debug("Tenant ID is null or empty");
            return false;
        }

        // In a real application, you might check against a tenant registry/database
        // For now, we validate based on existing user profiles or allow any tenant
        log.debug("Validating tenant: {}", tenantId);
        return true; // Allow any tenant for flexibility
    }

    @Override
    public List<UserProfile> getUsersForTenant(String tenantId) {
        log.debug("Fetching users for tenant: {}", tenantId);

        if (!isValidTenant(tenantId)) {
            log.warn("Invalid tenant ID: {}", tenantId);
            return new ArrayList<>();
        }

        List<UserProfile> users = userProfileRepository
            .findAll()
            .stream()
            .filter(user -> tenantId.equals(user.getTenantId()))
            .collect(Collectors.toList());

        log.debug("Found {} users for tenant: {}", users.size(), tenantId);
        return users;
    }

    @Override
    public List<String> getUserTenants(String userEmail) {
        log.debug("Fetching tenants for user: {}", userEmail);

        List<UserProfile> userProfiles = userProfileRepository
            .findAll()
            .stream()
            .filter(user -> userEmail.equals(user.getEmail()))
            .collect(Collectors.toList());

        List<String> tenantIds = userProfiles.stream().map(UserProfile::getTenantId).distinct().collect(Collectors.toList());

        log.debug("User {} belongs to {} tenants", userEmail, tenantIds.size());
        return tenantIds;
    }

    @Override
    public boolean userBelongsToTenant(String userEmail, String tenantId) {
        log.debug("Checking if user {} belongs to tenant {}", userEmail, tenantId);

        Optional<UserProfile> userProfile = userProfileRepository.findByEmailAndTenantId(userEmail, tenantId);
        boolean belongs = userProfile.isPresent();

        log.debug("User {} {} to tenant {}", userEmail, belongs ? "belongs" : "does not belong", tenantId);
        return belongs;
    }

    @Override
    public Optional<UserProfile> addUserToTenant(String userEmail, String tenantId) {
        log.info("Adding user {} to tenant {}", userEmail, tenantId);

        if (!isValidTenant(tenantId)) {
            log.warn("Cannot add user to invalid tenant: {}", tenantId);
            return Optional.empty();
        }

        // Check if user already exists in this tenant
        Optional<UserProfile> existing = userProfileRepository.findByEmailAndTenantId(userEmail, tenantId);
        if (existing.isPresent()) {
            log.info("User already exists in tenant, returning existing profile");
            return existing;
        }

        // For adding to a new tenant, we would need additional user information
        // This is typically called after user creation with full details
        log.warn("User profile must be created with all required details first");
        return Optional.empty();
    }

    @Override
    public boolean removeUserFromTenant(String userEmail, String tenantId) {
        log.info("Removing user {} from tenant {}", userEmail, tenantId);

        Optional<UserProfile> userProfile = userProfileRepository.findByEmailAndTenantId(userEmail, tenantId);

        if (userProfile.isEmpty()) {
            log.warn("User profile not found for email {} and tenant {}", userEmail, tenantId);
            return false;
        }

        // Note: In a production system, you might want to soft-delete or archive
        // instead of hard delete
        try {
            UserProfile profile = userProfile.orElseThrow(() -> new RuntimeException("User profile not found"));
            userProfileRepository.delete(profile);
            log.info("Successfully removed user {} from tenant {}", userEmail, tenantId);
            return true;
        } catch (Exception e) {
            log.error("Error removing user from tenant", e);
            return false;
        }
    }

    @Override
    public Optional<UserProfile> switchTenant(String clerkUserId, String targetTenantId) {
        log.info("Switching tenant for user {} to {}", clerkUserId, targetTenantId);

        if (!isValidTenant(targetTenantId)) {
            log.warn("Cannot switch to invalid tenant: {}", targetTenantId);
            return Optional.empty();
        }

        // Find user profile for the target tenant
        Optional<UserProfile> userProfile = userProfileRepository
            .findAll()
            .stream()
            .filter(user -> clerkUserId.equals(user.getUserId()) && targetTenantId.equals(user.getTenantId()))
            .findFirst();

        if (userProfile.isEmpty()) {
            log.warn("User {} does not have access to tenant {}", clerkUserId, targetTenantId);
            return Optional.empty();
        }

        log.info("Successfully switched to tenant {} for user {}", targetTenantId, clerkUserId);
        return userProfile;
    }

    @Override
    public String getDefaultTenant() {
        return defaultTenant;
    }

    @Override
    public boolean validateTenantAccess(String clerkUserId, String tenantId) {
        log.debug("Validating tenant access for user {} to tenant {}", clerkUserId, tenantId);

        if (!isValidTenant(tenantId)) {
            log.warn("Invalid tenant ID: {}", tenantId);
            return false;
        }

        // Check if user has a profile in this tenant
        Optional<UserProfile> userProfile = userProfileRepository
            .findAll()
            .stream()
            .filter(user -> clerkUserId.equals(user.getUserId()) && tenantId.equals(user.getTenantId()))
            .findFirst();

        boolean hasAccess = userProfile.isPresent();
        log.debug("User {} {} access to tenant {}", clerkUserId, hasAccess ? "has" : "does not have", tenantId);
        return hasAccess;
    }
}
