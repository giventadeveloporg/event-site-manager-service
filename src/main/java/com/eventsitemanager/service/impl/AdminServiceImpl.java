package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.UserProfile;
import com.eventsitemanager.repository.UserProfileRepository;
import com.eventsitemanager.service.AdminService;
import com.eventsitemanager.service.ClerkIntegrationService;
import com.eventsitemanager.service.PermissionService;
import com.eventsitemanager.service.UserProfileQueryService;
import com.eventsitemanager.service.criteria.UserProfileCriteria;
import com.eventsitemanager.service.dto.AdminUserResponse;
import com.eventsitemanager.service.dto.UpdateRoleRequest;
import com.eventsitemanager.service.dto.UpdateStatusRequest;
import com.eventsitemanager.service.dto.UserProfileDTO;
import java.time.ZonedDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of AdminService for admin operations on users.
 */
@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private static final Logger log = LoggerFactory.getLogger(AdminServiceImpl.class);

    private final UserProfileRepository userProfileRepository;
    private final UserProfileQueryService userProfileQueryService;
    private final PermissionService permissionService;
    private final ClerkIntegrationService clerkIntegrationService;

    public AdminServiceImpl(
        UserProfileRepository userProfileRepository,
        UserProfileQueryService userProfileQueryService,
        PermissionService permissionService,
        ClerkIntegrationService clerkIntegrationService
    ) {
        this.userProfileRepository = userProfileRepository;
        this.userProfileQueryService = userProfileQueryService;
        this.permissionService = permissionService;
        this.clerkIntegrationService = clerkIntegrationService;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserProfileDTO> listUsers(UserProfileCriteria criteria, Pageable pageable, String adminClerkUserId, String tenantId) {
        log.info("Admin {} requesting user list for tenant {}", adminClerkUserId, tenantId);

        // Check if user is admin
        if (!permissionService.isAdmin(adminClerkUserId, tenantId)) {
            log.warn("Non-admin user {} attempted to list users", adminClerkUserId);
            throw new AccessDeniedException("Admin access required");
        }

        // Apply tenant filter if not provided
        if (criteria.getTenantId() == null) {
            criteria.setTenantId(new tech.jhipster.service.filter.StringFilter());
            criteria.getTenantId().setEquals(tenantId);
        }

        Page<UserProfileDTO> users = userProfileQueryService.findByCriteria(criteria, pageable);
        log.info("Returning {} users for tenant {}", users.getContent().size(), tenantId);

        return users;
    }

    @Override
    public AdminUserResponse updateUserRole(Long userId, UpdateRoleRequest request, String adminClerkUserId, String tenantId) {
        log.info("Admin {} updating role for user {} in tenant {}", adminClerkUserId, userId, tenantId);

        // Check if user is admin
        if (!permissionService.isAdmin(adminClerkUserId, tenantId)) {
            log.warn("Non-admin user {} attempted to update user role", adminClerkUserId);
            throw new AccessDeniedException("Admin access required");
        }

        // Find user profile
        UserProfile userProfile = userProfileRepository
            .findById(userId)
            .orElseThrow(() -> {
                log.warn("User profile not found: {}", userId);
                return new RuntimeException("User profile not found");
            });

        // Verify user belongs to the same tenant
        if (!tenantId.equals(userProfile.getTenantId())) {
            log.warn("User {} does not belong to tenant {}", userId, tenantId);
            throw new AccessDeniedException("Cannot update users from different tenant");
        }

        // Update role
        String oldRole = userProfile.getUserRole();
        userProfile.setUserRole(request.getRole());
        userProfile.setUpdatedAt(ZonedDateTime.now());

        if (request.getReason() != null) {
            userProfile.setAdminComments(request.getReason());
        }

        userProfileRepository.save(userProfile);

        log.info("Updated user {} role from {} to {}", userId, oldRole, request.getRole());

        // Prepare response
        AdminUserResponse response = new AdminUserResponse();
        response.setSuccess(true);
        response.setMessage("User role updated successfully");
        response.setUserId(userId);
        response.setUpdatedField("userRole");
        response.setNewValue(request.getRole());
        response.setUpdatedAt(ZonedDateTime.now());

        return response;
    }

    @Override
    public AdminUserResponse updateUserStatus(Long userId, UpdateStatusRequest request, String adminClerkUserId, String tenantId) {
        log.info("Admin {} updating status for user {} in tenant {}", adminClerkUserId, userId, tenantId);

        // Check if user is admin
        if (!permissionService.isAdmin(adminClerkUserId, tenantId)) {
            log.warn("Non-admin user {} attempted to update user status", adminClerkUserId);
            throw new AccessDeniedException("Admin access required");
        }

        // Find user profile
        UserProfile userProfile = userProfileRepository
            .findById(userId)
            .orElseThrow(() -> {
                log.warn("User profile not found: {}", userId);
                return new RuntimeException("User profile not found");
            });

        // Verify user belongs to the same tenant
        if (!tenantId.equals(userProfile.getTenantId())) {
            log.warn("User {} does not belong to tenant {}", userId, tenantId);
            throw new AccessDeniedException("Cannot update users from different tenant");
        }

        // Update status
        String oldStatus = userProfile.getUserStatus();
        userProfile.setUserStatus(request.getStatus());
        userProfile.setUpdatedAt(ZonedDateTime.now());

        if (request.getReason() != null) {
            userProfile.setAdminComments(request.getReason());
        }

        userProfileRepository.save(userProfile);

        // Revoke sessions if user is being suspended
        if ("SUSPENDED".equals(request.getStatus()) && Boolean.TRUE.equals(request.getRevokeSessions())) {
            try {
                List<java.util.Map<String, Object>> sessions = clerkIntegrationService.getUserSessions(userProfile.getUserId());
                log.info("Revoking {} sessions for suspended user {}", sessions.size(), userId);

                for (java.util.Map<String, Object> session : sessions) {
                    String sessionId = (String) session.get("id");
                    if (sessionId != null) {
                        try {
                            clerkIntegrationService.revokeSession(sessionId);
                            log.debug("Revoked session: {}", sessionId);
                        } catch (Exception e) {
                            log.error("Error revoking session {}", sessionId, e);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("Error revoking sessions for user {}", userId, e);
                // Don't fail the status update if session revocation fails
            }
        }

        log.info("Updated user {} status from {} to {}", userId, oldStatus, request.getStatus());

        // Prepare response
        AdminUserResponse response = new AdminUserResponse();
        response.setSuccess(true);
        response.setMessage("User status updated successfully");
        response.setUserId(userId);
        response.setUpdatedField("userStatus");
        response.setNewValue(request.getStatus());
        response.setUpdatedAt(ZonedDateTime.now());

        return response;
    }
}
