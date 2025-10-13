package com.nextjstemplate.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * DTO for user sign-out request.
 */
@Schema(description = "Request body for user sign-out")
public class SignOutRequest implements Serializable {

    @NotBlank(message = "Session ID is required")
    @Schema(description = "Clerk session ID to revoke", example = "sess_2abc123def456", required = true)
    private String sessionId;

    // Getters and Setters
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "SignOutRequest{sessionId='" + sessionId + "'}";
    }
}
