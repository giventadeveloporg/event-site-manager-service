package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.UserProfile;
import com.eventsitemanager.properties.RoleMappingConfiguration;
import com.eventsitemanager.repository.UserProfileRepository;
import com.eventsitemanager.service.PermissionService;
import com.eventsitemanager.service.TenantService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of PermissionService for checking permissions and roles.
 */
@Service
@Transactional(readOnly = true)
public class PermissionServiceImpl implements PermissionService {

    private static final Logger log = LoggerFactory.getLogger(PermissionServiceImpl.class);

    private final UserProfileRepository userProfileRepository;
    private final TenantService tenantService;
    private final RoleMappingConfiguration roleMappingConfiguration;

    public PermissionServiceImpl(
        UserProfileRepository userProfileRepository,
        TenantService tenantService,
        RoleMappingConfiguration roleMappingConfiguration
    ) {
        this.userProfileRepository = userProfileRepository;
        this.tenantService = tenantService;
        this.roleMappingConfiguration = roleMappingConfiguration;
    }

    @Override
    public boolean hasRole(String clerkUserId, String role, String tenantId) {
        log.debug("Checking if user {} has role {} in tenant {}", clerkUserId, role, tenantId);

        String userRole = getUserRole(clerkUserId, tenantId);
        if (userRole == null) {
            return false;
        }

        boolean hasRole = userRole.equalsIgnoreCase(role);
        log.debug("User {} {} role {}", clerkUserId, hasRole ? "has" : "does not have", role);
        return hasRole;
    }

    @Override
    public boolean hasMinimumRole(String clerkUserId, String requiredRole, String tenantId) {
        log.debug("Checking if user {} has minimum role {} in tenant {}", clerkUserId, requiredRole, tenantId);

        String userRole = getUserRole(clerkUserId, tenantId);
        if (userRole == null) {
            return false;
        }

        boolean hasMinimumRole = roleMappingConfiguration.hasRole(userRole, requiredRole);
        log.debug("User {} {} minimum role {}", clerkUserId, hasMinimumRole ? "has" : "does not have", requiredRole);
        return hasMinimumRole;
    }

    @Override
    public boolean hasAnyRole(String clerkUserId, String[] roles, String tenantId) {
        log.debug("Checking if user {} has any of {} roles in tenant {}", clerkUserId, roles.length, tenantId);

        for (String role : roles) {
            if (hasRole(clerkUserId, role, tenantId)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean hasAllRoles(String clerkUserId, String[] roles, String tenantId) {
        log.debug("Checking if user {} has all {} roles in tenant {}", clerkUserId, roles.length, tenantId);

        for (String role : roles) {
            if (!hasRole(clerkUserId, role, tenantId)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean canAccessResource(String clerkUserId, String resourceId, String resourceType, String tenantId) {
        log.debug("Checking if user {} can access resource {} of type {} in tenant {}", clerkUserId, resourceId, resourceType, tenantId);

        // First, check if user has access to the tenant
        if (!hasTenantAccess(clerkUserId, tenantId)) {
            log.debug("User does not have tenant access");
            return false;
        }

        // Check if user is admin (admins can access all resources)
        if (isAdmin(clerkUserId, tenantId)) {
            log.debug("User is admin, granting access");
            return true;
        }

        // Resource-specific access control would go here
        // For now, USER role and above can access resources in their tenant
        boolean hasAccess = hasMinimumRole(clerkUserId, "USER", tenantId);
        log.debug("User {} access to resource", hasAccess ? "has" : "does not have");
        return hasAccess;
    }

    @Override
    public boolean canPerformAction(String clerkUserId, String action, String resourceId, String resourceType, String tenantId) {
        log.debug("Checking if user {} can perform action {} on resource {} in tenant {}", clerkUserId, action, resourceId, tenantId);

        // First, check basic resource access
        if (!canAccessResource(clerkUserId, resourceId, resourceType, tenantId)) {
            return false;
        }

        // Action-based permissions
        return switch (action.toLowerCase()) {
            case "read", "view" -> true; // Anyone with resource access can read
            case "create", "update", "write" -> hasMinimumRole(clerkUserId, "USER", tenantId); // USER and above can create/update
            case "delete", "archive" -> hasMinimumRole(clerkUserId, "MANAGER", tenantId); // MANAGER and above can delete
            case "admin", "manage" -> isAdmin(clerkUserId, tenantId); // Only ADMINs can perform admin actions
            default -> false; // Unknown action, deny by default
        };
    }

    @Override
    public boolean isAdmin(String clerkUserId, String tenantId) {
        log.debug("Checking if user {} is admin in tenant {}", clerkUserId, tenantId);

        String userRole = getUserRole(clerkUserId, tenantId);
        if (userRole == null) {
            return false;
        }

        boolean isAdmin = userRole.equalsIgnoreCase("ADMIN") || userRole.equalsIgnoreCase("SUPER_ADMIN");
        log.debug("User {} {} admin", clerkUserId, isAdmin ? "is" : "is not");
        return isAdmin;
    }

    @Override
    public boolean isSuperAdmin(String clerkUserId) {
        log.debug("Checking if user {} is super admin", clerkUserId);

        // Check all user profiles to see if any have SUPER_ADMIN role
        boolean isSuperAdmin = userProfileRepository
            .findAll()
            .stream()
            .anyMatch(user -> clerkUserId.equals(user.getUserId()) && "SUPER_ADMIN".equalsIgnoreCase(user.getUserRole()));

        log.debug("User {} {} super admin", clerkUserId, isSuperAdmin ? "is" : "is not");
        return isSuperAdmin;
    }

    @Override
    public String getUserRole(String clerkUserId, String tenantId) {
        log.debug("Getting role for user {} in tenant {}", clerkUserId, tenantId);

        Optional<UserProfile> userProfileOpt = userProfileRepository
            .findAll()
            .stream()
            .filter(user -> clerkUserId.equals(user.getUserId()) && tenantId.equals(user.getTenantId()))
            .findFirst();

        if (!userProfileOpt.isPresent()) {
            log.debug("User profile not found");
            return null;
        }

        String role = userProfileOpt.orElseThrow().getUserRole();
        log.debug("User role: {}", role);
        return role;
    }

    @Override
    public boolean hasTenantAccess(String clerkUserId, String tenantId) {
        log.debug("Checking if user {} has access to tenant {}", clerkUserId, tenantId);

        boolean hasAccess = tenantService.validateTenantAccess(clerkUserId, tenantId);
        log.debug("User {} {} access to tenant", clerkUserId, hasAccess ? "has" : "does not have");
        return hasAccess;
    }
}
