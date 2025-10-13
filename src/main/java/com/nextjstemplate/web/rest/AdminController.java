package com.nextjstemplate.web.rest;

import com.nextjstemplate.security.TenantContext;
import com.nextjstemplate.service.AdminService;
import com.nextjstemplate.service.criteria.UserProfileCriteria;
import com.nextjstemplate.service.dto.AdminUserResponse;
import com.nextjstemplate.service.dto.UpdateRoleRequest;
import com.nextjstemplate.service.dto.UpdateStatusRequest;
import com.nextjstemplate.service.dto.UserProfileDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

/**
 * REST controller for admin operations on users.
 */
@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin User Management", description = "Admin endpoints for managing users")
public class AdminController {

    private final Logger log = LoggerFactory.getLogger(AdminController.class);

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * GET /api/admin/users : Get all users (admin only)
     *
     * @param criteria the filtering criteria
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and list of users
     */
    @GetMapping("/users")
    @Operation(summary = "List all users", description = "Get paginated list of users with filtering (admin only)")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied - admin privileges required"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
        }
    )
    public ResponseEntity<List<UserProfileDTO>> listUsers(
        @org.springdoc.core.annotations.ParameterObject UserProfileCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.info("Admin request to list users");

        try {
            // Get admin's Clerk user ID from security context
            String adminClerkUserId = extractClerkUserIdFromContext();

            // Get tenant ID from context
            String tenantId = TenantContext.getCurrentTenant();
            if (tenantId == null) {
                log.warn("No tenant context available");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // List users with admin authorization check
            Page<UserProfileDTO> page = adminService.listUsers(criteria, pageable, adminClerkUserId, tenantId);

            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (AccessDeniedException e) {
            log.warn("Access denied: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            log.error("Error listing users", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * PUT /api/admin/users/{userId}/role : Update user role (admin only)
     *
     * @param userId  the user profile ID
     * @param request the role update request
     * @return the ResponseEntity with status 200 (OK) and update response
     */
    @PutMapping("/users/{userId}/role")
    @Operation(summary = "Update user role", description = "Update a user's role (admin only)")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Role updated successfully",
                content = @Content(schema = @Schema(implementation = AdminUserResponse.class))
            ),
            @ApiResponse(responseCode = "403", description = "Access denied - admin privileges required"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
        }
    )
    public ResponseEntity<AdminUserResponse> updateUserRole(@PathVariable Long userId, @Valid @RequestBody UpdateRoleRequest request) {
        log.info("Admin request to update role for user {}", userId);

        try {
            // Get admin's Clerk user ID from security context
            String adminClerkUserId = extractClerkUserIdFromContext();

            // Get tenant ID from context
            String tenantId = TenantContext.getCurrentTenant();
            if (tenantId == null) {
                log.warn("No tenant context available");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // Update user role with admin authorization check
            AdminUserResponse response = adminService.updateUserRole(userId, request, adminClerkUserId, tenantId);

            return ResponseEntity.ok(response);
        } catch (AccessDeniedException e) {
            log.warn("Access denied: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            log.error("Error updating user role", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * PUT /api/admin/users/{userId}/status : Update user status (admin only)
     *
     * @param userId  the user profile ID
     * @param request the status update request
     * @return the ResponseEntity with status 200 (OK) and update response
     */
    @PutMapping("/users/{userId}/status")
    @Operation(summary = "Update user status", description = "Update a user's status and optionally revoke sessions (admin only)")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Status updated successfully",
                content = @Content(schema = @Schema(implementation = AdminUserResponse.class))
            ),
            @ApiResponse(responseCode = "403", description = "Access denied - admin privileges required"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
        }
    )
    public ResponseEntity<AdminUserResponse> updateUserStatus(@PathVariable Long userId, @Valid @RequestBody UpdateStatusRequest request) {
        log.info("Admin request to update status for user {} to {}", userId, request.getStatus());

        try {
            // Get admin's Clerk user ID from security context
            String adminClerkUserId = extractClerkUserIdFromContext();

            // Get tenant ID from context
            String tenantId = TenantContext.getCurrentTenant();
            if (tenantId == null) {
                log.warn("No tenant context available");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // Update user status with admin authorization check
            AdminUserResponse response = adminService.updateUserStatus(userId, request, adminClerkUserId, tenantId);

            return ResponseEntity.ok(response);
        } catch (AccessDeniedException e) {
            log.warn("Access denied: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            log.error("Error updating user status", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Extract Clerk user ID from Spring Security context.
     */
    private String extractClerkUserIdFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            log.warn("No authentication found in security context");
            throw new AccessDeniedException("Authentication required");
        }

        // The principal should be the Clerk user ID set by ClerkJwtAuthenticationFilter
        String clerkUserId = authentication.getPrincipal().toString();
        log.debug("Extracted Clerk user ID from context: {}", clerkUserId);

        return clerkUserId;
    }
}
