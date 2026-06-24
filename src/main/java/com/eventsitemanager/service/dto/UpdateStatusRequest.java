package com.eventsitemanager.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * DTO for admin status update request.
 */
@Schema(description = "Request body for updating user status")
public class UpdateStatusRequest implements Serializable {

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "ACTIVE|SUSPENDED|DELETED|PENDING", message = "Status must be one of: ACTIVE, SUSPENDED, DELETED, PENDING")
    @Schema(
        description = "New status for the user",
        example = "SUSPENDED",
        required = true,
        allowableValues = { "ACTIVE", "SUSPENDED", "DELETED", "PENDING" }
    )
    private String status;

    @Schema(description = "Reason for status change", example = "Violated terms of service")
    private String reason;

    @Schema(description = "Whether to revoke all active sessions", example = "true")
    private Boolean revokeSessions = true;

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Boolean getRevokeSessions() {
        return revokeSessions;
    }

    public void setRevokeSessions(Boolean revokeSessions) {
        this.revokeSessions = revokeSessions;
    }

    @Override
    public String toString() {
        return "UpdateStatusRequest{" + "status='" + status + "', reason='" + reason + "', revokeSessions=" + revokeSessions + '}';
    }
}
