package com.eventsitemanager.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * DTO for admin role update request.
 */
@Schema(description = "Request body for updating user role")
public class UpdateRoleRequest implements Serializable {

    @NotBlank(message = "Role is required")
    @Pattern(regexp = "GUEST|USER|MANAGER|ADMIN|SUPER_ADMIN", message = "Role must be one of: GUEST, USER, MANAGER, ADMIN, SUPER_ADMIN")
    @Schema(
        description = "New role for the user",
        example = "ADMIN",
        required = true,
        allowableValues = { "GUEST", "USER", "MANAGER", "ADMIN", "SUPER_ADMIN" }
    )
    private String role;

    @Schema(description = "Reason for role change", example = "Promoted to team leader")
    private String reason;

    // Getters and Setters
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "UpdateRoleRequest{" + "role='" + role + "', reason='" + reason + "'}";
    }
}
