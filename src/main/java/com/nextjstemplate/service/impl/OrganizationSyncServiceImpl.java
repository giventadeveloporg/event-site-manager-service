package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.repository.UserProfileRepository;
import com.nextjstemplate.service.ClerkIntegrationService;
import com.nextjstemplate.service.OrganizationSyncService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of OrganizationSyncService for syncing Clerk organizations.
 */
@Service
@Transactional
public class OrganizationSyncServiceImpl implements OrganizationSyncService {

    private static final Logger log = LoggerFactory.getLogger(OrganizationSyncServiceImpl.class);

    private final ClerkIntegrationService clerkIntegrationService;
    private final UserProfileRepository userProfileRepository;

    public OrganizationSyncServiceImpl(ClerkIntegrationService clerkIntegrationService, UserProfileRepository userProfileRepository) {
        this.clerkIntegrationService = clerkIntegrationService;
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public List<Map<String, Object>> syncUserOrganizations(String clerkUserId) {
        log.info("Syncing organizations for user: {}", clerkUserId);

        try {
            // Get organization memberships from Clerk
            List<Map<String, Object>> memberships = clerkIntegrationService.getUserOrganizationMemberships(clerkUserId);

            List<Map<String, Object>> syncedOrganizations = new ArrayList<>();

            for (Map<String, Object> membership : memberships) {
                try {
                    // Extract organization and role information
                    Object orgObject = membership.get("organization");
                    if (!(orgObject instanceof Map)) {
                        continue;
                    }
                    @SuppressWarnings("unchecked")
                    Map<String, Object> organization = (Map<String, Object>) orgObject;
                    String organizationId = (String) organization.get("id");
                    String role = (String) membership.get("role");

                    // Sync this membership
                    Map<String, Object> syncedData = syncOrganizationMembership(clerkUserId, organizationId);
                    if (syncedData != null) {
                        syncedData.put("role", role);
                        syncedOrganizations.add(syncedData);
                    }

                    // Update user role in database
                    updateUserRoleFromClerkRole(clerkUserId, role, organizationId);
                } catch (Exception e) {
                    log.error("Error syncing individual organization membership", e);
                }
            }

            log.info("Successfully synced {} organizations for user: {}", syncedOrganizations.size(), clerkUserId);
            return syncedOrganizations;
        } catch (Exception e) {
            log.error("Error syncing user organizations for: {}", clerkUserId, e);
            return new ArrayList<>();
        }
    }

    @Override
    public Map<String, Object> syncOrganizationMembership(String clerkUserId, String organizationId) {
        log.debug("Syncing organization membership for user {} in organization {}", clerkUserId, organizationId);

        try {
            // Get organization details from Clerk
            List<Map<String, Object>> organizations = clerkIntegrationService.getUserOrganizations(clerkUserId);

            Map<String, Object> organizationData = organizations
                .stream()
                .filter(org -> organizationId.equals(org.get("id")))
                .findFirst()
                .orElseThrow(() -> {
                    log.warn("Organization {} not found for user {}", organizationId, clerkUserId);
                    return new RuntimeException("Organization not found");
                });

            // Prepare sync data
            Map<String, Object> syncData = new HashMap<>();
            syncData.put("clerk_user_id", clerkUserId);
            syncData.put("organization_id", organizationId);
            syncData.put("organization_name", organizationData.get("name"));
            syncData.put("organization_slug", organizationData.get("slug"));
            syncData.put("synced_at", java.time.ZonedDateTime.now().toString());

            log.debug("Organization membership synced: {}", syncData);
            return syncData;
        } catch (Exception e) {
            log.error("Error syncing organization membership", e);
            return null;
        }
    }

    @Override
    public String updateUserRoleFromClerkRole(String clerkUserId, String clerkRole, String tenantId) {
        log.info("Updating user role for {} with Clerk role: {} in tenant: {}", clerkUserId, clerkRole, tenantId);

        try {
            // Map Clerk role to application role
            String applicationRole = mapClerkRoleToApplicationRole(clerkRole);

            // Find user profile
            UserProfile userProfile = userProfileRepository
                .findAll()
                .stream()
                .filter(user -> clerkUserId.equals(user.getUserId()) && tenantId.equals(user.getTenantId()))
                .findFirst()
                .orElseThrow(() -> {
                    log.warn("User profile not found for user {} in tenant {}", clerkUserId, tenantId);
                    return new RuntimeException("User profile not found");
                });
            userProfile.setUserRole(applicationRole);
            userProfile.setUpdatedAt(java.time.ZonedDateTime.now());
            userProfileRepository.save(userProfile);

            log.info("Updated user role to: {}", applicationRole);
            return applicationRole;
        } catch (Exception e) {
            log.error("Error updating user role", e);
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getUserOrganizationMemberships(String clerkUserId) {
        log.debug("Fetching organization memberships for user: {}", clerkUserId);

        try {
            List<Map<String, Object>> memberships = clerkIntegrationService.getUserOrganizationMemberships(clerkUserId);
            log.debug("Found {} organization memberships for user: {}", memberships.size(), clerkUserId);
            return memberships;
        } catch (Exception e) {
            log.error("Error fetching organization memberships", e);
            return new ArrayList<>();
        }
    }

    @Override
    public boolean syncOrganizationToTenant(String organizationId) {
        log.info("Syncing organization {} to tenant settings", organizationId);

        try {
            // In a full implementation, this would:
            // 1. Fetch organization details from Clerk
            // 2. Create or update TenantOrganization record
            // 3. Update TenantSettings
            // For now, we log the operation

            log.info("Organization {} sync would be performed here", organizationId);
            return true;
        } catch (Exception e) {
            log.error("Error syncing organization to tenant", e);
            return false;
        }
    }

    /**
     * Map Clerk organization role to application role.
     *
     * Clerk roles: org:admin, org:member, org:billing, org:guest
     * Application roles: ADMIN, USER, MANAGER, GUEST
     */
    private String mapClerkRoleToApplicationRole(String clerkRole) {
        if (clerkRole == null) {
            return "USER";
        }

        return switch (clerkRole.toLowerCase()) {
            case "org:admin", "admin" -> "ADMIN";
            case "org:billing", "billing" -> "MANAGER";
            case "org:member", "member" -> "USER";
            case "org:guest", "guest" -> "GUEST";
            default -> "USER";
        };
    }
}
